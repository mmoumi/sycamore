/**
 * 
 */
package it.diunipi.volpi.sycamore.plugins.schedulers;

import it.diunipi.volpi.sycamore.engine.ComputablePoint;
import it.diunipi.volpi.sycamore.engine.SycamoreAbstractPoint;
import it.diunipi.volpi.sycamore.engine.SycamoreRobot;
import it.diunipi.volpi.sycamore.gui.SycamoreSystem;
import it.diunipi.volpi.sycamore.util.ApplicationProperties;
import it.diunipi.volpi.sycamore.util.PropertyManager;
import it.diunipi.volpi.sycamore.util.SycamoreUtil;

import java.util.Vector;
import java.util.concurrent.PriorityBlockingQueue;

import net.xeoh.plugins.base.annotations.PluginImplementation;

/**
 * @author Vale
 *
 */
@PluginImplementation
public class AsynchronousSchedulerPriorityQueue<P extends SycamoreAbstractPoint & ComputablePoint<P>> extends AsynchronousScheduler<P>
{
	private class RobotTimer implements Comparable<RobotTimer>
	{
		private SycamoreRobot<P> robot = null;
		private long millis = 0;
		
		/**
		 * 
		 */
		public RobotTimer(SycamoreRobot<P> robot, long millis)
		{
			this.robot = robot;
			this.millis = millis;
		}
		
		/**
		 * @return the robot
		 */
		public SycamoreRobot<P> getRobot()
		{
			return robot;
		}
		
		/**
		 * @return the millis
		 */
		public long getMillis()
		{
			return millis;
		}
		
		/**
		 * @param millis the millis to set
		 */
		public void setMillis(long millis)
		{
			this.millis = millis;
		}
		
		/* (non-Javadoc)
		 * @see java.lang.Comparable#compareTo(java.lang.Object)
		 */
		@Override
		public int compareTo(RobotTimer o)
		{
			return (int) (this.millis - o.getMillis());
		}
	}
	
	private PriorityBlockingQueue<RobotTimer> queue = new PriorityBlockingQueue<RobotTimer>();
	
	/**
	 * @return
	 */
	private long getTimer()
	{
		if (isContinuous())
		{
			return 0;
		}
		else
		{
			if (isFair())
			{
				int fairnessCount = PropertyManager.getSharedInstance().getIntegerProperty(ApplicationProperties.FAIRNESS_COUNT);
				return SycamoreUtil.getRandomLong(0, (long) (SycamoreSystem.getSchedulerFrequency() * 1000 * fairnessCount));
			}
			else
			{
				return SycamoreUtil.getRandomLong(0, Long.MAX_VALUE);
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see it.diunipi.volpi.sycamore.plugins.schedulers.AsynchronousScheduler#runLoopIteration()
	 */
	@Override
	public synchronized void runLoopIteration()
	{
		if (!appEngine.isSimulationFinished())
		{
			// get not moving robots
			Vector<SycamoreRobot<P>> notMovingRobots = getNotMovingRobots();
			Vector<SycamoreRobot<P>> robots = new Vector<SycamoreRobot<P>>();
			
			// add all not moving robots in the queue
			for (SycamoreRobot<P> robot : notMovingRobots)
			{
				boolean contained = false;
				
				// check if robot is already in queue
				for (RobotTimer robotTimer: this.queue)
				{
					if (robotTimer.getRobot() == robot)
					{
						contained = true;
						break;
					}
				}
				
				// if not, add
				if (!contained)
				{
					RobotTimer robotTimer = new RobotTimer(robot, getTimer());
					this.queue.add(robotTimer);
				}
			}
			
			// decrement all the timers and extract the zero timer robots
			for (RobotTimer robotTimer : this.queue)
			{
				robotTimer.setMillis((long) (robotTimer.getMillis() - (SycamoreSystem.getSchedulerFrequency() * 1000)));
				if (robotTimer.getMillis() <= 0)
				{
					robots.add(robotTimer.getRobot());
				}
			}
			
			// call next operation on the robot 
			for (SycamoreRobot<P> robot : robots)
			{
				if (!robot.isFinished())
				{
					// call next and save robot for speed and destination changes
					robot.nextOperation();
					destinationModified.put(robot, false);
					speedModified.put(robot, false);
				}
			}
			
			// eliminate all the moving robots
			for (RobotTimer robotTimer : this.queue)
			{
				if (robotTimer.getRobot().isMoving())
				{
					queue.remove(robotTimer);
				}
			}
		}
	}
}
