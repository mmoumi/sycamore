package it.diunipi.volpi.sycamore.engine;

import java.util.Iterator;
import java.util.Vector;

/**
 * Wrapper that abstracts the observation concept during look phase.
 * 
 * @author Valerio Volpi - vale.v@me.com
 */
public class Observation<P extends SycamoreAbstractPoint & ComputablePoint<P>>
{
	private final P								robotPosition;
	private final Vector<SycamoreRobotLight<P>>	robotLights;
	private final boolean						humanPilot;

	/**
	 * Default constructor.
	 * 
	 * @param p_last
	 * @param p_types
	 * @param p_light
	 */
	public Observation(final P robotPosition, final Vector<SycamoreRobotLight<P>> robotLights, final boolean humanPilot)
	{
		this.robotPosition = robotPosition;
		this.robotLights = robotLights;
		this.humanPilot = humanPilot;
	}

	/**
	 * Returns the position of the observed robot.
	 * 
	 * @return the robotPosition
	 */
	public P getRobotPosition()
	{
		return robotPosition;
	}

	/**
	 * Returns the {@link Iterator} object that gives access to all the lights of the observed
	 * robot.
	 * 
	 * @return the {@link Iterator} object that gives access to all the lights of the observed
	 *         robot.
	 */
	public Iterator<SycamoreRobotLight<P>> getLightsIterator()
	{
		return robotLights.iterator();
	}

	/**
	 * Returns true if the observed robot is a Human Pilot, false otherwise.
	 * 
	 * @return true if the observed robot is a Human Pilot, false otherwise.
	 */
	public boolean isHumanPilot()
	{
		return humanPilot;
	}
}
