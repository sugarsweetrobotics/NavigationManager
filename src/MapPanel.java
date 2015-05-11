import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import RTC.OGMap;
import RTC.Path2D;
import RTC.Point2D;
import RTC.Pose2D;
import RTC.RangeData;
import RTC.Waypoint2D;

@SuppressWarnings("serial")
public class MapPanel extends JPanel {

	BufferedImage bufferImage;

	RTC.Pose2D robotPose;

	RTC.RangeData rangeData;

	RTC.Path2D path;

	private MapImageHolder mapImageHolder;

	public MapPanel() {
		super();
		// image = new BufferedImage(w, h, BufferedImage.TYPE_BYTE_GRAY);
		this.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				onPanelClicked(e);
			}

		});
	}

	class Goal {

		public Goal(double x, double y, double th) {
			this.x = x;
			this.y = y;
			this.th = th;
		}

		public double x;
		public double y;
		public double th;
	};

	Goal goal;

	public double getGoalX() {
		return goal.x;
	}

	public double getGoalY() {
		return goal.y;
	}

	public double getGoalTh() {
		return goal.th;
	}

	private void onPanelClicked(MouseEvent e) {
		if (mapImageHolder != null) {
			Point2D p = mapImageHolder.pixelToPosition(e.getPoint());
			goal = new Goal(p.x, p.y, 0);
		}
	}

	public void setMap(OGMap map) {
		this.mapImageHolder = new MapImageHolder(map);
		this.setSize(
				mapImageHolder.getPixelWidth(), mapImageHolder.getPixelHeight());
		this.bufferImage = new BufferedImage(mapImageHolder.getPixelWidth(),
				mapImageHolder.getPixelHeight(), BufferedImage.TYPE_INT_ARGB);
		if (robotPose == null) {
			robotPose = new Pose2D(new Point2D(0, 0), 0);
		}
	}

	@Override
	public void repaint() {// Graphics g) {
		Graphics2D g2d = (Graphics2D) getGraphics();
		if(g2d != null) {
			if (bufferImage != null) {
				mapImageHolder.getImage().copyData(bufferImage.getRaster());
				Graphics2D g2d2 = (Graphics2D) bufferImage.getGraphics();
				drawGoal(g2d2);
				drawAxis(g2d2);
				drawPath(g2d2);
				drawRobot(g2d2);
				g2d.drawImage(bufferImage, 0, 0, this);
			} else {
				g2d.drawString("No Map Loaded", 10, 20);
			}
		}
	}

	private void drawRobot(Graphics2D g2d) {

		if (robotPose != null) {
			double d = 0.3;
			double[] body_polygon_xs = { d, 0, -d, -d, 0 };
			double[] body_polygon_ys = { 0, d, d, -d, -d };
			int[] body_xs = new int[body_polygon_xs.length];
			int[] body_ys = new int[body_polygon_xs.length];

			Point[] polygonPoints = new Point[body_polygon_xs.length];
			for (int i = 0; i < body_polygon_xs.length; i++) {
				double cos = Math.cos(robotPose.heading);
				double sin = Math.sin(robotPose.heading);
				double rotated_x = body_polygon_xs[i] * cos
						+ body_polygon_ys[i] * sin;
				double rotated_y = body_polygon_xs[i] * sin
						- body_polygon_ys[i] * cos;
				polygonPoints[i] = mapImageHolder.positionToPixel(
						robotPose.position.x + rotated_x, robotPose.position.y
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
		if (goal != null) {
			Point goalPoint = mapImageHolder.positionToPixel(goal.x, goal.y);
			Color oc = g2d2.getColor();
			g2d2.setColor(Color.magenta);
			g2d2.setStroke(new BasicStroke(2));
			int size = 5;
			g2d2.drawLine(goalPoint.x - size, goalPoint.y - size, goalPoint.x
					+ size, goalPoint.y + size);
			g2d2.drawLine(goalPoint.x + size, goalPoint.y - size, goalPoint.x
					- size, goalPoint.y + size);
			g2d2.setColor(oc);
		}
	}

	private void drawPath(Graphics2D g2d) {
		Color oc = g2d.getColor();
		g2d.setColor(Color.cyan);

		if (path != null) {
			Point old_p = null;
			for (Waypoint2D w : path.waypoints) {
				Point p = mapImageHolder.positionToPixel(w.target.position);
				if (old_p != null) {
					g2d.drawLine(old_p.x, old_p.y, p.x, p.y);
				}
				old_p = p;
			}
		}
		g2d.setColor(oc);
	}

	private void drawRange(Graphics2D g2d2) {
		if (rangeData != null) {
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

				double cos = Math.cos(robotPose.heading);
				double sin = Math.sin(robotPose.heading);

				double x_in_map = x_in_robot * cos + y_in_robot * sin
						+ robotPose.position.x;
				double y_in_map = x_in_robot * sin - y_in_robot * cos
						+ robotPose.position.y;

				Point point = mapImageHolder
						.positionToPixel(x_in_map, y_in_map);

				g2d2.fill(new Rectangle2D.Double(point.x - 1, point.y - 1, 2, 2));
				rangeTh -= step;
			}
		}
	}

	public void setRobotPose(Pose2D pose) {
		robotPose = pose;
	}

	public void setRangeData(RangeData range) {
		rangeData = range;
	}

	public void setPath2D(Path2D path) {
		this.path = path;
	}

	public Path2D getPath2D() {
		return this.path;
	}

	public void saveImage(String filename) {
		mapImageHolder.saveImage(filename);
	}
}
