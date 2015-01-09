package RTC;


/**
* RTC/OGMapServerPOA.java .
* IDL-to-Javaコンパイラ(ポータブル)、バージョン"3.2"によって生成されました
* idl/MobileRobot.idlから
* 2015年1月3日 14時09分23秒 JST
*/


/*!
   * @interface OGMapServer
   * @brief Occupancy Grid Map Service Interface
   */
public abstract class OGMapServerPOA extends org.omg.PortableServer.Servant
 implements RTC.OGMapServerOperations, org.omg.CORBA.portable.InvokeHandler
{

  // Constructors

  private static java.util.Hashtable _methods = new java.util.Hashtable ();
  static
  {
    _methods.put ("requestCurrentBuiltMap", new java.lang.Integer (0));
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

  /// Request Current Build Map Data
       case 0:  // RTC/OGMapServer/requestCurrentBuiltMap
       {
         RTC.OGMapHolder map = new RTC.OGMapHolder ();
         RTC.RETURN_VALUE $result = null;
         $result = this.requestCurrentBuiltMap (map);
         out = $rh.createReply();
         RTC.RETURN_VALUEHelper.write (out, $result);
         RTC.OGMapHelper.write (out, map.value);
         break;
       }

       default:
         throw new org.omg.CORBA.BAD_OPERATION (0, org.omg.CORBA.CompletionStatus.COMPLETED_MAYBE);
    }

    return out;
  } // _invoke

  // Type-specific CORBA::Object operations
  private static String[] __ids = {
    "IDL:RTC/OGMapServer:1.0"};

  public String[] _all_interfaces (org.omg.PortableServer.POA poa, byte[] objectId)
  {
    return (String[])__ids.clone ();
  }

  public OGMapServer _this() 
  {
    return OGMapServerHelper.narrow(
    super._this_object());
  }

  public OGMapServer _this(org.omg.CORBA.ORB orb) 
  {
    return OGMapServerHelper.narrow(
    super._this_object(orb));
  }


} // class OGMapServerPOA
