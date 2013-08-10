package it.diunipi.volpi.sycamore.engine;

import it.diunipi.volpi.sycamore.engine.SycamoreEngine.TYPE;
import it.diunipi.volpi.sycamore.gui.PluginComparator;
import it.diunipi.volpi.sycamore.gui.SycamoreSystem;
import it.diunipi.volpi.sycamore.plugins.PluginLoader;
import it.diunipi.volpi.sycamore.plugins.SycamorePlugin;
import it.diunipi.volpi.sycamore.plugins.SycamoreTypedPlugin;
import it.diunipi.volpi.sycamore.plugins.agreements.Agreement;
import it.diunipi.volpi.sycamore.plugins.algorithms.Algorithm;
import it.diunipi.volpi.sycamore.plugins.initialconditions.InitialConditions;
import it.diunipi.volpi.sycamore.plugins.measures.Measure;
import it.diunipi.volpi.sycamore.plugins.memory.Memory;
import it.diunipi.volpi.sycamore.plugins.schedulers.Scheduler;
import it.diunipi.volpi.sycamore.plugins.visibilities.Visibility;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.logging.Level;

/**
 * The plugin manager. It cares about loading the plugins in the workspace and offer them to the
 * objects that need them. There is always just an instance of the plugins manager. It can be
 * accessed everywhere by calling <code>SycamorePluginManager.getSharedInstance()</code> method. The
 * plugins manager can be queried for loaded plugins, or can be asked to clear the plugins list.
 * 
 * @author Valerio Volpi - vale.v@me.com
 */
public class SycamorePluginManager
{
	/**
	 * Compares two typed plugins. 2D plugins come before 3D ones.
	 * 
	 * @author Valerio Volpi - vale.v@me.com
	 * 
	 * @param <A>
	 */
	private class TypedPluginComparator<P extends SycamoreTypedPlugin> implements Comparator<P>
	{

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
		 */
		@Override
		public int compare(P o1, P o2)
		{
			TYPE type1 = o1.getType();
			TYPE type2 = o2.getType();

			if (type1 == type2)
			{
				return new PluginComparator().compare(o1, o2);
			}
			else
			{
				if (type1 == TYPE.TYPE_2D && type2 == TYPE.TYPE_3D)
				{
					return -1;
				}
				else
					return 1;
			}
		}

	}

	private PluginLoader					pluginLoader			= null;
	private static SycamorePluginManager	sharedInstance			= null;

	private ArrayList<Scheduler>			loadedSchedulers		= null;
	private ArrayList<Measure>				loadedMeasures			= null;
	private ArrayList<Algorithm>			loadedAlgorithms		= null;
	private ArrayList<Visibility>			loadedVisibilities		= null;
	private ArrayList<Agreement>			loadedAgreements		= null;
	private ArrayList<InitialConditions>	loadedInitialConditions	= null;
	private ArrayList<SycamorePlugin>		loadedPlugins			= null;
	private ArrayList<Memory>				loadedMemories			= null;

	/**
	 * @return the plugin manager shared instance
	 */
	public static SycamorePluginManager getSharedInstance()
	{
		if (sharedInstance == null)
		{
			sharedInstance = new SycamorePluginManager();
		}
		return sharedInstance;
	}

	/**
	 * Default constructor
	 */
	private SycamorePluginManager()
	{
		loadedSchedulers = new ArrayList<Scheduler>();
		loadedMeasures = new ArrayList<Measure>();
		loadedAlgorithms = new ArrayList<Algorithm>();
		loadedVisibilities = new ArrayList<Visibility>();
		loadedAgreements = new ArrayList<Agreement>();
		loadedInitialConditions = new ArrayList<InitialConditions>();
		loadedPlugins = new ArrayList<SycamorePlugin>();
		loadedMemories = new ArrayList<Memory>();

		this.loadPlugins();
		this.logManagerStarted(System.err);
	}

	/**
	 * Refreshes the loaded plugins
	 */
	public void refreshPlugins()
	{
		this.loadedSchedulers.clear();
		this.loadedMeasures.clear();
		this.loadedAlgorithms.clear();
		this.loadedVisibilities.clear();
		this.loadedAgreements.clear();
		this.loadedInitialConditions.clear();
		this.loadedPlugins.clear();
		this.loadedMemories.clear();

		this.loadPlugins();
		this.logManagerStarted(System.err);
	}

	/**
	 * Loads the plugins
	 */
	private void loadPlugins()
	{
		this.pluginLoader = null;
		this.pluginLoader = new PluginLoader(SycamoreSystem.getPluginsDirectory());

		// load plugins
		this.loadedSchedulers = pluginLoader.loadSchedulers();
		this.loadedMeasures = pluginLoader.loadMeasures();
		this.loadedAlgorithms = pluginLoader.loadAlgorithms();
		this.loadedVisibilities = pluginLoader.loadVisibilities();
		this.loadedAgreements = pluginLoader.loadAgreements();
		this.loadedInitialConditions = pluginLoader.loadInitialConditions();
		this.loadedMemories = pluginLoader.loadMemories();

		// sort lists
		Collections.sort(this.loadedSchedulers, new PluginComparator());
		Collections.sort(this.loadedMeasures, new PluginComparator());
		Collections.sort(this.loadedAlgorithms, new TypedPluginComparator<Algorithm>());
		Collections.sort(this.loadedVisibilities, new TypedPluginComparator<Visibility>());
		Collections.sort(this.loadedAgreements, new TypedPluginComparator<Agreement>());
		Collections.sort(this.loadedInitialConditions, new TypedPluginComparator<InitialConditions>());
		Collections.sort(this.loadedMemories, new TypedPluginComparator<Memory>());

		// fill loaded plugins
		loadedPlugins.addAll(loadedSchedulers);
		loadedPlugins.addAll(loadedAlgorithms);
		loadedPlugins.addAll(loadedMeasures);
		loadedPlugins.addAll(loadedAgreements);
		loadedPlugins.addAll(loadedVisibilities);
		loadedPlugins.addAll(loadedInitialConditions);
		loadedPlugins.addAll(loadedMemories);
	}

	/**
	 * Just writes to System.out that the engine loading is fine and lists the loaded plugins
	 */
	private void logManagerStarted(PrintStream outStream)
	{
		if (SycamoreSystem.getLoggerLevel().intValue() < Level.WARNING.intValue())
		{
			outStream.println("Sycamore plugins manager started");
			outStream.println("Loaded schedulers:");
			for (Scheduler scheduler : loadedSchedulers)
			{
				outStream.println("\t" + scheduler.toString());
			}
			outStream.println();
			outStream.println("Loaded measures:");
			for (Measure measure : loadedMeasures)
			{
				outStream.println("\t" + measure.toString());
			}
			outStream.println();
			outStream.println("Loaded algorithms:");
			for (Algorithm algorithm : loadedAlgorithms)
			{
				outStream.println("\t" + algorithm.toString());
			}
			outStream.println();
			outStream.println("Loaded visibilities:");
			for (Visibility visibility : loadedVisibilities)
			{
				outStream.println("\t" + visibility.toString());
			}
			outStream.println();
			outStream.println("Loaded agreements:");
			for (Agreement agreement : loadedAgreements)
			{
				outStream.println("\t" + agreement.toString());
			}
			outStream.println();
			outStream.println("Loaded initial conditions:");
			for (InitialConditions initialCondition : loadedInitialConditions)
			{
				outStream.println("\t" + initialCondition.toString());
			}
			outStream.println();
			outStream.println("Loaded memories:");
			for (Memory memory : loadedMemories)
			{
				outStream.println("\t" + memory.toString());
			}
			outStream.println();
		}
	}

	/**
	 * @return the loadedSchedulers
	 */
	public ArrayList<Scheduler> getLoadedSchedulers()
	{
		return loadedSchedulers;
	}

	/**
	 * @return the loadedMeasures
	 */
	public ArrayList<Measure> getLoadedMeasures()
	{
		return loadedMeasures;
	}

	/**
	 * @return the loadedAlgorithms
	 */
	public ArrayList<Algorithm> getLoadedAlgorithms()
	{
		return loadedAlgorithms;
	}

	/**
	 * @return the loadedVisibilities
	 */
	public ArrayList<Visibility> getLoadedVisibilities()
	{
		return loadedVisibilities;
	}

	/**
	 * @return the loadedAgreements
	 */
	public ArrayList<Agreement> getLoadedAgreements()
	{
		return loadedAgreements;
	}

	/**
	 * @return the loadedInitialConditions
	 */
	public ArrayList<InitialConditions> getLoadedInitialConditions()
	{
		return loadedInitialConditions;
	}

	/**
	 * @return the loadedPlugins
	 */
	public ArrayList<SycamorePlugin> getLoadedPlugins()
	{
		return loadedPlugins;
	}
	
	/**
	 * @return the loadedMemories
	 */
	public ArrayList<Memory> getLoadedMemories()
	{
		return loadedMemories;
	}
}
