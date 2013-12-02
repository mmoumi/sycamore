/**
 * 
 */
package it.diunipi.volpi.sycamore.plugins.visibilities;

import it.diunipi.volpi.sycamore.engine.Observation;
import it.diunipi.volpi.sycamore.engine.Point2D;
import it.diunipi.volpi.sycamore.engine.SycamoreObservedLight;
import it.diunipi.volpi.sycamore.util.SycamoreUtil;

import java.util.Iterator;
import java.util.Vector;

import net.xeoh.plugins.base.annotations.PluginImplementation;

/**
 * 2D Visibility with the shape of a circle. This visibility is modeled by a circle centered in the
 * position of the robot and with a diameter equal to the visibility range. The border of the circle
 * is included in the visible area. Any object whose radial distance from the robot is less than
 * half the visibility range is considered visible. For objects that are outside the visible area,
 * if they have one or more lights with intensity, a point is returned representing the direction
 * from which the light comes. The points that are outside the visible area, thus, are not the real
 * position of a robot, but they instead define a half-line for the direction. The robot is on a
 * point an such half-line. If a robot outside the visible area has no light with intensity on, it
 * is ignored by this visibility.
 * 
 * 
 * @author Valerio Volpi - vale.v@me.com
 */
@PluginImplementation
public class CircularIntensityVisibility extends CircularVisibility
{
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * it.diunipi.volpi.sycamore.plugins.visibilities.CircularVisibility#filter(java.util.Vector)
	 */
	@Override
	public Vector<Observation<Point2D>> filter(Vector<Observation<Point2D>> observations)
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

	/**
	 * Given an observation, returns another one with a different position, computed on the line
	 * between the robt and the original position.
	 */
	private Observation<Point2D> getUpdatedObservation(Observation<Point2D> observation)
	{
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

		return new Observation<Point2D>(target, lightsVector, observation.isHumanPilot());
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
		return "2D circular visibility that does filter position but not intensities.";
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
		return "2D Visibility with the shape of a circle. This visibility is modeled by a circle centered in the "
				+ "position of the robot and with a diameter equal to the visibility range. The border of the circle "
				+ "is included in the visible area. Any object whose radial distance from the robot is less than half "
				+ "the visibility range is considered visible. For objects that are outside the visible area, if they "
				+ "have one or more lights with intensity, a point is returned representing the direction from which "
				+ "the light comes. The points that are outside the visible area, thus, are not the real position of a "
				+ "robot, but they instead define a half-line for the direction. The robot is on a point an such half-line. "
				+ "If a robot outside the visible area has no light with intensity on, it is ignored by this visibility.";
	}
}
