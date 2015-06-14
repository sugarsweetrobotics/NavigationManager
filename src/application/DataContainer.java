package application;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import RTC.CameraImage;
import RTC.OGMap;
import RTC.Path2D;
import RTC.Point2D;
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
	public ArrayList<PoseLog> poseLogs = new ArrayList<PoseLog>();

	
	public class PoseLog {
		public ArrayList<TimedPose2D> poses = new ArrayList<TimedPose2D>();
		public File file;

		public PoseLog(File file) throws IOException {
			this.file = file;
			BufferedReader br = new BufferedReader(new FileReader(file));
			while (br.ready()) {
				String line = br.readLine();
				String[] tokens = line.split(",");
				if (tokens.length != 5) {
					throw new IOException();
				}
				
				int sec = Integer.parseInt(tokens[0].trim());
				int nsec = Integer.parseInt(tokens[1].trim());
				double x = Double.parseDouble(tokens[2].trim());
				double y = Double.parseDouble(tokens[3].trim());
				double phi = Double.parseDouble(tokens[4].trim());
				
				poses.add(new TimedPose2D(new RTC.Time(sec, nsec), new Pose2D(new Point2D(x, y), phi)));
			}
		}
	}

	
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

	public void loadRobotLogFile(File robotLogFile) throws IOException {
		poseLogs.add(new PoseLog(robotLogFile));

	}
}
