package application;
import java.awt.Color;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.imageio.ImageIO;

import RTC.OGMap;
import RTC.Point2D;


public class MapImageHolder {

	private OGMap map;
	
	public OGMap getMap() {return map;}
	
	private BufferedImage image;
	
	private float zoomFactor = 1.0f;
	
	public MapImageHolder(OGMap map) {
		this.map = map;
	}
	
	public int getPixelWidth() {
		return (int)(map.config.width);
	}
	
	public int getPixelHeight() {
		return (int)(map.config.height);
	}
	

	public double getMapWidth() {
		return (map.config.width * map.config.xScale);
	}
	
	public double getMapHeight() {
		return (map.config.height * map.config.yScale);
	}

	public Point positionToPixel(Point2D point2d) {
		return positionToPixel(point2d.x, point2d.y);
	}
		
	public Point positionToPixel(double x, double y) {
		return new Point((int)(zoomFactor* (x + map.config.origin.position.x) / map.config.xScale),
		-(int)(zoomFactor* (y + map.config.origin.position.y) / map.config.yScale)); 
	}
	
	public Point2D pixelToPosition(int x, int y) {
		float x_ = x / zoomFactor;
		float y_ = y / zoomFactor;
		return new Point2D(x_ * map.config.xScale - map.config.origin.position.x,
				-y_ * map.config.yScale - map.config.origin.position.y);
	}
		
	public Point2D pixelToPosition(Point point) {
		return pixelToPosition(point.x, point.y);
	}
	
	public Point2D pixelToPosition(Point point, float zoomFactor) {
		setZoomFactor(zoomFactor);
		return pixelToPosition(point.x, point.y);
	}
	
	public BufferedImage getImage() {
		if(image == null) {
			int w = getPixelWidth();
			int h = getPixelHeight();
			image = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		
			for(int i = 0;i < h;i++) {
				for(int j = 0;j < w;j++) {
					int r = 0;
					r |= (0x0FF & map.map.cells[(i)*w+(j)]);
					Color c = new Color(r/255.0f, r/255.0f, r/255.0f);
					int rgb = c.getRGB();
					image.setRGB(j, i, rgb);
				}
			}
		}
		return image;
	}
	
	public void saveImage(String filename) {
		if (!filename.endsWith(".png")) {
			filename = filename + ".png";
		}
		String fileContext = filename.substring(0, filename.length() - 4);
		System.out.println("You chose to open this file: " + fileContext);
		File f = new File(filename);
		try {
			double xresolution = map.config.xScale;
			double yresolution = map.config.yScale;
			double origin_x = map.config.origin.position.x;
			double origin_y = map.config.origin.position.y;
			double origin_th = map.config.origin.heading;
			int row = map.map.row;
			int column = map.map.column;
			ImageIO.write(getImage(), "png", f);
			File f2 = new File(fileContext + ".yaml");
			FileWriter fw = new FileWriter(f2);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write("# Image File Name");
			bw.write("\nimage : " + f.getName());
			bw.write("\n# Configuration");
			bw.write("\nconfig : ");
			bw.write("\n# Resolution of Map. Length of 1 px in meter.");
			bw.write("\n  xScale : " + xresolution);
			bw.write("\n  yScale : " + yresolution);
			bw.write("\n# Pose from the Top-Left point in meter and radian");
			bw.write("\n# X-Axis is horizontally, left to right.");
			bw.write("\n# Y-Axis is vertically, bottom to top.");
			bw.write("\n  origin_x : " + origin_x);
			bw.write("\n  origin_y : " + origin_y);
			bw.write("\n  origin_th : " + origin_th);
			bw.write("\n# If multiple Map is used, row and column must be set.");
			bw.write("\n  row : " + row);
			bw.write("\n  column : " + column);
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void loadMap(File file) throws IOException {
		this.map = MapLoader.loadMap(file);
	}

	public void setZoomFactor(float z) {
		this.zoomFactor = z;
	}
}
