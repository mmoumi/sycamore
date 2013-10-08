/**
 * 
 */
package it.diunipi.volpi.sycamore.plugins.memory;

import it.diunipi.volpi.sycamore.engine.ComputablePoint;
import it.diunipi.volpi.sycamore.engine.SycamoreAbstractPoint;
import it.diunipi.volpi.sycamore.engine.SycamoreSystemMemory;
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
	 * @return the memory limit for self positions. Integer.MAX_VALUE means infinite
	 */
	public int getSelfPositionsLimit();
	
	/**
	 * @return the memory limit for snapshots. Integer.MAX_VALUE means infinite
	 */
	public int getSnapshotsLimit();

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
