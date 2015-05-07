/**
 * CorbaNamingResolveException.java
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
public class CorbaNamingResolveException extends Exception {

	/**
	 * Constructor
	 */
	public CorbaNamingResolveException() {
	}

	/**
	 * Constructor
	 * @param message
	 */
	public CorbaNamingResolveException(String message) {
		super(message);
	}

	/**
	 * Constructor
	 * @param cause
	 */
	public CorbaNamingResolveException(Throwable cause) {
		super(cause);
	}

	/**
	 * Constructor
	 * @param message
	 * @param cause
	 */
	public CorbaNamingResolveException(String message, Throwable cause) {
		super(message, cause);
	}

}
