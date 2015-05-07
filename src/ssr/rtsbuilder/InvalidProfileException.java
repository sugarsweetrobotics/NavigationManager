/**
 * InvalidProfileException.java
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
public class InvalidProfileException extends Exception {

	/**
	 * Constructor
	 */
	public InvalidProfileException() {
	}

	/**
	 * Constructor
	 * @param message
	 */
	public InvalidProfileException(String message) {
		super(message);
	}

	/**
	 * Constructor
	 * @param cause
	 */
	public InvalidProfileException(Throwable cause) {
		super(cause);
	}

	/**
	 * Constructor
	 * @param message
	 * @param cause
	 */
	public InvalidProfileException(String message, Throwable cause) {
		super(message, cause);
	}

}
