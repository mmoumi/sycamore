/**
 * 
 */
package it.diunipi.volpi.sycamore.plugins.algorithms;

import it.diunipi.volpi.sycamore.engine.SycamoreEngine;
import it.diunipi.volpi.sycamore.gui.SycamorePanel;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * @author Vale
 * 
 */
public class SingleHumanProtocolSettingsPanel extends SycamorePanel
{
	private static final long	serialVersionUID	= 5640717044864824779L;
	private JPanel				panel_settings;
	private JLabel				label_radius;
	private JLabel				label_sides;
	private JSpinner			spinner_radius;
	private JSpinner			spinner_sides;
	private JPanel				panel_contents;

	/**
	 * Constructor.
	 */
	public SingleHumanProtocolSettingsPanel()
	{
		initialize();
	}

	/**
	 * Init GUI
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

	/**
	 * 
	 * @return panel_settings
	 */
	private JPanel getPanel_settings()
	{
		if (panel_settings == null)
		{
			panel_settings = new JPanel();
			panel_settings.setBorder(new TitledBorder(null, "Settings for Zombie protocol", TitledBorder.LEADING, TitledBorder.TOP, null, null));
			GridBagLayout gbl_panel_settings = new GridBagLayout();
			gbl_panel_settings.columnWidths = new int[]
			{ 0, 0 };
			gbl_panel_settings.rowHeights = new int[]
			{ 0, 0 };
			gbl_panel_settings.columnWeights = new double[]
			{ 1.0, Double.MIN_VALUE };
			gbl_panel_settings.rowWeights = new double[]
			{ 1.0, Double.MIN_VALUE };
			panel_settings.setLayout(gbl_panel_settings);
			GridBagConstraints gbc_panel_contents = new GridBagConstraints();
			gbc_panel_contents.fill = GridBagConstraints.HORIZONTAL;
			gbc_panel_contents.gridx = 0;
			gbc_panel_contents.gridy = 0;
			panel_settings.add(getPanel_contents(), gbc_panel_contents);
		}
		return panel_settings;
	}

	/**
	 * 
	 * @return label_minActivity
	 */
	private JLabel getLabel_radius()
	{
		if (label_radius == null)
		{
			label_radius = new JLabel("Radius:");
		}
		return label_radius;
	}

	/**
	 * 
	 * @return label_maxActivity
	 */
	private JLabel getLabel_sides()
	{
		if (label_sides == null)
		{
			label_sides = new JLabel("Number of sides:");
		}
		return label_sides;
	}

	/**
	 * 
	 * @return spinner_minActivity
	 */
	private JSpinner getSpinner_radius()
	{
		if (spinner_radius == null)
		{
			spinner_radius = new JSpinner();
			spinner_radius.setMaximumSize(new Dimension(80, 27));
			spinner_radius.setMinimumSize(new Dimension(80, 27));
			spinner_radius.setPreferredSize(new Dimension(80, 27));
			spinner_radius.setModel(new SpinnerNumberModel(0, 0, 9999, 1));
			spinner_radius.setValue(SingleHumanProtocol.getRadius());
			spinner_radius.addChangeListener(new ChangeListener()
			{
				@Override
				public void stateChanged(ChangeEvent arg0)
				{
					int value = (Integer) spinner_radius.getValue();
					SingleHumanProtocol.setRadius(value);
				}
			});
		}
		return spinner_radius;
	}

	/**
	 * 
	 * @return spinner_MaxActivity
	 */
	private JSpinner getSpinner_sides()
	{
		if (spinner_sides == null)
		{
			spinner_sides = new JSpinner();
			spinner_sides.setMaximumSize(new Dimension(80, 27));
			spinner_sides.setMinimumSize(new Dimension(80, 27));
			spinner_sides.setPreferredSize(new Dimension(80, 27));
			spinner_sides.setModel(new SpinnerNumberModel(0, 0, 9999, 1));
			spinner_sides.setValue(SingleHumanProtocol.getSides());
			spinner_sides.addChangeListener(new ChangeListener()
			{
				@Override
				public void stateChanged(ChangeEvent arg0)
				{
					int value = (Integer) spinner_sides.getValue();
					SingleHumanProtocol.setSides(value);
				}
			});
		}
		return spinner_sides;
	}

	/*
	 * returns panel_contents
	 */
	private JPanel getPanel_contents()
	{
		if (panel_contents == null)
		{
			panel_contents = new JPanel();
			GridBagLayout gbl_panel_contents = new GridBagLayout();
			gbl_panel_contents.columnWidths = new int[]
			{ 0, 0, 0 };
			gbl_panel_contents.rowHeights = new int[]
			{ 0, 0, 0 };
			gbl_panel_contents.columnWeights = new double[]
			{ 1.0, 1.0, Double.MIN_VALUE };
			gbl_panel_contents.rowWeights = new double[]
			{ 0.0, 0.0, Double.MIN_VALUE };
			panel_contents.setLayout(gbl_panel_contents);
			GridBagConstraints gbc_label_minActivity = new GridBagConstraints();
			gbc_label_minActivity.anchor = GridBagConstraints.EAST;
			gbc_label_minActivity.insets = new Insets(0, 0, 5, 5);
			gbc_label_minActivity.gridx = 0;
			gbc_label_minActivity.gridy = 0;
			panel_contents.add(getLabel_radius(), gbc_label_minActivity);
			GridBagConstraints gbc_spinner_minActivity = new GridBagConstraints();
			gbc_spinner_minActivity.anchor = GridBagConstraints.WEST;
			gbc_spinner_minActivity.insets = new Insets(0, 0, 5, 0);
			gbc_spinner_minActivity.gridx = 1;
			gbc_spinner_minActivity.gridy = 0;
			panel_contents.add(getSpinner_radius(), gbc_spinner_minActivity);
			GridBagConstraints gbc_label_maxActivity = new GridBagConstraints();
			gbc_label_maxActivity.anchor = GridBagConstraints.EAST;
			gbc_label_maxActivity.insets = new Insets(0, 0, 0, 5);
			gbc_label_maxActivity.gridx = 0;
			gbc_label_maxActivity.gridy = 1;
			panel_contents.add(getLabel_sides(), gbc_label_maxActivity);
			GridBagConstraints gbc_spinner_MaxActivity = new GridBagConstraints();
			gbc_spinner_MaxActivity.anchor = GridBagConstraints.WEST;
			gbc_spinner_MaxActivity.gridx = 1;
			gbc_spinner_MaxActivity.gridy = 1;
			panel_contents.add(getSpinner_sides(), gbc_spinner_MaxActivity);
		}
		return panel_contents;
	}
}
