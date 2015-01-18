package RTC;


/**
* RTC/PathPlannerPOA.java .
* IDL-to-Javaコンパイラ(ポータブル)、バージョン"3.2"によって生成されました
* idl/MobileRobot.idlから
* 2015年1月18日 16時11分22秒 JST
*/

public abstract class PathPlannerPOA extends org.omg.PortableServer.Servant
 implements RTC.PathPlannerOperations, org.omg.CORBA.portable.InvokeHandler
{

  // Constructors

  private static java.util.Hashtable _methods = new java.util.Hashtable ();
  static
  {
    _methods.put ("planPath", new java.lang.Integer (0));
  }

  public org.omg.CORBA.portable.OutputStream _invoke (String $method,
                                org.omg.CORBA.portable.InputStream in,
                                org.omg.CORBA.portable.ResponseHandler $rh)
  {
    org.omg.CORBA.portable.OutputStream out = null;
    java.lang.Integer __method = (java.lang.Integer)_methods.get ($method);
    if (__method == null)
      throw new org.omg.CORBA.BAD_OPERATION (0, org.omg.CORBA.CompletionStatus.COMPLETED_MAYBE);

    switch (__method.intValue ())
    {

  /// Plan Path from PathPlanParater.
       case 0:  // RTC/PathPlanner/planPath
       {
         RTC.PathPlanParameter param = RTC.PathPlanParameterHelper.read (in);
         RTC.Path2DHolder outPath = new RTC.Path2DHolder ();
         RTC.RETURN_VALUE $result = null;
         $result = this.planPath (param, outPath);
         out = $rh.createReply();
         RTC.RETURN_VALUEHelper.write (out, $result);
         RTC.Path2DHelper.write (out, outPath.value);
         break;
       }

       default:
         throw new org.omg.CORBA.BAD_OPERATION (0, org.omg.CORBA.CompletionStatus.COMPLETED_MAYBE);
    }

    return out;
  } // _invoke

  // Type-specific CORBA::Object operations
  private static String[] __ids = {
    "IDL:RTC/PathPlanner:1.0"};

  public String[] _all_interfaces (org.omg.PortableServer.POA poa, byte[] objectId)
  {
    return (String[])__ids.clone ();
  }

  public PathPlanner _this() 
  {
    return PathPlannerHelper.narrow(
    super._this_object());
  }

  public PathPlanner _this(org.omg.CORBA.ORB orb) 
  {
    return PathPlannerHelper.narrow(
    super._this_object(orb));
  }


} // class PathPlannerPOA
