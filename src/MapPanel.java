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

	BufferedImage image;
	
	BufferedImage mapImage;
	
	RTC.Pose2D robotPose;
	
	RTC.RangeData rangeData;
	
	RTC.Path2D path;
	
	OGMap map;
	public MapPanel() {
		super();
		//image = new BufferedImage(w, h, BufferedImage.TYPE_BYTE_GRAY);
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
	
	public double getGoalX() {return goal.x; }
	public double getGoalY() {return goal.y; }
	public double getGoalTh() {return goal.th; }
	
	
	private void onPanelClicked(MouseEvent e) {
		Point p = e.getPoint();
		System.out.println("p = " + p.x + ", " + p.y);
		if (mapImage != null) {
			double rx = map.config.xScale;
			double ry = map.config.yScale;
			int ox = -(int)(map.config.origin.position.x / rx);
			int oy = -(int)(map.config.origin.position.y / ry);
			
			double goal_x = (p.x - ox) * rx;
			double goal_y = -(p.y - oy) * ry;
			double goal_th = 0;
			System.out.println("goal = " + goal_x + ", " + goal_y);
			goal = new Goal(goal_x, goal_y, goal_th);
		}
	}
	
	public void setMap(OGMap map) {
		this.map = map;
		if (map == null) {
			int h = image.getHeight();
			int w = image.getWidth();
			for(int i = 0;i < h;i++) {
				for(int j = 0;j < w;j++) {
					int rgb = 0xFF0000;
					image.setRGB(j, i, rgb);
				}
			}			
			return ;
		}
		
		int w = map.config.width;
		int h = map.config.height;
		if (image == null) {
			image = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
			mapImage = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		}
		else if (image.getWidth() != w || image.getHeight() != h) {
			image = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
			mapImage = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		}
		for(int i = 0;i < h;i++) {
			for(int j = 0;j < w;j++) {
				int r = 0;
				r |= (0x0FF & map.map.cells[(i)*w+(j)]);
				Color c = new Color(r/255.0f, r/255.0f, r/255.0f);
				int rgb = c.getRGB();// /* (r << 24) |*/ (r << 16) | (r << 8) | r;
				
				mapImage.setRGB(i, (h-1-j), rgb);
			}
		}
		//mapImage.copyData(mapImage.getRaster());
		
		if(robotPose == null) {
			robotPose = new Pose2D(new Point2D(0,0), 0);
		}
	}
	
	
	@Override
	public void repaint() {//Graphics g) {
		Graphics2D g2d = (Graphics2D)getGraphics();
		if(mapImage != null) {
			mapImage.copyData(image.getRaster());
			Graphics2D g2d2 = (Graphics2D)image.getGraphics();
			drawGoal(g2d2);
			drawAxis(g2d2, image.getWidth(), image.getHeight());
			drawPath(g2d2);
			drawRobot(g2d2);

			
			
			g2d.drawImage(image, 0, 0, this);
		}
		
		
	}
	
	
	private void drawRobot(Graphics2D g2d) {
		
		if (robotPose != null) {
			double rx = map.config.xScale;
			double ry = map.config.yScale;
			int ox = (int)(map.config.origin.position.x / rx);
			int oy = (int)(map.config.origin.position.y / ry);
			double oth = map.config.origin.heading;
			
			
			double x = robotPose.position.x;
			double y = robotPose.position.y;
			double th = robotPose.heading;
			
			double x2 =  x * Math.cos(oth) + y * Math.sin(oth);
			double y2 =  x * Math.sin(oth) - y * Math.cos(oth);
			
			
			double xd = -ox + x2 / rx;
			double yd = -oy + y2 / ry;
			
			
			int[] xpoints = {0, 7, 7, -7, -7};
			int[] ypoints = {-7, 0, 7, 7, 0};
			Polygon p = new Polygon(xpoints, ypoints, 5);
			
			g2d.fill(p);
			
			g2d.translate(xd, yd);
			//g2d2.translate(yd, xd);
			g2d.rotate(-th+Math.PI/2);
			
			Color oldColor = g2d.getColor();
			g2d.setColor(Color.orange);
			g2d.fill(p);
			

			g2d.setColor(Color.yellow);
			drawRange(g2d);

			g2d.setColor(oldColor);
		}
	}
	
	private void drawAxis(Graphics2D g2d2, int width, int height) {
		
		double rx = map.config.xScale;
		double ry = map.config.yScale;
		int ox = -(int)(map.config.origin.position.x / rx);
		int oy = -(int)(map.config.origin.position.y / ry);
		
		
		Color oc = g2d2.getColor();
		g2d2.setColor(Color.red);
		g2d2.setStroke(new BasicStroke(1));
		g2d2.drawLine(0, oy, width, oy);
		g2d2.setColor(Color.green);
		g2d2.drawLine(ox, 0, ox, height);
		g2d2.setColor(oc);
	}
	
	private void drawGoal(Graphics2D g2d2) {
		if (goal == null) {
			return;
		}
		double rx = map.config.xScale;
		double ry = map.config.yScale;
		int ox = -(int)(map.config.origin.position.x / rx);
		int oy = -(int)(map.config.origin.position.y / ry);
		int size = 5; // px
		
		int dx = (int)(ox + goal.x/rx);
		int dy = (int)(oy - goal.y/ry);
		
		Color oc = g2d2.getColor();
		g2d2.setColor(Color.magenta);
		g2d2.setStroke(new BasicStroke(2));
		g2d2.drawLine(dx - size, dy-size, dx + size, dy+size);
		g2d2.drawLine(dx + size, dy-size, dx - size, dy+size);
		g2d2.setColor(oc);
	}
	
	private void drawPath(Graphics2D g2d) {
		Color oc = g2d.getColor();
		g2d.setColor(Color.cyan);
		
		if (path != null) {
			double rx = map.config.xScale;
			double ry = map.config.yScale;
			int ox = -(int)(map.config.origin.position.x / rx);
			int oy = -(int)(map.config.origin.position.y / ry);
			
			Point old_p = null;
			for(Waypoint2D w : path.waypoints){
				double x = w.target.position.x;
				double y = w.target.position.y;
				int dx = (int)(ox + x/rx);
				int dy = (int)(oy - y/ry);
				Point p = new Point(dx, dy);
				if (old_p != null) {
					g2d.drawLine(old_p.x, old_p.y, p.x, p.y);
				}
				old_p = p;
			}
		}
		g2d.setColor(oc);
	}
	
	private void drawRange(Graphics2D g2d2) {
		if(rangeData != null) {
			int skip = 5;
			double rangeTh = rangeData.config.minAngle + Math.PI/2;
			double step = rangeData.config.angularRes * skip;
			double ofx = rangeData.geometry.geometry.pose.position.x;
			double ofy = rangeData.geometry.geometry.pose.position.y;
			double rrx = map.config.xScale;
			double rry = map.config.yScale;
			
			for(int i = 0;i < rangeData.ranges.length;i+=skip) {
				double distance = rangeData.ranges[i];
				
				double px = distance * Math.cos(-rangeTh) + ofx;
				double py = distance * Math.sin(-rangeTh) + ofy;
				
				double pyd = py / rry;
				double pxd = px / rrx;
				g2d2.fill(new Rectangle2D.Double(pyd-1, pxd-1, 2, 2));
				rangeTh += step;
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
	
	public Path2D getPath2D(){
		return this.path;
	}
}
