/**
 * 
 */
package it.diunipi.volpi.sycamore.plugins.algorithms;

import it.diunipi.volpi.sycamore.engine.Observation;
import it.diunipi.volpi.sycamore.engine.Point2D;
import it.diunipi.volpi.sycamore.engine.SycamoreObservedLight;
import it.diunipi.volpi.sycamore.engine.SycamoreObservedRobot;
import it.diunipi.volpi.sycamore.engine.TooManyLightsException;

import java.util.Vector;

import net.xeoh.plugins.base.annotations.PluginImplementation;

import com.jme3.math.ColorRGBA;

/**
 * @author Vale
 *
 */
@PluginImplementation
public class FlockingProtocol extends SingleHumanProtocol
{
	private float diameterBound = 5;
	private float incr = 0.3f;
	
	private Point2D center = new Point2D();
	
	/* (non-Javadoc)
	 * @see it.diunipi.volpi.sycamore.plugins.algorithms.SingleHumanProtocol#compute(java.util.Vector, it.diunipi.volpi.sycamore.engine.SycamoreObservedRobot)
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
				// measure diameter
				float diameter = measureDiameter(observations);
				if (diameter <= diameterBound)
				{
					center = new Point2D(center.x + incr, center.y);
					computePolygon(center);
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
	 * @param observations
	 * @return
	 */
	private float measureDiameter(Vector<Observation<Point2D>> observations)
	{
		// TODO Auto-generated method stub
		return 0;
	}
}
