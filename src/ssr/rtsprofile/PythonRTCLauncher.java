/**
 * PythonRTCLauncher.java
 *
 * @author Yuki Suga (ysuga.net)
 * @date 2011/10/11
 * @copyright 2011, ysuga.net allrights reserved.
 *
 */

package ssr.rtsprofile;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ysuga
 *
 */
public class PythonRTCLauncher implements Runnable {

	private Thread thread;
	
	private File workingDir;
	
	private File pythonFile;

	private ProcessBuilder processBuilder;

	private Process process;

	private InputStream inputStream;
	
	public InputStream getInputStream() {return inputStream;}
	public Process getProcess() {
		return process;
	}
	
	public boolean isAlive() {
		try {
			process.exitValue();
			return false;
		} catch (IllegalThreadStateException e) {
			return true;
		}
	}
	
	
	public PythonRTCLauncher(File workingDir, File file) {
		this.workingDir = workingDir;
		this.pythonFile = file;
	}
	
	
	public void execute() {
		List<String> arg = new ArrayList<String>();
		arg.add("python");
		arg.add("-u");
		arg.add(pythonFile.getAbsolutePath());
		processBuilder = new ProcessBuilder(arg);
		processBuilder.directory(workingDir);
		try {
			process = processBuilder.start();

		} catch (IOException e) {
			// TODO 閾ｪ蜍慕函謌舌＆繧後◆ catch 繝悶Ο繝�け
			e.printStackTrace();
		}
	}
	
	public void terminate() {
		process.destroy();
	}
	
	public void join() throws InterruptedException {
		thread.join();
	}
	
	/*
	 * 縺薙％縺ｧ繧ｹ繝ｬ繝�ラ繧堤ｫ九■荳翫￡縺ｦ�檎ｵゆｺ�凾縺ｫ縺ｯjoin縺吶ｋ繧医≧縺ｫ縺励↑縺�→繝､繝舌う��
	 */
	public void run()  {

	}
}
