package it.diunipi.volpi.sycamore.engine;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import it.diunipi.volpi.sycamore.gui.SycamoreSystem;

import com.jme3.math.Vector3f;

/**
 * A 3 dimensional point (x, y, z)
 * 
 * @author Valerio Volpi - vale.v@me.com
 */
public class Point3D extends SycamoreAbstractPoint implements Comparable<Point3D>, ComputablePoint<Point3D>
{
	public float	x, y, z;

	/**
	 * Default constructor. Creates the zero point
	 */
	public Point3D()
	{
		this(0, 0, 0);
	}

	/**
	 * Constructor. Creates a new point with passed data
	 * 
	 * @param x
	 * @param y
	 * @param z
	 */
	public Point3D(float x, float y, float z)
	{
		this.x = x;
		this.y = y;
		this.z = z;
	}

	/**
	 * Constructor. Creates a new point with data contained in passed vector
	 * 
	 * @param vector3f
	 */
	public Point3D(Vector3f vector3)
	{
		this(vector3.x, vector3.y, vector3.z);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.diunipi.volpi.sycamore.model.SycamoreAbstractPoint#toVector3f()
	 */
	public Vector3f toVector3f()
	{
		return new Vector3f(this.x, this.y, this.z);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * it.diunipi.volpi.sycamore.model.SycamoreAbstractPoint#fromVector3f(com.jme3.math.Vector3f)
	 */
	@Override
	public void fromVector3f(Vector3f vector3)
	{
		this.x = vector3.getX();
		this.y = vector3.getY();
		this.z = vector3.getZ();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return "(" + x + ", " + y + ", " + z + ")";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(Point3D o)
	{
		int ret = 0;
		if ((ret = Float.compare(this.x, o.x)) == 0)
		{
			if ((ret = Float.compare(this.y, o.y)) == 0)
			{
				if ((ret = Float.compare(this.z, o.z)) == 0)
				{
					return 0;
				}
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
	public float distanceTo(Point3D point)
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
	public boolean differsModuloEpsilon(Point3D point)
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
	public Point3D sumWith(Point3D point)
	{
		Point3D result = new Point3D();

		result.x = this.x + point.x;
		result.y = this.y + point.y;
		result.z = this.z + point.z;

		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.diunipi.volpi.sycamore.model.SycamoreAbstractPoint#divide(float)
	 */
	@Override
	public Point3D divideFor(float value)
	{
		Point3D point3D = new Point3D();
		point3D.x = this.x / value;
		point3D.y = this.y / value;
		point3D.z = this.z / value;

		return point3D;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * it.diunipi.volpi.sycamore.model.SycamoreAbstractPoint#interpolateWithPoint(it.diunipi.volpi
	 * .sycamore.model.SycamoreAbstractPoint, float, float, float)
	 */
	@Override
	public Point3D interpolateWith(Point3D point, float ratio)
	{
		Point3D point3D = (Point3D) point;

		float newX = this.x * (1 - ratio) + point3D.x * ratio;
		float newY = this.y * (1 - ratio) + point3D.y * ratio;
		float newZ = this.z * (1 - ratio) + point3D.z * ratio;

		return new Point3D(newX, newY, newZ);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj)
	{
		if (obj instanceof Point3D)
		{
			Point3D o = (Point3D) obj;
			return this.x == o.x && this.y == o.y && this.z == o.z;
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
		return new Point3D(this.x, this.y, this.z);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * it.diunipi.volpi.sycamore.model.ComputablePoint#getRotationAngles(it.diunipi.volpi.sycamore
	 * .model.SycamoreAbstractPoint)
	 */
	@Override
	public float[] getRotationAngles(Point3D destination)
	{
		float[] ret = new float[3];

		float roll;
		float pitch;
		float yaw;

		//
		Vector3f axisY = Vector3f.UNIT_Y;
		if (this.z < destination.z)
		{
			Vector3f dest = destination.toVector3f().subtract(this.toVector3f());
			dest.normalizeLocal();
			roll = dest.angleBetween(axisY);
		}
		else
		{
			Vector3f dest = this.toVector3f().subtract(destination.toVector3f());
			dest.normalizeLocal();
			roll = (float) (axisY.angleBetween(dest) - Math.PI);
		}

		//
		Vector3f axisZ = Vector3f.UNIT_Z;
		if (this.x < destination.x)
		{
			Vector3f dest = destination.toVector3f().subtract(this.toVector3f());
			dest.normalizeLocal();
			pitch = dest.angleBetween(axisZ);
		}
		else
		{
			Vector3f dest = this.toVector3f().subtract(destination.toVector3f());
			dest.normalizeLocal();
			pitch = (float) (axisZ.angleBetween(dest) - Math.PI);
		}

		//
		Vector3f axisX = Vector3f.UNIT_X;
		if (this.y < destination.y)
		{
			Vector3f dest = destination.toVector3f().subtract(this.toVector3f());
			dest.normalizeLocal();
			yaw = dest.angleBetween(axisX);
		}
		else
		{
			Vector3f dest = this.toVector3f().subtract(destination.toVector3f());
			dest.normalizeLocal();
			yaw = (float) (axisX.angleBetween(dest) - Math.PI);
		}

		ret[0] = roll;
		ret[1] = pitch;
		ret[2] = yaw;

		return ret;
	}
	
	/* (non-Javadoc)
	 * @see it.diunipi.volpi.sycamore.engine.SycamoreAbstractPoint#encode(javax.xml.parsers.DocumentBuilderFactory, javax.xml.parsers.DocumentBuilder, org.w3c.dom.Document)
	 */
	@Override
	public Element encode(DocumentBuilderFactory factory, DocumentBuilder builder, Document document)
	{
		// create element
		Element element = document.createElement("Point3D");
		
		// children
		
		Element xElem = document.createElement("x");
		xElem.appendChild(document.createTextNode(x + ""));
		
		Element yElem = document.createElement("y");
		yElem.appendChild(document.createTextNode(y + ""));
		
		Element zElem = document.createElement("z");
		zElem.appendChild(document.createTextNode(z + ""));
		
		// append children
		element.appendChild(xElem);
		element.appendChild(yElem);
		element.appendChild(zElem);
		
		return element;
	}
}