import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;


@SuppressWarnings("serial")
public class JoyFrame extends JFrame {

	private int state = DEF;

	private JoyPanel jp;

	private SpeedPanel sp;

	final public static int UP = 0;
	final public static int DOWN = 1;
	final public static int RIGHT = 2;
	final public static int LEFT = 3;
	final public static int DEF = 4;

	public int getState() {
		return state;
	}
	
	public void setState(int state) {
		this.state = state;
	}

	public class JoyPanel extends JPanel {

		public JoyPanel() {
			setLayout(new GridLayout(3, 3));

			this.add(new JLabel("Up:(0,1)"));
			this.add(new JButton(new AbstractAction("Up") {
				public void actionPerformed(ActionEvent arg0) {
					state = UP;
				}
			}));
			this.add(new JLabel("Right:(1,0)"));
			this.add(new JButton(new AbstractAction("Left") {
				public void actionPerformed(ActionEvent arg0) {
					state = LEFT;
				}
			}));
			this.add(new JButton(new AbstractAction("Default") {
				public void actionPerformed(ActionEvent arg0) {
					state = DEF;
				}
			}));
			this.add(new JButton(new AbstractAction("Right") {
				public void actionPerformed(ActionEvent arg0) {
					state = RIGHT;
				}
			}));
			this.add(new JLabel("Left:(-1,0)"));
			this.add(new JButton(new AbstractAction("Down") {
				public void actionPerformed(ActionEvent arg0) {
					state = DOWN;
				}
			}));
			this.add(new JLabel("Down:(0,-1)"));

		}
	}

	public double getRotationVelocity() {
		return sp.js1.getValue() / 100.0 * 1;
	}
	
	public double getTranslationVelocity() {
		return sp.js0.getValue() / 100.0 * 1;
	}

	public class SpeedPanel extends JPanel {
		
		private JSlider js0, js1;
		
		
		public SpeedPanel() {
			super();
			GridBagLayout layout = new GridBagLayout();
			setLayout(layout);

			GridBagConstraints gbc = new GridBagConstraints();
			gbc.gridx = 0;
			gbc.gridy = 0;
			gbc.gridwidth = 1;
			gbc.gridheight = 1;
			JLabel l0 = new JLabel("Velocity X");
			layout.setConstraints(l0, gbc);
			add(l0);
			
			this.js0 = new JSlider(0, 100);
			gbc.gridx = 1;
			gbc.gridwidth = 2;
			layout.setConstraints(js0, gbc);
			add(js0);
			
			JLabel l1 = new JLabel("Rotation ");
			gbc.gridx = 0;
			gbc.gridy = 2;
			gbc.gridwidth = 1;
			gbc.gridheight = 1;
			layout.setConstraints(l1, gbc);
			add(l1);
			
			this.js1 = new JSlider(0, 100);
			gbc.gridx = 1;
			gbc.gridwidth = 2;
			layout.setConstraints(js1, gbc);
			add(js1);
			
		}
		
	}
	
	public JoyFrame() {
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		initPresentation();
		setLocation(740, 0);
		setSize(300, 400);
		//frame.pack();
		setVisible(true);
	}

	public void initPresentation() {
		this.jp = new JoyPanel();
		this.sp = new SpeedPanel();
		BorderLayout layout = new BorderLayout();
		setLayout(layout);
		/*
		GridBagLayout layout = new GridBagLayout();
			
		setLayout(layout);
		
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 1;
		gbc.gridheight = 2;
		layout.setConstraints(jp, gbc);
		
	
		
		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.gridwidth = 1;
		gbc.gridheight = 2;
		

		layout.setConstraints(sp, gbc);
		
		*/
		getContentPane().add(jp, BorderLayout.CENTER);
		getContentPane().add(sp, BorderLayout.SOUTH);
	}

	public static void main(String[] s) {
		new JoyFrame();
	}

}
