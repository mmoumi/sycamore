/**
 * 
 */
package it.diunipi.volpi.sycamore.plugins.visibilities;

import it.diunipi.volpi.sycamore.engine.ComputablePoint;
import it.diunipi.volpi.sycamore.engine.SycamoreAbstractPoint;
import it.diunipi.volpi.sycamore.engine.SycamoreEngine.TYPE;
import it.diunipi.volpi.sycamore.util.PropertyManager;
import it.diunipi.volpi.sycamore.util.SycamoreProperty;

/**
 * @author Vale
 * 
 */
public abstract class VisibilityImpl<P extends SycamoreAbstractPoint & ComputablePoint<P>> implements Visibility<P>
{
	private enum VisibilityProperties implements SycamoreProperty
	{
		VISIBILITY_RANGE_SIZE("Visibility range size", "" + 10);

		private String	description			= null;
		private String	defaultValue	= null;

		/**
		 * Default constructor.
		 */
		VisibilityProperties(String description, String defaultValue)
		{
			this.description = description;
			this.defaultValue = defaultValue;
		}

		/* (non-Javadoc)
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
	
	/**
	 * @return the visibilityRange
	 */
	public static float getVisibilityRange()
	{
		float size = PropertyManager.getSharedInstance().getFloatProperty(VisibilityProperties.VISIBILITY_RANGE_SIZE.name());
		if (Float.isInfinite(size))
		{
			size = Float.parseFloat(VisibilityProperties.VISIBILITY_RANGE_SIZE.getDefaultValue());
		}
		
		return size;
	}
	
	/**
	 * @param visibilityRange the visibilityRange to set
	 */
	public static void setVisibilityRange(float visibilityRange)
	{
		PropertyManager.getSharedInstance().putProperty(VisibilityProperties.VISIBILITY_RANGE_SIZE.name(), visibilityRange);
	}
	
	/**
	 * Update the visibility geometry
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
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return getPluginName() + ": " + getPluginShortDescription() + " (" + getPluginClassShortDescription() + ")";
	}
}
