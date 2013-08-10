/**
 * 
 */
package it.diunipi.volpi.sycamore.gui;

import it.diunipi.volpi.sycamore.engine.SycamoreEngine;
import it.diunipi.volpi.sycamore.jmescene.SycamoreJMEScene;
import it.diunipi.volpi.sycamore.model.NNotKnownException;
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

/**
 * This class represents the system and contains all data that is accessible everywhere
 * 
 * @author Valerio Volpi - vale.v@me.com
 */
public class SycamoreSystem
{
	private static final Object							schedulerGuiSynchronizer	= new Object();
	public static final float							DEFAULT_ROBOT_SPEED			= 0.1f;
	public static float									schedulerFrequency			= 1.0f / 120.0f;

	private static File									workspace					= null;
	private static AssetManager							assetManager				= null;
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
		String workSpacePath = PropertyManager.getSharedInstance().getProperty(ApplicationProperties.WORKSPACE_DIR.getName());

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
	 * @param assetManager
	 *            the assetManager to set
	 */
	public static void setAssetManager(AssetManager assetManager)
	{
		SycamoreSystem.assetManager = assetManager;
	}

	/**
	 * @return the assetManager
	 */
	public static AssetManager getAssetManager()
	{
		return assetManager;
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
		jmeSceneManager.enqueue(callable);
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
		PropertyManager.getSharedInstance().putProperty(ApplicationProperties.N_KNOWN.getName(), isNKnown);
	}

	/**
	 * @return the isNKnown
	 */
	protected static boolean isNKnown()
	{
		return PropertyManager.getSharedInstance().getBooleanProperty(ApplicationProperties.N_KNOWN.getName());
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
}
