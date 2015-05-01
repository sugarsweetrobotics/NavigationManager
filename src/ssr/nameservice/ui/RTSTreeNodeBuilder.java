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

import javax.swing.JComponent;
import javax.swing.tree.DefaultMutableTreeNode;

import ssr.nameservice.CorbaNamingCannotFindException;
import ssr.nameservice.CorbaNamingResolveException;
import ssr.nameservice.RTNamingContext;
import ssr.rtsbuilder.RTSystemBuilder;
import RTC.PortInterfacePolarity;
import RTC.PortInterfaceProfile;
import RTC.PortService;
import _SDOPackage.NameValue;

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
			for(RTSTreeNode n : buildRTCPropertyNodes(view, namingContext)) {
				rootNode.add(n);
			}
		}
		for(RTNamingContext nc : namingContext) {
			rootNode.add(buildRTSTreeNode(view, nc));
		}
		return rootNode;
		
	}
	
	
	public static ArrayList<RTSTreeNode> buildRTCPropertyNodes(RTSystemTreeView view, RTNamingContext namingContext) {
		ArrayList<RTSTreeNode> list = new ArrayList<RTSTreeNode>();
		
		try {
			RTC.RTObject obj = RTSystemBuilder.getComponent(namingContext.getFullPath());
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
				if(portType.equals("svcport")) {
					RTSTreeNode tn = new RTSTreeNode(view, new RTNamingContext(name, portType));
					tn.setPrivateData(ps);
					list.add(tn);
					for(PortInterfaceProfile pi : ps.get_port_profile().interfaces) {
						if(pi.polarity.equals(PortInterfacePolarity.PROVIDED)) {
							tn.add(new RTSTreeNode(view, new RTNamingContext(pi.type_name, "provided")));
						} else {
							tn.add(new RTSTreeNode(view, new RTNamingContext(pi.type_name, "required")));
						}
					}
				} else {
					RTSTreeNode tn = new RTSTreeNode(view, new RTNamingContext(name, portType));
					tn.setPrivateData(ps);
					list.add(tn);
					tn.add(new DefaultMutableTreeNode("data_type: " + dataType));
				}
			}
		} catch (CorbaNamingCannotFindException | CorbaNamingResolveException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (org.omg.CORBA.COMM_FAILURE ex) {
			ex.printStackTrace();
		}
		
		return list;
	}
}
