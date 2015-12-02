import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Logger;

import javax.swing.JFileChooser;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;

import RTC.MAPPER_STATE;
import RTC.OGMap;
import RTC.Path2D;
import RTC.PathPlanParameter;
import RTC.TimedPose2D;
import RTC.Velocity2D;
import application.DataContainer;
import application.NavigationLogger;
import application.PathUtil;
import application.VirtualJoystickContainer;

public class Application implements Runnable {

	public MapperViewerFrame view;

	public NavigationManagerImpl rtc;

	public DataContainer dataContainer;

	private NavigationLogger logger;

	private Thread applicationRoutine;

	private MAPPER_STATE mapper_state;

	private boolean endFlag = false;

	public VirtualJoystickContainer joystickContainer;

	public Application(NavigationManagerImpl rtc) {
		this.rtc = rtc;

		logger = new NavigationLogger();
		applicationRoutine = new Thread(this);
		dataContainer = new DataContainer();
		view = new MapperViewerFrame(this);
		joystickContainer = new VirtualJoystickContainer();
	}

	public void activate() {
		startRoutine();
		view.setStatus("Active");
		view.setTitle(MapperViewerFrame.getTitleStr(this, "ACTIVE"));
	}

	private void startRoutine() {
		endFlag = false;
		applicationRoutine.start();
	}

	public void deactivate() {
		stopRoutine();
		view.setStatus("Inactive");
		view.setTitle(MapperViewerFrame.getTitleStr(this, "INACTIVE"));
	}

	private void stopRoutine() {
		endFlag = true;
		try {
			applicationRoutine.join();
		} catch (InterruptedException e) {
			logger.warning("InterruptedException occured when stop routine.");
		}
	}

	public void onError() {
		view.setStatus("Error");
	}

	public void run() {
		while (!endFlag) {
			try {
				if (isAutoRepaint()) {

					SwingUtilities.invokeLater(new Runnable() {
						public void run() {
							view.repaint();
						}
					});

				}

				if (isAutoRequestMap()) {
					requestMap();
				}

				Thread.sleep(getInterval());
			} catch (InterruptedException e) {
				e.printStackTrace();
				logger.warning("Exception: " + e);
			} catch (Exception e) {
				e.printStackTrace();
				logger.warning("Exception: " + e);
			}
		}
	}

	private int getInterval() {
		return 200;
	}

	private boolean isAutoRequestMap() {
		return true;
	}

	private boolean isAutoRepaint() {
		return true;
	}

	private synchronized void requestMap() {
		OGMap map = rtc.requestMap();
		this.dataContainer.setMap(map);
		if (map != null) {
			logger.fine("Map is successfully acquired. Rendering....");
		}
	}

	public void startMapping() {
		mapper_state = rtc.requestState();
		if (!mapper_state.equals(MAPPER_STATE.MAPPER_MAPPING)) {
			if (rtc.startMapping()) {
				logger.info("start Mapping success.");
			} else {
				logger.warning("start Mapping failed.");
			}
		}
	}

	public void stopMapping() {
		mapper_state = rtc.requestState();
		if (mapper_state.equals(MAPPER_STATE.MAPPER_MAPPING)) {
			if (rtc.stopMapping()) {
				logger.info("stop Mapping success.");
			} else {
				logger.warning("stop Mapping failed.");
			}

		}
	}

	public void planPath() {
		logger.info("Start Planning....");
		PathPlanParameter param = new PathPlanParameter();
		param.targetPose = dataContainer.getGoal();// new RTC.Pose2D(new
													// RTC.Point2D(getGoalX(),
													// getGoalY()), 0);
		param.maxSpeed = new Velocity2D(1.0, 0, 1.0);
		param.distanceTolerance = 9999;
		param.headingTolerance = 9999;
		param.timeLimit = new RTC.Time(1000000, 0);

		RTC.Path2D path = rtc.planPath(param);
		if (path != null) {
			logger.info("Path is successfully acquired. Rendering....");
			dataContainer.setPath(path);
		}
	}

	public void savePath() {
		logger.info("Save Path Plan...");
		RTC.Path2D path = dataContainer.getPath();
		JFileChooser fc = new JFileChooser();
		fc.setFileFilter(new FileNameExtensionFilter("*.yaml", "yaml"));

		if (fc.showOpenDialog(this.view) == JFileChooser.APPROVE_OPTION) {
			File file = fc.getSelectedFile();
			if (!file.getName().endsWith("yaml")) {
				file = new File(file.getAbsolutePath() + ".yaml");
			}
			logger.info("Saving Path File to " + file.getAbsolutePath());
			PathUtil.savePath(path, file);
		}
	}

	public void loadPath() {
		logger.info("Load Path Plan...");
		JFileChooser fc = new JFileChooser();
		fc.setFileFilter(new FileNameExtensionFilter("*.yaml", "yaml"));
		if (fc.showOpenDialog(this.view) == JFileChooser.APPROVE_OPTION) {
			File file = fc.getSelectedFile();
			logger.info("Load Path From File " + file.getAbsolutePath());
			try {
				Path2D path = PathUtil.loadPath(file);
				this.dataContainer.setPath(path);
				dataContainer
						.setGoal(path.waypoints[path.waypoints.length - 1].target);
			} catch (IOException e) {
				logger.warning("Failed to load Path from "
						+ file.getAbsolutePath());
				e.printStackTrace();
			}
		}
	}

	public void follow() {
		logger.info("Start Following...");
		rtc.followPath(dataContainer.getPath());
		logger.info("Following End");
	}

	public String getVersion() {
		return this.rtc.get_component_profile().version;
	}

	private Object loggerMutex = new Object();
	private boolean logging;
	private File robotLogFile;
	private PrintWriter robotLogWriter;

	public void startRobotLogging() throws IOException {
		synchronized (loggerMutex) {
			logger.info("Save Robot Log To ...");
			JFileChooser fc = new JFileChooser();
			fc.setFileFilter(new FileNameExtensionFilter("*.csv", "csv"));
			if (fc.showOpenDialog(this.view) == JFileChooser.APPROVE_OPTION) {
				robotLogFile = fc.getSelectedFile();
				if (!robotLogFile.getName().endsWith("csv")) {
					robotLogFile = new File(robotLogFile.getAbsolutePath() + ".csv");
				}
				logger.info("Save Log to  " + robotLogFile.getAbsolutePath());
				robotLogWriter = new PrintWriter(new BufferedWriter(
						new FileWriter(robotLogFile)));
				logging = true;
			} else {
				robotLogFile = null;
			}
		}
	}

	public void stopRobotLogging() {
		synchronized (loggerMutex) {
			logging = false;
			robotLogWriter.close();
			robotLogFile = null;
			robotLogWriter = null;
		}
	}

	public void setRobotPose(TimedPose2D v) {
		dataContainer.setRobotPose(v);
		synchronized (loggerMutex) {
			if (logging) {
				robotLogWriter.write(v.tm.sec + ", " + v.tm.nsec + ", "
						+ v.data.position.x + ", " + v.data.position.y + ", "
						+ v.data.heading + "\n");
			}
		}
	}


	
	public void openLog() {
		logger.info("Open Robot Log To ...");
		JFileChooser fc = new JFileChooser();
		fc.setFileFilter(new FileNameExtensionFilter("*.csv", "csv"));
		if (fc.showOpenDialog(this.view) == JFileChooser.APPROVE_OPTION) {
			robotLogFile = fc.getSelectedFile();
			if (robotLogFile.exists()) {
				try {
					dataContainer.loadRobotLogFile(robotLogFile);
				} catch (IOException e) {
					logger.warning("Log Open Failed.");
					e.printStackTrace();
				}
			}
		} else {
			robotLogFile = null;
		}
	}

}
