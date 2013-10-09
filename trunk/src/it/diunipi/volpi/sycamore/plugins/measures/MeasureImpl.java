package it.diunipi.volpi.sycamore.plugins.measures;

/**
 * A basic implementation of the <code>Measure</code> interface. It implements some methods using
 * default values. While implementing a plugin, it is not recommended to start directly from the
 * <code>Measure</code> interface, but it is suggested to extend the <code>MeasureImpl</code>
 * class instead.
 * 
 * @author Valerio Volpi - vale.v@me.com
 */
public abstract class MeasureImpl implements Measure
{
	/*
	 * (non-Javadoc)
	 * 
	 * @see it.diunipi.volpi.sycamore.plugins.Algorithm#getTypeShortDescription()
	 */
	@Override
	public final String getPluginClassShortDescription()
	{
		return "MEAS";
	}
	
	/* (non-Javadoc)
	 * @see it.diunipi.volpi.sycamore.plugins.SycamorePlugin#getPluginLongDescription()
	 */
	@Override
	public final String getPluginClassLongDescription()
	{
		return "Measure";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.diunipi.volpi.sycamore.plugins.Algorithm#getTypeDescription()
	 */
	@Override
	public final String getPluginClassDescription()
	{
		return "Cost measure";
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return getPluginName() + ": " + getPluginShortDescription();
	}
}
