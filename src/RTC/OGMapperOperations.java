package RTC;


/**
* RTC/OGMapperOperations.java .
* IDL-to-Javaコンパイラ(ポータブル)、バージョン"3.2"によって生成されました
* idl/MobileRobot.idlから
* 2014年8月6日 9時22分31秒 JST
*/


/*!
   * @interface OGMapper
   * @brief Occupancy Grid Map Builder Service Interface
   */
public interface OGMapperOperations 
{

  /// Initialize Current Build Map Data
  RTC.RETURN_VALUE initializeMap (RTC.OGMapConfig config, RTC.Pose2D initialPose);
  RTC.RETURN_VALUE startMapping ();
  RTC.RETURN_VALUE stopMapping ();
  RTC.RETURN_VALUE suspendMapping ();
  RTC.RETURN_VALUE resumeMapping ();
  RTC.RETURN_VALUE getState (RTC.MAPPER_STATEHolder state);

  /// Request Current Build Map Data
  RTC.RETURN_VALUE requestCurrentBuiltMap (RTC.OGMapHolder map);
} // interface OGMapperOperations
