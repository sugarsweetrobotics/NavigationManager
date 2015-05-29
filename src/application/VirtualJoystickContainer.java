package application;
import RTC.Velocity2D;


public class VirtualJoystickContainer {
	
	private JoyFrame joyFrame;
	
	public VirtualJoystickContainer() {
		joyFrame = new JoyFrame();
	}
	
	
	public void setVisible(boolean flag) {
		if(joyFrame.isVisible() != flag) {
			joyFrame.setVisible(flag);
		}
	}
	
	public Velocity2D getTargetVelocity() {
		return getTargetVelocityEx();
	}
	
	public Velocity2D getTargetVelocityEx() {
		double dx = joyFrame.getDX();
		double dy = joyFrame.getDY();

		double tvel = joyFrame.getTranslationVelocity();
		double rvel = joyFrame.getRotationVelocity();
		
		return new Velocity2D(dx * tvel, 0, dy * rvel);
	}
	
	public Velocity2D getTargetVelocityOld() {
		int state = joyFrame.getState();
		double vx = 0;
		double vy = 0;
		double va = 0;
		double tvel = joyFrame.getTranslationVelocity();
		double rvel = joyFrame.getRotationVelocity();
		switch (state) {
		case JoyFrame.UP:
			vx = tvel;
			break;
		case JoyFrame.DOWN:
			vx = -tvel;
			break;
		case JoyFrame.LEFT:
			va = rvel;
			break;
		case JoyFrame.RIGHT:
			va = -rvel;
			break;
		case JoyFrame.DEF:
			break;
		default:
			break;
		}
		
		return new Velocity2D(vx, vy, va);
	}
	
}
