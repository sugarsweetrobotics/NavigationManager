package ssr.nameservice.ui;

import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.tree.DefaultMutableTreeNode;

import ssr.nameservice.RTNamingContext;
import ssr.rtsbuilder.RTCCondition;
import ssr.rtsprofile.RTComponent;

@SuppressWarnings("serial")
public class RTSTreeNode extends DefaultMutableTreeNode {

	private RTNamingContext rtNamingContext;
	private RTComponent component;

	public RTSTreeNode(RTNamingContext rtNamingContext) {
		this.rtNamingContext = rtNamingContext;
	}

	public String toString() {
		return rtNamingContext.getName();
	}

	public String getFullPath() {
		if (this.isRoot()) {
			return toString();
		}

		return rtNamingContext.getFullPath();
	}

	/**
	 * onClicked
	 * 
	 * @param e
	 */
	public void onClicked(MouseEvent e) {
		if (this.isLeaf()) {
			// JOptionPane.showMessageDialog(null, getFullPath());
		}
	}

	/**
	 * onPressed
	 * 
	 * @param e
	 */
	public void onPressed(MouseEvent e) {

	}

	static ImageIcon mgrImageIcon;
	static ImageIcon cxtImageIcon;
	static ImageIcon activeRTCImageIcon;
	static ImageIcon inactiveRTCImageIcon;
	static ImageIcon errorRTCImageIcon;
	static ImageIcon unknownRTCImageIcon;
	static ImageIcon unknownImageIcon;
	static {
		BufferedImage image;
		try {
			image = ImageIO.read(new File("tree_icon.gif"));
			BufferedImage sub = image.getSubimage(0, 0, 16, 16);
			mgrImageIcon = new ImageIcon(sub);
			sub = image.getSubimage(16, 0, 16, 16);
			cxtImageIcon = new ImageIcon(sub);
			sub = image.getSubimage(32, 0, 16, 16);
			activeRTCImageIcon = new ImageIcon(sub);
			sub = image.getSubimage(48, 0, 16, 16);
			inactiveRTCImageIcon = new ImageIcon(sub);
			sub = image.getSubimage(64, 0, 16, 16);
			errorRTCImageIcon = new ImageIcon(sub);
			sub = image.getSubimage(80, 0, 16, 16);
			unknownRTCImageIcon = new ImageIcon(sub);
			sub = image.getSubimage(96, 0, 16, 16);
			unknownImageIcon = new ImageIcon(sub);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * getImageIcon
	 * 
	 * @return
	 */
	public Icon getImageIcon() {
		if (this.toString().endsWith("rtc")) {
			if (component == null) {
				try {
					component = (RTComponent) RTSProfileHolder.getInstance()
							.get(RTSProfileHolder.ONLINE)
							.getComponentByFullPath(getFullPath());
					if (component == null) {
						return unknownRTCImageIcon;
					}
				} catch (Exception e) {
					// TODO 自動生成された catch ブロ�?��
					e.printStackTrace();
					component = null;
					return unknownRTCImageIcon;
				}
			}
			try {
				RTCCondition condition = component.getCondition();
				if (condition.equals(RTCCondition.ACTIVE)) {
					return activeRTCImageIcon;
				} else if (condition.equals(RTCCondition.INACTIVE)) {
					return inactiveRTCImageIcon;
				} else if (condition.equals(RTCCondition.ERROR)) {
					return errorRTCImageIcon;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return unknownRTCImageIcon;
		} else if (this.toString().endsWith("mgr")) {
			return mgrImageIcon;
		} else if (this.toString().endsWith("cxt")) {
			return cxtImageIcon;
		}
		return unknownImageIcon;
	}
}
