/**
 * RTSTree.java
 *
 * @author Yuki Suga (ysuga.net)
 * @date 2011/09/02
 * @copyright 2011, ysuga.net allrights reserved.
 *
 */
package ssr.nameservice.ui;

import java.awt.Component;
import java.util.ArrayList;
import java.util.Enumeration;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import ssr.nameservice.CorbaNamingParser;
import ssr.nameservice.RTNamingContext;

/**
 * <div lang="ja">
 * 
 * </div> <div lang="en">
 * 
 * </div>
 * 
 * @author ysuga
 * 
 */
public class RTSTree extends JTree {

	class RTSTreeCellRenderer extends DefaultTreeCellRenderer {

		@Override
		public Component getTreeCellRendererComponent(JTree tree, Object value,
				boolean sel, boolean expanded, boolean leaf, int row,
				boolean hasFocus) {
			Component c = super.getTreeCellRendererComponent(tree, value, sel,
					expanded, leaf, row, hasFocus);
			if (value instanceof RTSTreeNode) {
				setIcon(((RTSTreeNode) value).getImageIcon());
			}
			return c;
		}
	};

	/**
	 * 
	 * Constructor
	 */
	public RTSTree() {
		super();
		RTSTreeMouseAdapter mouseAdapter = new RTSTreeMouseAdapter(this);
		this.addMouseListener(mouseAdapter);
		this.addMouseMotionListener(mouseAdapter);

		this.setCellRenderer(new RTSTreeCellRenderer());
	}

	public synchronized void refreshModel() {
		DefaultMutableTreeNode rootNode = (DefaultMutableTreeNode) this
				.getModel().getRoot();
		int count = rootNode.getChildCount();
		ArrayList<String> addressList = new ArrayList<String>();

		saveExpandedNodeName(new TreePath(rootNode));
		// Store IP addresses of servers.
		for (int i = 0; i < count; i++) {
			String hostAddress = ((DefaultMutableTreeNode) rootNode
					.getChildAt(i)).toString();
			addressList.add(hostAddress);
		}
		rootNode.removeAllChildren();

		for (int i = 0; i < count; i++) {
			try {
				RTNamingContext nc = CorbaNamingParser
						.buildRTNamingContext(addressList.get(i));

				/*
				 * RTSystemProfile onlineProfile =
				 * RTSProfileHolder.getInstance() .get(RTSProfileHolder.ONLINE);
				 * onlineProfile.addAllComponent(nc);
				 */
				rootNode.add(RTSTreeNodeBuilder.buildRTSTreeNode(nc));
			} catch (Exception e1) {
				// logger.warning("Refreshing Name Server View failed.");
			}
		}

		((DefaultTreeModel) getModel()).reload();
		restoreExpandedNode(new TreePath(rootNode));
		expandedNodeName = null;
	}

	private ArrayList<String> expandedNodeName;

	private void saveExpandedNodeName(TreePath parent) {
		if (expandedNodeName == null) {
			expandedNodeName = new ArrayList<String>();
		}

		TreeNode node = (TreeNode) parent.getLastPathComponent();
		if (!node.isLeaf() && node.getChildCount() >= 0) {
			if (isExpanded(parent)) {
				expandedNodeName.add(node.toString());
			}
			Enumeration e = node.children();
			while (e.hasMoreElements()) {
				TreeNode n = (TreeNode) e.nextElement();
				TreePath path = parent.pathByAddingChild(n);
				saveExpandedNodeName(path);
			}
		}
	}

	private void restoreExpandedNode(TreePath parent) {
		if (expandedNodeName == null) {
			return;
		}
		TreeNode node = (TreeNode) parent.getLastPathComponent();
		if (!node.isLeaf() && node.getChildCount() >= 0) {
			if (expandedNodeName.contains(node.toString())) {
				expandPath(parent);
			}
			Enumeration e = node.children();
			while (e.hasMoreElements()) {
				TreeNode n = (TreeNode) e.nextElement();
				TreePath path = parent.pathByAddingChild(n);
				restoreExpandedNode(path);
			}
		}
	}

}
