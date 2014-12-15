package RTC;


/**
* RTC/OGMapServerOperations.java .
* IDL-to-Javaコンパイラ(ポータブル)、バージョン"3.2"によって生成されました
* idl/MobileRobot.idlから
* 2014年12月15日 15時01分43秒 JST
*/


/*!
   * @interface OGMapServer
   * @brief Occupancy Grid Map Service Interface
   */
public interface OGMapServerOperations 
{

  /// Request Current Build Map Data
  RTC.RETURN_VALUE requestCurrentBuiltMap (RTC.OGMapHolder map);
} // interface OGMapServerOperations
