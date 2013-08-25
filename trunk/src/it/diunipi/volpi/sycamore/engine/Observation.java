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
	 * @return the robotPosition
	 */
	public P getRobotPosition()
	{
		return robotPosition;
	}

	/**
	 * @return
	 */
	public Iterator<SycamoreRobotLight<P>> getLightsIterator()
	{
		return robotLights.iterator();
	}

	/**
	 * @return the humanPilot
	 */
	public boolean isHumanPilot()
	{
		return humanPilot;
	}
}
