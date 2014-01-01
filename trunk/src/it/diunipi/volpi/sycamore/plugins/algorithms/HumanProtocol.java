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
import it.diunipi.volpi.sycamore.gui.SycamoreSystem;
import it.diunipi.volpi.sycamore.gui.SycamoreSystem.TIMELINE_MODE;
import it.diunipi.volpi.sycamore.util.SycamoreUtil;

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
	private int		count	= 0;
	private boolean	bitten	= false;

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
		SycamoreSystem.setTimelineMode(TIMELINE_MODE.LIVE);
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
				}
				catch (TooManyLightsException e)
				{
					e.printStackTrace();
				}
			}

			if (count % 2000 == 0)
			{
				Runnable runnable = new Runnable()
				{
					@Override
					public void run()
					{
						try
						{
							caller.turnLightOn(ColorRGBA.Yellow, 200.0f);
							Thread.sleep(SycamoreUtil.getRandomInt(100, 2000));
							if (!bitten)
							{
								caller.turnLightOff();
							}
						}
						catch (Exception e)
						{
							e.printStackTrace();
						}
						return;
					}
				};

				new Thread(runnable).start();
			}

			count++;
			return null;
		}
		else
		{
			return null;
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
	
	/* (non-Javadoc)
	 * @see it.diunipi.volpi.sycamore.plugins.algorithms.AlgorithmImpl#reset()
	 */
	@Override
	public synchronized void reset()
	{
		super.reset();
		bitten = false;
	}
}
