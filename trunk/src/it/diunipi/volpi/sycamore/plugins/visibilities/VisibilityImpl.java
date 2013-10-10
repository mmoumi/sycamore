/**
 * 
 */
package it.diunipi.volpi.sycamore.plugins.visibilities;

import it.diunipi.volpi.sycamore.engine.ComputablePoint;
import it.diunipi.volpi.sycamore.engine.SycamoreAbstractPoint;
import it.diunipi.volpi.sycamore.engine.SycamoreEngine.TYPE;
import it.diunipi.volpi.sycamore.engine.SycamoreRobot;
import it.diunipi.volpi.sycamore.util.PropertyManager;
import it.diunipi.volpi.sycamore.util.SycamoreProperty;

/**
 * A basic implementation of the {@link Visibility} interface. It implements some methods using
 * default values. While implementing a plugin, it is not recommended to start directly from the
 * {@link Visibility} interface, but it is suggested to extend the <code>AgreementImpl</code> class
 * instead. This class handles a general function for the management of the visibility range size.
 * The subclasses can not care about this element and the related control panel.
 * 
 * @author Valerio Volpi - vale.v@me.com
 */
public abstract class VisibilityImpl<P extends SycamoreAbstractPoint & ComputablePoint<P>> implements Visibility<P>
{
	/**
	 * The properties related to all the visibilities in the system
	 * 
	 * @author Valerio Volpi - vale.v@me.com
	 */
	private enum VisibilityProperties implements SycamoreProperty
	{
		VISIBILITY_RANGE_SIZE("Visibility range size", "" + 10);

		private String	description		= null;
		private String	defaultValue	= null;

		/**
		 * Default constructor.
		 */
		VisibilityProperties(String description, String defaultValue)
		{
			this.description = description;
			this.defaultValue = defaultValue;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see it.diunipi.volpi.sycamore.util.SycamoreProperty#getDescription()
		 */
		@Override
		public String getDescription()
		{
			return description;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see it.diunipi.volpi.sycamore.util.SycamoreProperty#getDefaultValue()
		 */
		@Override
		public String getDefaultValue()
		{
			return defaultValue;
		}
	}

	protected SycamoreRobot<P>	robot;

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.diunipi.volpi.sycamore.plugins.visibilities.Visibility#getRobot()
	 */
	@Override
	public SycamoreRobot<P> getRobot()
	{
		return robot;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * it.diunipi.volpi.sycamore.plugins.visibilities.Visibility#setRobot(it.diunipi.volpi.sycamore
	 * .engine.SycamoreRobot)
	 */
	@Override
	public void setRobot(SycamoreRobot<P> robot)
	{
		this.robot = robot;
	}

	/**
	 * Returns the size of the visibility range.
	 * 
	 * @return the size of the visibility range.
	 */
	public static float getVisibilityRange()
	{
		return PropertyManager.getSharedInstance().getFloatProperty(VisibilityProperties.VISIBILITY_RANGE_SIZE);
	}

	/**
	 * Sets passed value as the new size of the visibility range.
	 * 
	 * @param visibilityRange
	 *            the visibilityRange to set
	 */
	public static void setVisibilityRange(float visibilityRange)
	{
		PropertyManager.getSharedInstance().putProperty(VisibilityProperties.VISIBILITY_RANGE_SIZE, visibilityRange);
	}

	/**
	 * Updates the visibility geometry
	 */
	public abstract void updateVisibilityGeometry();

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.diunipi.volpi.sycamore.plugins.SycamorePlugin#getShortDescription()
	 */
	@Override
	public final String getPluginClassShortDescription()
	{
		return "VIS " + this.getTypeString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.diunipi.volpi.sycamore.plugins.SycamorePlugin#getPluginLongDescription()
	 */
	@Override
	public final String getPluginClassLongDescription()
	{
		return "Visibility of type " + this.getTypeString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.diunipi.volpi.sycamore.plugins.SycamorePlugin#getTypeDescription()
	 */
	@Override
	public final String getPluginClassDescription()
	{
		return "Visibility";
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
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return getPluginName() + ": " + getPluginShortDescription() + " (" + getPluginClassShortDescription() + ")";
	}
}
