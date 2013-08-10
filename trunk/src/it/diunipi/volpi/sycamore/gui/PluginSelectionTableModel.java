/**
 * 
 */
package it.diunipi.volpi.sycamore.gui;

import it.diunipi.volpi.sycamore.plugins.SycamorePlugin;

import java.util.ArrayList;

import javax.swing.table.DefaultTableModel;

/**
 * The model for tables for the selection of plugins
 * 
 * @author Valerio Volpi - vale.v@me.com
 *
 * @param <P>
 */
public class PluginSelectionTableModel<P extends SycamorePlugin> extends DefaultTableModel
{
	private static final long	serialVersionUID	= 1972237612787395876L;
	private ArrayList<P>		plugins				= null;

	/**
	 * Default constructor.
	 */
	public PluginSelectionTableModel(ArrayList<P> plugins)
	{
		this.plugins = plugins;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.table.TableModel#getRowCount()
	 */
	@Override
	public int getRowCount()
	{
		if (this.plugins == null)
		{
			return 0;
		}
		else
		{
			return this.plugins.size();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.table.TableModel#getColumnCount()
	 */
	@Override
	public int getColumnCount()
	{
		return 1;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.table.TableModel#getColumnName(int)
	 */
	@Override
	public String getColumnName(int columnIndex)
	{
		return "Measure";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.table.TableModel#isCellEditable(int, int)
	 */
	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex)
	{
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.table.TableModel#getValueAt(int, int)
	 */
	@Override
	public Object getValueAt(int rowIndex, int columnIndex)
	{
		if (plugins != null)
		{
			return plugins.get(rowIndex);
		}
		else
		{
			return null;
		}
	}
}
