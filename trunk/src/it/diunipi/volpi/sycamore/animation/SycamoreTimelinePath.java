package it.diunipi.volpi.sycamore.animation;

import it.diunipi.volpi.sycamore.engine.SycamoreAbstractPoint;

import java.util.Vector;

/**
 * This class represents a path that an animated object of the timeline follows during movement.
 * 
 * @author Valerio Volpi - vale.v@me.com
 * 
 * @param <P>
 *            the type of point on which this engine and all the contained objects (robots,
 *            plugins...) are build.
 */
public class SycamoreTimelinePath<P extends SycamoreAbstractPoint>
{
	private Vector<P>	points;

	/**
	 * Default constructor.
	 * @param points
	 */
	public SycamoreTimelinePath(Vector<P> points)
	{
		this.points = points;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return "Path with " + numPoints() + " points";
	}

	/**
	 * Returns a string representation of this path, by listing all the points in it.
	 * 
	 * @return
	 */
	public String toStringPointList()
	{
		StringBuilder str = new StringBuilder();
		str.append(this.toString() + ":\n");
		for (int i = 0; i < this.points.size(); i++)
		{
			str.append(this.points.get(i).toString() + "\n");
		}

		return str.toString();
	}

	/**
	 * Returns the points of this path
	 * 
	 * @return 
	 */
	public Vector<P> getPoints()
	{
		return points;
	}

	/**
	 * Returns the number of points in this path
	 * 
	 * @return 
	 */
	public int numPoints()
	{
		return points.size();
	}

	/**
	 * Returns the i-th point of this path
	 * 
	 * @param i
	 * @return
	 */
	public P getPoint(int i)
	{
		return points.get(i);
	}
}