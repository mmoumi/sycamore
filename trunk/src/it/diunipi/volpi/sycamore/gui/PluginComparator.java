/**
 * 
 */
package it.diunipi.volpi.sycamore.gui;

import it.diunipi.volpi.sycamore.plugins.SycamorePlugin;

import java.util.Comparator;

/**
 * A simple comparator that sorts plugins by their names, in a lexicographical order.
 * 
 * @author Valerio Volpi - vale.v@me.com
 */
public class PluginComparator implements Comparator<SycamorePlugin>
{
	/* (non-Javadoc)
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	@Override
	public int compare(SycamorePlugin o1, SycamorePlugin o2)
	{
		return o1.getPluginName().compareTo(o2.getPluginName());
	}
}
