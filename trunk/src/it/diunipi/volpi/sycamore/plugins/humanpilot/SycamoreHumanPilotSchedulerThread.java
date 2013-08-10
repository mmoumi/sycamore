/**
 * 
 */
package it.diunipi.volpi.sycamore.plugins.humanpilot;

import it.diunipi.volpi.sycamore.engine.SycamoreEngine;
import it.diunipi.volpi.sycamore.gui.SycamoreSystem;

/**
 * The thread that manages the scheduler for human pilot. It owns the human pilot scheduler and
 * defines when to call the scheduler methods. This thread is also the one that interacts with the
 * GUI, in fact it stops the human pilot scheduler if the user presses the pause/stop button.
 * 
 * @author Valerio Volpi - vale.v@me.com
 */
public class SycamoreHumanPilotSchedulerThread extends Thread
{
	/**
	 * The possible states of the scheduler
	 * 
	 * @author Vale
	 */
	public static enum SCHEDULER_STATE
	{
		/**
		 * Scheduler not started yet
		 */
		NOT_STARTED,
		/**
		 * Scheduler running
		 */
		RUNNING,
		/**
		 * Scheduler paused
		 */
		PAUSED;
	}

	private SycamoreEngine	engine	= null;
	private SCHEDULER_STATE	state	= SCHEDULER_STATE.NOT_STARTED;

	/**
	 * @param engine
	 *            the engine to set
	 */
	public void setEngine(SycamoreEngine engine)
	{
		this.engine = engine;
	}

	/**
	 * @return the engine
	 */
	public SycamoreEngine getEngine()
	{
		return engine;
	}

	/**
	 * Check if the thread is in paused state, and in this case starts waiting
	 */
	private void checkState()
	{
		if (state == SCHEDULER_STATE.PAUSED)
		{
			this.doWaitGui();
		}
	}

	/**
	 * Waits until the GUI calls a <code>play()</code> method
	 */
	public void doWaitGui()
	{
		synchronized (SycamoreSystem.getSchedulerGuiSynchronizer())
		{
			try
			{
				SycamoreSystem.getSchedulerGuiSynchronizer().wait();
			}
			catch (InterruptedException e)
			{
				System.err.println("Interrupted thread while sleeping.");
			}
		}
	}

	/**
	 * Makes the scheduler stop waiting and start performing its iterations
	 */
	public void play()
	{
		this.state = SCHEDULER_STATE.RUNNING;
		synchronized (SycamoreSystem.getSchedulerGuiSynchronizer())
		{
			SycamoreSystem.getSchedulerGuiSynchronizer().notifyAll();
		}
	}

	/**
	 * @param state
	 *            the state to set
	 */
	public void pause()
	{
		this.state = SCHEDULER_STATE.PAUSED;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Thread#run()
	 */
	@Override
	public void run()
	{
		super.run();

		// scheduler starts paused. The user will have to press play to start
		this.pause();
		while (true)
		{
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
