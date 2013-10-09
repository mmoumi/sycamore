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
 * This plugin represents the memory capabilities of the robots. The Sycamore system offers to the
 * robot the ability to explicitly remember the past positions of both the robot itself and all the
 * others. In particular, each snapshot of the system is stored in a system memory together with the
 * position occupied by the observer robot. Such system memory is accessible to the memory plugin,
 * that can filter it to offer different memory capabilities to the robots. The memory can be
 * queried by the robots using the following methods:
 * <ul>
 * <li><code>getSelfPositionsLimit()</code> and <code>getSnapshotsLimit()</code> return the size of
 * the memory in terms of, respectively, number of positions in the past for the robot and number of
 * past snapshots taken by the robot</li>
 * <li><code>getPastSelfPosition()</code> and <code>getPastSnapshot()</code> return respectively the
 * i-th position occupied by the robot in the past, or the i-th snapshot taken by the robot in the
 * past. These latter two methods can throw a <code>RequestedDataNotInMemoryException</code> if the
 * data is not present or it is not available.</li>
 * </ul>
 * While implementing a plugin, it is not recommended to start directly from the <code>Memory</code>
 * interface, but it is suggested to extend the <code>MemoryImpl</code> class instead.
 * 
 * @author Valerio Volpi - vale.v@me.com
 */
public interface Memory<P extends SycamoreAbstractPoint & ComputablePoint<P>> extends SycamoreTypedPlugin
{
	/**
	 * Set passed system memory as the memory that will be used by this plugin. After a call to this
	 * method, the other methods of the plugin will query the passed system memory to obtain data.
	 * 
	 * @param memory
	 */
	public void setSystemMemory(SycamoreSystemMemory<P> memory);

	/**
	 * Returns the memory limit for self positions. Integer.MAX_VALUE means infinite.
	 * 
	 * @return
	 */
	public int getSelfPositionsLimit();

	/**
	 * Returns the memory limit for snapshots. Integer.MAX_VALUE means infinite.
	 * 
	 * @return
	 */
	public int getSnapshotsLimit();

	/**
	 * Returns the number-th self observation in the past, or null if this data is not available.
	 * 
	 * @param number
	 * @return
	 */
	public P getPastSelfPosition(int number) throws RequestedDataNotInMemoryException;

	/**
	 * Returns the number-th snapshot in the past, or null if this data is not available.
	 * 
	 * @param number
	 * @return
	 */
	public Vector<P> getPastSnapshot(int number) throws RequestedDataNotInMemoryException;
}
