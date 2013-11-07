package it.diunipi.volpi.sycamore.plugins.measures;

import it.diunipi.volpi.sycamore.engine.SycamoreEngine;
import it.diunipi.volpi.sycamore.plugins.SycamorePlugin;

/**
 * This plugin cares about measuring the simulation. It offers a set of methods that are called by
 * the system, whose purpose is to setup and perform measures of the simulation, either step-by step
 * or overall the whole duration. the methods called <code>onSimulationStart()</code>,
 * <code>onSimulationStep()</code> and <code>onSimulationEnd()</code> are called during the
 * simulation in order to count the measured cost. Such measured cost is returned by the
 * <code>getMeasuredCost()</code> method. While implementing a plugin, it is not recommended to
 * start directly from the <code>Measure</code> interface, but it is suggested to extend the
 * <code>MeasureImpl</code> class instead.
 * 
 * @author Valerio Volpi - vale.v@me.com
 */
public interface Measure extends SycamorePlugin
{
	/**
	 * @param engine
	 */
	public void setEngine(SycamoreEngine engine);
	
	/**
	 * Performs the initial setup for the measuring. It is called before the starting of the
	 * simulation.
	 * 
	 * @param robot
	 */
	public void onSimulationStart();

	/**
	 * Counts the cost of one step of the simulation. It is called after the end of a step of the
	 * scheduler.
	 * 
	 * @param robot
	 */
	public void onSimulationStep();

	/**
	 * Performs the final setup for the measuring. It is called after the ending of the simulation.
	 * 
	 * @param robot
	 */
	public void onSimulationEnd();
}
