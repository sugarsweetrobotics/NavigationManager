/**
 * CorbaNamingCannotFindException.java
 *
 * @author Yuki Suga (ysuga.net)
 * @date 2011/10/26
 * @copyright 2011, ysuga.net allrights reserved.
 *
 */
package ssr.nameservice;

/**
 *
 * @author ysuga
 *
 */
@SuppressWarnings("serial")
public class CorbaNamingCannotFindException extends Exception {

	/**
	 * Constructor
	 */
	public CorbaNamingCannotFindException() {
	}

	/**
	 * Constructor
	 * @param message
	 */
	public CorbaNamingCannotFindException(String message) {
		super(message);
	}

	/**
	 * Constructor
	 * @param cause
	 */
	public CorbaNamingCannotFindException(Throwable cause) {
		super(cause);
	}

	/**
	 * Constructor
	 * @param message
	 * @param cause
	 */
	public CorbaNamingCannotFindException(String message, Throwable cause) {
		super(message, cause);
	}

}
