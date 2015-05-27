package application;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

import RTC.TimedPose2D;


public class Pose2DLogger {

	File file;
	PrintWriter printWriter;
	FileInputStream fis;
	public Pose2DLogger(File file) throws FileNotFoundException {
		this.file = file;
		this.fis = new FileInputStream(file);
	}
	
	
	public void log(TimedPose2D pose) {
		
	}
}
