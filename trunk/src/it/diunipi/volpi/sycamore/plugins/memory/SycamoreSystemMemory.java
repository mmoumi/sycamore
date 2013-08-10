/**
 * 
 */
package it.diunipi.volpi.sycamore.plugins.memory;

import it.diunipi.volpi.sycamore.model.ComputablePoint;
import it.diunipi.volpi.sycamore.model.Observation;
import it.diunipi.volpi.sycamore.model.SycamoreAbstractPoint;

import java.util.Vector;

/**
 * @author Vale
 * 
 */
public class SycamoreSystemMemory<P extends SycamoreAbstractPoint & ComputablePoint<P>>
{
	private Vector<P>			selfPositions	= null;
	private Vector<Vector<P>>	snapshots	= null;

	/**
	 * 
	 */
	public SycamoreSystemMemory()
	{
		this.selfPositions = new Vector<P>();
		this.snapshots = new Vector<Vector<P>>();
	}

	/**
	 * @return the selfPositions
	 */
	public Vector<P> getSelfPositions()
	{
		return selfPositions;
	}

	/**
	 * @return the selfSnapshots
	 */
	public Vector<Vector<P>> getSnapshots()
	{
		return snapshots;
	}

	/**
	 * @param point
	 * @return
	 */
	private P clonePoint(P point)
	{
		try
		{
			return (P) point.clone();
		}
		catch (CloneNotSupportedException e)
		{
			e.printStackTrace();
		}
		
		return null;
	}

	/**
	 * @param position
	 */
	public void addSelfPosition(P position)
	{
		this.selfPositions.add(this.clonePoint(position));
	}
	
	/**
	 * @param position
	 */
	public void addSnapshot(Vector<Observation<P>> snapshot)
	{
		Vector<P> newSnapshot = new Vector<P>();
		
		for (Observation<P> observation : snapshot)
		{
			newSnapshot.add(this.clonePoint(observation.getRobotPosition()));
		}
		
		this.snapshots.add(newSnapshot);
	}
}
