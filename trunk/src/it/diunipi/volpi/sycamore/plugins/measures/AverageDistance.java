/**
 * 
 */
package it.diunipi.volpi.sycamore.plugins.measures;

import it.diunipi.volpi.sycamore.engine.Point2D;
import it.diunipi.volpi.sycamore.engine.SycamoreRobot;
import it.diunipi.volpi.sycamore.engine.SycamoreRobotMatrix;
import it.diunipi.volpi.sycamore.gui.SycamorePanel;
import it.diunipi.volpi.sycamore.gui.SycamoreSystem;
import it.diunipi.volpi.sycamore.util.ApplicationProperties;
import it.diunipi.volpi.sycamore.util.PropertyManager;
import it.diunipi.volpi.sycamore.util.SycamoreProperty;

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
public class AverageDistance extends MeasureImpl
{
	/**
	 * Properties related to the average distance plugin
	 * 
	 * @author Valerio Volpi - vale.v@me.com
	 */
	private enum AverageDistanceProperties implements SycamoreProperty
	{
		OUTPUT_FILE_PATH("Output file path", ApplicationProperties.WORKSPACE_DIR.getDefaultValue() + System.getProperty("file.separator") + "Reports");

		private String	description		= null;
		private String	defaultValue	= null;

		/**
		 * Constructor.
		 */
		AverageDistanceProperties(String description, String defaultValue)
		{
			this.description = description;
			this.defaultValue = defaultValue;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see it.diunipi.volpi.sycamore.util.SycamoreProperty#getDescription()
		 */
		@Override
		public String getDescription()
		{
			return description;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see it.diunipi.volpi.sycamore.util.SycamoreProperty#getDefaultValue()
		 */
		@Override
		public String getDefaultValue()
		{
			return defaultValue;
		}
	}
	
	private long							startingMillis	= 0;
	private int								stepCounter		= 0;
	private File							file			= null;
	private PrintWriter						printWriter		= null;
	private AverageDistanceSettingsPanel	panel_settings	= null;

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
			this.file = new File(PropertyManager.getSharedInstance().getProperty(AverageDistanceProperties.OUTPUT_FILE_PATH));
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

			// get all the robots and measure the sum of distances
			float sum = 0;
			float num = 0;

			Iterator<SycamoreRobot<Point2D>> robotsIterator = robots.robotsIterator();
			while (robotsIterator.hasNext())
			{
				SycamoreRobot<Point2D> robot = robotsIterator.next();

				sum = sum + (robot.getGlobalPosition().distanceTo(humanPilotPos));
				num++;
			}

			float average = sum / num;

			long elapsedMillis = (System.currentTimeMillis() - startingMillis);
			long elapsedSeconds = (elapsedMillis / 1000);

			float target = 10.0f; // i want 10 updates per second
			float frequency = 1.0f / SycamoreSystem.getSchedulerFrequency();

			if ((stepCounter % (frequency / target)) == 0)
			{
				this.printWriter.println("\t" + elapsedSeconds + "\t" + average);
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
		return "Measures the average distance between robots and leader.";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.diunipi.volpi.sycamore.plugins.SycamorePlugin#getLongDescription()
	 */
	@Override
	public String getPluginLongDescription()
	{
		return "This plugin measures the average distance between robots and the leader. It counts 10 steps per second and writes a line in a GnuPlot-supported file that contains the time (in seconds) on the x axis and the average on y axis. Only one leader is supposed to be in the scene.";
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see it.diunipi.volpi.sycamore.plugins.SycamorePlugin#getPanel_settings()
	 */
	@Override
	public SycamorePanel getPanel_settings()
	{
		if (panel_settings == null)
		{
			panel_settings = new AverageDistanceSettingsPanel();
		}
		return panel_settings;
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
