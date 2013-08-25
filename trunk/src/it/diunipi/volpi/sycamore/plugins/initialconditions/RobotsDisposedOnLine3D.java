/**
 * 
 */
package it.diunipi.volpi.sycamore.plugins.initialconditions;

import it.diunipi.volpi.sycamore.engine.Point3D;
import it.diunipi.volpi.sycamore.engine.SycamoreRobotMatrix;
import it.diunipi.volpi.sycamore.engine.SycamoreEngine.TYPE;
import it.diunipi.volpi.sycamore.gui.SycamorePanel;
import it.diunipi.volpi.sycamore.util.SycamoreUtil;
import net.xeoh.plugins.base.annotations.PluginImplementation;

/**
 * An initial condition where the robots are randomly disposed on the line starting from point (-10,
 * 1, 1) and terminating on point (10, 1, 1).
 * 
 * @author Valerio Volpi - vale.v@me.com
 */
@PluginImplementation
public class RobotsDisposedOnLine3D extends InitialConditionsImpl<Point3D>
{
	/*
	 * (non-Javadoc)
	 * 
	 * @see it.diunipi.volpi.sycamore.plugins.InitialConditions#nextStartingPoint(java.util.Vector)
	 */
	@Override
	public Point3D nextStartingPoint(SycamoreRobotMatrix<Point3D> robots)
	{
		float y = 1.0f;
		float x = SycamoreUtil.getRandomFloat(-10.0f, 10.0f);
		float z = 1.0f;

		return new Point3D(x, y, z);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.diunipi.volpi.sycamore.plugins.SycamoreTypedPlugin#getType()
	 */
	@Override
	public TYPE getType()
	{
		return TYPE.TYPE_3D;
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
		return "Makes the robots randomly disposed on a 3D line.";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.diunipi.volpi.sycamore.plugins.SycamorePlugin#getPluginLongDescription()
	 */
	@Override
	public String getPluginLongDescription()
	{
		return "An initial condition where the robots are randomly disposed on the line starting from point (-10, 1, 1) and terminating on point (10, 1, 1).";
	}
}
