package it.diunipi.volpi.sycamore.engine;

import it.diunipi.volpi.sycamore.engine.SycamoreEngine.TYPE;
import it.diunipi.volpi.sycamore.gui.SycamoreSystem;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

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
			
			return (this.x >= (o.x - SycamoreSystem.getEpsilon()) && this.x <= (o.x + SycamoreSystem.getEpsilon()))
					&& (this.y >= (o.y - SycamoreSystem.getEpsilon()) && this.y <= (o.y + SycamoreSystem.getEpsilon()));
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.diunipi.volpi.sycamore.engine.SycamoreAbstractPoint#encode(javax.xml.parsers.
	 * DocumentBuilderFactory, javax.xml.parsers.DocumentBuilder, org.w3c.dom.Document)
	 */
	@Override
	public synchronized Element encode(DocumentBuilderFactory factory, DocumentBuilder builder, Document document)
	{
		// create element
		Element element = document.createElement("Point2D");

		// children

		Element xElem = document.createElement("x");
		xElem.appendChild(document.createTextNode(x + ""));

		Element yElem = document.createElement("y");
		yElem.appendChild(document.createTextNode(y + ""));

		// append children
		element.appendChild(xElem);
		element.appendChild(yElem);

		return element;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.diunipi.volpi.sycamore.engine.SycamoreAbstractPoint#decode(org.w3c.dom.Element,
	 * it.diunipi.volpi.sycamore.engine.SycamoreEngine.TYPE)
	 */
	@Override
	public synchronized boolean decode(Element element, TYPE type)
	{
		NodeList nodes = element.getElementsByTagName("Point2D");

		// if there is at least a Point2D node, decode it
		if (nodes.getLength() > 0)
		{
			// x
			NodeList x = element.getElementsByTagName("x");
			if (x.getLength() > 0)
			{
				Element xElem = (Element) x.item(0);
				this.x = Float.parseFloat(xElem.getTextContent());
			}

			// y
			NodeList y = element.getElementsByTagName("y");
			if (y.getLength() > 0)
			{
				Element yElem = (Element) y.item(0);
				this.y = Float.parseFloat(yElem.getTextContent());
			}
		}

		return true;
	}

	@Override
	public boolean isPoint2D() {
		// TODO Auto-generated method stub
		return true;
	}
}
