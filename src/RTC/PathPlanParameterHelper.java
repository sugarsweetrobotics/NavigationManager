package RTC;


/**
* RTC/PathPlanParameterHelper.java .
* IDL-to-Javaコンパイラ(ポータブル)、バージョン"3.2"によって生成されました
* idl/MobileRobot.idlから
* 2014年12月1日 16時59分53秒 JST
*/

abstract public class PathPlanParameterHelper
{
  private static String  _id = "IDL:RTC/PathPlanParameter:1.0";

  public static void insert (org.omg.CORBA.Any a, RTC.PathPlanParameter that)
  {
    org.omg.CORBA.portable.OutputStream out = a.create_output_stream ();
    a.type (type ());
    write (out, that);
    a.read_value (out.create_input_stream (), type ());
  }

  public static RTC.PathPlanParameter extract (org.omg.CORBA.Any a)
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
          org.omg.CORBA.StructMember[] _members0 = new org.omg.CORBA.StructMember [6];
          org.omg.CORBA.TypeCode _tcOf_members0 = null;
          _tcOf_members0 = RTC.Pose2DHelper.type ();
          _members0[0] = new org.omg.CORBA.StructMember (
            "targetPose",
            _tcOf_members0,
            null);
          _tcOf_members0 = RTC.Pose2DHelper.type ();
          _members0[1] = new org.omg.CORBA.StructMember (
            "currentPose",
            _tcOf_members0,
            null);
          _tcOf_members0 = org.omg.CORBA.ORB.init ().get_primitive_tc (org.omg.CORBA.TCKind.tk_double);
          _members0[2] = new org.omg.CORBA.StructMember (
            "distanceTolerance",
            _tcOf_members0,
            null);
          _tcOf_members0 = org.omg.CORBA.ORB.init ().get_primitive_tc (org.omg.CORBA.TCKind.tk_double);
          _members0[3] = new org.omg.CORBA.StructMember (
            "headingTolerance",
            _tcOf_members0,
            null);
          _tcOf_members0 = RTC.TimeHelper.type ();
          _members0[4] = new org.omg.CORBA.StructMember (
            "timeLimit",
            _tcOf_members0,
            null);
          _tcOf_members0 = RTC.Velocity2DHelper.type ();
          _members0[5] = new org.omg.CORBA.StructMember (
            "maxSpeed",
            _tcOf_members0,
            null);
          __typeCode = org.omg.CORBA.ORB.init ().create_struct_tc (RTC.PathPlanParameterHelper.id (), "PathPlanParameter", _members0);
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

  public static RTC.PathPlanParameter read (org.omg.CORBA.portable.InputStream istream)
  {
    RTC.PathPlanParameter value = new RTC.PathPlanParameter ();
    value.targetPose = RTC.Pose2DHelper.read (istream);
    value.currentPose = RTC.Pose2DHelper.read (istream);
    value.distanceTolerance = istream.read_double ();
    value.headingTolerance = istream.read_double ();
    value.timeLimit = RTC.TimeHelper.read (istream);
    value.maxSpeed = RTC.Velocity2DHelper.read (istream);
    return value;
  }

  public static void write (org.omg.CORBA.portable.OutputStream ostream, RTC.PathPlanParameter value)
  {
    RTC.Pose2DHelper.write (ostream, value.targetPose);
    RTC.Pose2DHelper.write (ostream, value.currentPose);
    ostream.write_double (value.distanceTolerance);
    ostream.write_double (value.headingTolerance);
    RTC.TimeHelper.write (ostream, value.timeLimit);
    RTC.Velocity2DHelper.write (ostream, value.maxSpeed);
  }

}
