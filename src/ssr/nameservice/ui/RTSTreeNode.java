package ssr.nameservice.ui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.tree.DefaultMutableTreeNode;

import jp.go.aist.rtm.RTC.Manager;
import jp.go.aist.rtm.RTC.util.CORBA_SeqUtil;
import jp.go.aist.rtm.RTC.util.NVUtil;

import org.omg.CORBA.Any;

import ssr.RTMHelper;
import ssr.nameservice.CorbaNamingCannotFindException;
import ssr.nameservice.CorbaNamingResolveException;
import ssr.nameservice.RTNamingContext;
import ssr.rtsbuilder.RTCCondition;
import ssr.rtsbuilder.RTSystemBuilder;
import ssr.rtsprofile.RTComponent;
import RTC.ConnectorProfile;
import RTC.PortInterfacePolarity;
import RTC.PortInterfaceProfile;
import RTC.PortService;
import RTC.RTObject;
import _SDOPackage.ConfigurationSet;
import _SDOPackage.InterfaceNotImplemented;
import _SDOPackage.InternalError;
import _SDOPackage.InvalidParameter;
import _SDOPackage.NVListHolder;
import _SDOPackage.NameValue;
import _SDOPackage.NotAvailable;

@SuppressWarnings("serial")
public class RTSTreeNode extends DefaultMutableTreeNode {

	private RTNamingContext rtNamingContext;
	private RTComponent component;
	private RTSystemTreeView view;
	private Logger logger;

	public RTSTreeNode(RTSystemTreeView view, RTNamingContext rtNamingContext) {
		this.rtNamingContext = rtNamingContext;
		this.view = view;
		logger = Logger.getLogger("MapperViewer");
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

	class ConnectToMeAction extends AbstractAction {
		private String portName;
		private PortService targetPortService;
		private PortService sourcePortService;
		private Map<String, String> properties;

		public ConnectToMeAction(String title,
				String targetPortName, PortService portService, Map<String, String> properties) {
			super(title);
			this.portName = targetPortName;
			List<PortService> ports = RTMHelper
					.searchPortServicesFromType("MapperViewer",
							portName);
			if (ports.size() == 1) {
				sourcePortService = ports.get(0);
			} else if (ports.size() == 0) {
				logger.warning("Can not find " + "MapperViewer."
						+ targetPortName);
				logger.warning("MapperViewer does not support Connection Functions");
			} else {
				logger.warning("Can find multiple" + "MapperViewer."
						+ targetPortName);
				logger.warning("MapperViewer does not support Connection Functions");
			}
			this.targetPortService = portService;
			this.properties = properties;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			RTMHelper
					.connectPorts(sourcePortService, targetPortService, properties);
		}
	}
	
	class PortConnectAction extends AbstractAction {
		private PortService portService;
		private Map<String, String> properties;
		public PortConnectAction(String title, PortService port, Map<String, String> properties) {
			super(title);
			portService = port;
			this.properties = properties;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			List<String> hostAddresses = view.getHostAddresses();
			PortConnectionDialog dialog = new PortConnectionDialog(
					null, portService, hostAddresses, properties);
			dialog.setModal(true);
			dialog.setSize(new Dimension(400, 400));
			dialog.setVisible(true);
		}

	}

	
	/**
	 * onClicked
	 * 
	 * @param e
	 */
	public void onClicked(MouseEvent e) {
		if (e.getButton() != e.BUTTON3) {
			return;
		}
		if (rtNamingContext.getKind().equals("mgr")) {
			JPopupMenu pop = new JPopupMenu();
			view.pop = pop;
			pop.setLocation(e.getLocationOnScreen());
			try {
				privateData = RTSystemBuilder.getManager(rtNamingContext
						.getFullPath());
			} catch (CorbaNamingCannotFindException
					| CorbaNamingResolveException e1) {
				logger.warning(e1.getMessage());
			}
			pop.add(new JMenuItem(new AbstractAction("Shutdown") {

				@Override
				public void actionPerformed(ActionEvent e) {
					RTM.Manager rto = (RTM.Manager) privateData;

					logger.info("Shutting Down Manager....");
					if(rto.shutdown() == RTC.ReturnCode_t.RTC_OK) {
						logger.info("Successfully Shutdown");
					} else {
						logger.warning("Failed.");
					}
				}
			}));
			pop.show(view,
					e.getLocationOnScreen().x - view.getLocationOnScreen().x,
					e.getLocationOnScreen().y - view.getLocationOnScreen().y);

		} else if (rtNamingContext.getKind().equals("rtc")) {
			JPopupMenu pop = new JPopupMenu();
			view.pop = pop;
			;
			pop.setLocation(e.getLocationOnScreen());

			try {
				privateData = RTSystemBuilder.getComponent(rtNamingContext
						.getFullPath());
			} catch (CorbaNamingCannotFindException
					| CorbaNamingResolveException e1) {
				e1.printStackTrace();
			}
			pop.add(new JMenuItem(new AbstractAction("Activate") {

				@Override
				public void actionPerformed(ActionEvent e) {
					RTC.RTObject rto = (RTC.RTObject) privateData;

					logger.info("Activating "
							+ rto.get_component_profile().instance_name);
					rto.get_context(0).activate_component(rto);
				}
			}));
			pop.add(new JMenuItem(new AbstractAction("Deactivate") {

				@Override
				public void actionPerformed(ActionEvent e) {
					RTC.RTObject rto = (RTC.RTObject) privateData;
					logger.info("Dectivating "
							+ rto.get_component_profile().instance_name);
					rto.get_context(0).deactivate_component(rto);
				}
			}));
			pop.add(new JMenuItem(new AbstractAction("Reset") {

				@Override
				public void actionPerformed(ActionEvent e) {
					RTC.RTObject rto = (RTC.RTObject) privateData;
					logger.info("Resetting "
							+ rto.get_component_profile().instance_name);
					rto.get_context(0).reset_component(rto);
				}
			}));
			pop.add(new JMenuItem(new AbstractAction("Exit") {

				@Override
				public void actionPerformed(ActionEvent e) {
					RTC.RTObject rto = (RTC.RTObject) privateData;
					logger.info("Exitting "
							+ rto.get_component_profile().instance_name);
					rto.exit();
				}
			}));
			pop.show(view,
					e.getLocationOnScreen().x - view.getLocationOnScreen().x,
					e.getLocationOnScreen().y - view.getLocationOnScreen().y);

		} else if (rtNamingContext.getKind().equals("inport")) {
			PortService ps = (PortService) privateData;
			String data_type = RTMHelper.getDataPortDataType(ps);

			JPopupMenu pop = new JPopupMenu();
			view.pop = pop;
			if(data_type.equals("IDL:RTC/TimedPose2D:1.0")) {
				pop.add(new JMenuItem(new ConnectToMeAction(
						"Connect to Me", "targetVelocity",
						(PortService) privateData, RTMHelper.getDefaultDataPortConnectionProperties())));
			}
			pop.add(new JMenuItem(new PortConnectAction("Connect", ps, RTMHelper.getDefaultDataPortConnectionProperties())));
			pop.show(view,
					e.getLocationOnScreen().x
							- view.getLocationOnScreen().x,
					e.getLocationOnScreen().y
							- view.getLocationOnScreen().y);			
		} else if (rtNamingContext.getKind().equals("outport")) {
			PortService ps = (PortService) privateData;
			String data_type = RTMHelper.getDataPortDataType(ps);

			JPopupMenu pop = new JPopupMenu();
			view.pop = pop;
			if(data_type.equals("IDL:RTC/RangeData:1.0")) {
				pop.add(new JMenuItem(new ConnectToMeAction(
						"Connect to Me", "range",
						(PortService) privateData, RTMHelper.getDefaultDataPortConnectionProperties())));
			} else if(data_type.equals("IDL:RTC/TimedPose2D:1.0")) {
				pop.add(new JMenuItem(new ConnectToMeAction(
						"Connect to Me", "currentPose",
						(PortService) privateData, RTMHelper.getDefaultDataPortConnectionProperties())));
			} else if(data_type.equals("IDL:RTC/CameraImage:1.0")) {
				pop.add(new JMenuItem(new ConnectToMeAction(
						"Connect to Me", "camera",
						(PortService) privateData, RTMHelper.getDefaultDataPortConnectionProperties())));
			}
			pop.add(new JMenuItem(new PortConnectAction("Connect", ps, RTMHelper.getDefaultDataPortConnectionProperties())));
			pop.show(view,
					e.getLocationOnScreen().x
							- view.getLocationOnScreen().x,
					e.getLocationOnScreen().y
							- view.getLocationOnScreen().y);
			
		} else if (rtNamingContext.getKind().equals("svcport")) {
			PortService ps = (PortService) privateData;
			for (PortInterfaceProfile pi : ps.get_port_profile().interfaces) {
				JPopupMenu pop = new JPopupMenu();
				view.pop = pop;
				if (pi.type_name.equals("RTC::OGMapServer")
						&& pi.polarity == PortInterfacePolarity.PROVIDED) {
					pop.add(new JMenuItem(new ConnectToMeAction(
							"Connect to Me", "mapServer",
							(PortService) privateData, null)));
				} else if (pi.type_name.equals("RTC::PathPlanner")
						&& pi.polarity == PortInterfacePolarity.PROVIDED) {
					pop.add(new JMenuItem(new ConnectToMeAction(
							"Connect to Me", "pathPlanner",
							(PortService) privateData, null)));
				} else if (pi.type_name.equals("RTC::OGMapper")
						&& pi.polarity == PortInterfacePolarity.PROVIDED) {
					pop.add(new JMenuItem(new ConnectToMeAction(
							"Connect to Me", "mapperService",
							(PortService) privateData, null)));
				}
				pop.add(new JMenuItem(new PortConnectAction("Connect", ps, null)));
				pop.add(new JMenuItem(new AbstractAction("Disconnect All") {

					@Override
					public void actionPerformed(ActionEvent e) {
						PortService ps = (PortService) privateData;
						ps.disconnect_all();
					}
					
				}));
				pop.show(view,
						e.getLocationOnScreen().x
								- view.getLocationOnScreen().x,
						e.getLocationOnScreen().y
								- view.getLocationOnScreen().y);

			}
		} else if (rtNamingContext.getKind().equals("connection")) {
			JPopupMenu pop = new JPopupMenu();
			view.pop = pop;
			pop.add(new JMenuItem(new AbstractAction("Disconnect") {

				@Override
				public void actionPerformed(ActionEvent e) {
					ConnectorProfile cp = (ConnectorProfile)privateData;
					PortService ps = (PortService) ((RTSTreeNode)parent).privateData;
					logger.info("Disconnecting " + cp.connector_id);
					if(ps.disconnect(cp.connector_id) == RTC.ReturnCode_t.RTC_OK) {
						logger.info("Successfully Disconnected");
					} else {
						logger.info("Failed");
					}
				}
				
			}));
			pop.show(view,
					e.getLocationOnScreen().x
							- view.getLocationOnScreen().x,
					e.getLocationOnScreen().y
							- view.getLocationOnScreen().y);
		} else if (rtNamingContext.getKind().equals("conf")) {
			JPopupMenu pop = new JPopupMenu();
			view.pop = pop;
			pop.add(new JMenuItem(new AbstractAction("Modify") {

				@Override
				public void actionPerformed(ActionEvent e) {
					String name = rtNamingContext.getName().split("\\(")[0].trim();
					RTObject rto = (RTObject) privateData;
					ConfigurationSet confSet = null;
					try {
						confSet = rto.get_configuration().get_active_configuration_set();
					} catch (NotAvailable e2) {
						// TODO Auto-generated catch block
						e2.printStackTrace();
					} catch (InternalError e2) {
						// TODO Auto-generated catch block
						e2.printStackTrace();
					} catch (InterfaceNotImplemented e2) {
						// TODO Auto-generated catch block
						e2.printStackTrace();
					}//(ConfigurationSet)((RTSTreeNode)parent).privateData;
					String def_value = "";
					for(NameValue nv : confSet.configuration_data) {
						if(nv.name.equals(name)) {
							def_value = nv.value.extract_string();
						}
					}
					String new_value = JOptionPane.showInputDialog(view, 
							"Input Configuration Value of '" + name + "'", def_value);
					if(new_value != null) {
						NVListHolder nvholder = new NVListHolder();
						if (nvholder.value == null)
							nvholder.value = new NameValue[0];
						for(NameValue nv : confSet.configuration_data) {
							String value = nv.value.extract_string();
							if(nv.name.equals(name)) {
								value = new_value;
							}
							
							CORBA_SeqUtil.push_back(nvholder, NVUtil.newNVString(
								nv.name, value));
						}
						confSet.configuration_data = nvholder.value;
						Any d = Manager.instance().getORB().create_any();
						d.insert_string(new_value);
						try {
							rto.get_configuration().set_configuration_set_values(confSet);
							//rto.get_configuration()
						} catch (InvalidParameter e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						} catch (NotAvailable e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						} catch (InternalError e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						} catch (InterfaceNotImplemented e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
				}
				
			}));
			pop.show(view,
					e.getLocationOnScreen().x
							- view.getLocationOnScreen().x,
					e.getLocationOnScreen().y
							- view.getLocationOnScreen().y);
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
	static ImageIcon connectionImageIcon;
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
			sub = image.getSubimage(192, 0, 16, 16);
			connectionImageIcon = new ImageIcon(sub);
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
		} else if (this.toString().endsWith("connection")) {
			return connectionImageIcon;
		}
		return null;
	}

}
