import java.io.File;
import java.util.logging.Logger;

import javax.swing.JFileChooser;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;

import RTC.MAPPER_STATE;
import RTC.OGMap;
import RTC.PathPlanParameter;
import RTC.Velocity2D;
import application.DataContainer;
import application.PathUtil;
import application.VirtualJoystickContainer;

public class Application implements Runnable {

	public MapperViewerFrame view;

	public NavigationManagerImpl rtc;

	public DataContainer dataContainer;

	private Logger logger;

	private Thread applicationRoutine;

	private MAPPER_STATE mapper_state;

	private boolean endFlag = false;

	public VirtualJoystickContainer joystickContainer;

	public Application(NavigationManagerImpl rtc) {
		this.rtc = rtc;

		logger = Logger.getLogger("MapperViewer");
		applicationRoutine = new Thread(this);
		dataContainer = new DataContainer();
		view = new MapperViewerFrame(this);
		joystickContainer = new VirtualJoystickContainer();
	}

	public void activate() {
		startRoutine();
		view.setStatus("Active");
	}

	private void startRoutine() {
		endFlag = false;
		applicationRoutine.start();
	}

	public void deactivate() {
		stopRoutine();
		view.setStatus("Inactive");
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
		fc.setFileFilter(new FileNameExtensionFilter("*.png", "png"));

		if (fc.showOpenDialog(this.view) == JFileChooser.APPROVE_OPTION) {
			File file = fc.getSelectedFile();
			logger.info("Saving Path File to " + file.getAbsolutePath());
			PathUtil.savePath(path, file);
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

}
