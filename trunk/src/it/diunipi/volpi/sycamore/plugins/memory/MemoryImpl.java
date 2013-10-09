/**
 * 
 */
package it.diunipi.volpi.sycamore.plugins.memory;

import it.diunipi.volpi.sycamore.engine.ComputablePoint;
import it.diunipi.volpi.sycamore.engine.SycamoreAbstractPoint;
import it.diunipi.volpi.sycamore.engine.SycamoreSystemMemory;
import it.diunipi.volpi.sycamore.engine.SycamoreEngine.TYPE;

/**
 * A basic implementation of the <code>Memory</code> interface. It implements some methods using
 * default values. While implementing a plugin, it is not recommended to start directly from the
 * <code>Memory</code> interface, but it is suggested to extend the <code>MemoryImpl</code> class
 * instead. This class cares about the management of the system memory, avoiding plugins from
 * directly handling with it.
 * 
 * @author Valerio Volpi - vale.v@me.com
 */
public abstract class MemoryImpl<P extends SycamoreAbstractPoint & ComputablePoint<P>> implements Memory<P>
{
	protected SycamoreSystemMemory<P>	systemMemory	= null;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * it.diunipi.volpi.sycamore.plugins.memory.Memory#setSystemMemory(it.diunipi.volpi.sycamore
	 * .plugins.memory.SycamoreSystemMemory)
	 */
	@Override
	public void setSystemMemory(SycamoreSystemMemory<P> memory)
	{
		this.systemMemory = memory;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.diunipi.volpi.sycamore.plugins.SycamorePlugin#getShortDescription()
	 */
	@Override
	public final String getPluginClassShortDescription()
	{
		return "MEM " + this.getTypeString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.diunipi.volpi.sycamore.plugins.SycamorePlugin#getPluginLongDescription()
	 */
	@Override
	public final String getPluginClassLongDescription()
	{
		return "Memory of type " + this.getTypeString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.diunipi.volpi.sycamore.plugins.SycamorePlugin#getTypeDescription()
	 */
	@Override
	public final String getPluginClassDescription()
	{
		return "Memory";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.diunipi.volpi.sycamore.plugins.SycamoreTypedPlugin#getTypeString()
	 */
	@Override
	public String getTypeString()
	{
		if (this.getType() == TYPE.TYPE_2D)
		{
			return "2D";
		}
		else if (this.getType() == TYPE.TYPE_3D)
		{
			return "3D";
		}
		else
			return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return getPluginName() + ": " + getPluginShortDescription() + " (" + getPluginClassShortDescription() + ")";
	}
}
