package RTC;


/**
* RTC/PathFollowerHelper.java .
* IDL-to-Javaコンパイラ(ポータブル)、バージョン"3.2"によって生成されました
* idl/MobileRobot.idlから
* 2015年1月14日 14時51分21秒 JST
*/

abstract public class PathFollowerHelper
{
  private static String  _id = "IDL:RTC/PathFollower:1.0";

  public static void insert (org.omg.CORBA.Any a, RTC.PathFollower that)
  {
    org.omg.CORBA.portable.OutputStream out = a.create_output_stream ();
    a.type (type ());
    write (out, that);
    a.read_value (out.create_input_stream (), type ());
  }

  public static RTC.PathFollower extract (org.omg.CORBA.Any a)
  {
    return read (a.create_input_stream ());
  }

  private static org.omg.CORBA.TypeCode __typeCode = null;
  synchronized public static org.omg.CORBA.TypeCode type ()
  {
    if (__typeCode == null)
    {
      __typeCode = org.omg.CORBA.ORB.init ().create_interface_tc (RTC.PathFollowerHelper.id (), "PathFollower");
    }
    return __typeCode;
  }

  public static String id ()
  {
    return _id;
  }

  public static RTC.PathFollower read (org.omg.CORBA.portable.InputStream istream)
  {
    return narrow (istream.read_Object (_PathFollowerStub.class));
  }

  public static void write (org.omg.CORBA.portable.OutputStream ostream, RTC.PathFollower value)
  {
    ostream.write_Object ((org.omg.CORBA.Object) value);
  }

  public static RTC.PathFollower narrow (org.omg.CORBA.Object obj)
  {
    if (obj == null)
      return null;
    else if (obj instanceof RTC.PathFollower)
      return (RTC.PathFollower)obj;
    else if (!obj._is_a (id ()))
      throw new org.omg.CORBA.BAD_PARAM ();
    else
    {
      org.omg.CORBA.portable.Delegate delegate = ((org.omg.CORBA.portable.ObjectImpl)obj)._get_delegate ();
      RTC._PathFollowerStub stub = new RTC._PathFollowerStub ();
      stub._set_delegate(delegate);
      return stub;
    }
  }

  public static RTC.PathFollower unchecked_narrow (org.omg.CORBA.Object obj)
  {
    if (obj == null)
      return null;
    else if (obj instanceof RTC.PathFollower)
      return (RTC.PathFollower)obj;
    else
    {
      org.omg.CORBA.portable.Delegate delegate = ((org.omg.CORBA.portable.ObjectImpl)obj)._get_delegate ();
      RTC._PathFollowerStub stub = new RTC._PathFollowerStub ();
      stub._set_delegate(delegate);
      return stub;
    }
  }

}
