/**
 * 
 */
package it.diunipi.volpi.sycamore.gui;

import javax.swing.JPanel;

import it.diunipi.volpi.sycamore.engine.SycamoreEngine;

/**
 * The base panel for all the Sycamore Gui elements
 * 
 * @author Valerio Volpi - vale.v@me.com
 */
public abstract class SycamorePanel extends JPanel
{
	private static final long	serialVersionUID	= -353494685389658659L;

	/**
	 * Changes the current app engine replacing it with passed one.
	 * 
	 * @param appEngine
	 */
	public abstract void setAppEngine(SycamoreEngine appEngine);

	/**
	 * Updates the GUI in order to reflect changes in the engine
	 */
	public abstract void updateGui();
}
