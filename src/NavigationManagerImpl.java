	// -*- Java -*-
/*!
 * @file  MapperViewerImpl.java
 * @brief Mapper Viewer RTC
 * @date  $Date$
 *
 * $Id$
 */

import java.util.Calendar;

import jp.go.aist.rtm.RTC.DataFlowComponentBase;
import jp.go.aist.rtm.RTC.Manager;
import jp.go.aist.rtm.RTC.port.CorbaConsumer;
import jp.go.aist.rtm.RTC.port.CorbaPort;
import jp.go.aist.rtm.RTC.port.InPort;
import jp.go.aist.rtm.RTC.port.OutPort;
import jp.go.aist.rtm.RTC.util.DataRef;
import jp.go.aist.rtm.RTC.util.DoubleHolder;
import jp.go.aist.rtm.RTC.util.IntegerHolder;
import RTC.CameraImage;
import RTC.MAPPER_STATE;
import RTC.MAPPER_STATEHolder;
import RTC.OGMap;
import RTC.OGMapHolder;
import RTC.OGMapServer;
import RTC.OGMapper;
import RTC.Path2D;
import RTC.Path2DHolder;
import RTC.PathFollower;
import RTC.PathPlanParameter;
import RTC.PathPlanner;
import RTC.Point2D;
import RTC.Pose2D;
import RTC.RETURN_VALUE;
import RTC.RTObject;
import RTC.RangeData;
import RTC.ReturnCode_t;
import RTC.Time;
import RTC.TimedPose2D;
import RTC.TimedVelocity2D;
import RTC.Velocity2D;
import RTC.Waypoint2D;
import application.NavigationLogger;

/*!
 * @class MapperViewerImpl
 * @brief Mapper Viewer RTC
 *
 */
public class NavigationManagerImpl extends DataFlowComponentBase {

	//private MapperViewerFrame frame;

	private Application app;
	private Calendar m_lastReceivedTime;
	private float m_poseTimeout = (float) 3.0; // should be added config

	private NavigationLogger logger;

	/*
	 * !
	 * 
	 * @brief constructor
	 * 
	 * @param manager Maneger Object
	 */
	public NavigationManagerImpl(Manager manager) {
		super(manager);
		// <rtc-template block="initializer">
		
		System.out.println("NavigationManagerImpl.init().");
		m_currentPose_val = new TimedPose2D(new Time(0, 0), new Pose2D(
				new Point2D(0, 0), 0));
		System.out.println("CP1");
		m_currentPose = new DataRef<TimedPose2D>(m_currentPose_val);
		m_currentPoseIn = new InPort<TimedPose2D>("currentPose", m_currentPose);
		m_range_val = new RangeData();
		m_range = new DataRef<RangeData>(m_range_val);
		m_rangeIn = new InPort<RangeData>("range", m_range);
		m_path_val = new Path2D();
		m_path = new DataRef<Path2D>(m_path_val);
		m_pathIn = new InPort<Path2D>("path", m_path);
		m_camera_val = new CameraImage();
		m_camera = new DataRef<CameraImage>(m_camera_val);
		m_cameraIn = new InPort<CameraImage>("camera", m_camera);
		m_targetVelocity_val = new TimedVelocity2D(new RTC.Time(0, 0),
				new RTC.Velocity2D(0, 0, 0));
		m_targetVelocity = new DataRef<TimedVelocity2D>(m_targetVelocity_val);
		m_targetVelocityOut = new OutPort<TimedVelocity2D>("targetVelocity",
				m_targetVelocity);
		m_goal_val = new Waypoint2D();
		m_goal = new DataRef<Waypoint2D>(m_goal_val);
		m_goalOut = new OutPort<Waypoint2D>("goal", m_goal);
		m_mapperServicePort = new CorbaPort("mapperService");
		m_mapServerPort = new CorbaPort("mapServer");
		m_pathPlannerPort = new CorbaPort("pathPlanner");
		m_pathFollowerPort = new CorbaPort("pathFollower");
		// </rtc-template>

		System.out.println("Object created.");
		logger = new NavigationLogger();
	}

	/*
	 * !
	 * 
	 * The initialize action (on CREATED->ALIVE transition) formaer
	 * rtc_init_entry()
	 * 
	 * @return RTC::ReturnCode_t
	 */
	@Override
	protected ReturnCode_t onInitialize() {
		// Registration: InPort/OutPort/Service
		// <rtc-template block="registration">
		// Set InPort buffers

		System.out.println("NavigationManagerImpl.onInitialize");
		addInPort("currentPose", m_currentPoseIn);
		addInPort("range", m_rangeIn);
		//addInPort("path", m_pathIn);
		addInPort("camera", m_cameraIn);

		// Set OutPort buffer
		addOutPort("targetVelocity", m_targetVelocityOut);
		//addOutPort("goal", m_goalOut);

		// Set service consumers to Ports
		m_mapperServicePort.registerConsumer("OGMapper", "RTC::OGMapper",
				m_mapperBase);
		m_mapServerPort.registerConsumer("mapServer", "RTC::OGMapServer",
				m_OGMapServerBase);
		m_pathPlannerPort.registerConsumer("PathPlanner", "RTC::PathPlanner",
				m_pathPlannerBase);
		m_pathFollowerPort.registerConsumer("PathFollower",
				"RTC::PathFollower", m_pathFollowerBase);

		// Set CORBA Service Ports
		addPort(m_mapperServicePort);
		addPort(m_mapServerPort);
		addPort(m_pathPlannerPort);
		addPort(m_pathFollowerPort);

		// </rtc-template>
		bindParameter("debug", m_debug, "0");
		bindParameter("interval", m_interval, "1.0");
		bindParameter("pathDistanceTolerance", m_pathDistanceTolerance, "1.0");
		bindParameter("pathHeadingTolerance", m_pathHeadingTolerance, "1.0");

		//this.frame = new MapperViewerFrame(this, this.dataContainer);
		app = new Application(this);
		logger.info("Successfully Initialized");
		return super.onInitialize();
	}

	/***
	 * 
	 * The finalize action (on ALIVE->END transition) formaer
	 * rtc_exiting_entry()
	 * 
	 * @return RTC::ReturnCode_t
	 * 
	 * 
	 */
	// @Override
	// protected ReturnCode_t onFinalize() {
	// return super.onFinalize();
	// }

	/***
	 * 
	 * The startup action when ExecutionContext startup former
	 * rtc_starting_entry()
	 * 
	 * @param ec_id
	 *            target ExecutionContext Id
	 * 
	 * @return RTC::ReturnCode_t
	 * 
	 * 
	 */
	// @Override
	// protected ReturnCode_t onStartup(int ec_id) {
	// return super.onStartup(ec_id);
	// }

	/***
	 * 
	 * The shutdown action when ExecutionContext stop former
	 * rtc_stopping_entry()
	 * 
	 * @param ec_id
	 *            target ExecutionContext Id
	 * 
	 * @return RTC::ReturnCode_t
	 * 
	 * 
	 */
	// @Override
	// protected ReturnCode_t onShutdown(int ec_id) {
	// return super.onShutdown(ec_id);
	// }

	/***
	 * 
	 * The activated action (Active state entry action) former
	 * rtc_active_entry()
	 * 
	 * @param ec_id
	 *            target ExecutionContext Id
	 * 
	 * @return RTC::ReturnCode_t
	 * 
	 * 
	 */
	@Override
	protected ReturnCode_t onActivated(int ec_id) {
		m_lastReceivedTime = Calendar.getInstance();

		logger.info("Successfully Activated");
		app.activate();
		return super.onActivated(ec_id);
	}

	/***
	 * 
	 * The deactivated action (Active state exit action) former
	 * rtc_active_exit()
	 * 
	 * @param ec_id
	 *            target ExecutionContext Id
	 * 
	 * @return RTC::ReturnCode_t
	 * 
	 * 
	 */
	@Override
	protected ReturnCode_t onDeactivated(int ec_id) {

		logger.info("Successfully Deactivated");
		app.deactivate();
		return super.onDeactivated(ec_id);
	}

	/***
	 * 
	 * The execution action that is invoked periodically former rtc_active_do()
	 * 
	 * @param ec_id
	 *            target ExecutionContext Id
	 * 
	 * @return RTC::ReturnCode_t
	 * 
	 * 
	 */
	@Override
	protected ReturnCode_t onExecute(int ec_id) {
		Calendar currentTime = Calendar.getInstance();
		if (m_currentPoseIn.isNew()) {
			m_currentPoseIn.read();
			app.setRobotPose(m_currentPose.v);
		}

		if (m_rangeIn.isNew()) {
			m_rangeIn.read();
			app.dataContainer.setRangeData(m_range.v);
			m_lastReceivedTime = currentTime;
		} else {
			/*
			if (m_lastReceivedTime != null) {
				double duration = currentTime.getTimeInMillis()
						- m_lastReceivedTime.getTimeInMillis();
				if (duration > m_poseTimeout * 1000 && m_poseTimeout > 0) {
					// System.out.println("Range Data is Timeout to MapperViewer");
					logger.warning("Range Data Timeout");
					app.dataContainer.setRangeData(null);
					m_lastReceivedTime = null;
				}
			}
			*/
		}

		if (m_cameraIn.isNew()) {
			m_cameraIn.read();
			app.dataContainer.setCameraImage(m_camera.v);
		}

		if (m_targetVelocityOut.getConnectorProfiles().size() > 0) {
			app.joystickContainer.setVisible(true);
			if(!app.joystickContainer.isMute()) {
				Velocity2D v = app.joystickContainer.getTargetVelocity();
				this.m_targetVelocity.v.data.vx = v.vx;
				this.m_targetVelocity.v.data.vy = v.vy;
				this.m_targetVelocity.v.data.va = v.va;
				
				m_targetVelocityOut.setTimestamp(m_targetVelocity.v);
				app.dataContainer.setTargetVelocity(this.m_targetVelocity.v);
				m_targetVelocityOut.write();
			}
		} else {
			app.joystickContainer.setVisible(false);
		}
		return super.onExecute(ec_id);
	}

	/***
	 * 
	 * The aborting action when main logic error occurred. former
	 * rtc_aborting_entry()
	 * 
	 * @param ec_id
	 *            target ExecutionContext Id
	 * 
	 * @return RTC::ReturnCode_t
	 * 
	 * 
	 */
	// @Override
	// public ReturnCode_t onAborting(int ec_id) {
	// return super.onAborting(ec_id);
	// }

	/***
	 * 
	 * The error action in ERROR state former rtc_error_do()
	 * 
	 * @param ec_id
	 *            target ExecutionContext Id
	 * 
	 * @return RTC::ReturnCode_t
	 * 
	 * 
	 */
	// @Override
	public ReturnCode_t onError(int ec_id) {
		app.onError();
		return super.onError(ec_id);
	}

	/***
	 * 
	 * The reset action that is invoked resetting This is same but different the
	 * former rtc_init_entry()
	 * 
	 * @param ec_id
	 *            target ExecutionContext Id
	 * 
	 * @return RTC::ReturnCode_t
	 * 
	 * 
	 */
	@Override
	protected ReturnCode_t onReset(int ec_id) {
		return super.onReset(ec_id);
	}

	/***
	 * 
	 * The state update action that is invoked after onExecute() action no
	 * corresponding operation exists in OpenRTm-aist-0.2.0
	 * 
	 * @param ec_id
	 *            target ExecutionContext Id
	 * 
	 * @return RTC::ReturnCode_t
	 * 
	 * 
	 */
	// @Override
	// protected ReturnCode_t onStateUpdate(int ec_id) {
	// return super.onStateUpdate(ec_id);
	// }

	/***
	 * 
	 * The action that is invoked when execution context's rate is changed no
	 * corresponding operation exists in OpenRTm-aist-0.2.0
	 * 
	 * @param ec_id
	 *            target ExecutionContext Id
	 * 
	 * @return RTC::ReturnCode_t
	 * 
	 * 
	 */
	// @Override
	// protected ReturnCode_t onRateChanged(int ec_id) {
	// return super.onRateChanged(ec_id);
	// }
	//
	// Configuration variable declaration
	// <rtc-template block="config_declare">
	/*
	 * !
	 * 
	 * - Name: debug - DefaultValue: 0
	 */
	protected IntegerHolder m_debug = new IntegerHolder();
	/*
	 * !
	 * 
	 * - Name: interval - DefaultValue: 1.0
	 */
	protected DoubleHolder m_interval = new DoubleHolder();

	protected DoubleHolder m_pathDistanceTolerance = new DoubleHolder();

	protected DoubleHolder m_pathHeadingTolerance = new DoubleHolder();
	// </rtc-template>

	// DataInPort declaration
	// <rtc-template block="inport_declare">
	protected TimedPose2D m_currentPose_val;
	protected DataRef<TimedPose2D> m_currentPose;
	/*
	 * !
	 */
	protected InPort<TimedPose2D> m_currentPoseIn;

	protected RangeData m_range_val;
	protected DataRef<RangeData> m_range;
	/*
	 * !
	 */
	protected InPort<RangeData> m_rangeIn;

	protected Path2D m_path_val;
	protected DataRef<Path2D> m_path;
	/*
	 * !
	 */
	protected InPort<Path2D> m_pathIn;

	protected CameraImage m_camera_val;
	protected DataRef<CameraImage> m_camera;
	/*
	 * !
	 */
	protected InPort<CameraImage> m_cameraIn;

	// </rtc-template>

	// DataOutPort declaration
	// <rtc-template block="outport_declare">
	protected TimedVelocity2D m_targetVelocity_val;
	protected DataRef<TimedVelocity2D> m_targetVelocity;
	/*
	 * !
	 */
	protected OutPort<TimedVelocity2D> m_targetVelocityOut;

	protected Waypoint2D m_goal_val;
	protected DataRef<Waypoint2D> m_goal;
	/*
	 * !
	 */
	protected OutPort<Waypoint2D> m_goalOut;

	// </rtc-template>

	// CORBA Port declaration
	// <rtc-template block="corbaport_declare">
	/*
	 * !
	 */
	protected CorbaPort m_mapperServicePort;
	/*
	 * !
	 */
	protected CorbaPort m_mapServerPort;
	/*
	 * !
	 */
	protected CorbaPort m_pathPlannerPort;
	/*
	 * !
	 */
	protected CorbaPort m_pathFollowerPort;

	// </rtc-template>

	// Service declaration
	// <rtc-template block="service_declare">

	// </rtc-template>

	// Consumer declaration
	// <rtc-template block="consumer_declare">
	protected CorbaConsumer<OGMapper> m_mapperBase = new CorbaConsumer<OGMapper>(
			OGMapper.class);
	/*
	 * !
	 */
	protected OGMapper m_mapper;
	protected CorbaConsumer<OGMapServer> m_OGMapServerBase = new CorbaConsumer<OGMapServer>(
			OGMapServer.class);
	/*
	 * !
	 */
	protected OGMapServer m_OGMapServer;
	protected CorbaConsumer<PathPlanner> m_pathPlannerBase = new CorbaConsumer<PathPlanner>(
			PathPlanner.class);
	/*
	 * !
	 */
	protected PathPlanner m_pathPlanner;
	protected CorbaConsumer<PathFollower> m_pathFollowerBase = new CorbaConsumer<PathFollower>(
			PathFollower.class);
	/*
	 * !
	 */
	protected PathFollower m_pathFollower;

	// </rtc-template>

	/**
	 * requestMap
	 * 
	 * @return
	 */
	public OGMap requestMap() {
		logger.entering("MapperViewerImpl", "requestMap");
		if (this.get_context(0).get_component_state(this.m_objref) != RTC.LifeCycleState.ACTIVE_STATE) {
			return null;
		}

		
		OGMap map = new OGMap();
		OGMapHolder mapHolder = new OGMapHolder(map);
		try {
			if (m_mapperServicePort.get_connector_profiles().length != 0) {
				RETURN_VALUE retval;
				RTObject obj = null;
				if (m_mapperServicePort.get_connector_profiles()[0].ports[0].get_port_profile().name.equals(m_mapperServicePort.get_port_profile().name)) {
					obj = m_mapperServicePort.get_connector_profiles()[0].ports[1].get_port_profile().owner;
				} else {
					obj = m_mapperServicePort.get_connector_profiles()[0].ports[0].get_port_profile().owner;
				}
				if (obj.get_context(0).get_component_state(obj).equals(RTC.LifeCycleState.ACTIVE_STATE)) {
				retval = this.m_mapperBase._ptr().requestCurrentBuiltMap(mapHolder);
				if (this.m_mapperBase._ptr().requestCurrentBuiltMap(mapHolder) == RETURN_VALUE.RETVAL_OK) {
					return mapHolder.value;
				} else if (retval == RETURN_VALUE.RETVAL_ODOMETRY_TIME_OUT) {
					logger.warning("ERROR: Mobile Robot is timeout in Mapper RTC.");
				} else if (retval == RETURN_VALUE.RETVAL_RANGE_TIME_OUT) {
					logger.warning("ERROR: Range Sensor is timeout in Mapper RTC.");
				} else if (retval == RETURN_VALUE.RETVAL_ODOMETRY_INVALID_VALUE) {
					logger.warning("ERROR: Kobuki out of map range");
				}
				}	
			} else if (this.m_mapServerPort.get_connector_profiles().length != 0) {// dose
																					// it
																					// connected
																					// with
																					// MapServer?
				RETURN_VALUE retval;
				retval = this.m_OGMapServerBase._ptr().requestCurrentBuiltMap(
						mapHolder);
				if (retval == RETURN_VALUE.RETVAL_OK) {
					return mapHolder.value;
				} else if (retval == RETURN_VALUE.RETVAL_EMPTY_MAP) {
					logger.warning("ERROR: Empty Map");
				}
			}
		} catch (org.omg.CORBA.UNKNOWN e) {
			e.printStackTrace();
		} catch (org.omg.CORBA.OBJECT_NOT_EXIST e) {
			e.printStackTrace();
		}
		return null;
	}

	public boolean startMapping() {
		if (m_mapperServicePort.get_connector_profiles().length != 0) {
			if (this.m_mapperBase._ptr().startMapping() == RETURN_VALUE.RETVAL_OK) {
				return true;
			}
		}
		return false;
	}

	public boolean stopMapping() {
		if (m_mapperServicePort.get_connector_profiles().length != 0) {
			if (this.m_mapperBase._ptr().stopMapping() == RETURN_VALUE.RETVAL_OK) {
				return true;
			}
		}
		return false;
	}

	public MAPPER_STATE requestState() {
		MAPPER_STATE mapper_state = RTC.MAPPER_STATE.MAPPER_UNKNOWN;
		MAPPER_STATEHolder state = new MAPPER_STATEHolder(mapper_state);
		if (m_mapperServicePort.get_connector_profiles().length != 0) {
			if (this.m_mapperBase._ptr().getState(state) == RETURN_VALUE.RETVAL_OK) {
				return mapper_state;
			}
		}
		return MAPPER_STATE.MAPPER_UNKNOWN;
	}

	public int getInterval() {
		return (int) (m_interval.getValue() * 1000);
	}

	public Path2D planPath(PathPlanParameter param) {
		logger.entering("MapperViewerImpl", "planPath");
		Path2DHolder pathHolder = new Path2DHolder();

		param.currentPose = new RTC.Pose2D(new RTC.Point2D(
				this.m_currentPose.v.data.position.x,
				this.m_currentPose.v.data.position.y), m_currentPose.v.data.heading);
		param.map = requestMap();
		param.distanceTolerance = this.m_pathDistanceTolerance.getValue();
		param.headingTolerance = this.m_pathHeadingTolerance.getValue();

		if (m_pathPlannerPort.get_connector_profiles().length != 0) {// dose it
																		// connected
																		// with
																		// Mapper_MRPT?
			RETURN_VALUE retval;
			retval = this.m_pathPlannerBase._ptr().planPath(param, pathHolder);
			if (retval == RETURN_VALUE.RETVAL_OK) {
				logger.info("SUCCESS: Planning Success");
			} else if (retval == RETURN_VALUE.RETVAL_NOT_FOUND) {
				logger.warning("ERROR: Path Not Found");
			} else if (retval == RETURN_VALUE.RETVAL_INVALID_PARAMETER) {
				logger.warning("ERROR: Invalid Start or Goal coordinates");
			}
		}

		return pathHolder.value;
	}

	Path2D followingTargetPath;
	Thread followingTask;

	public void followPath(Path2D path) {
		followingTargetPath = path;

		followingTask = new Thread(new Runnable() {

			@Override
			public void run() {
				follow();
			}

		});

		followingTask.start();
	}

	private void follow() {
		Path2D path = followingTargetPath;
		logger.entering("MapperViewerImpl", "followPath()");
		if (m_pathFollowerPort.get_connector_profiles().length != 0) {// dose it
																		// connected
																		// with
																		// Mapper_MRPT?
			RETURN_VALUE retval;
			retval = this.m_pathFollowerBase._ptr().followPath(path);
			if (retval == RETURN_VALUE.RETVAL_OK) {
				logger.info("SUCCESS: FOLLOW SUCCESS");
				return;
			} else if (retval == RETURN_VALUE.RETVAL_EMERGENCY_STOP) {
				logger.warning("ERROR: FOLLOWING EMERGENCY STOP");
				return;
			} else if (retval == RETURN_VALUE.RETVAL_ODOMETRY_TIME_OUT) {
				logger.warning("ERROR: FOLLOWING Localization disconnected or Kobuki error");
				return;
			} else if (retval == RETURN_VALUE.RETVAL_ODOMETRY_INVALID_VALUE) {
				logger.warning("ERROR: FOLLOWING Localization sent Strange Value");
				return;
			} else {
				logger.warning("ERROR: FOLLOWING FAILED with UNKNOWN ERROR");
				return;
			}
		}
	}
	
}