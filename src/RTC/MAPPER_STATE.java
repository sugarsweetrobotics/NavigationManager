package RTC;


/**
* RTC/MAPPER_STATE.java .
* IDL-to-Javaコンパイラ(ポータブル)、バージョン"3.2"によって生成されました
* idl/MobileRobot.idlから
* 2014年8月4日 11時36分28秒 JST
*/

public class MAPPER_STATE implements org.omg.CORBA.portable.IDLEntity
{
  private        int __value;
  private static int __size = 5;
  private static RTC.MAPPER_STATE[] __array = new RTC.MAPPER_STATE [__size];

  public static final int _MAPPER_STOPPED = 0;
  public static final RTC.MAPPER_STATE MAPPER_STOPPED = new RTC.MAPPER_STATE(_MAPPER_STOPPED);
  public static final int _MAPPER_MAPPING = 1;
  public static final RTC.MAPPER_STATE MAPPER_MAPPING = new RTC.MAPPER_STATE(_MAPPER_MAPPING);
  public static final int _MAPPER_SUSPEND = 2;
  public static final RTC.MAPPER_STATE MAPPER_SUSPEND = new RTC.MAPPER_STATE(_MAPPER_SUSPEND);
  public static final int _MAPPER_ERROR = 3;
  public static final RTC.MAPPER_STATE MAPPER_ERROR = new RTC.MAPPER_STATE(_MAPPER_ERROR);
  public static final int _MAPPER_UNKNOWN = 4;
  public static final RTC.MAPPER_STATE MAPPER_UNKNOWN = new RTC.MAPPER_STATE(_MAPPER_UNKNOWN);

  public int value ()
  {
    return __value;
  }

  public static RTC.MAPPER_STATE from_int (int value)
  {
    if (value >= 0 && value < __size)
      return __array[value];
    else
      throw new org.omg.CORBA.BAD_PARAM ();
  }

  protected MAPPER_STATE (int value)
  {
    __value = value;
    __array[__value] = this;
  }
} // class MAPPER_STATE
