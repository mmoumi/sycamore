/**
 * 
 */
package it.diunipi.volpi.sycamore.plugins.measures;

import it.diunipi.volpi.sycamore.engine.Point2D;
import it.diunipi.volpi.sycamore.engine.SycamoreRobot;
import it.diunipi.volpi.sycamore.engine.SycamoreRobotMatrix;
import it.diunipi.volpi.sycamore.gui.SycamorePanel;
import it.diunipi.volpi.sycamore.plugins.measures.FileExportingSettingsPanel.FileExportingProperties;
import it.diunipi.volpi.sycamore.util.PropertyManager;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;

import net.xeoh.plugins.base.annotations.PluginImplementation;

/**
 * @author Vale
 * 
 */
@PluginImplementation
public class RobotsPositions extends MeasureImpl
{
	private FileExportingSettingsPanel			panel_settings	= null;
	private HashMap<SycamoreRobot, Integer>		counts			= new HashMap<SycamoreRobot, Integer>();
	private HashMap<SycamoreRobot, PrintWriter>	writers			= new HashMap<SycamoreRobot, PrintWriter>();

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.diunipi.volpi.sycamore.plugins.measures.Measure#onSimulationStart()
	 */
	@Override
	public void onSimulationStart()
	{

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.diunipi.volpi.sycamore.plugins.measures.Measure#onSimulationStep()
	 */
	@Override
	public void onSimulationStep()
	{
		SycamoreRobotMatrix robots = engine.getRobots();
		Iterator<SycamoreRobot> iterator = robots.robotsIterator();

		while (iterator.hasNext())
		{
			try
			{
				SycamoreRobot robot = iterator.next();
				if (robot.getAlgorithm() != null)
				{
					// apro un file per ogni umano
					File file = new File(PropertyManager.getSharedInstance().getProperty(FileExportingProperties.OUTPUT_FILE_PATH) + System.getProperty("file.separator") + this.getPluginName() + "_"
							+ robot.getAlgorithm().getPluginName() + "_" + robot.getID() + ".csv");

					boolean writeHeader = false;
					if (!file.exists())
					{
						writeHeader = true;
						
						counts.put(robot, 0);
						writers.put(robot, new PrintWriter(new BufferedWriter(new FileWriter(file, true))));
					}

					PrintWriter printWriter = writers.get(robot);
					if (writeHeader)
					{
						printWriter.append("X;Y\n");
					}

					int count = counts.get(robot);
					if (count % 30 == 0)
					{
						Point2D position = (Point2D) robot.getLocalPosition();
						printWriter.append(position.x + ";" + position.y + "\n");
					}

					counts.put(robot, count + 1);
				}
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.diunipi.volpi.sycamore.plugins.measures.Measure#onSimulationEnd()
	 */
	@Override
	public void onSimulationEnd()
	{
		counts.clear();
		
		for (PrintWriter printWriter : writers.values())
		{
			printWriter.close();
		}
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
