package it.diunipi.volpi.sycamore.plugins.measures;


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
