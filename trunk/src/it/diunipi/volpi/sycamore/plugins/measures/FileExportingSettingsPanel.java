/**
 * 
 */
package it.diunipi.volpi.sycamore.plugins.measures;

import it.diunipi.volpi.sycamore.engine.SycamoreEngine;
import it.diunipi.volpi.sycamore.gui.SycamorePanel;
import it.diunipi.volpi.sycamore.util.ApplicationProperties;
import it.diunipi.volpi.sycamore.util.PropertyManager;
import it.diunipi.volpi.sycamore.util.SycamoreProperty;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

/**
 * 
 * @author Valerio Volpi - vale.v@me.com
 */
public class FileExportingSettingsPanel extends SycamorePanel
{
	/**
	 * Properties related to the file exporting plugins
	 * 
	 * @author Valerio Volpi - vale.v@me.com
	 */
	public static enum FileExportingProperties implements SycamoreProperty
	{
		OUTPUT_FILE_PATH("Output file path", ApplicationProperties.WORKSPACE_DIR.getDefaultValue() + System.getProperty("file.separator") + "Reports");

		private String	description		= null;
		private String	defaultValue	= null;

		/**
		 * Constructor.
		 */
		FileExportingProperties(String description, String defaultValue)
		{
			this.description = description;
			this.defaultValue = defaultValue;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see it.diunipi.volpi.sycamore.util.SycamoreProperty#getDescription()
		 */
		@Override
		public String getDescription()
		{
			return description;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see it.diunipi.volpi.sycamore.util.SycamoreProperty#getDefaultValue()
		 */
		@Override
		public String getDefaultValue()
		{
			return defaultValue;
		}
	}
	
	private static final long	serialVersionUID		= -8159817537321449729L;
	private JPanel				panel_settings			= null;
	private JLabel				label_outputPath		= null;
	private JTextField			textField_outputPath	= null;
	private JButton				button_open				= null;

	/**
	 * Default constructor.
	 */
	public FileExportingSettingsPanel()
	{
		initialize();
	}

	/**
	 * Init Gui
	 */
	private void initialize()
	{
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWeights = new double[]
		{ 1.0 };
		gridBagLayout.rowWeights = new double[]
		{ 1.0 };
		setLayout(gridBagLayout);

		GridBagConstraints gbc_panel_settings = new GridBagConstraints();
		gbc_panel_settings.fill = GridBagConstraints.BOTH;
		gbc_panel_settings.gridx = 0;
		gbc_panel_settings.gridy = 0;
		add(getPanel_settings(), gbc_panel_settings);
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

	private JPanel getPanel_settings()
	{
		if (panel_settings == null)
		{
			panel_settings = new JPanel();
			panel_settings.setBorder(new TitledBorder(null, "Paths", TitledBorder.LEADING, TitledBorder.TOP, null, null));
			GridBagLayout gbl_panel_settings = new GridBagLayout();
			gbl_panel_settings.columnWidths = new int[]
			{ 0, 0, 0 };
			gbl_panel_settings.rowHeights = new int[]
			{ 0, 0, 0 };
			gbl_panel_settings.columnWeights = new double[]
			{ 1.0, 0.0, Double.MIN_VALUE };
			gbl_panel_settings.rowWeights = new double[]
			{ 1.0, 1.0, Double.MIN_VALUE };
			panel_settings.setLayout(gbl_panel_settings);
			GridBagConstraints gbc_label_outputPath = new GridBagConstraints();
			gbc_label_outputPath.gridwidth = 2;
			gbc_label_outputPath.anchor = GridBagConstraints.SOUTH;
			gbc_label_outputPath.insets = new Insets(2, 2, 2, 2);
			gbc_label_outputPath.gridx = 0;
			gbc_label_outputPath.gridy = 0;
			panel_settings.add(getLabel_outputPath(), gbc_label_outputPath);
			GridBagConstraints gbc_textField_outputPath = new GridBagConstraints();
			gbc_textField_outputPath.anchor = GridBagConstraints.NORTH;
			gbc_textField_outputPath.insets = new Insets(2, 2, 2, 2);
			gbc_textField_outputPath.fill = GridBagConstraints.HORIZONTAL;
			gbc_textField_outputPath.gridx = 0;
			gbc_textField_outputPath.gridy = 1;
			panel_settings.add(getTextField_outputPath(), gbc_textField_outputPath);
			GridBagConstraints gbc_button_open = new GridBagConstraints();
			gbc_button_open.insets = new Insets(2, 2, 2, 2);
			gbc_button_open.anchor = GridBagConstraints.NORTH;
			gbc_button_open.gridx = 1;
			gbc_button_open.gridy = 1;
			panel_settings.add(getButton_open(), gbc_button_open);
		}
		return panel_settings;
	}

	/**
	 * @return label_outputPath
	 */
	private JLabel getLabel_outputPath()
	{
		if (label_outputPath == null)
		{
			label_outputPath = new JLabel("Output file path:");
		}
		return label_outputPath;
	}

	/**
	 * @return textField_outputPath
	 */
	private JTextField getTextField_outputPath()
	{
		if (textField_outputPath == null)
		{
			final String path = FileExportingProperties.OUTPUT_FILE_PATH.getDefaultValue();
			
			textField_outputPath = new JTextField();
			textField_outputPath.setText(path);
			textField_outputPath.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent arg0)
				{
					PropertyManager.getSharedInstance().putProperty(FileExportingProperties.OUTPUT_FILE_PATH, path);
				}
			});
		}
		return textField_outputPath;
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

					int returnVal = fileChoser.showOpenDialog(FileExportingSettingsPanel.this);

					// set the workspace
					if (returnVal == JFileChooser.APPROVE_OPTION)
					{
						File file = fileChoser.getSelectedFile();
						String path = file.getAbsolutePath();

						getTextField_outputPath().setText(path);

						PropertyManager.getSharedInstance().putProperty(FileExportingProperties.OUTPUT_FILE_PATH, path);
					}
				}
			});
		}
		return button_open;
	}
}
