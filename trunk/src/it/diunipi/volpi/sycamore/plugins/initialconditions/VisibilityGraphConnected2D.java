/**
 * 
 */
package it.diunipi.volpi.sycamore.plugins.initialconditions;

import it.diunipi.volpi.sycamore.engine.SycamoreEngine.TYPE;
import it.diunipi.volpi.sycamore.gui.SycamorePanel;
import it.diunipi.volpi.sycamore.model.Point2D;
import it.diunipi.volpi.sycamore.model.SycamoreRobot;
import it.diunipi.volpi.sycamore.model.SycamoreRobotMatrix;
import it.diunipi.volpi.sycamore.plugins.visibilities.Visibility;
import it.diunipi.volpi.sycamore.util.SycamoreUtil;

import java.util.Iterator;

import net.xeoh.plugins.base.annotations.PluginImplementation;

/**
 * An initial condition that makes the robots start disposed in a way that their visibility graph is
 * connected, so that all of them can see at least one of its neighbors.
 * 
 * @author Valerio Volpi - vale.v@me.com
 */
@PluginImplementation
public class VisibilityGraphConnected2D extends InitialConditionsImpl<Point2D>
{
	/*
	 * (non-Javadoc)
	 * 
	 * @see it.diunipi.volpi.sycamore.plugins.InitialConditions#nextStartingPoint(java.util.Vector)
	 */
	@Override
	public Point2D nextStartingPoint(SycamoreRobotMatrix<Point2D> robots)
	{
		if (robots.robotsCount() > 0)
		{
			// choose one random robot between passed ones
			int num = SycamoreUtil.getRandomInt(0, robots.robotsCount() + 1);
			Iterator<SycamoreRobot<Point2D>> iterator = robots.robotsIterator();

			SycamoreRobot<Point2D> chosen = null;
			for (int i = 0; i < num; i++)
			{
				chosen = iterator.next();
			}

			if (chosen != null)
			{
				// take a point inside robot's visible area
				Visibility<Point2D> visibilty = chosen.getVisibility();
				if (visibilty != null)
				{
					return visibilty.getPointInside(chosen.getLocalPosition());
				}
			}
		}

		return SycamoreUtil.getRandomPoint2D(-5, 5, -5, 5);
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
		return "Makes the visibility graph initially connected";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.diunipi.volpi.sycamore.plugins.SycamorePlugin#getPluginLongDescription()
	 */
	@Override
	public String getPluginLongDescription()
	{
		return "An initial condition that makes the robots start disposed in a way that their visibility graph is connected, so that all of them can see at least one of its neighbors.";
	}
}
