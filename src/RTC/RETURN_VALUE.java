package RTC;


/**
* RTC/RETURN_VALUE.java .
* IDL-to-Javaコンパイラ(ポータブル)、バージョン"3.2"によって生成されました
* idl/MobileRobot.idlから
* 2015年1月18日 16時11分22秒 JST
*/

public class RETURN_VALUE implements org.omg.CORBA.portable.IDLEntity
{
  private        int __value;
  private static int __size = 14;
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
  public static final int _RETVAL_NOT_FOUND = 6;
  public static final RTC.RETURN_VALUE RETVAL_NOT_FOUND = new RTC.RETURN_VALUE(_RETVAL_NOT_FOUND);
  public static final int _RETVAL_EMERGENCY_STOP = 7;
  public static final RTC.RETURN_VALUE RETVAL_EMERGENCY_STOP = new RTC.RETURN_VALUE(_RETVAL_EMERGENCY_STOP);
  public static final int _RETVAL_CURRENT_POSE_INVALID_VALUE = 8;
  public static final RTC.RETURN_VALUE RETVAL_CURRENT_POSE_INVALID_VALUE = new RTC.RETURN_VALUE(_RETVAL_CURRENT_POSE_INVALID_VALUE);
  public static final int _RETVAL_CURRENT_POSE_TIME_OUT = 9;
  public static final RTC.RETURN_VALUE RETVAL_CURRENT_POSE_TIME_OUT = new RTC.RETURN_VALUE(_RETVAL_CURRENT_POSE_TIME_OUT);
  public static final int _RETVAL_ODOMETRY_INVALID_VALUE = 10;
  public static final RTC.RETURN_VALUE RETVAL_ODOMETRY_INVALID_VALUE = new RTC.RETURN_VALUE(_RETVAL_ODOMETRY_INVALID_VALUE);
  public static final int _RETVAL_ODOMETRY_TIME_OUT = 11;
  public static final RTC.RETURN_VALUE RETVAL_ODOMETRY_TIME_OUT = new RTC.RETURN_VALUE(_RETVAL_ODOMETRY_TIME_OUT);
  public static final int _RETVAL_RANGE_INVALID_VALUE = 12;
  public static final RTC.RETURN_VALUE RETVAL_RANGE_INVALID_VALUE = new RTC.RETURN_VALUE(_RETVAL_RANGE_INVALID_VALUE);
  public static final int _RETVAL_RANGE_TIME_OUT = 13;
  public static final RTC.RETURN_VALUE RETVAL_RANGE_TIME_OUT = new RTC.RETURN_VALUE(_RETVAL_RANGE_TIME_OUT);

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
