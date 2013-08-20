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
	 * @return the description
	 */
	public String getDescription();
	
	/**
	 * @return
	 */
	public String getDefaultValue();
}
