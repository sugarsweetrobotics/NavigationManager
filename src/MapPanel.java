import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import RTC.OGMap;
import RTC.Pose2D;
import RTC.RangeData;


public class MapPanel extends JPanel {

	BufferedImage image;
	
	RTC.Pose2D robotPose;
	
	RTC.RangeData rangeData;
	
	OGMap map;
	public MapPanel() {
		super();
		int w = 640;
		int h = 640;
		//image = new BufferedImage(w, h, BufferedImage.TYPE_BYTE_GRAY);
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
		}
		else if (image.getWidth() != w || image.getHeight() != h) {
			image = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		}
		double rx = map.config.xScale;
		double ry = map.config.yScale;
		int ox = (int)(-map.config.origin.position.x / rx);
		int oy = (int)(-map.config.origin.position.y / ry);
		double oth = map.config.origin.heading;
		for(int i = 0;i < h;i++) {
			for(int j = 0;j < w;j++) {
				byte r = map.map.cells[(h-i-1)*w+(w-j-1)];
				int rgb = (r << 24) | (r << 16) | (r << 8) | r;
				
				image.setRGB(j, i, rgb);
			}
		}
	}
	
	
	@Override
	public void repaint() {//Graphics g) {
		Graphics2D g2d = (Graphics2D)getGraphics();
		if(image != null) {
			
			if (robotPose != null) {
				Graphics2D g2d2 = (Graphics2D)image.getGraphics();
				double rx = map.config.xScale;
				double ry = map.config.yScale;
				int ox = (int)(map.config.origin.position.x / rx);
				int oy = (int)(map.config.origin.position.y / ry);
				double oth = map.config.origin.heading;
				
				double x = robotPose.position.x;
				double y = robotPose.position.y;
				double th = robotPose.heading;
				
				double y2 = - x * Math.cos(oth) + y * Math.sin(oth);
				double x2 =  x * Math.sin(oth) + y * Math.cos(oth);
				
				
				double xd = ox - x2 / rx;
				double yd = oy + y2 / ry;
				
				int[] xpoints = {0, 7, 7, -7, -7};
				int[] ypoints = {-7, 0, 7, 7, 0};
				Polygon p = new Polygon(xpoints, ypoints, 5);
				
				g2d2.translate(xd, yd);
				g2d2.rotate(-th);
				
				Color oldColor = g2d2.getColor();
				g2d2.setColor(Color.red);
				g2d2.fill(p);
				
				if(rangeData != null) {
					int skip = 5;
					double rangeTh = rangeData.config.minAngle;
					double step = rangeData.config.angularRes * skip;
					double ofx = rangeData.geometry.geometry.pose.position.x;
					double ofy = rangeData.geometry.geometry.pose.position.y;
					
					for(int i = 0;i < rangeData.ranges.length;i+=skip) {
						double distance = rangeData.ranges[i];
						double px = distance * Math.cos(rangeTh) + ofx;
						double py = distance * Math.sin(rangeTh) + ofy;
						
						double pxd = -py / ry;
						double pyd = -px / rx;
						g2d2.fill(new Rectangle2D.Double(pxd-2, pyd-2, 4, 4));
						rangeTh += step;
					}
				}

				g2d2.setColor(oldColor);
			}
			g2d.drawImage(image, 0, 0, this);

		}
	}

	
	public void setRobotPose(Pose2D pose) {
		robotPose = pose;
	}

	public void setRangeData(RangeData range) {
		rangeData = range;
	}
}
