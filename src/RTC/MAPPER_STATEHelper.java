package RTC;


/**
* RTC/MAPPER_STATEHelper.java .
* IDL-to-Javaコンパイラ(ポータブル)、バージョン"3.2"によって生成されました
* idl/MobileRobot.idlから
* 2014年9月4日 22時56分50秒 JST
*/

abstract public class MAPPER_STATEHelper
{
  private static String  _id = "IDL:RTC/MAPPER_STATE:1.0";

  public static void insert (org.omg.CORBA.Any a, RTC.MAPPER_STATE that)
  {
    org.omg.CORBA.portable.OutputStream out = a.create_output_stream ();
    a.type (type ());
    write (out, that);
    a.read_value (out.create_input_stream (), type ());
  }

  public static RTC.MAPPER_STATE extract (org.omg.CORBA.Any a)
  {
    return read (a.create_input_stream ());
  }

  private static org.omg.CORBA.TypeCode __typeCode = null;
  synchronized public static org.omg.CORBA.TypeCode type ()
  {
    if (__typeCode == null)
    {
      __typeCode = org.omg.CORBA.ORB.init ().create_enum_tc (RTC.MAPPER_STATEHelper.id (), "MAPPER_STATE", new String[] { "MAPPER_STOPPED", "MAPPER_MAPPING", "MAPPER_SUSPEND", "MAPPER_ERROR", "MAPPER_UNKNOWN"} );
    }
    return __typeCode;
  }

  public static String id ()
  {
    return _id;
  }

  public static RTC.MAPPER_STATE read (org.omg.CORBA.portable.InputStream istream)
  {
    return RTC.MAPPER_STATE.from_int (istream.read_long ());
  }

  public static void write (org.omg.CORBA.portable.OutputStream ostream, RTC.MAPPER_STATE value)
  {
    ostream.write_long (value.value ());
  }

}
