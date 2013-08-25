/**
 * 
 */
package it.diunipi.volpi.sycamore.plugins.memory;

import it.diunipi.volpi.sycamore.engine.ComputablePoint;
import it.diunipi.volpi.sycamore.engine.SycamoreAbstractPoint;
import it.diunipi.volpi.sycamore.gui.SycamorePanel;
import it.diunipi.volpi.sycamore.util.PropertyManager;
import it.diunipi.volpi.sycamore.util.SycamoreProperty;
import net.xeoh.plugins.base.annotations.PluginImplementation;

/**
 * @author Vale
 * 
 */
@PluginImplementation
public abstract class BoundedMemory<P extends SycamoreAbstractPoint & ComputablePoint<P>> extends MemoryImpl<P>
{
	private enum BoundedMemoryProperties implements SycamoreProperty
	{
		MEMORY_SIZE("Memory size", "" + 10);

		private String	description			= null;
		private String	defaultValue	= null;

		/**
		 * 
		 */
		BoundedMemoryProperties(String description, String defaultValue)
		{
			this.description = description;
			this.defaultValue = defaultValue;
		}

		/* (non-Javadoc)
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

	private static BoundedMemorySettingsPanel	panel_settings	= null;

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.diunipi.volpi.sycamore.plugins.memory.Memory#getMemoryLimit()
	 */
	@Override
	public int getMemoryLimit()
	{
		return getMemorySize();
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
			panel_settings = new BoundedMemorySettingsPanel();
		}
		return panel_settings;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.diunipi.volpi.sycamore.plugins.SycamorePlugin#getPluginName()
	 */
	@Override
	public String getPluginName()
	{
		return this.getClass().getSimpleName();
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
		return "Tiny memory. Remembers just one past snapshot";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.diunipi.volpi.sycamore.plugins.SycamorePlugin#getPluginLongDescription()
	 */
	@Override
	public String getPluginLongDescription()
	{
		return "Tiny memory that remembers just one step in the past. Remembers both the last snapshot and the last self-observation.";
	}

	/**
	 * @return the memorySize
	 */
	protected static int getMemorySize()
	{
		int size = PropertyManager.getSharedInstance().getIntegerProperty(BoundedMemoryProperties.MEMORY_SIZE.name());
		if (size < 0)
		{
			size = Integer.parseInt(BoundedMemoryProperties.MEMORY_SIZE.getDefaultValue());
		}
		
		return size;
	}

	/**
	 * @param memorySize
	 *            the memorySize to set
	 */
	protected static void setMemorySize(int memorySize)
	{
		PropertyManager.getSharedInstance().putProperty(BoundedMemoryProperties.MEMORY_SIZE.name(), memorySize);
	}

}
