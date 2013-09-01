package it.diunipi.volpi.sycamore.plugins.algorithms;

import it.diunipi.volpi.sycamore.engine.Observation;
import it.diunipi.volpi.sycamore.engine.Point3D;
import it.diunipi.volpi.sycamore.engine.SycamoreObservedRobot;
import it.diunipi.volpi.sycamore.engine.TooManyLightsException;
import it.diunipi.volpi.sycamore.engine.SycamoreEngine.TYPE;
import it.diunipi.volpi.sycamore.gui.SycamorePanel;
import it.diunipi.volpi.sycamore.util.ApplicationProperties;
import it.diunipi.volpi.sycamore.util.PropertyManager;
import it.diunipi.volpi.sycamore.util.SycamoreUtil;

import java.util.Vector;

import net.xeoh.plugins.base.annotations.PluginImplementation;

import com.jme3.math.ColorRGBA;

/**
 * A dummy 3D algorithm. It makes the robots perform simple and predefinite movements for the
 * purpose of testing.
 * 
 * @author Valerio Volpi - vale.v@me.com
 */
@PluginImplementation
public class DummyAlgorithm3D extends AlgorithmImpl<Point3D>
{
	private final Vector<Point3D>	points	= new Vector<Point3D>();
	private int						count	= 0;
	private static final int		ROUNDS	= 11;

	/**
	 * Default constructor.
	 */
	public DummyAlgorithm3D()
	{
		int minX = PropertyManager.getSharedInstance().getIntegerProperty(ApplicationProperties.INITIAL_POSITION_MIN_X.name());
		int maxX = PropertyManager.getSharedInstance().getIntegerProperty(ApplicationProperties.INITIAL_POSITION_MAX_X.name());
		int minY = PropertyManager.getSharedInstance().getIntegerProperty(ApplicationProperties.INITIAL_POSITION_MIN_Y.name());
		int maxY = PropertyManager.getSharedInstance().getIntegerProperty(ApplicationProperties.INITIAL_POSITION_MAX_Y.name());
		int minZ = PropertyManager.getSharedInstance().getIntegerProperty(ApplicationProperties.INITIAL_POSITION_MIN_Z.name());
		int maxZ = PropertyManager.getSharedInstance().getIntegerProperty(ApplicationProperties.INITIAL_POSITION_MAX_Z.name());
		
		for (int i = 0; i < ROUNDS; i++)
		{
			points.add(new Point3D(0, 0, 0));
			points.add(SycamoreUtil.getRandomPoint3D(minX, maxX, minY, maxY, minZ, maxZ));
			points.add(new Point3D(3, 0, 0));
			points.add(new Point3D(0, 0, 0));
			points.add(SycamoreUtil.getRandomPoint3D(minX, maxX, minY, maxY, minZ, maxZ));
			points.add(new Point3D(0, 3, 0));
			points.add(new Point3D(0, 0, 0));
			points.add(SycamoreUtil.getRandomPoint3D(minX, maxX, minY, maxY, minZ, maxZ));
			points.add(new Point3D(0, 0, 3));
			points.add(new Point3D(0, 0, 0));
			points.add(SycamoreUtil.getRandomPoint3D(minX, maxX, minY, maxY, minZ, maxZ));
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.diunipi.volpi.sycamore.plugins.SycamorePlugin#getPluginShortDescription()
	 */
	@Override
	public String getPluginShortDescription()
	{
		return "Dummy 3D algorithm";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.diunipi.volpi.sycamore.plugins.SycamorePlugin#getPluginLongDescription()
	 */
	@Override
	public String getPluginLongDescription()
	{
		return "A dummy algorithm. It makes the robots perform simple and predefinite movements for the purpose of testing.";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.diunipi.volpi.sycamore.plugins.Algorithm#getReferences()
	 */
	@Override
	public String getReferences()
	{
		return "Nessun articolo";
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
	 * @see it.diunipi.volpi.sycamore.plugins.Algorithm#compute(java.util.Vector,
	 * it.diunipi.volpi.sycamore.model.Observation)
	 */
	@Override
	public Point3D compute(Vector<Observation<Point3D>> observations, SycamoreObservedRobot<Point3D> callee)
	{
		if (count < ROUNDS)
		{
			Point3D ret = points.elementAt(count);
			count++;

			return ret;
		}
		else
		{
			try
			{
				callee.turnLightOn(ColorRGBA.Yellow);
			}
			catch (TooManyLightsException e)
			{
				e.printStackTrace();
			}

			setFinished(true);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.diunipi.volpi.sycamore.plugins.AlgorithmImpl#reset()
	 */
	@Override
	public void reset()
	{
		super.reset();
		count = 0;

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
}
