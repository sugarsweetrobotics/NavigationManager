

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;

import RTC.OGMap;
import RTC.Path2D;
import RTC.Point2D;
import RTC.Pose2D;
import RTC.RangeData;
import RTC.TimedPose2D;
import RTC.Waypoint2D;
import application.MapImageHolder;

@SuppressWarnings("serial")
public class MapPanel extends JComponent {

	private BufferedImage bufferImage;

	private MapImageHolder mapImageHolder;

	private float zoomFactor = 1.0f;
	private float pendingZoomFactor = 1.0f;
	
	private Application app;

	public float getZoomFactor() {
		return zoomFactor;
	}

	public void setZoomFactor(float v) {
		pendingZoomFactor = v;
	}

	public MapPanel(Application app) {
		super();
		this.app = app;
		this.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				onPanelClicked(e);
			}

		});
		this.setPreferredSize(new Dimension(800, 800));
		setSize(800, 800);
	}

	private void onPanelClicked(MouseEvent e) {
		if (mapImageHolder != null) {
			Point p_ = new Point((int) (e.getPoint().x / zoomFactor),
					(int) (e.getPoint().y / zoomFactor));
			Point2D p = mapImageHolder.pixelToPosition(p_);
			GoalPoseDialog d = new GoalPoseDialog(app.view, p.x, p.y, 0);
			if (d.doModal()) {
				app.dataContainer.setGoal(new Pose2D(p, d.getTh()));
			} 
		}
	}

	void reallocImage() {
		Dimension d_zoomed = new Dimension(
				(int) (mapImageHolder.getPixelWidth() * zoomFactor),
				(int) (mapImageHolder.getPixelHeight() * zoomFactor));

		Dimension d = d_zoomed;// new
								// Dimension((int)(mapImageHolder.getPixelWidth()),
		// (int)(mapImageHolder.getPixelHeight()));
		this.setSize(d_zoomed);
		this.setPreferredSize(d_zoomed);
		if (bufferImage == null) {
			this.bufferImage = new BufferedImage(d.width, d.height,
					BufferedImage.TYPE_INT_ARGB);
		} else if (bufferImage.getWidth() != d.width
				|| bufferImage.getHeight() != d.height) {
			this.bufferImage = new BufferedImage(d.width, d.height,
					BufferedImage.TYPE_INT_ARGB);
		}
	}

	synchronized public void setMap(OGMap map) {
		if(map == null) {
			bufferImage = null;
		} else {
			this.mapImageHolder = new MapImageHolder(map);
			reallocImage();
		}
	}

	
	int frameNo = 0;
	synchronized public void draw(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;// getGraphics();//mapCanvas.getGraphics();//
										// g;//
										// getGraphics();
		if (g2d != null) {
			OGMap map = app.dataContainer.getMap();
			setMap(map);
			if (bufferImage != null) {
				// mapImageHolder.getImage().copyData(bufferImage.getRaster());
				if (pendingZoomFactor != zoomFactor) {
					zoomFactor = pendingZoomFactor;
					reallocImage();
				}
				Graphics2D g2d2 = (Graphics2D) bufferImage.getGraphics();
				g2d2.drawImage(mapImageHolder.getImage(), new AffineTransform(
						zoomFactor, 0f, 0f, zoomFactor, 0f, 0f), this);
				g2d2.setTransform(new AffineTransform(zoomFactor, 0f, 0f,
						zoomFactor, 0f, 0f));
				drawGoal(g2d2);
				drawAxis(g2d2);
				drawPath(g2d2);
				drawRobot(g2d2);
				
				g2d2.drawString("Frame: " + frameNo, 10, 10);
				frameNo++;
				g2d.drawImage(bufferImage, 0, 0, this);
			} else {
				g2d.drawString("Frame: " + frameNo, 10, 10);
				frameNo++;
				g2d.drawString("No Map Loaded", 10, 20);
			}
		}
	}

	@Override
	public void paintComponent(Graphics g) {
		draw(g);
	}

	private void drawRobot(Graphics2D g2d) {
		TimedPose2D pose = app.dataContainer.getCurrentPose();
		if (pose == null) {
			return;
		}
		double d = 0.3;
		double[] body_polygon_xs = { d, 0, -d, -d, 0 };
		double[] body_polygon_ys = { 0, d, d, -d, -d };
		int[] body_xs = new int[body_polygon_xs.length];
		int[] body_ys = new int[body_polygon_xs.length];

		Point[] polygonPoints = new Point[body_polygon_xs.length];
		for (int i = 0; i < body_polygon_xs.length; i++) {
			double cos = Math.cos(pose.data.heading);
			double sin = Math.sin(pose.data.heading);
			double rotated_x = body_polygon_xs[i] * cos
					+ body_polygon_ys[i] * sin;
			double rotated_y = body_polygon_xs[i] * sin
					- body_polygon_ys[i] * cos;
			polygonPoints[i] = mapImageHolder.positionToPixel(
					pose.data.position.x + rotated_x, pose.data.position.y
							+ rotated_y);
			body_xs[i] = polygonPoints[i].x;
			body_ys[i] = polygonPoints[i].y;
		}

		Polygon p = new Polygon(body_xs, body_ys, body_xs.length);

		Color oldColor = g2d.getColor();
		g2d.setColor(Color.red);
		g2d.fill(p);

		g2d.setColor(Color.blue);
		drawRange(g2d);

		g2d.setColor(oldColor);
		
	}

	private void drawAxis(Graphics2D g2d2) {
		Point originPoint = mapImageHolder.positionToPixel(0, 0);
		Color oc = g2d2.getColor();
		g2d2.setColor(Color.red);
		g2d2.setStroke(new BasicStroke(1));
		g2d2.drawLine(0, originPoint.y, mapImageHolder.getPixelWidth(),
				originPoint.y);
		g2d2.setColor(Color.green);
		g2d2.drawLine(originPoint.x, 0, originPoint.x,
				mapImageHolder.getPixelHeight());
		g2d2.setColor(oc);
	}

	private void drawGoal(Graphics2D g2d2) {
		Pose2D goal = app.dataContainer.getGoal();
		if (goal == null) {
			return;
		}
		Point goalPoint = mapImageHolder.positionToPixel(goal.position.x,
				goal.position.y);
		Color oc = g2d2.getColor();
		g2d2.setColor(Color.magenta);
		g2d2.setStroke(new BasicStroke(2));
		int size = 10;
		int arrow_x = (int) (size * 2 * Math.cos(goal.heading));
		int arrow_y = (int) (size * 2 * Math.sin(goal.heading));
		g2d2.drawOval(goalPoint.x - size / 2, goalPoint.y - size / 2, size,
				size);
		g2d2.drawLine(goalPoint.x, goalPoint.y, goalPoint.x + arrow_x,
				goalPoint.y - arrow_y);
		g2d2.setColor(oc);
	
	}

	private void drawPath(Graphics2D g2d) {
		Path2D path = app.dataContainer.getPath();
		if (path == null) {
			return;
		}
		Color oc = g2d.getColor();
		g2d.setColor(Color.cyan);
		Point old_p = null;
		for (Waypoint2D w : path.waypoints) {
			Point p = mapImageHolder.positionToPixel(w.target.position);
			if (old_p != null) {
				g2d.drawLine(old_p.x, old_p.y, p.x, p.y);
			}
			old_p = p;
		}
		g2d.setColor(oc);
	}

	private void drawRange(Graphics2D g2d2) {
		RangeData rangeData = app.dataContainer.getRangeData();
		Pose2D pose = app.dataContainer.getCurrentPose().data;
		if (rangeData == null || pose == null) {
			return;
		}
		int skip = 5;
		double step = rangeData.config.angularRes * skip;
		double ofx = rangeData.geometry.geometry.pose.position.x;
		double ofy = rangeData.geometry.geometry.pose.position.y;
		double rangeTh = rangeData.config.maxAngle;

		for (int i = 0; i < rangeData.ranges.length; i += skip) {
			double distance = rangeData.ranges[i];
			double x_in_robot = distance * Math.cos(rangeTh) + ofx; // in
																	// robot
																	// coordinates
			double y_in_robot = distance * Math.sin(rangeTh) + ofy; // in
																	// robot
																	// coordinates

			double cos = Math.cos(pose.heading);
			double sin = Math.sin(pose.heading);

			double x_in_map = x_in_robot * cos + y_in_robot * sin
					+ pose.position.x;
			double y_in_map = x_in_robot * sin - y_in_robot * cos
					+ pose.position.y;

			Point point = mapImageHolder
					.positionToPixel(x_in_map, y_in_map);

			g2d2.fill(new Rectangle2D.Double(point.x - 1, point.y - 1, 2, 2));
			rangeTh -= step;
		
		}
	}

	public void saveImage(String filename) {
		mapImageHolder.saveImage(filename);
	}

}

@SuppressWarnings("serial")
class GoalPoseDialog extends JDialog {

	private JComponent panel;
	private JComponent buttonPanel;
	private JButton okButton;
	private JButton cancelButton;
	private boolean ok = false;
	private double x;
	private double y;
	private double th;

	public double getTh() {
		return th;
	}

	public GoalPoseDialog(Frame parent, double x, double y, double th) {
		super(parent);
		this.x = x;
		this.y = y;
		this.th = th;
		setTitle("Set Goal Pose");
		setLayout(new BorderLayout());
		panel = new PosePanel();
		add(BorderLayout.CENTER, panel);
		buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout(FlowLayout.TRAILING));
		add(BorderLayout.SOUTH, buttonPanel);
		okButton = new JButton(new AbstractAction("OK") {

			@Override
			public void actionPerformed(ActionEvent e) {
				ok = true;
				setVisible(false);
			}
		});
		cancelButton = new JButton(new AbstractAction("Cancel") {

			@Override
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
			}
		});
		buttonPanel.add(cancelButton);
		buttonPanel.add(okButton);
		pack();
	}

	class PosePanel extends JPanel {
		public PosePanel() {
			super();
			setSize(400, 400);
			setPreferredSize(new Dimension(400, 400));

			this.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					int width = 400;
					int height = 400;
					int dx = e.getPoint().x - width / 2;
					int dy = -(e.getPoint().y - height / 2);
					th = Math.atan2(dy, dx);
					repaint();
				}

			});
		}

		@Override
		public void paintComponent(Graphics g) {
			int width = 400;
			int height = 400;
			int padding = 80;
			g.setColor(Color.white);
			g.fillRect(0, 0, width, height);
			g.setColor(Color.black);
			g.drawLine(width / 2, 0, width / 2, height);
			g.drawLine(0, height / 2, width, height / 2);
			g.drawString("Click panel to specify the heading direction TH", 20, 20);
			g.drawString("X : " + x, 20, 40);
			g.drawString("Y : " + y, 20, 60);
			g.drawString("TH: " + th, 20, 80);
			g.setColor(Color.red);
			g.drawOval(padding, padding, width - padding * 2, height - padding
					* 2);
			g.drawLine(width / 2, height / 2, width / 2
					+ (int) ((width / 2 - padding) * Math.cos(th)), height / 2
					- (int) ((height / 2 - padding) * Math.sin(th)));
		}

	}

	public boolean doModal() {
		this.setModal(true);
		this.setVisible(true);
		return ok;
	}

}
