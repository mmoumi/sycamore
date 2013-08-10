package it.diunipi.volpi.sycamore.plugins;

import it.diunipi.volpi.sycamore.gui.SycamorePanel;

import net.xeoh.plugins.base.Plugin;

/**
 * This interface represents a generic plugin in Sycamore
 * 
 * @author Valerio Volpi - volpiv@cli.di.unipi.it
 */
public interface SycamorePlugin extends Plugin
{
	/**
	 * @return the author
	 */
	public String getAuthor();
	
	/**
	 * @return the name of the plugin
	 */
	public String getPluginName();
	
	/**
	 * @return the short description of the plugin
	 */
	public String getPluginShortDescription();
	
	/**
	 * @return the long description of the plugin
	 */
	public String getPluginLongDescription();
	
	/**
	 * @return the long description of the generic class (or family) of the plugin
	 */
	public String getPluginClassLongDescription();
	
	/**
	 * @return the short description of the generic class (or family) of the plugin
	 */
	public String getPluginClassShortDescription();
	
	/**
	 * @return the description of the generic class (or family) of the plugin
	 */
	public String getPluginClassDescription();
	
	/**
	 * @return the settings panel for the plugin
	 */
	public SycamorePanel getPanel_settings();
	
	/**
	 * @return a String representation of the plugin
	 */
	public String toString();
}
