package it.diunipi.volpi.sycamore.plugins.humanpilot;
import it.diunipi.volpi.sycamore.engine.Observation;
import it.diunipi.volpi.sycamore.engine.Point3D;
import it.diunipi.volpi.sycamore.engine.SycamoreEngine.TYPE;
import it.diunipi.volpi.sycamore.engine.SycamoreObservedRobot;
import it.diunipi.volpi.sycamore.gui.SycamorePanel;
import it.diunipi.volpi.sycamore.plugins.algorithms.AlgorithmImpl;
import it.diunipi.volpi.sycamore.util.ApplicationProperties;
import it.diunipi.volpi.sycamore.util.PropertyManager;
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
public class HumanPilot3D extends AlgorithmImpl<Point3D>
{
	/* (non-Javadoc)
	 * @see it.diunipi.volpi.sycamore.plugins.algorithms.Algorithm#init()
	 */
	@Override
	public void init(SycamoreObservedRobot<Point3D> robot)
	{
		// Nothing to do
	}
	
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
	public Point3D compute(Vector<Observation<Point3D>> observations, SycamoreObservedRobot<Point3D> caller)
	{
		int minX = PropertyManager.getSharedInstance().getIntegerProperty(ApplicationProperties.INITIAL_POSITION_MIN_X);
		int maxX = PropertyManager.getSharedInstance().getIntegerProperty(ApplicationProperties.INITIAL_POSITION_MAX_X);
		int minY = PropertyManager.getSharedInstance().getIntegerProperty(ApplicationProperties.INITIAL_POSITION_MIN_Y);
		int maxY = PropertyManager.getSharedInstance().getIntegerProperty(ApplicationProperties.INITIAL_POSITION_MAX_Y);
		int minZ = PropertyManager.getSharedInstance().getIntegerProperty(ApplicationProperties.INITIAL_POSITION_MIN_Z);
		int maxZ = PropertyManager.getSharedInstance().getIntegerProperty(ApplicationProperties.INITIAL_POSITION_MAX_Z);

		
		return SycamoreUtil.getRandomPoint3D(minX, maxX, minY, maxY, minZ, maxZ);
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
		return TYPE.TYPE_3D;
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
