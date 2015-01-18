package RTC;


/**
* RTC/PathPlannerHelper.java .
* IDL-to-Javaコンパイラ(ポータブル)、バージョン"3.2"によって生成されました
* idl/MobileRobot.idlから
* 2015年1月18日 16時11分22秒 JST
*/

abstract public class PathPlannerHelper
{
  private static String  _id = "IDL:RTC/PathPlanner:1.0";

  public static void insert (org.omg.CORBA.Any a, RTC.PathPlanner that)
  {
    org.omg.CORBA.portable.OutputStream out = a.create_output_stream ();
    a.type (type ());
    write (out, that);
    a.read_value (out.create_input_stream (), type ());
  }

  public static RTC.PathPlanner extract (org.omg.CORBA.Any a)
  {
    return read (a.create_input_stream ());
  }

  private static org.omg.CORBA.TypeCode __typeCode = null;
  synchronized public static org.omg.CORBA.TypeCode type ()
  {
    if (__typeCode == null)
    {
      __typeCode = org.omg.CORBA.ORB.init ().create_interface_tc (RTC.PathPlannerHelper.id (), "PathPlanner");
    }
    return __typeCode;
  }

  public static String id ()
  {
    return _id;
  }

  public static RTC.PathPlanner read (org.omg.CORBA.portable.InputStream istream)
  {
    return narrow (istream.read_Object (_PathPlannerStub.class));
  }

  public static void write (org.omg.CORBA.portable.OutputStream ostream, RTC.PathPlanner value)
  {
    ostream.write_Object ((org.omg.CORBA.Object) value);
  }

  public static RTC.PathPlanner narrow (org.omg.CORBA.Object obj)
  {
    if (obj == null)
      return null;
    else if (obj instanceof RTC.PathPlanner)
      return (RTC.PathPlanner)obj;
    else if (!obj._is_a (id ()))
      throw new org.omg.CORBA.BAD_PARAM ();
    else
    {
      org.omg.CORBA.portable.Delegate delegate = ((org.omg.CORBA.portable.ObjectImpl)obj)._get_delegate ();
      RTC._PathPlannerStub stub = new RTC._PathPlannerStub ();
      stub._set_delegate(delegate);
      return stub;
    }
  }

  public static RTC.PathPlanner unchecked_narrow (org.omg.CORBA.Object obj)
  {
    if (obj == null)
      return null;
    else if (obj instanceof RTC.PathPlanner)
      return (RTC.PathPlanner)obj;
    else
    {
      org.omg.CORBA.portable.Delegate delegate = ((org.omg.CORBA.portable.ObjectImpl)obj)._get_delegate ();
      RTC._PathPlannerStub stub = new RTC._PathPlannerStub ();
      stub._set_delegate(delegate);
      return stub;
    }
  }

}
