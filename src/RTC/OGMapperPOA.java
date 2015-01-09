package RTC;


/**
* RTC/OGMapperPOA.java .
* IDL-to-Javaコンパイラ(ポータブル)、バージョン"3.2"によって生成されました
* idl/MobileRobot.idlから
* 2015年1月3日 14時09分23秒 JST
*/


/*!
   * @interface OGMapper
   * @brief Occupancy Grid Map Builder Service Interface
   */
public abstract class OGMapperPOA extends org.omg.PortableServer.Servant
 implements RTC.OGMapperOperations, org.omg.CORBA.portable.InvokeHandler
{

  // Constructors

  private static java.util.Hashtable _methods = new java.util.Hashtable ();
  static
  {
    _methods.put ("initializeMap", new java.lang.Integer (0));
    _methods.put ("startMapping", new java.lang.Integer (1));
    _methods.put ("stopMapping", new java.lang.Integer (2));
    _methods.put ("suspendMapping", new java.lang.Integer (3));
    _methods.put ("resumeMapping", new java.lang.Integer (4));
    _methods.put ("getState", new java.lang.Integer (5));
    _methods.put ("requestCurrentBuiltMap", new java.lang.Integer (6));
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

  /// Initialize Current Build Map Data
       case 0:  // RTC/OGMapper/initializeMap
       {
         RTC.OGMapConfig config = RTC.OGMapConfigHelper.read (in);
         RTC.Pose2D initialPose = RTC.Pose2DHelper.read (in);
         RTC.RETURN_VALUE $result = null;
         $result = this.initializeMap (config, initialPose);
         out = $rh.createReply();
         RTC.RETURN_VALUEHelper.write (out, $result);
         break;
       }

       case 1:  // RTC/OGMapper/startMapping
       {
         RTC.RETURN_VALUE $result = null;
         $result = this.startMapping ();
         out = $rh.createReply();
         RTC.RETURN_VALUEHelper.write (out, $result);
         break;
       }

       case 2:  // RTC/OGMapper/stopMapping
       {
         RTC.RETURN_VALUE $result = null;
         $result = this.stopMapping ();
         out = $rh.createReply();
         RTC.RETURN_VALUEHelper.write (out, $result);
         break;
       }

       case 3:  // RTC/OGMapper/suspendMapping
       {
         RTC.RETURN_VALUE $result = null;
         $result = this.suspendMapping ();
         out = $rh.createReply();
         RTC.RETURN_VALUEHelper.write (out, $result);
         break;
       }

       case 4:  // RTC/OGMapper/resumeMapping
       {
         RTC.RETURN_VALUE $result = null;
         $result = this.resumeMapping ();
         out = $rh.createReply();
         RTC.RETURN_VALUEHelper.write (out, $result);
         break;
       }

       case 5:  // RTC/OGMapper/getState
       {
         RTC.MAPPER_STATEHolder state = new RTC.MAPPER_STATEHolder ();
         RTC.RETURN_VALUE $result = null;
         $result = this.getState (state);
         out = $rh.createReply();
         RTC.RETURN_VALUEHelper.write (out, $result);
         RTC.MAPPER_STATEHelper.write (out, state.value);
         break;
       }


  /// Request Current Build Map Data
       case 6:  // RTC/OGMapper/requestCurrentBuiltMap
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
    "IDL:RTC/OGMapper:1.0"};

  public String[] _all_interfaces (org.omg.PortableServer.POA poa, byte[] objectId)
  {
    return (String[])__ids.clone ();
  }

  public OGMapper _this() 
  {
    return OGMapperHelper.narrow(
    super._this_object());
  }

  public OGMapper _this(org.omg.CORBA.ORB orb) 
  {
    return OGMapperHelper.narrow(
    super._this_object(orb));
  }


} // class OGMapperPOA
