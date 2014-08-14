package RTC;


/**
* RTC/RETURN_VALUE.java .
* IDL-to-Javaコンパイラ(ポータブル)、バージョン"3.2"によって生成されました
* idl/MobileRobot.idlから
* 2014年8月11日 11時46分06秒 JST
*/

public class RETURN_VALUE implements org.omg.CORBA.portable.IDLEntity
{
  private        int __value;
  private static int __size = 6;
  private static RTC.RETURN_VALUE[] __array = new RTC.RETURN_VALUE [__size];

  public static final int _RETVAL_OK = 0;
  public static final RTC.RETURN_VALUE RETVAL_OK = new RTC.RETURN_VALUE(_RETVAL_OK);
  public static final int _RETVAL_INVALID_PARAMETER = 1;
  public static final RTC.RETURN_VALUE RETVAL_INVALID_PARAMETER = new RTC.RETURN_VALUE(_RETVAL_INVALID_PARAMETER);
  public static final int _RETVAL_EMPTY_MAP = 2;
  public static final RTC.RETURN_VALUE RETVAL_EMPTY_MAP = new RTC.RETURN_VALUE(_RETVAL_EMPTY_MAP);
  public static final int _RETVAL_INVALID_PRECONDITION = 3;
  public static final RTC.RETURN_VALUE RETVAL_INVALID_PRECONDITION = new RTC.RETURN_VALUE(_RETVAL_INVALID_PRECONDITION);
  public static final int _RETVAL_NOT_IMPL = 4;
  public static final RTC.RETURN_VALUE RETVAL_NOT_IMPL = new RTC.RETURN_VALUE(_RETVAL_NOT_IMPL);
  public static final int _RETVAL_UNKNOWN_ERROR = 5;
  public static final RTC.RETURN_VALUE RETVAL_UNKNOWN_ERROR = new RTC.RETURN_VALUE(_RETVAL_UNKNOWN_ERROR);

  public int value ()
  {
    return __value;
  }

  public static RTC.RETURN_VALUE from_int (int value)
  {
    if (value >= 0 && value < __size)
      return __array[value];
    else
      throw new org.omg.CORBA.BAD_PARAM ();
  }

  protected RETURN_VALUE (int value)
  {
    __value = value;
    __array[__value] = this;
  }
} // class RETURN_VALUE
