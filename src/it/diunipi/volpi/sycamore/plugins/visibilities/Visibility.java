/**
 * 
 */
package it.diunipi.volpi.sycamore.plugins.visibilities;

import it.diunipi.volpi.sycamore.engine.ComputablePoint;
import it.diunipi.volpi.sycamore.engine.Observation;
import it.diunipi.volpi.sycamore.engine.SycamoreAbstractPoint;
import it.diunipi.volpi.sycamore.engine.SycamoreRobot;
import it.diunipi.volpi.sycamore.plugins.SycamoreTypedPlugin;

import java.util.Vector;

import com.jme3.scene.Geometry;

/**
 * This plugin has the purpose of limiting the visibility of a robot. If no visibility is set, a
 * robot can see its neighbors independently from the distance that there is between them, but if a
 * visibility is set, a robot can see just inside a fixed range. The plugin defines the shape of
 * such range and its dimension. The Visibility is associated with a robot, whose position and
 * direction are considered to define the visibility range. The methods offered by the plugin are
 * basically three:
 * <ul>
 * <li><code>isPointVisible()</code> returns true if passed point is visible by the robot associated
 * to the visibility plugin, and false otherwise.</li>
 * <li><code>filter()</code> takes a snapshot of the system and filters it by removing all the
 * observations that are relative to not visible robots. The filtered snapshot will be given to the
 * robot for the COMPUTE phase.</li>
 * <li><code>getPointInside()</code> returns a point that is guaranteed to be visible by the robot
 * associated to the visibility plugin..</li>
 * </ul>
 * Finally, the plugin must care about drawing its visibility range in JME scene. The method called
 * <code>getVisibilityRangeGeometry()</code> returns a {@link Geometry} object that is attached to
 * the axes node in the scene. While implementing a plugin, it is not recommended to start directly
 * from the <code>Agreement</code> interface, but it is suggested to extend the
 * {@link VisibilityImpl} class instead.
 * 
 * @author Valerio Volpi - vale.v@me.com
 */
public interface Visibility<P extends SycamoreAbstractPoint & ComputablePoint<P>> extends SycamoreTypedPlugin
{
	/**
	 * Returns the robot associated to this visibility
	 * 
	 * @return the robot associated to this visibility
	 */
	public SycamoreRobot<P> getRobot();

	/**
	 * Associates passed robot to this visibility. After this call, the visibility will use as its
	 * own center and direction the values taken by passed robot.
	 * 
	 * @param robot
	 *            the robot to associate with this visibility
	 */
	public void setRobot(SycamoreRobot<P> robot);

	/**
	 * Returns true if passed point is visible with this visibility, false otherwise. If no robot is
	 * associated to this visibility, always returns false.
	 * 
	 * @param point
	 *            the point to check
	 * @return true if the robot associated to this visibility can see passed point, false otherwise
	 *         or if there is no robot associated.
	 */
	public boolean isPointVisible(P point);

	/**
	 * Returns a point that is guaranteed to be visible
	 * 
	 * @return a point that is guaranteed to be visible
	 */
	public P getPointInside();

	/**
	 * Given a vector of {@link Observation} objects, filters it by removing all the ones that refer
	 * to points that are outside the visibility range. The returned vector contains just the
	 * observations for the visible robots.
	 * 
	 * @return a vector containing just visible observations
	 */
	public Vector<Observation<P>> filter(Vector<Observation<P>> observations);

	/**
	 * Returns a {@link Geometry} JME object that represents the visibility range. This object is
	 * attached as-is to the JME scene.
	 * 
	 * @return a {@link Geometry} JME object that represents the visibility range.
	 */
	public Geometry getVisibilityRangeGeometry();
}
