/**
 * 
 */
package it.diunipi.volpi.sycamore.engine;

import it.diunipi.volpi.sycamore.gui.SycamoreSystem;
import it.diunipi.volpi.sycamore.plugins.schedulers.SchedulerImpl;

/**
 * The thread that manages the scheduler for robots. It owns the scheduler selected by the users and
 * defines when to call its methods. This thread is also the one that interacts with the GUI, in
 * fact it stops the scheduler if the user presses the pause/stop button.
 * 
 * @author Valerio Volpi - vale.v@me.com
 */
public class SycamoreSchedulerThread extends Thread
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
		PAUSED,
		/**
		 * Scheduler interrupted
		 */
		INTERRUPTED;
	}

	protected SycamoreEngine	engine			= null;
	protected int				roundCounter	= 0;
	protected SCHEDULER_STATE	state			= SCHEDULER_STATE.NOT_STARTED;

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
	protected void checkState()
	{
		if (state == SCHEDULER_STATE.PAUSED)
		{
			this.doWaitGui();
		}
	}

	/**
	 * Wait after a GUI pressure of the pause button
	 */
	public synchronized void doWaitGui()
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
	 * Continue execution after a wait
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
	 * @see java.lang.Thread#interrupt()
	 */
	@Override
	public void interrupt()
	{
		if (this.state == SCHEDULER_STATE.PAUSED)
		{
			play();
		}

		this.state = SCHEDULER_STATE.INTERRUPTED;
	}

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
				System.out.println("Scheduler returning...");
				manageSimulationFinished();
				return;
			}

			// check the state, if is paused or not
			checkState();

			if (engine != null && engine.getCurrentScheduler() != null)
			{
				if (engine.isSimulationFinished())
				{
					// manage the simulation finished
					manageSimulationFinished();
				}
				else
				{
					SchedulerImpl schedulerImpl = (SchedulerImpl) engine.getCurrentScheduler();

					if (roundCounter == 0)
					{
						// manage the runloop start
						schedulerImpl.runLoop_pre();
						schedulerImpl.updateTimelines();
						engine.performMeasuresSimulationStart();
					}

					// runloop iteration
					schedulerImpl.runLoopIteration();
					schedulerImpl.updateTimelines();

					engine.performMeasuresSimulationStep();

					roundCounter++;

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

	/**
	 * Perform operations after a finish of the simulation
	 */
	protected void manageSimulationFinished()
	{
		if (engine != null)
		{
			SchedulerImpl schedulerImpl = (SchedulerImpl) engine.getCurrentScheduler();
			if (schedulerImpl != null)
			{
				schedulerImpl.runLoop_post();
				schedulerImpl.updateTimelines();
			}

			engine.performMeasuresSimulationEnd();
			engine.manageSimulationFinished(false);
		}

		roundCounter = 0;
		this.pause();
	}
}
