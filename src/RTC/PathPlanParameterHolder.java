package RTC;

/**
* RTC/PathPlanParameterHolder.java .
* IDL-to-Javaコンパイラ(ポータブル)、バージョン"3.2"によって生成されました
* idl/MobileRobot.idlから
* 2015年1月3日 14時09分23秒 JST
*/

public final class PathPlanParameterHolder implements org.omg.CORBA.portable.Streamable
{
  public RTC.PathPlanParameter value = null;

  public PathPlanParameterHolder ()
  {
  }

  public PathPlanParameterHolder (RTC.PathPlanParameter initialValue)
  {
    value = initialValue;
  }

  public void _read (org.omg.CORBA.portable.InputStream i)
  {
    value = RTC.PathPlanParameterHelper.read (i);
  }

  public void _write (org.omg.CORBA.portable.OutputStream o)
  {
    RTC.PathPlanParameterHelper.write (o, value);
  }

  public org.omg.CORBA.TypeCode _type ()
  {
    return RTC.PathPlanParameterHelper.type ();
  }

}
