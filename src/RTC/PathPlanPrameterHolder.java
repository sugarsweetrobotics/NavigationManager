package RTC;

/**
* RTC/PathPlanPrameterHolder.java .
* IDL-to-Javaコンパイラ(ポータブル)、バージョン"3.2"によって生成されました
* idl/MobileRobot.idlから
* 2014年9月4日 22時56分14秒 JST
*/

public final class PathPlanPrameterHolder implements org.omg.CORBA.portable.Streamable
{
  public RTC.PathPlanPrameter value = null;

  public PathPlanPrameterHolder ()
  {
  }

  public PathPlanPrameterHolder (RTC.PathPlanPrameter initialValue)
  {
    value = initialValue;
  }

  public void _read (org.omg.CORBA.portable.InputStream i)
  {
    value = RTC.PathPlanPrameterHelper.read (i);
  }

  public void _write (org.omg.CORBA.portable.OutputStream o)
  {
    RTC.PathPlanPrameterHelper.write (o, value);
  }

  public org.omg.CORBA.TypeCode _type ()
  {
    return RTC.PathPlanPrameterHelper.type ();
  }

}
