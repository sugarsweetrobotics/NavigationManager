/**
 * RTSProfileHolder.java
 *
 * @author Yuki Suga (ysuga.net)
 * @date 2011/10/24
 * @copyright 2011, ysuga.net allrights reserved.
 *
 */
package ssr.nameservice.ui;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;

import ssr.nameservice.CorbaNamingParser;
import ssr.nameservice.RTNamingContext;
import ssr.rtsbuilder.RTSystemBuilder;
import ssr.rtsprofile.RTSystemProfile;

/**
 * 
 * @author ysuga
 * 
 */
@SuppressWarnings("serial")
public class RTSProfileHolder extends HashMap<String, RTSystemProfile> implements Runnable {

	public static final String ONLINE = "online";

	private Set<String> namingAddressSet;

	public void addNamingAddress(String namingAddress) {
		namingAddressSet.add(namingAddress);
	}

	private static RTSProfileHolder instance;
	
	static {
		instance = new RTSProfileHolder();
	}

	static public RTSProfileHolder getInstance() {
       return instance;
	}

	private boolean endflag;
	
	public void add(RTSystemProfile profile) {
		put(profile.getName(), profile);
	}

	ExecutorService executor;
	/**
	 * Constructor
	 */
	RTSProfileHolder() {
		super();
		namingAddressSet = new HashSet<String>();

		add(new RTSystemProfile(RTSProfileHolder.ONLINE, "defaultVendor", "1.0"));
//		executor = Executors.newSingleThreadExecutor();
//		executor.execute(this);
	}

	public void refreshAll() {
		for (String address : namingAddressSet) {
			RTNamingContext nc;
			try {
				nc = CorbaNamingParser.buildRTNamingContext(address);

				RTSystemProfile onlineProfile = RTSProfileHolder.getInstance()
						.get(RTSProfileHolder.ONLINE);
				onlineProfile.addAllComponent(nc);
			} catch (Exception e) {
				
				e.printStackTrace();
			}
		}
		
		for(RTSystemProfile rtsProfile:this.values()) {
			try {
				RTSystemBuilder.downwardSynchronization(rtsProfile);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public void run() {
		while(!endflag) {
			System.out.println("Refresh");
			refreshAll();
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
