/**
 * 
 */
package it.diunipi.volpi.sycamore.plugins.algorithms;

import it.diunipi.volpi.sycamore.engine.Point2D;
import it.diunipi.volpi.sycamore.engine.SycamoreObservedRobot;
import net.xeoh.plugins.base.annotations.PluginImplementation;

/**
 * @author Vale
 * 
 */
@PluginImplementation
public class MultipleHumanProtocol extends SingleHumanProtocol
{
	protected final int		totRobots	= 6;
	protected static int	numRobots	= 0;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * it.diunipi.volpi.sycamore.plugins.algorithms.SingleHumanProtocol#init(it.diunipi.volpi.sycamore
	 * .engine.SycamoreObservedRobot)
	 */
	@Override
	public void init(SycamoreObservedRobot<Point2D> robot)
	{
		super.init(robot);

		numRobots++;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * it.diunipi.volpi.sycamore.plugins.algorithms.SingleHumanProtocol#computePolygon(it.diunipi
	 * .volpi.sycamore.engine.Point2D)
	 */
	@Override
	protected void computePolygon(Point2D center)
	{
		double angle = 6.2831853071795862D / (double) sides;

		for (int i = (numRobots * (sides / totRobots)); i < (sides + (numRobots * (sides / totRobots))); i++)
		{
			float xPoint = (float) (center.x + (radius * Math.cos(i * angle)));
			float yPoint = (float) (center.y - (radius * Math.sin(i * angle)));

			points.add(new Point2D(xPoint, yPoint));
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.diunipi.volpi.sycamore.plugins.algorithms.SingleHumanProtocol#reset()
	 */
	@Override
	public synchronized void reset()
	{
		super.reset();
		numRobots--;
	}
}
