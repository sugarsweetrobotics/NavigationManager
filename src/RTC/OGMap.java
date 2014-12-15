package RTC;


/**
* RTC/OGMap.java .
* IDL-to-Javaコンパイラ(ポータブル)、バージョン"3.2"によって生成されました
* idl/MobileRobot.idlから
* 2014年12月15日 15時01分43秒 JST
*/

public final class OGMap implements org.omg.CORBA.portable.IDLEntity
{

  /// Time stamp.
  public RTC.Time tm = null;

  /// OccupancyGridMap Configuration
  public RTC.OGMapConfig config = null;

  /// OccupancyGridMap Data
  public RTC.OGMapTile map = null;

  public OGMap ()
  {
  } // ctor

  public OGMap (RTC.Time _tm, RTC.OGMapConfig _config, RTC.OGMapTile _map)
  {
    tm = _tm;
    config = _config;
    map = _map;
  } // ctor

} // class OGMap
