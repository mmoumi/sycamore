package it.diunipi.volpi.sycamore.plugins.algorithms;

import it.diunipi.volpi.sycamore.engine.Observation;
import it.diunipi.volpi.sycamore.engine.Point2D;
import it.diunipi.volpi.sycamore.engine.SycamoreEngine.TYPE;
import it.diunipi.volpi.sycamore.engine.SycamoreObservedLight;
import it.diunipi.volpi.sycamore.engine.SycamoreObservedRobot;
import it.diunipi.volpi.sycamore.gui.SycamorePanel;
import it.diunipi.volpi.sycamore.gui.SycamoreSystem;
import it.diunipi.volpi.sycamore.gui.SycamoreSystem.TIMELINE_MODE;
import it.diunipi.volpi.sycamore.plugins.visibilities.DirectionalVisibility;
import it.diunipi.volpi.sycamore.util.ApplicationProperties;
import it.diunipi.volpi.sycamore.util.PropertyManager;
import it.diunipi.volpi.sycamore.util.SycamoreProperty;
import it.diunipi.volpi.sycamore.util.SycamoreUtil;

import java.util.Iterator;
import java.util.Vector;

import net.xeoh.plugins.base.annotations.PluginImplementation;

import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;

/**
 * A simple 2D algorithm where the robots look for the leader, identified by the human pilot robot,
 * and goes in the direction where the leader is. The computed destination is such that the robot
 * goes just for a short distance in the detected direction and then it looks again. This is done
 * with the purpose of not missing the caught leader.
 * 
 * @author Valerio Volpi - vale.v@me.com
 */
@PluginImplementation
public class Following extends AlgorithmImpl<Point2D>
{
	/**
	 * Properties related to Following algorithm
	 * 
	 * @author Valerio Volpi - vale.v@me.com
	 */
	private enum FollowingProperties implements SycamoreProperty
	{
		FOLLOWING_MOVE("Move", "" + false), FOLLOWING_ROTATE("Rotate", "" + false), FOLLOWING_USE_LIGHT("Use Lights", "" + false);

		private String	description		= null;
		private String	defaultValue	= null;

		/**
		 * Constructor.
		 */
		FollowingProperties(String description, String defaultValue)
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

	private float					angle			= 0;
	private FollowingSettingsPanel	panel_settings	= null;

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.diunipi.volpi.sycamore.plugins.algorithms.Algorithm#init()
	 */
	@Override
	public void init(SycamoreObservedRobot<Point2D> robot)
	{
		SycamoreSystem.setTimelineMode(TIMELINE_MODE.LIVE);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.diunipi.volpi.sycamore.plugins.Algorithm#compute(java.util.Vector,
	 * it.diunipi.volpi.sycamore.model.Observation)
	 */
	@Override
	public Point2D compute(Vector<Observation<Point2D>> snapshot, SycamoreObservedRobot<Point2D> caller)
	{
		// eventually used destination point
		Point2D dest = null;

		// look for a HumanPilot executing robot
		for (Observation<Point2D> observation : snapshot)
		{
			if (observation.isHumanPilot())
			{
				// get robot and leader positions
				Point2D robotPosition = caller.getLocalPosition();
				Point2D observationPosition = observation.getRobotPosition();

				if (isUseLights())
				{
					// if lights are used, turn on the green light to indicate that the leader has
					// been observed
					try
					{
						caller.turnLightOn(ColorRGBA.Green);
					}
					catch (Exception e)
					{
						// Light already on
					}
				}

				if (caller.getVisibility() instanceof DirectionalVisibility)
				{
					// if the visibility is directional, get it
					DirectionalVisibility visibility = (DirectionalVisibility) caller.getVisibility();

					// get the centroid of the triangle
					Point2D target = visibility.getCentroid();

					float deltaX = observationPosition.x - target.x;
					float deltaY = observationPosition.y - target.y;

					return new Point2D(robotPosition.x + deltaX, robotPosition.y + deltaY);
				}
				else
				{
					// if the visibility is not directional, fallback to default Following
					return observationPosition;
				}
			}
			else if (isUseLights())
			{
				// if lights are used and observation does not refer to a human pilot,
				// look for a green light
				Iterator<SycamoreObservedLight> lights = observation.getLightsIterator();
				if (lights.hasNext())
				{
					SycamoreObservedLight light = lights.next();

					if (SycamoreUtil.areColorsEqual(light.getColor(), ColorRGBA.Green))
					{
						dest = observation.getRobotPosition();
					}
				}
			}
		}

		// here no leader has been found.

		if (isUseLights())
		{
			// clear lights
			caller.turnLightOff();

			// if lights are used and no leader has been found, look if some green light has been
			// observed
			// in that case dest contains the robot with the light
			if (dest != null)
			{
				// move toward dest for a short path
				return caller.getLocalPosition().interpolateWith(dest, 0.3f);
			}
		}

		if (isRotate())
		{
			// eventually rotate the direction. If is paired with MOVE, rotation is faster
			Point2D p = new Point2D(caller.getLocalPosition().x + FastMath.cos(angle), caller.getLocalPosition().y + FastMath.sin(angle));
			angle += (isMove() ? 0.1f : 0.005f);

			caller.setDirection(p);
		}

		if (isMove())
		{
			// move randomly, like the human pilot
			Point2D position = caller.getLocalPosition();

			int lowerBoundX = PropertyManager.getSharedInstance().getIntegerProperty(ApplicationProperties.INITIAL_POSITION_MIN_X);
			int upperBoundX = PropertyManager.getSharedInstance().getIntegerProperty(ApplicationProperties.INITIAL_POSITION_MAX_X);
			int lowerBoundY = PropertyManager.getSharedInstance().getIntegerProperty(ApplicationProperties.INITIAL_POSITION_MIN_Y);
			int upperBoundY = PropertyManager.getSharedInstance().getIntegerProperty(ApplicationProperties.INITIAL_POSITION_MAX_Y);

			float minX = Math.max(lowerBoundX, position.x - 2.0f);
			float maxX = Math.min(upperBoundX, position.x + 2.0f);
			float minY = Math.max(lowerBoundY, position.y - 2.0f);
			float maxY = Math.min(upperBoundY, position.y + 2.0f);

			return SycamoreUtil.getRandomPoint2D(minX, maxX, minY, maxY);
		}
		else
		{
			return null;
		}
	}

	/**
	 * @return
	 */
	public static boolean isMove()
	{
		return PropertyManager.getSharedInstance().getBooleanProperty(FollowingProperties.FOLLOWING_MOVE);
	}

	/**
	 * @param move
	 */
	public static void setMove(Boolean move)
	{
		PropertyManager.getSharedInstance().putProperty(FollowingProperties.FOLLOWING_MOVE, move);
	}

	/**
	 * @return
	 */
	public static boolean isRotate()
	{
		return PropertyManager.getSharedInstance().getBooleanProperty(FollowingProperties.FOLLOWING_ROTATE);
	}

	/**
	 * @param rotate
	 */
	public static void setRotate(Boolean rotate)
	{
		PropertyManager.getSharedInstance().putProperty(FollowingProperties.FOLLOWING_ROTATE, rotate);
	}

	/**
	 * @return
	 */
	public static boolean isUseLights()
	{
		return PropertyManager.getSharedInstance().getBooleanProperty(FollowingProperties.FOLLOWING_USE_LIGHT);
	}

	/**
	 * @param rotate
	 */
	public static void setUseLights(Boolean useLights)
	{
		PropertyManager.getSharedInstance().putProperty(FollowingProperties.FOLLOWING_USE_LIGHT, useLights);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.diunipi.volpi.sycamore.plugins.Algorithm#getReferences()
	 */
	@Override
	public String getReferences()
	{
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.diunipi.volpi.sycamore.plugins.Algorithm#getType()
	 */
	@Override
	public TYPE getType()
	{
		return TYPE.TYPE_2D;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.diunipi.volpi.sycamore.plugins.SycamorePlugin#getShortDescription()
	 */
	@Override
	public String getPluginShortDescription()
	{
		return "An algorithm that makes the executing robot follow a leader, trying not to miss it.";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.diunipi.volpi.sycamore.plugins.SycamorePlugin#getLongDescription()
	 */
	@Override
	public String getPluginLongDescription()
	{
		return "A simple 2D algorithm where the robots look for the leader, identified by the human pilot robot, and goes in the direction where "
				+ "the leader is. The computed destination is such that the robot goes just for a short distance in the detected direction and then it "
				+ "looks again. This is done with the purpose of not missing the caught leader.";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.diunipi.volpi.sycamore.plugins.SycamorePlugin#getSettings()
	 */
	@Override
	public SycamorePanel getPanel_settings()
	{
		if (panel_settings == null)
		{
			panel_settings = new FollowingSettingsPanel();
		}
		return panel_settings;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.diunipi.volpi.sycamore.plugins.SycamorePlugin#getAuthor()
	 */
	@Override
	public String getAuthor()
	{
		return "Valerio Volpi - vale.v@me.com";
	}
}
