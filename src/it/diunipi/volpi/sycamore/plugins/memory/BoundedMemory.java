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
 * This plugin represents the bounded memory. This memory has a fixed size that can be chosen by the
 * user. Such size applies both to the number of positions occupied by the robot in the past and to
 * the number of stored past snapshots.
 * 
 * @author Valerio Volpi - vale.v@me.com
 */
@PluginImplementation
public abstract class BoundedMemory<P extends SycamoreAbstractPoint & ComputablePoint<P>> extends MemoryImpl<P>
{
	/**
	 * Properties related to the bounded memory
	 * 
	 * @author Valerio Volpi - vale.v@me.com
	 */
	private enum BoundedMemoryProperties implements SycamoreProperty
	{
		BOUNDED_MEMORY_SIZE("Memory size", "" + 10);

		private String	description		= null;
		private String	defaultValue	= null;

		/**
		 * Constructor.
		 */
		BoundedMemoryProperties(String description, String defaultValue)
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

	private static BoundedMemorySettingsPanel	panel_settings	= null;

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.diunipi.volpi.sycamore.plugins.memory.Memory#getMemoryLimit()
	 */
	@Override
	public int getSelfPositionsLimit()
	{
		return getMemorySize();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.diunipi.volpi.sycamore.plugins.memory.Memory#getSnapshotsLimit()
	 */
	@Override
	public int getSnapshotsLimit()
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
		return "Bounded memory. Remembers a finite number of steps in the past.";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.diunipi.volpi.sycamore.plugins.SycamorePlugin#getPluginLongDescription()
	 */
	@Override
	public String getPluginLongDescription()
	{
		return "This plugin represents the bounded memory. This memory has a fixed size that can be chosen by the user. " +
				"Such size applies both to the number of positions occupied by the robot in the past and to the number of " +
				"stored past snapshots.";
	}

	/**
	 * @return the memorySize
	 */
	protected static int getMemorySize()
	{
		return PropertyManager.getSharedInstance().getIntegerProperty(BoundedMemoryProperties.BOUNDED_MEMORY_SIZE);
	}

	/**
	 * @param memorySize
	 *            the memorySize to set
	 */
	protected static void setMemorySize(int memorySize)
	{
		PropertyManager.getSharedInstance().putProperty(BoundedMemoryProperties.BOUNDED_MEMORY_SIZE, memorySize);
	}

}
