/**
 * 
 */
package it.diunipi.volpi.sycamore.plugins.measures;

import java.io.File;
import java.io.PrintWriter;
import java.util.Iterator;

import net.xeoh.plugins.base.annotations.PluginImplementation;

import it.diunipi.volpi.sycamore.engine.SycamoreRobot;
import it.diunipi.volpi.sycamore.engine.SycamoreRobotMatrix;
import it.diunipi.volpi.sycamore.gui.SycamorePanel;
import it.diunipi.volpi.sycamore.gui.SycamoreSystem;
import it.diunipi.volpi.sycamore.plugins.algorithms.Algorithm;
import it.diunipi.volpi.sycamore.plugins.algorithms.ZombieProtocol;
import it.diunipi.volpi.sycamore.plugins.measures.FileExportingSettingsPanel.FileExportingProperties;
import it.diunipi.volpi.sycamore.util.PropertyManager;

/**
 * @author Vale
 * 
 */
@PluginImplementation
public class AreaDiameter extends MeasureImpl
{
	private long						startingMillis	= 0;
	private int							stepCounter		= 0;
	private File						file			= null;
	private PrintWriter					printWriter		= null;
	private FileExportingSettingsPanel	panel_settings	= null;

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.diunipi.volpi.sycamore.plugins.measures.Measure#onSimulationStart()
	 */
	@Override
	public void onSimulationStart()
	{
		startingMillis = System.currentTimeMillis();

		// Open the file
		try
		{
			this.file = new File(PropertyManager.getSharedInstance().getProperty(FileExportingProperties.OUTPUT_FILE_PATH) + System.getProperty("file.separator") + this.getPluginName() + ".dat");
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
	 * @see it.diunipi.volpi.sycamore.plugins.measures.Measure#onSimulationStep()
	 */
	@Override
	public void onSimulationStep()
	{
		float diameter = 0;
		SycamoreRobotMatrix robots = engine.getRobots();
		
		Iterator<SycamoreRobot> iterator1 = robots.iterator();
		Iterator<SycamoreRobot> iterator2 = robots.iterator();
		while (iterator1.hasNext())
		{
			SycamoreRobot robot1 = iterator1.next();
			Algorithm algorithm1 = robot1.getAlgorithm();
			
			if (algorithm1 != null && algorithm1 instanceof ZombieProtocol)
			{
				while (iterator2.hasNext())
				{
					SycamoreRobot robot2 = iterator2.next();
					Algorithm algorithm2 = robot2.getAlgorithm();
					
					if (algorithm2 != null && algorithm2 instanceof ZombieProtocol)
					{
						// distance between zobies
						float distance = robot1.getLocalPosition().toVector3f().distance(robot2.getLocalPosition().toVector3f());
						if (distance > diameter)
						{
							diameter = distance;
						}
					}
				}
			}
		}
		
		long elapsedMillis = (System.currentTimeMillis() - startingMillis);
		double elapsedSeconds = ((double) elapsedMillis / 1000.0);

		float target = 10.0f; // i want 10 updates per second
		float frequency = 1.0f / SycamoreSystem.getSchedulerFrequency();

		if ((stepCounter % (frequency / target)) == 0)
		{
			this.printWriter.println("\t" + elapsedSeconds + "\t" + diameter);
		}
		
		stepCounter++;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.diunipi.volpi.sycamore.plugins.measures.Measure#onSimulationEnd()
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
		// TODO Auto-generated method stub
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
		// TODO Auto-generated method stub
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
		if (panel_settings == null)
		{
			panel_settings = new FileExportingSettingsPanel();
		}
		return panel_settings;
	}

}
