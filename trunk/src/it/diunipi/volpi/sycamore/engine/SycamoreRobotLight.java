/**
 * 
 */
package it.diunipi.volpi.sycamore.engine;

import it.diunipi.volpi.sycamore.engine.SycamoreEngine.TYPE;
import it.diunipi.volpi.sycamore.jmescene.SycamoreJMEScene;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;

/**
 * This class represents a light of the robot. It is owned by a robot and it can be off or on. While
 * the light s on, it can have any color.
 * 
 * @author Valerio Volpi - vale.v@me.com
 */
public abstract class SycamoreRobotLight<P extends SycamoreAbstractPoint & ComputablePoint<P>> implements Cloneable, SycamoreObservedLight
{
	protected ColorRGBA			color;
	protected final Geometry	lightGeometry;
	protected float				intensity;

	/**
	 * Default constructor.
	 */
	public SycamoreRobotLight(ColorRGBA color, Geometry lightGeometry, float intensity)
	{
		this.intensity = intensity;
		this.lightGeometry = lightGeometry;
		setColor(color);
	}

	/**
	 * Default constructor.
	 */
	public SycamoreRobotLight(ColorRGBA color, Geometry lightGeometry)
	{
		this(color, lightGeometry, -1.0f);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#clone()
	 */
	@Override
	public abstract Object clone() throws CloneNotSupportedException;

	/**
	 * @param intensity
	 *            the intensity to set
	 */
	public void setIntensity(float intensity)
	{
		// clamp intensity.
		// -1 is the only value allowed being outside the range
		if (intensity < 0.0f && intensity != -1.0f)
		{
			intensity = 0.0f;
		}

		this.intensity = intensity;
	}

	/* (non-Javadoc)
	 * @see it.diunipi.volpi.sycamore.engine.SycamoreObservedLight#getIntensity()
	 */
	@Override
	public float getIntensity()
	{
		return intensity;
	}

	/**
	 * Set the light's color
	 * 
	 * @param color
	 *            the color to set
	 */
	public abstract void setColor(final ColorRGBA color);

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.diunipi.volpi.sycamore.engine.SycamoreObservedLight#getColor()
	 */
	@Override
	public ColorRGBA getColor()
	{
		return color;
	}

	/**
	 * @return the lightGeometry
	 */
	public Geometry getLightGeometry()
	{
		return lightGeometry;
	}

	/**
	 * Encode this object to XML format. The encoded Element will contain all data necessary to
	 * re-create and object that is equal to this one.
	 * 
	 * @return an XML Element containing the XML description of this object.
	 */
	public synchronized Element encode(DocumentBuilderFactory factory, DocumentBuilder builder, Document document)
	{
		// create element
		Element element = document.createElement("SycamoreRobotLight");

		// children
		Element colorElem = document.createElement("lightColor");
		colorElem.appendChild(SycamoreJMEScene.encodeColorRGBA(color, factory, builder, document));

		Element currentRatioElem = document.createElement("intensity");
		currentRatioElem.appendChild(document.createTextNode(intensity + ""));

		// append children
		element.appendChild(colorElem);

		return element;
	}

	/**
	 * Decode the fields in this light by taking them from passed XML element. TYPE parameter is
	 * used to determine the type (2D or 3D) of the decoded object.
	 * 
	 * @param documentElement
	 */
	public boolean decode(Element element, TYPE type)
	{
		boolean success = true;
		NodeList nodes = element.getElementsByTagName("SycamoreRobotLight");

		// if there is at least a SycamoreRobotLight node, decode it
		if (nodes.getLength() > 0)
		{
			// lightColor
			NodeList lightColor = element.getElementsByTagName("lightColor");
			if (lightColor.getLength() > 0)
			{
				Element lightColorElem = (Element) lightColor.item(0);
				setColor(SycamoreJMEScene.decodeColorRGBA(lightColorElem));
			}

			// intensity
			NodeList intensity = element.getElementsByTagName("intensity");
			if (intensity.getLength() > 0)
			{
				Element intensityElem = (Element) intensity.item(0);
				float intensityValue = Float.parseFloat(intensityElem.getTextContent());

				this.setIntensity(intensityValue);
			}
		}

		return success;
	}
}
