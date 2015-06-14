// -*- Java -*-
/*!
 * @file NavigationManager.java
 * @date $Date$
 *
 * $Id$
 */

import jp.go.aist.rtm.RTC.Manager;
import jp.go.aist.rtm.RTC.RTObject_impl;
import jp.go.aist.rtm.RTC.RegisterModuleFunc;
import jp.go.aist.rtm.RTC.RtcDeleteFunc;
import jp.go.aist.rtm.RTC.RtcNewFunc;
import jp.go.aist.rtm.RTC.util.Properties;

/*!
 * @class NavigationManager
 * @brief Mapper Viewer RTC
 */
public class NavigationManager implements RtcNewFunc, RtcDeleteFunc, RegisterModuleFunc {

//  Module specification
//  <rtc-template block="module_spec">
    public static String component_conf[] = {
    	    "implementation_id", "NavigationManager",
    	    "type_name",         "NavigationManager",
    	    "description",       "Mapper Viewer RTC",
    	    "version",           "1.0.1",
    	    "vendor",            "Sugar Sweet Robotics",
    	    "category",          "Navigation",
    	    "activity_type",     "STATIC",
    	    "max_instance",      "1",
    	    "language",          "Java",
    	    "lang_type",         "compile",
            // Configuration variables
            "conf.default.debug", "0",
            // Widget
            "conf.__widget__.debug", "text",
            // Constraints
    	    ""
            };
//  </rtc-template>

    public RTObject_impl createRtc(Manager mgr) {
        return new NavigationManagerImpl(mgr);
    }

    public void deleteRtc(RTObject_impl rtcBase) {
        rtcBase = null;
    }
    public void registerModule() {
        Properties prop = new Properties(component_conf);
        final Manager manager = Manager.instance();
        manager.registerFactory(prop, new NavigationManager(), new NavigationManager());
    }
}
