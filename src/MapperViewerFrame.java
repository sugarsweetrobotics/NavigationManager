import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.Timer;
import javax.swing.filechooser.FileNameExtensionFilter;

import RTC.OGMap;
import RTC.Pose2D;
import RTC.RangeData;


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

	public MapperViewerFrame(MapperViewerImpl rtc) {
		super("Mapper Viewer");
		
		this.rtc = rtc;
		mapPanel = new MapPanel();
		setContentPane(mapPanel);
		
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.addWindowListener(new WindowListener() {

			@Override
			public void windowOpened(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void windowClosing(WindowEvent e) {
				// TODO Auto-generated method stub
				onExit();
			}

			@Override
			public void windowClosed(WindowEvent e) {
				// TODO Auto-generated method stub
			}

			@Override
			public void windowIconified(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void windowDeiconified(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void windowActivated(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void windowDeactivated(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
			
		});
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
				onStart();
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
		JMenuItem enableUpdateMenu = new JMenuItem(new AbstractAction("EnableAutoUpdating") {

			@Override
			public void actionPerformed(ActionEvent e) {
				onEnableUpdating();
			}
			
		});
		mapMenu.add(enableUpdateMenu);
		JMenuItem disableUpdateMenu = new JMenuItem(new AbstractAction("DisableAutoUpdating") {

			@Override
			public void actionPerformed(ActionEvent e) {
				onDisableUpdating();
			}
			
		});
		mapMenu.add(disableUpdateMenu);
		JMenuItem requestMenu = new JMenuItem(new AbstractAction("Request") {

			@Override
			public void actionPerformed(ActionEvent e) {
				onRequest();
			}
			
		});
		mapMenu.add(requestMenu);
		
		
		this.controlMenu = new JMenu("Control");
		getJMenuBar().add(controlMenu);
		JMenuItem openJoystick = new JMenuItem(new AbstractAction("Open Joystick") {

			@Override
			public void actionPerformed(ActionEvent e) {
				onOpenJoystick();
			}
			
		});
		controlMenu.add(openJoystick);
		
		setSize(new Dimension(640, 480));
		setVisible(true);
	}
	
	private void onSaveAs() {
		JFileChooser fc = new JFileChooser();
		fc.setFileFilter(new FileNameExtensionFilter("*.png", "png"));    // (2)

		if(fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {    // (3), (4)
			String filename = fc.getSelectedFile().getAbsolutePath();
			if (!filename.endsWith(".png")) {
				filename = filename + ".png";
			}
			String fileContext = filename.substring(0, filename.length()-4);
		    System.out.println("You chose to open this file: " + fileContext);
		    File f = new File(filename);
		    try {
		    	double xresolution = mapPanel.map.config.xScale;
		    	double yresolution = mapPanel.map.config.yScale;
		    	double origin_x = mapPanel.map.config.origin.position.x;
		    	double origin_y = mapPanel.map.config.origin.position.y;
		    	double origin_th = mapPanel.map.config.origin.heading;
				ImageIO.write(mapPanel.mapImage,"png", f);
				File f2 = new File(fileContext + ".yaml");
				FileWriter fw = new FileWriter(f2);
				BufferedWriter bw = new BufferedWriter(fw);
				bw.write("# Resolution of Map. Length of 1 px in meter.");
				bw.write("\nresolution_x : " + xresolution);
				bw.write("\nresolution_y : " + yresolution);
				bw.write("\n# Pose of the Top-Left point in meter / radian");
				bw.write("\n# X-Axis is horizontally, left to right.");
				bw.write("\n# Y-Axis is vertically, bottom to top.");
				bw.write("\norigin_x : " + origin_x);
				bw.write("\norigin_y : " + origin_y);
				bw.write("\norigin_th : " + origin_th);
				bw.write("\nimage : " + f.getName());
				bw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	private void onExit() {
		System.exit(0);
	}
	
	private void onRequest() {
		this.map = rtc.requestMap();
		if(map!= null) {
			mapPanel.setMap(map);
		}
		repaint();
	}
	
	private void onStart() {
		if(rtc.startMapping()) {
			stopMenu.setEnabled(true);
		}
		
	}
	private void onStop() {
		rtc.stopMapping();
	}
	private void onEnableUpdating() {
		this.timer = new Timer(100, new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (!mapMenu.isPopupMenuVisible() && 
						!fileMenu.isPopupMenuVisible()) {
					onRequest();
				}
			}
			
		});
		timer.start();
	}
	
	private void onDisableUpdating() {
		//this.timer = new Timer();
		timer.stop();
	}
	
	@Override
	public void repaint() {
		mapPanel.repaint();
	}
	
	public void setRobotPose(Pose2D pose) {
		if(map != null) {
			mapPanel.setRobotPose(pose);
		}
	}
	
	public void setRangeData(RangeData range) {
		if(map != null) {
			mapPanel.setRangeData(range);
		}
	}
	
	public void onOpenJoystick() {
		this.joyFrame = new JoyFrame();
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
