/**
 * 
 */
package it.diunipi.volpi.sycamore.plugins.agreements;

import it.diunipi.volpi.sycamore.engine.ComputablePoint;
import it.diunipi.volpi.sycamore.engine.SycamoreAbstractPoint;
import it.diunipi.volpi.sycamore.engine.SycamoreRobot;
import it.diunipi.volpi.sycamore.plugins.SycamoreTypedPlugin;

import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;

/**
 * This plugin represents the agreements on axes. It states that any point in robot's space can be
 * translated from a local (to robot) coordinate system to a global coordinate system. The methods
 * that care about this conversion are <code>toLocalCoordinates()</code> and
 * <code>toGlobalCoordinates()</code>. Depending on how these translations are implemented it is
 * possible to define different levels of agreement on the local coordinate system's properties,
 * like translation, scale, signum of scale and rotation, all of them on each axis. This plugin also
 * offers the ability of giving the translation, rotation and scale vectors to be used by the
 * outside. The methods that care about this feature are <code>getLocalTranslation()</code>,
 * <code>getLocalRotation()</code> and <code>getLocalScale()</code>. Finally, the plugin must care
 * about drawing its local coordinates axes in JME scene. The method called
 * <code>getAxesNode()</code> returns a {@link Node} object that is attached to the axes node in the
 * scene. While implementing a plugin, it is not recommended to start directly from the
 * <code>Agreement</code> interface, but it is suggested to extend the {@link AgreementImpl}
 * class instead.
 * 
 * @author Valerio Volpi - vale.v@me.com
 */
public interface Agreement<P extends SycamoreAbstractPoint & ComputablePoint<P>> extends SycamoreTypedPlugin
{
	/**
	 * Returns the conversion of passed, supposed to be expressed in global coordinates, into local
	 * coordinates.
	 * 
	 * @param point
	 * @return
	 */
	public P toLocalCoordinates(P point);

	/**
	 * Returns the conversion of passed, supposed to be expressed in local coordinates, into global
	 * coordinates.
	 * 
	 * @param point
	 * @return
	 */
	public P toGlobalCoordinates(P point);

	/**
	 * Sets the robot that owns this agreement
	 * 
	 * @param robot
	 */
	public void setRobot(SycamoreRobot<P> robot);

	/**
	 * Returns the local translation factors on x, y, z axes. If the plugin has type 2D, the z
	 * translation should be zero, but it is anyway ignored by the system.
	 * 
	 * @return
	 */
	public Vector3f getLocalTranslation();

	/**
	 * Returns a {@link Quaternion} object that describes the local rotation factors around x,
	 * y, z axes. If the plugin has type 2D, the rotations around x and y should be zero, but they
	 * are anyway ignored by the system.
	 * 
	 * @return
	 */
	public Quaternion getLocalRotation();

	/**
	 * Returns the local scale factors on x, y, z axes. If the plugin has type 2D, the z translation
	 * should be zero, but it is anyway ignored by the system.
	 * 
	 * @return
	 */
	public Vector3f getLocalScale();

	/**
	 * Return a JME {@link Node} object that represents the local coordinate system axes. This
	 * object is attached as-is to the JME scene.
	 * 
	 * @return
	 */
	public Node getAxesNode();
}
