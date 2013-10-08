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
 * @author Vale
 *
 */
public interface Visibility<P extends SycamoreAbstractPoint & ComputablePoint<P>> extends SycamoreTypedPlugin
{
	/**
	 * @return
	 */
	public SycamoreRobot<P> getRobot();
	
	/**
	 * @param robot
	 */
	public void setRobot(SycamoreRobot<P> robot);
	
	/**
	 * @param center
	 * @param point
	 * @return
	 */
	public boolean isPointVisible(P point);
	
	/**
	 * @return
	 */
	public P getPointInside();
	
	/**
	 * @return
	 */
	public Vector<Observation<P>> filter(Vector<Observation<P>> observations);
	
	/**
	 * @return
	 */
	public Geometry getVisibilityRangeGeometry();
}
