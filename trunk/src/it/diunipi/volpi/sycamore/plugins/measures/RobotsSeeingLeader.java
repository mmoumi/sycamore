/**
 * 
 */
package it.diunipi.volpi.sycamore.plugins.measures;

import it.diunipi.volpi.sycamore.engine.Point2D;
import it.diunipi.volpi.sycamore.engine.SycamoreRobot;
import it.diunipi.volpi.sycamore.engine.SycamoreRobotMatrix;
import it.diunipi.volpi.sycamore.gui.SycamorePanel;
import it.diunipi.volpi.sycamore.gui.SycamoreSystem;
import it.diunipi.volpi.sycamore.plugins.visibilities.Visibility;

import java.io.File;
import java.io.PrintWriter;
import java.util.Iterator;

import net.xeoh.plugins.base.annotations.PluginImplementation;

/**
 * This plugin measures the elapsed time in the simulation. It has a counter that is set to zero
 * when the simulation begins and that returns the elapsed time expressed in milliseconds at each
 * call of <code>getMeasuredCost()</code>.
 * 
 * @author Valerio Volpi - vale.v@me.com
 */
@PluginImplementation
public class RobotsSeeingLeader extends MeasureImpl
{
	private long		startingMillis	= 0;
	private int			stepCounter		= 0;
	private File		file			= null;
	private PrintWriter	printWriter		= null;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * it.diunipi.volpi.sycamore.plugins.Measure#onSimulationStart(it.diunipi.volpi.sycamore.model
	 * .SycamoreRobot)
	 */
	@Override
	public void onSimulationStart()
	{
		startingMillis = System.currentTimeMillis();

		// Open the file
		try
		{
			this.file = new File("/Users/Vale/Desktop/robotsLeader.dat");
			this.printWriter = new PrintWriter(file);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * it.diunipi.volpi.sycamore.plugins.Measure#onSimulationStep(it.diunipi.volpi.sycamore.model
	 * .SycamoreRobot)
	 */
	@Override
	public void onSimulationStep()
	{
		// get the average of the distances between the robots and the leader
		SycamoreRobotMatrix<Point2D> robots = engine.getRobots();

		// look for the human pilot
		Iterator<SycamoreRobot<Point2D>> humanPilotIterator = robots.humanPilotsIterator();
		if (humanPilotIterator.hasNext())
		{
			SycamoreRobot<Point2D> humanPilot = humanPilotIterator.next();
			Point2D humanPilotPos = humanPilot.getGlobalPosition();

			int count = 0;

			Iterator<SycamoreRobot<Point2D>> robotsIterator = robots.robotsIterator();
			while (robotsIterator.hasNext())
			{
				SycamoreRobot<Point2D> robot = robotsIterator.next();
				Visibility<Point2D> visibility = robot.getVisibility();
				
				if (visibility.isPointVisible(humanPilotPos))
				{
					count++;
				}
			}

			long elapsedMillis = (System.currentTimeMillis() - startingMillis);
			long elapsedSeconds = (elapsedMillis / 1000);

			float target = 10.0f; // i want 10 updates per second
			float frequency = 1.0f / SycamoreSystem.getSchedulerFrequency();

			if ((stepCounter % (frequency / target)) == 0)
			{
				this.printWriter.println("\t" + elapsedSeconds + "\t" + count);
			}
		}
		stepCounter++;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * it.diunipi.volpi.sycamore.plugins.Measure#onSimulationEnd(it.diunipi.volpi.sycamore.model
	 * .SycamoreRobot)
	 */
	@Override
	public void onSimulationEnd()
	{
		// Close file
		this.printWriter.close();
		stepCounter = 0;

		startingMillis = System.currentTimeMillis();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.diunipi.volpi.sycamore.plugins.SycamorePlugin#getShortDescription()
	 */
	@Override
	public String getPluginShortDescription()
	{
		return "Counts how many robots have the leader in their visibility range.";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.diunipi.volpi.sycamore.plugins.SycamorePlugin#getLongDescription()
	 */
	@Override
	public String getPluginLongDescription()
	{
		return "This plugin counts the number of robots that have the leader (the human pilot) in their visible area. It counts 10 steps per second and writes a line in a GnuPlot-supported file that contains the time (in seconds) on the x axis and the count value on y axis. Only one leader is supposed to be in the scene.";
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
		return "Valerio Volpi";
	}

}
