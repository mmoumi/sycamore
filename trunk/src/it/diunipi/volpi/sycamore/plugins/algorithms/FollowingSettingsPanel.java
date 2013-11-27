/**
 * 
 */
package it.diunipi.volpi.sycamore.plugins.algorithms;

import it.diunipi.volpi.sycamore.engine.SycamoreEngine;
import it.diunipi.volpi.sycamore.gui.SwitchToggle;
import it.diunipi.volpi.sycamore.gui.SycamorePanel;

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
 * The settings panel for <code>ConsistentCompass2D</code> plugin
 * 
 * @author Valerio Volpi - vale.v@me.com
 */
public class FollowingSettingsPanel extends SycamorePanel
{
	private static final long	serialVersionUID	= 7587684962080577106L;
	private JPanel				panel_settings		= null;
	private JLabel				label_move			= null;
	private JLabel				label_rotate		= null;
	private SwitchToggle		switchToggle_move	= null;
	private SwitchToggle		switchToggle_rotate	= null;
	private JLabel				label_useLights;
	private SwitchToggle		switchToggle_useLights;

	/**
	 * Default constructor.
	 */
	public FollowingSettingsPanel()
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
		{ 0 };
		gridBagLayout.rowHeights = new int[]
		{ 117, 0 };
		gridBagLayout.columnWeights = new double[]
		{ Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[]
		{ 1.0, Double.MIN_VALUE };
		setLayout(gridBagLayout);
		GridBagConstraints gbc_panel_settings = new GridBagConstraints();
		gbc_panel_settings.anchor = GridBagConstraints.NORTHWEST;
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
		getSwitchToggle_move().setSelected(Following.isMove());
		getSwitchToggle_rotate().setSelected(Following.isRotate());
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
			panel_settings.setBorder(BorderFactory.createTitledBorder("Select features to use"));
			GridBagLayout gbl_panel_settings = new GridBagLayout();
			gbl_panel_settings.columnWeights = new double[]{1.0, 1.0};
			panel_settings.setLayout(gbl_panel_settings);
			GridBagConstraints gbc_label_move = new GridBagConstraints();
			gbc_label_move.anchor = GridBagConstraints.EAST;
			gbc_label_move.insets = new Insets(2, 2, 2, 2);
			gbc_label_move.gridx = 0;
			gbc_label_move.gridy = 0;
			panel_settings.add(getLabel_move(), gbc_label_move);
			GridBagConstraints gbc_switchToggle_flipX = new GridBagConstraints();
			gbc_switchToggle_flipX.anchor = GridBagConstraints.WEST;
			gbc_switchToggle_flipX.insets = new Insets(2, 2, 2, 2);
			gbc_switchToggle_flipX.gridx = 1;
			gbc_switchToggle_flipX.gridy = 0;
			panel_settings.add(getSwitchToggle_move(), gbc_switchToggle_flipX);
			GridBagConstraints gbc_label_rotate = new GridBagConstraints();
			gbc_label_rotate.anchor = GridBagConstraints.EAST;
			gbc_label_rotate.insets = new Insets(2, 2, 2, 2);
			gbc_label_rotate.gridx = 0;
			gbc_label_rotate.gridy = 1;
			panel_settings.add(getLabel_rotate(), gbc_label_rotate);
			GridBagConstraints gbc_switchToggle_flipY = new GridBagConstraints();
			gbc_switchToggle_flipY.anchor = GridBagConstraints.WEST;
			gbc_switchToggle_flipY.insets = new Insets(2, 2, 2, 2);
			gbc_switchToggle_flipY.gridx = 1;
			gbc_switchToggle_flipY.gridy = 1;
			panel_settings.add(getSwitchToggle_rotate(), gbc_switchToggle_flipY);
			GridBagConstraints gbc_label_useLights = new GridBagConstraints();
			gbc_label_useLights.anchor = GridBagConstraints.EAST;
			gbc_label_useLights.insets = new Insets(2, 2, 2, 2);
			gbc_label_useLights.gridx = 0;
			gbc_label_useLights.gridy = 2;
			panel_settings.add(getLabel_useLights(), gbc_label_useLights);
			GridBagConstraints gbc_switchToggle_useLights = new GridBagConstraints();
			gbc_switchToggle_useLights.anchor = GridBagConstraints.WEST;
			gbc_switchToggle_useLights.insets = new Insets(2, 2, 2, 2);
			gbc_switchToggle_useLights.gridx = 1;
			gbc_switchToggle_useLights.gridy = 2;
			panel_settings.add(getSwitchToggle_useLights(), gbc_switchToggle_useLights);
		}
		return panel_settings;
	}

	/**
	 * @return label_flipX
	 */
	private JLabel getLabel_move()
	{
		if (label_move == null)
		{
			label_move = new JLabel("Move:");
		}
		return label_move;
	}

	/**
	 * @return label_flipY
	 */
	private JLabel getLabel_rotate()
	{
		if (label_rotate == null)
		{
			label_rotate = new JLabel("Rotate:");
		}
		return label_rotate;
	}

	/**
	 * @return
	 */
	private JLabel getLabel_useLights()
	{
		if (label_useLights == null)
		{
			label_useLights = new JLabel("Use lights:");
		}
		return label_useLights;
	}

	/**
	 * @return switchToggle_flipX
	 */
	private SwitchToggle getSwitchToggle_move()
	{
		if (switchToggle_move == null)
		{
			switchToggle_move = new SwitchToggle();
			switchToggle_move.setMaximumSize(new Dimension(71, 25));
			switchToggle_move.setMinimumSize(new Dimension(71, 25));
			switchToggle_move.setPreferredSize(new Dimension(71, 25));
			switchToggle_move.setSelected(Following.isMove());
			switchToggle_move.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent e)
				{
					boolean selected = switchToggle_move.isSelected();
					Following.setMove(selected);
				}
			});
		}
		return switchToggle_move;
	}

	/**
	 * @return switchToggle_flipY
	 */
	private SwitchToggle getSwitchToggle_rotate()
	{
		if (switchToggle_rotate == null)
		{
			switchToggle_rotate = new SwitchToggle();
			switchToggle_rotate.setMaximumSize(new Dimension(71, 25));
			switchToggle_rotate.setMinimumSize(new Dimension(71, 25));
			switchToggle_rotate.setPreferredSize(new Dimension(71, 25));
			switchToggle_rotate.setSelected(Following.isRotate());
			switchToggle_rotate.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent e)
				{
					boolean selected = switchToggle_rotate.isSelected();
					Following.setRotate(selected);
				}
			});
		}
		return switchToggle_rotate;
	}

	/**
	 * @return
	 */
	private SwitchToggle getSwitchToggle_useLights()
	{
		if (switchToggle_useLights == null)
		{
			switchToggle_useLights = new SwitchToggle();
			switchToggle_useLights.setSelected(false);
			switchToggle_useLights.setPreferredSize(new Dimension(71, 25));
			switchToggle_useLights.setMinimumSize(new Dimension(71, 25));
			switchToggle_useLights.setMaximumSize(new Dimension(71, 25));
			switchToggle_useLights.setSelected(Following.isUseLights());
			switchToggle_useLights.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent e)
				{
					boolean selected = switchToggle_useLights.isSelected();
					Following.setUseLights(selected);
				}
			});
		}
		return switchToggle_useLights;
	}
}
