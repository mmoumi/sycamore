package it.diunipi.volpi.sycamore.plugins.schedulers;

import it.diunipi.volpi.sycamore.gui.SycamorePanel;
import it.diunipi.volpi.sycamore.model.ComputablePoint;
import it.diunipi.volpi.sycamore.model.SycamoreAbstractPoint;
import it.diunipi.volpi.sycamore.model.SycamoreRobot;

import java.util.Iterator;

import net.xeoh.plugins.base.annotations.PluginImplementation;

/**
 * This is an implementation of the fully synchronous scheduler. It chooses which robots are allowed
 * to move using a priority queue.
 * 
 * @author Vale
 */
@PluginImplementation
public class FullySynchronousScheduler<P extends SycamoreAbstractPoint & ComputablePoint<P>> extends SchedulerImpl<P>
{
	/**
	 * @return
	 */
	@Override
	public String getAuthor()
	{
		return "Valerio Volpi";
	}

	/**
	 * @return
	 */
	@Override
	public String getPluginShortDescription()
	{
		return "A fully sinchronous scheduler";
	}

	/**
	 * @return
	 */
	@Override
	public String getPluginLongDescription()
	{
		return "A fully sinchronous scheduler";
	}

	/**
	 * @return
	 */
	@Override
	public SycamorePanel getPanel_settings()
	{
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.diunipi.volpi.sycamore.plugins.SycamorePlugin#getPluginName()
	 */
	@Override
	public String getPluginName()
	{
		return "FullySynchronousScheduler";
	}

	/**
	 * 
	 */
	private boolean noneIsMoving()
	{
		Iterator<SycamoreRobot<P>> iterator = appEngine.getRobots().robotsIterator();

		while (iterator.hasNext())
		{
			SycamoreRobot<P> robot = iterator.next();
			if (robot.isMoving())
			{
				return false;
			}
		}

		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.diunipi.volpi.sycamore.plugins.Scheduler#runLoop_pre()
	 */
	@Override
	public void runLoop_pre()
	{

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.diunipi.volpi.sycamore.plugins.SchedulerImpl#runLoopIteration()
	 */
	@Override
	public synchronized void runLoopIteration()
	{
		if (!appEngine.isSimulationFinished() && noneIsMoving())
		{
			Iterator<SycamoreRobot<P>> iterator = appEngine.getRobots().robotsIterator();

			while (iterator.hasNext())
			{
				SycamoreRobot<P> robot = iterator.next();

				if (!robot.isFinished())
				{
					// look phase
					robot.doLook();

					// compute phase
					robot.doCompute();

					if (!robot.isFinished())
					{
						// move phase, if not finished
						robot.doMove();
					}
				}
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.diunipi.volpi.sycamore.plugins.Scheduler#runLoop_post()
	 */
	@Override
	public void runLoop_post()
	{

	}
}