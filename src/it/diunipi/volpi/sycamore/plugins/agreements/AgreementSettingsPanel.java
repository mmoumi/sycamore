/**
 * 
 */
package it.diunipi.volpi.sycamore.plugins.agreements;

import it.diunipi.volpi.sycamore.engine.SycamoreEngine;
import it.diunipi.volpi.sycamore.gui.SwitchToggle;
import it.diunipi.volpi.sycamore.gui.SycamorePanel;
import it.diunipi.volpi.sycamore.util.SycamoreFiredActionEvents;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * @author Vale
 * 
 */
public class AgreementSettingsPanel extends SycamorePanel
{
	private static final long	serialVersionUID			= 7587684962080577106L;
	private JPanel				panel_generalSettings		= null;
	private JPanel				panel_contents				= null;
	private JLabel				label_fixMeasureUnit		= null;
	private SwitchToggle		switchToggle_fixMeasureUnit	= null;
	protected JPanel			panel_container				= null;

	/**
	 * Default constructor.
	 */
	public AgreementSettingsPanel()
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
		GridBagConstraints gbc_panel_container = new GridBagConstraints();
		gbc_panel_container.fill = GridBagConstraints.BOTH;
		gbc_panel_container.gridx = 0;
		gbc_panel_container.gridy = 0;
		add(getPanel_container(), gbc_panel_container);
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
	 * @return panel_settings
	 */
	private JPanel getPanel_generalSettings()
	{
		if (panel_generalSettings == null)
		{
			panel_generalSettings = new JPanel();
			panel_generalSettings.setBorder(BorderFactory.createTitledBorder("General settings"));
			GridBagLayout gbl_panel_generalSettings = new GridBagLayout();
			gbl_panel_generalSettings.columnWidths = new int[]
			{ 0, 0 };
			gbl_panel_generalSettings.rowHeights = new int[]
			{ 0, 0 };
			gbl_panel_generalSettings.columnWeights = new double[]
			{ 1.0, Double.MIN_VALUE };
			gbl_panel_generalSettings.rowWeights = new double[]
			{ 1.0, Double.MIN_VALUE };
			panel_generalSettings.setLayout(gbl_panel_generalSettings);
			GridBagConstraints gbc_panel_contents = new GridBagConstraints();
			gbc_panel_contents.fill = GridBagConstraints.HORIZONTAL;
			gbc_panel_contents.gridx = 0;
			gbc_panel_contents.gridy = 0;
			panel_generalSettings.add(getPanel_contents(), gbc_panel_contents);
		}
		return panel_generalSettings;
	}

	/**
	 * @return panel_contents
	 */
	private JPanel getPanel_contents()
	{
		if (panel_contents == null)
		{
			panel_contents = new JPanel();
			GridBagLayout gbl_panel_contents = new GridBagLayout();
			gbl_panel_contents.columnWidths = new int[]
			{ 0, 0 };
			gbl_panel_contents.rowHeights = new int[]
			{ 0, 0 };
			gbl_panel_contents.columnWeights = new double[]
			{ 1.0, 1.0 };
			gbl_panel_contents.rowWeights = new double[]
			{ 0.0, Double.MIN_VALUE };
			panel_contents.setLayout(gbl_panel_contents);
			GridBagConstraints gbc_label_fixMeasureUnit = new GridBagConstraints();
			gbc_label_fixMeasureUnit.anchor = GridBagConstraints.EAST;
			gbc_label_fixMeasureUnit.insets = new Insets(2, 2, 2, 2);
			gbc_label_fixMeasureUnit.gridx = 0;
			gbc_label_fixMeasureUnit.gridy = 0;
			panel_contents.add(getLabel_fixMeasureUnit(), gbc_label_fixMeasureUnit);
			GridBagConstraints gbc_switchToggle_fixMeasureUnit = new GridBagConstraints();
			gbc_switchToggle_fixMeasureUnit.anchor = GridBagConstraints.WEST;
			gbc_switchToggle_fixMeasureUnit.insets = new Insets(2, 2, 2, 2);
			gbc_switchToggle_fixMeasureUnit.gridx = 1;
			gbc_switchToggle_fixMeasureUnit.gridy = 0;
			panel_contents.add(getSwitchToggle_fixMeasureUnit(), gbc_switchToggle_fixMeasureUnit);
		}
		return panel_contents;
	}

	/**
	 * @return
	 */
	private JLabel getLabel_fixMeasureUnit()
	{
		if (label_fixMeasureUnit == null)
		{
			label_fixMeasureUnit = new JLabel("Fix measure unit");
			label_fixMeasureUnit.setToolTipText("Make all the robots in the system have the same measure unit.");
		}
		return label_fixMeasureUnit;
	}

	/**
	 * @return
	 */
	private SwitchToggle getSwitchToggle_fixMeasureUnit()
	{
		if (switchToggle_fixMeasureUnit == null)
		{
			switchToggle_fixMeasureUnit = new SwitchToggle();
			switchToggle_fixMeasureUnit.setMaximumSize(new Dimension(71, 25));
			switchToggle_fixMeasureUnit.setMinimumSize(new Dimension(71, 25));
			switchToggle_fixMeasureUnit.setPreferredSize(new Dimension(71, 25));
			switchToggle_fixMeasureUnit.setSelected(AgreementImpl.isFixMeasureUnit());
			switchToggle_fixMeasureUnit.setToolTipText("Make all the robots in the system have the same measure unit.");
			switchToggle_fixMeasureUnit.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent e)
				{
					boolean selected = switchToggle_fixMeasureUnit.isSelected();
					AgreementImpl.setFixMeasureUnit(selected);
					fireActionEvent(new ActionEvent(AgreementSettingsPanel.this, 0, SycamoreFiredActionEvents.UPDATE_AGREEMENTS_GRAPHICS.name()));

					updateGui();
				}
			});
		}
		return switchToggle_fixMeasureUnit;
	}

	/**
	 * @return
	 */
	private JPanel getPanel_container()
	{
		if (panel_container == null)
		{
			panel_container = new JPanel();
			GridBagLayout gbl_panel_container = new GridBagLayout();
			gbl_panel_container.columnWidths = new int[]
			{ 0 };
			gbl_panel_container.rowHeights = new int[]
			{ 0, 0 };
			gbl_panel_container.columnWeights = new double[]
			{ 1.0 };
			gbl_panel_container.rowWeights = new double[]
			{ 1.0, 1.0 };
			panel_container.setLayout(gbl_panel_container);
			GridBagConstraints gbc_panel_generalSettings = new GridBagConstraints();
			gbc_panel_generalSettings.insets = new Insets(0, 0, 5, 0);
			gbc_panel_generalSettings.fill = GridBagConstraints.BOTH;
			gbc_panel_generalSettings.gridx = 0;
			gbc_panel_generalSettings.gridy = 0;
			panel_container.add(getPanel_generalSettings(), gbc_panel_generalSettings);
		}
		return panel_container;
	}
}
