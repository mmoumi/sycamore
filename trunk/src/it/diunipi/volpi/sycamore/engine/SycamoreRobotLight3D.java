/**
 * 
 */
package it.diunipi.volpi.sycamore.engine;

import java.util.concurrent.Callable;

import it.diunipi.volpi.sycamore.gui.SycamoreSystem;

import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;

/**
 * This class represents a light of the robot. It is owned by a robot and it can be off or on. While
 * the light s on, it can have any color.
 * 
 * @author Valerio Volpi - vale.v@me.com
 */
public class SycamoreRobotLight3D extends SycamoreRobotLight<Point3D>
{
	/**
	 * @param color
	 * @param lightGeometry
	 */
	public SycamoreRobotLight3D(ColorRGBA color, Geometry lightGeometry)
	{
		super(color, lightGeometry);
	}

	/**
	 * Set the light's color
	 * 
	 * @param color
	 *            the color to set
	 */
	public void setColor(final ColorRGBA color)
	{
		this.color = color;
		final Geometry geom = this.getLightGeometry();
		if (geom != null)
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
					Material mat = geom.getMaterial();
					mat.setColor("Ambient", color);
					mat.setColor("Diffuse", color);
					mat.setColor("Specular", ColorRGBA.White);

					return null;
				}
			});
		}
	}
}
