package it.diunipi.volpi.sycamore.plugins.algorithms;

import it.diunipi.volpi.sycamore.engine.SycamoreEngine.TYPE;
import it.diunipi.volpi.sycamore.gui.SycamorePanel;
import it.diunipi.volpi.sycamore.model.Observation;
import it.diunipi.volpi.sycamore.model.Point3D;
import it.diunipi.volpi.sycamore.model.SycamoreObservedRobot;
import it.diunipi.volpi.sycamore.plugins.memory.Memory;
import it.diunipi.volpi.sycamore.plugins.memory.RequestedDataNotInMemoryException;

import java.util.Vector;

import net.xeoh.plugins.base.annotations.PluginImplementation;

/**
 * A dummy 3D algorithm. It makes the robots perform simple and predefinite movements for the purpose
 * of testing.
 * 
 * @author Valerio Volpi - vale.v@me.com
 */
@PluginImplementation
public class DummyAlgorithm3D extends AlgorithmImpl<Point3D>
{
	private int	roundCounter	= 0;
	private int	roundsTotal		= 10;

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
		int multiplier = (roundCounter % 2 == 0 ? 1 : -2);
		int pos = roundCounter * multiplier;
		Point3D ret = new Point3D(pos, pos, pos);

		roundCounter++;
		if (roundCounter == roundsTotal)
		{
			setFinished(true);
			return null;
		}

		// print memory
		Memory<Point3D> memory = callee.getMemory();
		if (memory != null)
		{
			for (int i = 0; i < memory.getMemoryLimit(); i++)
			{
				try
				{
					System.out.println("PAST POS " + i + ": " + memory.getPastSelfPosition(i));
					System.out.println("PAST SNAP " + i + ": " + memory.getPastSnapshot(i));
				}
				catch (RequestedDataNotInMemoryException e)
				{
					System.out.println("Memory does not contain step " + i);
				}
			}
		}

		return ret;
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
		roundCounter = 0;

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
