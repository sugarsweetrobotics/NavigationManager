package application;
import RTC.CameraImage;
import RTC.OGMap;
import RTC.Path2D;
import RTC.Pose2D;
import RTC.RangeData;
import RTC.TimedPose2D;
import RTC.TimedVelocity2D;


public class DataContainer {

	private TimedPose2D currentPose;// = new TimedPose2D(new RTC.Time(0,0), new RTC.Pose2D(new RTC.Point2D(0,0), 0));
	private RangeData rangeData;// = new RangeData(new RTC.Timed(0,0));
	private CameraImage cameraImage;
	private TimedVelocity2D targetVelocity;
	private OGMap map;
	private Pose2D goal;
	private Path2D path;
	
	public DataContainer() {
		
	}

	public void setGoal(Pose2D goal) {
		this.goal = goal;
	}
	
	public Pose2D getGoal() {
		return this.goal;
	}
	
	public void setMap(OGMap map) {
		this.map = map;
	}
	
	public OGMap getMap() {
		return this.map;
	}
	
	public void setRobotPose(TimedPose2D v) {
		currentPose = v;
	}

	public TimedPose2D getCurrentPose() {
		return currentPose;
	}
	
	public void setRangeData(RangeData v) {
		rangeData = v;
	}

	public void setCameraImage(CameraImage v) {
		cameraImage = v;
	}

	public void setTargetVelocity(TimedVelocity2D v) {
		targetVelocity = v;
	}

	public void setPath(Path2D path) {
		this.path = path;
	}
	
	public Path2D getPath() {
		return path;
	}

	public RangeData getRangeData() {
		return rangeData;
	}
}
