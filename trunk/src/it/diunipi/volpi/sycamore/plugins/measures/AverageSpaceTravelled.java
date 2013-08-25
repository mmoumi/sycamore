/**
 * 
 */
package it.diunipi.volpi.sycamore.plugins.measures;

import it.diunipi.volpi.sycamore.engine.SycamoreRobot;
import it.diunipi.volpi.sycamore.gui.SycamorePanel;


import net.xeoh.plugins.base.annotations.PluginImplementation;

/**
 * @author Vale
 *
 */
@PluginImplementation
public class AverageSpaceTravelled extends MeasureImpl
{

	/* (non-Javadoc)
	 * @see it.diunipi.volpi.sycamore.plugins.Measure#onSimulationStart(it.diunipi.volpi.sycamore.model.SycamoreRobot)
	 */
	@Override
	public void onSimulationStart(SycamoreRobot<?> robot)
	{
		
	}

	/* (non-Javadoc)
	 * @see it.diunipi.volpi.sycamore.plugins.Measure#onSimulationStep(it.diunipi.volpi.sycamore.model.SycamoreRobot)
	 */
	@Override
	public void onSimulationStep(SycamoreRobot<?> robot)
	{

	}

	/* (non-Javadoc)
	 * @see it.diunipi.volpi.sycamore.plugins.Measure#onSimulationEnd(it.diunipi.volpi.sycamore.model.SycamoreRobot)
	 */
	@Override
	public void onSimulationEnd(SycamoreRobot<?> robot)
	{

	}
	
	/* (non-Javadoc)
	 * @see it.diunipi.volpi.sycamore.plugins.Measure#getMeasuredCost()
	 */
	@Override
	public Object getMeasuredCost()
	{
		return null;
	}

	/* (non-Javadoc)
	 * @see it.diunipi.volpi.sycamore.plugins.SycamorePlugin#getShortDescription()
	 */
	@Override
	public String getPluginShortDescription()
	{
		return "Measures the average space travelled";
	}

	/* (non-Javadoc)
	 * @see it.diunipi.volpi.sycamore.plugins.SycamorePlugin#getLongDescription()
	 */
	@Override
	public String getPluginLongDescription()
	{
		return "Measures the average space travelled by each robot in the whole simulation";
	}

	/* (non-Javadoc)
	 * @see it.diunipi.volpi.sycamore.plugins.SycamorePlugin#getSettings()
	 */
	@Override
	public SycamorePanel getPanel_settings()
	{
		return null;
	}

	/* (non-Javadoc)
	 * @see it.diunipi.volpi.sycamore.plugins.SycamorePlugin#getPluginName()
	 */
	@Override
	public String getPluginName()
	{
		return "AverageSpaceTravelled";
	}

	/* (non-Javadoc)
	 * @see it.diunipi.volpi.sycamore.plugins.SycamorePlugin#getAuthor()
	 */
	@Override
	public String getAuthor()
	{
		return "Valerio Volpi";
	}

}
