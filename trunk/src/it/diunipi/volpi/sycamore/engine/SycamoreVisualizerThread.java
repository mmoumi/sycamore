package it.diunipi.volpi.sycamore.engine;

import it.diunipi.volpi.sycamore.gui.SycamoreSystem;

import java.util.Iterator;

/**
 * This class is the thread that runs in the Visualizer. Since in Visualizer there is no scheduler,
 * this thread cares about playing the animation by setting the appropriate ratios into robots.
 * 
 * @author Valerio Volpi - vale.v@me.com
 */
public class SycamoreVisualizerThread extends SycamoreSchedulerThread
{
	/*
	 * (non-Javadoc)
	 * 
	 * @see it.diunipi.volpi.sycamore.engine.SycamoreSchedulerThread#run()
	 */
	@Override
	public void run()
	{
		// scheduler starts paused. The user will have to press play to start
		this.pause();
		while (true)
		{
			// eventually return
			if (state == SCHEDULER_STATE.INTERRUPTED)
			{
				System.out.println("Scheduler returning...");
				return;
			}

			// check the state, if is paused or not
			checkState();

			if (engine != null)
			{
				// update robots ratios
				updateRobotsRatios();

				// sleep to have fixed frequency
				try
				{
					Thread.sleep((long) (SycamoreSystem.getSchedulerFrequency() * 1000));
				}
				catch (InterruptedException e)
				{
					e.printStackTrace();
				}
			}

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.diunipi.volpi.sycamore.engine.SycamoreSchedulerThread#manageSimulationFinished()
	 */
	@Override
	protected void manageSimulationFinished()
	{
		engine.manageSimulationFinished(false);
	}

	/**
	 * Updates the robots ratios in order to have an animation
	 */
	private void updateRobotsRatios()
	{
		Iterator<SycamoreRobot> iterator = engine.getRobots().robotsIterator();

		while (iterator.hasNext())
		{
			SycamoreRobot robot = iterator.next();

			float delta = SycamoreSystem.getSchedulerFrequency() * engine.getAnimationSpeedMultiplier() * (1.0f / robot.getTimelineDuration());

			float ratio = robot.getCurrentRatio();
			ratio = ratio + delta;

			if (ratio < 1)
			{
				robot.setCurrentRatio(ratio);
			}
		}
	}
}
