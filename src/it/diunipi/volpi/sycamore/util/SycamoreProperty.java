/**
 * 
 */
package it.diunipi.volpi.sycamore.util;

import java.io.Serializable;

/**
 * @author Vale
 *
 */
public interface SycamoreProperty extends Serializable
{
	/**
	 * @return the name
	 */
	public String getName();
	
	/**
	 * @return
	 */
	public String getDefaultValue();
}
