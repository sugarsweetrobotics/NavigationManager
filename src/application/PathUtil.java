package application;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;

import org.ho.yaml.Yaml;

import RTC.Path2D;
import RTC.Point2D;
import RTC.Pose2D;
import RTC.Waypoint2D;

public class PathUtil {

	static public void savePath(Path2D path, File file) {
		try {
			PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(
					file)));
			for (int i = 0; i < path.waypoints.length; i++) {
				pw.write("- target : ["
						+ path.waypoints[i].target.position.x + ", " + path.waypoints[i].target.position.y + ", " + path.waypoints[i].target.heading
						+ "]\n"
						+ "  distanceTolerance : " 
						+ path.waypoints[i].distanceTolerance
						+ "\n"
						+ "  headingTolerance : " 
						+ path.waypoints[i].headingTolerance
						+ "\n"
						+ "  timeLimit : [" 
						+ path.waypoints[i].timeLimit.sec + ", " +  path.waypoints[i].timeLimit.nsec
						+ "]\n"
						+ "  maxSpeed : ["
						+ path.waypoints[i].maxSpeed.position.x + ", " + path.waypoints[i].maxSpeed.position.y + ", " + path.waypoints[i].maxSpeed.heading
						+ "]\n"
				);
			}
			pw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	static public Path2D loadPath(File yamlfile) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(yamlfile));
		StringBuffer strBuf = new StringBuffer();
		while(true) {
			String r = br.readLine();
			if (r == null) {
				break;
			}
			strBuf.append(r);
			strBuf.append("\n");
			System.out.println(r);
		}
		br.close();
		
		ArrayList<HashMap<String, Object>> param = (ArrayList<HashMap<String, Object>>)Yaml.load(strBuf.toString());//load(new FileReader(f));
		Path2D path = new Path2D();
		path.waypoints = new Waypoint2D[param.size()];
		for(int i = 0;i < param.size();i++) {
			HashMap<String, Object> w = (HashMap<String, Object>)param.get(i);
			ArrayList<Double> target = (ArrayList<Double>)w.get("target");
			double x = target.get(0).doubleValue();
			double y = target.get(1).doubleValue();
			double phi = target.get(2).doubleValue();
			Double distanceTolerance = (Double)w.get("distanceTolerance");
			Double headingTolerance = (Double)w.get("headingTolerance");
			ArrayList<Integer> timeLimit = (ArrayList<Integer>)w.get("timeLimit");
			int sec = timeLimit.get(0).intValue();
			int nsec = timeLimit.get(1).intValue();
			ArrayList<Double> maxSpeed = (ArrayList<Double>)w.get("maxSpeed");
			double maxX = maxSpeed.get(0).doubleValue();
			double maxY= maxSpeed.get(1).doubleValue();
			double maxPhi = maxSpeed.get(2).doubleValue();
			
			path.waypoints[i] = new Waypoint2D(new Pose2D(new Point2D(x, y), phi), 
					distanceTolerance.doubleValue(), headingTolerance.doubleValue(),
					new RTC.Time(sec, nsec), new Pose2D(new Point2D(maxX, maxY), maxPhi));
			
		}
		
		return path;
	}
}
