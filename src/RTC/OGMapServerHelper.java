package RTC;


/**
* RTC/OGMapServerHelper.java .
* IDL-to-Javaコンパイラ(ポータブル)、バージョン"3.2"によって生成されました
* idl/MobileRobot.idlから
* 2014年8月11日 11時46分06秒 JST
*/


/*!
   * @interface OGMapServer
   * @brief Occupancy Grid Map Service Interface
   */
abstract public class OGMapServerHelper
{
  private static String  _id = "IDL:RTC/OGMapServer:1.0";

  public static void insert (org.omg.CORBA.Any a, RTC.OGMapServer that)
  {
    org.omg.CORBA.portable.OutputStream out = a.create_output_stream ();
    a.type (type ());
    write (out, that);
    a.read_value (out.create_input_stream (), type ());
  }

  public static RTC.OGMapServer extract (org.omg.CORBA.Any a)
  {
    return read (a.create_input_stream ());
  }

  private static org.omg.CORBA.TypeCode __typeCode = null;
  synchronized public static org.omg.CORBA.TypeCode type ()
  {
    if (__typeCode == null)
    {
      __typeCode = org.omg.CORBA.ORB.init ().create_interface_tc (RTC.OGMapServerHelper.id (), "OGMapServer");
    }
    return __typeCode;
  }

  public static String id ()
  {
    return _id;
  }

  public static RTC.OGMapServer read (org.omg.CORBA.portable.InputStream istream)
  {
    return narrow (istream.read_Object (_OGMapServerStub.class));
  }

  public static void write (org.omg.CORBA.portable.OutputStream ostream, RTC.OGMapServer value)
  {
    ostream.write_Object ((org.omg.CORBA.Object) value);
  }

  public static RTC.OGMapServer narrow (org.omg.CORBA.Object obj)
  {
    if (obj == null)
      return null;
    else if (obj instanceof RTC.OGMapServer)
      return (RTC.OGMapServer)obj;
    else if (!obj._is_a (id ()))
      throw new org.omg.CORBA.BAD_PARAM ();
    else
    {
      org.omg.CORBA.portable.Delegate delegate = ((org.omg.CORBA.portable.ObjectImpl)obj)._get_delegate ();
      RTC._OGMapServerStub stub = new RTC._OGMapServerStub ();
      stub._set_delegate(delegate);
      return stub;
    }
  }

  public static RTC.OGMapServer unchecked_narrow (org.omg.CORBA.Object obj)
  {
    if (obj == null)
      return null;
    else if (obj instanceof RTC.OGMapServer)
      return (RTC.OGMapServer)obj;
    else
    {
      org.omg.CORBA.portable.Delegate delegate = ((org.omg.CORBA.portable.ObjectImpl)obj)._get_delegate ();
      RTC._OGMapServerStub stub = new RTC._OGMapServerStub ();
      stub._set_delegate(delegate);
      return stub;
    }
  }

}
