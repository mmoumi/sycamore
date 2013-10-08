/**
 * 
 */
package it.diunipi.volpi.sycamore.util;

import java.io.Serializable;

/**
 * The interface to be implemented by any property in Sycamore
 * 
 * @author Valerio Volpi - vale.v@me.com
 */
public interface SycamoreProperty extends Serializable
{
	/**
	 * @return the description
	 */
	public String getDescription();
	
	/**
	 * @return
	 */
	public String getDefaultValue();
}
