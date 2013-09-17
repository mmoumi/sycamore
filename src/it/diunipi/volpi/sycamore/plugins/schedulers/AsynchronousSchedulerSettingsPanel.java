/**
 * 
 */
package it.diunipi.volpi.sycamore.plugins.schedulers;

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
 * @author Vale
 * 
 */
public class AsynchronousSchedulerSettingsPanel extends SycamorePanel
{
	private static final long	serialVersionUID	= 608718342507251071L;

	private JPanel				panel_settings;
	private JLabel				label_continuous;
	private JLabel				label_rigid;
	private JLabel				label_changeSpeed;
	private JLabel				label_fair;
	private SwitchToggle		switchToggle_continuous;
	private SwitchToggle		switchToggle_rigid;
	private SwitchToggle		switchToggle_changeSpeed;
	private SwitchToggle		switchToggle_fair;

	/**
	 * Constructor.
	 */
	public AsynchronousSchedulerSettingsPanel()
	{
		initialize();
		updateGui();
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
		GridBagConstraints gbc_panel_settings = new GridBagConstraints();
		gbc_panel_settings.fill = GridBagConstraints.BOTH;
		gbc_panel_settings.gridx = 0;
		gbc_panel_settings.gridy = 0;
		add(getPanel_settings(), gbc_panel_settings);
	}

	/**
	 * @return
	 */
	private JPanel getPanel_settings()
	{
		if (panel_settings == null)
		{
			panel_settings = new JPanel();
			panel_settings.setBorder(BorderFactory.createTitledBorder("Scheduler abilities"));
			GridBagLayout gbl_panel_settings = new GridBagLayout();
			gbl_panel_settings.columnWidths = new int[]
			{ 0, 0, 0, 0, 0 };
			gbl_panel_settings.rowHeights = new int[]
			{ 0, 0, 0 };
			gbl_panel_settings.columnWeights = new double[]
			{ 1.0, 0.0, 0.0, 1.0, Double.MIN_VALUE };
			gbl_panel_settings.rowWeights = new double[]
			{ 1.0, 1.0, Double.MIN_VALUE };
			panel_settings.setLayout(gbl_panel_settings);
			GridBagConstraints gbc_label_continuous = new GridBagConstraints();
			gbc_label_continuous.anchor = GridBagConstraints.EAST;
			gbc_label_continuous.insets = new Insets(2, 2, 2, 2);
			gbc_label_continuous.gridx = 0;
			gbc_label_continuous.gridy = 0;
			panel_settings.add(getLabel_continuous(), gbc_label_continuous);
			GridBagConstraints gbc_switchToggle_continuous = new GridBagConstraints();
			gbc_switchToggle_continuous.anchor = GridBagConstraints.WEST;
			gbc_switchToggle_continuous.insets = new Insets(2, 2, 2, 2);
			gbc_switchToggle_continuous.gridx = 1;
			gbc_switchToggle_continuous.gridy = 0;
			panel_settings.add(getSwitchToggle_continuous(), gbc_switchToggle_continuous);
			GridBagConstraints gbc_label_changeSpeed = new GridBagConstraints();
			gbc_label_changeSpeed.anchor = GridBagConstraints.EAST;
			gbc_label_changeSpeed.insets = new Insets(2, 20, 2, 2);
			gbc_label_changeSpeed.gridx = 2;
			gbc_label_changeSpeed.gridy = 0;
			panel_settings.add(getLabel_changeSpeed(), gbc_label_changeSpeed);
			GridBagConstraints gbc_switchToggle_changeSpeed = new GridBagConstraints();
			gbc_switchToggle_changeSpeed.anchor = GridBagConstraints.WEST;
			gbc_switchToggle_changeSpeed.insets = new Insets(2, 2, 2, 2);
			gbc_switchToggle_changeSpeed.gridx = 3;
			gbc_switchToggle_changeSpeed.gridy = 0;
			panel_settings.add(getSwitchToggle_changeSpeed(), gbc_switchToggle_changeSpeed);
			GridBagConstraints gbc_label_rigid = new GridBagConstraints();
			gbc_label_rigid.anchor = GridBagConstraints.EAST;
			gbc_label_rigid.insets = new Insets(2, 2, 2, 2);
			gbc_label_rigid.gridx = 0;
			gbc_label_rigid.gridy = 1;
			panel_settings.add(getLabel_rigid(), gbc_label_rigid);
			GridBagConstraints gbc_switchToggle_rigid = new GridBagConstraints();
			gbc_switchToggle_rigid.anchor = GridBagConstraints.WEST;
			gbc_switchToggle_rigid.insets = new Insets(2, 2, 2, 2);
			gbc_switchToggle_rigid.gridx = 1;
			gbc_switchToggle_rigid.gridy = 1;
			panel_settings.add(getSwitchToggle_rigid(), gbc_switchToggle_rigid);
			GridBagConstraints gbc_label_fair = new GridBagConstraints();
			gbc_label_fair.insets = new Insets(2, 2, 2, 2);
			gbc_label_fair.anchor = GridBagConstraints.EAST;
			gbc_label_fair.gridx = 2;
			gbc_label_fair.gridy = 1;
			panel_settings.add(getLabel_fair(), gbc_label_fair);
			GridBagConstraints gbc_switchToggle_fair = new GridBagConstraints();
			gbc_switchToggle_fair.insets = new Insets(2, 2, 2, 2);
			gbc_switchToggle_fair.anchor = GridBagConstraints.WEST;
			gbc_switchToggle_fair.gridx = 3;
			gbc_switchToggle_fair.gridy = 1;
			panel_settings.add(getSwitchToggle_fair(), gbc_switchToggle_fair);
		}
		return panel_settings;
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
		getSwitchToggle_changeSpeed().setSelected(AsynchronousScheduler.isChangesRobotSpeed());
		getSwitchToggle_continuous().setSelected(AsynchronousScheduler.isContinuous());
		getSwitchToggle_fair().setSelected(AsynchronousScheduler.isFair());
		getSwitchToggle_rigid().setSelected(AsynchronousScheduler.isRigid());
	}

	/**
	 * @return
	 */
	private JLabel getLabel_continuous()
	{
		if (label_continuous == null)
		{
			label_continuous = new JLabel("Continuous:");
		}
		return label_continuous;
	}

	/**
	 * @return
	 */
	private JLabel getLabel_rigid()
	{
		if (label_rigid == null)
		{
			label_rigid = new JLabel("Rigid movement:");
		}
		return label_rigid;
	}

	/**
	 * @return
	 */
	private JLabel getLabel_changeSpeed()
	{
		if (label_changeSpeed == null)
		{
			label_changeSpeed = new JLabel("Changes robot speed:");
		}
		return label_changeSpeed;
	}

	/**
	 * @return
	 */
	private JLabel getLabel_fair()
	{
		if (label_fair == null)
		{
			label_fair = new JLabel("Fair:");
		}
		return label_fair;
	}

	/**
	 * @return
	 */
	private SwitchToggle getSwitchToggle_continuous()
	{
		if (switchToggle_continuous == null)
		{
			switchToggle_continuous = new SwitchToggle();
			switchToggle_continuous.setMaximumSize(new Dimension(71, 25));
			switchToggle_continuous.setMinimumSize(new Dimension(71, 25));
			switchToggle_continuous.setPreferredSize(new Dimension(71, 25));
			switchToggle_continuous.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent e)
				{
					boolean selected = switchToggle_continuous.isSelected();
					AsynchronousScheduler.setContinuous(selected);
					
					if (selected)
					{
						getSwitchToggle_fair().setEnabled(false);
					}
					else
					{
						getSwitchToggle_fair().setEnabled(true);
					}
				}
			});
		}
		return switchToggle_continuous;
	}

	/**
	 * @return
	 */
	private SwitchToggle getSwitchToggle_rigid()
	{
		if (switchToggle_rigid == null)
		{
			switchToggle_rigid = new SwitchToggle();
			switchToggle_rigid.setMaximumSize(new Dimension(71, 25));
			switchToggle_rigid.setMinimumSize(new Dimension(71, 25));
			switchToggle_rigid.setPreferredSize(new Dimension(71, 25));
			switchToggle_rigid.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent e)
				{
					boolean selected = switchToggle_rigid.isSelected();
					AsynchronousScheduler.setRigid(selected);
				}
			});
		}
		return switchToggle_rigid;
	}

	/**
	 * @return
	 */
	private SwitchToggle getSwitchToggle_changeSpeed()
	{
		if (switchToggle_changeSpeed == null)
		{
			switchToggle_changeSpeed = new SwitchToggle();
			switchToggle_changeSpeed.setMaximumSize(new Dimension(71, 25));
			switchToggle_changeSpeed.setMinimumSize(new Dimension(71, 25));
			switchToggle_changeSpeed.setPreferredSize(new Dimension(71, 25));
			switchToggle_changeSpeed.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent e)
				{
					boolean selected = switchToggle_changeSpeed.isSelected();
					AsynchronousScheduler.setChangesRobotSpeed(selected);
				}
			});
		}
		return switchToggle_changeSpeed;
	}

	/**
	 * @return
	 */
	private SwitchToggle getSwitchToggle_fair()
	{
		if (switchToggle_fair == null)
		{
			switchToggle_fair = new SwitchToggle();
			switchToggle_fair.setMaximumSize(new Dimension(71, 25));
			switchToggle_fair.setMinimumSize(new Dimension(71, 25));
			switchToggle_fair.setPreferredSize(new Dimension(71, 25));
			switchToggle_fair.setEnabled(!AsynchronousScheduler.isContinuous());
			switchToggle_fair.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent e)
				{
					boolean selected = switchToggle_fair.isSelected();
					AsynchronousScheduler.setFair(selected);
				}
			});
		}
		return switchToggle_fair;
	}
	
	/* (non-Javadoc)
	 * @see it.diunipi.volpi.sycamore.gui.SycamorePanel#reset()
	 */
	@Override
	public void reset()
	{
		// Nothing to do
	}
}
