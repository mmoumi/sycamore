/**
 * 
 */
package it.diunipi.volpi.sycamore.engine;

import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;

/**
 * This class represents a light of the robot. It is owned by a robot and it can be off or on. While
 * the light s on, it can have any color.
 * 
 * @author Valerio Volpi - vale.v@me.com
 */
public abstract class SycamoreRobotLight<P extends SycamoreAbstractPoint & ComputablePoint<P>>
{
	protected ColorRGBA		color;
	protected final Geometry	lightGeometry;

	/**
	 * Default constructor.
	 */
	public SycamoreRobotLight(ColorRGBA color, Geometry lightGeometry)
	{
		this.lightGeometry = lightGeometry;
		setColor(color);
	}

	/**
	 * Set the light's color
	 * 
	 * @param color
	 *            the color to set
	 */
	public abstract void setColor(final ColorRGBA color);

	/**
	 * @return the color
	 */
	public ColorRGBA getColor()
	{
		return color;
	}

	/**
	 * @return the lightGeometry
	 */
	public Geometry getLightGeometry()
	{
		return lightGeometry;
	}
}
