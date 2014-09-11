import java.awt.BorderLayout;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JToolBar;


public class CameraViewPanel extends JPanel {

	
	class CameraImagePanel extends JPanel {
		public BufferedImage img;
		
		public CameraImagePanel() {
			super();
		}
		
		public void setImage(RTC.CameraImage image) {
			if (img == null) {
				img = new BufferedImage(image.width, image.height, BufferedImage.TYPE_INT_RGB);
			}
			for (int y = 0;y < image.height;y++) {
				for (int x = 0;x < image.width;x++) {
					int index = y * image.width + x;
					byte r = image.pixels[index*3 + 2];
					byte g = image.pixels[index*3 + 1];
					byte b = image.pixels[index*3 + 0];
					int rgb = (0x00FF & (int)r) << 16 | (0x00FF & (int)g) << 8 | (0x00FF & (int)b);
					img.setRGB(x, y, rgb);
				}
			}
		}

		@Override
		public void repaint() {
			if (img != null) {
				Graphics2D g = (Graphics2D)getGraphics();
				int width = this.getWidth();
				int height = this.getHeight();
				g.drawImage(img, 0, 0, width, height, this);
			}
		}
	}
	
	private CameraImagePanel cameraImagePanel;
	public BufferedImage img;
	
	public CameraViewPanel() {
		super();
		setLayout(new BorderLayout());
		/*
		JToolBar toolBar = new JToolBar();
		this.add(toolBar, BorderLayout.NORTH);
		JButton ctrlBtn = new JButton("Ctrl");
		toolBar.add(ctrlBtn);
		*/
		cameraImagePanel = new CameraImagePanel();
		add(cameraImagePanel, BorderLayout.CENTER);
	}
	

	public void setImage(RTC.CameraImage image) {
		cameraImagePanel.setImage(image);
	}
}
