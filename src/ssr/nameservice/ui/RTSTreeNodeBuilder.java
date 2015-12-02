/**
 * RTSTreeNodeBuilder.java
 *
 * @author Yuki Suga (ysuga.net)
 * @date 2011/09/02
 * @copyright 2011, ysuga.net allrights reserved.
 *
 */
package ssr.nameservice.ui;

import java.util.ArrayList;
import java.util.logging.Logger;

import javax.swing.tree.DefaultMutableTreeNode;

import ssr.nameservice.CorbaNamingCannotFindException;
import ssr.nameservice.CorbaNamingResolveException;
import ssr.nameservice.RTNamingContext;
import ssr.rtsbuilder.RTSystemBuilder;
import RTC.ConnectorProfile;
import RTC.PortInterfacePolarity;
import RTC.PortInterfaceProfile;
import RTC.PortService;
import _SDOPackage.ConfigurationSet;
import _SDOPackage.InterfaceNotImplemented;
import _SDOPackage.InternalError;
import _SDOPackage.NameValue;
import _SDOPackage.NotAvailable;

/**
 * <div lang="ja">
 *
 * </div>
 * <div lang="en">
 *
 * </div>
 * @author ysuga
 *
 */
public class RTSTreeNodeBuilder {

	/**
	 * <div lang="ja">
	 * コンストラクタ
	 * </div>
	 * <div lang="en">
	 * Constructor
	 * </div>
	 */
	public RTSTreeNodeBuilder() {
		// TODO 自動生成されたコンストラクター・スタブ
	}

	
	/**
	 * 
	 * buildRTSTreeNode
	 * <div lang="ja">
	 * 
	 * @param namingContext
	 * @return
	 * </div>
	 * <div lang="en">
	 *
	 * @param namingContext
	 * @return
	 * </div>
	 */
	public static RTSTreeNode buildRTSTreeNode(RTSystemTreeView view, RTNamingContext namingContext) {
		RTSTreeNode rootNode = new RTSTreeNode(view, namingContext);
		if (namingContext.getKind().equals("rtc")) {
			for(DefaultMutableTreeNode n : buildRTCPropertyNodes(view, namingContext)) {
				rootNode.add(n);
			}
		}
		for(RTNamingContext nc : namingContext) {
			rootNode.add(buildRTSTreeNode(view, nc));
		}
		return rootNode;
		
	}
	
	
	public static ArrayList<DefaultMutableTreeNode> buildRTCPropertyNodes(RTSystemTreeView view, RTNamingContext namingContext) {
		ArrayList<DefaultMutableTreeNode> list = new ArrayList<DefaultMutableTreeNode>();
		
		try {
			RTC.RTObject obj = RTSystemBuilder.getComponent(namingContext.getFullPath());
			
			/// Port Profiles
			DefaultMutableTreeNode portNode = new DefaultMutableTreeNode("ports");
			list.add(portNode);
			for(PortService ps : obj.get_ports()) {
				String name = ps.get_port_profile().name;
				if(name.split("\\.").length > 1) {
					name = name.split("\\.")[1];
				}
				String dataType = "UnknownType";
				String portType = "port";
				for(NameValue nv : ps.get_port_profile().properties) {
					if (nv.name.equals("dataport.data_type")) {
						dataType = nv.value.extract_string();
					}
					if (nv.name.equals("port.port_type")) {
						if (nv.value.extract_string().equals("DataOutPort")) { 
							portType = "outport";
						} else if (nv.value.extract_string().equals("DataInPort")) {
						    portType = "inport";
						} else {
						    portType = "svcport";
						}
					}
				}
				RTSTreeNode tn = null;
				if(portType.equals("svcport")) {
					tn = new RTSTreeNode(view, new RTNamingContext(name, portType));
					tn.setPrivateData(ps);
					portNode.add(tn);
					for(PortInterfaceProfile pi : ps.get_port_profile().interfaces) {
						if(pi.polarity.equals(PortInterfacePolarity.PROVIDED)) {
							tn.add(new RTSTreeNode(view, new RTNamingContext(pi.type_name, "provided")));
						} else {
							tn.add(new RTSTreeNode(view, new RTNamingContext(pi.type_name, "required")));
						}
					}
				} else {
					tn = new RTSTreeNode(view, new RTNamingContext(name, portType));
					tn.setPrivateData(ps);
					portNode.add(tn);
					tn.add(new DefaultMutableTreeNode("data_type: '" + dataType + "'"));
				}
				
				// Add Connection Info
				if(ps.get_connector_profiles().length > 0) {
					for(ConnectorProfile cp : ps.get_connector_profiles()) {
						for(PortService ps2 : cp.ports) {
							if(!ps2.get_port_profile().name.equals(ps.get_port_profile().name)){
								RTSTreeNode n = new RTSTreeNode(view, new RTNamingContext(ps2.get_port_profile().name, "connection"));
								n.setPrivateData(cp);
								tn.add(n);
							}
						}
					}
				}
				

			}
			
			DefaultMutableTreeNode confNode = new DefaultMutableTreeNode("configurations");
			list.add(confNode);
			try {
				String active_id = obj.get_configuration().get_active_configuration_set().id;
				for(ConfigurationSet confSet : obj.get_configuration().get_configuration_sets()) {
					String kind = "conf_set";
					if(confSet.id.equals(active_id)) {
						kind = "active_conf_set";
					}
					if(confSet.id.startsWith("_")) {
						continue;
					}
					RTSTreeNode n = new RTSTreeNode(view, new RTNamingContext(confSet.id, kind));
					n.setPrivateData(confSet);
					for(NameValue conf : confSet.configuration_data) {
						RTSTreeNode cn = new RTSTreeNode(view, new RTNamingContext(conf.name + "(" + conf.value.extract_string() + ")", "conf"));
						cn.setPrivateData(obj);
						n.add(cn);
					}
					confNode.add(n);
				}
			} catch (NotAvailable | InternalError | InterfaceNotImplemented e) {
				//Logger.getLogger("MapperViewer").warning(e.getMessage());
			}
			
			// Configuration Profiles
		} catch (CorbaNamingCannotFindException | CorbaNamingResolveException | org.omg.CORBA.COMM_FAILURE ex) {
			//Logger.getLogger("MapperViewer").warning(ex.getMessage());
		}
		
		return list;
	}
}
