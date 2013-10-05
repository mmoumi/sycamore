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
 * @author Vale
 * 
 */
@PluginImplementation
public class SquaredDividedVisibility extends SquaredVisibility
{
	/**
	 * 
	 */
	public SquaredDividedVisibility()
	{
		super();
		
		SycamoreSystem.enqueueToJME(new Callable<Object>()
		{
			@Override
			public Object call() throws Exception
			{
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
		return "Squared visibility divided into 4 quadrants";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.diunipi.volpi.sycamore.plugins.SycamorePlugin#getPluginLongDescription()
	 */
	@Override
	public String getPluginLongDescription()
	{
		return "Visibility bounded by a 2D square and divided into 4 quadrants: NW, NE, SE, SW";
	}
}