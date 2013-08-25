/**
 * 
 */
package it.diunipi.volpi.sycamore.plugins.initialconditions;

import it.diunipi.volpi.sycamore.engine.SycamoreEngine.TYPE;
import it.diunipi.volpi.sycamore.gui.SycamorePanel;
import it.diunipi.volpi.sycamore.model.Point2D;
import it.diunipi.volpi.sycamore.model.SycamoreRobotMatrix;
import net.xeoh.plugins.base.annotations.PluginImplementation;

/**
 * An initial condition where the robots are always disposed on origin (0,0)
 * 
 * @author Valerio Volpi - vale.v@me.com
 */
@PluginImplementation
public class DummyInitialCondition2D extends InitialConditionsImpl<Point2D>
{
	/*
	 * (non-Javadoc)
	 * 
	 * @see it.diunipi.volpi.sycamore.plugins.InitialConditions#nextStartingPoint(java.util.Vector)
	 */
	@Override
	public Point2D nextStartingPoint(SycamoreRobotMatrix<Point2D> robots)
	{
		return new Point2D(0, 0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.diunipi.volpi.sycamore.plugins.SycamoreTypedPlugin#getType()
	 */
	@Override
	public TYPE getType()
	{
		return TYPE.TYPE_2D;
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
		return "Valerio Volpi - vale.v@me.com";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.diunipi.volpi.sycamore.plugins.SycamorePlugin#getPluginShortDescription()
	 */
	@Override
	public String getPluginShortDescription()
	{
		return "Dummy 2D Initial condition";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.diunipi.volpi.sycamore.plugins.SycamorePlugin#getPluginLongDescription()
	 */
	@Override
	public String getPluginLongDescription()
	{
		return "Dummy 2D Initial condition where robots are disposed on the origin of the axes";
	}
}
