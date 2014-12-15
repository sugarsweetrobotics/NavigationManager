package RTC;

/**
* RTC/OGMapperHolder.java .
* IDL-to-Javaコンパイラ(ポータブル)、バージョン"3.2"によって生成されました
* idl/MobileRobot.idlから
* 2014年12月15日 15時01分43秒 JST
*/


/*!
   * @interface OGMapper
   * @brief Occupancy Grid Map Builder Service Interface
   */
public final class OGMapperHolder implements org.omg.CORBA.portable.Streamable
{
  public RTC.OGMapper value = null;

  public OGMapperHolder ()
  {
  }

  public OGMapperHolder (RTC.OGMapper initialValue)
  {
    value = initialValue;
  }

  public void _read (org.omg.CORBA.portable.InputStream i)
  {
    value = RTC.OGMapperHelper.read (i);
  }

  public void _write (org.omg.CORBA.portable.OutputStream o)
  {
    RTC.OGMapperHelper.write (o, value);
  }

  public org.omg.CORBA.TypeCode _type ()
  {
    return RTC.OGMapperHelper.type ();
  }

}
