package it.diunipi.volpi.sycamore.plugins.humanpilot;

import it.diunipi.volpi.sycamore.engine.SycamoreEngine.TYPE;
import it.diunipi.volpi.sycamore.gui.SycamorePanel;
import it.diunipi.volpi.sycamore.model.Observation;
import it.diunipi.volpi.sycamore.model.Point2D;
import it.diunipi.volpi.sycamore.model.SycamoreObservedRobot;
import it.diunipi.volpi.sycamore.plugins.algorithms.AlgorithmImpl;
import it.diunipi.volpi.sycamore.util.SycamoreUtil;

import java.util.Vector;

import net.xeoh.plugins.base.annotations.PluginImplementation;

/**
 * A robot that behaves like if it was driven by a human. Its behavior is unpredictable. The human
 * pilot robot is not subject to the robot scheduler, but to the human pilot scheduler. A robot that
 * executes this algorithm is considered a human pilot robot.
 * 
 * @author Valerio Volpi - vale.v@me.com
 */
@PluginImplementation
public class HumanPilot2D extends AlgorithmImpl<Point2D>
{
	/*
	 * (non-Javadoc)
	 * 
	 * @see it.diunipi.volpi.sycamore.plugins.AlgorithmImpl#isHumanPilot()
	 */
	@Override
	public boolean isHumanPilot()
	{
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.diunipi.volpi.sycamore.plugins.Algorithm#compute(java.util.Vector,
	 * it.diunipi.volpi.sycamore.model.Observation)
	 */
	@Override
	public Point2D compute(Vector<Observation<Point2D>> observations, SycamoreObservedRobot<Point2D> callee)
	{
		return SycamoreUtil.getRandomPoint2D(-10, 10, -10, 10);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.diunipi.volpi.sycamore.plugins.Algorithm#getReferences()
	 */
	@Override
	public String getReferences()
	{
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.diunipi.volpi.sycamore.plugins.Algorithm#getType()
	 */
	@Override
	public TYPE getType()
	{
		return TYPE.TYPE_2D;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.diunipi.volpi.sycamore.plugins.SycamorePlugin#getShortDescription()
	 */
	@Override
	public String getPluginShortDescription()
	{
		return "A human-driven robot.";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.diunipi.volpi.sycamore.plugins.SycamorePlugin#getLongDescription()
	 */
	@Override
	public String getPluginLongDescription()
	{
		return "A robot that behaves like if it was driven by a human. Its behavior is unpredictable.";
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

}
