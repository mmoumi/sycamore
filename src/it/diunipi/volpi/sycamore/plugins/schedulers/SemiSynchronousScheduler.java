package it.diunipi.volpi.sycamore.plugins.schedulers;

import it.diunipi.volpi.sycamore.engine.ComputablePoint;
import it.diunipi.volpi.sycamore.engine.SycamoreAbstractPoint;
import it.diunipi.volpi.sycamore.engine.SycamoreRobot;
import it.diunipi.volpi.sycamore.gui.SycamorePanel;
import it.diunipi.volpi.sycamore.util.SycamoreUtil;

import java.util.Iterator;
import java.util.Vector;

import net.xeoh.plugins.base.annotations.PluginImplementation;

/**
 * This is an implementation of the Semi-Synchronous scheduler. It implements the model in theory
 * that is called SSYNCH, SYm or ATOM. At each step of the scheduler, a subset of the robots are
 * activated, and they obtain the same snapshot of the environment. The same subset of the robots
 * executes the COMPUTE and MOVE operation synchronously, and the step of the scheduler ends just
 * when all the robots reach their destination. As a consequence, no robot will ever be observed
 * while moving, and the understanding of the active robots is always consistent.
 * 
 *  * @see Paola Flocchini, Giuseppe Prencipe, Nicola Santoro - Distributed Computing by Oblivious
 *      Mobile Robots, Morgan&Claypool publishers, 2012
 * 
 * @author Vale
 */
@PluginImplementation
public class SemiSynchronousScheduler<P extends SycamoreAbstractPoint & ComputablePoint<P>> extends SchedulerImpl<P>
{
	/* (non-Javadoc)
	 * @see it.diunipi.volpi.sycamore.plugins.SycamorePlugin#getAuthor()
	 */
	@Override
	public String getAuthor()
	{
		return "Valerio Volpi";
	}

	/* (non-Javadoc)
	 * @see it.diunipi.volpi.sycamore.plugins.SycamorePlugin#getPluginShortDescription()
	 */
	@Override
	public String getPluginShortDescription()
	{
		return "A Semy-Sinchronous scheduler. It implements the SSYNCH (SYm, ATOM) model.";
	}

	/* (non-Javadoc)
	 * @see it.diunipi.volpi.sycamore.plugins.SycamorePlugin#getPluginLongDescription()
	 */
	@Override
	public String getPluginLongDescription()
	{
		return "This is an implementation of the Semi-Synchronous scheduler. It implements the model in theory that is called SSYNCH, SYm or ATOM.\n" +
				"At each step of the scheduler, a subset of the robots are activated, and they obtain the same snapshot of the environment. " +
				"The same subset of the robots executes the COMPUTE and MOVE operation synchronously, and the step of the scheduler ends just when " +
				"all the robots reach their destination. As a consequence, no robot will ever be observed while moving, and the understanding of " +
				"the active robots is always consistent.";
	}

	/* (non-Javadoc)
	 * @see it.diunipi.volpi.sycamore.plugins.SycamorePlugin#getPanel_settings()
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
		return "SemiSynchronousScheduler";
	}

	/**
	 * Returns true just if no robot in the system is in MOVING state
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
		// Nothing to do
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.diunipi.volpi.sycamore.plugins.SchedulerImpl#runLoopIteration()
	 */
	public void runLoopIteration()
	{
		// the step is performed just if no robot is moving
		if (!appEngine.isSimulationFinished() && noneIsMoving())
		{
			Vector<SycamoreRobot<P>> robots = SycamoreUtil.randomFairSubset(appEngine.getRobots().toRobotsVector());

			Iterator<SycamoreRobot<P>> iterator = robots.iterator();

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
		// Nothing to do
	}
}
