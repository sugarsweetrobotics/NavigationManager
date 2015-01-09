package RTC;


/**
* RTC/OGMapperHelper.java .
* IDL-to-Javaコンパイラ(ポータブル)、バージョン"3.2"によって生成されました
* idl/MobileRobot.idlから
* 2015年1月3日 14時09分23秒 JST
*/


/*!
   * @interface OGMapper
   * @brief Occupancy Grid Map Builder Service Interface
   */
abstract public class OGMapperHelper
{
  private static String  _id = "IDL:RTC/OGMapper:1.0";

  public static void insert (org.omg.CORBA.Any a, RTC.OGMapper that)
  {
    org.omg.CORBA.portable.OutputStream out = a.create_output_stream ();
    a.type (type ());
    write (out, that);
    a.read_value (out.create_input_stream (), type ());
  }

  public static RTC.OGMapper extract (org.omg.CORBA.Any a)
  {
    return read (a.create_input_stream ());
  }

  private static org.omg.CORBA.TypeCode __typeCode = null;
  synchronized public static org.omg.CORBA.TypeCode type ()
  {
    if (__typeCode == null)
    {
      __typeCode = org.omg.CORBA.ORB.init ().create_interface_tc (RTC.OGMapperHelper.id (), "OGMapper");
    }
    return __typeCode;
  }

  public static String id ()
  {
    return _id;
  }

  public static RTC.OGMapper read (org.omg.CORBA.portable.InputStream istream)
  {
    return narrow (istream.read_Object (_OGMapperStub.class));
  }

  public static void write (org.omg.CORBA.portable.OutputStream ostream, RTC.OGMapper value)
  {
    ostream.write_Object ((org.omg.CORBA.Object) value);
  }

  public static RTC.OGMapper narrow (org.omg.CORBA.Object obj)
  {
    if (obj == null)
      return null;
    else if (obj instanceof RTC.OGMapper)
      return (RTC.OGMapper)obj;
    else if (!obj._is_a (id ()))
      throw new org.omg.CORBA.BAD_PARAM ();
    else
    {
      org.omg.CORBA.portable.Delegate delegate = ((org.omg.CORBA.portable.ObjectImpl)obj)._get_delegate ();
      RTC._OGMapperStub stub = new RTC._OGMapperStub ();
      stub._set_delegate(delegate);
      return stub;
    }
  }

  public static RTC.OGMapper unchecked_narrow (org.omg.CORBA.Object obj)
  {
    if (obj == null)
      return null;
    else if (obj instanceof RTC.OGMapper)
      return (RTC.OGMapper)obj;
    else
    {
      org.omg.CORBA.portable.Delegate delegate = ((org.omg.CORBA.portable.ObjectImpl)obj)._get_delegate ();
      RTC._OGMapperStub stub = new RTC._OGMapperStub ();
      stub._set_delegate(delegate);
      return stub;
    }
  }

}
