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
 * The settings panel for <code>AbsoluteAgreement3D</code> plugin
 * 
 * @author Valerio Volpi - vale.v@me.com
 */
public class AbsoluteAgreement3DSettingsPanel extends AgreementSettingsPanel
{
	private static final long	serialVersionUID		= 7587684962080577106L;
	private JPanel				panel_settings			= null;
	private JLabel				label_translation_x		= null;
	private JLabel				label_translation_y		= null;
	private JSpinner			spinner_translation_x	= null;
	private JSpinner			spinner_translation_y	= null;
	private JLabel				label_scale_x			= null;
	private JLabel				label_scale_y			= null;
	private JSpinner			spinner_scale_x			= null;
	private JSpinner			spinner_scale_y			= null;
	private JLabel				label_rotation_x		= null;
	private JSpinner			spinner_rotation_x		= null;
	private JPanel				panel_contents			= null;
	private JLabel				label_translation_z		= null;
	private JSpinner			spinner_translation_z	= null;
	private JLabel				label_scale_z			= null;
	private JLabel				label_rotation_y		= null;
	private JLabel				label_rotation_z		= null;
	private JSpinner			spinner_scale_z			= null;
	private JSpinner			spinner_rotation_y		= null;
	private JSpinner			spinner_rotation_z		= null;

	private double				storeScaleX				= 0;
	private double				storeScaleY				= 0;
	private double				storeScaleZ				= 0;

	/**
	 * Default constructor.
	 */
	public AbsoluteAgreement3DSettingsPanel()
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
		super.updateGui();

		// if the measure unit is fixed, the scale factor is 1 and teh related controls are
		// disabled
		if (AgreementImpl.isFixMeasureUnit())
		{
			// save scale values
			storeScaleX = AbsoluteAgreement3D.getScaleX();
			storeScaleY = AbsoluteAgreement3D.getScaleY();
			storeScaleZ = AbsoluteAgreement3D.getScaleZ();

			// set scale to 1
			getSpinner_scale_x().setValue(new Double(1.0));
			getSpinner_scale_y().setValue(new Double(1.0));
			getSpinner_scale_z().setValue(new Double(1.0));

			// disable controls
			getSpinner_scale_x().setEnabled(false);
			getSpinner_scale_y().setEnabled(false);
			getSpinner_scale_z().setEnabled(false);
		}
		else
		{
			// sets scale to its value
			getSpinner_scale_x().setValue(storeScaleX);
			getSpinner_scale_y().setValue(storeScaleY);
			getSpinner_scale_z().setValue(storeScaleZ);

			// enable controls
			getSpinner_scale_x().setEnabled(true);
			getSpinner_scale_y().setEnabled(true);
			getSpinner_scale_z().setEnabled(true);
		}
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
	 * @return label_translation_x
	 */
	private JLabel getLabel_translation_x()
	{
		if (label_translation_x == null)
		{
			label_translation_x = new JLabel("Translation X:");
		}
		return label_translation_x;
	}

	/**
	 * @return label_translation_y
	 */
	private JLabel getLabel_translation_y()
	{
		if (label_translation_y == null)
		{
			label_translation_y = new JLabel("Translation Y:");
		}
		return label_translation_y;
	}

	/**
	 * @return
	 */
	private JLabel getLabel_translation_z()
	{
		if (label_translation_z == null)
		{
			label_translation_z = new JLabel("Translation Z:");
		}
		return label_translation_z;
	}

	/**
	 * @return spinner_translation_x
	 */
	private JSpinner getSpinner_translation_x()
	{
		if (spinner_translation_x == null)
		{
			spinner_translation_x = new JSpinner();
			spinner_translation_x.setMaximumSize(new Dimension(80, 27));
			spinner_translation_x.setMinimumSize(new Dimension(80, 27));
			spinner_translation_x.setPreferredSize(new Dimension(80, 27));
			spinner_translation_x.setModel(new SpinnerNumberModel(0.0, -Double.MAX_VALUE, Double.MAX_VALUE, 0.1d));
			spinner_translation_x.setValue(AbsoluteAgreement3D.getTranslationX());
			spinner_translation_x.addChangeListener(new ChangeListener()
			{
				@Override
				public void stateChanged(ChangeEvent e)
				{
					AbsoluteAgreement3D.setTranslationX((Double) spinner_translation_x.getValue());
					fireActionEvent(new ActionEvent(AbsoluteAgreement3DSettingsPanel.this, 0, SycamoreFiredActionEvents.UPDATE_AGREEMENTS_GRAPHICS.name()));
				}
			});
		}
		return spinner_translation_x;
	}

	/**
	 * @return spinner_translation_y
	 */
	private JSpinner getSpinner_translation_y()
	{
		if (spinner_translation_y == null)
		{
			spinner_translation_y = new JSpinner();
			spinner_translation_y.setMaximumSize(new Dimension(80, 27));
			spinner_translation_y.setMinimumSize(new Dimension(80, 27));
			spinner_translation_y.setPreferredSize(new Dimension(80, 27));
			spinner_translation_y.setModel(new SpinnerNumberModel(0.0, -Double.MAX_VALUE, Double.MAX_VALUE, 0.1d));
			spinner_translation_y.setValue(AbsoluteAgreement3D.getTranslationY());
			spinner_translation_y.addChangeListener(new ChangeListener()
			{
				@Override
				public void stateChanged(ChangeEvent e)
				{
					AbsoluteAgreement3D.setTranslationY((Double) spinner_translation_y.getValue());
					fireActionEvent(new ActionEvent(AbsoluteAgreement3DSettingsPanel.this, 0, SycamoreFiredActionEvents.UPDATE_AGREEMENTS_GRAPHICS.name()));
				}
			});
		}
		return spinner_translation_y;
	}

	/**
	 * @return
	 */
	private JSpinner getSpinner_translation_z()
	{
		if (spinner_translation_z == null)
		{
			spinner_translation_z = new JSpinner();
			spinner_translation_z.setMaximumSize(new Dimension(80, 27));
			spinner_translation_z.setMinimumSize(new Dimension(80, 27));
			spinner_translation_z.setPreferredSize(new Dimension(80, 27));
			spinner_translation_z.setModel(new SpinnerNumberModel(0.0, -Double.MAX_VALUE, Double.MAX_VALUE, 0.1d));
			spinner_translation_z.setValue(AbsoluteAgreement3D.getTranslationZ());
			spinner_translation_z.addChangeListener(new ChangeListener()
			{
				@Override
				public void stateChanged(ChangeEvent e)
				{
					AbsoluteAgreement3D.setTranslationZ((Double) spinner_translation_z.getValue());
					fireActionEvent(new ActionEvent(AbsoluteAgreement3DSettingsPanel.this, 0, SycamoreFiredActionEvents.UPDATE_AGREEMENTS_GRAPHICS.name()));
				}
			});
		}
		return spinner_translation_z;
	}

	/**
	 * @return label_scale_x
	 */
	private JLabel getLabel_scale_x()
	{
		if (label_scale_x == null)
		{
			label_scale_x = new JLabel("Scale X:");
		}
		return label_scale_x;
	}

	/**
	 * @return label_scale_y
	 */
	private JLabel getLabel_scale_y()
	{
		if (label_scale_y == null)
		{
			label_scale_y = new JLabel("Scale Y:");
		}
		return label_scale_y;
	}

	/**
	 * @return
	 */
	private JLabel getLabel_scale_z()
	{
		if (label_scale_z == null)
		{
			label_scale_z = new JLabel("Scale Z:");
		}
		return label_scale_z;
	}

	/**
	 * @return spinner_scale_x
	 */
	private JSpinner getSpinner_scale_x()
	{
		if (spinner_scale_x == null)
		{
			spinner_scale_x = new JSpinner();
			spinner_scale_x.setMaximumSize(new Dimension(80, 27));
			spinner_scale_x.setMinimumSize(new Dimension(80, 27));
			spinner_scale_x.setPreferredSize(new Dimension(80, 27));
			spinner_scale_x.setModel(new SpinnerNumberModel(0.0, -Double.MAX_VALUE, Double.MAX_VALUE, 0.1d));
			spinner_scale_x.setValue(AgreementImpl.isFixMeasureUnit() ? 1.0 : AbsoluteAgreement3D.getScaleX());
			spinner_scale_x.setEnabled(!AgreementImpl.isFixMeasureUnit());
			spinner_scale_x.addChangeListener(new ChangeListener()
			{
				@Override
				public void stateChanged(ChangeEvent e)
				{
					AbsoluteAgreement3D.setScaleX((Double) spinner_scale_x.getValue());
					fireActionEvent(new ActionEvent(AbsoluteAgreement3DSettingsPanel.this, 0, SycamoreFiredActionEvents.UPDATE_AGREEMENTS_GRAPHICS.name()));
				}
			});
		}
		return spinner_scale_x;
	}

	/**
	 * @return spinner_scale_y
	 */
	private JSpinner getSpinner_scale_y()
	{
		if (spinner_scale_y == null)
		{
			spinner_scale_y = new JSpinner();
			spinner_scale_y.setMaximumSize(new Dimension(80, 27));
			spinner_scale_y.setMinimumSize(new Dimension(80, 27));
			spinner_scale_y.setPreferredSize(new Dimension(80, 27));
			spinner_scale_y.setModel(new SpinnerNumberModel(0.0, -Double.MAX_VALUE, Double.MAX_VALUE, 0.1d));
			spinner_scale_y.setValue(AgreementImpl.isFixMeasureUnit() ? 1.0 : AbsoluteAgreement3D.getScaleY());
			spinner_scale_y.setEnabled(!AgreementImpl.isFixMeasureUnit());
			spinner_scale_y.addChangeListener(new ChangeListener()
			{
				@Override
				public void stateChanged(ChangeEvent e)
				{
					AbsoluteAgreement3D.setScaleY((Double) spinner_scale_y.getValue());
					fireActionEvent(new ActionEvent(AbsoluteAgreement3DSettingsPanel.this, 0, SycamoreFiredActionEvents.UPDATE_AGREEMENTS_GRAPHICS.name()));
				}
			});
		}
		return spinner_scale_y;
	}

	/**
	 * @return
	 */
	private JSpinner getSpinner_scale_z()
	{
		if (spinner_scale_z == null)
		{
			spinner_scale_z = new JSpinner();
			spinner_scale_z.setMaximumSize(new Dimension(80, 27));
			spinner_scale_z.setMinimumSize(new Dimension(80, 27));
			spinner_scale_z.setPreferredSize(new Dimension(80, 27));
			spinner_scale_z.setModel(new SpinnerNumberModel(0.0, -Double.MAX_VALUE, Double.MAX_VALUE, 0.1d));
			spinner_scale_z.setValue(AgreementImpl.isFixMeasureUnit() ? 1.0 : AbsoluteAgreement3D.getScaleZ());
			spinner_scale_z.setEnabled(!AgreementImpl.isFixMeasureUnit());
			spinner_scale_z.addChangeListener(new ChangeListener()
			{
				@Override
				public void stateChanged(ChangeEvent e)
				{
					AbsoluteAgreement3D.setScaleZ((Double) spinner_scale_z.getValue());
					fireActionEvent(new ActionEvent(AbsoluteAgreement3DSettingsPanel.this, 0, SycamoreFiredActionEvents.UPDATE_AGREEMENTS_GRAPHICS.name()));
				}
			});
		}
		return spinner_scale_z;
	}

	/**
	 * @return label_rotation
	 */
	private JLabel getLabel_rotation_x()
	{
		if (label_rotation_x == null)
		{
			label_rotation_x = new JLabel("Rotation on X (degrees):");
		}
		return label_rotation_x;
	}

	/**
	 * @return
	 */
	private JLabel getLabel_rotation_y()
	{
		if (label_rotation_y == null)
		{
			label_rotation_y = new JLabel("Rotation on Y (degrees):");
		}
		return label_rotation_y;
	}

	/**
	 * @return
	 */
	private JLabel getLabel_rotation_z()
	{
		if (label_rotation_z == null)
		{
			label_rotation_z = new JLabel("Rotation on Z (degrees):");
		}
		return label_rotation_z;
	}

	/**
	 * @return spinner_rotation
	 */
	private JSpinner getSpinner_rotation_x()
	{
		if (spinner_rotation_x == null)
		{
			spinner_rotation_x = new JSpinner();
			spinner_rotation_x.setMaximumSize(new Dimension(80, 27));
			spinner_rotation_x.setMinimumSize(new Dimension(80, 27));
			spinner_rotation_x.setPreferredSize(new Dimension(80, 27));
			spinner_rotation_x.setModel(new SpinnerNumberModel(0.0, -Double.MAX_VALUE, Double.MAX_VALUE, 1));
			spinner_rotation_x.setValue(AbsoluteAgreement3D.getRotationX());
			spinner_rotation_x.addChangeListener(new ChangeListener()
			{
				@Override
				public void stateChanged(ChangeEvent e)
				{
					AbsoluteAgreement3D.setRotationX((Double) spinner_rotation_x.getValue());
					fireActionEvent(new ActionEvent(AbsoluteAgreement3DSettingsPanel.this, 0, SycamoreFiredActionEvents.UPDATE_AGREEMENTS_GRAPHICS.name()));
				}
			});
		}
		return spinner_rotation_x;
	}

	/**
	 * @return
	 */
	private JSpinner getSpinner_rotation_y()
	{
		if (spinner_rotation_y == null)
		{
			spinner_rotation_y = new JSpinner();
			spinner_rotation_y.setMaximumSize(new Dimension(80, 27));
			spinner_rotation_y.setMinimumSize(new Dimension(80, 27));
			spinner_rotation_y.setPreferredSize(new Dimension(80, 27));
			spinner_rotation_y.setModel(new SpinnerNumberModel(0.0, -Double.MAX_VALUE, Double.MAX_VALUE, 1));
			spinner_rotation_y.setValue(AbsoluteAgreement3D.getRotationY());
			spinner_rotation_y.addChangeListener(new ChangeListener()
			{
				@Override
				public void stateChanged(ChangeEvent e)
				{
					AbsoluteAgreement3D.setRotationY((Double) spinner_rotation_y.getValue());
					fireActionEvent(new ActionEvent(AbsoluteAgreement3DSettingsPanel.this, 0, SycamoreFiredActionEvents.UPDATE_AGREEMENTS_GRAPHICS.name()));
				}
			});
		}
		return spinner_rotation_y;
	}

	/**
	 * @return
	 */
	private JSpinner getSpinner_rotation_z()
	{
		if (spinner_rotation_z == null)
		{
			spinner_rotation_z = new JSpinner();
			spinner_rotation_z.setMaximumSize(new Dimension(80, 27));
			spinner_rotation_z.setMinimumSize(new Dimension(80, 27));
			spinner_rotation_z.setPreferredSize(new Dimension(80, 27));
			spinner_rotation_z.setModel(new SpinnerNumberModel(0.0, -Double.MAX_VALUE, Double.MAX_VALUE, 1));
			spinner_rotation_z.setValue(AbsoluteAgreement3D.getRotationZ());
			spinner_rotation_z.addChangeListener(new ChangeListener()
			{
				@Override
				public void stateChanged(ChangeEvent e)
				{
					AbsoluteAgreement3D.setRotationZ((Double) spinner_rotation_z.getValue());
					fireActionEvent(new ActionEvent(AbsoluteAgreement3DSettingsPanel.this, 0, SycamoreFiredActionEvents.UPDATE_AGREEMENTS_GRAPHICS.name()));
				}
			});
		}
		return spinner_rotation_z;
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
			{ 0, 0, 0, 0, 0, 0, 0 };
			gbl_panel_contents.rowHeights = new int[]
			{ 0, 0, 0, 0 };
			gbl_panel_contents.columnWeights = new double[]
			{ 0.0, 1.0, 1.0, 1.0, 1.0, 1.0, Double.MIN_VALUE };
			gbl_panel_contents.rowWeights = new double[]
			{ 0.0, 0.0, 0.0, Double.MIN_VALUE };
			panel_contents.setLayout(gbl_panel_contents);
			GridBagConstraints gbc_label_translation_x = new GridBagConstraints();
			gbc_label_translation_x.anchor = GridBagConstraints.EAST;
			gbc_label_translation_x.insets = new Insets(0, 0, 5, 5);
			gbc_label_translation_x.gridx = 0;
			gbc_label_translation_x.gridy = 0;
			panel_contents.add(getLabel_translation_x(), gbc_label_translation_x);
			GridBagConstraints gbc_spinner_translation_x = new GridBagConstraints();
			gbc_spinner_translation_x.anchor = GridBagConstraints.WEST;
			gbc_spinner_translation_x.insets = new Insets(0, 0, 5, 5);
			gbc_spinner_translation_x.gridx = 1;
			gbc_spinner_translation_x.gridy = 0;
			panel_contents.add(getSpinner_translation_x(), gbc_spinner_translation_x);
			GridBagConstraints gbc_label_scale_x = new GridBagConstraints();
			gbc_label_scale_x.anchor = GridBagConstraints.EAST;
			gbc_label_scale_x.insets = new Insets(0, 0, 5, 5);
			gbc_label_scale_x.gridx = 2;
			gbc_label_scale_x.gridy = 0;
			panel_contents.add(getLabel_scale_x(), gbc_label_scale_x);
			GridBagConstraints gbc_spinner_scale_x = new GridBagConstraints();
			gbc_spinner_scale_x.anchor = GridBagConstraints.WEST;
			gbc_spinner_scale_x.insets = new Insets(0, 0, 5, 5);
			gbc_spinner_scale_x.gridx = 3;
			gbc_spinner_scale_x.gridy = 0;
			panel_contents.add(getSpinner_scale_x(), gbc_spinner_scale_x);
			GridBagConstraints gbc_label_rotation_x = new GridBagConstraints();
			gbc_label_rotation_x.anchor = GridBagConstraints.EAST;
			gbc_label_rotation_x.insets = new Insets(0, 0, 5, 5);
			gbc_label_rotation_x.gridx = 4;
			gbc_label_rotation_x.gridy = 0;
			panel_contents.add(getLabel_rotation_x(), gbc_label_rotation_x);
			GridBagConstraints gbc_spinner_rotation_x = new GridBagConstraints();
			gbc_spinner_rotation_x.insets = new Insets(0, 0, 5, 0);
			gbc_spinner_rotation_x.anchor = GridBagConstraints.WEST;
			gbc_spinner_rotation_x.gridx = 5;
			gbc_spinner_rotation_x.gridy = 0;
			panel_contents.add(getSpinner_rotation_x(), gbc_spinner_rotation_x);
			GridBagConstraints gbc_label_translation_y = new GridBagConstraints();
			gbc_label_translation_y.anchor = GridBagConstraints.EAST;
			gbc_label_translation_y.insets = new Insets(0, 0, 5, 5);
			gbc_label_translation_y.gridx = 0;
			gbc_label_translation_y.gridy = 1;
			panel_contents.add(getLabel_translation_y(), gbc_label_translation_y);
			GridBagConstraints gbc_spinner_translation_y = new GridBagConstraints();
			gbc_spinner_translation_y.anchor = GridBagConstraints.WEST;
			gbc_spinner_translation_y.insets = new Insets(0, 0, 5, 5);
			gbc_spinner_translation_y.gridx = 1;
			gbc_spinner_translation_y.gridy = 1;
			panel_contents.add(getSpinner_translation_y(), gbc_spinner_translation_y);
			GridBagConstraints gbc_label_scale_y = new GridBagConstraints();
			gbc_label_scale_y.anchor = GridBagConstraints.EAST;
			gbc_label_scale_y.insets = new Insets(0, 0, 5, 5);
			gbc_label_scale_y.gridx = 2;
			gbc_label_scale_y.gridy = 1;
			panel_contents.add(getLabel_scale_y(), gbc_label_scale_y);
			GridBagConstraints gbc_spinner_scale_y = new GridBagConstraints();
			gbc_spinner_scale_y.anchor = GridBagConstraints.WEST;
			gbc_spinner_scale_y.insets = new Insets(0, 0, 5, 5);
			gbc_spinner_scale_y.gridx = 3;
			gbc_spinner_scale_y.gridy = 1;
			panel_contents.add(getSpinner_scale_y(), gbc_spinner_scale_y);
			GridBagConstraints gbc_label_rotation_y = new GridBagConstraints();
			gbc_label_rotation_y.anchor = GridBagConstraints.EAST;
			gbc_label_rotation_y.insets = new Insets(0, 0, 5, 5);
			gbc_label_rotation_y.gridx = 4;
			gbc_label_rotation_y.gridy = 1;
			panel_contents.add(getLabel_rotation_y(), gbc_label_rotation_y);
			GridBagConstraints gbc_spinner_rotation_y = new GridBagConstraints();
			gbc_spinner_rotation_y.anchor = GridBagConstraints.WEST;
			gbc_spinner_rotation_y.insets = new Insets(0, 0, 5, 0);
			gbc_spinner_rotation_y.gridx = 5;
			gbc_spinner_rotation_y.gridy = 1;
			panel_contents.add(getSpinner_rotation_y(), gbc_spinner_rotation_y);
			GridBagConstraints gbc_label_translation_z = new GridBagConstraints();
			gbc_label_translation_z.anchor = GridBagConstraints.EAST;
			gbc_label_translation_z.insets = new Insets(0, 0, 0, 5);
			gbc_label_translation_z.gridx = 0;
			gbc_label_translation_z.gridy = 2;
			panel_contents.add(getLabel_translation_z(), gbc_label_translation_z);
			GridBagConstraints gbc_spinner_translation_z = new GridBagConstraints();
			gbc_spinner_translation_z.anchor = GridBagConstraints.WEST;
			gbc_spinner_translation_z.insets = new Insets(0, 0, 0, 5);
			gbc_spinner_translation_z.gridx = 1;
			gbc_spinner_translation_z.gridy = 2;
			panel_contents.add(getSpinner_translation_z(), gbc_spinner_translation_z);
			GridBagConstraints gbc_label_scale_z = new GridBagConstraints();
			gbc_label_scale_z.anchor = GridBagConstraints.EAST;
			gbc_label_scale_z.insets = new Insets(0, 0, 0, 5);
			gbc_label_scale_z.gridx = 2;
			gbc_label_scale_z.gridy = 2;
			panel_contents.add(getLabel_scale_z(), gbc_label_scale_z);
			GridBagConstraints gbc_spinner_scale_z = new GridBagConstraints();
			gbc_spinner_scale_z.anchor = GridBagConstraints.WEST;
			gbc_spinner_scale_z.insets = new Insets(0, 0, 0, 5);
			gbc_spinner_scale_z.gridx = 3;
			gbc_spinner_scale_z.gridy = 2;
			panel_contents.add(getSpinner_scale_z(), gbc_spinner_scale_z);
			GridBagConstraints gbc_label_rotation_z = new GridBagConstraints();
			gbc_label_rotation_z.anchor = GridBagConstraints.EAST;
			gbc_label_rotation_z.insets = new Insets(0, 0, 0, 5);
			gbc_label_rotation_z.gridx = 4;
			gbc_label_rotation_z.gridy = 2;
			panel_contents.add(getLabel_rotation_z(), gbc_label_rotation_z);
			GridBagConstraints gbc_spinner_rotation_z = new GridBagConstraints();
			gbc_spinner_rotation_z.anchor = GridBagConstraints.WEST;
			gbc_spinner_rotation_z.gridx = 5;
			gbc_spinner_rotation_z.gridy = 2;
			panel_contents.add(getSpinner_rotation_z(), gbc_spinner_rotation_z);
		}
		return panel_contents;
	}
}
