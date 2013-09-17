/**
 * 
 */
package it.diunipi.volpi.sycamore.engine;

import it.diunipi.volpi.sycamore.jmescene.SycamoreJMEScene;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;

/**
 * This class represents a light of the robot. It is owned by a robot and it can be off or on. While
 * the light s on, it can have any color.
 * 
 * @author Valerio Volpi - vale.v@me.com
 */
public abstract class SycamoreRobotLight<P extends SycamoreAbstractPoint & ComputablePoint<P>>
{
	protected ColorRGBA		color;
	protected final Geometry	lightGeometry;

	/**
	 * Default constructor.
	 */
	public SycamoreRobotLight(ColorRGBA color, Geometry lightGeometry)
	{
		this.lightGeometry = lightGeometry;
		setColor(color);
	}

	/**
	 * Set the light's color
	 * 
	 * @param color
	 *            the color to set
	 */
	public abstract void setColor(final ColorRGBA color);

	/**
	 * @return the color
	 */
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
	public Element encode(DocumentBuilderFactory factory, DocumentBuilder builder, Document document)
	{
		// create element
		Element element = document.createElement("SycamoreRobotLight");
		
		// children
		Element colorElem = document.createElement("color");
		colorElem.appendChild(SycamoreJMEScene.encodeColorRGBA(color, factory, builder, document));
		
		// append children
		element.appendChild(colorElem);
		
		return element;
	}
}
