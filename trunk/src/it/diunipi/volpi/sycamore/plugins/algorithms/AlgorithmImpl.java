package it.diunipi.volpi.sycamore.plugins.algorithms;

import it.diunipi.volpi.sycamore.engine.ComputablePoint;
import it.diunipi.volpi.sycamore.engine.SycamoreAbstractPoint;
import it.diunipi.volpi.sycamore.engine.SycamoreEngine.TYPE;

/**
 * A basic implementation of the <code>Algorithm</code> interface. It implements some methods using
 * default values. While implementing a plugin, it is not recommended to start directly from the
 * <code>Algorithm</code> interface, but it is suggested to extend the <code>AlgorithmImpl</code>
 * class instead.
 * 
 * @author Valerio Volpi - vale.v@me.com
 * 
 * @param <P>
 * @param <R>
 */
public abstract class AlgorithmImpl<P extends SycamoreAbstractPoint & ComputablePoint<P>> implements Algorithm<P>
{
	private boolean	finished		= false;
	private String	paperFilePath	= null;

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.diunipi.volpi.sycamore.plugins.Algorithm#isFinished()
	 */
	@Override
	public synchronized boolean isFinished()
	{
		return finished;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.diunipi.volpi.sycamore.plugins.Algorithm#reset()
	 */
	@Override
	public synchronized void reset()
	{
		this.setFinished(false);
	}

	/**
	 * @param finished
	 *            the finished to set
	 */
	protected synchronized void setFinished(boolean finished)
	{
		this.finished = finished;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.diunipi.volpi.sycamore.plugins.Algorithm#isHumanPilot()
	 */
	@Override
	public boolean isHumanPilot()
	{
		return false;
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
	 * @see it.diunipi.volpi.sycamore.plugins.SycamorePlugin#getPluginName()
	 */
	@Override
	public String getPluginName()
	{
		return getClass().getSimpleName();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.diunipi.volpi.sycamore.plugins.Algorithm#getTypeShortDescription()
	 */
	@Override
	public final String getPluginClassShortDescription()
	{
		return "ALG " + this.getTypeString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.diunipi.volpi.sycamore.plugins.SycamorePlugin#getPluginLongDescription()
	 */
	@Override
	public final String getPluginClassLongDescription()
	{
		return "Algorithm of type " + this.getTypeString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.diunipi.volpi.sycamore.plugins.Algorithm#getTypeDescription()
	 */
	@Override
	public final String getPluginClassDescription()
	{
		return "Algorithm " + this.getTypeString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.diunipi.volpi.sycamore.plugins.algorithms.Algorithm#getPaperFilePath()
	 */
	@Override
	public String getPaperFilePath()
	{
		return paperFilePath;
	}

	/**
	 * @param paperFilePath
	 *            the paperFilePath to set
	 */
	public void setPaperFilePath(String paperFilePath)
	{
		this.paperFilePath = paperFilePath;
	}
}
