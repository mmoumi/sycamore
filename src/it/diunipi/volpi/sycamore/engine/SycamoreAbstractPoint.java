package it.diunipi.volpi.sycamore.engine;

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
	 */
	public abstract void fromVector3f(Vector3f vector3);

	/**
	 * Returns a Vector3f object corresponding to this point
	 * 
	 * @return
	 */
	public abstract Vector3f toVector3f();

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#clone()
	 */
	@Override
	public abstract Object clone() throws CloneNotSupportedException;
}