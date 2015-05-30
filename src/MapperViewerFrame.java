
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;
import java.util.logging.Logger;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JToolBar;
import javax.swing.filechooser.FileNameExtensionFilter;

import ssr.RTMHelper;
import ssr.logger.ui.LoggerView;
import ssr.nameservice.ui.PortConnectionDialog;
import ssr.nameservice.ui.RTSTreeNode;
import ssr.nameservice.ui.RTSystemTreeView;
import RTC.PortService;
import application.CameraViewPanel;

@SuppressWarnings("serial")
public class MapperViewerFrame extends JFrame {

	private MapPanel mapPanel;

	private JMenuItem startMenu;

	private JMenuItem stopMenu;

	private JMenu fileMenu;

	private JMenu mapMenu;

	private JSplitPane vSplitPaneSmall;

	private JSplitPane vSplitPane;

	private JSplitPane hSplitPane;

	public CameraViewPanel cameraViewPanel;

	private RTSystemTreeView systemTreeView;

	private Logger logger;

	private StatusBar statusBar;

	private Application app;

	private JMenu pathMenu;

	private JMenu viewMenu;

	private JMenu helpMenu;

	private JMenu logMenu;

	private JMenu controlMenu;

	public MapperViewerFrame(Application app) {
		super("Navigation Manager(" + app.getVersion() + ")");
		logger = Logger.getLogger("MapperViewer");

		this.app = app;

		initializePresentation();
		setVisible(true);
	}

	private void initializePresentation() {

		int width = 800;
		int height = 600;
		mapPanel = new MapPanel(this.app);
		cameraViewPanel = new CameraViewPanel();

		systemTreeView = new RTSystemTreeView();

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

		add(hSplitPane, BorderLayout.CENTER);

		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				onExit();
			}
		});

		setupToolbar();

		setupMenu();

		statusBar = new StatusBar("Ready");
		this.add(BorderLayout.SOUTH, statusBar);

		setSize(new Dimension(width, height));
	}

	private void setupToolbar() {
		JToolBar toolBar = new JToolBar();
		this.add(toolBar, BorderLayout.NORTH);
		JButton startButton = new JButton(new AbstractAction("Start Mapping") {

			@Override
			public void actionPerformed(ActionEvent e) {
				onStartMapping();
			}

		});
		toolBar.add(startButton);
		JButton stopButton = new JButton(new AbstractAction("Stop Mapping") {

			@Override
			public void actionPerformed(ActionEvent e) {
				onStopMapping();
			}

		});
		toolBar.add(stopButton);
		JButton saveAsButton = new JButton(new AbstractAction("Save Map") {

			@Override
			public void actionPerformed(ActionEvent e) {
				onSaveMapAs();
			}

		});
		toolBar.add(saveAsButton);
		toolBar.add(new JToolBar.Separator());

		JButton planButton = new JButton(new AbstractAction("Plan Path") {

			@Override
			public void actionPerformed(ActionEvent e) {
				onPlan();
			}

		});
		toolBar.add(planButton);
		JButton savePathButton = new JButton(new AbstractAction("Save Path") {

			@Override
			public void actionPerformed(ActionEvent e) {
				onSavePath();
			}

		});
		toolBar.add(savePathButton);
		JButton followButton = new JButton(new AbstractAction("Follow") {

			@Override
			public void actionPerformed(ActionEvent e) {
				onFollow();
			}

		});
		toolBar.add(followButton);
		toolBar.add(new JToolBar.Separator());
		JButton zoomInButton = new JButton(new AbstractAction("Zoom In") {

			@Override
			public void actionPerformed(ActionEvent e) {
				onZoomIn();
			}

		});
		toolBar.add(zoomInButton);
		JButton zoomOutButton = new JButton(new AbstractAction("Zoom Out") {

			@Override
			public void actionPerformed(ActionEvent e) {
				onZoomOut();
			}

		});
		toolBar.add(zoomOutButton);
	}

	private void setupMenu() {
		JMenuBar menuBar = new JMenuBar();
		this.setJMenuBar(menuBar);
		this.fileMenu = new JMenu("File");
		menuBar.add(fileMenu);

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
				onStopMapping();
			}
		});
		mapMenu.add(stopMenu);
		JMenuItem saveAsMenu = new JMenuItem(new AbstractAction(
				"Save Map As...") {

			@Override
			public void actionPerformed(ActionEvent e) {
				onSaveMapAs();
			}

		});
		mapMenu.add(saveAsMenu);

		this.pathMenu = new JMenu("Path");
		menuBar.add(pathMenu);
		JMenuItem planPathMenu = new JMenuItem(new AbstractAction("Plan Path") {

			@Override
			public void actionPerformed(ActionEvent e) {
				onPlan();
			}

		});
		pathMenu.add(planPathMenu);
		JMenuItem savePathMenu = new JMenuItem(new AbstractAction(
				"Save Path As ...") {

			@Override
			public void actionPerformed(ActionEvent e) {
				onSavePath();
			}

		});
		pathMenu.add(savePathMenu);
		JMenuItem followPathMenu = new JMenuItem(new AbstractAction(
				"Follow Path") {

			@Override
			public void actionPerformed(ActionEvent e) {
				onFollow();
			}

		});
		pathMenu.add(followPathMenu);

		this.viewMenu = new JMenu("View");
		menuBar.add(viewMenu);

		this.logMenu = new JMenu("Log");
		menuBar.add(logMenu);

		this.controlMenu = new JMenu("Control");
		menuBar.add(controlMenu);
		JMenuItem startControlMenu = new JMenuItem(new AbstractAction(
				"Start Control") {

			@Override
			public void actionPerformed(ActionEvent e) {
				onStartControl();
			}

		});
		controlMenu.add(startControlMenu);
		JMenuItem stopControlMenu = new JMenuItem(new AbstractAction(
				"Stop Control") {

			@Override
			public void actionPerformed(ActionEvent e) {
				onStopControl();
			}

		});
		controlMenu.add(stopControlMenu);

		this.helpMenu = new JMenu("Help");
		menuBar.add(helpMenu);
	}

	public void setStatus(String text) {
		statusBar.setText(text);
	}

	private void onSaveMapAs() {
		JFileChooser fc = new JFileChooser();
		fc.setFileFilter(new FileNameExtensionFilter("*.png", "png"));

		if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
			String filename = fc.getSelectedFile().getAbsolutePath();
			logger.info("Saving File to " + filename);
			mapPanel.saveImage(filename);
		}
	}

	public synchronized void setImage(RTC.CameraImage image) {
		cameraViewPanel.setImage(image);
	}

	private void onStartControl() {
		PortService ps = app.rtc.m_targetVelocityOut.get_port_profile().port_ref;
		List<String> hostAddresses = systemTreeView.getHostAddresses();
		if(hostAddresses.size() == 0) {
			hostAddresses.add("localhost:2809");
		}
		PortConnectionDialog dialog = new PortConnectionDialog(
				null, ps, hostAddresses, RTMHelper.getDefaultDataPortConnectionProperties());
		dialog.setModal(true);
		dialog.setSize(new Dimension(400, 400));
		dialog.setVisible(true);
	}

	private void onStopControl() {
		PortService ps = app.rtc.m_targetVelocityOut.get_port_profile().port_ref;
		ps.disconnect_all();
	}

	private void onExit() {
		System.exit(0);
	}

	private void onStartMapping() {
		app.startMapping();
	}

	private void onStopMapping() {
		app.stopMapping();
	}

	private void onPlan() {
		app.planPath();
	}

	private void onSavePath() {
		app.savePath();
	}

	private void onFollow() {
		app.follow();
	}

	private void onZoomIn() {
		mapPanel.setZoomFactor(mapPanel.getZoomFactor() * 2.0f);
	}

	private void onZoomOut() {
		float zf = mapPanel.getZoomFactor() / 2.0f;
		mapPanel.setZoomFactor(zf);
	}

	public class StatusBar extends JLabel {

		public StatusBar(String title) {
			super(title);
		}
	}
}
