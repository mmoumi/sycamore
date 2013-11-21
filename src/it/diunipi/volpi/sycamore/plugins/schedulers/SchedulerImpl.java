package it.diunipi.volpi.sycamore.plugins.schedulers;

import it.diunipi.volpi.sycamore.engine.ComputablePoint;
import it.diunipi.volpi.sycamore.engine.SycamoreAbstractPoint;
import it.diunipi.volpi.sycamore.engine.SycamoreEngine;
import it.diunipi.volpi.sycamore.engine.SycamoreRobot;
import it.diunipi.volpi.sycamore.engine.SycamoreRobot.ROBOT_STATE;
import it.diunipi.volpi.sycamore.gui.SycamoreSystem;

import java.util.Iterator;
import java.util.Vector;

/**
 * A basic implementation of the <code>Scheduler</code> interface. It implements some methods using
 * default values. While implementing a plugin, it is not recommended to start directly from the
 * <code>Scheduler</code> interface, but it is suggested to extend the <code>SchedulerImpl</code>
 * class instead. This class offers a basic implementation of the <code>updateTimelines()</code>
 * method that moves the robots along linear paths at constant speed. The speed used is the one
 * selected for the robot.
 * 
 * @author Valerio Volpi - vale.v@me.com
 */
public abstract class SchedulerImpl<P extends SycamoreAbstractPoint & ComputablePoint<P>> implements Scheduler<P>
{
	protected SycamoreEngine<P>	appEngine	= null;

	/**
	 * @param appEngine
	 *            the engine to set
	 */
	public void setAppEngine(SycamoreEngine<P> appEngine)
	{
		this.appEngine = appEngine;
	}

	/**
	 * @return the engine
	 */
	public SycamoreEngine<P> getAppEngine()
	{
		return appEngine;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.diunipi.volpi.sycamore.plugins.SycamorePlugin#getPluginName()
	 */
	@Override
	public String getPluginName()
	{
		return getClass().getSimpleName();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.diunipi.volpi.sycamore.plugins.Algorithm#getTypeShortDescription()
	 */
	@Override
	public final String getPluginClassShortDescription()
	{
		return "SCHED";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.diunipi.volpi.sycamore.plugins.Algorithm#getTypeDescription()
	 */
	@Override
	public final String getPluginClassDescription()
	{
		return "Scheduler";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.diunipi.volpi.sycamore.plugins.SycamorePlugin#getPluginLongDescription()
	 */
	@Override
	public final String getPluginClassLongDescription()
	{
		return "Scheduler";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return getPluginName() + ": " + getPluginShortDescription();
	}

	/**
	 * Returns the list of not moving robots in the system.
	 * 
	 * @return
	 */
	protected Vector<SycamoreRobot<P>> getNotMovingRobots()
	{
		// prepare return vector
		Vector<SycamoreRobot<P>> ret = new Vector<SycamoreRobot<P>>();

		// take all the robots
		Iterator<SycamoreRobot<P>> iterator = appEngine.getRobots().robotsIterator();

		while (iterator.hasNext())
		{
			SycamoreRobot<P> robot = iterator.next();
			if (!robot.isMoving())
			{
				// if a robot is not moving, add it to return vector
				ret.add(robot);
			}
		}

		return ret;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.diunipi.volpi.sycamore.plugins.schedulers.Scheduler#updateTimelines()
	 */
	@Override
	public void moveRobots()
	{
		// take all the robots
		Iterator<SycamoreRobot<P>> iterator = appEngine.getRobots().robotsIterator();

		while (iterator.hasNext())
		{
			SycamoreRobot<P> robot = iterator.next();

			// compute the delta ratio, that is the difference between the current ratio and the
			// ratio that the robot will have at the end.
			float delta = SycamoreSystem.getSchedulerFrequency() * appEngine.getAnimationSpeedMultiplier() * (1.0f / robot.getTimelineDuration());

			if (robot.isMoving())
			{
				// if the robot is moving, compute its new ratio
				float ratio = robot.getCurrentRatio();
				ratio = ratio + delta;

				if (ratio > 1.0f)
				{
					// if ratio is higher than one, the robot reached its destination. So it is not
					// moving animore, but it becomes ready to look again
					robot.setCurrentState(ROBOT_STATE.READY_TO_LOOK);
					ratio = 1.0f;
				}

				// set the new robot ratio
				robot.setCurrentRatio(ratio);
			}
			else
			{
				// if the robot is not moving, register in the timeline that for the current
				// scheduler step's duration the robot did not move, by the addition of a pause
				// keyframe. This pause describes the "stay still" time of the robot.
				robot.addPause(SycamoreSystem.getSchedulerFrequency() * appEngine.getAnimationSpeedMultiplier());
			}
		}
	}
}
