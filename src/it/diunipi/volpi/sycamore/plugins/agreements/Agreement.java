/**
 * 
 */
package it.diunipi.volpi.sycamore.plugins.agreements;

import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;

import it.diunipi.volpi.sycamore.engine.ComputablePoint;
import it.diunipi.volpi.sycamore.engine.SycamoreAbstractPoint;
import it.diunipi.volpi.sycamore.engine.SycamoreRobot;
import it.diunipi.volpi.sycamore.plugins.SycamoreTypedPlugin;

/**
 * @author Vale
 * 
 */
public interface Agreement<P extends SycamoreAbstractPoint & ComputablePoint<P>> extends SycamoreTypedPlugin
{
	/**
	 * @param point
	 * @return
	 */
	public P toLocalCoordinates(P point);

	/**
	 * @param point
	 * @return
	 */
	public P toGlobalCoordinates(P point);

	/**
	 * @param owner
	 */
	public void setOwner(SycamoreRobot<P> owner);

	/**
	 * @return
	 */
	public Vector3f getLocalTranslation();

	/**
	 * @return
	 */
	public Quaternion getLocalRotation();

	/**
	 * @return
	 */
	public Vector3f getLocalScale();

	/**
	 * @return
	 */
	public Node getAxesNode();
}
