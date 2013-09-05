/**
 * 
 */
package it.diunipi.volpi.app.sycamore;

import it.diunipi.volpi.sycamore.gui.SycamoreInfoPanel;
import it.diunipi.volpi.sycamore.gui.SycamorePluginsExportingPanel;
import it.diunipi.volpi.sycamore.gui.SycamorePrefsPane;
import it.diunipi.volpi.sycamore.gui.SycamoreSystem;
import it.diunipi.volpi.sycamore.plugins.SycamorePluginExporter;
import it.diunipi.volpi.sycamore.plugins.SycamorePluginExporter.EXPORT_MODE;
import it.diunipi.volpi.sycamore.util.ApplicationProperties;
import it.diunipi.volpi.sycamore.util.PropertyManager;
import it.diunipi.volpi.sycamore.util.SycamoreFiredActionEvents;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

/**
 * The generic menu bar for the application. It defines the basic menu elements that will be used by
 * the concrete menu bars for the various environments.
 * 
 * @author Valerio Volpi - vale.v@me.com
 */
public abstract class SycamoreMenuBar extends JMenuBar
{
	private static final long		serialVersionUID					= 5611549792940856705L;

	private final SycamoreApp		application;

	private JMenu					menu_file							= null;
	private JMenu					menu_view							= null;
	private JMenu					menu_edit							= null;
	private JMenu					menu_help							= null;

	// file and edit menu items
	private JMenuItem				menuItem_new						= null;
	private JMenuItem				menuItem_newBatch					= null;
	private JMenuItem				menuItem_open						= null;
	private JMenu					menu_openRecent						= null;
	private JMenuItem				menuItem_closeWindow				= null;
	private JMenuItem				menuItem_save						= null;
	private JMenuItem				menuItem_saveAs						= null;
	private JMenuItem				menuItem_import						= null;
	private JMenuItem				menuItem_export						= null;
	private JMenu					menu_switchWorkspace				= null;
	private JMenuItem				menuItem_preferences				= null;

	// view menu items
	private JCheckBoxMenuItem		checkBoxmenuItem_grid				= null;
	private JCheckBoxMenuItem		checkBoxmenuItem_axes				= null;
	private JCheckBoxMenuItem		checkBoxmenuItem_visibilityRange	= null;
	private JCheckBoxMenuItem		checkBoxmenuItem_visibilityGraph	= null;
	private JCheckBoxMenuItem		checkBoxmenuItem_directions			= null;
	private JCheckBoxMenuItem		checkBoxmenuItem_localCoords		= null;
	private JCheckBoxMenuItem		checkBoxmenuItem_baricentrum		= null;
	private JCheckBoxMenuItem		checkBoxmenuItem_lights				= null;
	private JCheckBoxMenuItem		checkBoxmenuItem_visualSupports		= null;

	// help menu item
	private JMenuItem				menuItem_help						= null;
	private JMenuItem				menuItem_about						= null;

	private Vector<String>			oldWorkspaces						= null;
	private Vector<ActionListener>	listeners							= null;

	/**
	 * Default constructor.
	 */
	public SycamoreMenuBar(SycamoreApp application)
	{
		this.listeners = new Vector<ActionListener>();
		this.application = application;
		fillOldWorkspacesList();
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
	 * Fills the vector of the old workspaces, by taking them from application properties
	 */
	private void fillOldWorkspacesList()
	{
		this.oldWorkspaces = new Vector<String>();

		int i = 0;
		while (PropertyManager.getSharedInstance().getProperty("OLD_WORKSPACE_" + i) != null)
		{
			oldWorkspaces.add(PropertyManager.getSharedInstance().getProperty("OLD_WORKSPACE_" + i));
			i++;
		}
	}

	/**
	 * Returns the file menu
	 * 
	 * @return
	 */
	protected JMenu getMenu_file()
	{
		if (menu_file == null)
		{
			menu_file = new JMenu("File");
		}
		return menu_file;
	}

	/**
	 * Returns the view menu
	 * 
	 * @return
	 */
	protected JMenu getMenu_view()
	{
		if (menu_view == null)
		{
			menu_view = new JMenu("View");
		}
		return menu_view;
	}

	/**
	 * Returns the edit menu
	 * 
	 * @return the menu_edit
	 */
	protected JMenu getMenu_edit()
	{
		if (menu_edit == null)
		{
			menu_edit = new JMenu("Edit");
		}
		return menu_edit;
	}

	/**
	 * Returns the help menu
	 * 
	 * @return
	 */
	protected JMenu getMenu_help()
	{
		if (menu_help == null)
		{
			menu_help = new JMenu("Help");
		}
		return menu_help;
	}

	/**
	 * Return the menuitem for new menu
	 * 
	 * @return
	 */
	protected JMenuItem getMenuItem_new()
	{
		if (menuItem_new == null)
		{
			menuItem_new = new JMenuItem("New simulation");
			menuItem_new.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
			menuItem_new.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent e)
				{
					String pt1 = "<html><body><p>Do you want to save the current project before starting a new simulation?<br>";
					String pt2 = "All unsaved data will be lost.</p></body></html>";
					String s = pt1 + pt2;

					int retVal = JOptionPane.showOptionDialog(null, s, "Save project?", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, null, null, null);
					if (retVal == JOptionPane.YES_OPTION)
					{
						// TODO save
					}
					application.reset();
				}
			});
		}
		return menuItem_new;
	}

	/**
	 * Return the menuitem for new menu
	 * 
	 * @return
	 */
	protected JMenuItem getMenuItem_newBatch()
	{
		if (menuItem_newBatch == null)
		{
			menuItem_newBatch = new JMenuItem("New scheduled simulation");
			menuItem_newBatch.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, (java.awt.event.InputEvent.SHIFT_MASK | (Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()))));
		}
		return menuItem_newBatch;
	}

	/**
	 * Return the menuitem for Open menu
	 * 
	 * @return
	 */
	protected JMenuItem getMenuItem_open()
	{
		if (menuItem_open == null)
		{
			menuItem_open = new JMenuItem("Open...");
			menuItem_open.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		}
		return menuItem_open;
	}

	/**
	 * Return the menu for Open Recent menu
	 * 
	 * @return
	 */
	protected JMenu getMenu_openRecent()
	{
		if (menu_openRecent == null)
		{
			menu_openRecent = new JMenu("Open recent");
		}
		return menu_openRecent;
	}

	/**
	 * Return the menuitem for close window menu
	 * 
	 * @return
	 */
	protected JMenuItem getMenuItem_closeWindow()
	{
		if (menuItem_closeWindow == null)
		{
			menuItem_closeWindow = new JMenuItem("Close window");
			menuItem_closeWindow.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		}
		return menuItem_closeWindow;
	}

	/**
	 * Return the menuitem for Save menu
	 * 
	 * @return
	 */
	protected JMenuItem getMenuItem_save()
	{
		if (menuItem_save == null)
		{
			menuItem_save = new JMenuItem("Save");
			menuItem_save.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		}
		return menuItem_save;
	}

	/**
	 * Return the menuitem for Save menu
	 * 
	 * @return
	 */
	protected JMenuItem getMenuItem_saveAs()
	{
		if (menuItem_saveAs == null)
		{
			menuItem_saveAs = new JMenuItem("Save as...");
		}
		return menuItem_saveAs;
	}

	/**
	 * Return the menuitem for import menu
	 * 
	 * @return
	 */
	protected JMenuItem getMenuItem_Import()
	{
		if (menuItem_import == null)
		{
			menuItem_import = new JMenuItem("Import a new plugin...");
			menuItem_import.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_I, (java.awt.event.InputEvent.SHIFT_MASK | (Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()))));
			menuItem_import.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent e)
				{
					fireActionEvent(new ActionEvent(this, 0, SycamoreFiredActionEvents.LOAD_PLUGIN.name()));
				}
			});
		}
		return menuItem_import;
	}

	/**
	 * Return the menuitem for export menu
	 * 
	 * @return
	 */
	protected JMenuItem getMenuItem_Export()
	{
		if (menuItem_export == null)
		{
			menuItem_export = new JMenuItem("Export plugins...");
			menuItem_export.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent e)
				{
					SycamorePluginsExportingPanel pluginsExportingPanel = new SycamorePluginsExportingPanel();

					int result = JOptionPane.showOptionDialog(null, pluginsExportingPanel, "Export plugins to another location of the File System", JOptionPane.OK_CANCEL_OPTION,
							JOptionPane.PLAIN_MESSAGE, null, null, null);
					if (result == JOptionPane.OK_OPTION)
					{
						File[] filesToExport = pluginsExportingPanel.getFilesToExport();
						File exportDir = new File(pluginsExportingPanel.getExportingPath());
						EXPORT_MODE mode = pluginsExportingPanel.getExportMode();

						new SycamorePluginExporter().exportPlugins(filesToExport, exportDir, mode);
					}
				}
			});
		}
		return menuItem_export;
	}

	/**
	 * Return the menu for Open Recent menu
	 * 
	 * @return
	 */
	protected JMenu getMenu_switchWorkspace()
	{
		if (menu_switchWorkspace == null)
		{
			menu_switchWorkspace = new JMenu("Switch Workspace");

			for (final String path : this.oldWorkspaces)
			{
				JMenuItem old = new JMenuItem(path);
				old.addActionListener(new ActionListener()
				{
					@Override
					public void actionPerformed(ActionEvent e)
					{
						PropertyManager.getSharedInstance().putProperty(ApplicationProperties.WORKSPACE_DIR.name(), path);
						confirmReboot();
					}
				});

				menu_switchWorkspace.add(old);
			}

			JMenuItem other = new JMenuItem("Other...");
			other.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent e)
				{
					// open the file chooser
					JFileChooser fileChoser = new JFileChooser();
					fileChoser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

					// get the return val
					int returnVal = fileChoser.showOpenDialog(SycamoreMenuBar.this.getParent());

					if (returnVal == JFileChooser.APPROVE_OPTION)
					{
						// save old workspace
						String oldWorkspace = PropertyManager.getSharedInstance().getProperty(ApplicationProperties.WORKSPACE_DIR.name());

						int index = oldWorkspaces.size();
						PropertyManager.getSharedInstance().putProperty("OLD_WORKSPACE_" + index, oldWorkspace);

						// store new workspace
						File file = fileChoser.getSelectedFile();
						PropertyManager.getSharedInstance().putProperty(ApplicationProperties.WORKSPACE_DIR.name(), file.getAbsolutePath());

						// eventually reboot
						confirmReboot();
					}
				}
			});

			menu_switchWorkspace.add(other);
		}
		return menu_switchWorkspace;
	}

	/**
	 * Show a confirm message and eventually close the application
	 */
	private void confirmReboot()
	{
		String pt1 = "<html><body><p>You choose: " + PropertyManager.getSharedInstance().getProperty(ApplicationProperties.WORKSPACE_DIR.name()) + "<br>";
		String pt2 = "The new workspace will be effective on next Sycamore execution.</p>";
		String pt3 = "Do you want to exit now?</p></body></html>";

		String s = pt1 + pt2 + pt3;

		int retVal = JOptionPane.showOptionDialog(null, s, "Exit?", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, null, null, null);
		if (retVal == JOptionPane.YES_OPTION)
		{
			application.reboot();
		}
	}
	
	/**
	 * @return the menuItem_preferences
	 */
	protected JMenuItem getMenuItem_preferences()
	{
		if (menuItem_preferences == null)
		{
			menuItem_preferences = new JMenuItem("Preferences");
			menuItem_preferences.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent e)
				{
					ImageIcon icon = new ImageIcon(getClass().getResource("/it/diunipi/volpi/sycamore/resources/settings_64x64.png"));
					JOptionPane.showMessageDialog(null, new SycamorePrefsPane(), "Sycamore preferences", JOptionPane.INFORMATION_MESSAGE, icon);
				}
			});
		}
		return menuItem_preferences;
	}

	/**
	 * @return the checkBoxmenuItem_axes
	 */
	protected JCheckBoxMenuItem getCheckBoxmenuItem_axes()
	{
		if (checkBoxmenuItem_axes == null)
		{
			checkBoxmenuItem_axes = new JCheckBoxMenuItem("System coordinate axes");
			checkBoxmenuItem_axes.setIcon(new ImageIcon("/Users/Vale/Documents/Workspace Tesi/Sycamore-2-0/src/it/diunipi/volpi/sycamore/resources/axes_16x16.png"));
			checkBoxmenuItem_axes.setSelected(SycamoreSystem.isAxesVisible());
			checkBoxmenuItem_axes.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent e)
				{
					fireActionEvent(new ActionEvent(checkBoxmenuItem_axes, 0, SycamoreFiredActionEvents.SHOW_AXES.name()));
				}
			});
		}
		return checkBoxmenuItem_axes;
	}

	/**
	 * @return the checkBoxmenuItem_grid
	 */
	protected JCheckBoxMenuItem getCheckBoxmenuItem_grid()
	{
		if (checkBoxmenuItem_grid == null)
		{
			checkBoxmenuItem_grid = new JCheckBoxMenuItem("3D scene grid");
			checkBoxmenuItem_grid.setIcon(new ImageIcon("/Users/Vale/Documents/Workspace Tesi/Sycamore-2-0/src/it/diunipi/volpi/sycamore/resources/grid_16x16.png"));
			checkBoxmenuItem_grid.setSelected(SycamoreSystem.isGridVisible());
			checkBoxmenuItem_grid.addActionListener(new ActionListener()
			{

				@Override
				public void actionPerformed(ActionEvent e)
				{
					fireActionEvent(new ActionEvent(checkBoxmenuItem_grid, 0, SycamoreFiredActionEvents.SHOW_GRID.name()));
				}
			});
		}
		return checkBoxmenuItem_grid;
	}

	/**
	 * Return the checkbox menu item for visibility range
	 * 
	 * @return
	 */
	protected JCheckBoxMenuItem getCheckBoxmenuItem_visibilityRange()
	{
		if (checkBoxmenuItem_visibilityRange == null)
		{
			checkBoxmenuItem_visibilityRange = new JCheckBoxMenuItem("Visibility range");
			checkBoxmenuItem_visibilityRange.setIcon(new ImageIcon("/Users/Vale/Documents/Workspace Tesi/Sycamore-2-0/src/it/diunipi/volpi/sycamore/resources/radar_16x16.png"));
			checkBoxmenuItem_visibilityRange.setSelected(SycamoreSystem.isVisibilityRangesVisible());
			checkBoxmenuItem_visibilityRange.addActionListener(new ActionListener()
			{

				@Override
				public void actionPerformed(ActionEvent e)
				{
					fireActionEvent(new ActionEvent(checkBoxmenuItem_visibilityRange, 0, SycamoreFiredActionEvents.SHOW_VISIBILITY_RANGE.name()));
				}
			});
		}
		return checkBoxmenuItem_visibilityRange;
	}

	/**
	 * Return the checkbox menu item for visibility graph
	 * 
	 * @return
	 */
	protected JCheckBoxMenuItem getCheckBoxmenuItem_visibilityGraph()
	{
		if (checkBoxmenuItem_visibilityGraph == null)
		{
			checkBoxmenuItem_visibilityGraph = new JCheckBoxMenuItem("Visibility graph");
			checkBoxmenuItem_visibilityGraph.setIcon(new ImageIcon("/Users/Vale/Documents/Workspace Tesi/Sycamore-2-0/src/it/diunipi/volpi/sycamore/resources/graph_16x16.png"));
			checkBoxmenuItem_visibilityGraph.setSelected(SycamoreSystem.isVisibilityGraphVisible());
			checkBoxmenuItem_visibilityGraph.addActionListener(new ActionListener()
			{

				@Override
				public void actionPerformed(ActionEvent e)
				{
					fireActionEvent(new ActionEvent(checkBoxmenuItem_visibilityGraph, 0, SycamoreFiredActionEvents.SHOW_VISIBILITY_GRAPH.name()));
				}
			});
		}
		return checkBoxmenuItem_visibilityGraph;
	}

	/**
	 * Return the checkbox menu item for directions
	 * 
	 * @return
	 */
	protected JCheckBoxMenuItem getCheckBoxmenuItem_directions()
	{
		if (checkBoxmenuItem_directions == null)
		{
			checkBoxmenuItem_directions = new JCheckBoxMenuItem("Robots directions");
			checkBoxmenuItem_directions.setIcon(new ImageIcon("/Users/Vale/Documents/Workspace Tesi/Sycamore-2-0/src/it/diunipi/volpi/sycamore/resources/directions_16x16.png"));
			checkBoxmenuItem_directions.setSelected(SycamoreSystem.isMovementDirectionsVisible());
			checkBoxmenuItem_directions.addActionListener(new ActionListener()
			{

				@Override
				public void actionPerformed(ActionEvent e)
				{
					fireActionEvent(new ActionEvent(checkBoxmenuItem_directions, 0, SycamoreFiredActionEvents.SHOW_MOVEMENT_DIRECTIONS.name()));
				}
			});
		}
		return checkBoxmenuItem_directions;
	}

	/**
	 * Return the checkbox menu item for local coordinates
	 * 
	 * @return
	 */
	protected JCheckBoxMenuItem getCheckBoxmenuItem_localCoords()
	{
		if (checkBoxmenuItem_localCoords == null)
		{
			checkBoxmenuItem_localCoords = new JCheckBoxMenuItem("Local coordinate system");
			checkBoxmenuItem_localCoords.setIcon(new ImageIcon("/Users/Vale/Documents/Workspace Tesi/Sycamore-2-0/src/it/diunipi/volpi/sycamore/resources/compass_16x16.png"));
			checkBoxmenuItem_localCoords.setSelected(SycamoreSystem.isLocalCoordinatesVisible());
			checkBoxmenuItem_localCoords.addActionListener(new ActionListener()
			{

				@Override
				public void actionPerformed(ActionEvent e)
				{
					fireActionEvent(new ActionEvent(checkBoxmenuItem_localCoords, 0, SycamoreFiredActionEvents.SHOW_LOCAL_COORDINATES.name()));
				}
			});
		}
		return checkBoxmenuItem_localCoords;
	}

	/**
	 * Return the checkbox menu item for baricentrum
	 * 
	 * @return
	 */
	protected JCheckBoxMenuItem getCheckBoxmenuItem_baricentrum()
	{
		if (checkBoxmenuItem_baricentrum == null)
		{
			checkBoxmenuItem_baricentrum = new JCheckBoxMenuItem("Baricentrum");
			checkBoxmenuItem_baricentrum.setIcon(new ImageIcon("/Users/Vale/Documents/Workspace Tesi/Sycamore-2-0/src/it/diunipi/volpi/sycamore/resources/plus_16x16.png"));
			checkBoxmenuItem_baricentrum.setSelected(SycamoreSystem.isBaricentrumVisible());
			checkBoxmenuItem_baricentrum.addActionListener(new ActionListener()
			{

				@Override
				public void actionPerformed(ActionEvent e)
				{
					fireActionEvent(new ActionEvent(checkBoxmenuItem_baricentrum, 0, SycamoreFiredActionEvents.SHOW_BARICENTRUM.name()));
				}
			});
		}
		return checkBoxmenuItem_baricentrum;
	}

	/**
	 * Return the checkbox menu item for lights
	 * 
	 * @return
	 */
	protected JCheckBoxMenuItem getCheckBoxmenuItem_lights()
	{
		if (checkBoxmenuItem_lights == null)
		{
			checkBoxmenuItem_lights = new JCheckBoxMenuItem("Lights");
			checkBoxmenuItem_lights.setIcon(new ImageIcon("/Users/Vale/Documents/Workspace Tesi/Sycamore-2-0/src/it/diunipi/volpi/sycamore/resources/lamp_16x16.png"));
			checkBoxmenuItem_lights.setSelected(SycamoreSystem.isRobotsLightsVisible());
			checkBoxmenuItem_lights.addActionListener(new ActionListener()
			{

				@Override
				public void actionPerformed(ActionEvent e)
				{
					fireActionEvent(new ActionEvent(checkBoxmenuItem_lights, 0, SycamoreFiredActionEvents.SHOW_LIGHTS.name()));
				}
			});
		}
		return checkBoxmenuItem_lights;
	}

	/**
	 * Return the checkbox menu item for visual supports
	 * 
	 * @return
	 */
	protected JCheckBoxMenuItem getCheckBoxmenuItem_visualSupports()
	{
		if (checkBoxmenuItem_visualSupports == null)
		{
			checkBoxmenuItem_visualSupports = new JCheckBoxMenuItem("Visual support elements");
			checkBoxmenuItem_visualSupports.setIcon(new ImageIcon("/Users/Vale/Documents/Workspace Tesi/Sycamore-2-0/src/it/diunipi/volpi/sycamore/resources/eye_16x16.png"));
			checkBoxmenuItem_visualSupports.setSelected(SycamoreSystem.isVisualElementsVisible());
			checkBoxmenuItem_visualSupports.addActionListener(new ActionListener()
			{

				@Override
				public void actionPerformed(ActionEvent e)
				{
					fireActionEvent(new ActionEvent(checkBoxmenuItem_visualSupports, 0, SycamoreFiredActionEvents.SHOW_VISUAL_ELEMENTS.name()));
				}
			});
		}
		return checkBoxmenuItem_visualSupports;
	}

	/**
	 * Return the menuitem for help menu item
	 * 
	 * @return
	 */
	protected JMenuItem getMenuItem_help()
	{
		if (menuItem_help == null)
		{
			menuItem_help = new JMenuItem("Help on Sycamore...");
		}
		return menuItem_help;
	}
	
	/**
	 * Return the menuitem for about menu item
	 * 
	 * @return the menuItem_about
	 */
	protected JMenuItem getMenuItem_about()
	{
		if (menuItem_about == null)
		{
			menuItem_about = new JMenuItem("About Sycamore...");
			menuItem_about.addActionListener(new ActionListener()
			{			
				@Override
				public void actionPerformed(ActionEvent e)
				{
					ImageIcon icon = new ImageIcon(getClass().getResource("/it/diunipi/volpi/sycamore/resources/sycamore_64x64.png"));
					JOptionPane.showMessageDialog(null, new SycamoreInfoPanel(), "About Sycamore", JOptionPane.INFORMATION_MESSAGE, icon);
				}
			});
		}
		return menuItem_about;
	}

	/**
	 * Setup the concrete menu bar for a specific environment
	 */
	protected abstract void setupMenuBar();

	/**
	 * Setup the preferences menu for a specific environment
	 */
	protected abstract void setupPreferencesmenu();

	/**
	 * Setup the about menu for a specific environment
	 */
	protected abstract void setupAboutMenu();

	/**
	 * Update Gui to reflect changes in engine
	 */
	public void updateGui()
	{
		getCheckBoxmenuItem_axes().setSelected(SycamoreSystem.isAxesVisible());
		getCheckBoxmenuItem_grid().setSelected(SycamoreSystem.isGridVisible());
		getCheckBoxmenuItem_visibilityRange().setSelected(SycamoreSystem.isVisibilityRangesVisible());
		getCheckBoxmenuItem_visibilityGraph().setSelected(SycamoreSystem.isVisibilityGraphVisible());
		getCheckBoxmenuItem_directions().setSelected(SycamoreSystem.isMovementDirectionsVisible());
		getCheckBoxmenuItem_localCoords().setSelected(SycamoreSystem.isLocalCoordinatesVisible());
		getCheckBoxmenuItem_baricentrum().setSelected(SycamoreSystem.isBaricentrumVisible());
		getCheckBoxmenuItem_lights().setSelected(SycamoreSystem.isRobotsLightsVisible());
		getCheckBoxmenuItem_visualSupports().setSelected(SycamoreSystem.isVisualElementsVisible());
	}
}
