package it.diunipi.volpi.sycamore.engine;

import it.diunipi.volpi.sycamore.engine.SycamoreEngine.TYPE;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.jme3.math.Vector3f;

/**
 * An abstract point of the system
 * 
 * @author Valerio Volpi - vale.v@me.com
 */
public abstract class SycamoreAbstractPoint implements Cloneable
{
	/**
	 * Fills the fields in this point using data in passed Vector3f
	 * 
	 * @param vector3
	 *            the vector containing the coordinates to give to this point.
	 */
	public abstract void fromVector3f(Vector3f vector3);

	/**
	 * Returns a Vector3f object corresponding to this point
	 * 
	 * @return a Vector3f object corresponding to this point
	 */
	public abstract Vector3f toVector3f();

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#clone()
	 */
	@Override
	public abstract Object clone() throws CloneNotSupportedException;

	/**
	 * Encode this object to XML format. The encoded Element will contain all data necessary to
	 * re-create and object that is equal to this one.
	 * 
	 * @return an XML Element containing the XML description of this object.
	 */
	public abstract Element encode(DocumentBuilderFactory factory, DocumentBuilder builder, Document document);

	/**
	 * Decode the fields in this Abstract point by taking them from passed XML element. TYPE
	 * parameter is used to determine the type (2D or 3D) of the decoded object.
	 * 
	 * @param element
	 *            the XML element where to take the data
	 * @param type
	 *            the type (2D or 3D) of the decoded object
	 * @return true if the object is decoded successfully, false otherwise.
	 */
	public abstract boolean decode(Element element, TYPE type);
}
