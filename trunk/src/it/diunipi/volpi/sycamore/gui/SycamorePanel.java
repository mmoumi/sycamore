/**
 * 
 */
package it.diunipi.volpi.sycamore.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.JPanel;

import it.diunipi.volpi.sycamore.engine.SycamoreEngine;

/**
 * The base panel for all the Sycamore Gui elements
 * 
 * @author Valerio Volpi - vale.v@me.com
 */
public abstract class SycamorePanel extends JPanel
{
	private static final long		serialVersionUID	= -353494685389658659L;
	private Vector<ActionListener>	listeners			= null;

	protected boolean				changeLock			= false;

	/**
	 * Adds an <code>ActionListener</code> to the button.
	 * 
	 * @param listener
	 *            the <code>ActionListener</code> to be added
	 */
	public void addActionListener(ActionListener listener)
	{
		if (this.listeners == null)
		{
			this.listeners = new Vector<ActionListener>();
		}
		this.listeners.add(listener);
	}

	/**
	 * Removes an <code>ActionListener</code> from the button. If the listener is the currently set
	 * <code>Action</code> for the button, then the <code>Action</code> is set to <code>null</code>.
	 * 
	 * @param listener
	 *            the listener to be removed
	 */
	public void removeActionListener(ActionListener listener)
	{
		if (this.listeners == null)
		{
			this.listeners = new Vector<ActionListener>();
		}
		this.listeners.remove(listener);
	}

	/**
	 * Fires passed ActionEvent to all registered listeners, by calling <code>ActionPerformed</code>
	 * method on all of them.
	 * 
	 * @param e
	 */
	protected void fireActionEvent(ActionEvent e)
	{
		if (this.listeners != null)
		{
			for (ActionListener listener : this.listeners)
			{
				listener.actionPerformed(e);
			}
		}
	}

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

	/**
	 * Reset this panel to its initial settings
	 */
	public abstract void reset();
}
