package it.diunipi.volpi.sycamore.gui;

import it.diunipi.volpi.sycamore.plugins.SycamorePlugin;

import java.util.ArrayList;

import javax.swing.DefaultComboBoxModel;

/**
 * The model for combo boxes for the selection of plugins
 * 
 * @author Valerio Volpi - vale.v@me.com
 * 
 * @param <P>
 */
public class PluginSelectionComboboxModel<P extends SycamorePlugin> extends DefaultComboBoxModel
{
	private static final long	serialVersionUID	= -3935503352403674387L;
	private ArrayList<P>		plugins				= null;

	/**
	 * Default constructor.
	 */
	public PluginSelectionComboboxModel(ArrayList<P> plugins)
	{
		this.plugins = plugins;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.DefaultComboBoxModel#getSize()
	 */
	@Override
	public int getSize()
	{
		return this.plugins.size() + 1;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.DefaultComboBoxModel#getElementAt(int)
	 */
	@Override
	public Object getElementAt(int index)
	{
		if (index == 0)
		{
			return null;
		}
		else
		{
			return this.plugins.get(index - 1);
		}
	}
}
