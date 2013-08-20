package it.diunipi.volpi.sycamore.plugins.algorithms;

import it.diunipi.volpi.sycamore.engine.SycamoreEngine.TYPE;
import it.diunipi.volpi.sycamore.gui.SycamorePanel;
import it.diunipi.volpi.sycamore.model.Observation;
import it.diunipi.volpi.sycamore.model.Point2D;
import it.diunipi.volpi.sycamore.model.SycamoreObservedRobot;
import it.diunipi.volpi.sycamore.model.TooManyLightsException;
import it.diunipi.volpi.sycamore.util.SycamoreUtil;

import java.util.Vector;

import net.xeoh.plugins.base.annotations.PluginImplementation;

import com.jme3.math.ColorRGBA;

/**
 * A dummy 2D algorithm. It makes the robots perform simple and predefinite movements for the purpose
 * of testing.
 * 
 * @author Valerio Volpi - vale.v@me.com
 */
@PluginImplementation
public class DummyAlgorithm2D extends AlgorithmImpl<Point2D>
{
	private final Vector<Point2D>	points	= new Vector<Point2D>();
	private int						count	= 0;
	private static final int		ROUNDS	= 10000;

	/**
	 * Default constructor.
	 */
	public DummyAlgorithm2D()
	{
		for (int i = 0; i < ROUNDS; i++)
		{
			Point2D point = (i % 2 == 0 ? new Point2D(0, 0) : SycamoreUtil.getRandomPoint2D(-10, 10, -10, 10));
			points.add(point);
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
		return "Dummy 2D algorithm";
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
	 * @see it.diunipi.volpi.sycamore.plugins.SycamorePlugin#getPanel_settings()
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
	 * @see it.diunipi.volpi.sycamore.plugins.algorithms.Algorithm#getReferences()
	 */
	@Override
	public String getReferences()
	{
		return null;
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
		if (count < ROUNDS)
		{
			Point2D ret = points.elementAt(count);
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
		return TYPE.TYPE_2D;
	}
}