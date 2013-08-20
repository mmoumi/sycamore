/**
 * 
 */
package it.diunipi.volpi.sycamore.plugins.visibilities;

import it.diunipi.volpi.sycamore.engine.SycamoreEngine;
import it.diunipi.volpi.sycamore.gui.SycamorePanel;

import java.awt.GridBagLayout;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import java.awt.Insets;
import java.awt.Dimension;
import java.awt.Font;

/**
 * @author Vale
 * 
 */
public class VisibilitySettingsPanel extends SycamorePanel
{
	private static final long	serialVersionUID		= -7346984457971790162L;

	private SycamoreEngine		appEngine				= null;
	private JLabel				label_visibilityRange	= null;
	private JSlider				slider_visibilityRange	= null;
	private JPanel				panel_size				= null;

	/**
	 * Default constructor
	 */
	public VisibilitySettingsPanel()
	{
		initialize();
	}

	/**
	 * Init Gui
	 */
	private void initialize()
	{
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]
		{ 0, 0 };
		gridBagLayout.rowHeights = new int[]
		{ 0, 0 };
		gridBagLayout.columnWeights = new double[]
		{ 1.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[]
		{ 1.0, Double.MIN_VALUE };
		setLayout(gridBagLayout);
		GridBagConstraints gbc_panel_size = new GridBagConstraints();
		gbc_panel_size.insets = new Insets(2, 2, 2, 2);
		gbc_panel_size.fill = GridBagConstraints.BOTH;
		gbc_panel_size.gridx = 0;
		gbc_panel_size.gridy = 0;
		add(getPanel_size(), gbc_panel_size);
	}

	/**
	 * @return
	 */
	private JLabel getLabel_visibilityRange()
	{
		if (label_visibilityRange == null)
		{
			label_visibilityRange = new JLabel("Visibility range size (global coordinates units): " + VisibilityImpl.getVisibilityRange());
			label_visibilityRange.setFont(label_visibilityRange.getFont().deriveFont(label_visibilityRange.getFont().getStyle() | Font.BOLD));
		}
		return label_visibilityRange;
	}

	/**
	 * @return
	 */
	private JSlider getSlider_visibilityRange()
	{
		if (slider_visibilityRange == null)
		{
			slider_visibilityRange = new JSlider(0, 200);
			slider_visibilityRange.setMinimumSize(new Dimension(430, 40));
			slider_visibilityRange.setPreferredSize(new Dimension(430, 40));
			slider_visibilityRange.setPaintTicks(true);
			slider_visibilityRange.setPaintLabels(true);
			slider_visibilityRange.setMajorTickSpacing(50);
			slider_visibilityRange.setMinorTickSpacing(10);
			slider_visibilityRange.setValue((int) VisibilityImpl.getVisibilityRange());
			slider_visibilityRange.addChangeListener(new ChangeListener()
			{
				@Override
				public void stateChanged(ChangeEvent e)
				{
					float value = slider_visibilityRange.getValue();
					VisibilityImpl.setVisibilityRange(value);
					getLabel_visibilityRange().setText("Visibility range size (global coordinates units): " + VisibilityImpl.getVisibilityRange());

					if (appEngine != null)
					{
						appEngine.updateVisibilityRange();
					}
				}
			});
		}
		return slider_visibilityRange;
	}

	/**
	 * @return
	 */
	private JPanel getPanel_size()
	{
		if (panel_size == null)
		{
			panel_size = new JPanel();
			panel_size.setBorder(BorderFactory.createTitledBorder("Visibility range settings"));

			GridBagLayout gbl_panel_size = new GridBagLayout();
			gbl_panel_size.columnWidths = new int[]
			{ 98, 0, 0 };
			gbl_panel_size.rowHeights = new int[]
			{ 16, 29, 0 };
			gbl_panel_size.columnWeights = new double[]
			{ 1.0, 0.0, Double.MIN_VALUE };
			gbl_panel_size.rowWeights = new double[]
			{ 0.0, 1.0, Double.MIN_VALUE };
			panel_size.setLayout(gbl_panel_size);
			GridBagConstraints gbc_label_visibilityRange = new GridBagConstraints();
			gbc_label_visibilityRange.insets = new Insets(10, 2, 5, 5);
			gbc_label_visibilityRange.gridx = 0;
			gbc_label_visibilityRange.gridy = 0;
			panel_size.add(getLabel_visibilityRange(), gbc_label_visibilityRange);
			GridBagConstraints gbc_slider_visibilityRange = new GridBagConstraints();
			gbc_slider_visibilityRange.gridwidth = 2;
			gbc_slider_visibilityRange.insets = new Insets(2, 2, 2, 5);
			gbc_slider_visibilityRange.gridx = 0;
			gbc_slider_visibilityRange.gridy = 1;
			panel_size.add(getSlider_visibilityRange(), gbc_slider_visibilityRange);
		}
		return panel_size;
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
		this.appEngine = appEngine;
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
}
