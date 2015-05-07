/**
 * ZombieRTObjectException.java
 *
 * @author Yuki Suga (ysuga.net)
 * @date 2011/10/26
 * @copyright 2011, ysuga.net allrights reserved.
 *
 */
package ssr.rtsbuilder;

/**
 *
 * @author ysuga
 *
 */
@SuppressWarnings("serial")
public class ZombieRTObjectException extends Exception {

	/**
	 * Constructor
	 */
	public ZombieRTObjectException() {
	}

	/**
	 * Constructor
	 * @param message
	 */
	public ZombieRTObjectException(String message) {
		super(message);
	}

	/**
	 * Constructor
	 * @param cause
	 */
	public ZombieRTObjectException(Throwable cause) {
		super(cause);
	}

	/**
	 * Constructor
	 * @param message
	 * @param cause
	 */
	public ZombieRTObjectException(String message, Throwable cause) {
		super(message, cause);
	}

}
