/**
 * CorbaNamingInvalidContextException.java
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
public class CorbaNamingInvalidContextException extends Exception {

	/**
	 * Constructor
	 */
	public CorbaNamingInvalidContextException() {
	}

	/**
	 * Constructor
	 * @param message
	 */
	public CorbaNamingInvalidContextException(String message) {
		super(message);
	}

	/**
	 * Constructor
	 * @param cause
	 */
	public CorbaNamingInvalidContextException(Throwable cause) {
		super(cause);
	}

	/**
	 * Constructor
	 * @param message
	 * @param cause
	 */
	public CorbaNamingInvalidContextException(String message, Throwable cause) {
		super(message, cause);
	}

}
