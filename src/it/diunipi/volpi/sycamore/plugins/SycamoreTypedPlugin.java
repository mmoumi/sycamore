/**
 * 
 */
package it.diunipi.volpi.sycamore.plugins;

import it.diunipi.volpi.sycamore.engine.SycamoreEngine.TYPE;

/**
 * This interface represents a typed plugin in Sycamore. A plugin is typed if, in addition to all
 * the plugins features, it has a type (2D or 3D).
 * 
 * @author Valerio Volpi - volpiv@cli.di.unipi.it
 */
public interface SycamoreTypedPlugin extends SycamorePlugin
{
	/**
	 * Returns the TYPE object that describes the type of the plugin (2D or 3D).
	 * 
	 * @return
	 */
	public abstract TYPE getType();

	/**
	 * Returns a string that describes the type of the algorithm (2D or 3D)
	 * 
	 * @return
	 */
	public String getTypeString();
}
