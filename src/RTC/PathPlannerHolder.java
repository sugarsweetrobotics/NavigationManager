package RTC;

/**
* RTC/PathPlannerHolder.java .
* IDL-to-Javaコンパイラ(ポータブル)、バージョン"3.2"によって生成されました
* idl/MobileRobot.idlから
* 2014年12月1日 16時59分53秒 JST
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
