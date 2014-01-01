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
public class ZombieProtocolSettingsPanel extends SycamorePanel
{
	private static final long	serialVersionUID	= 5640717044864824779L;
	private JPanel				panel_settings;
	private JLabel				label_minActivity;
	private JLabel				label_maxActivity;
	private JSpinner			spinner_minActivity;
	private JSpinner			spinner_maxActivity;
	private JPanel				panel_contents;

	/**
	 * Constructor.
	 */
	public ZombieProtocolSettingsPanel()
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
	private JLabel getLabel_minActivity()
	{
		if (label_minActivity == null)
		{
			label_minActivity = new JLabel("Min activity level:");
		}
		return label_minActivity;
	}

	/**
	 * 
	 * @return label_maxActivity
	 */
	private JLabel getLabel_maxActivity()
	{
		if (label_maxActivity == null)
		{
			label_maxActivity = new JLabel("Max activity level:");
		}
		return label_maxActivity;
	}

	/**
	 * 
	 * @return spinner_minActivity
	 */
	private JSpinner getSpinner_minActivity()
	{
		if (spinner_minActivity == null)
		{
			spinner_minActivity = new JSpinner();
			spinner_minActivity.setMaximumSize(new Dimension(80, 27));
			spinner_minActivity.setMinimumSize(new Dimension(80, 27));
			spinner_minActivity.setPreferredSize(new Dimension(80, 27));
			spinner_minActivity.setModel(new SpinnerNumberModel(0.0, -Double.MAX_VALUE, Double.MAX_VALUE, 1));
			spinner_minActivity.setValue(ZombieProtocol.getMinActivity());
			spinner_minActivity.addChangeListener(new ChangeListener()
			{
				@Override
				public void stateChanged(ChangeEvent arg0)
				{
					double value = (Double) spinner_minActivity.getValue();
					ZombieProtocol.setMinActivity((float) value);
				}
			});
		}
		return spinner_minActivity;
	}

	/**
	 * 
	 * @return spinner_MaxActivity
	 */
	private JSpinner getSpinner_MaxActivity()
	{
		if (spinner_maxActivity == null)
		{
			spinner_maxActivity = new JSpinner();
			spinner_maxActivity.setMaximumSize(new Dimension(80, 27));
			spinner_maxActivity.setMinimumSize(new Dimension(80, 27));
			spinner_maxActivity.setPreferredSize(new Dimension(80, 27));
			spinner_maxActivity.setModel(new SpinnerNumberModel(0.0, -Double.MAX_VALUE, Double.MAX_VALUE, 1));
			spinner_maxActivity.setValue(ZombieProtocol.getMaxActivity());
			spinner_maxActivity.addChangeListener(new ChangeListener()
			{
				@Override
				public void stateChanged(ChangeEvent arg0)
				{
					double value = (Double) spinner_maxActivity.getValue();
					ZombieProtocol.setMaxActivity((float) value);
				}
			});
		}
		return spinner_maxActivity;
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
			panel_contents.add(getLabel_minActivity(), gbc_label_minActivity);
			GridBagConstraints gbc_spinner_minActivity = new GridBagConstraints();
			gbc_spinner_minActivity.anchor = GridBagConstraints.WEST;
			gbc_spinner_minActivity.insets = new Insets(0, 0, 5, 0);
			gbc_spinner_minActivity.gridx = 1;
			gbc_spinner_minActivity.gridy = 0;
			panel_contents.add(getSpinner_minActivity(), gbc_spinner_minActivity);
			GridBagConstraints gbc_label_maxActivity = new GridBagConstraints();
			gbc_label_maxActivity.anchor = GridBagConstraints.EAST;
			gbc_label_maxActivity.insets = new Insets(0, 0, 0, 5);
			gbc_label_maxActivity.gridx = 0;
			gbc_label_maxActivity.gridy = 1;
			panel_contents.add(getLabel_maxActivity(), gbc_label_maxActivity);
			GridBagConstraints gbc_spinner_MaxActivity = new GridBagConstraints();
			gbc_spinner_MaxActivity.anchor = GridBagConstraints.WEST;
			gbc_spinner_MaxActivity.gridx = 1;
			gbc_spinner_MaxActivity.gridy = 1;
			panel_contents.add(getSpinner_MaxActivity(), gbc_spinner_MaxActivity);
		}
		return panel_contents;
	}
}
