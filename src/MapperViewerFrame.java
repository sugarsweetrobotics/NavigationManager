import java.awt.Dimension;

import javax.swing.JFrame;


public class MapperViewerFrame extends JFrame {

	
	private MapperViewerImpl rtc;

	public MapperViewerFrame(MapperViewerImpl rtc) {
		super("Mapper Viewer");
		
		this.rtc = rtc;
		
		
		setSize(new Dimension(640, 480));
		setVisible(true);
	}
}
