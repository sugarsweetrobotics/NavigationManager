package ssr.nameservice.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.Timer;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import ssr.nameservice.CorbaNamingCannotFindException;
import ssr.nameservice.CorbaNamingParser;
import ssr.nameservice.RTNamingContext;
import ssr.rtsbuilder.RTSystemBuilder;
import ssr.rtsprofile.RTSystemProfile;
import application.NavigationLogger;

@SuppressWarnings("serial")
public class RTSystemTreeView extends JPanel {

	static NavigationLogger logger = new NavigationLogger();//Logger.getLogger("MapperViewer");
	DefaultMutableTreeNode rootNode;
	JToolBar toolBar;

	private List<String> hostAddresses;
	private RTSTree treeView;
	public JPopupMenu pop;
	private JButton autoRefreshButton;

	public RTSTree getTree() {
		return treeView;
	}

	protected void onConnect() {
		String hostAddress = JOptionPane.showInputDialog(null, "Name Server?",
				"localhost:2809");
		if (hostAddress != null) {
			try {
				StringTokenizer tokenizer2 = new StringTokenizer(hostAddress,
						":");
				if (tokenizer2.countTokens() == 1) {
					hostAddress = hostAddress + ":2809";
				}

				if (hostAddresses.contains(hostAddress)) {
					logger.info("Host " + hostAddress
							+ " is already connected.");
					return;
				}
				hostAddresses.add(hostAddress);

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
				JOptionPane.showMessageDialog(null,
						"Cannot connect to Name Service (" + hostAddress + ")");
			} catch (Exception ex) {
				logger.warning("RTSystemTreeView.onConnect(): connecting "
						+ hostAddress + " failed.");
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

		autoRefreshButton = new JButton(
				new AbstractAction("Start Auto Refresh") {
					public void actionPerformed(ActionEvent e) {
						onStartAutoRefresh();
					}
				});
		toolBar.add(autoRefreshButton);
	}

	private void onStartAutoRefresh() {

		if (refreshTimer == null) {
			refreshTimer = new Timer(1000, new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent arg0) {
					onRefresh();
				}

			});

			refreshTimer.start();
			autoRefreshButton.setText("Stop Auto Refresh");
		} else {
			refreshTimer.stop();
			refreshTimer = null;
			autoRefreshButton.setText("Start Auto Refresh");
		}
	}

	Timer refreshTimer;

	public RTSystemTreeView() {
		super();

		initToolBar();
		treeView = new RTSTree();

		rootNode = new DefaultMutableTreeNode("/");

		DefaultTreeModel model = (DefaultTreeModel) treeView.getModel();
		model.setRoot(rootNode);

		setLayout(new BorderLayout());
		add(BorderLayout.CENTER, new JScrollPane(treeView));
		add(BorderLayout.NORTH, toolBar);

		hostAddresses = new ArrayList<String>();
		this.setPreferredSize(new Dimension(1400, 1200));
	}

	public List<String> getHostAddresses() {
		// TODO Auto-generated method stub
		return hostAddresses;
	}

}
