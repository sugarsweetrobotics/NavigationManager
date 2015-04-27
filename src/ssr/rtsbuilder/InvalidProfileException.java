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
		// TODO 閾ｪ蜍慕函謌舌＆繧後◆繧ｳ繝ｳ繧ｹ繝医Λ繧ｯ繧ｿ繝ｼ繝ｻ繧ｹ繧ｿ繝�
	}

	/**
	 * Constructor
	 * @param message
	 */
	public InvalidProfileException(String message) {
		super(message);
		// TODO 閾ｪ蜍慕函謌舌＆繧後◆繧ｳ繝ｳ繧ｹ繝医Λ繧ｯ繧ｿ繝ｼ繝ｻ繧ｹ繧ｿ繝�
	}

	/**
	 * Constructor
	 * @param cause
	 */
	public InvalidProfileException(Throwable cause) {
		super(cause);
		// TODO 閾ｪ蜍慕函謌舌＆繧後◆繧ｳ繝ｳ繧ｹ繝医Λ繧ｯ繧ｿ繝ｼ繝ｻ繧ｹ繧ｿ繝�
	}

	/**
	 * Constructor
	 * @param message
	 * @param cause
	 */
	public InvalidProfileException(String message, Throwable cause) {
		super(message, cause);
		// TODO 閾ｪ蜍慕函謌舌＆繧後◆繧ｳ繝ｳ繧ｹ繝医Λ繧ｯ繧ｿ繝ｼ繝ｻ繧ｹ繧ｿ繝�
	}

}
