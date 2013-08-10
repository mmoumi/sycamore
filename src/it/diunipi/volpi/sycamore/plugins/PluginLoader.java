package it.diunipi.volpi.sycamore.plugins;

import it.diunipi.volpi.sycamore.plugins.agreements.Agreement;
import it.diunipi.volpi.sycamore.plugins.algorithms.Algorithm;
import it.diunipi.volpi.sycamore.plugins.initialconditions.InitialConditions;
import it.diunipi.volpi.sycamore.plugins.measures.Measure;
import it.diunipi.volpi.sycamore.plugins.memory.Memory;
import it.diunipi.volpi.sycamore.plugins.schedulers.Scheduler;
import it.diunipi.volpi.sycamore.plugins.visibilities.Visibility;

import java.io.File;
import java.util.ArrayList;
import net.xeoh.plugins.base.PluginManager;
import net.xeoh.plugins.base.impl.PluginManagerFactory;
import net.xeoh.plugins.base.util.PluginManagerUtil;

/**
 * This class offers methods to load the plugins from hard drive
 * 
 * @author Valerio Volpi - volpiv@cli.di.unipi.it
 */
public class PluginLoader
{
	protected PluginManagerUtil	pluginManagerUtil;

	/**
	 * Default constructor
	 */
	public PluginLoader(File pluginsDirectory)
	{
		if (pluginsDirectory != null)
		{
			if (!pluginsDirectory.exists())
			{
				pluginsDirectory.mkdir();
			}

			PluginManager pm = PluginManagerFactory.createPluginManager();
			pm.addPluginsFrom(pluginsDirectory.toURI());

			this.pluginManagerUtil = new PluginManagerUtil(pm);
		}
	}

	/**
	 * Loads the schedulers from pluginsPath and returns the loaded vector
	 * 
	 * @return
	 */
	public ArrayList<Scheduler> loadSchedulers()
	{
		return (ArrayList<Scheduler>) pluginManagerUtil.getPlugins(Scheduler.class);
	}

	/**
	 * Loads the measures from pluginsPath and returns the loaded vector
	 * 
	 * @return
	 */
	public ArrayList<Measure> loadMeasures()
	{
		return (ArrayList<Measure>) pluginManagerUtil.getPlugins(Measure.class);
	}

	/**
	 * Loads the algorithms from pluginsPath and returns the loaded vector
	 * 
	 * @return
	 */
	public ArrayList<Algorithm> loadAlgorithms()
	{
		return (ArrayList<Algorithm>) pluginManagerUtil.getPlugins(Algorithm.class);
	}

	/**
	 * Loads the visibilities from pluginsPath and returns the loaded vector
	 * 
	 * @return
	 */
	public ArrayList<Visibility> loadVisibilities()
	{
		return (ArrayList<Visibility>) pluginManagerUtil.getPlugins(Visibility.class);
	}

	/**
	 * Loads the agreements from pluginsPath and returns the loaded vector
	 * 
	 * @return
	 */
	public ArrayList<Agreement> loadAgreements()
	{
		return (ArrayList<Agreement>) pluginManagerUtil.getPlugins(Agreement.class);
	}

	/**
	 * Loads the initial conditions from pluginsPath and returns the loaded vector
	 * 
	 * @return
	 */
	public ArrayList<InitialConditions> loadInitialConditions()
	{
		return (ArrayList<InitialConditions>) pluginManagerUtil.getPlugins(InitialConditions.class);
	}

	/**
	 * Loads the memories from pluginsPath and returns the loaded vector
	 * 
	 * @return
	 */
	public ArrayList<Memory> loadMemories()
	{
		return (ArrayList<Memory>) pluginManagerUtil.getPlugins(Memory.class);
	}
}
