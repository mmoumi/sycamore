/**
 * 
 */
package it.diunipi.volpi.sycamore.plugins.initialconditions;

import it.diunipi.volpi.sycamore.engine.ComputablePoint;
import it.diunipi.volpi.sycamore.engine.SycamoreAbstractPoint;
import it.diunipi.volpi.sycamore.engine.SycamoreRobotMatrix;
import it.diunipi.volpi.sycamore.plugins.SycamoreTypedPlugin;

/**
 * This plugin implements the initial conditions of the system. It defines where to place the robots
 * before the simulation starts. The <code>nextStartingPoint()</code> method is called each time the
 * system needs to place a new robot in the environment. While implementing a plugin, it is not
 * recommended to start directly from the <code>InitialConditions</code> interface, but it is
 * suggested to extend the <code>InitialConditionsImpl</code> class instead.
 * 
 * @author Valerio Volpi - vale.v@me.com
 */
public interface InitialConditions<P extends SycamoreAbstractPoint & ComputablePoint<P>> extends SycamoreTypedPlugin
{
	/**
	 * Compute a new starting point for a robot
	 * 
	 * @return
	 */
	public P nextStartingPoint(SycamoreRobotMatrix<P> robots);
}
