package ssr;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.logging.Logger;

import org.omg.CosNaming.Binding;
import org.omg.CosNaming.BindingIteratorHolder;
import org.omg.CosNaming.BindingListHolder;
import org.omg.CosNaming.BindingType;
import org.omg.CosNaming.NamingContext;

import ssr.rtsbuilder.RTSystemBuilder;

import jp.go.aist.rtm.RTC.CorbaNaming;
import jp.go.aist.rtm.RTC.Manager;
import jp.go.aist.rtm.RTC.RTObject_impl;
import jp.go.aist.rtm.RTC.util.CORBA_SeqUtil;
import jp.go.aist.rtm.RTC.util.NVUtil;
import jp.go.aist.rtm.RTC.util.ORBUtil;
import RTC.ConnectorProfile;
import RTC.ConnectorProfileHolder;
import RTC.PortInterfaceProfile;
import RTC.PortService;
import RTC.RTObject;
import _SDOPackage.NVListHolder;
import _SDOPackage.NameValue;

public class RTMHelper {

	private static Logger logger;
	private static Map<String, String> defaultDataPortConnectionProperties;
	
	static {
		logger = Logger.getLogger("MapperViewer");
		
		defaultDataPortConnectionProperties = new HashMap<String, String>();
		defaultDataPortConnectionProperties.put("dataport.interface_type", "corba_cdr");
		defaultDataPortConnectionProperties.put("dataport.dataflow_type", "push");
		defaultDataPortConnectionProperties.put("dataport.subscription_type", "flush");
	}
	

	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	public static Set<String> getRTObjectPathUriSet(String hostAddress)
			throws Exception {
		Set<String> pathUriSet = new HashSet<String>();
		BindingListHolder bl = new BindingListHolder();
		BindingIteratorHolder bi = new BindingIteratorHolder();

		String namingUri = hostAddress;
		StringTokenizer tokenizer2 = new StringTokenizer(namingUri, ":");
		if (tokenizer2.countTokens() == 1) {
			namingUri = namingUri + ":2809";
		}
		CorbaNaming corbaNaming = new CorbaNaming(ORBUtil.getOrb(), namingUri);

		NamingContext namingContext = corbaNaming.getRootContext();
		namingContext.list(30, bl, bi);
		for (Binding binding : bl.value) {
			String bindingName = binding.binding_name[0].id + "."
					+ binding.binding_name[0].kind;

			if (binding.binding_type == BindingType.ncontext) {
				Set<String> childSet = getRTObjectPathUriSetSub((NamingContext) namingContext
						.resolve(binding.binding_name));
				for (String childPathUri : childSet) {
					pathUriSet.add(hostAddress + "/" + bindingName + "/"
							+ childPathUri);
				}
			} else {
				pathUriSet.add(hostAddress + "/" + bindingName);
			}
		}
		return pathUriSet;
	}

	/**
	 * 
	 * @param namingContext
	 * @return
	 * @throws Exception
	 */
	protected static Set<String> getRTObjectPathUriSetSub(
			NamingContext namingContext) throws Exception {
		Set<String> pathUriSet = new HashSet<String>();
		BindingListHolder bl = new BindingListHolder();
		BindingIteratorHolder bi = new BindingIteratorHolder();
		namingContext.list(30, bl, bi);

		for (Binding binding : bl.value) {
			String bindingName = binding.binding_name[0].id + "."
					+ binding.binding_name[0].kind;

			if (binding.binding_type == BindingType.ncontext) {
				Set<String> childSet = getRTObjectPathUriSetSub((NamingContext) namingContext
						.resolve(binding.binding_name));
				for (String childPathUri : childSet) {
					pathUriSet.add(bindingName + "/" + childPathUri);
				}
			} else {
				pathUriSet.add(bindingName);
			}
		}
		return pathUriSet;
	}

	
	public static Set<RTObject> getRegisteredRTObjectSet(String hostAddress)
			throws Exception {
		Set<RTObject> componentSet = new HashSet<RTObject>();
		Set<String> componentUriSet = getRTObjectPathUriSet(hostAddress);

		String namingUri = hostAddress;
		StringTokenizer tokenizer2 = new StringTokenizer(namingUri, ":");
		if (tokenizer2.countTokens() == 1) {
			namingUri = namingUri + ":2809";
		}
		for (String uri : componentUriSet) {
			RTObject component = RTSystemBuilder.getComponent(uri);
			if (component != null) {
				componentSet.add(component);
			}
		}
		return componentSet;
	}
	
	public static String getPathUriOfPort(PortService ps) {
		String name = ps.get_port_profile().name;
		if(name.split("\\.").length > 1) {
			name = name.split("\\.")[1];
		}
		return RTSystemBuilder.getPathUri(ps.get_port_profile().owner) + ':' + name;
	}
	
	
	public static Set<PortService> getRegisteredPortServiceSet(String hostAddress) throws Exception {
		Set<PortService> portSet = new HashSet<PortService>();
		Set<RTObject> compSet = getRegisteredRTObjectSet(hostAddress);
		for(RTObject comp : compSet) {
			for(PortService ps : comp.get_ports()) {
				portSet.add(ps);
			}
		}
		return portSet;
	}
	
	public static Map<String, String> getDefaultDataPortConnectionProperties() {
		return defaultDataPortConnectionProperties;
	}
	
	public static String getDataPortDataType(PortService port) {
		for(NameValue nv : port.get_port_profile().properties) {
			if(nv.name.equals("dataport.data_type")) {
				return nv.value.extract_string();
			}
		}
		return null;
	}


	static public boolean isConnectable(PortService ps1, PortService ps2) {
		String ps1DataType = null;
		String ps1PortType = null;
		String ps2DataType = null;
		String ps2PortType = null;
		for(NameValue nv : ps1.get_port_profile().properties) {
			if (nv.name.equals("dataport.data_type")) {
				ps1DataType = nv.value.extract_string();
			}
			if (nv.name.equals("port.port_type")) {
				if (nv.value.extract_string().equals("DataOutPort")) { 
					ps1PortType = "outport";
				} else if (nv.value.extract_string().equals("DataInPort")) {
				    ps1PortType = "inport";
				} else {
				    ps1PortType = "svcport";
				}
			}
		}
		
		for(NameValue nv : ps2.get_port_profile().properties) {
			if (nv.name.equals("dataport.data_type")) {
				ps2DataType = nv.value.extract_string();
			}
			if (nv.name.equals("port.port_type")) {
				if (nv.value.extract_string().equals("DataOutPort")) { 
					ps2PortType = "outport";
				} else if (nv.value.extract_string().equals("DataInPort")) {
				    ps2PortType = "inport";
				} else {
				    ps2PortType = "svcport";
				}
			}
		}
		
		if ( (ps1PortType.equals("outport") && ps2PortType.equals("inport")) ||
			 (ps1PortType.equals("inport") && ps2PortType.equals("outport")) ) {
			if(ps1DataType.equals(ps2DataType)) {
				return true;
			}
		} else if( ps1PortType.equals("svcport") && ps2PortType.equals("svcport")) {
			for(PortInterfaceProfile pi1 : ps1.get_port_profile().interfaces) {
				for(PortInterfaceProfile pi2 : ps2.get_port_profile().interfaces) {
					if (pi1.type_name.equals(pi2.type_name)) {
						if(pi1.polarity != pi2.polarity) {
							return true;
						}
					}
				}
			}
		}
		
		
				
		return false;
	}
	
	public static List<PortService> searchPortServicesFromType(String componentTypeName, String portName) {
		List<RTObject_impl> rtos = searchRTObjectsFromType(componentTypeName);
		ArrayList<PortService> ports = new ArrayList<PortService>();
		for(RTObject_impl rto : rtos) {
			if(rto.get_component_profile().type_name.equals(componentTypeName)) {
				for(PortService ps: rto.get_ports()) {
					String name = ps.get_port_profile().name;
					if(name.split("\\.").length > 1) {
						name = name.split("\\.")[1];
					}
					if(name.equals(portName)) {
						ports.add(ps);
					}
				}
			}
		}
		return ports;
	}
	
	public static List<RTObject_impl> searchRTObjectsFromType(String componentTypeName) {
		Manager mgr = Manager.instance();
		ArrayList<RTObject_impl> rtos = new ArrayList<RTObject_impl>();
		for(RTObject_impl rto : mgr.getComponents()) {
			if(rto.get_component_profile().type_name.equals(componentTypeName)) {
				rtos.add(rto);
			}
		}
		return rtos;
	}
	
	public static void connectPorts(PortService port0, PortService port1) {
		connectPorts(port0, port1, null);
	}
	
	public static void connectPorts(PortService port0, PortService port1,
			Map<String, String> properties) {
		String defaultId = port0.get_port_profile().name + "_"
				+ port1.get_port_profile().name;
		logger.info("Connecting " + defaultId );
		ConnectorProfile prof = new ConnectorProfile();
		if(properties != null) {
			prof.connector_id = properties.containsKey("id") ? properties.get("id") : defaultId;
			prof.name = properties.containsKey("name") ? properties.get("name") : defaultId;
		} else {
			prof.connector_id = defaultId;
			prof.name = defaultId;
		}
		prof.ports = new PortService[2];
		prof.ports[0] = port0;
		prof.ports[1] = port1;
		NVListHolder nvholder = new NVListHolder();
		nvholder.value = prof.properties;
		if (nvholder.value == null)
			nvholder.value = new NameValue[0];
		if(properties != null) {
			for(String key : properties.keySet()) {
				if (key != "id" && key != "name") {
					CORBA_SeqUtil.push_back(nvholder, NVUtil.newNVString(
								key, properties.get(key)));
				}
			}
		}
		prof.properties = nvholder.value;
		ConnectorProfileHolder proflist = new ConnectorProfileHolder();
		proflist.value = prof;
		port0.connect(proflist);
		logger.info("Successfully Connected.");
	}
}
