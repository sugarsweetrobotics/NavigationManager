package application;
import java.util.logging.Logger;


public class NavigationLogger {

	Logger logger = Logger.getLogger("MapperViewer");
	
	public NavigationLogger() {}
	
	
	public void warning(String str) {}
	
	public void fine(String str) {}
	
	public void info(String str) {}
	
	public void entering(String name, String str) {}
	
	public void exitting(String name, String str) {}
	
}
