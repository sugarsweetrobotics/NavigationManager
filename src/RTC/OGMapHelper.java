package RTC;


/**
* RTC/OGMapHelper.java .
* IDL-to-Javaコンパイラ(ポータブル)、バージョン"3.2"によって生成されました
* idl/MobileRobot.idlから
* 2014年8月11日 11時46分06秒 JST
*/

abstract public class OGMapHelper
{
  private static String  _id = "IDL:RTC/OGMap:1.0";

  public static void insert (org.omg.CORBA.Any a, RTC.OGMap that)
  {
    org.omg.CORBA.portable.OutputStream out = a.create_output_stream ();
    a.type (type ());
    write (out, that);
    a.read_value (out.create_input_stream (), type ());
  }

  public static RTC.OGMap extract (org.omg.CORBA.Any a)
  {
    return read (a.create_input_stream ());
  }

  private static org.omg.CORBA.TypeCode __typeCode = null;
  private static boolean __active = false;
  synchronized public static org.omg.CORBA.TypeCode type ()
  {
    if (__typeCode == null)
    {
      synchronized (org.omg.CORBA.TypeCode.class)
      {
        if (__typeCode == null)
        {
          if (__active)
          {
            return org.omg.CORBA.ORB.init().create_recursive_tc ( _id );
          }
          __active = true;
          org.omg.CORBA.StructMember[] _members0 = new org.omg.CORBA.StructMember [3];
          org.omg.CORBA.TypeCode _tcOf_members0 = null;
          _tcOf_members0 = RTC.TimeHelper.type ();
          _members0[0] = new org.omg.CORBA.StructMember (
            "tm",
            _tcOf_members0,
            null);
          _tcOf_members0 = RTC.OGMapConfigHelper.type ();
          _members0[1] = new org.omg.CORBA.StructMember (
            "config",
            _tcOf_members0,
            null);
          _tcOf_members0 = RTC.OGMapTileHelper.type ();
          _members0[2] = new org.omg.CORBA.StructMember (
            "map",
            _tcOf_members0,
            null);
          __typeCode = org.omg.CORBA.ORB.init ().create_struct_tc (RTC.OGMapHelper.id (), "OGMap", _members0);
          __active = false;
        }
      }
    }
    return __typeCode;
  }

  public static String id ()
  {
    return _id;
  }

  public static RTC.OGMap read (org.omg.CORBA.portable.InputStream istream)
  {
    RTC.OGMap value = new RTC.OGMap ();
    value.tm = RTC.TimeHelper.read (istream);
    value.config = RTC.OGMapConfigHelper.read (istream);
    value.map = RTC.OGMapTileHelper.read (istream);
    return value;
  }

  public static void write (org.omg.CORBA.portable.OutputStream ostream, RTC.OGMap value)
  {
    RTC.TimeHelper.write (ostream, value.tm);
    RTC.OGMapConfigHelper.write (ostream, value.config);
    RTC.OGMapTileHelper.write (ostream, value.map);
  }

}
