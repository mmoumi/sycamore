package it.diunipi.volpi.sycamore.plugins.schedulers;

import it.diunipi.volpi.sycamore.animation.Timeline;
import it.diunipi.volpi.sycamore.gui.SycamorePanel;
import it.diunipi.volpi.sycamore.model.ComputablePoint;
import it.diunipi.volpi.sycamore.model.SycamoreAbstractPoint;
import it.diunipi.volpi.sycamore.model.SycamoreRobot;
import it.diunipi.volpi.sycamore.util.PropertyManager;
import it.diunipi.volpi.sycamore.util.SycamoreProperty;
import it.diunipi.volpi.sycamore.util.SycamoreUtil;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;

import net.xeoh.plugins.base.annotations.PluginImplementation;

/**
 * This is an implementation of the asynchronous scheduler.
 * 
 * @author Vale
 */
@PluginImplementation
public class AsynchronousScheduler<P extends SycamoreAbstractPoint & ComputablePoint<P>> extends SchedulerImpl<P>
{
	private enum AsynchronousSchedulerProperties implements SycamoreProperty
	{
		RIGID("Rigid", "" + true), CONTINUOUS("Continuous", "" + true), CHANGES_ROBOT_SPEED("Changes robot speed", "" + true), FAIR("Fair", "" + true);

		private String	name			= null;
		private String	defaultValue	= null;

		/**
		 * 
		 */
		AsynchronousSchedulerProperties(String name, String defaultValue)
		{
			this.name = name;
			this.defaultValue = defaultValue;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see it.diunipi.volpi.sycamore.util.SycamoreProperty#getName()
		 */
		@Override
		public String getName()
		{
			return name;
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

	private static boolean							rigid				= PropertyManager.getSharedInstance().getBooleanProperty(AsynchronousSchedulerProperties.RIGID.getName());
	private static boolean							continuous			= PropertyManager.getSharedInstance().getBooleanProperty(AsynchronousSchedulerProperties.CONTINUOUS.getName());
	private static boolean							changesRobotSpeed	= PropertyManager.getSharedInstance().getBooleanProperty(AsynchronousSchedulerProperties.CHANGES_ROBOT_SPEED.getName());
	private static boolean							fair				= PropertyManager.getSharedInstance().getBooleanProperty(AsynchronousSchedulerProperties.FAIR.getName());
	private AsynchronousSchedulerSettingsPanel		panel_settings		= null;

	private Hashtable<SycamoreRobot<P>, Boolean>	destinationModified	= new Hashtable<SycamoreRobot<P>, Boolean>();
	private Hashtable<SycamoreRobot<P>, Boolean>	speedModified		= new Hashtable<SycamoreRobot<P>, Boolean>();

	/**
	 * @return the rigid
	 */
	public static boolean isRigid()
	{
		return rigid;
	}

	/**
	 * @param rigid
	 *            the rigid to set
	 */
	public static void setRigid(boolean rigid)
	{
		AsynchronousScheduler.rigid = rigid;
		PropertyManager.getSharedInstance().putProperty(AsynchronousSchedulerProperties.RIGID.getName(), rigid);
	}

	/**
	 * @return the continuous
	 */
	public static boolean isContinuous()
	{
		return continuous;
	}

	/**
	 * @param continuous
	 *            the continuous to set
	 */
	public static void setContinuous(boolean continuous)
	{
		AsynchronousScheduler.continuous = continuous;
		PropertyManager.getSharedInstance().putProperty(AsynchronousSchedulerProperties.CONTINUOUS.getName(), continuous);
	}

	/**
	 * @return the changesRobotSpeed
	 */
	public static boolean isChangesRobotSpeed()
	{
		return changesRobotSpeed;
	}

	/**
	 * @param changesRobotSpeed
	 *            the changesRobotSpeed to set
	 */
	public static void setChangesRobotSpeed(boolean changesRobotSpeed)
	{
		AsynchronousScheduler.changesRobotSpeed = changesRobotSpeed;
		PropertyManager.getSharedInstance().putProperty(AsynchronousSchedulerProperties.CHANGES_ROBOT_SPEED.getName(), changesRobotSpeed);
	}

	/**
	 * @return the fair
	 */
	public static boolean isFair()
	{
		return fair;
	}

	/**
	 * @param fair
	 *            the fair to set
	 */
	public static void setFair(boolean fair)
	{
		AsynchronousScheduler.fair = fair;
		PropertyManager.getSharedInstance().putProperty(AsynchronousSchedulerProperties.FAIR.getName(), fair);
	}

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
		return "An asynchronous scheduler";
	}

	/**
	 * @return
	 */
	@Override
	public String getPluginLongDescription()
	{
		return "An asynchronous scheduler where robots can stay without moving.";
	}

	/**
	 * @return
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
	 * @see it.diunipi.volpi.sycamore.plugins.SycamorePlugin#getPluginName()
	 */
	@Override
	public String getPluginName()
	{
		return "AsynchronousScheduler";
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

			if (!isRigid())
			{
				// change the robot's destination
				changeRobotDestination(robot);
			}

			if (isChangesRobotSpeed())
			{
				// change robot speed in this segment
				changeRobotSpeed(robot);
			}
		}

		// continue as usual
		super.updateTimelines();
	}

	/**
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

			if (destinationModified.get(robot) != null && destinationModified.get(robot) == false && points.size() > 1 && robot.getCurrentState() == SycamoreRobot.ROBOT_STATE.READY_TO_MOVE && !robot.isFinished())
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
