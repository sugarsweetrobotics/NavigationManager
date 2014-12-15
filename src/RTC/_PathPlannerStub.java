package RTC;


/**
* RTC/_PathPlannerStub.java .
* IDL-to-Javaコンパイラ(ポータブル)、バージョン"3.2"によって生成されました
* idl/MobileRobot.idlから
* 2014年12月15日 15時01分43秒 JST
*/

public class _PathPlannerStub extends org.omg.CORBA.portable.ObjectImpl implements RTC.PathPlanner
{


  /// Plan Path from PathPlanParater.
  public RTC.RETURN_VALUE planPath (RTC.PathPlanParameter param, RTC.Path2DHolder outPath)
  {
            org.omg.CORBA.portable.InputStream $in = null;
            try {
                org.omg.CORBA.portable.OutputStream $out = _request ("planPath", true);
                RTC.PathPlanParameterHelper.write ($out, param);
                $in = _invoke ($out);
                RTC.RETURN_VALUE $result = RTC.RETURN_VALUEHelper.read ($in);
                outPath.value = RTC.Path2DHelper.read ($in);
                return $result;
            } catch (org.omg.CORBA.portable.ApplicationException $ex) {
                $in = $ex.getInputStream ();
                String _id = $ex.getId ();
                throw new org.omg.CORBA.MARSHAL (_id);
            } catch (org.omg.CORBA.portable.RemarshalException $rm) {
                return planPath (param, outPath        );
            } finally {
                _releaseReply ($in);
            }
  } // planPath

  // Type-specific CORBA::Object operations
  private static String[] __ids = {
    "IDL:RTC/PathPlanner:1.0"};

  public String[] _ids ()
  {
    return (String[])__ids.clone ();
  }

  private void readObject (java.io.ObjectInputStream s) throws java.io.IOException
  {
     String str = s.readUTF ();
     String[] args = null;
     java.util.Properties props = null;
     org.omg.CORBA.ORB orb = org.omg.CORBA.ORB.init (args, props);
   try {
     org.omg.CORBA.Object obj = orb.string_to_object (str);
     org.omg.CORBA.portable.Delegate delegate = ((org.omg.CORBA.portable.ObjectImpl) obj)._get_delegate ();
     _set_delegate (delegate);
   } finally {
     orb.destroy() ;
   }
  }

  private void writeObject (java.io.ObjectOutputStream s) throws java.io.IOException
  {
     String[] args = null;
     java.util.Properties props = null;
     org.omg.CORBA.ORB orb = org.omg.CORBA.ORB.init (args, props);
   try {
     String str = orb.object_to_string (this);
     s.writeUTF (str);
   } finally {
     orb.destroy() ;
   }
  }
} // class _PathPlannerStub
