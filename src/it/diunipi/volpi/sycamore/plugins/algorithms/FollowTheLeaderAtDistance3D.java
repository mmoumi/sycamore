package it.diunipi.volpi.sycamore.plugins.algorithms;
import it.diunipi.volpi.sycamore.engine.Observation;
import it.diunipi.volpi.sycamore.engine.Point3D;
import it.diunipi.volpi.sycamore.engine.SycamoreEngine.TYPE;
import it.diunipi.volpi.sycamore.engine.SycamoreObservedRobot;
import it.diunipi.volpi.sycamore.engine.TooManyLightsException;
import it.diunipi.volpi.sycamore.gui.SycamorePanel;

import java.util.Vector;

import net.xeoh.plugins.base.annotations.PluginImplementation;

import com.jme3.math.ColorRGBA;

/**
 * A simple 3D algorithm where the robots look for the leader, identified by the human pilot robot,
 * and reach the position where the leader is.
 * 
 * @author Valerio Volpi - vale.v@me.com
 */
@PluginImplementation
public class FollowTheLeaderAtDistance3D extends AlgorithmImpl<Point3D>
{
	/* (non-Javadoc)
	 * @see it.diunipi.volpi.sycamore.plugins.algorithms.Algorithm#init()
	 */
	@Override
	public void init(SycamoreObservedRobot<Point3D> robot)
	{
		// Nothing to do
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see it.diunipi.volpi.sycamore.plugins.Algorithm#compute(java.util.Vector,
	 * it.diunipi.volpi.sycamore.model.Observation)
	 */
	@Override
	public Point3D compute(Vector<Observation<Point3D>> observations, SycamoreObservedRobot<Point3D> caller)
	{
		//try
		//{
			// look for a HumanPilot executing robot
			Point3D dest = new Point3D();
			Point3D H = null; 
			Point3D R = null;
			for (Observation<Point3D> observation : observations)
			{
				if (observation.isHumanPilot())
				{
					//System.out.println("SEEING HUMAN PILOT");
					if(observation.getRobotPosition().distanceTo(caller.getLocalPosition()) > 6)
						H = observation.getRobotPosition();
					else
						H = caller.getLocalPosition();
					
					//System.out.println("H: " + H.toString());
					//Point3D pos = caller.getLocalPosition();
					
					/*if (dest.compareTo(pos) == 0)
					{
						caller.turnLightOn(ColorRGBA.Yellow);
						System.out.println("Leader catch!");
					}*/
					
					//return dest;
				} else {
					//System.out.println("SEEING NO HUMAN PILOT");
					if(observation.getRobotPosition().distanceTo(caller.getLocalPosition()) < 4)
						R = observation.getRobotPosition();
					
					if (R != null) System.out.println("SOME FELLOW TOO CLOSE!!!! R: " + R.toString());
				}
				
				
				
				
				
				//System.out.println("dest: " + dest.toString() + " -- H: " + H.toString() +" -- R: " + R.toString());
			}
			
			if (R!=null){
				dest.x = H.x - R.x; 
				dest.y = H.y - R.y;
				dest.z = H.z - R.z;
			} else {
				System.out.println("NO FELLOW WAS TOO CLOSE....");
				dest = H;
			}
			
			return dest;
		//}
		/*catch (TooManyLightsException e)
		{
			System.out.println("Finished!");
			setFinished(true);
		}*/

		// here no leader has been found.
		//System.err.println("No leader found.");
		//return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.diunipi.volpi.sycamore.plugins.Algorithm#getReferences()
	 */
	@Override
	public String getReferences()
	{
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.diunipi.volpi.sycamore.plugins.Algorithm#getType()
	 */
	@Override
	public TYPE getType()
	{
		return TYPE.TYPE_3D;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.diunipi.volpi.sycamore.plugins.SycamorePlugin#getShortDescription()
	 */
	@Override
	public String getPluginShortDescription()
	{
		return "An algorithm that makes the executing robot follow a leader.";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.diunipi.volpi.sycamore.plugins.SycamorePlugin#getLongDescription()
	 */
	@Override
	public String getPluginLongDescription()
	{
		return "A simple algorithm where the robots look for the leader, identified by the human pilot robot, and reach the position where the leader is.";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.diunipi.volpi.sycamore.plugins.SycamorePlugin#getSettings()
	 */
	@Override
	public SycamorePanel getPanel_settings()
	{
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.diunipi.volpi.sycamore.plugins.SycamorePlugin#getAuthor()
	 */
	@Override
	public String getAuthor()
	{
		return "Valerio Volpi - vale.v@me.com";
	}

}
