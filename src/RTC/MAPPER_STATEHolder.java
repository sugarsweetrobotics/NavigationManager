package RTC;

/**
* RTC/MAPPER_STATEHolder.java .
* IDL-to-Javaコンパイラ(ポータブル)、バージョン"3.2"によって生成されました
* idl/MobileRobot.idlから
* 2015年1月14日 14時51分21秒 JST
*/

public final class MAPPER_STATEHolder implements org.omg.CORBA.portable.Streamable
{
  public RTC.MAPPER_STATE value = null;

  public MAPPER_STATEHolder ()
  {
  }

  public MAPPER_STATEHolder (RTC.MAPPER_STATE initialValue)
  {
    value = initialValue;
  }

  public void _read (org.omg.CORBA.portable.InputStream i)
  {
    value = RTC.MAPPER_STATEHelper.read (i);
  }

  public void _write (org.omg.CORBA.portable.OutputStream o)
  {
    RTC.MAPPER_STATEHelper.write (o, value);
  }

  public org.omg.CORBA.TypeCode _type ()
  {
    return RTC.MAPPER_STATEHelper.type ();
  }

}
