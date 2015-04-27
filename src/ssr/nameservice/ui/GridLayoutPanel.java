package ssr.nameservice.ui;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JPanel;

public class GridLayoutPanel extends JPanel {

	/**
	 * 
	 */
	public GridLayoutPanel() {
		super();
		gridBag = new GridBagLayout();
		constraints = new GridBagConstraints();
		constraints.insets = new Insets(3, 3, 3, 3);
		constraints.anchor = GridBagConstraints.WEST;
		constraints.fill = GridBagConstraints.BOTH;
		setLayout(gridBag);
	}
	
	protected GridBagLayout gridBag;
	protected GridBagConstraints constraints;
	

	final public void addComponent(int x, int y, int w, int h, Component label) {
		constraints.gridx = x;
		constraints.gridy = y;
		constraints.gridwidth = w;
		constraints.gridheight = h;
		gridBag.setConstraints(label, constraints);
		add(label);
	}
	
	final public void addComponent(int x, int y, double wx, double wy, int w, int h, Component label) {
		constraints.gridx = x;
		constraints.gridy = y;
		constraints.weightx = wx;
		constraints.weighty = wy;
		constraints.gridwidth = w;
		constraints.gridheight = h;
		gridBag.setConstraints(label, constraints);
		add(label);
	}

}
