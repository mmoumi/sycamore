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
 * @author Vale
 * 
 * @param <P>
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
	 * Returns the list of not moving robots
	 * 
	 * @return
	 */
	protected Vector<SycamoreRobot<P>> getNotMovingRobots()
	{
		Vector<SycamoreRobot<P>> ret = new Vector<SycamoreRobot<P>>();
		Iterator<SycamoreRobot<P>> iterator = appEngine.getRobots().robotsIterator();

		while (iterator.hasNext())
		{
			SycamoreRobot<P> robot = iterator.next();
			if (!robot.isMoving())
			{
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
	public void updateTimelines()
	{
		Iterator<SycamoreRobot<P>> iterator = appEngine.getRobots().robotsIterator();

		while (iterator.hasNext())
		{
			SycamoreRobot<P> robot = iterator.next();

			float delta = SycamoreSystem.getSchedulerFrequency() * appEngine.getAnimationSpeedMultiplier() * (1.0f / robot.getTimelineDuration());

			if (robot.isMoving())
			{
				float ratio = robot.getCurrentRatio();
				ratio = ratio + delta;

				if (ratio > 1.0f)
				{
					robot.setCurrentState(ROBOT_STATE.READY_TO_LOOK);
					ratio = 1.0f;
				}
				robot.setCurrentRatio(ratio);
			}
			else
			{
				robot.addPause(SycamoreSystem.getSchedulerFrequency() * appEngine.getAnimationSpeedMultiplier());
			}
		}
	}
}
