/**
 * InvalidPreStateException.java
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
public class InvalidPreStateException extends Exception {

	/**
	 * Constructor
	 */
	public InvalidPreStateException() {
	}

	/**
	 * Constructor
	 * @param message
	 */
	public InvalidPreStateException(String message) {
		super(message);
	}

	/**
	 * Constructor
	 * @param cause
	 */
	public InvalidPreStateException(Throwable cause) {
		super(cause);
	}

	/**
	 * Constructor
	 * @param message
	 * @param cause
	 */
	public InvalidPreStateException(String message, Throwable cause) {
		super(message, cause);
	}

}
