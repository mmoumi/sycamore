/**
 * 
 */
package it.diunipi.volpi.sycamore.engine;

import it.diunipi.volpi.sycamore.plugins.memory.Memory;
import it.diunipi.volpi.sycamore.plugins.visibilities.Visibility;

import java.util.Vector;

import com.jme3.math.ColorRGBA;

/**
 * This interface represents the robot viewed by the algorithm's <code>compute()</code> method. This
 * method has limited capabilities over robots, so it can just perform the operations defined in
 * this interface.
 * 
 * @author Valerio Volpi - vale.v@me.com
 */
public interface SycamoreObservedRobot<P extends SycamoreAbstractPoint & ComputablePoint<P>>
{
	/**
	 * Returns the current local position of this robot
	 * 
	 * @return
	 * @throws TimelineNotAccessibleException
	 */
	public P getLocalPosition();

	/**
	 * Returns the robots's visibility
	 * 
	 * @return the visibility
	 */
	public Visibility<P> getVisibility();

	/**
	 * Returns true if this robot is finished, false otherwise
	 * 
	 * @return
	 */
	public boolean isFinished();

	/**
	 * Returns all the lights of this robot
	 * 
	 * @return the lights
	 */
	public Vector<SycamoreObservedLight> getLights();

	/**
	 * Turns on a light using passed color. If all the lights of the robot are already on, a
	 * TooManyLightsException is thrown.
	 * 
	 * @param color
	 */
	public void turnLightOn(ColorRGBA color) throws TooManyLightsException;
	
	/**
	 * Tuns off the last light that was turned on.
	 */
	public void turnLightOff();

	/**
	 * Returns the memory of this robot.
	 * 
	 * @return
	 */
	public Memory<P> getMemory();

	/**
	 * Returns the direction of this robot
	 * 
	 * @param p
	 */
	public void setDirection(P p);
	
	/**
	 * Returns the number of robots in the system, if this value is available. If not available,
	 * throws a <code>NNotKnownException</code>
	 * 
	 * @return
	 * @throws NNotKnownException
	 */
	public int getN() throws NNotKnownException;

	/**
	 * @return
	 */
	public P getDirection();
}
