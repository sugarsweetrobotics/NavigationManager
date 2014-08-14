# MapperViewer

0. Introduction
Mapper Viewer RT-Component for OpenRTM.

This RTC uses OGMapper interface to control Mapper RTCs.

1. How to Build 

1.1 Install 
  - JDK7 (32bit) or higher. Never use x64 version for this.
  - This RTC Source Code includes OpenRTM-aist-1.1.0 Java version in Jar directory. You do not need to install OpenRTM-aist.
  - Eclipse 3.8 is provided in http://openrtm.org. This eclipse includes all of the RTC development tool. I recommend to use it.
  - Import the .project in this source code by Eclipse. Then, build it.

2. Launch
  - Launch MapperViewerComp.java as the Java application from Eclipse.

  This RTC has simple interface:

 Inport:
  - range : RangeData - LIDAR data. You can overlay the ranger data on the Map. See http://github.com/sugarsweetrobotics/UrgRTC
  
  - pose : TimedPose2D - Pose of the Robot. You can overlay the robot pose on the Map.

 Outport:
  - targetVelocity : TimedVelocity2D - You can control the Robot by controller dialog (select menu->control->Open Joystick)

 ServicePort :
  - gridMapper : RTC::OGMapper - This must be connected to the Mapper RTC. A useful example of the MapperRTC is Mapper_MRPT. See http://github.com/sugarsweetrobotics/Mapper_MRPT

3. How to use
  Even if connect the whole RT-system and activate all, Mapper will not start mapping. You need to click Menu > Mapping > startMapping.
  If you want to see the newest map, select Menu > Mapping > Request Map. If you click Menu > Mapping > Enable Auto Update, the MapViewer automatically update the view.
  
  If you click Menu > control > Open Joystick, you can use virtual joystick for control your robot. MapperViewer.targetVelocity port will output the velocity you operated.
  
  If you want to save map data, Menu > File > Save as.. You will get two files. One is $MAP_NAME$.png and the other is $MAP_NAME$.yaml. the YAML file involves Map data (original point, resolution)
  
  
  


Author: Yuki Suga (Sugar Sweet Robotics, co ltd.) ysuga@sugarsweetrobotics.com
Copyright : 2014, Sugar Sweet Robotics, Co. LTD.
License : GPLv3 (Read attached file : COPYING)

