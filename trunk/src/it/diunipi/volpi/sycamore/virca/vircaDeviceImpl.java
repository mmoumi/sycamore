package it.diunipi.volpi.sycamore.virca;

import java.util.HashMap;
import java.util.HashSet;

import hu.sztaki.virca.api.Vector3;
import hu.sztaki.virca.api.VirCADevice;
import it.diunipi.volpi.sycamore.engine.Point3D;


public class vircaDeviceImpl extends VirCADevice{

	static{
		System.loadLibrary("VirCAJavaApi");
	}
	HashMap<String,Point3D> robots = new HashMap<String,Point3D>();
	HashSet<String> alreadyThere = new HashSet<String>();
	public static Point3D cameraPosition = new Point3D();
	public boolean onActivated(){

		return true;
	}
	
	public boolean onExecute(){
		
		synchronized (this) {
			Vector3 v = getCameraPosition();
			cameraPosition = new Point3D(v.getX() / 10.0f,v.getY()/10-10,v.getZ()/10f);
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
			setCyberDevicePosition(id, new Vector3(point.x*10, point.y*10+100, point.z*10));
			getCyberDevicePosition(id);
		}
		}
		return true;
	}
	
	public synchronized void setRobotPosition(String id, Point3D p){
		robots.put(id, p);
	}
}
