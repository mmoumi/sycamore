package it.diunipi.volpi.sycamore.virca;

import java.util.HashMap;
import java.util.HashSet;

import it.diunipi.volpi.sycamore.engine.Point3D;

import org.virca.api.*;

public class vircaDeviceImpl extends VirCADevice{

	static{
		System.loadLibrary("VirCAJavaApi");
	}
	HashMap<String,Point3D> robots = new HashMap<String,Point3D>();
	HashSet<String> alreadyThere = new HashSet<String>();
	public boolean onActivated(){

		return true;
	}
	
	public boolean onExecute(){
		for (String id : robots.keySet())
		{
			if (!alreadyThere.contains(id))
			{
				if (id.startsWith("H"))
					registerCyberDevice(id,"ball_blue.mesh");
				else
					registerCyberDevice(id,"ball_red.mesh");
				alreadyThere.add(id);
			}
			Point3D point = robots.get(id);
			setCyberDevicePosition(id, point.x*10, point.y*10+100, point.z*10);	
		}
		return true;
	}
	
	public void setRobotPosition(String id, Point3D p){
		robots.put(id, p);
	}
}
