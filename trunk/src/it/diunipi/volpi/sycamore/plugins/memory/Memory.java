/**
 * 
 */
package it.diunipi.volpi.sycamore.plugins.memory;

import it.diunipi.volpi.sycamore.model.ComputablePoint;
import it.diunipi.volpi.sycamore.model.SycamoreAbstractPoint;
import it.diunipi.volpi.sycamore.plugins.SycamoreTypedPlugin;

import java.util.Vector;

/**
 * @author Vale
 * 
 */
public interface Memory<P extends SycamoreAbstractPoint & ComputablePoint<P>> extends SycamoreTypedPlugin
{
	/**
	 * @param memory
	 */
	public void setSystemMemory(SycamoreSystemMemory<P> memory);
	
	/**
	 * @return the memory limit. Integer.MAX_VALUE means infinite
	 */
	public int getMemoryLimit();

	/**
	 * @param number
	 * @return the number-th self observation in the past, or null if this data is not available
	 */
	public P getPastSelfPosition(int number) throws RequestedDataNotInMemoryException;
	
	/**
	 * @param number
	 * @return the number-th snapshot in the past, or null if this data is not available
	 */
	public Vector<P> getPastSnapshot(int number) throws RequestedDataNotInMemoryException;
}
