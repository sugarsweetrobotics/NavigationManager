package ssr.nameservice.ui;

import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.tree.DefaultMutableTreeNode;

import jp.go.aist.rtm.RTC.Manager;
import jp.go.aist.rtm.RTC.RTObject_impl;
import ssr.nameservice.CorbaNamingCannotFindException;
import ssr.nameservice.CorbaNamingResolveException;
import ssr.nameservice.RTNamingContext;
import ssr.rtsbuilder.RTCCondition;
import ssr.rtsbuilder.RTSystemBuilder;
import ssr.rtsprofile.RTComponent;
import RTC.ConnectorProfile;
import RTC.ConnectorProfileHolder;
import RTC.PortInterfacePolarity;
import RTC.PortInterfaceProfile;
import RTC.PortService;
import _SDOPackage.NVListHolder;
import _SDOPackage.NameValue;

@SuppressWarnings("serial")
public class RTSTreeNode extends DefaultMutableTreeNode {

	private RTNamingContext rtNamingContext;
	private RTComponent component;
	private RTSystemTreeView view;

	public RTSTreeNode(RTSystemTreeView view, RTNamingContext rtNamingContext) {
		this.rtNamingContext = rtNamingContext;
		this.view = view;
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
	
	private Object privateData;
	public void setPrivateData(Object obj) {
		privateData = obj;
	}

	
	
	/**
	 * onClicked
	 * 
	 * @param e
	 */
	public void onClicked(MouseEvent e) {
		if (e.getButton() != e.BUTTON3) {
			return ;
		}
		System.out.println("rtNamingContext:" + rtNamingContext.getFullPath());
		if (rtNamingContext.getKind().equals("rtc")) {
			JPopupMenu pop = new JPopupMenu();
			view.pop = pop;;
			pop.setLocation(e.getLocationOnScreen());

			try {
				privateData = RTSystemBuilder.getComponent(rtNamingContext.getFullPath());
			} catch (CorbaNamingCannotFindException
					| CorbaNamingResolveException e1) {
				e1.printStackTrace();
			}
			pop.add(new JMenuItem(new AbstractAction("Activate") {

				@Override
				public void actionPerformed(ActionEvent e) {
					RTC.RTObject rto = (RTC.RTObject)privateData;
					rto.get_context(0).activate_component(rto);
				}
			}));
			pop.add(new JMenuItem(new AbstractAction("Deactivate") {

				@Override
				public void actionPerformed(ActionEvent e) {
					RTC.RTObject rto = (RTC.RTObject)privateData;
					rto.get_context(0).deactivate_component(rto);
				}
			}));
			pop.add(new JMenuItem(new AbstractAction("Reset") {

				@Override
				public void actionPerformed(ActionEvent e) {
					RTC.RTObject rto = (RTC.RTObject)privateData;
					rto.get_context(0).reset_component(rto);
				}
			}));
			pop.show(view,e.getLocationOnScreen().x-view.getLocationOnScreen().x,
					e.getLocationOnScreen().y-view.getLocationOnScreen().y);
			
		} else if (rtNamingContext.getKind().equals("inport")) {
			
		} else if (rtNamingContext.getKind().equals("outport")) {
			
		} else if (rtNamingContext.getKind().equals("svcport")) {
			PortService ps = (PortService)privateData;
			
		
			class ServicePortConnectAction extends AbstractAction {

				private String portName;
				private String connectionName;
				private PortService sourcePortService;
				
				public ServicePortConnectAction(String title, String targetPortName, PortService sourcePortService) {
					super(title);
					this.portName = targetPortName;
					this.connectionName = targetPortName + "_" + sourcePortService.get_port_profile().name;
					this.sourcePortService = sourcePortService;
				}
				
				@Override
				public void actionPerformed(ActionEvent e) {
					PortService sourcePortService = (PortService)privateData;
					connectPort(portName, connectionName,
							this.sourcePortService);
				}
				
			}
			
			
			for(PortInterfaceProfile pi : ps.get_port_profile().interfaces) {
				if (pi.type_name.equals("RTC::OGMapServer") && pi.polarity == PortInterfacePolarity.PROVIDED) {
					JPopupMenu pop = new JPopupMenu();
					view.pop = pop;
					pop.add(new JMenuItem(new ServicePortConnectAction("Connect",
							"mapServer", (PortService)privateData)));
					pop.show(view, 
							e.getLocationOnScreen().x-view.getLocationOnScreen().x,
							e.getLocationOnScreen().y-view.getLocationOnScreen().y);
				} else if (pi.type_name.equals("RTC::PathPlanner") && pi.polarity == PortInterfacePolarity.PROVIDED) {
					JPopupMenu pop = new JPopupMenu();
					view.pop = pop;
					pop.add(new JMenuItem(new ServicePortConnectAction("Connect",
							"pathPlanner", (PortService)privateData)));
					pop.show(view, 
							e.getLocationOnScreen().x-view.getLocationOnScreen().x,
							e.getLocationOnScreen().y-view.getLocationOnScreen().y);
				} else if (pi.type_name.equals("RTC::OGMapper") && pi.polarity == PortInterfacePolarity.PROVIDED) {
					JPopupMenu pop = new JPopupMenu();
					view.pop = pop;
					pop.add(new JMenuItem(new ServicePortConnectAction("Connect",
							"mapperService", (PortService)privateData)));
					pop.show(view, 
							e.getLocationOnScreen().x-view.getLocationOnScreen().x,
							e.getLocationOnScreen().y-view.getLocationOnScreen().y);
				}
			}
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
	static ImageIcon inPortImageIcon;
	static ImageIcon outPortImageIcon;
	static ImageIcon servicePortImageIcon;
	static ImageIcon requiredImageIcon;
	static ImageIcon providedImageIcon;
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
			sub = image.getSubimage(112, 0, 16, 16);
			inPortImageIcon = new ImageIcon(sub);
			sub = image.getSubimage(128, 0, 16, 16);
			outPortImageIcon = new ImageIcon(sub);
			sub = image.getSubimage(144, 0, 16, 16);
			servicePortImageIcon = new ImageIcon(sub);
			sub = image.getSubimage(160, 0, 16, 16);
			providedImageIcon = new ImageIcon(sub);
			sub = image.getSubimage(176, 0, 16, 16);
			requiredImageIcon = new ImageIcon(sub);
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
		} else if (this.toString().endsWith("inport")) {
			return inPortImageIcon;
		} else if (this.toString().endsWith("outport")) {
			return outPortImageIcon;
		} else if (this.toString().endsWith("svcport")) {
			return servicePortImageIcon;
		} else if (this.toString().endsWith("required")) {
			return requiredImageIcon;
		} else if (this.toString().endsWith("provided")) {
			return providedImageIcon;
		}
		return unknownImageIcon;
	}

	private void connectPort(String ownPortName, String connectionName,
			PortService sourcePortService) {
		Manager mgr = Manager.instance();
		for(RTObject_impl rto : mgr.getComponents()) {
			if(rto.get_component_profile().type_name.equals("MapperViewer")) {
				for(PortService ps2: rto.get_ports()) {
					String name = ps2.get_port_profile().name;
					
					if(name.split("\\.").length > 1) {
						System.out.println("split");
						name = name.split("\\.")[1];
					}
					System.out.println(name);
					if(name.equals(ownPortName)) {
						ConnectorProfile prof = new ConnectorProfile();
						prof.connector_id = connectionName;
						prof.name = connectionName;
						prof.ports = new PortService[2];
						prof.ports[0] = sourcePortService;
						prof.ports[1] = ps2;
						NVListHolder nvholder = new NVListHolder();
						nvholder.value = prof.properties;
						if (nvholder.value == null)
							nvholder.value = new NameValue[0];
						prof.properties = nvholder.value;
						ConnectorProfileHolder proflist = new ConnectorProfileHolder();
						proflist.value = prof;
						ps2.connect(proflist);
					}
				}
			}
		}
	}
}
