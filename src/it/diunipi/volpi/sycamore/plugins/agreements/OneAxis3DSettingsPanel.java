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
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
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
public class OneAxis3DSettingsPanel extends AgreementSettingsPanel
{
	private static final long	serialVersionUID	= 7587684962080577106L;
	private JPanel				panel_settings		= null;
	private JLabel				label_axis			= null;
	private JLabel				label_rotations		= null;
	private JSpinner			spinner_rotation_1	= null;
	private JPanel				panel_contents		= null;
	private JComboBox			comboBox_axis		= null;
	private JLabel				label_1				= null;
	private JSpinner			spinner_rotation_2	= null;
	private JLabel				label_2				= null;

	/**
	 * Default constructor.
	 */
	public OneAxis3DSettingsPanel()
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
	 * @return label_translation_y
	 */
	private JLabel getLabel_axis()
	{
		if (label_axis == null)
		{
			label_axis = new JLabel("Agreed axis:");
		}
		return label_axis;
	}

	/**
	 * @return label_rotation
	 */
	private JLabel getLabel_rotations()
	{
		if (label_rotations == null)
		{
			label_rotations = new JLabel("Rotation on the other 2 axes (degrees):");
		}
		return label_rotations;
	}

	/**
	 * @return spinner_rotation
	 */
	private JSpinner getSpinner_rotation_1()
	{
		if (spinner_rotation_1 == null)
		{
			spinner_rotation_1 = new JSpinner();
			spinner_rotation_1.setMaximumSize(new Dimension(80, 27));
			spinner_rotation_1.setMinimumSize(new Dimension(80, 27));
			spinner_rotation_1.setPreferredSize(new Dimension(80, 27));
			spinner_rotation_1.setModel(new SpinnerNumberModel(0.0, -Double.MAX_VALUE, Double.MAX_VALUE, 1));
			spinner_rotation_1.setValue(OneAxis3D.getRotation_1());
			spinner_rotation_1.addChangeListener(new ChangeListener()
			{
				@Override
				public void stateChanged(ChangeEvent e)
				{
					OneAxis3D.setRotation_1((Double) spinner_rotation_1.getValue());
					fireActionEvent(new ActionEvent(OneAxis3DSettingsPanel.this, 0, SycamoreFiredActionEvents.UPDATE_AGREEMENTS_GRAPHICS.name()));
				}
			});
		}
		return spinner_rotation_1;
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
			{ 0, 0, 0, 0, 0, 0 };
			gbl_panel_contents.rowHeights = new int[]
			{ 0, 0, 0 };
			gbl_panel_contents.columnWeights = new double[]
			{ 1.0, 1.0, 0.0, 0.0, 1.0, Double.MIN_VALUE };
			gbl_panel_contents.rowWeights = new double[]
			{ 0.0, 0.0, Double.MIN_VALUE };
			panel_contents.setLayout(gbl_panel_contents);
			GridBagConstraints gbc_label_axis = new GridBagConstraints();
			gbc_label_axis.insets = new Insets(2, 2, 2, 2);
			gbc_label_axis.gridx = 0;
			gbc_label_axis.gridy = 0;
			panel_contents.add(getLabel_axis(), gbc_label_axis);
			GridBagConstraints gbc_label_rotations = new GridBagConstraints();
			gbc_label_rotations.gridwidth = 4;
			gbc_label_rotations.insets = new Insets(2, 2, 2, 2);
			gbc_label_rotations.gridx = 1;
			gbc_label_rotations.gridy = 0;
			panel_contents.add(getLabel_rotations(), gbc_label_rotations);
			GridBagConstraints gbc_comboBox_axis = new GridBagConstraints();
			gbc_comboBox_axis.insets = new Insets(2, 2, 2, 2);
			gbc_comboBox_axis.fill = GridBagConstraints.HORIZONTAL;
			gbc_comboBox_axis.gridx = 0;
			gbc_comboBox_axis.gridy = 1;
			panel_contents.add(getComboBox_axis(), gbc_comboBox_axis);
			GridBagConstraints gbc_label_1 = new GridBagConstraints();
			gbc_label_1.anchor = GridBagConstraints.EAST;
			gbc_label_1.insets = new Insets(2, 2, 2, 2);
			gbc_label_1.gridx = 1;
			gbc_label_1.gridy = 1;
			panel_contents.add(getLabel_1(), gbc_label_1);
			GridBagConstraints gbc_spinner_rotation_1 = new GridBagConstraints();
			gbc_spinner_rotation_1.insets = new Insets(2, 2, 2, 20);
			gbc_spinner_rotation_1.anchor = GridBagConstraints.WEST;
			gbc_spinner_rotation_1.gridx = 2;
			gbc_spinner_rotation_1.gridy = 1;
			panel_contents.add(getSpinner_rotation_1(), gbc_spinner_rotation_1);
			GridBagConstraints gbc_label_2 = new GridBagConstraints();
			gbc_label_2.anchor = GridBagConstraints.EAST;
			gbc_label_2.insets = new Insets(2, 20, 2, 2);
			gbc_label_2.gridx = 3;
			gbc_label_2.gridy = 1;
			panel_contents.add(getLabel_2(), gbc_label_2);
			GridBagConstraints gbc_spinner_rotation_2 = new GridBagConstraints();
			gbc_spinner_rotation_2.insets = new Insets(2, 2, 2, 2);
			gbc_spinner_rotation_2.anchor = GridBagConstraints.WEST;
			gbc_spinner_rotation_2.gridx = 4;
			gbc_spinner_rotation_2.gridy = 1;
			panel_contents.add(getSpinner_rotation_2(), gbc_spinner_rotation_2);
		}
		return panel_contents;
	}

	/**
	 * @return
	 */
	private JComboBox getComboBox_axis()
	{
		if (comboBox_axis == null)
		{
			comboBox_axis = new JComboBox();
			comboBox_axis.setModel(new DefaultComboBoxModel(new String[]
			{ "X", "Y", "Z" }));
			comboBox_axis.setSelectedItem(OneAxis3D.getAxis());

			comboBox_axis.addActionListener(new ActionListener()
			{

				@Override
				public void actionPerformed(ActionEvent e)
				{
					OneAxis3D.setAxis((String) comboBox_axis.getSelectedItem());
					fireActionEvent(new ActionEvent(OneAxis3DSettingsPanel.this, 0, SycamoreFiredActionEvents.UPDATE_AGREEMENTS_GRAPHICS.name()));
				}
			});
		}
		return comboBox_axis;
	}

	/**
	 * @return
	 */
	private JLabel getLabel_1()
	{
		if (label_1 == null)
		{
			label_1 = new JLabel("1:");
		}
		return label_1;
	}

	/**
	 * @return
	 */
	private JSpinner getSpinner_rotation_2()
	{
		if (spinner_rotation_2 == null)
		{
			spinner_rotation_2 = new JSpinner();
			spinner_rotation_2.setPreferredSize(new Dimension(80, 27));
			spinner_rotation_2.setMinimumSize(new Dimension(80, 27));
			spinner_rotation_2.setMaximumSize(new Dimension(80, 27));
			spinner_rotation_2.setModel(new SpinnerNumberModel(0.0, -Double.MAX_VALUE, Double.MAX_VALUE, 1));
			spinner_rotation_2.setValue(OneAxis3D.getRotation_2());
			spinner_rotation_2.addChangeListener(new ChangeListener()
			{
				@Override
				public void stateChanged(ChangeEvent e)
				{
					OneAxis3D.setRotation_2((Double) spinner_rotation_2.getValue());
					fireActionEvent(new ActionEvent(OneAxis3DSettingsPanel.this, 0, SycamoreFiredActionEvents.UPDATE_AGREEMENTS_GRAPHICS.name()));
				}
			});
		}
		return spinner_rotation_2;
	}

	/**
	 * @return
	 */
	private JLabel getLabel_2()
	{
		if (label_2 == null)
		{
			label_2 = new JLabel("2:");
		}
		return label_2;
	}
}
