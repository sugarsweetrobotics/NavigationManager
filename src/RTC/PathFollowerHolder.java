package RTC;

/**
* RTC/PathFollowerHolder.java .
* IDL-to-Javaコンパイラ(ポータブル)、バージョン"3.2"によって生成されました
* idl/MobileRobot.idlから
* 2015年1月14日 14時51分21秒 JST
*/

public final class PathFollowerHolder implements org.omg.CORBA.portable.Streamable
{
  public RTC.PathFollower value = null;

  public PathFollowerHolder ()
  {
  }

  public PathFollowerHolder (RTC.PathFollower initialValue)
  {
    value = initialValue;
  }

  public void _read (org.omg.CORBA.portable.InputStream i)
  {
    value = RTC.PathFollowerHelper.read (i);
  }

  public void _write (org.omg.CORBA.portable.OutputStream o)
  {
    RTC.PathFollowerHelper.write (o, value);
  }

  public org.omg.CORBA.TypeCode _type ()
  {
    return RTC.PathFollowerHelper.type ();
  }

}
