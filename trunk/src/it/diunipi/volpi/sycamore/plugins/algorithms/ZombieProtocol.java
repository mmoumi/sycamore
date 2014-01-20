/**
 * 
 */
package it.diunipi.volpi.sycamore.plugins.algorithms;

import it.diunipi.volpi.sycamore.engine.Observation;
import it.diunipi.volpi.sycamore.engine.ObservationExt;
import it.diunipi.volpi.sycamore.engine.Point2D;
import it.diunipi.volpi.sycamore.engine.SycamoreEngine.TYPE;
import it.diunipi.volpi.sycamore.engine.SycamoreObservedLight;
import it.diunipi.volpi.sycamore.engine.SycamoreObservedRobot;
import it.diunipi.volpi.sycamore.engine.TooManyLightsException;
import it.diunipi.volpi.sycamore.gui.SycamorePanel;
import it.diunipi.volpi.sycamore.plugins.visibilities.HumanZombieVisibility;
import it.diunipi.volpi.sycamore.plugins.visibilities.Visibility;
import it.diunipi.volpi.sycamore.util.PropertyManager;
import it.diunipi.volpi.sycamore.util.SycamoreProperty;

import java.util.Iterator;
import java.util.Vector;

import net.xeoh.plugins.base.annotations.PluginImplementation;

import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;

/**
 * @author Valerio Volpi - vale.v@me.com
 * 
 */
@PluginImplementation
public class ZombieProtocol extends AlgorithmImpl<Point2D>
{
	/**
	 * Properties related to Zombie protocol
	 * 
	 * @author Valerio Volpi - vale.v@me.com
	 */
	private enum ZombieProtocolProperties implements SycamoreProperty
	{
		ZOMBIE_PROTOCOL_MIN_ACTIVITY("Smallest possible activity", "" + 2), ZOMBIE_PROTOCOL_MAX_ACTIVITY("Highest possible activity", "" + 20);

		private String	description		= null;
		private String	defaultValue	= null;

		/**
		 * Constructor.
		 */
		ZombieProtocolProperties(String description, String defaultValue)
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

	private float						activityLevel	= getMinActivity();
	private ZombieProtocolSettingsPanel	panel_settings	= null;

	/**
	 * Returns the smallest possible activity level of a zombie
	 */
	public static float getMinActivity()
	{
		return PropertyManager.getSharedInstance().getFloatProperty(ZombieProtocolProperties.ZOMBIE_PROTOCOL_MIN_ACTIVITY);
	}

	/**
	 * Sets passed value as the smallest possible activity level of a zombie
	 */
	public static void setMinActivity(float activity)
	{
		PropertyManager.getSharedInstance().putProperty(ZombieProtocolProperties.ZOMBIE_PROTOCOL_MIN_ACTIVITY, activity);
	}

	/**
	 * Returns the highest possible activity level of a zombie
	 */
	public static float getMaxActivity()
	{
		return PropertyManager.getSharedInstance().getFloatProperty(ZombieProtocolProperties.ZOMBIE_PROTOCOL_MAX_ACTIVITY);
	}

	/**
	 * Sets passed value as the highest possible activity level of a zombie
	 */
	public static void setMaxActivity(float activity)
	{
		PropertyManager.getSharedInstance().putProperty(ZombieProtocolProperties.ZOMBIE_PROTOCOL_MAX_ACTIVITY, activity);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.diunipi.volpi.sycamore.plugins.algorithms.Algorithm#init()
	 */
	@Override
	public void init(SycamoreObservedRobot<Point2D> robot)
	{

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.diunipi.volpi.sycamore.plugins.algorithms.Algorithm#compute(java.util.Vector,
	 * it.diunipi.volpi.sycamore.engine.SycamoreObservedRobot)
	 */
	@Override
	public Point2D compute(Vector<Observation<Point2D>> observations, SycamoreObservedRobot<Point2D> caller)
	{
		float newActivityLevel = getMinActivity();
		Vector3f destination = null;

		// The zombie is expected to have just one blue light, that identifies that it is a zombie.
		// take the first light of the caller
		SycamoreObservedLight firstLight = caller.getLights().firstElement();

		// if that light is not blue, turn it on
		if (!firstLight.getColor().equals(ColorRGBA.Blue))
		{
			try
			{
				caller.turnLightOn(ColorRGBA.Blue);
			}
			catch (TooManyLightsException e)
			{
				e.printStackTrace();
			}
		}

		Visibility<Point2D> visibility = caller.getVisibility();
		if (visibility != null && visibility instanceof HumanZombieVisibility)
		{
			for (Observation<Point2D> observation : observations)
			{
				ObservationExt<Point2D> observationExt = null;
				observationExt = (ObservationExt<Point2D>) observation;

				// compute the attack level
				// take the lights of the robot
				Iterator<SycamoreObservedLight> lights = observation.getLightsIterator();

				if (lights.hasNext())
				{
					SycamoreObservedLight light = lights.next();

					// The red light identifies a human.
					if (light.getColor().equals(ColorRGBA.Red))
					{
						if (visibility.isPointVisible(observation.getRobotPosition()))
						{
							// bite human
							Algorithm<Point2D> algorithm = observationExt.getAlgorithm();
							if (algorithm instanceof HumanProtocol)
							{
								HumanProtocol humanProtocol = (HumanProtocol) algorithm;
								humanProtocol.biteHuman();
							}

						}
						else if (lights.hasNext())
						{
							// Look for the second light, the sound
							SycamoreObservedLight sound = lights.next();

							// The yellow light identifies a sound
							if (sound.getColor().equals(ColorRGBA.Yellow) && sound.getIntensity() > 0)
							{
								// a sound has been sensed.
								// increase activity level
								newActivityLevel = newActivityLevel + sound.getIntensity();

								// adjust direction
								if (destination == null)
								{
									destination = caller.getLocalPosition().toVector3f();
								}
								destination = adjustDirection(caller.getLocalPosition(), destination, observation.getRobotPosition(), sound.getIntensity());
							}
						}
					}
				}

			}

			// Trim activity level
			if (newActivityLevel > getMaxActivity())
			{
				newActivityLevel = getMaxActivity();
			}
			if (newActivityLevel < getMinActivity())
			{
				newActivityLevel = getMinActivity();
			}

			// Adjust attack range properly
			HumanZombieVisibility zombieVisibility = (HumanZombieVisibility) visibility;
			if (newActivityLevel > activityLevel)
			{
				zombieVisibility.setAttackRange(newActivityLevel);
				caller.setSpeed(newActivityLevel / 100.0f);
			}
			else
			{
				activityLevel = newActivityLevel;
				float attackRange = zombieVisibility.getAttackRange() / 1.001f;
				if (attackRange > getMinActivity())
				{
					zombieVisibility.setAttackRange(attackRange);
					caller.setSpeed(attackRange / 100.0f);
				}
				else
				{
					zombieVisibility.setAttackRange(getMinActivity());
					caller.setSpeed(getMinActivity() / 100.0f);
				}
			}
		}

		if (destination != null)
		{
			Point2D dest = new Point2D(destination);
			dest = caller.getLocalPosition().interpolateWith(dest, (1.0f / (float) observations.size()));
			return dest;
		}
		else
		{
			return null;
		}
	}

	/**
	 * Computes a vectorial sum between the direction and the vector representing intensity
	 * 
	 * @param robotPosition
	 * @param intensity
	 */
	private Vector3f adjustDirection(Point2D currentPosition, Vector3f direction, Point2D robotPosition, float intensity)
	{
		Vector3f vector = null;

		// compute the vector
		float dst = currentPosition.distanceTo(robotPosition);

		if (intensity < dst)
		{
			float xp = robotPosition.x - currentPosition.x;
			float yp = robotPosition.y - currentPosition.y;

			// proportions:
			// dst/intensity = xp/xc
			// dst/intensity = yp/yc

			float xc = ((xp * intensity) / dst) + currentPosition.x;
			float yc = ((yp * intensity) / dst) + currentPosition.y;

			vector = new Vector3f(xc, yc, 0);
		}
		else
		{
			vector = robotPosition.toVector3f();
		}

		// translate both vector to have origin in zero
		vector.x = vector.x - currentPosition.x;
		vector.y = vector.y - currentPosition.y;

		direction.x = direction.x - currentPosition.x;
		direction.y = direction.y - currentPosition.y;

		direction = direction.add(vector);

		// translate back
		direction.x = direction.x + currentPosition.x;
		direction.y = direction.y + currentPosition.y;

		return direction;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.diunipi.volpi.sycamore.plugins.algorithms.Algorithm#getReferences()
	 */
	@Override
	public String getReferences()
	{
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.diunipi.volpi.sycamore.plugins.SycamoreTypedPlugin#getType()
	 */
	@Override
	public TYPE getType()
	{
		return TYPE.TYPE_2D;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.diunipi.volpi.sycamore.plugins.SycamorePlugin#getAuthor()
	 */
	@Override
	public String getAuthor()
	{
		return "Valerio Volpi";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.diunipi.volpi.sycamore.plugins.SycamorePlugin#getPluginShortDescription()
	 */
	@Override
	public String getPluginShortDescription()
	{
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.diunipi.volpi.sycamore.plugins.SycamorePlugin#getPluginLongDescription()
	 */
	@Override
	public String getPluginLongDescription()
	{
		return null;
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
			panel_settings = new ZombieProtocolSettingsPanel();
		}
		return panel_settings;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.diunipi.volpi.sycamore.plugins.algorithms.AlgorithmImpl#reset()
	 */
	@Override
	public synchronized void reset()
	{
		super.reset();

		activityLevel = getMinActivity();
	}
}
