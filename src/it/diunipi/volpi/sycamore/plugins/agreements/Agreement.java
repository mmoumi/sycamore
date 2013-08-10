/**
 * 
 */
package it.diunipi.volpi.sycamore.plugins.agreements;

import it.diunipi.volpi.sycamore.model.SycamoreAbstractPoint;
import it.diunipi.volpi.sycamore.plugins.SycamoreTypedPlugin;

/**
 * @author Vale
 * 
 */
public interface Agreement<P extends SycamoreAbstractPoint> extends SycamoreTypedPlugin
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
}
