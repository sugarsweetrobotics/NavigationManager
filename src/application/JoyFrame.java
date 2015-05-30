package application;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;

@SuppressWarnings("serial")
public class JoyFrame extends JFrame {

	private int state = DEF;

	private JoyPanelEx jp;
	private MutePanel jm;
	private SpeedPanel sp;

	final public static int UP = 0;
	final public static int DOWN = 1;
	final public static int RIGHT = 2;
	final public static int LEFT = 3;
	final public static int DEF = 4;

	final public double vx = 0;
	final public double va = 0;

	private JPanel controlPanel;

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

	public class JoyPanelEx extends JPanel {

		private int len = 0;

		private Point point;
		private Object pointMutex = new Object();

		public JoyPanelEx() {
			MouseAdapter adapter = new MouseAdapter() {

				@Override
				public void mousePressed(MouseEvent e) {

					synchronized (pointMutex) {
						point = e.getPoint();
					}
					repaint();

				}

				@Override
				public void mouseDragged(MouseEvent e) {

					synchronized (pointMutex) {
						point = e.getPoint();
					}
					repaint();
				}

				@Override
				public void mouseReleased(MouseEvent e) {

					synchronized (pointMutex) {
						point = null;
					}
					repaint();
				}

			};

			this.addMouseListener(adapter);
			this.addMouseMotionListener(adapter);
		}

		public void updateLength() {
			int width = this.getWidth();
			int height = this.getHeight();
			len = width < height ? width : height;
		}

		@Override
		public void paintComponent(Graphics g) {
			Graphics2D g2d = (Graphics2D) g;
			updateLength();
			g2d.setColor(Color.white);
			g2d.fillRect(0, 0, len, len);

			drawAxis(g2d);
			drawPoint(g2d);
		}

		public void drawAxis(Graphics2D g2d) {
			g2d.setColor(Color.black);
			g2d.drawLine(0, len / 2, len, len / 2);
			g2d.drawLine(len / 2, 0, len / 2, len);
		}

		public void drawPoint(Graphics2D g2d) {
			Point drawPoint = null;
			synchronized (pointMutex) {
				drawPoint = point;
			}
			if (drawPoint == null) {
				drawPoint = new Point(len / 2, len / 2);
			}
			int w = 20;
			g2d.setColor(Color.black);
			g2d.drawLine(len / 2, len / 2, drawPoint.x, drawPoint.y);
			g2d.setColor(Color.black);
			g2d.drawOval(drawPoint.x - w / 2, drawPoint.y - w / 2, w, w);
			g2d.setColor(Color.yellow);
			g2d.fillOval(drawPoint.x - w / 2, drawPoint.y - w / 2, w, w);
		}

		public double getDY() {
			synchronized (pointMutex) {
				if (point == null)
					return 0;
				return -((double) point.x - len / 2) / (len / 2);
			}
		}

		public double getDX() {

			synchronized (pointMutex) {
				if (point == null)
					return 0;
				return -((double) point.y - len / 2) / (len / 2);
			}
		}
	}

	public double getRotationVelocity() {
		return sp.js1.getValue() / 100.0 * 5;
	}

	public double getTranslationVelocity() {
		return sp.js0.getValue() / 100.0 * 2;
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
			gbc.weightx = 1.0;
			JLabel l0 = new JLabel("Velocity X");
			layout.setConstraints(l0, gbc);
			add(l0);

			this.js0 = new JSlider(0, 100);
			gbc.gridx = 1;
			gbc.gridwidth = 2;
			gbc.weightx = 2.0;
			layout.setConstraints(js0, gbc);
			add(js0);

			JLabel l1 = new JLabel("Rotation ");
			gbc.gridx = 0;
			gbc.gridy = 2;
			gbc.gridwidth = 1;
			gbc.gridheight = 1;
			gbc.weightx = 1.0;
			layout.setConstraints(l1, gbc);
			add(l1);

			this.js1 = new JSlider(0, 100);
			gbc.gridx = 1;
			gbc.gridwidth = 2;
			gbc.weightx = 2.0;
			layout.setConstraints(js1, gbc);
			add(js1);

		}

	}

	public class MutePanel extends JPanel {

		private JCheckBox jr;
		
		public MutePanel() {
			super();

			GridBagLayout layout = new GridBagLayout();
			setLayout(layout);

			GridBagConstraints gbc = new GridBagConstraints();
			JLabel l2 = new JLabel("Mute Velocity Output ");
			gbc.gridx = 0;
			gbc.gridy = 0;
			gbc.gridwidth = 2;
			gbc.gridheight = 1;
			gbc.weightx = 2;
			layout.setConstraints(l2, gbc);
			add(l2);

			this.jr = new JCheckBox();
			gbc.gridx = 3;
			gbc.gridwidth = 1;
			gbc.gridheight = 1;
			gbc.weightx = 1;
			layout.setConstraints(jr, gbc);
			add(jr);
		}
		
		boolean isMute() {
			return jr.isSelected();
		}
	}

	public JoyFrame() {
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		initPresentation();
		setLocation(740, 0);
		setSize(300, 400);
	}

	public void initPresentation() {
		

		BorderLayout layout = new BorderLayout();
		setLayout(layout);
		this.jp = new JoyPanelEx();

		getContentPane().add(jp, BorderLayout.CENTER);
		
		///getContentPane().add(jm, BorderLayout.NORTH);
		
		
		this.controlPanel = new JPanel();
		controlPanel.setLayout(new GridLayout(2,1));
		this.sp = new SpeedPanel();
		this.jm = new MutePanel();
		controlPanel.add(jm);
		controlPanel.add(sp);
		getContentPane().add(controlPanel, BorderLayout.SOUTH);
	}

	public static void main(String[] s) {
		new JoyFrame().setVisible(true);

	}

	public double getDX() {
		return jp.getDX();
	}

	public double getDY() {
		return jp.getDY();
	}
	
	
	public boolean isMute() {
		return jm.isMute();
	}

}
