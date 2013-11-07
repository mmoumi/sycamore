package it.diunipi.volpi.sycamore.plugins.algorithms;

import it.diunipi.volpi.sycamore.engine.Observation;
import it.diunipi.volpi.sycamore.engine.Point2D;
import it.diunipi.volpi.sycamore.engine.SycamoreEngine.TYPE;
import it.diunipi.volpi.sycamore.engine.SycamoreObservedRobot;
import it.diunipi.volpi.sycamore.gui.SycamorePanel;
import it.diunipi.volpi.sycamore.gui.SycamoreSystem;
import it.diunipi.volpi.sycamore.gui.SycamoreSystem.TIMELINE_MODE;
import it.diunipi.volpi.sycamore.util.ApplicationProperties;
import it.diunipi.volpi.sycamore.util.PropertyManager;
import it.diunipi.volpi.sycamore.util.SycamoreUtil;

import java.util.Vector;

import net.xeoh.plugins.base.annotations.PluginImplementation;

/**
 * A simple 2D algorithm where the robots look for the leader, identified by the human pilot robot,
 * and goes in the direction where the leader is. The computed destination is such that the robot
 * goes just for a short distance in the detected direction and then it looks again. This is done
 * with the purpose of not missing the caught leader.
 * 
 * @author Valerio Volpi - vale.v@me.com
 */
@PluginImplementation
public class Following extends AlgorithmImpl<Point2D>
{
	private static final boolean MOVE = true;
	private static final boolean ROTATE = false;
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see it.diunipi.volpi.sycamore.plugins.algorithms.Algorithm#init()
	 */
	@Override
	public void init()
	{
		SycamoreSystem.setTimelineMode(TIMELINE_MODE.LIVE);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.diunipi.volpi.sycamore.plugins.Algorithm#compute(java.util.Vector,
	 * it.diunipi.volpi.sycamore.model.Observation)
	 */
	@Override
	public Point2D compute(Vector<Observation<Point2D>> snapshot, SycamoreObservedRobot<Point2D> callee)
	{
		// look for a HumanPilot executing robot
		for (Observation<Point2D> observation : snapshot)
		{
			if (observation.isHumanPilot())
			{			
				Point2D robotPosition = callee.getLocalPosition();
				Point2D observationPosition = observation.getRobotPosition();

				if (robotPosition.distanceTo(observationPosition) > 1)
				{
					Point2D destination = robotPosition.interpolateWith(observationPosition, 0.1f);
					return destination;
				}
				else
					return observationPosition;
			}
		}
		// here no leader has been found.
		
		if (MOVE)
		{
			// move randomly, like the human pilot
			Point2D position = callee.getLocalPosition();
			
			int lowerBoundX = PropertyManager.getSharedInstance().getIntegerProperty(ApplicationProperties.INITIAL_POSITION_MIN_X);
			int upperBoundX = PropertyManager.getSharedInstance().getIntegerProperty(ApplicationProperties.INITIAL_POSITION_MAX_X);
			int lowerBoundY = PropertyManager.getSharedInstance().getIntegerProperty(ApplicationProperties.INITIAL_POSITION_MIN_Y);
			int upperBoundY = PropertyManager.getSharedInstance().getIntegerProperty(ApplicationProperties.INITIAL_POSITION_MAX_Y);
			
			float minX = Math.max(lowerBoundX, position.x - 4.0f);
			float maxX = Math.min(upperBoundX, position.x + 4.0f);
			float minY = Math.max(lowerBoundY, position.y - 4.0f);
			float maxY = Math.min(upperBoundY, position.y + 4.0f);
			
			return SycamoreUtil.getRandomPoint2D(minX, maxX, minY, maxY);
		}
		else
		{
			return null;
		}
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
		return TYPE.TYPE_2D;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.diunipi.volpi.sycamore.plugins.SycamorePlugin#getShortDescription()
	 */
	@Override
	public String getPluginShortDescription()
	{
		return "An algorithm that makes the executing robot follow a leader, trying not to miss it.";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.diunipi.volpi.sycamore.plugins.SycamorePlugin#getLongDescription()
	 */
	@Override
	public String getPluginLongDescription()
	{
		return "A simple 2D algorithm where the robots look for the leader, identified by the human pilot robot, and goes in the direction where "
				+ "the leader is. The computed destination is such that the robot goes just for a short distance in the detected direction and then it "
				+ "looks again. This is done with the purpose of not missing the caught leader.";
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
