package application;
import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class CameraViewPanel extends JPanel {

	
	private RTC.CameraImage image;

	public void setImage(RTC.CameraImage image) {
		this.image = image;
	}

	private BufferedImage img;

	public CameraViewPanel() {
		super();
		setLayout(new BorderLayout());
		setSize(640, 480);
		/*
		 * JToolBar toolBar = new JToolBar(); this.add(toolBar,
		 * BorderLayout.NORTH); JButton ctrlBtn = new JButton("Ctrl");
		 * toolBar.add(ctrlBtn);
		 */
		// add(cameraImagePanel, BorderLayout.CENTER);
	}

	@Override
	public void paintComponent(Graphics g_) {
		if (image == null) {
			Graphics2D g = (Graphics2D) g_;//getGraphics();
			if (g != null) {
				g.drawString("No Image", 10, 20);
			}
		} else {
			Graphics2D g2d = (Graphics2D) getGraphics();
			int width = this.getWidth();
			int height = this.getHeight();
			synchronized (this) {
				if (img == null) {
					img = new BufferedImage(image.width, image.height,
							BufferedImage.TYPE_INT_RGB);
				} else if (img.getWidth() != image.width || img.getHeight() != image.height) {
					img = new BufferedImage(image.width, image.height,
							BufferedImage.TYPE_INT_RGB);
				}
				
				for (int y = 0; y < image.height; y++) {
					for (int x = 0; x < image.width; x++) {
						int index = y * image.width + x;
						byte r = image.pixels[index * 3 + 2];
						byte g = image.pixels[index * 3 + 1];
						byte b = image.pixels[index * 3 + 0];
						int rgb = (0x00FF & (int) r) << 16
								| (0x00FF & (int) g) << 8 | (0x00FF & (int) b);
						img.setRGB(x, y, rgb);
					}
				}
				g2d.drawImage(img, 0, 0, width, height, this);
			}
		}
	}
}
