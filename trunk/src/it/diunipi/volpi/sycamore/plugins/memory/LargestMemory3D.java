/**
 * 
 */
package it.diunipi.volpi.sycamore.plugins.memory;

import it.diunipi.volpi.sycamore.engine.SycamoreEngine.TYPE;
import it.diunipi.volpi.sycamore.gui.SycamorePanel;
import it.diunipi.volpi.sycamore.model.Point3D;

import java.util.Vector;

import net.xeoh.plugins.base.annotations.PluginImplementation;

/**
 * @author Vale
 * 
 */
@PluginImplementation
public class LargestMemory3D extends MemoryImpl<Point3D>
{
	/*
	 * (non-Javadoc)
	 * 
	 * @see it.diunipi.volpi.sycamore.plugins.memory.Memory#getMemoryLimit()
	 */
	@Override
	public int getMemoryLimit()
	{
		return Integer.MAX_VALUE;
	}
	
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
			if (number >= 0 && number < positions.size())
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
			if (number >= 0 && number < snapshots.size())
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.diunipi.volpi.sycamore.plugins.SycamorePlugin#getPanel_settings()
	 */
	@Override
	public SycamorePanel getPanel_settings()
	{
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.diunipi.volpi.sycamore.plugins.SycamorePlugin#getPluginName()
	 */
	@Override
	public String getPluginName()
	{
		return "LargestMemory3D";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.diunipi.volpi.sycamore.plugins.SycamorePlugin#getAuthor()
	 */
	@Override
	public String getAuthor()
	{
		return "Valerio Volpi";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.diunipi.volpi.sycamore.plugins.SycamorePlugin#getPluginShortDescription()
	 */
	@Override
	public String getPluginShortDescription()
	{
		return "The larges possible memory. Remembers up to 2^32 snapshots.";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.diunipi.volpi.sycamore.plugins.SycamorePlugin#getPluginLongDescription()
	 */
	@Override
	public String getPluginLongDescription()
	{
		return "The larges possible memory, that remembers up to 2^32 steps in the past, both snapshots and self-observations.";
	}

}
