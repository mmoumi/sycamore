/**
 * 
 */
package it.diunipi.volpi.sycamore.gui;

import it.diunipi.volpi.sycamore.engine.SycamoreEngine;
import it.diunipi.volpi.sycamore.plugins.SycamorePlugin;
import it.diunipi.volpi.sycamore.plugins.algorithms.Algorithm;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.WindowConstants;
import javax.swing.text.DefaultCaret;

import org.icepdf.ri.common.ComponentKeyBinding;
import org.icepdf.ri.common.SwingController;
import org.icepdf.ri.common.views.DocumentViewController;
import org.icepdf.ri.common.views.DocumentViewControllerImpl;

/**
 * A panel that displays informations about a plugin, and eventually the plugins's settng panel.
 * 
 * @author Valerio Volpi - vale.v@me.com
 */
public class SycamorePluginInformationsPanel extends SycamorePanel
{
	private static final long		serialVersionUID			= 1575372130256223294L;
	private final SycamorePlugin	plugin;

	private String					paperPath					= null;

	private JLabel					label_pluginName			= null;
	private JTextPane				textPane_longDescription	= null;
	private JLabel					label_settings				= null;
	private JPanel					panel_settingsContainer		= null;
	private JScrollPane				scrollPane_description		= null;
	private JLabel					label_author				= null;
	private JLabel					label_pluginAuthor			= null;
	private JLabel					label_references			= null;
	private JLabel					label_pluginReferences		= null;
	private JLabel					label_description			= null;
	private JLabel					label_aricle				= null;
	private JButton					button_viewArticle			= null;

	/**
	 * Default constructor
	 */
	public SycamorePluginInformationsPanel(SycamorePlugin plugin)
	{
		this.plugin = plugin;
		initialize();

		if (plugin != null)
		{
			// set up gui elements
			getLabel_pluginName().setText(plugin.getPluginName() + " - " + plugin.getPluginClassLongDescription());
			getLabel_settings().setText("Settings for plugin: " + plugin.getPluginName());
			getTextPane_longDescription().setText(plugin.getPluginLongDescription());
			getLabel_pluginAuthor().setText(plugin.getAuthor());

			if (plugin instanceof Algorithm)
			{
				// setup also references and paper
				Algorithm algorithm = (Algorithm) plugin;

				getLabel_references().setVisible(true);
				getLabel_pluginReferences().setVisible(true);
				getLabel_aricle().setVisible(true);
				getButton_viewArticle().setVisible(true);

				// fix the null references case
				String references = algorithm.getReferences();
				if (references == null)
				{
					references = "none";
				}
				getLabel_pluginReferences().setText(references);

				// fix the null paper case
				paperPath = algorithm.getPaperFilePath();
				if (paperPath == null)
				{
					getButton_viewArticle().setEnabled(false);
				}
				else
				{
					getButton_viewArticle().setEnabled(true);
				}

			}
			else
			{
				// hide references and paper
				getLabel_references().setVisible(false);
				getLabel_pluginReferences().setVisible(false);
				getLabel_aricle().setVisible(false);
				getButton_viewArticle().setVisible(false);
			}

			if (plugin.getPanel_settings() != null)
			{
				GridBagConstraints gbc_panel = new GridBagConstraints();
				gbc_panel.fill = GridBagConstraints.BOTH;
				gbc_panel.gridx = 0;
				gbc_panel.gridy = 0;

				SycamorePanel settingPanel = plugin.getPanel_settings();
				getPanel_settingsContainer().add(settingPanel, gbc_panel);
			}
			else
			{
				getLabel_settings().setVisible(false);
				getPanel_settingsContainer().setVisible(false);
			}
		}
	}

	/**
	 * Init Gui
	 */
	private void initialize()
	{
		setPreferredSize(new Dimension(600, 400));

		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]
		{ 0, 0 };
		gridBagLayout.rowHeights = new int[]
		{ 0, 0, 0, 0, 0, 0, 0, 0, 0 };
		gridBagLayout.columnWeights = new double[]
		{ 0.0, 1.0 };
		gridBagLayout.rowWeights = new double[]
		{ 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, Double.MIN_VALUE };
		setLayout(gridBagLayout);

		GridBagConstraints gbc_label_pluginName = new GridBagConstraints();
		gbc_label_pluginName.gridwidth = 2;
		gbc_label_pluginName.anchor = GridBagConstraints.WEST;
		gbc_label_pluginName.insets = new Insets(2, 2, 2, 2);
		gbc_label_pluginName.gridx = 0;
		gbc_label_pluginName.gridy = 0;
		add(getLabel_pluginName(), gbc_label_pluginName);
		GridBagConstraints gbc_label_author = new GridBagConstraints();
		gbc_label_author.anchor = GridBagConstraints.WEST;
		gbc_label_author.insets = new Insets(2, 2, 2, 2);
		gbc_label_author.gridx = 0;
		gbc_label_author.gridy = 1;
		add(getLabel_author(), gbc_label_author);
		GridBagConstraints gbc_label_pluginAuthor = new GridBagConstraints();
		gbc_label_pluginAuthor.anchor = GridBagConstraints.WEST;
		gbc_label_pluginAuthor.insets = new Insets(2, 2, 2, 2);
		gbc_label_pluginAuthor.gridx = 1;
		gbc_label_pluginAuthor.gridy = 1;
		add(getLabel_pluginAuthor(), gbc_label_pluginAuthor);
		GridBagConstraints gbc_label_references = new GridBagConstraints();
		gbc_label_references.anchor = GridBagConstraints.WEST;
		gbc_label_references.insets = new Insets(2, 2, 2, 2);
		gbc_label_references.gridx = 0;
		gbc_label_references.gridy = 2;
		add(getLabel_references(), gbc_label_references);
		GridBagConstraints gbc_label_pluginReferences = new GridBagConstraints();
		gbc_label_pluginReferences.anchor = GridBagConstraints.WEST;
		gbc_label_pluginReferences.insets = new Insets(2, 2, 2, 2);
		gbc_label_pluginReferences.gridx = 1;
		gbc_label_pluginReferences.gridy = 2;
		add(getLabel_pluginReferences(), gbc_label_pluginReferences);
		GridBagConstraints gbc_label_aricle = new GridBagConstraints();
		gbc_label_aricle.anchor = GridBagConstraints.WEST;
		gbc_label_aricle.insets = new Insets(2, 2, 2, 2);
		gbc_label_aricle.gridx = 0;
		gbc_label_aricle.gridy = 3;
		add(getLabel_aricle(), gbc_label_aricle);
		GridBagConstraints gbc_button_viewArticle = new GridBagConstraints();
		gbc_button_viewArticle.anchor = GridBagConstraints.WEST;
		gbc_button_viewArticle.insets = new Insets(2, 2, 2, 2);
		gbc_button_viewArticle.gridx = 1;
		gbc_button_viewArticle.gridy = 3;
		add(getButton_viewArticle(), gbc_button_viewArticle);
		GridBagConstraints gbc_label_description = new GridBagConstraints();
		gbc_label_description.anchor = GridBagConstraints.WEST;
		gbc_label_description.gridwidth = 2;
		gbc_label_description.insets = new Insets(20, 2, 2, 2);
		gbc_label_description.gridx = 0;
		gbc_label_description.gridy = 4;
		add(getLabel_description(), gbc_label_description);
		GridBagConstraints gbc_scrollPane_description = new GridBagConstraints();
		gbc_scrollPane_description.gridwidth = 2;
		gbc_scrollPane_description.fill = GridBagConstraints.BOTH;
		gbc_scrollPane_description.insets = new Insets(2, 2, 2, 2);
		gbc_scrollPane_description.gridx = 0;
		gbc_scrollPane_description.gridy = 5;
		add(getScrollPane_description(), gbc_scrollPane_description);

		GridBagConstraints gbc_label_settings = new GridBagConstraints();
		gbc_label_settings.gridwidth = 2;
		gbc_label_settings.anchor = GridBagConstraints.WEST;
		gbc_label_settings.insets = new Insets(2, 2, 2, 2);
		gbc_label_settings.gridx = 0;
		gbc_label_settings.gridy = 6;
		add(getLabel_settings(), gbc_label_settings);

		GridBagConstraints gbc_panel_settingsContainer = new GridBagConstraints();
		gbc_panel_settingsContainer.gridwidth = 2;
		gbc_panel_settingsContainer.fill = GridBagConstraints.BOTH;
		gbc_panel_settingsContainer.gridx = 0;
		gbc_panel_settingsContainer.gridy = 7;
		add(getPanel_settingsContainer(), gbc_panel_settingsContainer);
	}

	/**
	 * @return the label_description
	 */
	private JLabel getLabel_pluginName()
	{
		if (label_pluginName == null)
		{
			label_pluginName = new JLabel("<plugin name>");
			label_pluginName.setFont(label_pluginName.getFont().deriveFont(label_pluginName.getFont().getStyle() | Font.BOLD));
		}
		return label_pluginName;
	}

	/**
	 * @return the textPane_longDescription
	 */
	private JTextPane getTextPane_longDescription()
	{
		if (textPane_longDescription == null)
		{
			textPane_longDescription = new JTextPane();
			textPane_longDescription.setMargin(new Insets(2, 2, 2, 2));
			textPane_longDescription.setOpaque(false);
			textPane_longDescription.setEditable(false);
			textPane_longDescription.setPreferredSize(new Dimension(30, 200));
			textPane_longDescription.setText("<long description>");
		}
		return textPane_longDescription;
	}

	/**
	 * @return the label_settings
	 */
	private JLabel getLabel_settings()
	{
		if (label_settings == null)
		{
			label_settings = new JLabel("Settings for plugin: ");
			label_settings.setFont(label_settings.getFont().deriveFont(label_settings.getFont().getStyle() | Font.BOLD));
		}
		return label_settings;
	}

	/**
	 * @return the panel_settingsContainer
	 */
	private JPanel getPanel_settingsContainer()
	{
		if (panel_settingsContainer == null)
		{
			panel_settingsContainer = new JPanel();
			GridBagLayout gbl_panel_settingsContainer = new GridBagLayout();
			gbl_panel_settingsContainer.columnWidths = new int[]
			{ 0, 0 };
			gbl_panel_settingsContainer.rowHeights = new int[]
			{ 0, 0 };
			gbl_panel_settingsContainer.columnWeights = new double[]
			{ 1.0, Double.MIN_VALUE };
			gbl_panel_settingsContainer.rowWeights = new double[]
			{ 1.0, Double.MIN_VALUE };
			panel_settingsContainer.setLayout(gbl_panel_settingsContainer);
		}
		return panel_settingsContainer;
	}

	/**
	 * @return
	 */
	private JScrollPane getScrollPane_description()
	{
		if (scrollPane_description == null)
		{
			scrollPane_description = new JScrollPane();
			scrollPane_description.setOpaque(false);
			scrollPane_description.setPreferredSize(new Dimension(30, 200));
			scrollPane_description.setViewportView(getTextPane_longDescription());

			// scroll back to top
			DefaultCaret caret = (DefaultCaret) getTextPane_longDescription().getCaret();
			caret.setUpdatePolicy(DefaultCaret.NEVER_UPDATE);
		}
		return scrollPane_description;
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
		if (plugin.getPanel_settings() != null)
		{
			plugin.getPanel_settings().setAppEngine(appEngine);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.diunipi.volpi.sycamore.gui.SycamorePanel#updateGui()
	 */
	@Override
	public void updateGui()
	{
		if (plugin.getPanel_settings() != null)
		{
			plugin.getPanel_settings().updateGui();
		}
	}

	/**
	 * @return label_author
	 */
	private JLabel getLabel_author()
	{
		if (label_author == null)
		{
			label_author = new JLabel("Author:");
			label_author.setPreferredSize(new Dimension(90, 20));
			label_author.setFont(label_author.getFont().deriveFont(label_author.getFont().getStyle() | Font.BOLD));
		}
		return label_author;
	}

	/**
	 * @return label_pluginAuthor
	 */
	private JLabel getLabel_pluginAuthor()
	{
		if (label_pluginAuthor == null)
		{
			label_pluginAuthor = new JLabel("<author>");
		}
		return label_pluginAuthor;
	}

	/**
	 * @return label_references
	 */
	private JLabel getLabel_references()
	{
		if (label_references == null)
		{
			label_references = new JLabel("References:");
			label_references.setPreferredSize(new Dimension(90, 20));
			label_references.setFont(label_references.getFont().deriveFont(label_references.getFont().getStyle() | Font.BOLD));
		}
		return label_references;
	}

	/**
	 * @return label_pluginReferences
	 */
	private JLabel getLabel_pluginReferences()
	{
		if (label_pluginReferences == null)
		{
			label_pluginReferences = new JLabel("<references>");
		}
		return label_pluginReferences;
	}

	/**
	 * @return label_description
	 */
	private JLabel getLabel_description()
	{
		if (label_description == null)
		{
			label_description = new JLabel("Description:");
			label_description.setFont(label_description.getFont().deriveFont(label_description.getFont().getStyle() | Font.BOLD));
		}
		return label_description;
	}

	/**
	 * @return label_aricle
	 */
	private JLabel getLabel_aricle()
	{
		if (label_aricle == null)
		{
			label_aricle = new JLabel("Article:");
			label_aricle.setPreferredSize(new Dimension(90, 20));
			label_aricle.setFont(label_aricle.getFont().deriveFont(label_aricle.getFont().getStyle() | Font.BOLD));
		}
		return label_aricle;
	}

	/**
	 * @return button_viewArticle
	 */
	private JButton getButton_viewArticle()
	{
		if (button_viewArticle == null)
		{
			button_viewArticle = new JButton("Click to view");
			button_viewArticle.setFont(button_viewArticle.getFont().deriveFont(button_viewArticle.getFont().getSize() - 2f));
			button_viewArticle.setPreferredSize(new Dimension(125, 20));
			button_viewArticle.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent e)
				{
					// build a component controller
					SwingController controller = new SwingController();
					controller.setIsEmbeddedComponent(true);

					// set the viewController embeddable flag.
					DocumentViewController viewController = controller.getDocumentViewController();

					JPanel viewerComponentPanel = new JPanel();
					viewerComponentPanel.add(viewController.getViewContainer());

					// add copy keyboard command
					ComponentKeyBinding.install(controller, viewerComponentPanel);

					// add interactive mouse link annotation support via callback
					controller.getDocumentViewController().setAnnotationCallback(new org.icepdf.ri.common.MyAnnotationCallback(controller.getDocumentViewController()));

					// build a containing JFrame for display
					JFrame applicationFrame = new JFrame();
					applicationFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
					applicationFrame.getContentPane().add(viewerComponentPanel);

					// Now that the GUI is all in place, we can try opening a PDF
					controller.openDocument(paperPath);

					// hard set the page view to single page which effectively give a single
					// page view. This should be done after openDocument as it has code that
					// can change the view mode if specified by the file.
					controller.setPageViewMode(DocumentViewControllerImpl.ONE_PAGE_VIEW, false);
					controller.setPageFitMode(DocumentViewControllerImpl.PAGE_FIT_WINDOW_WIDTH, false);

					// show the component
					applicationFrame.pack();			
					applicationFrame.setSize(1024, 640);		
					applicationFrame.setVisible(true);
				}
			});
		}
		return button_viewArticle;
	}
}
