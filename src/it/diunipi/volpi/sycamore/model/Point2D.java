package it.diunipi.volpi.sycamore.model;

import it.diunipi.volpi.sycamore.gui.SycamoreSystem;

import com.jme3.math.Vector3f;

/**
 * A 2 dimensional point (x, y)
 * 
 * @author Valerio Volpi - vale.v@me.com
 */
public class Point2D extends SycamoreAbstractPoint implements Comparable<Point2D>, ComputablePoint<Point2D>
{
	public static final float	DEFAULT_Z	= 0;
	public float				x, y;

	/**
	 * Default constructor. Creates the zero point
	 */
	public Point2D()
	{
		this(0, 0);
	}

	/**
	 * Constructor. Creates a new point with passed data
	 * 
	 * @param x
	 * @param y
	 * @param z
	 */
	public Point2D(float x, float y)
	{
		this.x = x;
		this.y = y;
	}

	/**
	 * Constructor. Creates a new point with data contained in passed vector
	 * 
	 * @param vector3
	 */
	public Point2D(Vector3f vector3f)
	{
		// z coord in vector3f is ignored
		this(vector3f.x, vector3f.y);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.diunipi.volpi.sycamore.model.SycamoreAbstractPoint#toVector3f()
	 */
	public Vector3f toVector3f()
	{
		// z coord in vector3f is set to zero
		return new Vector3f(this.x, this.y, DEFAULT_Z);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * it.diunipi.volpi.sycamore.model.SycamoreAbstractPoint#fromVector3f(com.jme3.math.Vector3f)
	 */
	@Override
	public void fromVector3f(Vector3f vector3f)
	{
		// z coord in vector3f is ignored
		this.x = vector3f.x;
		this.y = vector3f.y;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return "(" + x + ", " + y + ")";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(Point2D o)
	{
		int ret = 0;
		if ((ret = Float.compare(this.x, o.x)) == 0)
		{
			if ((ret = Float.compare(this.y, o.y)) == 0)
			{
				return 0;
			}
		}

		return ret;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * it.diunipi.volpi.sycamore.model.ComputablePoint#distanceTo(it.diunipi.volpi.sycamore.model
	 * .SycamoreAbstractPoint)
	 */
	@Override
	public float distanceTo(Point2D point)
	{
		Vector3f thisVector = this.toVector3f();
		Vector3f pointVector = point.toVector3f();

		return thisVector.distance(pointVector);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * it.diunipi.volpi.sycamore.model.SycamoreAbstractPoint#equalsModuloEpsilon(it.diunipi.volpi
	 * .sycamore.model.SycamoreAbstractPoint)
	 */
	@Override
	public boolean differsModuloEpsilon(Point2D point)
	{
		if (this.distanceTo(point) >= SycamoreSystem.getEpsilon())
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * it.diunipi.volpi.sycamore.model.SycamoreAbstractPoint#sum(it.diunipi.volpi.sycamore.model
	 * .SycamoreAbstractPoint, it.diunipi.volpi.sycamore.model.SycamoreAbstractPoint)
	 */
	@Override
	public Point2D sumWith(Point2D point)
	{
		Point2D result = new Point2D();

		result.x = this.x + point.x;
		result.y = this.y + point.y;

		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.diunipi.volpi.sycamore.model.SycamoreAbstractPoint#divide(float)
	 */
	@Override
	public Point2D divideFor(float value)
	{
		Point2D point2D = new Point2D();
		point2D.x = this.x / value;
		point2D.y = this.y / value;

		return point2D;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * it.diunipi.volpi.sycamore.model.SycamoreAbstractPoint#interpolateWithPoint(it.diunipi.volpi
	 * .sycamore.model.SycamoreAbstractPoint, float, float, float)
	 */
	@Override
	public Point2D interpolateWith(Point2D point, float ratio)
	{
		Point2D point2D = (Point2D) point;

		float newX = this.x * (1 - ratio) + point2D.x * ratio;
		float newY = this.y * (1 - ratio) + point2D.y * ratio;

		return new Point2D(newX, newY);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj)
	{
		if (obj instanceof Point2D)
		{
			Point2D o = (Point2D) obj;
			return this.x == o.x && this.y == o.y;
		}

		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.diunipi.volpi.sycamore.model.SycamoreAbstractPoint#clone()
	 */
	@Override
	public Object clone() throws CloneNotSupportedException
	{
		return new Point2D(this.x, this.y);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * it.diunipi.volpi.sycamore.model.ComputablePoint#getRotationAngles(it.diunipi.volpi.sycamore
	 * .model.SycamoreAbstractPoint)
	 */
	@Override
	public float[] getRotationAngles(Point2D destination)
	{
		float[] ret = new float[3];
		
		float angle;
		
		Vector3f src = Vector3f.UNIT_X;
		if (this.y < destination.y)
		{
			Vector3f dest = destination.toVector3f().subtract(this.toVector3f());
			dest.normalizeLocal();
			angle = dest.angleBetween(src);
		}
		else
		{
			Vector3f dest = this.toVector3f().subtract(destination.toVector3f());
			dest.normalizeLocal();
			angle = (float) (src.angleBetween(dest) - Math.PI);
		}
		
		ret[0] = 0;
		ret[1] = 0;
		ret[2] = angle;
		
		return ret;
	}
}
