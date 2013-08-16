/**
 * 
 */
package it.diunipi.volpi.sycamore.plugins.humanpilot;

import it.diunipi.volpi.sycamore.gui.SycamoreSystem;
import it.diunipi.volpi.sycamore.plugins.schedulers.SycamoreSchedulerThread;

/**
 * The thread that manages the scheduler for human pilot. It owns the human pilot scheduler and
 * defines when to call the scheduler methods. This thread is also the one that interacts with the
 * GUI, in fact it stops the human pilot scheduler if the user presses the pause/stop button.
 * 
 * @author Valerio Volpi - vale.v@me.com
 */
public class SycamoreHumanPilotSchedulerThread extends SycamoreSchedulerThread
{
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Thread#run()
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
				System.out.println("Human pilot scheduler returning...");
				return;
			}
			
			checkState();

			if (engine != null)
			{
				HumanPilotScheduler humanPilotScheduler = engine.getHumanPilotScheduler();

				humanPilotScheduler.runLoopIteration();
				humanPilotScheduler.updateTimelines();

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
}
