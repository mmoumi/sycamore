/**
 * 
 */
package it.diunipi.volpi.sycamore.gui;

import it.diunipi.volpi.sycamore.engine.SycamoreEngine;
import it.diunipi.volpi.sycamore.plugins.SycamorePlugin;
import it.diunipi.volpi.sycamore.plugins.SycamorePluginManager;
import it.diunipi.volpi.sycamore.plugins.SycamorePluginExporter.EXPORT_MODE;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.decorator.HighlighterFactory;

/**
 * A panel that lets the user export installed plugins in a jar file
 * 
 * @author Valerio Volpi - vale.v@me.com
 */
public class SycamorePluginsExportingPanel extends SycamorePanel
{
	/**
	 * @author Valerio Volpi - vale.v@me.com
	 */
	private class VoidPlugin implements SycamorePlugin
	{
		/*
		 * (non-Javadoc)
		 * 
		 * @see it.diunipi.volpi.sycamore.plugins.SycamorePlugin#getAuthor()
		 */
		@Override
		public String getAuthor()
		{
			return null;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see it.diunipi.volpi.sycamore.plugins.SycamorePlugin#getPluginName()
		 */
		@Override
		public String getPluginName()
		{
			return "Unable to find plugin associated with file.";
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see it.diunipi.volpi.sycamore.plugins.SycamorePlugin#getPluginShortDescription()
		 */
		@Override
		public String getPluginShortDescription()
		{
			return null;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see it.diunipi.volpi.sycamore.plugins.SycamorePlugin#getPluginLongDescription()
		 */
		@Override
		public String getPluginLongDescription()
		{
			return null;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see it.diunipi.volpi.sycamore.plugins.SycamorePlugin#getPluginClassLongDescription()
		 */
		@Override
		public String getPluginClassLongDescription()
		{
			return null;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see it.diunipi.volpi.sycamore.plugins.SycamorePlugin#getPluginClassShortDescription()
		 */
		@Override
		public String getPluginClassShortDescription()
		{
			return "Unknown";
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see it.diunipi.volpi.sycamore.plugins.SycamorePlugin#getPluginClassDescription()
		 */
		@Override
		public String getPluginClassDescription()
		{
			return null;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see it.diunipi.volpi.sycamore.plugins.SycamorePlugin#getPanel_settings()
		 */
		@Override
		public SycamorePanel getPanel_settings()
		{
			return null;
		}
	}

	/**
	 * The columns of the table of the plugins
	 * 
	 * @author Valerio Volpi - vale.v@me.com
	 */
	public static enum COLUMNS
	{
		FILE("Plugin file"), TYPE("Type"), NAME("Plugin name");

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
	 * @author Valerio Volpi - vale.v@me.com
	 */
	private class PluginsFileTableModel extends DefaultTableModel
	{
		private static final long		serialVersionUID	= 6047507836467475529L;
		private Vector<File>			pluginsFiles		= null;
		private Vector<SycamorePlugin>	pairedPlugins		= null;

		/**
		 * Default constructor
		 */
		public PluginsFileTableModel()
		{
			if (pluginsFiles == null)
			{
				initialize();
			}
		}

		/**
		 * Init model
		 */
		private void initialize()
		{
			this.pluginsFiles = new Vector<File>();
			this.pairedPlugins = new Vector<SycamorePlugin>();

			// list files in plugins directory
			File pluginsDir = SycamoreSystem.getPluginsDirectory();
			File[] files = pluginsDir.listFiles();

			// add plugins files
			for (File file : files)
			{
				if (file.getName().endsWith(".jar"))
				{
					pluginsFiles.add(file);
				}
			}

			// pair files with plugins
			ArrayList<SycamorePlugin> plugins = SycamorePluginManager.getSharedInstance().getLoadedPlugins();
			for (File pluginFile : pluginsFiles)
			{
				String fileWithoutExtension = pluginFile.getName().replaceAll(".jar", "");
				
				boolean found = false;
				for (SycamorePlugin plugin : plugins)
				{
					if (plugin.getPluginName().equalsIgnoreCase(fileWithoutExtension))
					{
						pairedPlugins.add(plugin);
						found = true;
						break;
					}
				}
				
				if (!found)
				{
					pairedPlugins.add(new VoidPlugin());
				}
			}
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see javax.swing.table.DefaultTableModel#getRowCount()
		 */
		@Override
		public int getRowCount()
		{
			if (pluginsFiles == null)
			{
				initialize();
			}
			return pluginsFiles.size();
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
			if (columnIndex == COLUMNS.FILE.ordinal())
			{
				return COLUMNS.FILE.getTitle();
			}
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
			if (columnIndex == COLUMNS.FILE.ordinal())
			{
				return COLUMNS.FILE.getClass();
			}
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
			if (pluginsFiles == null)
			{
				initialize();
			}
			
			// return value
			if (columnIndex == COLUMNS.FILE.ordinal())
			{
				return pluginsFiles.elementAt(rowIndex).getAbsolutePath();
			}
			else if (columnIndex == COLUMNS.TYPE.ordinal())
			{
				return pairedPlugins.elementAt(rowIndex).getPluginClassShortDescription();
			}
			else
			{
				return pairedPlugins.elementAt(rowIndex).getPluginName();
			}
		}
	}

	private static final long	serialVersionUID					= -7273224324197567065L;
	private JLabel				label_title_first					= null;
	private JXTable				table_pluginsTable					= null;
	private JScrollPane			scrollPane_pluginsTable				= null;
	private JLabel				label_title_second					= null;
	private JLabel				label_exportMode					= null;
	private JRadioButton		radioButton_exportMode_fileByFile	= null;
	private JRadioButton		radioButton_exportMode_zipped		= null;
	private JLabel				label_targetDirectory				= null;
	private JTextField			textField_targetDirectory			= null;
	private JButton				button_open							= null;

	private EXPORT_MODE			exportMode							= EXPORT_MODE.FILE_BY_FILE;

	/**
	 * Default constructor.
	 */
	public SycamorePluginsExportingPanel()
	{
		initialize();
	}

	/**
	 * Init GUI
	 */
	private void initialize()
	{
		this.setPreferredSize(new Dimension(800, 600));

		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWeights = new double[]
		{ 1.0, 0.0 };
		gridBagLayout.rowWeights = new double[]
		{ 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0 };
		setLayout(gridBagLayout);

		GridBagConstraints gbc_label_title_first = new GridBagConstraints();
		gbc_label_title_first.anchor = GridBagConstraints.WEST;
		gbc_label_title_first.gridwidth = 2;
		gbc_label_title_first.insets = new Insets(2, 2, 2, 2);
		gbc_label_title_first.gridx = 0;
		gbc_label_title_first.gridy = 0;
		add(getLabel_title_first(), gbc_label_title_first);
		GridBagConstraints gbc_label_title_second = new GridBagConstraints();
		gbc_label_title_second.anchor = GridBagConstraints.WEST;
		gbc_label_title_second.gridwidth = 2;
		gbc_label_title_second.insets = new Insets(2, 2, 2, 2);
		gbc_label_title_second.gridx = 0;
		gbc_label_title_second.gridy = 1;
		add(getLabel_title_second(), gbc_label_title_second);
		GridBagConstraints gbc_scrollPane_pluginsTable = new GridBagConstraints();
		gbc_scrollPane_pluginsTable.fill = GridBagConstraints.BOTH;
		gbc_scrollPane_pluginsTable.gridwidth = 2;
		gbc_scrollPane_pluginsTable.insets = new Insets(2, 2, 2, 2);
		gbc_scrollPane_pluginsTable.gridx = 0;
		gbc_scrollPane_pluginsTable.gridy = 2;
		add(getScrollPane_pluginsTable(), gbc_scrollPane_pluginsTable);
		GridBagConstraints gbc_label_exportMode = new GridBagConstraints();
		gbc_label_exportMode.anchor = GridBagConstraints.WEST;
		gbc_label_exportMode.gridwidth = 2;
		gbc_label_exportMode.insets = new Insets(2, 2, 2, 2);
		gbc_label_exportMode.gridx = 0;
		gbc_label_exportMode.gridy = 3;
		add(getLabel_exportMode(), gbc_label_exportMode);
		GridBagConstraints gbc_radioButton_exportMode_fileByFile = new GridBagConstraints();
		gbc_radioButton_exportMode_fileByFile.anchor = GridBagConstraints.WEST;
		gbc_radioButton_exportMode_fileByFile.gridwidth = 2;
		gbc_radioButton_exportMode_fileByFile.insets = new Insets(2, 2, 2, 2);
		gbc_radioButton_exportMode_fileByFile.gridx = 0;
		gbc_radioButton_exportMode_fileByFile.gridy = 4;
		add(getRadioButton_exportMode_fileByFile(), gbc_radioButton_exportMode_fileByFile);
		GridBagConstraints gbc_radioButton_exportMode_zipped = new GridBagConstraints();
		gbc_radioButton_exportMode_zipped.anchor = GridBagConstraints.WEST;
		gbc_radioButton_exportMode_zipped.gridwidth = 2;
		gbc_radioButton_exportMode_zipped.insets = new Insets(2, 2, 2, 2);
		gbc_radioButton_exportMode_zipped.gridx = 0;
		gbc_radioButton_exportMode_zipped.gridy = 5;
		add(getRadioButton_exportMode_zipped(), gbc_radioButton_exportMode_zipped);
		GridBagConstraints gbc_label_targetDirectory = new GridBagConstraints();
		gbc_label_targetDirectory.anchor = GridBagConstraints.SOUTH;
		gbc_label_targetDirectory.gridwidth = 2;
		gbc_label_targetDirectory.insets = new Insets(2, 2, 2, 2);
		gbc_label_targetDirectory.gridx = 0;
		gbc_label_targetDirectory.gridy = 6;
		add(getLabel_targetDirectory(), gbc_label_targetDirectory);
		GridBagConstraints gbc_textField_targetDirectory = new GridBagConstraints();
		gbc_textField_targetDirectory.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_targetDirectory.insets = new Insets(2, 2, 2, 2);
		gbc_textField_targetDirectory.gridx = 0;
		gbc_textField_targetDirectory.gridy = 7;
		add(getTextField_targetDirectory(), gbc_textField_targetDirectory);
		GridBagConstraints gbc_button_open = new GridBagConstraints();
		gbc_button_open.insets = new Insets(2, 2, 2, 2);
		gbc_button_open.gridx = 1;
		gbc_button_open.gridy = 7;
		add(getButton_open(), gbc_button_open);
		
		getTable_pluginsTable().requestFocus();
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
		// Nothing to do
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.diunipi.volpi.sycamore.gui.SycamorePanel#reset()
	 */
	@Override
	public void reset()
	{
		// Nothing to do
	}

	/**
	 * @param exportMode
	 *            the exportMode to set
	 */
	public void setExportMode(EXPORT_MODE exportMode)
	{
		this.exportMode = exportMode;
	}

	/**
	 * @return the exportMode
	 */
	public EXPORT_MODE getExportMode()
	{
		return exportMode;
	}

	/**
	 * @return label_title_first
	 */
	private JLabel getLabel_title_first()
	{
		if (label_title_first == null)
		{
			label_title_first = new JLabel("Here are the plugins files in the workspace directory:");
		}
		return label_title_first;
	}

	/**
	 * @return table_pluginsTable
	 */
	private JXTable getTable_pluginsTable()
	{
		if (table_pluginsTable == null)
		{
			table_pluginsTable = new JXTable();
			table_pluginsTable.addHighlighter(HighlighterFactory.createAlternateStriping());
			table_pluginsTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
			table_pluginsTable.setModel(new PluginsFileTableModel());
			table_pluginsTable.getColumn(COLUMNS.TYPE.ordinal()).setMinWidth(50);
			table_pluginsTable.getColumn(COLUMNS.TYPE.ordinal()).setPreferredWidth(70);
			table_pluginsTable.getColumn(COLUMNS.TYPE.ordinal()).setMaxWidth(100);
			table_pluginsTable.getColumn(COLUMNS.FILE.ordinal()).setPreferredWidth(400);
		}
		return table_pluginsTable;
	}

	/**
	 * @return scrollPane_pluginsTable
	 */
	private JScrollPane getScrollPane_pluginsTable()
	{
		if (scrollPane_pluginsTable == null)
		{
			scrollPane_pluginsTable = new JScrollPane();
			scrollPane_pluginsTable.setViewportView(getTable_pluginsTable());
		}
		return scrollPane_pluginsTable;
	}

	/**
	 * @return label_title_second
	 */
	private JLabel getLabel_title_second()
	{
		if (label_title_second == null)
		{
			label_title_second = new JLabel("Please, select the ones that you want to export.");
		}
		return label_title_second;
	}

	/**
	 * @return label_exportMode
	 */
	private JLabel getLabel_exportMode()
	{
		if (label_exportMode == null)
		{
			label_exportMode = new JLabel("How do you want to export plugins?");
		}
		return label_exportMode;
	}

	/**
	 * @return radioButton_exportMode_fileByFile
	 */
	private JRadioButton getRadioButton_exportMode_fileByFile()
	{
		if (radioButton_exportMode_fileByFile == null)
		{
			radioButton_exportMode_fileByFile = new JRadioButton("File by file");
			radioButton_exportMode_fileByFile.setSelected(true);
			radioButton_exportMode_fileByFile.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent e)
				{
					getRadioButton_exportMode_zipped().setSelected(false);
					exportMode = EXPORT_MODE.FILE_BY_FILE;
				}
			});
		}
		return radioButton_exportMode_fileByFile;
	}

	/**
	 * @return radioButton_exportMode_zipped
	 */
	private JRadioButton getRadioButton_exportMode_zipped()
	{
		if (radioButton_exportMode_zipped == null)
		{
			radioButton_exportMode_zipped = new JRadioButton("Zipped");
			radioButton_exportMode_zipped.setEnabled(false);
			radioButton_exportMode_zipped.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent e)
				{
					getRadioButton_exportMode_fileByFile().setSelected(false);
					exportMode = EXPORT_MODE.ZIPPED;
				}
			});
		}
		return radioButton_exportMode_zipped;
	}

	/**
	 * @return label_targetDirectory
	 */
	private JLabel getLabel_targetDirectory()
	{
		if (label_targetDirectory == null)
		{
			label_targetDirectory = new JLabel("Please, select the targer directory:");
		}
		return label_targetDirectory;
	}

	/**
	 * @return textField_targetDirectory
	 */
	private JTextField getTextField_targetDirectory()
	{
		if (textField_targetDirectory == null)
		{
			textField_targetDirectory = new JTextField();
			textField_targetDirectory.setText("/Users/Vale/Documents/Sycamore");
		}
		return textField_targetDirectory;
	}

	/**
	 * @return button_open
	 */
	private JButton getButton_open()
	{
		if (button_open == null)
		{
			button_open = new JButton();
			button_open.setPreferredSize(new Dimension(29, 29));
			button_open.setMinimumSize(new Dimension(29, 29));
			button_open.setMaximumSize(new Dimension(29, 29));
			button_open.setIcon(new ImageIcon(getClass().getResource("/it/diunipi/volpi/sycamore/resources/open_16x16.png")));
			button_open.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent e)
				{
					JFileChooser fileChoser = new JFileChooser();
					fileChoser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

					int returnVal = fileChoser.showOpenDialog(SycamorePluginsExportingPanel.this);

					// set the workspace
					if (returnVal == JFileChooser.APPROVE_OPTION)
					{
						File file = fileChoser.getSelectedFile();
						String path = file.getAbsolutePath();

						getTextField_targetDirectory().setText(path);
					}
				}
			});
		}
		return button_open;
	}

	/**
	 * Returns the list of files to be exported
	 * 
	 * @return the files to be exported
	 */
	public File[] getFilesToExport()
	{
		int[] selectedRows = getTable_pluginsTable().getSelectedRows();
		PluginsFileTableModel model = (PluginsFileTableModel) getTable_pluginsTable().getModel();
		
		File[] files = new File[selectedRows.length];
		for (int i = 0; i < selectedRows.length; i++)
		{
			String path = (String) model.getValueAt(selectedRows[i], COLUMNS.FILE.ordinal());
			
			files[i] = new File(path);
		}
		
		return files;
	}

	/**
	 * Returns the path where to export plugins
	 * 
	 * @return the exporting path
	 */
	public String getExportingPath()
	{
		return getTextField_targetDirectory().getText();
	}
}
