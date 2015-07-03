package ssr.nameservice.ui;

import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import ssr.RTMHelper;
import RTC.PortService;

@SuppressWarnings("serial")
public class PortConnectionDialog extends JDialog {
    
	private PortService portService;
	
	private Vector<PortServiceHolder> connectablePorts;
	private JComboBox<PortServiceHolder> comboBox;
	
	private Map<String, String> properties;
	
	class PortServiceHolder {
		public PortService portService;
		
		public PortServiceHolder(PortService ps) {
			portService = ps;
		}
		
		public String toString() {
			return portService.get_port_profile().name;
		}
	}
	
    public PortConnectionDialog(Frame owner, PortService port, List<String> hostAddresses, Map<String, String> properties) throws Exception {
        super(owner);
        this.portService = port;
        this.properties = properties;
        connectablePorts = new Vector<PortServiceHolder>();
        
        
        for(String hostAddress : hostAddresses) {
        	try {
				Set<PortService> ports = RTMHelper.getRegisteredPortServiceSet(hostAddress);
				for(PortService p : ports) {
					if(RTMHelper.isConnectable(portService, p)) {
						connectablePorts.add(new PortServiceHolder(p));
					}
				}
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				throw e1;
			}
        	
        }
        String title = "Select Port to be connected with " + port.get_port_profile().name;
        JLabel label = new JLabel(title);
        JButton cancelButton = new JButton(new AbstractAction("Cancel") {
        	@Override
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
			}
        });
        JButton okButton = new JButton(new AbstractAction("OK") {
        	@Override
			public void actionPerformed(ActionEvent e) {
        		connectPort();
				setVisible(false);
			}
        });
        comboBox = new JComboBox<PortServiceHolder>(connectablePorts);
        JPanel contentPane = (JPanel)getContentPane();
        contentPane.setLayout(new FlowLayout());
        contentPane.add(label);
        contentPane.add(comboBox);
        contentPane.add(okButton);
        contentPane.add(cancelButton);
        
        setTitle(title);
     }
    
    
    void connectPort() {
    	PortServiceHolder port = (PortServiceHolder)comboBox.getSelectedItem();
    	RTMHelper.connectPorts(portService, port.portService, properties);
    }
    
}
