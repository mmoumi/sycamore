/**
 * 
 */
package it.diunipi.volpi.sycamore.plugins.agreements;

import it.diunipi.volpi.sycamore.engine.ComputablePoint;
import it.diunipi.volpi.sycamore.engine.SycamoreAbstractPoint;
import it.diunipi.volpi.sycamore.engine.SycamoreEngine.TYPE;
import it.diunipi.volpi.sycamore.util.PropertyManager;
import it.diunipi.volpi.sycamore.util.SycamoreProperty;

/**
 * A basic implementation of the {@link Agreement} interface. It implements some methods using
 * default values. While implementing a plugin, it is not recommended to start directly from the
 * {@link Agreement} interface, but it is suggested to extend the <code>AgreementImpl</code> class
 * instead. This class offers the ability of fixing the measure unit. When the measure unit is
 * fixed, all the scale factors, independently from which agreement is set, are equal to one.
 * 
 * @author Valerio Volpi - vale.v@me.com
 */
public abstract class AgreementImpl<P extends SycamoreAbstractPoint & ComputablePoint<P>> implements Agreement<P>
{
	/**
	 * The properties related to all the agreements in the system
	 * 
	 * @author Valerio Volpi - vale.v@me.com
	 */
	private enum AgreementProperties implements SycamoreProperty
	{
		FIX_MEASURE_UNIT("Fix measure unit", true + "");

		private String	description		= null;
		private String	defaultValue	= null;

		/**
		 * Default constructor.
		 */
		AgreementProperties(String description, String defaultValue)
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

	/**
	 * @return the fixMeasureUnit
	 */
	public static boolean isFixMeasureUnit()
	{
		return PropertyManager.getSharedInstance().getBooleanProperty(AgreementProperties.FIX_MEASURE_UNIT);
	}

	/**
	 * @param fixMeasureUnit
	 *            the fixMeasureUnit to set
	 */
	public static void setFixMeasureUnit(boolean fixMeasureUnit)
	{
		PropertyManager.getSharedInstance().putProperty(AgreementProperties.FIX_MEASURE_UNIT, fixMeasureUnit);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.diunipi.volpi.sycamore.plugins.SycamorePlugin#getShortDescription()
	 */
	@Override
	public final String getPluginClassShortDescription()
	{
		return "AGR " + this.getTypeString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.diunipi.volpi.sycamore.plugins.SycamorePlugin#getPluginLongDescription()
	 */
	@Override
	public final String getPluginClassLongDescription()
	{
		return "Agreement of type " + this.getTypeString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.diunipi.volpi.sycamore.plugins.SycamorePlugin#getTypeDescription()
	 */
	@Override
	public final String getPluginClassDescription()
	{
		return "Agreement";
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
