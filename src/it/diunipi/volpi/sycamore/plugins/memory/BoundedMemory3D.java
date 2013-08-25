/**
 * 
 */
package it.diunipi.volpi.sycamore.plugins.memory;

import it.diunipi.volpi.sycamore.engine.Point3D;
import it.diunipi.volpi.sycamore.engine.SycamoreEngine.TYPE;

import java.util.Vector;

import net.xeoh.plugins.base.annotations.PluginImplementation;

/**
 * @author Vale
 * 
 */
@PluginImplementation
public class BoundedMemory3D extends BoundedMemory<Point3D>
{
	/*
	 * (non-Javadoc)
	 * 
	 * @see it.diunipi.volpi.sycamore.plugins.memory.Memory#getPastSelfObservation(int)
	 */
	@Override
	public Point3D getPastSelfPosition(int number) throws RequestedDataNotInMemoryException
	{
		if (systemMemory != null)
		{
			Vector<Point3D> positions = systemMemory.getSelfPositions();
			int min = Math.min(positions.size(), getMemorySize());
			
			if (number >= 0 && number < min)
			{
				return positions.elementAt(number);
			}
		}
		
		// if the code is here it means that number does not point to valid memory
		throw new RequestedDataNotInMemoryException("Memory does not contain requested self observation");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.diunipi.volpi.sycamore.plugins.memory.Memory#getPastSnapshot(int)
	 */
	@Override
	public Vector<Point3D> getPastSnapshot(int number) throws RequestedDataNotInMemoryException
	{
		if (systemMemory != null)
		{
			Vector<Vector<Point3D>> snapshots = systemMemory.getSnapshots();
			int min = Math.min(snapshots.size(), getMemorySize());
			
			if (number >= 0 && number < min)
			{
				return snapshots.elementAt(number);
			}
		}
		
		// if the code is here it means that number does not point to valid memory
		throw new RequestedDataNotInMemoryException("Memory does not contain requested snapshot");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.diunipi.volpi.sycamore.plugins.SycamoreTypedPlugin#getType()
	 */
	@Override
	public TYPE getType()
	{
		return TYPE.TYPE_3D;
	}
}
