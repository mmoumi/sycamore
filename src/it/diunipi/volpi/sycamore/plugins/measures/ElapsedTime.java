/**
 * 
 */
package it.diunipi.volpi.sycamore.plugins.measures;

import it.diunipi.volpi.sycamore.gui.SycamorePanel;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import net.xeoh.plugins.base.annotations.PluginImplementation;

/**
 * This plugin measures the elapsed time in the simulation. It has a counter that is set to zero when
 * the simulation begins and that returns the elapsed time expressed in milliseconds at each call of
 * <code>getMeasuredCost()</code>.
 * 
 * @author Valerio Volpi - vale.v@me.com
 */
@PluginImplementation
public class ElapsedTime extends MeasureImpl
{
	private long	startingMillis	= 0;
	private long	elapsed			= 0;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * it.diunipi.volpi.sycamore.plugins.Measure#onSimulationStart(it.diunipi.volpi.sycamore.model
	 * .SycamoreRobot)
	 */
	@Override
	public void onSimulationStart()
	{
		System.out.println("START - " + this.getPluginName());
		
		elapsed = 0;
		startingMillis = System.currentTimeMillis();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * it.diunipi.volpi.sycamore.plugins.Measure#onSimulationStep(it.diunipi.volpi.sycamore.model
	 * .SycamoreRobot)
	 */
	@Override
	public void onSimulationStep()
	{

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * it.diunipi.volpi.sycamore.plugins.Measure#onSimulationEnd(it.diunipi.volpi.sycamore.model
	 * .SycamoreRobot)
	 */
	@Override
	public void onSimulationEnd()
	{
		System.out.println("STOP - " + this.getPluginName());
		
		elapsed = (System.currentTimeMillis() - startingMillis);
		startingMillis = System.currentTimeMillis();

		Date date = new Date(elapsed);
		DateFormat formatter = new SimpleDateFormat("mm:ss:SSS");
		String dateFormatted = formatter.format(date);

		System.out.println("Elapsed time: " + dateFormatted);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.diunipi.volpi.sycamore.plugins.SycamorePlugin#getShortDescription()
	 */
	@Override
	public String getPluginShortDescription()
	{
		return "Measures the elapsed time in the simulation.";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.diunipi.volpi.sycamore.plugins.SycamorePlugin#getLongDescription()
	 */
	@Override
	public String getPluginLongDescription()
	{
		return "This plugin measures the elapsed time in the simulation. It has a counter that is set to zero when the " +
				"simulation begins and that returns the elapsed time expressed in milliseconds at each call of getMeasuredCost().";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.diunipi.volpi.sycamore.plugins.SycamorePlugin#getSettings()
	 */
	@Override
	public SycamorePanel getPanel_settings()
	{
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.diunipi.volpi.sycamore.plugins.SycamorePlugin#getAuthor()
	 */
	@Override
	public String getAuthor()
	{
		return "Valerio Volpi";
	}

}
