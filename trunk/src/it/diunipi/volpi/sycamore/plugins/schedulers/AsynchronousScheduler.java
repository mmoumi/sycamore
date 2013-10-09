package it.diunipi.volpi.sycamore.plugins.schedulers;

import it.diunipi.volpi.sycamore.animation.Timeline;
import it.diunipi.volpi.sycamore.engine.ComputablePoint;
import it.diunipi.volpi.sycamore.engine.SycamoreAbstractPoint;
import it.diunipi.volpi.sycamore.engine.SycamoreRobot;
import it.diunipi.volpi.sycamore.gui.SycamorePanel;
import it.diunipi.volpi.sycamore.util.PropertyManager;
import it.diunipi.volpi.sycamore.util.SycamoreProperty;
import it.diunipi.volpi.sycamore.util.SycamoreUtil;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;

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
 * selection of the robots to be activated is performed through a random subset of the not moving
 * robots. To be more precise, the scheduler in its step takes the list of all the robots that are
 * not in MOVING state, computes a random subset of them, and activates them by calling one
 * operation on each of the selected robots. This scheduler has also four funcions that can be
 * activated by the user:
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
public class AsynchronousScheduler<P extends SycamoreAbstractPoint & ComputablePoint<P>> extends SchedulerImpl<P>
{
	/**
	 * Properties related to chirality agreement 2D
	 * 
	 * @author Valerio Volpi - vale.v@me.com
	 */
	private enum AsynchronousSchedulerProperties implements SycamoreProperty
	{
		ASYNCHRONOUS_SCHEDULER_RIGID("Rigid", "" + true), 
		ASYNCHRONOUS_SCHEDULER_CONTINUOUS("Continuous", "" + false), 
		ASYNCHRONOUS_SCHEDULER_CHANGES_ROBOT_SPEED("Changes robot speed", "" + false), 
		ASYNCHRONOUS_SCHEDULER_FAIR("Fair", "" + true);

		private String	description		= null;
		private String	defaultValue	= null;

		/**
		 * Constructor.
		 */
		AsynchronousSchedulerProperties(String description, String defaultValue)
		{
			this.description = description;
			this.defaultValue = defaultValue;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see it.diunipi.volpi.sycamore.util.SycamoreProperty#getDescription()
		 */
		@Override
		public String getDescription()
		{
			return description;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see it.diunipi.volpi.sycamore.util.SycamoreProperty#getDefaultValue()
		 */
		@Override
		public String getDefaultValue()
		{
			return defaultValue;
		}

	}

	private AsynchronousSchedulerSettingsPanel		panel_settings		= null;

	protected Hashtable<SycamoreRobot<P>, Boolean>	destinationModified	= new Hashtable<SycamoreRobot<P>, Boolean>();
	protected Hashtable<SycamoreRobot<P>, Boolean>	speedModified		= new Hashtable<SycamoreRobot<P>, Boolean>();

	/**
	 * @return the rigid
	 */
	public static boolean isRigid()
	{
		String rigid = PropertyManager.getSharedInstance().getProperty(AsynchronousSchedulerProperties.ASYNCHRONOUS_SCHEDULER_RIGID.name());
		if (rigid == null)
		{
			rigid = AsynchronousSchedulerProperties.ASYNCHRONOUS_SCHEDULER_RIGID.getDefaultValue();
		}

		return Boolean.parseBoolean(rigid);
	}

	/**
	 * @param rigid
	 *            the rigid to set
	 */
	public static void setRigid(boolean rigid)
	{
		PropertyManager.getSharedInstance().putProperty(AsynchronousSchedulerProperties.ASYNCHRONOUS_SCHEDULER_RIGID, rigid);
	}

	/**
	 * @return the continuous
	 */
	public static boolean isContinuous()
	{
		return PropertyManager.getSharedInstance().getBooleanProperty(AsynchronousSchedulerProperties.ASYNCHRONOUS_SCHEDULER_CONTINUOUS);
	}

	/**
	 * @param continuous
	 *            the continuous to set
	 */
	public static void setContinuous(boolean continuous)
	{
		PropertyManager.getSharedInstance().putProperty(AsynchronousSchedulerProperties.ASYNCHRONOUS_SCHEDULER_CONTINUOUS, continuous);
	}

	/**
	 * @return the changesRobotSpeed
	 */
	public static boolean isChangesRobotSpeed()
	{
		return PropertyManager.getSharedInstance().getBooleanProperty(AsynchronousSchedulerProperties.ASYNCHRONOUS_SCHEDULER_CHANGES_ROBOT_SPEED);
	}

	/**
	 * @param changesRobotSpeed
	 *            the changesRobotSpeed to set
	 */
	public static void setChangesRobotSpeed(boolean changesRobotSpeed)
	{
		PropertyManager.getSharedInstance().putProperty(AsynchronousSchedulerProperties.ASYNCHRONOUS_SCHEDULER_CHANGES_ROBOT_SPEED, changesRobotSpeed);
	}

	/**
	 * @return the fair
	 */
	public static boolean isFair()
	{
		return PropertyManager.getSharedInstance().getBooleanProperty(AsynchronousSchedulerProperties.ASYNCHRONOUS_SCHEDULER_FAIR);
	}

	/**
	 * @param fair
	 *            the fair to set
	 */
	public static void setFair(boolean fair)
	{
		PropertyManager.getSharedInstance().putProperty(AsynchronousSchedulerProperties.ASYNCHRONOUS_SCHEDULER_FAIR, fair);
	}

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
		return "This is an implementation of the Asynchronous scheduler. It implements the ASYNCH or CORDA model of Mobile Robots theory.";
	}

	/* (non-Javadoc)
	 * @see it.diunipi.volpi.sycamore.plugins.SycamorePlugin#getPluginLongDescription()
	 */
	@Override
	public String getPluginLongDescription()
	{
		return "This is an implementation of the Asynchronous scheduler. It implements the ASYNCH or CORDA model of Mobile Robots theory. In ASYNCH, each robot is activated "
				+ "asynchronously and independently from the other robots. Furthermore, the duration of each LOOK, COMPUTE, MOVE and SLEEP as well as the time that passes between "
				+ "successive activities in the same cycle is finite but otherwise unpredictable. As a result, computations can be made based on totally obsolete observations, taken "
				+ "arbitrarily far in the past. Another consequence is that robots can be seen while moving, creating further inconsistencies in the robot's understanding of the universe. "
				+ "From a technical point of view, in each of its step this scheduler can activate any number of robots, and can execute any of the available operations on each of them. The "
				+ "operations are always executed in the right order: LOOK-COMPUTE-MOVE-SLEEP, but each of them is executed by the scheduler in a different step, and there is no guarantee "
				+ "about the time that passes between two calls. The selection of the robots to be activated is performed through a random subset of the not moving robots. To be more precise, "
				+ "the scheduler in its step takes the list of all the robots that are not in MOVING state, computes a random subset of them, and activates them by calling one operation "
				+ "on each of the selected robots. This scheduler has also four funcions that can be activated by the user:\n" 
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.diunipi.volpi.sycamore.plugins.SycamorePlugin#getPanel_settings()
	 */
	@Override
	public SycamorePanel getPanel_settings()
	{
		if (panel_settings == null)
		{
			panel_settings = new AsynchronousSchedulerSettingsPanel();
		}
		return panel_settings;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.diunipi.volpi.sycamore.plugins.SchedulerImpl#runLoop_pre()
	 */
	@Override
	public synchronized void runLoop_pre()
	{
		// Nothing to do
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.diunipi.volpi.sycamore.plugins.SchedulerImpl#runLoopIteration()
	 */
	@Override
	public synchronized void runLoopIteration()
	{
		if (!appEngine.isSimulationFinished())
		{
			// get not moving robots
			Vector<SycamoreRobot<P>> notMovingRobots = getNotMovingRobots();
			Vector<SycamoreRobot<P>> robots = null;

			if (!isContinuous())
			{
				// if scheduler is not continuous, choose a subset of such robot
				// the subset can have fairness property or not, depending on the fair parameter
				if (isFair())
				{
					robots = SycamoreUtil.randomFairSubset(notMovingRobots);
				}
				else
				{
					robots = SycamoreUtil.randomSubset(notMovingRobots);
				}
			}
			else
			{
				// if scheduler is continuous, choose all the not moving robot
				robots = notMovingRobots;
			}

			// then calls next operation
			for (SycamoreRobot<P> robot : robots)
			{
				if (!robot.isFinished())
				{
					robot.nextOperation();
					destinationModified.put(robot, false);
					speedModified.put(robot, false);
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
	public synchronized void runLoop_post()
	{
		// Nothing to do
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.diunipi.volpi.sycamore.plugins.schedulers.SchedulerImpl#updateTimelines()
	 */
	@Override
	public void updateTimelines()
	{
		// modify the robots timelines
		Iterator<SycamoreRobot<P>> iterator = appEngine.getRobots().robotsIterator();

		while (iterator.hasNext())
		{
			SycamoreRobot<P> robot = iterator.next();

			if (robot.getCurrentRatio() < 1 && !isRigid())
			{
				// change the robot's destination
				changeRobotDestination(robot);
			}

			if (robot.getCurrentRatio() < 1 && isChangesRobotSpeed())
			{
				// change robot speed in this segment
				changeRobotSpeed(robot);
			}
		}

		// continue as usual
		super.updateTimelines();
	}

	/**
	 * This method changes the last destination point of passed robot, by selecting as the new
	 * destination any point that is on the segment between the robot's position and the old
	 * destination. The arrival time is recomputed in order to have a constant speed movement. This
	 * method performs a permanent modification of the timeline. It will never be possible to
	 * restore the old destintion after a call to this method.
	 * 
	 * @param robot
	 */
	private void changeRobotDestination(SycamoreRobot<P> robot)
	{
		// this operation is performed with a probability of 30 %
		int n = SycamoreUtil.getRandomInt(0, 100);
		if (n >= 70)
		{
			// get last and second to last keyframe in timeline
			Timeline<P> timeline = robot.getTimeline();
			Vector<P> points = timeline.getPoints();

			if (destinationModified.get(robot) != null && destinationModified.get(robot) == false && points.size() > 1 && robot.getCurrentState() == SycamoreRobot.ROBOT_STATE.READY_TO_MOVE
					&& !robot.isFinished())
			{
				int lastKeyframe = timeline.getNumKeyframes() - 1;
				boolean lastpause = timeline.isPause(lastKeyframe);

				if (lastpause)
				{
					lastKeyframe--;
				}

				P lastPoint = points.elementAt(points.size() - 1);
				P secondToLastPoint = points.elementAt(points.size() - 2);

				// get an intermediate point between these 2 points
				P newPoint = SycamoreUtil.getRandomIntermediatePoint(lastPoint, secondToLastPoint);

				// recompute the distance between second to last and new point
				float distance = secondToLastPoint.distanceTo(newPoint);

				// recompute the time to reach newPoint
				float newTime = (distance / robot.getSpeed());

				// update timeline
				timeline.editKeyframe(lastKeyframe, newPoint, newTime);

				robot.setCurrentRatio(timeline.getRatioOfKeyframe(lastKeyframe - 1));

				destinationModified.put(robot, true);
			}
		}
	}

	/**
	 * This method changes the speed of the passed robot. The destination point's arrival time is
	 * changed in order to have the robot reach it slowly. The new speed is always lower than the
	 * robot's default speed but it is never equal to zero.
	 * 
	 * @param robot
	 */
	private void changeRobotSpeed(SycamoreRobot<P> robot)
	{
		// compute the new speed as a random number between a very tiny value and the speed value
		// set by the user
		float newSpeed = SycamoreUtil.getRandomFloat(0.01f, robot.getSpeed());

		// get last and second to last keyframe in timeline
		Timeline<P> timeline = robot.getTimeline();
		Vector<P> points = timeline.getPoints();

		if (speedModified.get(robot) != null && speedModified.get(robot) == false && points.size() > 1 && robot.getCurrentState() == SycamoreRobot.ROBOT_STATE.READY_TO_MOVE && !robot.isFinished())
		{
			int lastKeyframe = timeline.getNumKeyframes() - 1;
			boolean lastpause = timeline.isPause(lastKeyframe);

			if (lastpause)
			{
				lastKeyframe--;
			}

			P lastPoint = points.elementAt(points.size() - 1);
			P secondToLastPoint = points.elementAt(points.size() - 2);

			// recompute the distance between them
			float distance = secondToLastPoint.distanceTo(lastPoint);

			// recompute the time to cover that distance with new speed, and set the new time in
			// timeline
			float time = (distance / newSpeed);

			timeline.editKeyframe(lastKeyframe, lastPoint, time);

			robot.setCurrentRatio(timeline.getRatioOfKeyframe(lastKeyframe - 1));

			speedModified.put(robot, true);
		}
	}
}
