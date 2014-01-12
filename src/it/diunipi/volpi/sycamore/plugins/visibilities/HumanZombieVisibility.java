/**
 * 
 */
package it.diunipi.volpi.sycamore.plugins.visibilities;

import it.diunipi.volpi.sycamore.engine.Observation;
import it.diunipi.volpi.sycamore.engine.ObservationExt;
import it.diunipi.volpi.sycamore.engine.Point2D;
import it.diunipi.volpi.sycamore.engine.SycamoreObservedLight;
import it.diunipi.volpi.sycamore.engine.SycamoreRobot;
import it.diunipi.volpi.sycamore.gui.SycamorePanel;
import it.diunipi.volpi.sycamore.gui.SycamoreSystem;
import it.diunipi.volpi.sycamore.plugins.algorithms.HumanProtocol;
import it.diunipi.volpi.sycamore.plugins.algorithms.ZombieProtocol;
import it.diunipi.volpi.sycamore.util.SycamoreUtil;

import java.util.Iterator;
import java.util.Vector;
import java.util.concurrent.Callable;

import net.xeoh.plugins.base.annotations.PluginImplementation;

/**
 * 2D Visibility used in the Humans vs. Zombies simulations. This object represents the visibility
 * of a zombie, that is a 2D circular visibility that tells the exact positions of the robots that
 * are inside the visible area, but that also tells the direction of a robot that is outside and
 * that has a light with intensity turned on. The direction is obtained through a point outside the
 * visible area, that does not coincide with the actual position of the observed robot but that
 * instead is placed on the half-line connecting that robot with the observer The visibility range
 * in this case is called attack range, and it can become smaller or greater depending on the level
 * of activity of the zombie.
 * 
 * 
 * @author Valerio Volpi - vale.v@me.com
 */
@PluginImplementation
public class HumanZombieVisibility extends CircularVisibility
{
	private float	attackRange;

	/**
	 * Constructor.
	 */
	public HumanZombieVisibility()
	{
		super();
		setAttackRange(2.0f);

		SycamoreSystem.enqueueToJME(new Callable<Object>()
		{
			/*
			 * (non-Javadoc)
			 * 
			 * @see java.util.concurrent.Callable#call()
			 */
			@Override
			public Object call() throws Exception
			{
				quad.center();
				return null;
			}
		});
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * it.diunipi.volpi.sycamore.plugins.Visibility#canPointsSee(it.diunipi.volpi.sycamore.model
	 * .SycamoreAbstractPoint, it.diunipi.volpi.sycamore.model.SycamoreAbstractPoint)
	 */
	@Override
	public boolean isPointVisible(Point2D point)
	{
		// if the distance between the point and center is less than the radius, the point is
		// inside the circle
		float circleRadius = attackRange / 2;

		Point2D center = robot.getLocalPosition();
		if (center.distanceTo(point) < circleRadius)
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * it.diunipi.volpi.sycamore.plugins.visibilities.VisibilityImpl#setRobot(it.diunipi.volpi.sycamore
	 * .engine.SycamoreRobot)
	 */
	@Override
	public void setRobot(final SycamoreRobot<Point2D> robot)
	{
		super.setRobot(robot);

		if (robot.getAlgorithm() != null && robot.getAlgorithm() instanceof HumanProtocol)
		{
			SycamoreSystem.enqueueToJME(new Callable<Object>()
			{
				/*
				 * (non-Javadoc)
				 * 
				 * @see java.util.concurrent.Callable#call()
				 */
				@Override
				public Object call() throws Exception
				{
					robot.getRobotNode().detachChild(quad);
					quad = null;

					return null;
				}
			});
		}
	}

	/**
	 * @param attackRange
	 *            the attackRange to set
	 */
	public void setAttackRange(float attackRange)
	{
		if (this.attackRange != attackRange)
		{
			this.attackRange = attackRange;
			updateVisibilityGeometry();
		}
	}

	/**
	 * @return the attackRange
	 */
	public float getAttackRange()
	{
		return attackRange;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.diunipi.volpi.sycamore.plugins.visibilities.VisibilityImpl#updateVisibilityGeometry()
	 */
	@Override
	public void updateVisibilityGeometry()
	{
		SycamoreSystem.enqueueToJME(new Callable<Object>()
		{
			/*
			 * (non-Javadoc)
			 * 
			 * @see java.util.concurrent.Callable#call()
			 */
			@Override
			public Object call() throws Exception
			{
				if (quad != null)
				{
					quad.setLocalScale(getAttackRange());

					// translate the geometry to be centered in the robot's position again.
					float translationFactor = getAttackRange() / 2.0f;

					quad.setLocalTranslation(-translationFactor, -translationFactor, 0.5f);
					quad.updateGeometricState();
				}
				return null;
			}
		});
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * it.diunipi.volpi.sycamore.plugins.visibilities.CircularVisibility#filter(java.util.Vector)
	 */
	@Override
	public Vector<Observation<Point2D>> filter(Vector<Observation<Point2D>> observations)
	{
		if (!(robot.getAlgorithm() instanceof ZombieProtocol))
		{
			return observations;
		}
		else
		{
			Vector<Observation<Point2D>> filtered = new Vector<Observation<Point2D>>();

			// filter observations
			for (Observation<Point2D> observation : observations)
			{
				Point2D robotPosition = observation.getRobotPosition();
				if (isPointVisible(robotPosition))
				{
					filtered.add(observation);
				}
				else
				{
					// if the robot has light with intensity, add its direction
					Iterator<SycamoreObservedLight> lights = observation.getLightsIterator();
					while (lights.hasNext())
					{
						SycamoreObservedLight light = lights.next();
						if (light.getIntensity() != -1.0f)
						{
							filtered.add(getUpdatedObservation(observation));
							break;
						}
					}
				}
			}

			return filtered;
		}
	}

	/**
	 * Given an observation, returns another one with a different position, computed on the line
	 * between the robt and the original position.
	 */
	private Observation<Point2D> getUpdatedObservation(Observation<Point2D> observation)
	{
		ObservationExt<Point2D> observationExt = (ObservationExt<Point2D>) observation;

		// compute the new position
		Point2D point = observation.getRobotPosition();
		Point2D position = robot.getLocalPosition();

		// parameters
		float dst = position.distanceTo(point);
		float V = VisibilityImpl.getVisibilityRange();
		float xp = point.x - position.x;
		float yp = point.y - position.y;

		// proportions:
		// dst/V = xp/xc
		// dst/V = yp/yc

		float xc = (xp * V) / dst;
		float yc = (yp * V) / dst;

		// compute a point on the visibility circle
		Point2D range = new Point2D(position.x + xc, position.y + yc);

		// the target x is between range and point
		float ratio = SycamoreUtil.getRandomFloat(0, 1);

		Point2D target = range.interpolateWith(point, ratio);

		// recreate the vector of lights
		Vector<SycamoreObservedLight> lightsVector = new Vector<SycamoreObservedLight>();
		Iterator<SycamoreObservedLight> lights = observation.getLightsIterator();
		while (lights.hasNext())
		{
			lightsVector.add(lights.next());
		}

		return new ObservationExt<Point2D>(target, lightsVector, observationExt.getAlgorithm());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * it.diunipi.volpi.sycamore.plugins.visibilities.CircularVisibility#getPluginShortDescription()
	 */
	@Override
	public String getPluginShortDescription()
	{
		return "The visibility of a zombie.";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * it.diunipi.volpi.sycamore.plugins.visibilities.CircularVisibility#getPluginLongDescription()
	 */
	@Override
	public String getPluginLongDescription()
	{
		return "2D Visibility used in the Humans vs. Zombies simulations. This object represents the visibility of"
				+ "a zombie, that is a 2D circular visibility that tells the exact positions of the robots that are "
				+ "inside the visible area, but that also tells the direction of a robot that is outside and that has "
				+ "a light with intensity turned on. The direction is obtained through a point outside the visible "
				+ "area, that does not coincide with the actual position of the observed robot but that instead is "
				+ "placed on the half-line connecting that robot with the observer The visibility range inthis case is "
				+ "called attack range, and it can become smaller or greater depending on the level ofactivity of the zombie.";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.diunipi.volpi.sycamore.plugins.visibilities.CircularVisibility#getPanel_settings()
	 */
	@Override
	public SycamorePanel getPanel_settings()
	{
		return null;
	}
}
