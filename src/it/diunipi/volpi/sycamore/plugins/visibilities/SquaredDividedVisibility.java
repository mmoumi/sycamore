/**
 * 
 */
package it.diunipi.volpi.sycamore.plugins.visibilities;

import it.diunipi.volpi.sycamore.gui.SycamoreSystem;

import java.util.concurrent.Callable;

import net.xeoh.plugins.base.annotations.PluginImplementation;

import com.jme3.material.Material;
import com.jme3.texture.Texture;

/**
 * 2D Visibility with the shape of a square, divided into quadrants. This visibility is modeled by a
 * square centered in the position of the robot and with a side equal to the visibility range. The
 * border of the square is included in the visible area and any object that is inside the square's
 * area is considered visible. The visibility appears as divided into quadrants. This division is
 * only for representation purposes, this plugin actually behaves exactly as the
 * {@link SquaredVisibility} plugin.
 * 
 * @author Valerio Volpi - vale.v@me.com
 */
@PluginImplementation
public class SquaredDividedVisibility extends SquaredVisibility
{
	/**
	 * Default constructor.
	 */
	public SquaredDividedVisibility()
	{
		super();

		SycamoreSystem.enqueueToJME(new Callable<Object>()
		{
			@Override
			public Object call() throws Exception
			{
				// change the texture of the square
				Texture texture = SycamoreSystem.getAssetManager().loadTexture("it/diunipi/volpi/sycamore/resources/textures/square_div.png");
				Material mat = square.getMaterial();
				mat.setTexture("ColorMap", texture);

				return null;
			}
		});
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.diunipi.volpi.sycamore.plugins.SycamorePlugin#getShortDescription()
	 */
	@Override
	public String getPluginShortDescription()
	{
		return "2D Visibility with the shape of a square, divided into quadrants.";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.diunipi.volpi.sycamore.plugins.SycamorePlugin#getPluginLongDescription()
	 */
	@Override
	public String getPluginLongDescription()
	{
		return "2D Visibility with the shape of a square, divided into quadrants. This visibility is modeled by a square centered in the "
				+ "position of the robot and with a side equal to the visibility range. The border of the square is included in the visible "
				+ "area and any object that is inside the square's area is considered visible. The visibility appears as divided into quadrants. "
				+ "This division is only for representation purposes, this plugin actually behaves exactly as the SquaredVisibility plugin.";
	}
}