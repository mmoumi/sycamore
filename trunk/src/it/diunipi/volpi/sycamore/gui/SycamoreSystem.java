/**
 * 
 */
package it.diunipi.volpi.sycamore.gui;

import it.diunipi.volpi.sycamore.engine.NNotKnownException;
import it.diunipi.volpi.sycamore.engine.SycamoreEngine;
import it.diunipi.volpi.sycamore.jmescene.SycamoreJMEScene;
import it.diunipi.volpi.sycamore.plugins.agreements.Agreement;
import it.diunipi.volpi.sycamore.plugins.humanpilot.SycamoreHumanPilotSchedulerThread;
import it.diunipi.volpi.sycamore.plugins.initialconditions.InitialConditions;
import it.diunipi.volpi.sycamore.plugins.memory.Memory;
import it.diunipi.volpi.sycamore.plugins.schedulers.SycamoreSchedulerThread;
import it.diunipi.volpi.sycamore.plugins.visibilities.Visibility;
import it.diunipi.volpi.sycamore.util.ApplicationProperties;
import it.diunipi.volpi.sycamore.util.PropertyManager;

import java.io.File;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JFrame;

import com.jme3.asset.AssetManager;
import com.jme3.renderer.RenderManager;

/**
 * This class represents the system and contains all data that is accessible everywhere
 * 
 * @author Valerio Volpi - vale.v@me.com
 */
public class SycamoreSystem
{
	private static final Object							schedulerGuiSynchronizer	= new Object();
	public static final float							DEFAULT_ROBOT_SPEED			= 0.1f;
	public static float									schedulerFrequency			= 1.0f / 640.0f;

	private static File									workspace					= null;
	private static Level								loggerLevel					= Level.SEVERE;

	private static SycamoreEngine						engine						= null;
	private static SycamoreSchedulerThread				schedulerThread				= null;
	private static SycamoreHumanPilotSchedulerThread	humanPilotSchedulerThread	= null;
	private static SycamoreJMEScene						jmeSceneManager				= null;
	private static JFrame								mainFrame					= null;

	private static final float							epsilon						= 0.001f;

	private static Visibility							visibility					= null;
	private static Agreement							agreement					= null;
	private static InitialConditions					initialCondition			= null;
	private static Memory								memory						= null;

	/**
	 * Initialize the system
	 */
	public static void initialize()
	{
		loadWorkspace();

		schedulerThread = new SycamoreSchedulerThread();
		schedulerThread.start();

		humanPilotSchedulerThread = new SycamoreHumanPilotSchedulerThread();
		humanPilotSchedulerThread.start();

		Logger.getLogger("").setLevel(loggerLevel);
	}

	/**
	 * Load the workspace file. If it does not exists, creates the directory
	 * 
	 * @return
	 */
	private static File loadWorkspace()
	{
		String workSpacePath = PropertyManager.getSharedInstance().getProperty(ApplicationProperties.WORKSPACE_DIR.name());

		SycamoreSystem.workspace = new File(workSpacePath);
		if (!workspace.exists())
		{
			workspace.mkdir();
		}

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
		PropertyManager.getSharedInstance().putProperty(ApplicationProperties.N_KNOWN.name(), isNKnown);
	}

	/**
	 * @return the isNKnown
	 */
	protected static boolean isNKnown()
	{
		return PropertyManager.getSharedInstance().getBooleanProperty(ApplicationProperties.N_KNOWN.name());
	}

	/**
	 * @return the epsilon
	 */
	public static float getEpsilon()
	{
		return epsilon;
	}

	/**
	 * @param visibility
	 *            the visibility to set
	 */
	public static void setVisibility(Visibility visibility)
	{
		SycamoreSystem.visibility = visibility;
	}

	/**
	 * @return
	 */
	public static Visibility getVisibility()
	{
		return SycamoreSystem.visibility;
	}

	/**
	 * @param agreement
	 *            the agreement to set
	 */
	public static void setAgreement(Agreement agreement)
	{
		SycamoreSystem.agreement = agreement;
	}

	/**
	 * @return the agreement
	 */
	public static Agreement getAgreement()
	{
		return agreement;
	}

	/**
	 * @param initialCondition
	 *            the initialCondition to set
	 */
	public static void setInitialCondition(InitialConditions initialCondition)
	{
		SycamoreSystem.initialCondition = initialCondition;
	}

	/**
	 * @return the initialCondition
	 */
	public static InitialConditions getInitialCondition()
	{
		return initialCondition;
	}

	/**
	 * @param memory
	 *            the memory to set
	 */
	public static void setMemory(Memory memory)
	{
		SycamoreSystem.memory = memory;
	}

	/**
	 * @return the memory
	 */
	public static Memory getMemory()
	{
		return SycamoreSystem.memory;
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
		return PropertyManager.getSharedInstance().getBooleanProperty(ApplicationProperties.GRID_VISIBLE.name());
	}

	/**
	 * @param gridVisible
	 *            the gridVisible to set
	 */
	public static void setGridVisible(boolean gridVisible)
	{
		PropertyManager.getSharedInstance().putProperty(ApplicationProperties.GRID_VISIBLE.name(), gridVisible);
	}

	/**
	 * @return the axesVisible
	 */
	public static boolean isAxesVisible()
	{
		return PropertyManager.getSharedInstance().getBooleanProperty(ApplicationProperties.AXES_VISIBLE.name());
	}

	/**
	 * @param axesVisible
	 *            the axesVisible to set
	 */
	public static void setAxesVisible(boolean axesVisible)
	{
		PropertyManager.getSharedInstance().putProperty(ApplicationProperties.AXES_VISIBLE.name(), axesVisible);
	}

	/**
	 * @return the baricentrumVisible
	 */
	public static boolean isBaricentrumVisible()
	{
		return PropertyManager.getSharedInstance().getBooleanProperty(ApplicationProperties.BARICENTRUM_VISIBLE.name());
	}

	/**
	 * @param baricentrumVisible
	 *            the baricentrumVisible to set
	 */
	public static void setBaricentrumVisible(boolean baricentrumVisible)
	{
		PropertyManager.getSharedInstance().putProperty(ApplicationProperties.BARICENTRUM_VISIBLE.name(), baricentrumVisible);
	}

	/**
	 * @return the visibilityRangesVisible
	 */
	public static boolean isVisibilityRangesVisible()
	{
		return PropertyManager.getSharedInstance().getBooleanProperty(ApplicationProperties.VISIBILITY_RANGES_VISIBLE.name());
	}

	/**
	 * @param visibilityRangesVisible
	 *            the visibilityRangesVisible to set
	 */
	public static void setVisibilityRangesVisible(boolean visibilityRangesVisible)
	{
		PropertyManager.getSharedInstance().putProperty(ApplicationProperties.VISIBILITY_RANGES_VISIBLE.name(), visibilityRangesVisible);
	}

	/**
	 * @return the movementDirectionsVisible
	 */
	public static boolean isMovementDirectionsVisible()
	{
		return PropertyManager.getSharedInstance().getBooleanProperty(ApplicationProperties.MOVEMENT_DIRECTIONS_VISIBLE.name());
	}

	/**
	 * @param movementDirectionsVisible
	 *            the movementDirectionsVisible to set
	 */
	public static void setMovementDirectionsVisible(boolean movementDirectionsVisible)
	{
		PropertyManager.getSharedInstance().putProperty(ApplicationProperties.MOVEMENT_DIRECTIONS_VISIBLE.name(), movementDirectionsVisible);
	}

	/**
	 * @return the robotsLightsVisible
	 */
	public static boolean isRobotsLightsVisible()
	{
		return PropertyManager.getSharedInstance().getBooleanProperty(ApplicationProperties.ROBOTS_LIGHTS_VISIBLE.name());
	}

	/**
	 * @param robotsLightsVisible
	 *            the robotsLightsVisible to set
	 */
	public static void setRobotsLightsVisible(boolean robotsLightsVisible)
	{
		PropertyManager.getSharedInstance().putProperty(ApplicationProperties.ROBOTS_LIGHTS_VISIBLE.name(), robotsLightsVisible);
	}

	/**
	 * @return the visibilityGraphVisible
	 */
	public static boolean isVisibilityGraphVisible()
	{
		return PropertyManager.getSharedInstance().getBooleanProperty(ApplicationProperties.VISIBILITY_GRAPH_VISIBLE.name());
	}

	/**
	 * @param visibilityGraphVisible
	 *            the visibilityGraphVisible to set
	 */
	public static void setVisibilityGraphVisible(boolean visibilityGraphVisible)
	{
		PropertyManager.getSharedInstance().putProperty(ApplicationProperties.VISIBILITY_GRAPH_VISIBLE.name(), visibilityGraphVisible);
	}

	/**
	 * @return the localCoordinatesVisible
	 */
	public static boolean isLocalCoordinatesVisible()
	{
		return PropertyManager.getSharedInstance().getBooleanProperty(ApplicationProperties.LOCAL_COORDINATE_SYSTEMS_VISIBLE.name());
	}

	/**
	 * @param localCoordinatesVisible
	 *            the localCoordinatesVisible to set
	 */
	public static void setLocalCoordinatesVisible(boolean localCoordinatesVisible)
	{
		PropertyManager.getSharedInstance().putProperty(ApplicationProperties.LOCAL_COORDINATE_SYSTEMS_VISIBLE.name(), localCoordinatesVisible);
	}

	/**
	 * @return the visualElementsVisible
	 */
	public static boolean isVisualElementsVisible()
	{
		return PropertyManager.getSharedInstance().getBooleanProperty(ApplicationProperties.VISUAL_ELEMENTS_VISIBLE.name());
	}

	/**
	 * @param visualElementsVisible
	 *            the visualElementsVisible to set
	 */
	public static void setVisualElementsVisible(boolean visualElementsVisible)
	{
		PropertyManager.getSharedInstance().putProperty(ApplicationProperties.VISUAL_ELEMENTS_VISIBLE.name(), visualElementsVisible);
	}

	/**
	 * Reset the system to its initial state
	 */
	public static synchronized void reset()
	{
		try
		{
			// make current scheduler therads return
			schedulerThread.interrupt();
			humanPilotSchedulerThread.interrupt();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		// create new scheduler threads
		schedulerThread = new SycamoreSchedulerThread();
		schedulerThread.start();

		humanPilotSchedulerThread = new SycamoreHumanPilotSchedulerThread();
		humanPilotSchedulerThread.start();
	}
}
