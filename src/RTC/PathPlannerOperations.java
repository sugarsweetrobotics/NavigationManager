package RTC;


/**
* RTC/PathPlannerOperations.java .
* IDL-to-Javaコンパイラ(ポータブル)、バージョン"3.2"によって生成されました
* idl/MobileRobot.idlから
* 2014年12月1日 16時59分53秒 JST
*/

public interface PathPlannerOperations 
{

  /// Plan Path from PathPlanParater.
  RTC.RETURN_VALUE planPath (RTC.OGMap map, RTC.TimedPose2D currentPose, RTC.TimedPose2D targetGoal, RTC.Path2DHolder outPath);
} // interface PathPlannerOperations
