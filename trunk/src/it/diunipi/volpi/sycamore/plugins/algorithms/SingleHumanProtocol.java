/**
 * 
 */
package it.diunipi.volpi.sycamore.plugins.algorithms;

import it.diunipi.volpi.sycamore.engine.Observation;
import it.diunipi.volpi.sycamore.engine.ObservationExt;
import it.diunipi.volpi.sycamore.engine.Point2D;
import it.diunipi.volpi.sycamore.engine.SycamoreObservedLight;
import it.diunipi.volpi.sycamore.engine.SycamoreObservedRobot;
import it.diunipi.volpi.sycamore.engine.TooManyLightsException;
import it.diunipi.volpi.sycamore.gui.SycamorePanel;
import it.diunipi.volpi.sycamore.gui.SycamoreSystem;
import it.diunipi.volpi.sycamore.gui.SycamoreSystem.TIMELINE_MODE;
import it.diunipi.volpi.sycamore.util.PropertyManager;
import it.diunipi.volpi.sycamore.util.SycamoreProperty;
import it.diunipi.volpi.sycamore.util.SycamoreUtil;

import java.util.Vector;

import net.xeoh.plugins.base.annotations.PluginImplementation;

import com.jme3.math.ColorRGBA;

/**
 * @author Vale
 * 
 */
@PluginImplementation
public class SingleHumanProtocol extends HumanProtocol
{

	
	protected Vector<Point2D>			points			= new Vector<Point2D>();
	protected int						currentPoint	= 0;

	private SingleHumanProtocolSettingsPanel	panel_settings	= null;
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * it.diunipi.volpi.sycamore.plugins.algorithms.HumanProtocol#init(it.diunipi.volpi.sycamore
	 * .engine.SycamoreObservedRobot)
	 */
	@Override
	public void init(SycamoreObservedRobot<Point2D> robot)
	{
		SycamoreSystem.setTimelineMode(TIMELINE_MODE.LIVE);
		computePolygon(new Point2D());
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
			panel_settings = new SingleHumanProtocolSettingsPanel();
		}
		return panel_settings;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.diunipi.volpi.sycamore.plugins.algorithms.HumanProtocol#compute(java.util.Vector,
	 * it.diunipi.volpi.sycamore.engine.SycamoreObservedRobot)
	 */
	@Override
	public Point2D compute(Vector<Observation<Point2D>> observations, SycamoreObservedRobot<Point2D> caller)
	{
		// The human is expected to have the first light being red, to identify its nature, or black
		// if it is bitten
		// take the first light of the caller
		if (!caller.getLights().isEmpty())
		{
			SycamoreObservedLight firstLight = caller.getLights().firstElement();

			if (this.isBitten() && !firstLight.getColor().equals(ColorRGBA.Black))
			{
				try
				{
					caller.turnLightOff();
					caller.turnLightOff();
					caller.turnLightOn(ColorRGBA.Black);
					caller.turnLightOn(ColorRGBA.Black);
				}
				catch (TooManyLightsException e)
				{
					e.printStackTrace();
				}
			}

			// if not bitten act
			if (!this.isBitten())
			{
				// compute centroid
				if (isAroundCentroid())
				{
					Vector<Point2D> positions = ObservationExt.getPositions(observations);
					Point2D centroid = SycamoreUtil.getCentroid2D(positions);

					computePolygon(centroid);
				}

				// if that light is not red, turn it on
				if (!firstLight.getColor().equals(ColorRGBA.Red))
				{
					try
					{
						caller.turnLightOn(ColorRGBA.Red);
						caller.turnLightOn(ColorRGBA.Yellow, 200.0f);
					}
					catch (TooManyLightsException e)
					{
						e.printStackTrace();
					}
				}

				Point2D ret = points.elementAt(currentPoint);

				currentPoint++;
				if (currentPoint >= points.size())
				{
					currentPoint = 0;
				}

				return ret;
			}
		}

		return null;
	}

	/**
	 * 
	 */
	protected void computePolygon(Point2D center)
	{
		double angle = 6.2831853071795862D / (double) getSides();

		for (int i = 0; i < getSides(); i++)
		{
			float xPoint = (float) (center.x + (getRadius() * Math.cos(i * angle)));
			float yPoint = (float) (center.y - (getRadius() * Math.sin(i * angle)));

			points.add(new Point2D(xPoint, yPoint));
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.diunipi.volpi.sycamore.plugins.algorithms.HumanProtocol#reset()
	 */
	@Override
	public synchronized void reset()
	{
		super.reset();

		currentPoint = 0;
	}
}
