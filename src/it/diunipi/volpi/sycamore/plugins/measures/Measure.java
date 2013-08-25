package it.diunipi.volpi.sycamore.plugins.measures;

import it.diunipi.volpi.sycamore.engine.SycamoreRobot;
import it.diunipi.volpi.sycamore.plugins.SycamorePlugin;


/**
 * @author Vale
 *
 */
public interface Measure extends SycamorePlugin
{
	/**
	 * @param robot
	 */
	public void onSimulationStart(SycamoreRobot<?> robot);

	/**
	 * @param mem
	 */
	public void onSimulationStep(SycamoreRobot<?> robot);

	/**
	 * @param mem
	 */
	public void onSimulationEnd(SycamoreRobot<?> robot);
	
	/**
	 * @return
	 */
	public Object getMeasuredCost();
}
