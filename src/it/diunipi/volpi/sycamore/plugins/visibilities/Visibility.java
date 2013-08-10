/**
 * 
 */
package it.diunipi.volpi.sycamore.plugins.visibilities;

import it.diunipi.volpi.sycamore.model.Observation;
import it.diunipi.volpi.sycamore.model.SycamoreAbstractPoint;
import it.diunipi.volpi.sycamore.plugins.SycamoreTypedPlugin;

import java.util.Vector;

import com.jme3.scene.Geometry;

/**
 * @author Vale
 *
 */
public interface Visibility<P extends SycamoreAbstractPoint> extends SycamoreTypedPlugin
{
	/**
	 * @param center
	 * @param point
	 * @return
	 */
	public boolean isPointVisible(P center, P point);
	
	/**
	 * @return
	 */
	public P getPointInside(P center);
	
	/**
	 * @return
	 */
	public Vector<Observation<P>> filter(Vector<Observation<P>> observations, P calleePosition);
	
	/**
	 * @return
	 */
	public Geometry getVisibilityRangeGeometry();
}
