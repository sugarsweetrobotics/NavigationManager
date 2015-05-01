package ssr.nameservice.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.util.StringTokenizer;
import java.util.logging.Logger;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JToolBar;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import ssr.nameservice.CorbaNamingCannotFindException;
import ssr.nameservice.CorbaNamingParser;
import ssr.nameservice.RTNamingContext;
import ssr.rtsbuilder.RTSystemBuilder;
import ssr.rtsprofile.RTSystemProfile;


@SuppressWarnings("serial")
public class RTSystemTreeView extends JPanel {

	static Logger logger = Logger.getLogger("net.ysuga.rtsbuilder.ui");
	DefaultMutableTreeNode rootNode;
	JToolBar toolBar;

	private RTSTree treeView;
	public JPopupMenu pop;

	public RTSTree getTree() {
		return treeView;
	}

	protected void onConnect() {
		String hostAddress = JOptionPane.showInputDialog(null, "Name Server?",
				"localhost:2809");
		if (hostAddress != null) {
			try {

				StringTokenizer tokenizer2 = new StringTokenizer(hostAddress, ":");
				if (tokenizer2.countTokens() == 1) {
					hostAddress = hostAddress + ":2809";
				}
				
				RTNamingContext nc = CorbaNamingParser
						.buildRTNamingContext(hostAddress);
				RTSystemProfile onlineProfile = RTSProfileHolder.getInstance()
						.get(RTSProfileHolder.ONLINE);
				RTSystemBuilder.downwardSynchronization(onlineProfile);
				RTSProfileHolder.getInstance().addNamingAddress(hostAddress);
				onlineProfile.addAllComponent(nc);
				rootNode.add(RTSTreeNodeBuilder.buildRTSTreeNode(this, nc));
				treeView.invalidate();
				treeView.validate();
				((DefaultTreeModel) treeView.getModel()).reload();

			} catch (CorbaNamingCannotFindException ex) {
				JOptionPane.showMessageDialog(null, "Cannot connect to Name Service (" + hostAddress + ")");
			} catch (Exception ex) {
				logger.warning("RTSystemTreeView.onConnect(): connecting " + hostAddress + " failed.");
				ex.printStackTrace();
			}
		}

		RTSProfileHolder.getInstance().refreshAll();
		refresh();
	}

	public void refresh() {
		treeView.refreshModel(this);
		treeView.repaint();
	}

	/**
	 * 
	 * onRefresh
	 * 
	 */
	protected void onRefresh() {
		RTSProfileHolder.getInstance().refreshAll();
		refresh();
	}

	private void initToolBar() {
		toolBar = new JToolBar();
		toolBar.add(new JButton(new AbstractAction("Connect") {
			public void actionPerformed(ActionEvent e) {
				onConnect();
			}
		}));

		toolBar.add(new JButton(new AbstractAction("Refresh") {
			public void actionPerformed(ActionEvent e) {
				onRefresh();
			}
		}));
	}

	public RTSystemTreeView() {
		super();

		initToolBar();
		treeView = new RTSTree();

		rootNode = new DefaultMutableTreeNode("/");

		DefaultTreeModel model = (DefaultTreeModel) treeView.getModel();
		model.setRoot(rootNode);

		setLayout(new BorderLayout());
		add(BorderLayout.CENTER, treeView);
		add(BorderLayout.NORTH, toolBar);

		this.setPreferredSize(new Dimension(1400, 1200));
	}

}
