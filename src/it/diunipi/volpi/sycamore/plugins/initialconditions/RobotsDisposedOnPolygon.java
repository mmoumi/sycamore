/**
 * 
 */
package it.diunipi.volpi.sycamore.plugins.initialconditions;

import it.diunipi.volpi.sycamore.engine.Point2D;
import it.diunipi.volpi.sycamore.engine.SycamoreEngine.TYPE;
import it.diunipi.volpi.sycamore.engine.SycamoreRobotMatrix;
import it.diunipi.volpi.sycamore.gui.SycamorePanel;
import it.diunipi.volpi.sycamore.util.PropertyManager;
import it.diunipi.volpi.sycamore.util.SycamoreProperty;
import net.xeoh.plugins.base.annotations.PluginImplementation;

/**
 * @author Vale
 * 
 */
@PluginImplementation
public class RobotsDisposedOnPolygon extends InitialConditionsImpl<Point2D>
{
	/**
	 * Properties related to RobotsDisposedOnPolygon
	 * 
	 * @author Valerio Volpi - vale.v@me.com
	 */
	public static enum RobotsDisposedOnPolygonProperties implements SycamoreProperty
	{
		RDOP_SIDES("Number of sides of the polygon", 10 + ""), RDOP_X_CENTER("X of the center of the polygon", 0.0f + ""), RDOP_Y_CENTER("Y of the center of the polygon", 0.0f + ""), RDOP_RADIUS(
				"Radius of the polygon", 8.0f + "");

		private String	description		= null;
		private String	defaultValue	= null;

		/**
		 * Constructor.
		 */
		RobotsDisposedOnPolygonProperties(String description, String defaultValue)
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

	private int									returned		= 0;
	private double								angle			= 0;
	private RobotsDisposedOnPlygonSettingsPanel	panel_settings	= null;

	/**
	 * Constructor.
	 */
	public RobotsDisposedOnPolygon()
	{
		angle = 6.2831853071795862D / (double) getSides();
	}

	/**
	 * @return the sides
	 */
	public static int getSides()
	{
		return PropertyManager.getSharedInstance().getIntegerProperty(RobotsDisposedOnPolygonProperties.RDOP_SIDES);
	}

	/**
	 * @param sides
	 *            the sides to set
	 */
	public static void setSides(int sides)
	{
		PropertyManager.getSharedInstance().putProperty(RobotsDisposedOnPolygonProperties.RDOP_SIDES, sides);
	}

	/**
	 * @return the xCenter
	 */
	public static float getXCenter()
	{
		return PropertyManager.getSharedInstance().getFloatProperty(RobotsDisposedOnPolygonProperties.RDOP_X_CENTER);
	}

	/**
	 * @param xCenter
	 *            the xCenter to set
	 */
	public static void setXCenter(float xCenter)
	{
		PropertyManager.getSharedInstance().putProperty(RobotsDisposedOnPolygonProperties.RDOP_X_CENTER, xCenter);
	}

	/**
	 * @return the xCenter
	 */
	public static float getYCenter()
	{
		return PropertyManager.getSharedInstance().getFloatProperty(RobotsDisposedOnPolygonProperties.RDOP_Y_CENTER);
	}

	/**
	 * @param xCenter
	 *            the xCenter to set
	 */
	public static void setYCenter(float yCenter)
	{
		PropertyManager.getSharedInstance().putProperty(RobotsDisposedOnPolygonProperties.RDOP_Y_CENTER, yCenter);
	}

	/**
	 * @return the radius
	 */
	public static float getRadius()
	{
		return PropertyManager.getSharedInstance().getFloatProperty(RobotsDisposedOnPolygonProperties.RDOP_RADIUS);
	}

	/**
	 * @param radius
	 *            the radius to set
	 */
	public static void setRadius(float radius)
	{
		PropertyManager.getSharedInstance().putProperty(RobotsDisposedOnPolygonProperties.RDOP_RADIUS, radius);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * it.diunipi.volpi.sycamore.plugins.initialconditions.InitialConditions#nextStartingPoint(it
	 * .diunipi.volpi.sycamore.engine.SycamoreRobotMatrix)
	 */
	@Override
	public Point2D nextStartingPoint(SycamoreRobotMatrix<Point2D> robots)
	{
		if (returned < getSides())
		{
			float xPoint = (float) (getXCenter() + (getRadius() * Math.cos(returned * angle)));
			float yPoint = (float) (getXCenter() - (getRadius() * Math.sin(returned * angle)));
			returned++;

			return new Point2D(xPoint, yPoint);
		}
		else
		{
			return new Point2D(getXCenter(), getYCenter());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.diunipi.volpi.sycamore.plugins.SycamoreTypedPlugin#getType()
	 */
	@Override
	public TYPE getType()
	{
		return TYPE.TYPE_2D;
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
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.diunipi.volpi.sycamore.plugins.SycamorePlugin#getPluginLongDescription()
	 */
	@Override
	public String getPluginLongDescription()
	{
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.diunipi.volpi.sycamore.plugins.SycamorePlugin#getPanel_settings()
	 */
	@Override
	public SycamorePanel getPanel_settings()
	{
		if (panel_settings == null)
		{
			panel_settings = new RobotsDisposedOnPlygonSettingsPanel();
		}
		return panel_settings;
	}

}
