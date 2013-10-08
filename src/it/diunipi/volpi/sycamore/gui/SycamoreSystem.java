/**
 * 
 */
package it.diunipi.volpi.sycamore.gui;

import it.diunipi.volpi.app.sycamore.SycamoreApp.APP_MODE;
import it.diunipi.volpi.sycamore.engine.NNotKnownException;
import it.diunipi.volpi.sycamore.engine.SycamoreEngine;
import it.diunipi.volpi.sycamore.engine.SycamoreHumanPilotSchedulerThread;
import it.diunipi.volpi.sycamore.engine.SycamoreSchedulerThread;
import it.diunipi.volpi.sycamore.engine.SycamoreVisualizerThread;
import it.diunipi.volpi.sycamore.jmescene.SycamoreJMEScene;
import it.diunipi.volpi.sycamore.util.ApplicationProperties;
import it.diunipi.volpi.sycamore.util.PropertyManager;

import java.io.File;
import java.util.HashMap;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JFrame;

import com.jme3.asset.AssetManager;
import com.jme3.renderer.RenderManager;
import com.jme3.system.JmeContext;

/**
 * This class represents the system and contains all data that is accessible everywhere
 * 
 * @author Valerio Volpi - vale.v@me.com
 */
public class SycamoreSystem
{
	private static final Object							schedulerGuiSynchronizer	= new Object();
	public static float									schedulerFrequency			= 1.0f / 640.0f;

	private static File									workspace					= null;
	private static Level								loggerLevel					= Level.SEVERE;

	private static SycamoreEngine						engine						= null;
	private static SycamoreSchedulerThread				schedulerThread				= null;
	private static SycamoreHumanPilotSchedulerThread	humanPilotSchedulerThread	= null;
	private static SycamoreVisualizerThread				visualizerThread			= null;
	private static SycamoreJMEScene						jmeSceneManager				= null;
	private static JFrame								mainFrame					= null;

	/**
	 * Initialize the system
	 */
	public static void initialize(APP_MODE appMode)
	{
		loadWorkspace();

		if (appMode == APP_MODE.SIMULATOR)
		{
			schedulerThread = new SycamoreSchedulerThread();
			schedulerThread.start();

			humanPilotSchedulerThread = new SycamoreHumanPilotSchedulerThread();
			humanPilotSchedulerThread.start();
		}
		else
		{
			visualizerThread = new SycamoreVisualizerThread();
			visualizerThread.start();
		}

		Logger.getLogger("").setLevel(loggerLevel);
	}

	/**
	 * Load the workspace file. If it does not exists, creates the directory
	 * 
	 * @return
	 */
	private static File loadWorkspace()
	{
		String workSpacePath = PropertyManager.getSharedInstance().getProperty(ApplicationProperties.WORKSPACE_DIR);

		SycamoreSystem.workspace = new File(workSpacePath);
		if (!workspace.exists())
		{
			workspace.mkdir();
		}

		String pluginsPath = workSpacePath + System.getProperty("file.separator") + "Plugins";
		String projectsPath = workSpacePath + System.getProperty("file.separator") + "Projects";
		String scriptsPath = workSpacePath + System.getProperty("file.separator") + "Scripts";

		new File(pluginsPath).mkdir();
		new File(projectsPath).mkdir();
		new File(scriptsPath).mkdir();

		return workspace;
	}

	/**
	 * @return the workspace
	 */
	public static File getWorkspace()
	{
		return workspace;
	}

	/**
	 * @param workspace
	 *            the workspace to set
	 */
	public static void setWorkspace(File workspace)
	{
		SycamoreSystem.workspace = workspace;
	}

	/**
	 * @param engine
	 *            the engine to set
	 */
	public static void setEngine(SycamoreEngine engine)
	{
		SycamoreSystem.engine = engine;
	}

	/**
	 * @return the schedulerFrequency
	 */
	public static float getSchedulerFrequency()
	{
		return schedulerFrequency;
	}

	/**
	 * @param schedulerFrequency
	 *            the schedulerFrequency to set
	 */
	public static void setSchedulerFrequency(float schedulerFrequency)
	{
		SycamoreSystem.schedulerFrequency = schedulerFrequency;
	}

	/**
	 * @return the threadSynchronizer
	 */
	public static Object getSchedulerGuiSynchronizer()
	{
		return schedulerGuiSynchronizer;
	}

	/**
	 * @return
	 */
	public static RenderManager getRenderManager()
	{
		return jmeSceneManager.getRenderManager();
	}

	/**
	 * @return the assetManager
	 */
	public static AssetManager getAssetManager()
	{
		return jmeSceneManager.getAssetManager();
	}

	/**
	 * @return
	 */
	public static HashMap<String, String> getSystemCaps()
	{
		return jmeSceneManager.getCaps();
	}

	/**
	 * @return
	 */
	public static JmeContext getContext()
	{
		return jmeSceneManager.getContext();
	}

	/**
	 * @return the loggerLevel
	 */
	public static Level getLoggerLevel()
	{
		return loggerLevel;
	}

	/**
	 * @param loggerLevel
	 *            the loggerLevel to set
	 */
	public static void setLoggerLevel(Level loggerLevel)
	{
		SycamoreSystem.loggerLevel = loggerLevel;
	}

	/**
	 * @return the schedulerThread
	 */
	public static SycamoreSchedulerThread getSchedulerThread()
	{
		return schedulerThread;
	}

	/**
	 * @return the humanPilotSchedulerThread
	 */
	public static SycamoreHumanPilotSchedulerThread getHumanPilotSchedulerThread()
	{
		return humanPilotSchedulerThread;
	}

	/**
	 * @return the visualizerThread
	 */
	public static SycamoreVisualizerThread getVisualizerThread()
	{
		return visualizerThread;
	}

	/**
	 * @param jmeSceneManager
	 *            the jmeSceneManager to set
	 */
	public static void setJmeSceneManager(SycamoreJMEScene jmeSceneManager)
	{
		SycamoreSystem.jmeSceneManager = jmeSceneManager;
	}

	/**
	 * @param callable
	 */
	public static void enqueueToJME(Callable<Object> callable)
	{
		if (jmeSceneManager != null)
		{
			jmeSceneManager.enqueue(callable);
		}
	}

	/**
	 * @param callable
	 */
	public static void enqueueToJMEandWait(Callable<Object> callable)
	{
		if (jmeSceneManager != null)
		{
			Future<Object> result = jmeSceneManager.enqueue(callable);
			while (!result.isDone())
			{
				try
				{
					Thread.sleep(10);
				}
				catch (InterruptedException e)
				{
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * @param mainFrame
	 *            the mainFrame to set
	 */
	public static void setMainFrame(JFrame mainFrame)
	{
		SycamoreSystem.mainFrame = mainFrame;
	}

	/**
	 * @return the mainFrame
	 */
	public static JFrame getMainFrame()
	{
		return mainFrame;
	}

	/**
	 * @param isNKnown
	 *            the isNKnown to set
	 */
	protected static void setNKnown(boolean isNKnown)
	{
		PropertyManager.getSharedInstance().putProperty(ApplicationProperties.N_KNOWN, isNKnown);
	}

	/**
	 * @return the isNKnown
	 */
	protected static boolean isNKnown()
	{
		return PropertyManager.getSharedInstance().getBooleanProperty(ApplicationProperties.N_KNOWN);
	}

	/**
	 * @return the epsilon
	 */
	public static float getEpsilon()
	{
		return PropertyManager.getSharedInstance().getFloatProperty(ApplicationProperties.EPSILON);
	}

	/**
	 * Returns the number of robots in the system, if this value is available. If not available,
	 * throws a <code>NNotKnownException</code>
	 * 
	 * @return
	 * @throws NNotKnownException
	 */
	public static int getN() throws NNotKnownException
	{
		if (isNKnown())
		{
			return engine.getRobots().size();
		}
		else
		{
			throw new NNotKnownException("The value of n is not known");
		}
	}

	/**
	 * @return
	 */
	public static File getPluginsDirectory()
	{
		return new File(getWorkspace().getAbsolutePath() + System.getProperty("file.separator") + "Plugins");
	}

	/**
	 * @return
	 */
	public static File getPluginsResourcesDirectory()
	{
		return new File(getPluginsDirectory() + System.getProperty("file.separator") + "Resources");
	}

	/**
	 * @return the gridVisible
	 */
	public static boolean isGridVisible()
	{
		return PropertyManager.getSharedInstance().getBooleanProperty(ApplicationProperties.GRID_VISIBLE);
	}

	/**
	 * @param gridVisible
	 *            the gridVisible to set
	 */
	public static void setGridVisible(boolean gridVisible)
	{
		PropertyManager.getSharedInstance().putProperty(ApplicationProperties.GRID_VISIBLE, gridVisible);
	}

	/**
	 * @return the axesVisible
	 */
	public static boolean isAxesVisible()
	{
		return PropertyManager.getSharedInstance().getBooleanProperty(ApplicationProperties.AXES_VISIBLE);
	}

	/**
	 * @param axesVisible
	 *            the axesVisible to set
	 */
	public static void setAxesVisible(boolean axesVisible)
	{
		PropertyManager.getSharedInstance().putProperty(ApplicationProperties.AXES_VISIBLE, axesVisible);
	}

	/**
	 * @return the baricentrumVisible
	 */
	public static boolean isBaricentrumVisible()
	{
		return PropertyManager.getSharedInstance().getBooleanProperty(ApplicationProperties.BARICENTRUM_VISIBLE);
	}

	/**
	 * @param baricentrumVisible
	 *            the baricentrumVisible to set
	 */
	public static void setBaricentrumVisible(boolean baricentrumVisible)
	{
		PropertyManager.getSharedInstance().putProperty(ApplicationProperties.BARICENTRUM_VISIBLE, baricentrumVisible);
	}

	/**
	 * @return the visibilityRangesVisible
	 */
	public static boolean isVisibilityRangesVisible()
	{
		return PropertyManager.getSharedInstance().getBooleanProperty(ApplicationProperties.VISIBILITY_RANGES_VISIBLE);
	}

	/**
	 * @param visibilityRangesVisible
	 *            the visibilityRangesVisible to set
	 */
	public static void setVisibilityRangesVisible(boolean visibilityRangesVisible)
	{
		PropertyManager.getSharedInstance().putProperty(ApplicationProperties.VISIBILITY_RANGES_VISIBLE, visibilityRangesVisible);
	}

	/**
	 * @return the movementDirectionsVisible
	 */
	public static boolean isMovementDirectionsVisible()
	{
		return PropertyManager.getSharedInstance().getBooleanProperty(ApplicationProperties.MOVEMENT_DIRECTIONS_VISIBLE);
	}

	/**
	 * @param movementDirectionsVisible
	 *            the movementDirectionsVisible to set
	 */
	public static void setMovementDirectionsVisible(boolean movementDirectionsVisible)
	{
		PropertyManager.getSharedInstance().putProperty(ApplicationProperties.MOVEMENT_DIRECTIONS_VISIBLE, movementDirectionsVisible);
	}

	/**
	 * @return the robotsLightsVisible
	 */
	public static boolean isRobotsLightsVisible()
	{
		return PropertyManager.getSharedInstance().getBooleanProperty(ApplicationProperties.ROBOTS_LIGHTS_VISIBLE);
	}

	/**
	 * @param robotsLightsVisible
	 *            the robotsLightsVisible to set
	 */
	public static void setRobotsLightsVisible(boolean robotsLightsVisible)
	{
		PropertyManager.getSharedInstance().putProperty(ApplicationProperties.ROBOTS_LIGHTS_VISIBLE, robotsLightsVisible);
	}

	/**
	 * @return the visibilityGraphVisible
	 */
	public static boolean isVisibilityGraphVisible()
	{
		return PropertyManager.getSharedInstance().getBooleanProperty(ApplicationProperties.VISIBILITY_GRAPH_VISIBLE);
	}

	/**
	 * @param visibilityGraphVisible
	 *            the visibilityGraphVisible to set
	 */
	public static void setVisibilityGraphVisible(boolean visibilityGraphVisible)
	{
		PropertyManager.getSharedInstance().putProperty(ApplicationProperties.VISIBILITY_GRAPH_VISIBLE, visibilityGraphVisible);
	}

	/**
	 * @return the localCoordinatesVisible
	 */
	public static boolean isLocalCoordinatesVisible()
	{
		return PropertyManager.getSharedInstance().getBooleanProperty(ApplicationProperties.LOCAL_COORDINATE_SYSTEMS_VISIBLE);
	}

	/**
	 * @param localCoordinatesVisible
	 *            the localCoordinatesVisible to set
	 */
	public static void setLocalCoordinatesVisible(boolean localCoordinatesVisible)
	{
		PropertyManager.getSharedInstance().putProperty(ApplicationProperties.LOCAL_COORDINATE_SYSTEMS_VISIBLE, localCoordinatesVisible);
	}

	/**
	 * @return the visualElementsVisible
	 */
	public static boolean isVisualElementsVisible()
	{
		return PropertyManager.getSharedInstance().getBooleanProperty(ApplicationProperties.VISUAL_ELEMENTS_VISIBLE);
	}

	/**
	 * @param visualElementsVisible
	 *            the visualElementsVisible to set
	 */
	public static void setVisualElementsVisible(boolean visualElementsVisible)
	{
		PropertyManager.getSharedInstance().putProperty(ApplicationProperties.VISUAL_ELEMENTS_VISIBLE, visualElementsVisible);
	}

	/**
	 * Reset the system to its initial state
	 */
	public static synchronized void reset(APP_MODE appMode)
	{
		try
		{
			if (appMode == APP_MODE.SIMULATOR)
			{
				// make current scheduler therads return
				schedulerThread.interrupt();
				humanPilotSchedulerThread.interrupt();

				// create new scheduler threads
				schedulerThread = new SycamoreSchedulerThread();
				schedulerThread.start();

				humanPilotSchedulerThread = new SycamoreHumanPilotSchedulerThread();
				humanPilotSchedulerThread.start();
			}
			else
			{
				visualizerThread.interrupt();

				visualizerThread = new SycamoreVisualizerThread();
				visualizerThread.start();
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
