package RTC;

/**
* RTC/OGMapServerHolder.java .
* IDL-to-Javaコンパイラ(ポータブル)、バージョン"3.2"によって生成されました
* idl/MobileRobot.idlから
* 2014年12月1日 16時59分53秒 JST
*/


/*!
   * @interface OGMapServer
   * @brief Occupancy Grid Map Service Interface
   */
public final class OGMapServerHolder implements org.omg.CORBA.portable.Streamable
{
  public RTC.OGMapServer value = null;

  public OGMapServerHolder ()
  {
  }

  public OGMapServerHolder (RTC.OGMapServer initialValue)
  {
    value = initialValue;
  }

  public void _read (org.omg.CORBA.portable.InputStream i)
  {
    value = RTC.OGMapServerHelper.read (i);
  }

  public void _write (org.omg.CORBA.portable.OutputStream o)
  {
    RTC.OGMapServerHelper.write (o, value);
  }

  public org.omg.CORBA.TypeCode _type ()
  {
    return RTC.OGMapServerHelper.type ();
  }

}
