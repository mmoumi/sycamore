package it.diunipi.volpi.sycamore.gui;

import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JLabel;
import javax.swing.JPanel;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Font;
import java.awt.Component;
import javax.swing.SwingConstants;

/**
 * A rounded borders panel with title.
 * 
 * reference:
 * http://www.codeproject.com/Articles/114959/Rounded-Border-JPanel-JPanel-graphics-improvements
 * 
 * @author Valerio Volpi - vale.v@me.com
 */
public class SycamoreTitledRoundedBorderPanel extends SycamoreRoundedBorderPanel
{
	private static final long	serialVersionUID		= -184882594049663948L;
	private boolean				titleVisible			= true;
	private JLabel				label_title				= null;
	private JPanel				panel_contentContainer	= null;

	/**
	 * Constructor.
	 */
	public SycamoreTitledRoundedBorderPanel(boolean titleVisible, boolean shadowVisible)
	{
		this.titleVisible = titleVisible;
		this.setShadowVisible(shadowVisible);

		initialize();
	}

	/**
	 * Default constructor
	 */
	public SycamoreTitledRoundedBorderPanel()
	{
		this(true, true);
	}

	/**
	 * Init GUI
	 */
	private void initialize()
	{
		setOpaque(false);
		setBackground(Color.DARK_GRAY);

		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]
		{ 0, 0 };
		gridBagLayout.rowHeights = new int[]
		{ 0, 0, 0 };
		gridBagLayout.columnWeights = new double[]
		{ 1.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[]
		{ 0.0, 1.0, Double.MIN_VALUE };
		setLayout(gridBagLayout);

		if (isTitleVisible())
		{
			GridBagConstraints gbc_label_title = new GridBagConstraints();
			gbc_label_title.fill = GridBagConstraints.HORIZONTAL;
			gbc_label_title.gridx = 0;
			gbc_label_title.gridy = 0;
			add(getLabel_title(), gbc_label_title);
		}

		GridBagConstraints gbc_panel_contentContainer = new GridBagConstraints();
		gbc_panel_contentContainer.fill = GridBagConstraints.BOTH;
		gbc_panel_contentContainer.gridx = 0;
		gbc_panel_contentContainer.gridy = 1;
		add(getPanel_contentContainer(), gbc_panel_contentContainer);
	}

	/**
	 * @return the label_title
	 */
	public JLabel getLabel_title()
	{
		if (label_title == null)
		{
			label_title = new JLabel();
			label_title.setMinimumSize(new Dimension(45, 25));
			label_title.setMaximumSize(new Dimension(45, 25));
			label_title.setHorizontalAlignment(SwingConstants.CENTER);
			label_title.setAlignmentX(Component.CENTER_ALIGNMENT);
			label_title.setPreferredSize(new Dimension(45, 25));
			label_title.setForeground(Color.DARK_GRAY);
			label_title.setBackground(new Color(0.79f, 0.79f, 0.79f));
			label_title.setOpaque(true);
			label_title.setFont(new Font("Lucida Grande", Font.BOLD, 14));
		}
		return label_title;
	}

	/**
	 * @return the panel_contentContainer
	 */
	public JPanel getPanel_contentContainer()
	{
		if (panel_contentContainer == null)
		{
			panel_contentContainer = new JPanel();
			panel_contentContainer.setBackground(Color.WHITE);
		}
		return panel_contentContainer;
	}

	/**
	 * @return the titleVisible
	 */
	public boolean isTitleVisible()
	{
		return titleVisible;
	}

	/**
	 * @param titleVisible
	 */
	public void setTitleVisible(boolean titleVisible)
	{
		this.titleVisible = titleVisible;
	}
}