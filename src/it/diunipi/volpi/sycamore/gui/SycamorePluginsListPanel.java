package it.diunipi.volpi.sycamore.gui;

import it.diunipi.volpi.sycamore.engine.SycamoreEngine;
import it.diunipi.volpi.sycamore.plugins.SycamorePlugin;
import it.diunipi.volpi.sycamore.plugins.SycamorePluginManager;
import it.diunipi.volpi.sycamore.util.SycamoreFiredActionEvents;
import it.diunipi.volpi.sycamore.util.SycamoreUtil;

import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.table.DefaultTableModel;

import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.decorator.HighlighterFactory;

/**
 * The panel that contains a table with all loaded plugins
 * 
 * @author Valerio Volpi - vale.v@me.com
 */
public class SycamorePluginsListPanel extends SycamoreTitledRoundedBorderPanel
{
	/**
	 * The columns of the table of the plugins
	 * 
	 * @author Valerio Volpi - vale.v@me.com
	 */
	public static enum COLUMNS
	{
		TYPE("Type"), NAME("Name");

		private String	title;

		/**
		 * Constructor for COLUMNS
		 * 
		 * @param title
		 *            the title
		 */
		COLUMNS(String title)
		{
			this.title = title;
		}

		/**
		 * @return the title
		 */
		public String getTitle()
		{
			return title;
		}
	}

	/**
	 * The tableModel for plugins table
	 * 
	 * @author Valerio Volpi - vale.v@me.com
	 * 
	 */
	private class PluginsTableModel extends DefaultTableModel
	{
		private static final long	serialVersionUID	= -7839329788424850873L;

		/*
		 * (non-Javadoc)
		 * 
		 * @see javax.swing.table.DefaultTableModel#getRowCount()
		 */
		@Override
		public int getRowCount()
		{
			SycamorePluginManager manager = SycamorePluginManager.getSharedInstance();
			return manager.getLoadedPlugins().size();
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see javax.swing.table.DefaultTableModel#getColumnCount()
		 */
		@Override
		public int getColumnCount()
		{
			return COLUMNS.values().length;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see javax.swing.table.DefaultTableModel#getColumnName(int)
		 */
		@Override
		public String getColumnName(int columnIndex)
		{
			if (columnIndex == COLUMNS.TYPE.ordinal())
			{
				return COLUMNS.TYPE.getTitle();
			}
			if (columnIndex == COLUMNS.NAME.ordinal())
			{
				return COLUMNS.NAME.getTitle();
			}
			return null;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see javax.swing.table.AbstractTableModel#getColumnClass(int)
		 */
		@Override
		public Class<?> getColumnClass(int columnIndex)
		{
			if (columnIndex == COLUMNS.TYPE.ordinal())
			{
				return COLUMNS.TYPE.getClass();
			}
			if (columnIndex == COLUMNS.NAME.ordinal())
			{
				return COLUMNS.NAME.getClass();
			}
			return null;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see javax.swing.table.DefaultTableModel#isCellEditable(int, int)
		 */
		@Override
		public boolean isCellEditable(int rowIndex, int columnIndex)
		{
			return false;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see javax.swing.table.DefaultTableModel#getValueAt(int, int)
		 */
		@Override
		public Object getValueAt(int rowIndex, int columnIndex)
		{
			SycamorePluginManager manager = SycamorePluginManager.getSharedInstance();
			ArrayList<SycamorePlugin> plugins = manager.getLoadedPlugins();

			// get the data
			SycamorePlugin plugin = plugins.get(rowIndex);

			// return the value
			if (columnIndex == COLUMNS.TYPE.ordinal())
			{
				return plugin.getPluginClassShortDescription();
			}
			if (columnIndex == COLUMNS.NAME.ordinal())
			{
				return plugin.getPluginName();
			}

			return null;
		}

		/**
		 * Return the plugin at passed row
		 * 
		 * @param rowIndex
		 * @return
		 */
		public SycamorePlugin getValueAt(int rowIndex)
		{
			SycamorePluginManager manager = SycamorePluginManager.getSharedInstance();
			ArrayList<SycamorePlugin> plugins = manager.getLoadedPlugins();

			// get the data
			return plugins.get(rowIndex);
		}
	}

	private static final long	serialVersionUID			= -3336314826620173106L;
	private JXTable				table_loadedPlugins			= null;
	private SycamoreEngine		appEngine					= null;
	private JButton				button_newPlugin			= null;
	private JButton				button_refreshPlugins		= null;
	private JScrollPane			scrollPane_tableContainer	= null;
	private PluginsTableModel	tableModel					= null;

	/**
	 * Default contructor
	 */
	public SycamorePluginsListPanel()
	{
		setPreferredSize(new Dimension(250, 250));
		setMinimumSize(new Dimension(200, 55));
		initialize();
	}

	/**
	 * Populate GUI
	 */
	private void initialize()
	{
		getLabel_title().setText("Loaded plugins");
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]
		{ 1, 0 };
		gridBagLayout.rowHeights = new int[]
		{ 1, 0, 0 };
		gridBagLayout.columnWeights = new double[]
		{ 1.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[]
		{ 1.0, 0.0, Double.MIN_VALUE };
		getPanel_contentContainer().setLayout(gridBagLayout);

		GridBagConstraints gbc_scrollPane_tableContainer = new GridBagConstraints();
		gbc_scrollPane_tableContainer.fill = GridBagConstraints.BOTH;
		gbc_scrollPane_tableContainer.gridwidth = 2;
		gbc_scrollPane_tableContainer.insets = new Insets(0, 0, 2, 3);
		gbc_scrollPane_tableContainer.gridx = 0;
		gbc_scrollPane_tableContainer.gridy = 0;
		getPanel_contentContainer().add(getScrollPane_tableContainer(), gbc_scrollPane_tableContainer);

		GridBagConstraints gbc_button_newPlugin = new GridBagConstraints();
		gbc_button_newPlugin.weightx = 1.0;
		gbc_button_newPlugin.anchor = GridBagConstraints.EAST;
		gbc_button_newPlugin.insets = new Insets(2, 2, 10, 5);
		gbc_button_newPlugin.gridx = 0;
		gbc_button_newPlugin.gridy = 1;
		getPanel_contentContainer().add(getButton_newPlugin(), gbc_button_newPlugin);

		GridBagConstraints gbc_button_resetPlugins = new GridBagConstraints();
		gbc_button_resetPlugins.anchor = GridBagConstraints.EAST;
		gbc_button_resetPlugins.insets = new Insets(2, 2, 10, 7);
		gbc_button_resetPlugins.gridx = 1;
		gbc_button_resetPlugins.gridy = 1;
		getPanel_contentContainer().add(getButton_refreshPlugins(), gbc_button_resetPlugins);
	}

	/**
	 * Returns the table with loaded plugins
	 * 
	 * @return
	 */
	private JXTable getTable_loadedPlugins()
	{
		if (table_loadedPlugins == null)
		{
			table_loadedPlugins = new JXTable();
			table_loadedPlugins.setBorder(BorderFactory.createEmptyBorder());
			table_loadedPlugins.addHighlighter(HighlighterFactory.createAlternateStriping());
			table_loadedPlugins.addMouseListener(new MouseAdapter()
			{
				/*
				 * (non-Javadoc)
				 * 
				 * @see
				 * com.jogamp.newt.event.MouseAdapter#mouseClicked(com.jogamp.newt.event.MouseEvent)
				 */
				@Override
				public void mouseClicked(MouseEvent evt)
				{
					if (evt.getClickCount() == 2 && evt.getButton() == MouseEvent.BUTTON1)
					{
						int row = getTable_loadedPlugins().getSelectedRow();
						PluginsTableModel model = (PluginsTableModel) getTable_loadedPlugins().getModel();

						if (row >= 0 && row < model.getRowCount())
						{
							SycamorePlugin plugin = model.getValueAt(row);
							SycamorePluginInformationsPanel settingPanel = new SycamorePluginInformationsPanel(plugin);
							settingPanel.setAppEngine(appEngine);
							settingPanel.addActionListener(new ActionListener()
							{
								@Override
								public void actionPerformed(ActionEvent e)
								{
									// Forward event
									fireActionEvent(e);
								}
							});

							ImageIcon icon = new ImageIcon(getClass().getResource("/it/diunipi/volpi/sycamore/resources/settings_64x64.png"));
							JOptionPane.showMessageDialog(SycamorePluginsListPanel.this.getParent(), settingPanel, "Settings for plugin " + plugin.getPluginName(), JOptionPane.INFORMATION_MESSAGE,
									icon);
						}
					}
				}
			});
		}
		return table_loadedPlugins;
	}

	/**
	 * @return
	 */
	private JScrollPane getScrollPane_tableContainer()
	{
		if (scrollPane_tableContainer == null)
		{
			scrollPane_tableContainer = new JScrollPane();
			scrollPane_tableContainer.setBorder(BorderFactory.createEmptyBorder());
			scrollPane_tableContainer.setViewportView(getTable_loadedPlugins());
		}
		return scrollPane_tableContainer;
	}

	/**
	 * Lets the user load a new plugin by taking it from the file system
	 */
	public void loadPluginFromFileSystem()
	{
		// show a file dialog
		FileDialog dialog = new FileDialog((Frame) null);
		dialog.setTitle("Select the plugin's JAR file");
		dialog.setFilenameFilter(new FilenameFilter()
		{
			@Override
			public boolean accept(File dir, String name)
			{
				return name.endsWith(".jar");
			}
		});
		dialog.setVisible(true);

		// get the file
		String file = dialog.getFile();
		if (file != null)
		{
			try
			{
				// copy file into workspace
				String inPath = dialog.getDirectory() + file;
				String outPath = SycamoreSystem.getPluginsDirectory() + System.getProperty("file.separator") + file;

				SycamoreUtil.copyFile(new File(inPath), new File(outPath));

				refreshPlugins();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}

	/**
	 * @return the button_newPlugin
	 */
	private JButton getButton_newPlugin()
	{
		if (button_newPlugin == null)
		{
			button_newPlugin = new JButton();
			button_newPlugin.setIcon(new ImageIcon(getClass().getResource("/it/diunipi/volpi/sycamore/resources/plus_green_16x16.png")));
			button_newPlugin.setPreferredSize(new Dimension(30, 30));
			button_newPlugin.setMinimumSize(new Dimension(30, 30));
			button_newPlugin.setMaximumSize(new Dimension(30, 30));
			button_newPlugin.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent e)
				{
					loadPluginFromFileSystem();
				}
			});
		}
		return button_newPlugin;
	}

	/**
	 * @return the button_newPlugin
	 */
	private JButton getButton_refreshPlugins()
	{
		if (button_refreshPlugins == null)
		{
			button_refreshPlugins = new JButton();
			button_refreshPlugins.setIcon(new ImageIcon(getClass().getResource("/it/diunipi/volpi/sycamore/resources/refresh_16x16.png")));
			button_refreshPlugins.setPreferredSize(new Dimension(30, 30));
			button_refreshPlugins.setMinimumSize(new Dimension(30, 30));
			button_refreshPlugins.setMaximumSize(new Dimension(30, 30));
			button_refreshPlugins.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent e)
				{
					boolean doRefresh = true;
					if (appEngine != null)
					{
						String pt1 = "<html><body><p>Are you sure that you want to refresh the plugins?<br>";
						String pt2 = "This will destroy the current simulation.</p></body></html>";

						String s = pt1 + pt2;

						int choice = JOptionPane.showOptionDialog(getParent(), s, "Confirm refresh", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, null, null, null);
						doRefresh = (choice == JOptionPane.YES_OPTION);
					}

					if (doRefresh)
					{
						refreshPlugins();
					}
				}
			});
		}
		return button_refreshPlugins;
	}

	/**
	 * Refresh the loaded plugins
	 */
	private void refreshPlugins()
	{
		// refresh plugins and update
		SycamorePluginManager.getSharedInstance().refreshPlugins();
		fireActionEvent(new ActionEvent(this, 0, SycamoreFiredActionEvents.PLUGINS_REFRESHED.name()));

		if (appEngine != null)
		{
			fireActionEvent(new ActionEvent(this, 0, SycamoreFiredActionEvents.SIMULATION_DATA_BAD.name()));
		}
	}

	/**
	 * Returns the current app engine
	 * 
	 * @return
	 */
	public SycamoreEngine getAppEngine()
	{
		return appEngine;
	}

	/**
	 * Changes the current app engine replacing it with passed one.
	 * 
	 * @param appEngine
	 */
	public void setAppEngine(SycamoreEngine appEngine)
	{
		this.appEngine = appEngine;
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

		getButton_newPlugin().setEnabled(enabled);
		getButton_refreshPlugins().setEnabled(enabled);
		getTable_loadedPlugins().setEnabled(enabled);
	}

	/**
	 * Updates the GUI in order to reflect changes in the engine
	 */
	public void updateGui()
	{
		if (this.tableModel == null)
		{
			this.tableModel = new PluginsTableModel();

			getTable_loadedPlugins().setModel(this.tableModel);
			getTable_loadedPlugins().getColumn(COLUMNS.TYPE.ordinal()).setMinWidth(50);
			getTable_loadedPlugins().getColumn(COLUMNS.TYPE.ordinal()).setPreferredWidth(70);
			getTable_loadedPlugins().getColumn(COLUMNS.TYPE.ordinal()).setMaxWidth(100);
		}
		else
		{
			this.tableModel.fireTableDataChanged();
		}
	}
}
