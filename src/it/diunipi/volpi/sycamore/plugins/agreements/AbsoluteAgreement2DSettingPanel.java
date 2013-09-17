/**
 * 
 */
package it.diunipi.volpi.sycamore.plugins.agreements;

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
public class AbsoluteAgreement2DSettingPanel extends AgreementSettingsPanel
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
	private JLabel				label_rotation			= null;
	private JSpinner			spinner_rotation		= null;
	private JPanel				panel_contents			= null;

	private double				storeScaleX				= 0;
	private double				storeScaleY				= 0;

	/**
	 * Default constructor.
	 */
	public AbsoluteAgreement2DSettingPanel()
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
	 * @see it.diunipi.volpi.sycamore.plugins.agreements.AgreementSettingsPanel#updateGui()
	 */
	@Override
	public void updateGui()
	{
		super.updateGui();

		if (AgreementImpl.isFixMeasureUnit())
		{
			storeScaleX = AbsoluteAgreement2D.getScaleX();
			storeScaleY = AbsoluteAgreement2D.getScaleY();
			
			getSpinner_scale_x().setValue(new Double(1.0));
			getSpinner_scale_y().setValue(new Double(1.0));

			getSpinner_scale_x().setEnabled(false);
			getSpinner_scale_y().setEnabled(false);
		}
		else
		{
			getSpinner_scale_x().setValue(storeScaleX);
			getSpinner_scale_y().setValue(storeScaleY);

			getSpinner_scale_x().setEnabled(true);
			getSpinner_scale_y().setEnabled(true);
		}
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
			spinner_translation_x.setValue(AbsoluteAgreement2D.getTranslationX());
			spinner_translation_x.addChangeListener(new ChangeListener()
			{
				@Override
				public void stateChanged(ChangeEvent e)
				{
					AbsoluteAgreement2D.setTranslationX((Double) spinner_translation_x.getValue());
					fireActionEvent(new ActionEvent(AbsoluteAgreement2DSettingPanel.this, 0, SycamoreFiredActionEvents.UPDATE_AGREEMENTS_GRAPHICS.name()));
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
			spinner_translation_y.setValue(AbsoluteAgreement2D.getTranslationY());
			spinner_translation_y.addChangeListener(new ChangeListener()
			{
				@Override
				public void stateChanged(ChangeEvent e)
				{
					AbsoluteAgreement2D.setTranslationY((Double) spinner_translation_y.getValue());
					fireActionEvent(new ActionEvent(AbsoluteAgreement2DSettingPanel.this, 0, SycamoreFiredActionEvents.UPDATE_AGREEMENTS_GRAPHICS.name()));
				}
			});
		}
		return spinner_translation_y;
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
			spinner_scale_x.setValue(AgreementImpl.isFixMeasureUnit() ? 1.0 : AbsoluteAgreement2D.getScaleX());
			spinner_scale_x.setEnabled(!AgreementImpl.isFixMeasureUnit());
			spinner_scale_x.addChangeListener(new ChangeListener()
			{
				@Override
				public void stateChanged(ChangeEvent e)
				{
					AbsoluteAgreement2D.setScaleX((Double) spinner_scale_x.getValue());
					fireActionEvent(new ActionEvent(AbsoluteAgreement2DSettingPanel.this, 0, SycamoreFiredActionEvents.UPDATE_AGREEMENTS_GRAPHICS.name()));
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
			spinner_scale_y.setValue(AgreementImpl.isFixMeasureUnit() ? 1.0 : AbsoluteAgreement2D.getScaleY());
			spinner_scale_y.setEnabled(!AgreementImpl.isFixMeasureUnit());
			spinner_scale_y.addChangeListener(new ChangeListener()
			{
				@Override
				public void stateChanged(ChangeEvent e)
				{
					AbsoluteAgreement2D.setScaleY((Double) spinner_scale_y.getValue());
					fireActionEvent(new ActionEvent(AbsoluteAgreement2DSettingPanel.this, 0, SycamoreFiredActionEvents.UPDATE_AGREEMENTS_GRAPHICS.name()));
				}
			});
		}
		return spinner_scale_y;
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
			spinner_rotation.setValue(AbsoluteAgreement2D.getRotation());
			spinner_rotation.addChangeListener(new ChangeListener()
			{
				@Override
				public void stateChanged(ChangeEvent e)
				{
					AbsoluteAgreement2D.setRotation((Double) spinner_rotation.getValue());
					fireActionEvent(new ActionEvent(AbsoluteAgreement2DSettingPanel.this, 0, SycamoreFiredActionEvents.UPDATE_AGREEMENTS_GRAPHICS.name()));
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
			{ 0, 0, 0, 0, 0, 0, 0 };
			gbl_panel_contents.rowHeights = new int[]
			{ 0, 0, 0 };
			gbl_panel_contents.columnWeights = new double[]
			{ 0.0, 1.0, 1.0, 1.0, 1.0, 1.0, Double.MIN_VALUE };
			gbl_panel_contents.rowWeights = new double[]
			{ 0.0, 0.0, Double.MIN_VALUE };
			panel_contents.setLayout(gbl_panel_contents);
			GridBagConstraints gbc_label_translation_x = new GridBagConstraints();
			gbc_label_translation_x.anchor = GridBagConstraints.EAST;
			gbc_label_translation_x.insets = new Insets(2, 2, 2, 2);
			gbc_label_translation_x.gridx = 0;
			gbc_label_translation_x.gridy = 0;
			panel_contents.add(getLabel_translation_x(), gbc_label_translation_x);
			GridBagConstraints gbc_spinner_translation_x = new GridBagConstraints();
			gbc_spinner_translation_x.anchor = GridBagConstraints.WEST;
			gbc_spinner_translation_x.insets = new Insets(2, 2, 2, 2);
			gbc_spinner_translation_x.gridx = 1;
			gbc_spinner_translation_x.gridy = 0;
			panel_contents.add(getSpinner_translation_x(), gbc_spinner_translation_x);
			GridBagConstraints gbc_label_scale_x = new GridBagConstraints();
			gbc_label_scale_x.anchor = GridBagConstraints.EAST;
			gbc_label_scale_x.insets = new Insets(2, 2, 2, 2);
			gbc_label_scale_x.gridx = 2;
			gbc_label_scale_x.gridy = 0;
			panel_contents.add(getLabel_scale_x(), gbc_label_scale_x);
			GridBagConstraints gbc_spinner_scale_x = new GridBagConstraints();
			gbc_spinner_scale_x.anchor = GridBagConstraints.WEST;
			gbc_spinner_scale_x.insets = new Insets(2, 2, 2, 2);
			gbc_spinner_scale_x.gridx = 3;
			gbc_spinner_scale_x.gridy = 0;
			panel_contents.add(getSpinner_scale_x(), gbc_spinner_scale_x);
			GridBagConstraints gbc_label_rotation = new GridBagConstraints();
			gbc_label_rotation.anchor = GridBagConstraints.EAST;
			gbc_label_rotation.gridheight = 2;
			gbc_label_rotation.insets = new Insets(2, 2, 2, 2);
			gbc_label_rotation.gridx = 4;
			gbc_label_rotation.gridy = 0;
			panel_contents.add(getLabel_rotation(), gbc_label_rotation);
			GridBagConstraints gbc_spinner_rotation = new GridBagConstraints();
			gbc_spinner_rotation.anchor = GridBagConstraints.WEST;
			gbc_spinner_rotation.gridheight = 2;
			gbc_spinner_rotation.gridx = 5;
			gbc_spinner_rotation.gridy = 0;
			panel_contents.add(getSpinner_rotation(), gbc_spinner_rotation);
			GridBagConstraints gbc_label_translation_y = new GridBagConstraints();
			gbc_label_translation_y.anchor = GridBagConstraints.EAST;
			gbc_label_translation_y.insets = new Insets(2, 2, 2, 2);
			gbc_label_translation_y.gridx = 0;
			gbc_label_translation_y.gridy = 1;
			panel_contents.add(getLabel_translation_y(), gbc_label_translation_y);
			GridBagConstraints gbc_spinner_translation_y = new GridBagConstraints();
			gbc_spinner_translation_y.anchor = GridBagConstraints.WEST;
			gbc_spinner_translation_y.insets = new Insets(2, 2, 2, 2);
			gbc_spinner_translation_y.gridx = 1;
			gbc_spinner_translation_y.gridy = 1;
			panel_contents.add(getSpinner_translation_y(), gbc_spinner_translation_y);
			GridBagConstraints gbc_label_scale_y = new GridBagConstraints();
			gbc_label_scale_y.anchor = GridBagConstraints.EAST;
			gbc_label_scale_y.insets = new Insets(2, 2, 2, 2);
			gbc_label_scale_y.gridx = 2;
			gbc_label_scale_y.gridy = 1;
			panel_contents.add(getLabel_scale_y(), gbc_label_scale_y);
			GridBagConstraints gbc_spinner_scale_y = new GridBagConstraints();
			gbc_spinner_scale_y.anchor = GridBagConstraints.WEST;
			gbc_spinner_scale_y.insets = new Insets(2, 2, 2, 2);
			gbc_spinner_scale_y.gridx = 3;
			gbc_spinner_scale_y.gridy = 1;
			panel_contents.add(getSpinner_scale_y(), gbc_spinner_scale_y);
		}
		return panel_contents;
	}
}
