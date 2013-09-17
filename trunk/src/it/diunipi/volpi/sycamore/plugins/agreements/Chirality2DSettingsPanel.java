/**
 * 
 */
package it.diunipi.volpi.sycamore.plugins.agreements;

import it.diunipi.volpi.sycamore.engine.SycamoreEngine;
import it.diunipi.volpi.sycamore.gui.SwitchToggle;
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
public class Chirality2DSettingsPanel extends AgreementSettingsPanel
{
	private static final long	serialVersionUID	= 7587684962080577106L;
	private JPanel				panel_settings		= null;
	private JPanel				panel_contents		= null;
	private JLabel				label_flipX			= null;
	private JLabel				label_flipY			= null;
	private SwitchToggle		switchToggle_flipX	= null;
	private SwitchToggle		switchToggle_flipY	= null;

	/**
	 * Default constructor.
	 */
	public Chirality2DSettingsPanel()
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
		getSwitchToggle_flipX().setSelected(ConsistentCompass2D.isFlipX());
		getSwitchToggle_flipY().setSelected(ConsistentCompass2D.isFlipY());
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
	 * @return panel_contents
	 */
	private JPanel getPanel_contents()
	{
		if (panel_contents == null)
		{
			panel_contents = new JPanel();
			GridBagLayout gbl_panel_contents = new GridBagLayout();
			gbl_panel_contents.columnWidths = new int[]
			{ 0, 0, 0, 0 };
			gbl_panel_contents.rowHeights = new int[]
			{ 0, 0 };
			gbl_panel_contents.columnWeights = new double[]
			{ 1.0, 1.0, 1.0, 1.0 };
			gbl_panel_contents.rowWeights = new double[]
			{ 0.0, Double.MIN_VALUE };
			panel_contents.setLayout(gbl_panel_contents);
			GridBagConstraints gbc_label_flipX = new GridBagConstraints();
			gbc_label_flipX.anchor = GridBagConstraints.EAST;
			gbc_label_flipX.insets = new Insets(2, 2, 2, 2);
			gbc_label_flipX.gridx = 0;
			gbc_label_flipX.gridy = 0;
			panel_contents.add(getLabel_flipX(), gbc_label_flipX);
			GridBagConstraints gbc_switchToggle_flipX = new GridBagConstraints();
			gbc_switchToggle_flipX.anchor = GridBagConstraints.WEST;
			gbc_switchToggle_flipX.insets = new Insets(2, 2, 2, 2);
			gbc_switchToggle_flipX.gridx = 1;
			gbc_switchToggle_flipX.gridy = 0;
			panel_contents.add(getSwitchToggle_flipX(), gbc_switchToggle_flipX);
			GridBagConstraints gbc_label_flipY = new GridBagConstraints();
			gbc_label_flipY.anchor = GridBagConstraints.EAST;
			gbc_label_flipY.insets = new Insets(2, 2, 2, 2);
			gbc_label_flipY.gridx = 2;
			gbc_label_flipY.gridy = 0;
			panel_contents.add(getLabel_flipY(), gbc_label_flipY);
			GridBagConstraints gbc_switchToggle_flipY = new GridBagConstraints();
			gbc_switchToggle_flipY.anchor = GridBagConstraints.WEST;
			gbc_switchToggle_flipY.insets = new Insets(2, 2, 2, 2);
			gbc_switchToggle_flipY.gridx = 3;
			gbc_switchToggle_flipY.gridy = 0;
			panel_contents.add(getSwitchToggle_flipY(), gbc_switchToggle_flipY);
		}
		return panel_contents;
	}

	/**
	 * @return
	 */
	private JLabel getLabel_flipX()
	{
		if (label_flipX == null)
		{
			label_flipX = new JLabel("Flip X:");
		}
		return label_flipX;
	}

	/**
	 * @return
	 */
	private JLabel getLabel_flipY()
	{
		if (label_flipY == null)
		{
			label_flipY = new JLabel("Flip Y:");
		}
		return label_flipY;
	}

	/**
	 * @return
	 */
	private SwitchToggle getSwitchToggle_flipX()
	{
		if (switchToggle_flipX == null)
		{
			switchToggle_flipX = new SwitchToggle();
			switchToggle_flipX.setMaximumSize(new Dimension(71, 25));
			switchToggle_flipX.setMinimumSize(new Dimension(71, 25));
			switchToggle_flipX.setPreferredSize(new Dimension(71, 25));
			switchToggle_flipX.setSelected(Chirality2D.isFlipX());
			switchToggle_flipX.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent e)
				{
					boolean selected = switchToggle_flipX.isSelected();
					Chirality2D.setFlipX(selected);
					fireActionEvent(new ActionEvent(Chirality2DSettingsPanel.this, 0, SycamoreFiredActionEvents.UPDATE_AGREEMENTS_GRAPHICS.name()));
				}
			});
		}
		return switchToggle_flipX;
	}

	/**
	 * @return
	 */
	private SwitchToggle getSwitchToggle_flipY()
	{
		if (switchToggle_flipY == null)
		{
			switchToggle_flipY = new SwitchToggle();
			switchToggle_flipY.setMaximumSize(new Dimension(71, 25));
			switchToggle_flipY.setMinimumSize(new Dimension(71, 25));
			switchToggle_flipY.setPreferredSize(new Dimension(71, 25));
			switchToggle_flipY.setSelected(Chirality2D.isFlipY());
			switchToggle_flipY.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent e)
				{
					boolean selected = switchToggle_flipY.isSelected();
					Chirality2D.setFlipY(selected);
					fireActionEvent(new ActionEvent(Chirality2DSettingsPanel.this, 0, SycamoreFiredActionEvents.UPDATE_AGREEMENTS_GRAPHICS.name()));
				}
			});
		}
		return switchToggle_flipY;
	}
}
