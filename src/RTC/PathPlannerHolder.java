package RTC;

/**
* RTC/PathPlannerHolder.java .
* IDL-to-Javaコンパイラ(ポータブル)、バージョン"3.2"によって生成されました
* idl/MobileRobot.idlから
* 2015年1月3日 14時09分23秒 JST
*/

public final class PathPlannerHolder implements org.omg.CORBA.portable.Streamable
{
  public RTC.PathPlanner value = null;

  public PathPlannerHolder ()
  {
  }

  public PathPlannerHolder (RTC.PathPlanner initialValue)
  {
    value = initialValue;
  }

  public void _read (org.omg.CORBA.portable.InputStream i)
  {
    value = RTC.PathPlannerHelper.read (i);
  }

  public void _write (org.omg.CORBA.portable.OutputStream o)
  {
    RTC.PathPlannerHelper.write (o, value);
  }

  public org.omg.CORBA.TypeCode _type ()
  {
    return RTC.PathPlannerHelper.type ();
  }

}
