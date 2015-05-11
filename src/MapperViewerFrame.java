import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.logging.Logger;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JToolBar;
import javax.swing.Timer;
import javax.swing.filechooser.FileNameExtensionFilter;

import ssr.logger.ui.LoggerView;
import ssr.nameservice.ui.RTSystemTreeView;
import RTC.MAPPER_STATE;
import RTC.OGMap;
import RTC.PathPlanParameter;
import RTC.Pose2D;
import RTC.RangeData;
import RTC.Velocity2D;


@SuppressWarnings("serial")
public class MapperViewerFrame extends JFrame {

	private MapPanel mapPanel;

	private MapperViewerImpl rtc;

	private JMenuItem startMenu;

	private JMenuItem stopMenu;

	private Timer timer;

	private JMenu fileMenu;

	private JMenu mapMenu;

	private OGMap map;

	private JMenu controlMenu;

	private JoyFrame joyFrame;

	private JMenuItem enableUpdateMenu;

	private JMenuItem openJoystick;

	private MAPPER_STATE mapper_state = MAPPER_STATE.MAPPER_UNKNOWN;

	private JSplitPane vSplitPaneSmall;
	
	private JSplitPane vSplitPane;
	
	private JSplitPane hSplitPane;
	
	public CameraViewPanel cameraViewPanel;
	
	private RTSystemTreeView systemTreeView;
	
	private Logger logger;
	
	public MapperViewerFrame(MapperViewerImpl rtc) {
		super("Mapper Viewer");

		int width = 800;
		int height = 600;
		
		mapPanel = new MapPanel();
		cameraViewPanel = new CameraViewPanel();
		
		systemTreeView  = new RTSystemTreeView();
		
		hSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		vSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		vSplitPaneSmall = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		
		
		hSplitPane.setDividerLocation(width / 3);
		hSplitPane.add(vSplitPaneSmall);
		hSplitPane.add(vSplitPane);
		
		vSplitPaneSmall.setDividerLocation(height / 2);
		vSplitPaneSmall.add((systemTreeView));
		vSplitPaneSmall.add(new JScrollPane(cameraViewPanel));

		vSplitPane.setDividerLocation(height / 3 * 2);
		vSplitPane.add(new JScrollPane(mapPanel));
		vSplitPane.add(new LoggerView("MapperViewer"));
		
		
		this.rtc = rtc;
		
		// setContentPane(mapPanel);
		add(hSplitPane, BorderLayout.CENTER);
		
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				onExit();
			}
		});

		JToolBar toolBar = new JToolBar();
		this.add(toolBar, BorderLayout.NORTH);
		JButton startButton = new JButton(new AbstractAction("Start Mapping") {

			@Override
			public void actionPerformed(ActionEvent e) {
				onStartMapping();
			}

		});
		toolBar.add(startButton);
		JButton saveAsButton = new JButton(new AbstractAction("Save") {

			@Override
			public void actionPerformed(ActionEvent e) {
				onSaveAs();
			}

		});
		toolBar.add(saveAsButton);
		JButton planButton = new JButton(new AbstractAction("Plan") {

			@Override
			public void actionPerformed(ActionEvent e) {
				onPlan();
			}

		});
		toolBar.add(planButton);
		JButton followButton = new JButton(new AbstractAction("Follow") {

			@Override
			public void actionPerformed(ActionEvent e) {
				onFollow();
			}

		});
		toolBar.add(followButton);

		JMenuBar menuBar = new JMenuBar();
		this.setJMenuBar(menuBar);
		this.fileMenu = new JMenu("File");
		menuBar.add(fileMenu);
		JMenuItem saveAsMenu = new JMenuItem(new AbstractAction("Save As") {

			@Override
			public void actionPerformed(ActionEvent e) {
				onSaveAs();
			}

		});
		fileMenu.add(saveAsMenu);

		JMenuItem exitMenu = new JMenuItem(new AbstractAction("Exit") {

			@Override
			public void actionPerformed(ActionEvent e) {
				onExit();
			}

		});
		fileMenu.add(exitMenu);

		this.mapMenu = new JMenu("Mapping");
		menuBar.add(mapMenu);
		this.startMenu = new JMenuItem(new AbstractAction("Start mapping") {

			@Override
			public void actionPerformed(ActionEvent e) {
				onStartMapping();
			}

		});
		mapMenu.add(startMenu);
		
	    this.stopMenu = new JMenuItem(new AbstractAction("Stop mapping") {
		  
		    @Override
		    public void actionPerformed(ActionEvent e) {
		    	onStop();
		    }
		  
		  
		  
		  });
	    mapMenu.add(stopMenu);
		 
		enableUpdateMenu = new JMenuItem(new AbstractAction(
				"Disable Auto Map Updating") {

			@Override
			public void actionPerformed(ActionEvent e) {
				onEnableUpdating();
			}

		});
		mapMenu.add(enableUpdateMenu);

		JMenuItem requestMenu = new JMenuItem(new AbstractAction("Request") {

			@Override
			public void actionPerformed(ActionEvent e) {
				onRequest();
			}

		});
		mapMenu.add(requestMenu);

		this.controlMenu = new JMenu("Control");
		getJMenuBar().add(controlMenu);
		openJoystick = new JMenuItem(new AbstractAction("Open Joystick") {

			@Override
			public void actionPerformed(ActionEvent e) {
				onOpenJoystick();
			}

		});
		controlMenu.add(openJoystick);

		setSize(new Dimension(width, height));
		setVisible(true);
		startTimer();
		startAutoRefresh();
		
		logger = Logger.getLogger("MapperViewer");
	}

	private double getGoalX() {
		return mapPanel.getGoalX();
	}

	private double getGoalY() {
		return mapPanel.getGoalY();
	}

	private void onSaveAs() {
		JFileChooser fc = new JFileChooser();
		fc.setFileFilter(new FileNameExtensionFilter("*.png", "png"));

		if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
			String filename = fc.getSelectedFile().getAbsolutePath();
			logger.info("Saving File to " + filename);
			mapPanel.saveImage(filename);
		}
	}

	private void onTimer() {
		if (!mapMenu.isPopupMenuVisible() && !fileMenu.isPopupMenuVisible()) {
			try {
				onRequest();
				repaintCamera();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

	private void startTimer() {
		if (timer == null) {
			this.timer = new Timer(this.rtc.getInterval(),
					new ActionListener() {

						@Override
						public void actionPerformed(ActionEvent e) {
							onTimer();
						}

					});
			timer.start();
		}
	}

	Timer refreshTimer;

	private void startAutoRefresh() {

		refreshTimer = new Timer(100, new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
					onRepaint();
			}

		});

		refreshTimer.start();
	}

	private synchronized void onRepaint() {
		repaint();
	}
	
	public synchronized void setImage(RTC.CameraImage image) {
		cameraViewPanel.setImage(image);
	}
	
	private void stopTimer() {
		if (timer != null) {
			timer.stop();
			timer = null;
		}
	}

	private void onExit() {
		stopTimer();
		System.exit(0);
	}

	private synchronized void onRequest() {
		
		this.map = rtc.requestMap();
		if (map != null) {
			logger.fine("Map is successfully acquired. Rendering....");
			mapPanel.setMap(map);
		}
		
		mapper_state = rtc.requestState();
		if (mapper_state.equals(MAPPER_STATE.MAPPER_MAPPING)) {
			this.startMenu.setText("Stop Mapping");
		} else {
			this.startMenu.setText("Start Mapping");
		}
	}

	private void onStartMapping() {
		if (mapper_state.equals(MAPPER_STATE.MAPPER_MAPPING)) {
			if (rtc.stopMapping()) {

			}

		} else {
			if (rtc.startMapping()) {

			}
		}
	}

	private void onPlan() {
		logger.info("Start Planning....");
		PathPlanParameter param = new PathPlanParameter();
		param.targetPose = new RTC.Pose2D(new RTC.Point2D(getGoalX(),
				getGoalY()), 0);
		param.maxSpeed = new Velocity2D(1.0, 0, 1.0);
		param.distanceTolerance = 0.1;
		param.headingTolerance = 0.5;
		param.timeLimit = new RTC.Time(1000000, 0);

		RTC.Path2D path = rtc.planPath(param);
		if (path != null) {
			logger.info("Path is successfully acquired. Rendering....");
			mapPanel.setPath2D(path);
		}
	}

	private void onFollow() {
		logger.info("Start Following...");
		rtc.followPath(mapPanel.getPath2D());
		logger.info("Following End");
	}
	
	
	private void onStop() {
		rtc.stopMapping();
	}
	
	private void onEnableUpdating() {
		if (this.timer != null) { // Timer is active
			stopTimer();
			this.enableUpdateMenu.setText("Enable Auto Map Updating");
		} else { // Timer is inactive
			startTimer();
			this.enableUpdateMenu.setText("Disable Auto Map Updating");
		}
	}

	@Override
	public void repaint() {
		mapPanel.repaint();
		cameraViewPanel.repaint();
	}
	
	public void repaintCamera() {
		cameraViewPanel.repaint();
	}

	public void setRobotPose(Pose2D pose) {
		if (map != null) {
			mapPanel.setRobotPose(pose);
		}
	}

	public void setRangeData(RangeData range) {
		if (map != null) {
			mapPanel.setRangeData(range);
		}
	}

	public void openJoystickFrame(boolean flag) {
		if (flag) {
			if (this.joyFrame == null) {
				this.joyFrame = new JoyFrame();
			}
		} else {
			if (this.joyFrame != null) {
				this.joyFrame.setVisible(false);
				this.joyFrame = null;
			}
		}
	}

	public void onOpenJoystick() {
		if (joyFrame == null) { // JoyFrame inactive
			this.openJoystickFrame(true);
			this.openJoystick.setText("Close Joystick");
		} else { // JoyFrame active
			this.openJoystickFrame(false);
			this.openJoystick.setText("Open Joystick");
		}
	}

	public boolean isJoystick() {
		return joyFrame != null;
	}

	public int getJoyState() {
		return joyFrame.getState();
	}

	public double getTranslationVelocity() {
		return joyFrame.getTranslationVelocity();
	}

	public double getRotationVelocity() {
		return joyFrame.getRotationVelocity();
	}

}