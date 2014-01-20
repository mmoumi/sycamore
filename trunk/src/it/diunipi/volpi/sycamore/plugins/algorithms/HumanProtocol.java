/**
 * 
 */
package it.diunipi.volpi.sycamore.plugins.algorithms;

import it.diunipi.volpi.sycamore.engine.Observation;
import it.diunipi.volpi.sycamore.engine.Point2D;
import it.diunipi.volpi.sycamore.engine.SycamoreEngine.TYPE;
import it.diunipi.volpi.sycamore.engine.SycamoreObservedLight;
import it.diunipi.volpi.sycamore.engine.SycamoreObservedRobot;
import it.diunipi.volpi.sycamore.engine.TooManyLightsException;
import it.diunipi.volpi.sycamore.gui.SycamorePanel;

import java.util.Vector;

import net.xeoh.plugins.base.annotations.PluginImplementation;

import com.jme3.math.ColorRGBA;

/**
 * @author Valerio Volpi - vale.v@me.com
 * 
 */
@PluginImplementation
public class HumanProtocol extends AlgorithmImpl<Point2D>
{
	private boolean			bitten		= false;
	private int				robotID		= 0;
	protected float			radius		= 15.0f;

	protected final Point2D	center		= new Point2D();
	protected final boolean	move		= true;
	protected final int		totRobots	= 6;

	protected static int	numRobots	= 0;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * it.diunipi.volpi.sycamore.plugins.algorithms.Algorithm#init(it.diunipi.volpi.sycamore.engine
	 * .SycamoreObservedRobot)
	 */
	@Override
	public void init(SycamoreObservedRobot<Point2D> robot)
	{
		System.out.println("A � " + totRobots);
		System.out.println("A � " + numRobots);
		System.out.println("A � " + robotID);
		
		this.robotID = numRobots;
		numRobots++;
		
		System.out.println("B � " + totRobots);
		System.out.println("B � " + numRobots);
		System.out.println("B � " + robotID);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.diunipi.volpi.sycamore.plugins.algorithms.Algorithm#compute(java.util.Vector,
	 * it.diunipi.volpi.sycamore.engine.SycamoreObservedRobot)
	 */
	@Override
	public Point2D compute(Vector<Observation<Point2D>> observations, final SycamoreObservedRobot<Point2D> caller)
	{
		// The human is expected to have the first light being red, to identify its nature, or black
		// if it is bitten
		// take the first light of the caller
		SycamoreObservedLight firstLight = caller.getLights().firstElement();

		if (this.isBitten() && !firstLight.getColor().equals(ColorRGBA.Black))
		{
			try
			{
				caller.turnLightOff();
				caller.turnLightOff();
				caller.turnLightOn(ColorRGBA.Black);
				caller.turnLightOn(ColorRGBA.Black);
			}
			catch (TooManyLightsException e)
			{
				e.printStackTrace();
			}
		}

		// if not bitten act
		if (!this.isBitten())
		{

			// if that light is not red, turn it on
			if (!firstLight.getColor().equals(ColorRGBA.Red))
			{
				try
				{
					caller.turnLightOn(ColorRGBA.Red);
					caller.turnLightOn(ColorRGBA.Yellow, 200.0f);
				}
				catch (TooManyLightsException e)
				{
					e.printStackTrace();
				}
			}

			checkTermination(observations, caller);

			if (move && checkZombiesCloseness(observations, caller))
			{
				radius = radius + 15;
				return getPosition(center);
			}

			return null;
		}
		else
		{
			return null;
		}
	}

	/**
	 * @param observations
	 * @param caller
	 * @return
	 */
	private boolean checkZombiesCloseness(Vector<Observation<Point2D>> observations, SycamoreObservedRobot<Point2D> caller)
	{
		for (Observation<Point2D> observation : observations)
		{
			Point2D position = observation.getRobotPosition();
			if (caller.getLocalPosition().distanceTo(position) < (radius / 2))
			{
				return true;
			}
		}

		return false;
	}

	/**
	 * 
	 */
	protected void checkTermination(Vector<Observation<Point2D>> observations, final SycamoreObservedRobot<Point2D> caller)
	{
		boolean terminated = true;
		float bound = 2.0f * (ZombieProtocol.getMinActivity() / 2);

		for (Observation<Point2D> x : observations)
		{
			for (Observation<Point2D> y : observations)
			{
				if (x != y && x.getRobotPosition().distanceTo(y.getRobotPosition()) <= bound)
				{
					terminated = false;
				}
			}
		}

		if (terminated)
		{
			try
			{
				caller.turnLightOff();
				caller.turnLightOff();
				caller.turnLightOn(ColorRGBA.Green);
				caller.turnLightOn(ColorRGBA.Green);

				setFinished(true);
			}
			catch (TooManyLightsException e)
			{
				e.printStackTrace();
			}
		}
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
	 * @see it.diunipi.volpi.sycamore.plugins.SycamorePlugin#getAuthor()
	 */
	@Override
	public String getAuthor()
	{
		return "Valerio Volpi";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.diunipi.volpi.sycamore.plugins.SycamorePlugin#getPluginShortDescription()
	 */
	@Override
	public String getPluginShortDescription()
	{
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.diunipi.volpi.sycamore.plugins.SycamorePlugin#getPluginLongDescription()
	 */
	@Override
	public String getPluginLongDescription()
	{
		return null;
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

	/**
	 * 
	 */
	public void biteHuman()
	{
		bitten = true;
	}

	/**
	 * @return the bitten
	 */
	public boolean isBitten()
	{
		return bitten;
	}

	/**
	 * 
	 */
	protected Point2D getPosition(Point2D center)
	{
		System.out.println("C � " + totRobots);
		System.out.println("C � " + numRobots);
		System.out.println("C � " + robotID);
		
		double angle = 6.2831853071795862D / (double) totRobots;

		float xPoint = (float) (center.x + (radius * Math.cos(robotID * angle)));
		float yPoint = (float) (center.y - (radius * Math.sin(robotID * angle)));

		return new Point2D(xPoint, yPoint);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.diunipi.volpi.sycamore.plugins.algorithms.AlgorithmImpl#reset()
	 */
	@Override
	public synchronized void reset()
	{
		super.reset();
		bitten = false;
		
		numRobots--;
	}
}