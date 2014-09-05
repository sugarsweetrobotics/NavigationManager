package RTC;

/**
* RTC/OGMapHolder.java .
* IDL-to-Javaコンパイラ(ポータブル)、バージョン"3.2"によって生成されました
* idl/MobileRobot.idlから
* 2014年9月4日 22時56分50秒 JST
*/

public final class OGMapHolder implements org.omg.CORBA.portable.Streamable
{
  public RTC.OGMap value = null;

  public OGMapHolder ()
  {
  }

  public OGMapHolder (RTC.OGMap initialValue)
  {
    value = initialValue;
  }

  public void _read (org.omg.CORBA.portable.InputStream i)
  {
    value = RTC.OGMapHelper.read (i);
  }

  public void _write (org.omg.CORBA.portable.OutputStream o)
  {
    RTC.OGMapHelper.write (o, value);
  }

  public org.omg.CORBA.TypeCode _type ()
  {
    return RTC.OGMapHelper.type ();
  }

}
