package application;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import RTC.Path2D;

public class PathUtil {

	static public void savePath(Path2D path, File file) {
		try {
			PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(
					file)));
			pw.write("[");
			for (int i = 0; i < path.waypoints.length; i++) {
				pw.write("{\n"
						+ "  target : ["
						+ path.waypoints[i].target.position.x + ", " + path.waypoints[i].target.position.y + ", " + path.waypoints[i].target.heading
						+ "],\n"
						+ "  distanceTolerance : " 
						+ path.waypoints[i].distanceTolerance
						+ ",\n"
						+ "  headingTolerance : " 
						+ path.waypoints[i].headingTolerance
						+ ",\n"
						+ "  timeLimit : [" 
						+ path.waypoints[i].timeLimit.sec + ", " +  path.waypoints[i].timeLimit.nsec
						+ "],\n"
						+ "  maxSpeed : ["
						+ path.waypoints[i].maxSpeed.position.x + ", " + path.waypoints[i].maxSpeed.position.y + ", " + path.waypoints[i].maxSpeed.heading
						+ "]\n"
						+ "}\n"
				);
				if(i != path.waypoints.length-1) {
					pw.write(",");
				}
			}
			pw.write("]");
			pw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
