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

import ssr.nameservice.CorbaNamingCannotFindException;
import ssr.nameservice.CorbaNamingResolveException;
import ssr.nameservice.RTNamingContext;
import ssr.rtsbuilder.RTSystemBuilder;
import RTC.PortService;

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
	public static RTSTreeNode buildRTSTreeNode(RTNamingContext namingContext) {
		RTSTreeNode rootNode = new RTSTreeNode(namingContext);
		if (namingContext.getKind().equals("rtc")) {
			for(RTSTreeNode n : buildRTCPropertyNodes(namingContext)) {
				rootNode.add(n);
			}
		}
		for(RTNamingContext nc : namingContext) {
			rootNode.add(buildRTSTreeNode(nc));
		}
		return rootNode;
		
	}
	
	
	public static ArrayList<RTSTreeNode> buildRTCPropertyNodes(RTNamingContext namingContext) {
		ArrayList<RTSTreeNode> list = new ArrayList<RTSTreeNode>();
		
		try {
			RTC.RTObject obj = RTSystemBuilder.getComponent(namingContext.getFullPath());
			for(PortService ps : obj.get_ports()) {
				System.out.println("Port:" + ps.get_port_profile().name);
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
