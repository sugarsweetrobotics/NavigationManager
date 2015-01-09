package RTC;


/**
* RTC/_OGMapperStub.java .
* IDL-to-Javaコンパイラ(ポータブル)、バージョン"3.2"によって生成されました
* idl/MobileRobot.idlから
* 2015年1月3日 14時09分23秒 JST
*/


/*!
   * @interface OGMapper
   * @brief Occupancy Grid Map Builder Service Interface
   */
public class _OGMapperStub extends org.omg.CORBA.portable.ObjectImpl implements RTC.OGMapper
{


  /// Initialize Current Build Map Data
  public RTC.RETURN_VALUE initializeMap (RTC.OGMapConfig config, RTC.Pose2D initialPose)
  {
            org.omg.CORBA.portable.InputStream $in = null;
            try {
                org.omg.CORBA.portable.OutputStream $out = _request ("initializeMap", true);
                RTC.OGMapConfigHelper.write ($out, config);
                RTC.Pose2DHelper.write ($out, initialPose);
                $in = _invoke ($out);
                RTC.RETURN_VALUE $result = RTC.RETURN_VALUEHelper.read ($in);
                return $result;
            } catch (org.omg.CORBA.portable.ApplicationException $ex) {
                $in = $ex.getInputStream ();
                String _id = $ex.getId ();
                throw new org.omg.CORBA.MARSHAL (_id);
            } catch (org.omg.CORBA.portable.RemarshalException $rm) {
                return initializeMap (config, initialPose        );
            } finally {
                _releaseReply ($in);
            }
  } // initializeMap

  public RTC.RETURN_VALUE startMapping ()
  {
            org.omg.CORBA.portable.InputStream $in = null;
            try {
                org.omg.CORBA.portable.OutputStream $out = _request ("startMapping", true);
                $in = _invoke ($out);
                RTC.RETURN_VALUE $result = RTC.RETURN_VALUEHelper.read ($in);
                return $result;
            } catch (org.omg.CORBA.portable.ApplicationException $ex) {
                $in = $ex.getInputStream ();
                String _id = $ex.getId ();
                throw new org.omg.CORBA.MARSHAL (_id);
            } catch (org.omg.CORBA.portable.RemarshalException $rm) {
                return startMapping (        );
            } finally {
                _releaseReply ($in);
            }
  } // startMapping

  public RTC.RETURN_VALUE stopMapping ()
  {
            org.omg.CORBA.portable.InputStream $in = null;
            try {
                org.omg.CORBA.portable.OutputStream $out = _request ("stopMapping", true);
                $in = _invoke ($out);
                RTC.RETURN_VALUE $result = RTC.RETURN_VALUEHelper.read ($in);
                return $result;
            } catch (org.omg.CORBA.portable.ApplicationException $ex) {
                $in = $ex.getInputStream ();
                String _id = $ex.getId ();
                throw new org.omg.CORBA.MARSHAL (_id);
            } catch (org.omg.CORBA.portable.RemarshalException $rm) {
                return stopMapping (        );
            } finally {
                _releaseReply ($in);
            }
  } // stopMapping

  public RTC.RETURN_VALUE suspendMapping ()
  {
            org.omg.CORBA.portable.InputStream $in = null;
            try {
                org.omg.CORBA.portable.OutputStream $out = _request ("suspendMapping", true);
                $in = _invoke ($out);
                RTC.RETURN_VALUE $result = RTC.RETURN_VALUEHelper.read ($in);
                return $result;
            } catch (org.omg.CORBA.portable.ApplicationException $ex) {
                $in = $ex.getInputStream ();
                String _id = $ex.getId ();
                throw new org.omg.CORBA.MARSHAL (_id);
            } catch (org.omg.CORBA.portable.RemarshalException $rm) {
                return suspendMapping (        );
            } finally {
                _releaseReply ($in);
            }
  } // suspendMapping

  public RTC.RETURN_VALUE resumeMapping ()
  {
            org.omg.CORBA.portable.InputStream $in = null;
            try {
                org.omg.CORBA.portable.OutputStream $out = _request ("resumeMapping", true);
                $in = _invoke ($out);
                RTC.RETURN_VALUE $result = RTC.RETURN_VALUEHelper.read ($in);
                return $result;
            } catch (org.omg.CORBA.portable.ApplicationException $ex) {
                $in = $ex.getInputStream ();
                String _id = $ex.getId ();
                throw new org.omg.CORBA.MARSHAL (_id);
            } catch (org.omg.CORBA.portable.RemarshalException $rm) {
                return resumeMapping (        );
            } finally {
                _releaseReply ($in);
            }
  } // resumeMapping

  public RTC.RETURN_VALUE getState (RTC.MAPPER_STATEHolder state)
  {
            org.omg.CORBA.portable.InputStream $in = null;
            try {
                org.omg.CORBA.portable.OutputStream $out = _request ("getState", true);
                $in = _invoke ($out);
                RTC.RETURN_VALUE $result = RTC.RETURN_VALUEHelper.read ($in);
                state.value = RTC.MAPPER_STATEHelper.read ($in);
                return $result;
            } catch (org.omg.CORBA.portable.ApplicationException $ex) {
                $in = $ex.getInputStream ();
                String _id = $ex.getId ();
                throw new org.omg.CORBA.MARSHAL (_id);
            } catch (org.omg.CORBA.portable.RemarshalException $rm) {
                return getState (state        );
            } finally {
                _releaseReply ($in);
            }
  } // getState


  /// Request Current Build Map Data
  public RTC.RETURN_VALUE requestCurrentBuiltMap (RTC.OGMapHolder map)
  {
            org.omg.CORBA.portable.InputStream $in = null;
            try {
                org.omg.CORBA.portable.OutputStream $out = _request ("requestCurrentBuiltMap", true);
                $in = _invoke ($out);
                RTC.RETURN_VALUE $result = RTC.RETURN_VALUEHelper.read ($in);
                map.value = RTC.OGMapHelper.read ($in);
                return $result;
            } catch (org.omg.CORBA.portable.ApplicationException $ex) {
                $in = $ex.getInputStream ();
                String _id = $ex.getId ();
                throw new org.omg.CORBA.MARSHAL (_id);
            } catch (org.omg.CORBA.portable.RemarshalException $rm) {
                return requestCurrentBuiltMap (map        );
            } finally {
                _releaseReply ($in);
            }
  } // requestCurrentBuiltMap

  // Type-specific CORBA::Object operations
  private static String[] __ids = {
    "IDL:RTC/OGMapper:1.0"};

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
} // class _OGMapperStub
