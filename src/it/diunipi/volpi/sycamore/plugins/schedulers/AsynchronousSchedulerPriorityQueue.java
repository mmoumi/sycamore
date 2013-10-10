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
 * This is an implementation of the Asynchronous scheduler. It implements the ASYNCH or CORDA model
 * of Mobile Robots theory. In ASYNCH, each robot is activated asynchronously and independently from
 * the other robots. Furthermore, the duration of each LOOK, COMPUTE, MOVE and SLEEP as well as the
 * time that passes between successive activities in the same cycle is finite but otherwise
 * unpredictable. As a result, computations can be made based on totally obsolete observations,
 * taken arbitrarily far in the past. Another consequence is that robots can be seen while moving,
 * creating further inconsistencies in the robot's understanding of the universe. From a technical
 * point of view, in each of its step this scheduler can activate any number of robots, and can
 * execute any of the available operations on each of them. The operations are always executed in
 * the right order: LOOK-COMPUTE-MOVE-SLEEP, but each of them is executed by the scheduler in a
 * different step, and there is no guarantee about the time that passes between two calls. The
 * selection of the robots to be activated is performed through the usage of a priority queue where
 * just the not moving robots are stored. The weights associated to each robot are random, and they
 * are decreased at each step of the scheduler. When the weight associated to a robot becomes zero,
 * the robot is dequeued and activated. This scheduler has also four functions that can be activated
 * by the user:
 * <ul>
 * <li>RIGIDITY: If the scheduler is rigid, it makes a robot always perform a rigid movement, that
 * is, it makes a robot always reach its destination point. If otherwise the rigidity feature is
 * disabled, a robot can stop its movement before reaching its destination point. When a robot
 * stops, it always goes to the LOOK step, even if the destination point is not reached.</li>
 * <li>CONTINUITY: If the scheduler is continuous, a robot skips the SLEEP phase at the end of its
 * movement and it immediately jumps to the LOOK phase after a MOVE. If otherwise the scheduler is
 * not continuous, it can sleep for an arbitrary time before performing a new LOOK operation.</li>
 * <li>SPEED VARIANCE: The speed variance feature makes the scheduler decrease the speed of the
 * robots. The robot will still reach its destination (at least if RIGIDITY is enabled) but it could
 * take a lot of time to arrive to the final position. The speed can be arbitrarily changed, but it
 * will never be zero and it will never become higher than the robot's default speed. If this
 * feature is disabled, each robot moves at its own default constant speed.</li>
 * <li>FAIRNESS: The fairness is the ability of the scheduler to guarantee that a robot will never
 * sleep for an infinite amount of time. With FAIRNESS feature enabled, the time that passes between
 * two calls of the robot's methods will never exceed a predefined and well-known value. If this
 * feature is disabled, a robot could theoretically stay without moving for an infinite time.</li>
 * </ul>
 * 
 * * @see Paola Flocchini, Giuseppe Prencipe, Nicola Santoro - Distributed Computing by Oblivious
 * Mobile Robots, Morgan&Claypool publishers, 2012
 * 
 * @author Vale
 */
@PluginImplementation
public class AsynchronousSchedulerPriorityQueue<P extends SycamoreAbstractPoint & ComputablePoint<P>> extends AsynchronousScheduler<P>
{
	/**
	 * This class represents the timer that is used in the priority queue to count the weights that
	 * are assigned to the robots. The weights are simply equal to a number of milliseconds that
	 * shall pass before the robot is activated.
	 * 
	 * @author Valerio Volpi - vale.v@me.com
	 */
	private class RobotTimer implements Comparable<RobotTimer>
	{
		private SycamoreRobot<P>	robot	= null;
		private long				millis	= 0;

		/**
		 * Constructor.
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
		 * @param millis
		 *            the millis to set
		 */
		public void setMillis(long millis)
		{
			this.millis = millis;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Comparable#compareTo(java.lang.Object)
		 */
		@Override
		public int compareTo(RobotTimer o)
		{
			return (int) (this.millis - o.getMillis());
		}
	}

	private PriorityBlockingQueue<RobotTimer>	queue	= new PriorityBlockingQueue<RobotTimer>();

	/**
	 * @return a new weight for the robots
	 */
	private long getNewRobotWeight()
	{
		if (isContinuous())
		{
			// if the scheduler is continuous the weight is zero, to have the robot start immediately.
			return 0;
		}
		else
		{
			if (isFair())
			{
				// return a weight between zero and the fainess value
				int fairnessCount = PropertyManager.getSharedInstance().getIntegerProperty(ApplicationProperties.FAIRNESS_COUNT);
				return SycamoreUtil.getRandomLong(0, (long) (SycamoreSystem.getSchedulerFrequency() * 1000 * fairnessCount));
			}
			else
			{
				// return a weight between zero and infinity
				return SycamoreUtil.getRandomLong(0, Long.MAX_VALUE);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
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
				for (RobotTimer robotTimer : this.queue)
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
					RobotTimer robotTimer = new RobotTimer(robot, getNewRobotWeight());
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
	
	/* (non-Javadoc)
	 * @see it.diunipi.volpi.sycamore.plugins.SycamorePlugin#getPluginShortDescription()
	 */
	@Override
	public String getPluginShortDescription()
	{
		return "This is an implementation of the Asynchronous scheduler. It implements the ASYNCH or CORDA model using a priority queue.";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * it.diunipi.volpi.sycamore.plugins.schedulers.AsynchronousScheduler#getPluginLongDescription()
	 */
	@Override
	public String getPluginLongDescription()
	{
		return "This is an implementation of the Asynchronous scheduler. It implements the ASYNCH or CORDA model of Mobile Robots theory.\nIn ASYNCH, each robot is activated "
				+ "asynchronously and independently from the other robots. Furthermore, the duration of each LOOK, COMPUTE, MOVE and SLEEP as well as the time that passes between "
				+ "successive activities in the same cycle is finite but otherwise unpredictable. As a result, computations can be made based on totally obsolete observations, taken "
				+ "arbitrarily far in the past. Another consequence is that robots can be seen while moving, creating further inconsistencies in the robot's understanding of the universe.\n"
				+ "From a technical point of view, in each of its step this scheduler can activate any number of robots, and can execute any of the available operations on each of them. The "
				+ "operations are always executed in the right order: LOOK-COMPUTE-MOVE-SLEEP, but each of them is executed by the scheduler in a different step, and there is no guarantee "
				+ "about the time that passes between two calls.\nThe selection of the robots to be activated is performed through the usage of a priority queue where just the not moving "
				+ "robots are stored. The weights associated to each robot are random, and they are decreased at each step of the scheduler. When the weight associated to a robot becomes zero, "
				+ "the robot is dequeued and activated. This scheduler has also four funcions that can be activated by the user:\n"
				+ "- RIGIDITY: If the scheduler is rigid, it makes a robot always perform a rigid movement, that is, it makes a robot always reach its destination point. If otherwise "
				+ "the rigidity feature is disabled, a robot can stop its movement before reaching its destination point. When a robot stops, it always goes to the LOOK step, even if "
				+ "the destination point is not reached.\n"
				+ "- CONTINUITY: If the scheduler is continuous, a robot skips the SLEEP phase at the end of its movement and it immediately jumps to the LOOK phase after a MOVE. If "
				+ "otherwise the scheduler is not continuous, it can sleep for an arbitrary time before performing a new LOOK operation.\n"
				+ "- SPEED VARIANCE: The speed variance feature makes the scheduler decrease the speed of the robots. The robot will still reach its destination (at least if RIGIDITY is enabled) "
				+ "but it could take a lot of time to arrive to the final position. The speed can be arbitrarily changed, but it will never be zero and it will never become higher than the robot's "
				+ "default speed. If this feature is disabled, each robot moves at its own default constant speed.\n"
				+ "- FAIRNESS: The fairness is the ability of the scheduler to guarantee that a robot will never sleep for an infinite amount of time. With FAIRNESS feature enabled, the time "
				+ "that passes between two calls of the robot's methods will never exceed a predefined and well-known value. If this feature is disabled, a robot could theoretically stay without "
				+ "moving for an infinite time.\n";
	}
}
