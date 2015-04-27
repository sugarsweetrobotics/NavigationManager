/**
 * Connector.java
 * 
 * @author Yuki Suga (ysuga@ysuga.net)
 * @copyright 2011, ysuga.net allrights reserved.
 */


package ssr.rtsprofile;

import java.io.IOException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import ssr.rtsbuilder.RTSystemBuilder;

/**
 * Connector
 * @author ysuga
 *
 */
public class DataPortConnector extends PortConnector {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8324464900056590583L;


	/**
	 * Constructor
	 */
	public DataPortConnector(String connectorId, String name, 
			String dataType, String interfaceType, 
			String dataflowType, String subscriptionType) {
		super();
		pivotList = new PivotList();
		put(INTERFACE_TYPE, interfaceType);
		put(DATAFLOW_TYPE, dataflowType);
		put(SUBSCRIPTION_TYPE, subscriptionType);
		put(NAME, name);
		put(TYPE, "rtsExt:dataport_connector_ext");
		put(CONNECTOR_ID, connectorId);
		put(DATA_TYPE, dataType);
	}

	public DataPortConnector(String connectorId, String name, 
			String dataType, String interfaceType, 
			String dataflowType, String subscriptionType, String pushInterval) {
		super();
		pivotList = new PivotList();
		put(INTERFACE_TYPE, interfaceType);
		put(DATAFLOW_TYPE, dataflowType);
		put(SUBSCRIPTION_TYPE, subscriptionType);
		put(NAME, name);
		put(TYPE, "rtsExt:dataport_connector_ext");
		put(CONNECTOR_ID, connectorId);
		put(DATA_TYPE, dataType);
		put(PUSH_INTERVAL, pushInterval);
	}
	
	public DataPortConnector() throws IOException {
		this("defaultConnectorId", "defaultName",
				"defaultDataType", "defaultInterfaceType",
				"defaultDataflowType", "defaultSubscriptionType");
	}
	/**
	 * Constructor
	 * @param node XML node.
	 * @throws IOException
	 */
	public DataPortConnector(Node node) throws IOException {
		this();
		load(node);
		
		NodeList nodeList = node.getChildNodes();
		for (int i = 0; i < nodeList.getLength(); i++) {
			Node cnode = nodeList.item(i);
			if (cnode.getNodeName().equals("rts:sourceDataPort")) {
				sourcePort = new Port(cnode);
			} else if (cnode.getNodeName().equals("rts:targetDataPort")) {
				targetPort = new Port(cnode);
			}
		}
	}

	/* (non-Javadoc)
	 * @see net.ysuga.rtsystem.profile.RTSProperties#getElement(java.lang.String, org.w3c.dom.Document)
	 */
	@Override
	/**
	 * XML data Save helper function
	 * @param elementName XML format element name
	 * @param document XML document object
	 */
	public Element getElement(String elementName, Document document) {
		Element element = createElement(elementName, document);
		element.appendChild(sourcePort.getElement("rts:sourceDataPort", document));
		element.appendChild(targetPort.getElement("rts:targetDataPort", document));
		return element;
	}

	public void connect() throws Exception {
		RTSystemBuilder.connect(this);
	}
	
	public void disconnect() throws Exception {
		RTSystemBuilder.disconnect(this);
	}

}
