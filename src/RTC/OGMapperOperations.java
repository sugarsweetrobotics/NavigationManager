package RTC;


/**
* RTC/OGMapperOperations.java .
* IDL-to-Javaコンパイラ(ポータブル)、バージョン"3.2"によって生成されました
* idl/MobileRobot.idlから
* 2014年12月15日 15時01分43秒 JST
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
