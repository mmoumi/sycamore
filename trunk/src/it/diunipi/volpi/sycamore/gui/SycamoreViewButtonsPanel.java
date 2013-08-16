package it.diunipi.volpi.sycamore.gui;

import it.diunipi.volpi.sycamore.engine.SycamoreEngine;
import it.diunipi.volpi.sycamore.util.SycamoreFiredActionEvents;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JToggleButton;

/**
 * A panel that contains some toggle button to show/hide some elements in the JME scene
 * 
 * @author Valerio Volpi - vale.v@me.com
 */
public class SycamoreViewButtonsPanel extends SycamorePanel
{
	private static final long		serialVersionUID	= 1781889055489085712L;
	private JToggleButton			button_range		= null;
	private JToggleButton			button_graph		= null;
	private JToggleButton			button_dirs			= null;
	private JToggleButton			button_coords		= null;
	private JToggleButton			button_baricentrum	= null;
	private JToggleButton			button_lights		= null;
	private JToggleButton			button_visuals		= null;
	private JToggleButton			button_grid			= null;
	private JToggleButton			button_axes			= null;

	private Vector<ActionListener>	listeners			= null;

	/**
	 * Default constructor
	 */
	public SycamoreViewButtonsPanel()
	{
		this.listeners = new Vector<ActionListener>();
		initialize();
	}

	/**
	 * Init the GUI
	 */
	private void initialize()
	{
		FlowLayout flowLayout = new FlowLayout(FlowLayout.RIGHT, 5, 5);
		setLayout(flowLayout);
		add(getButton_axes());
		add(getButton_grid());
		add(getButton_range());
		add(getButton_graph());
		add(getButton_dirs());
		add(getButton_coords());
		add(getButton_baricentrum());
		add(getButton_lights());
		add(getButton_visuals());
	}

	/**
	 * Adds an <code>ActionListener</code> to the button.
	 * 
	 * @param listener
	 *            the <code>ActionListener</code> to be added
	 */
	public void addActionListener(java.awt.event.ActionListener listener)
	{
		this.listeners.add(listener);
	}

	/**
	 * Removes an <code>ActionListener</code> from the button. If the listener is the currently set
	 * <code>Action</code> for the button, then the <code>Action</code> is set to <code>null</code>.
	 * 
	 * @param listener
	 *            the listener to be removed
	 */
	public void removeActionListener(java.awt.event.ActionListener listener)
	{
		this.listeners.remove(listener);
	}

	/**
	 * Fires passed ActionEvent to all registered listeners, by calling <code>ActionPerformed</code>
	 * method on all of them.
	 * 
	 * @param e
	 */
	private void fireActionEvent(ActionEvent e)
	{
		for (java.awt.event.ActionListener listener : this.listeners)
		{
			listener.actionPerformed(e);
		}
	}

	/**
	 * @return the button_range
	 */
	private JToggleButton getButton_range()
	{
		if (button_range == null)
		{
			button_range = new JToggleButton();
			button_range.setIcon(new ImageIcon(getClass().getResource("/it/diunipi/volpi/sycamore/resources/radar_32x32.png")));
			button_range.setToolTipText("View visibility ranges");
			button_range.setMinimumSize(new Dimension(45, 45));
			button_range.setMaximumSize(new Dimension(45, 45));
			button_range.setPreferredSize(new Dimension(45, 45));
			button_range.setSelected(SycamoreSystem.isVisibilityRangesVisible());
			button_range.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent e)
				{
					fireActionEvent(new ActionEvent(button_range, 0, SycamoreFiredActionEvents.SHOW_VISIBILITY_RANGE.name()));
				}
			});
		}
		return button_range;
	}

	/**
	 * @return the button_graph
	 */
	private JToggleButton getButton_graph()
	{
		if (button_graph == null)
		{
			button_graph = new JToggleButton();
			button_graph.setIcon(new ImageIcon(getClass().getResource("/it/diunipi/volpi/sycamore/resources/graph_32x32.png")));
			button_graph.setToolTipText("View the visibility graph");
			button_graph.setMinimumSize(new Dimension(45, 45));
			button_graph.setMaximumSize(new Dimension(45, 45));
			button_graph.setPreferredSize(new Dimension(45, 45));
			button_graph.setSelected(SycamoreSystem.isVisibilityGraphVisible());
			button_graph.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent e)
				{
					fireActionEvent(new ActionEvent(button_graph, 0, SycamoreFiredActionEvents.SHOW_VISIBILITY_GRAPH.name()));
				}
			});
		}
		return button_graph;
	}

	/**
	 * @return the button_dirs
	 */
	private JToggleButton getButton_dirs()
	{
		if (button_dirs == null)
		{
			button_dirs = new JToggleButton();
			button_dirs.setIcon(new ImageIcon(getClass().getResource("/it/diunipi/volpi/sycamore/resources/directions_32x32.png")));
			button_dirs.setToolTipText("View movement directions");
			button_dirs.setMinimumSize(new Dimension(45, 45));
			button_dirs.setMaximumSize(new Dimension(45, 45));
			button_dirs.setPreferredSize(new Dimension(45, 45));
			button_dirs.setSelected(SycamoreSystem.isMovementDirectionsVisible());
			button_dirs.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent e)
				{
					fireActionEvent(new ActionEvent(button_dirs, 0, SycamoreFiredActionEvents.SHOW_MOVEMENT_DIRECTIONS.name()));
				}
			});
		}
		return button_dirs;
	}

	/**
	 * @return the button_coords
	 */
	private JToggleButton getButton_coords()
	{
		if (button_coords == null)
		{
			button_coords = new JToggleButton();
			button_coords.setIcon(new ImageIcon(getClass().getResource("/it/diunipi/volpi/sycamore/resources/compass_32x32.png")));
			button_coords.setToolTipText("View local coordinate systems");
			button_coords.setMinimumSize(new Dimension(45, 45));
			button_coords.setMaximumSize(new Dimension(45, 45));
			button_coords.setPreferredSize(new Dimension(45, 45));
			button_coords.setSelected(SycamoreSystem.isLocalCoordinatesVisible());
			button_coords.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent e)
				{
					fireActionEvent(new ActionEvent(button_coords, 0, SycamoreFiredActionEvents.SHOW_LOCAL_COORDINATES.name()));
				}
			});
		}
		return button_coords;
	}

	/**
	 * @return the button_camera
	 */
	private JToggleButton getButton_baricentrum()
	{
		if (button_baricentrum == null)
		{
			button_baricentrum = new JToggleButton();
			button_baricentrum.setIcon(new ImageIcon(getClass().getResource("/it/diunipi/volpi/sycamore/resources/plus_32x32.png")));
			button_baricentrum.setToolTipText("View system baricentrum");
			button_baricentrum.setMinimumSize(new Dimension(45, 45));
			button_baricentrum.setMaximumSize(new Dimension(45, 45));
			button_baricentrum.setPreferredSize(new Dimension(45, 45));
			button_baricentrum.setSelected(SycamoreSystem.isBaricentrumVisible());
			button_baricentrum.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent e)
				{
					fireActionEvent(new ActionEvent(button_baricentrum, 0, SycamoreFiredActionEvents.SHOW_BARICENTRUM.name()));
				}
			});
		}
		return button_baricentrum;
	}

	/**
	 * @return the button_lights
	 */
	private JToggleButton getButton_lights()
	{
		if (button_lights == null)
		{
			button_lights = new JToggleButton();
			button_lights.setIcon(new ImageIcon(getClass().getResource("/it/diunipi/volpi/sycamore/resources/lamp_32x32.png")));
			button_lights.setToolTipText("View lights");
			button_lights.setMinimumSize(new Dimension(45, 45));
			button_lights.setMaximumSize(new Dimension(45, 45));
			button_lights.setPreferredSize(new Dimension(45, 45));
			button_lights.setSelected(SycamoreSystem.isRobotsLightsVisible());
			button_lights.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent e)
				{
					fireActionEvent(new ActionEvent(button_lights, 0, SycamoreFiredActionEvents.SHOW_LIGHTS.name()));
				}
			});
		}
		return button_lights;
	}

	/**
	 * @return the button_visuals
	 */
	private JToggleButton getButton_visuals()
	{
		if (button_visuals == null)
		{
			button_visuals = new JToggleButton();
			button_visuals.setIcon(new ImageIcon(getClass().getResource("/it/diunipi/volpi/sycamore/resources/eye_32x32.png")));
			button_visuals.setToolTipText("View visual support elements");
			button_visuals.setMinimumSize(new Dimension(45, 45));
			button_visuals.setMaximumSize(new Dimension(45, 45));
			button_visuals.setPreferredSize(new Dimension(45, 45));
			button_visuals.setSelected(SycamoreSystem.isVisualElementsVisible());
			button_visuals.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent e)
				{
					fireActionEvent(new ActionEvent(button_coords, 0, SycamoreFiredActionEvents.SHOW_VISUAL_ELEMENTS.name()));
				}
			});
		}
		return button_visuals;
	}

	/**
	 * @return button_grid
	 */
	private JToggleButton getButton_grid()
	{
		if (button_grid == null)
		{
			button_grid = new JToggleButton();
			button_grid.setIcon(new ImageIcon(getClass().getResource("/it/diunipi/volpi/sycamore/resources/grid_32x32.png")));
			button_grid.setToolTipText("View 3D scene grid");
			button_grid.setMinimumSize(new Dimension(45, 45));
			button_grid.setMaximumSize(new Dimension(45, 45));
			button_grid.setPreferredSize(new Dimension(45, 45));
			button_grid.setSelected(SycamoreSystem.isGridVisible());
			button_grid.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent e)
				{
					fireActionEvent(new ActionEvent(button_grid, 0, SycamoreFiredActionEvents.SHOW_GRID.name()));
				}
			});
		}
		return button_grid;
	}

	/**
	 * @return button_axes
	 */
	private JToggleButton getButton_axes()
	{
		if (button_axes == null)
		{
			button_axes = new JToggleButton();
			button_axes.setIcon(new ImageIcon(getClass().getResource("/it/diunipi/volpi/sycamore/resources/axes_32x32.png")));
			button_axes.setToolTipText("View global coordinate axes");
			button_axes.setMinimumSize(new Dimension(45, 45));
			button_axes.setMaximumSize(new Dimension(45, 45));
			button_axes.setPreferredSize(new Dimension(45, 45));
			button_axes.setSelected(SycamoreSystem.isAxesVisible());
			button_axes.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent e)
				{
					fireActionEvent(new ActionEvent(button_axes, 0, SycamoreFiredActionEvents.SHOW_AXES.name()));
				}
			});
		}
		return button_axes;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.JComponent#setEnabled(boolean)
	 */
	@Override
	public void setEnabled(boolean enabled)
	{
		super.setEnabled(enabled);

		getButton_axes().setEnabled(enabled);
		getButton_grid().setEnabled(enabled);
		getButton_range().setEnabled(enabled);
		getButton_graph().setEnabled(enabled);
		getButton_dirs().setEnabled(enabled);
		getButton_coords().setEnabled(enabled);
		getButton_baricentrum().setEnabled(enabled);
		getButton_lights().setEnabled(enabled);
		getButton_visuals().setEnabled(enabled);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * it.diunipi.volpi.sycamore.gui.SycamorePanel#setAppEngine(it.diunipi.volpi.sycamore.engine
	 * .SycamoreEngine)
	 */
	@Override
	public void setAppEngine(SycamoreEngine appEngine)
	{
		// Nothing to do
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.diunipi.volpi.sycamore.gui.SycamorePanel#updateGui()
	 */
	@Override
	public void updateGui()
	{
		getButton_axes().setSelected(SycamoreSystem.isAxesVisible());
		getButton_grid().setSelected(SycamoreSystem.isGridVisible());
		getButton_range().setSelected(SycamoreSystem.isVisibilityRangesVisible());
		getButton_graph().setSelected(SycamoreSystem.isVisibilityGraphVisible());
		getButton_dirs().setSelected(SycamoreSystem.isMovementDirectionsVisible());
		getButton_coords().setSelected(SycamoreSystem.isLocalCoordinatesVisible());
		getButton_baricentrum().setSelected(SycamoreSystem.isBaricentrumVisible());
		getButton_lights().setSelected(SycamoreSystem.isRobotsLightsVisible());
		getButton_visuals().setSelected(SycamoreSystem.isVisualElementsVisible());
	}
	
	/* (non-Javadoc)
	 * @see it.diunipi.volpi.sycamore.gui.SycamorePanel#reset()
	 */
	@Override
	public void reset()
	{
		// Nothing to do
	}
}
