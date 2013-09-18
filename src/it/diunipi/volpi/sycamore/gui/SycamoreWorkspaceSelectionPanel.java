/**
 * 
 */
package it.diunipi.volpi.sycamore.gui;

import it.diunipi.volpi.sycamore.util.ApplicationProperties;
import it.diunipi.volpi.sycamore.util.PropertyManager;

import java.awt.Dimension;
import java.awt.Font;
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
import javax.swing.JTextPane;

/**
 * The panel for the selction of the workspace
 * 
 * @author Valerio Volpi - vale.v@me.com
 */
public class SycamoreWorkspaceSelectionPanel extends JPanel
{
	private static final long	serialVersionUID		= 4328957063146457493L;
	private JLabel				label_title				= null;
	private JTextPane			text_description		= null;
	private JLabel				label_workspaceDir		= null;
	private JTextField			textField_workspaceDir	= null;
	private JButton				button_open				= null;

	/**
	 * Default constructor
	 */
	public SycamoreWorkspaceSelectionPanel()
	{
		initialize();
	}

	/**
	 * Init gui
	 */
	private void initialize()
	{
		setPreferredSize(new Dimension(620, 230));
		setMinimumSize(new Dimension(620, 230));
		setMaximumSize(new Dimension(620, 230));

		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]
		{ 0, 0 };
		gridBagLayout.rowHeights = new int[]
		{ 0, 0, 0, 0, 0 };
		gridBagLayout.columnWeights = new double[]
		{ 1.0, 0.0 };
		gridBagLayout.rowWeights = new double[]
		{ 0.0, 1.0, 0.0, 0.0, Double.MIN_VALUE };
		this.setLayout(gridBagLayout);
		GridBagConstraints gbc_label_title = new GridBagConstraints();
		gbc_label_title.gridwidth = 2;
		gbc_label_title.insets = new Insets(10, 2, 10, 2);
		gbc_label_title.gridx = 0;
		gbc_label_title.gridy = 0;
		this.add(getLabel_title(), gbc_label_title);
		GridBagConstraints gbc_text_description = new GridBagConstraints();
		gbc_text_description.gridwidth = 2;
		gbc_text_description.insets = new Insets(2, 2, 2, 2);
		gbc_text_description.fill = GridBagConstraints.BOTH;
		gbc_text_description.gridx = 0;
		gbc_text_description.gridy = 1;
		this.add(getText_description(), gbc_text_description);
		GridBagConstraints gbc_label_workspaceDir = new GridBagConstraints();
		gbc_label_workspaceDir.gridwidth = 2;
		gbc_label_workspaceDir.insets = new Insets(2, 2, 2, 2);
		gbc_label_workspaceDir.gridx = 0;
		gbc_label_workspaceDir.gridy = 2;
		this.add(getLabel_workspaceDir(), gbc_label_workspaceDir);
		GridBagConstraints gbc_textField_workspaceDir = new GridBagConstraints();
		gbc_textField_workspaceDir.insets = new Insets(2, 2, 2, 2);
		gbc_textField_workspaceDir.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_workspaceDir.gridx = 0;
		gbc_textField_workspaceDir.gridy = 3;
		this.add(getTextField_workspaceDir(), gbc_textField_workspaceDir);
		GridBagConstraints gbc_button_open = new GridBagConstraints();
		gbc_button_open.insets = new Insets(2, 2, 2, 2);
		gbc_button_open.gridx = 1;
		gbc_button_open.gridy = 3;
		this.add(getButton_open(), gbc_button_open);
	}

	/**
	 * @return label_title
	 */
	private JLabel getLabel_title()
	{
		if (label_title == null)
		{
			label_title = new JLabel("Welcome to Sycamore!");
			label_title.setFont(label_title.getFont().deriveFont(label_title.getFont().getStyle() | Font.BOLD, label_title.getFont().getSize() + 1f));
		}
		return label_title;
	}

	/**
	 * @return text_description
	 */
	private JTextPane getText_description()
	{
		if (text_description == null)
		{
			text_description = new JTextPane();
			text_description.setMargin(new Insets(2, 2, 2, 2));
			text_description.setOpaque(false);
			text_description
					.setText("Before starting, it is required that you choose a workspace directory. The workspace is the place where the files that Sycamore needs to work are stored. The workspace contains plugins, preferences and more inportant files. You will be able to change the workspace at any time by using the \"Switch workspace\" menu.");
		}
		return text_description;
	}

	/**
	 * @return label_workspaceDir
	 */
	private JLabel getLabel_workspaceDir()
	{
		if (label_workspaceDir == null)
		{
			label_workspaceDir = new JLabel("Workspace dir:");
		}
		return label_workspaceDir;
	}

	/**
	 * @return textField_workspaceDir
	 */
	private JTextField getTextField_workspaceDir()
	{
		if (textField_workspaceDir == null)
		{
			textField_workspaceDir = new JTextField();
			String path = ApplicationProperties.WORKSPACE_DIR.getDefaultValue();
			textField_workspaceDir.setText(path);

			PropertyManager.getSharedInstance().putProperty(ApplicationProperties.WORKSPACE_DIR, path);
		}
		return textField_workspaceDir;
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

					int returnVal = fileChoser.showOpenDialog(SycamoreWorkspaceSelectionPanel.this);

					// set the workspace
					if (returnVal == JFileChooser.APPROVE_OPTION)
					{
						File file = fileChoser.getSelectedFile();
						String path = file.getAbsolutePath();

						getTextField_workspaceDir().setText(path);

						PropertyManager.getSharedInstance().putProperty(ApplicationProperties.WORKSPACE_DIR, path);
					}
				}
			});
		}
		return button_open;
	}
}
