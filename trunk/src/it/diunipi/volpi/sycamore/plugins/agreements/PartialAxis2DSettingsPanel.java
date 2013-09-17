/**
 * 
 */
package it.diunipi.volpi.sycamore.plugins.agreements;

import it.diunipi.volpi.sycamore.engine.SycamoreEngine;
import it.diunipi.volpi.sycamore.util.SycamoreFiredActionEvents;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * @author Vale
 * 
 */
public class PartialAxis2DSettingsPanel extends AgreementSettingsPanel
{
	private static final long	serialVersionUID	= 7587684962080577106L;
	private JPanel				panel_settings		= null;
	private JLabel				label_rotation		= null;
	private JSpinner			spinner_rotation	= null;
	private JPanel				panel_contents		= null;

	/**
	 * Default constructor.
	 */
	public PartialAxis2DSettingsPanel()
	{
		initialize();
	}

	/**
	 * Init Gui
	 */
	private void initialize()
	{
		GridBagConstraints gbc_panel_settings = new GridBagConstraints();
		gbc_panel_settings.fill = GridBagConstraints.BOTH;
		gbc_panel_settings.gridx = 0;
		gbc_panel_settings.gridy = 1;
		panel_container.add(getPanel_settings(), gbc_panel_settings);
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
	 * @return panel_settings
	 */
	private JPanel getPanel_settings()
	{
		if (panel_settings == null)
		{
			panel_settings = new JPanel();
			panel_settings.setBorder(BorderFactory.createTitledBorder("Local coordinate system settings"));
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
	 * @return label_rotation
	 */
	private JLabel getLabel_rotation()
	{
		if (label_rotation == null)
		{
			label_rotation = new JLabel("Rotation angle (degrees):");
		}
		return label_rotation;
	}

	/**
	 * @return spinner_rotation
	 */
	private JSpinner getSpinner_rotation()
	{
		if (spinner_rotation == null)
		{
			spinner_rotation = new JSpinner();
			spinner_rotation.setMaximumSize(new Dimension(80, 27));
			spinner_rotation.setMinimumSize(new Dimension(80, 27));
			spinner_rotation.setPreferredSize(new Dimension(80, 27));
			spinner_rotation.setModel(new SpinnerNumberModel(0.0, -Double.MAX_VALUE, Double.MAX_VALUE, 1));
			spinner_rotation.setValue(PartialAxis2D.getRotation());
			spinner_rotation.addChangeListener(new ChangeListener()
			{
				@Override
				public void stateChanged(ChangeEvent e)
				{
					PartialAxis2D.setRotation((Double) spinner_rotation.getValue());
					fireActionEvent(new ActionEvent(PartialAxis2DSettingsPanel.this, 0, SycamoreFiredActionEvents.UPDATE_AGREEMENTS_GRAPHICS.name()));
				}
			});
		}
		return spinner_rotation;
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
			{ 0, 0, 0 };
			gbl_panel_contents.rowHeights = new int[]
			{ 0, 0 };
			gbl_panel_contents.columnWeights = new double[]
			{ 1.0, 1.0, Double.MIN_VALUE };
			gbl_panel_contents.rowWeights = new double[]
			{ 0.0, Double.MIN_VALUE };
			panel_contents.setLayout(gbl_panel_contents);
			GridBagConstraints gbc_label_rotation = new GridBagConstraints();
			gbc_label_rotation.anchor = GridBagConstraints.EAST;
			gbc_label_rotation.insets = new Insets(2, 2, 2, 5);
			gbc_label_rotation.gridx = 0;
			gbc_label_rotation.gridy = 0;
			panel_contents.add(getLabel_rotation(), gbc_label_rotation);
			GridBagConstraints gbc_spinner_rotation = new GridBagConstraints();
			gbc_spinner_rotation.insets = new Insets(2, 2, 2, 2);
			gbc_spinner_rotation.anchor = GridBagConstraints.WEST;
			gbc_spinner_rotation.gridx = 1;
			gbc_spinner_rotation.gridy = 0;
			panel_contents.add(getSpinner_rotation(), gbc_spinner_rotation);
		}
		return panel_contents;
	}
}
