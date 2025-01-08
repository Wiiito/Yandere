<?xml version="1.0" encoding="UTF-8"?>
<tileset version="1.10" tiledversion="1.11.0" name="Colisoes" tilewidth="16" tileheight="16" tilecount="6" columns="6">
 <image source="Colisoes.png" width="96" height="16"/>
 <tile id="0">
  <properties>
   <property name="walkable" type="bool" value="true"/>
  </properties>
 </tile>
 <tile id="1">
  <properties>
   <property name="blockDirections" type="int" value="4"/>
   <property name="walkable" type="bool" value="false"/>
  </properties>
 </tile>
 <tile id="2">
  <properties>
   <property name="blockDirections" type="int" value="1"/>
   <property name="walkable" type="bool" value="true"/>
  </properties>
 </tile>
 <tile id="3">
  <properties>
   <property name="blockDirections" type="int" value="2"/>
   <property name="walkable" type="bool" value="true"/>
  </properties>
 </tile>
 <tile id="4">
  <properties>
   <property name="changeFloor" type="int" value="1"/>
   <property name="walkable" type="bool" value="true"/>
  </properties>
 </tile>
 <tile id="5">
  <properties>
   <property name="changeFloor" type="int" value="0"/>
   <property name="walkable" type="bool" value="true"/>
  </properties>
 </tile>
</tileset>
