package it.diunipi.volpi.sycamore.gui;

import it.diunipi.volpi.sycamore.engine.SycamoreEngine;
import it.diunipi.volpi.sycamore.engine.SycamoreEngine.TYPE;
import it.diunipi.volpi.sycamore.plugins.SycamorePluginManager;
import it.diunipi.volpi.sycamore.plugins.schedulers.Scheduler;
import it.diunipi.volpi.sycamore.util.SycamoreFiredActionEvents;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.ComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;

/**
 * The panel that lets the user configure the simulation. It offers the possibilty to choose a
 * measure, a scheduler and some algorithms between the loaded plugins.
 * 
 * @author Valerio Volpi - vale.v@me.com
 */
public class SycamoreSimulationSettingsPanel extends SycamoreTitledRoundedBorderPanel
{
	private static final long					serialVersionUID							= -423433900896222738L;
	private JLabel								label_selectScheduler						= null;
	private JComboBox							comboBox_selectScheduler					= null;
	private SycamoreEngine						appEngine									= null;
	private SycamoreRobotsConfigurationPanel	sycamoreRobotsConfigurationPanel			= null;
	private JScrollPane							scrollPane_robotsConfigurationsContainer	= null;
	private JButton								button_otherPlugins							= null;
	private SycamoreAdditionalPluginsPanel		additionalPluginsPanel						= null;
	private JLabel								label_robotsNumber							= null;
	private JButton								button_manageRobots							= null;
	private JLabel								label_pluginsInfo							= null;

	/**
	 * Default constructor
	 */
	public SycamoreSimulationSettingsPanel()
	{
		initialize();
	}

	/**
	 * Init the GUI
	 */
	private void initialize()
	{
		setMinimumSize(new Dimension(310, 240));
		setMaximumSize(new Dimension(310, 240));
		setPreferredSize(new Dimension(310, 240));
		setSize(new Dimension(310, 240));

		getLabel_title().setText("Simulation settings");

		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWeights = new double[]
		{ 1.0 };
		gridBagLayout.rowWeights = new double[]
		{ 0.0, 0.0, 0.0, 0.0, 0.0, 1.0 };
		getPanel_contentContainer().setLayout(gridBagLayout);

		GridBagConstraints gbc_label_selectScheduler = new GridBagConstraints();
		gbc_label_selectScheduler.anchor = GridBagConstraints.SOUTHWEST;
		gbc_label_selectScheduler.insets = new Insets(5, 5, 5, 5);
		gbc_label_selectScheduler.gridx = 0;
		gbc_label_selectScheduler.gridy = 0;
		getPanel_contentContainer().add(getLabel_selectScheduler(), gbc_label_selectScheduler);
		
		GridBagConstraints gbc_comboBox_selectScheduler = new GridBagConstraints();
		gbc_comboBox_selectScheduler.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboBox_selectScheduler.insets = new Insets(5, 5, 5, 7);
		gbc_comboBox_selectScheduler.gridx = 0;
		gbc_comboBox_selectScheduler.gridy = 1;
		getPanel_contentContainer().add(getComboBox_selectScheduler(), gbc_comboBox_selectScheduler);

		GridBagConstraints gbc_label_robotsNumber = new GridBagConstraints();
		gbc_label_robotsNumber.anchor = GridBagConstraints.SOUTHWEST;
		gbc_label_robotsNumber.insets = new Insets(5, 5, 5, 5);
		gbc_label_robotsNumber.gridx = 0;
		gbc_label_robotsNumber.gridy = 2;
		getPanel_contentContainer().add(getLabel_robotsNumber(), gbc_label_robotsNumber);
		
		GridBagConstraints gbc_button_manageRobots = new GridBagConstraints();
		gbc_button_manageRobots.fill = GridBagConstraints.HORIZONTAL;
		gbc_button_manageRobots.insets = new Insets(5, 5, 5, 7);
		gbc_button_manageRobots.gridx = 0;
		gbc_button_manageRobots.gridy = 3;
		getPanel_contentContainer().add(getButton_manageRobots(), gbc_button_manageRobots);

		GridBagConstraints gbc_label_pluginsInfo = new GridBagConstraints();
		gbc_label_pluginsInfo.anchor = GridBagConstraints.SOUTHWEST;
		gbc_label_pluginsInfo.insets = new Insets(5, 5, 5, 5);
		gbc_label_pluginsInfo.gridx = 0;
		gbc_label_pluginsInfo.gridy = 4;
		getPanel_contentContainer().add(getLabel_pluginsInfo(), gbc_label_pluginsInfo);
		
		GridBagConstraints gbc_button_otherPlugins = new GridBagConstraints();
		gbc_button_otherPlugins.anchor = GridBagConstraints.NORTH;
		gbc_button_otherPlugins.fill = GridBagConstraints.HORIZONTAL;
		gbc_button_otherPlugins.insets = new Insets(5, 5, 10, 7);
		gbc_button_otherPlugins.gridx = 0;
		gbc_button_otherPlugins.gridy = 5;
		getPanel_contentContainer().add(getButton_otherPlugins(), gbc_button_otherPlugins);
	}

	/**
	 * @return the label_selectScheduler
	 */
	private JLabel getLabel_selectScheduler()
	{
		if (label_selectScheduler == null)
		{
			label_selectScheduler = new JLabel("Select a scheduler:");
		}
		return label_selectScheduler;
	}

	/**
	 * @return the comboBox_selectScheduler
	 */
	private JComboBox getComboBox_selectScheduler()
	{
		if (comboBox_selectScheduler == null)
		{
			comboBox_selectScheduler = new JComboBox();

			// apply a PluginSelectionComboboxModel with generics for Schedulers to this combobox
			comboBox_selectScheduler.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent e)
				{
					if (!changeLock)
					{
						PluginSelectionComboboxModel<Scheduler> model = (PluginSelectionComboboxModel<Scheduler>) getComboBox_selectScheduler().getModel();
						Scheduler scheduler = (Scheduler) model.getSelectedItem();

						if (appEngine != null)
						{
							try
							{
								appEngine.createAndSetNewSchedulerInstance(scheduler);
								fireActionEvent(new ActionEvent(this, 0, SycamoreFiredActionEvents.UPDATE_GUI.name()));
							}
							catch (Exception e1)
							{
								e1.printStackTrace();
							}
						}
					}
				}
			});
		}
		return comboBox_selectScheduler;
	}

	/**
	 * @return the sycamoreRobotsConfigurationPanel
	 */
	private SycamoreRobotsConfigurationPanel getSycamoreRobotsConfigurationPanel()
	{
		if (sycamoreRobotsConfigurationPanel == null)
		{
			sycamoreRobotsConfigurationPanel = new SycamoreRobotsConfigurationPanel();
			sycamoreRobotsConfigurationPanel.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent e)
				{
					{
						// listen to the events that comes from the robots configuration panel. We
						// just
						// forward the event.
						fireActionEvent(e);
					}
				}
			});
		}
		return sycamoreRobotsConfigurationPanel;
	}

	/**
	 * @return the scrollPane_robotsConfigurationsContainer
	 */
	private JScrollPane getScrollPane_robotsConfigurationsContainer()
	{
		if (scrollPane_robotsConfigurationsContainer == null)
		{
			scrollPane_robotsConfigurationsContainer = new JScrollPane();
			scrollPane_robotsConfigurationsContainer.setViewportView(getSycamoreRobotsConfigurationPanel());
			scrollPane_robotsConfigurationsContainer.setBorder(BorderFactory.createEmptyBorder());
			scrollPane_robotsConfigurationsContainer.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
			scrollPane_robotsConfigurationsContainer.setPreferredSize(new Dimension(600, 450));
			scrollPane_robotsConfigurationsContainer.setOpaque(false);
		}
		return scrollPane_robotsConfigurationsContainer;
	}

	/**
	 * @return label_robotsNumber
	 */
	private JLabel getLabel_robotsNumber()
	{
		if (label_robotsNumber == null)
		{
			label_robotsNumber = new JLabel("There are no robots in the system");
		}
		return label_robotsNumber;
	}

	/**
	 * @return button_manageRobots
	 */
	private JButton getButton_manageRobots()
	{
		if (button_manageRobots == null)
		{
			button_manageRobots = new JButton("Manage robots");
			button_manageRobots.setIconTextGap(10);
			button_manageRobots.setHorizontalAlignment(SwingConstants.LEADING);
			button_manageRobots.setFont(new Font("Lucida Grande", Font.PLAIN, 13));
			button_manageRobots.setIcon(new ImageIcon(getClass().getResource("/it/diunipi/volpi/sycamore/resources/robot_24x24.png")));
			button_manageRobots.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent e)
				{
					ImageIcon icon = new ImageIcon(getClass().getResource("/it/diunipi/volpi/sycamore/resources/robot_64x64.png"));
					JOptionPane.showMessageDialog(SycamoreSimulationSettingsPanel.this.getParent(), getScrollPane_robotsConfigurationsContainer(), "Manage robots", JOptionPane.INFORMATION_MESSAGE,
							icon);
				}
			});
		}
		return button_manageRobots;
	}

	/**
	 * @return label_pluginsInfo
	 */
	private JLabel getLabel_pluginsInfo()
	{
		if (label_pluginsInfo == null)
		{
			label_pluginsInfo = new JLabel("Tune some system capabilities:");
		}
		return label_pluginsInfo;
	}

	/**
	 * @return button_otherPlugins
	 */
	private JButton getButton_otherPlugins()
	{
		if (button_otherPlugins == null)
		{
			button_otherPlugins = new JButton("Configure other plugins");
			button_otherPlugins.setIconTextGap(10);
			button_otherPlugins.setFont(new Font("Lucida Grande", Font.PLAIN, 13));
			button_otherPlugins.setHorizontalAlignment(SwingConstants.LEFT);
			button_otherPlugins.setIcon(new ImageIcon(getClass().getResource("/it/diunipi/volpi/sycamore/resources/plugins_24x24.png")));
			button_otherPlugins.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent e)
				{
					ImageIcon icon = new ImageIcon(getClass().getResource("/it/diunipi/volpi/sycamore/resources/plugins_64x64.png"));
					JOptionPane.showMessageDialog(SycamoreSimulationSettingsPanel.this.getParent(), getAdditionalPluginsPanel(), "Configure other plugins", JOptionPane.INFORMATION_MESSAGE, icon);
				}
			});
		}
		return button_otherPlugins;
	}

	/**
	 * @return the additionalPluginsPanel
	 */
	private SycamoreAdditionalPluginsPanel getAdditionalPluginsPanel()
	{
		if (additionalPluginsPanel == null)
		{
			additionalPluginsPanel = new SycamoreAdditionalPluginsPanel();
			additionalPluginsPanel.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent e)
				{
					// forward action event
					fireActionEvent(e);
				}
			});
		}
		return additionalPluginsPanel;
	}

	/**
	 * Returns the current app engine
	 * 
	 * @return
	 */
	public SycamoreEngine getAppEngine()
	{
		return appEngine;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * it.diunipi.volpi.sycamore.gui.SycamoreRoundedBorderPanel#setAppEngine(it.diunipi.volpi.sycamore
	 * .engine.SycamoreEngine)
	 */
	@Override
	public void setAppEngine(SycamoreEngine appEngine)
	{
		this.appEngine = appEngine;

		getSycamoreRobotsConfigurationPanel().setAppEngine(appEngine);
		getAdditionalPluginsPanel().setAppEngine(appEngine);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.JComponent#setEnabled(boolean)
	 */
	@Override
	public void setEnabled(boolean enabled)
	{
		super.setEnabled(enabled);

		getComboBox_selectScheduler().setEnabled(enabled);
		getSycamoreRobotsConfigurationPanel().setEnabled(enabled);
		getButton_manageRobots().setEnabled(enabled);
		getButton_otherPlugins().setEnabled(enabled);
	}

	/**
	 * Manage a refreshing of the plugins
	 */
	public void managePluginsChanged()
	{
		updateModels();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.diunipi.volpi.sycamore.gui.SycamoreRoundedBorderPanel#updateGui()
	 */
	@Override
	public void updateGui()
	{
		changeLock = true;
		if (appEngine != null)
		{
			ComboBoxModel model = getComboBox_selectScheduler().getModel();
			for (int i = 0; i < model.getSize(); i++)
			{
				Scheduler item = (Scheduler) model.getElementAt(i);
				Scheduler current = appEngine.getCurrentScheduler();
				if (item != null && current != null && item.getPluginName().equals(current.getPluginName()))
				{
					getComboBox_selectScheduler().setSelectedIndex(i);
				}
			}
		}

		getSycamoreRobotsConfigurationPanel().updateGui();
		getAdditionalPluginsPanel().updateGui();

		updateRobotsCountLabel();
		changeLock = false;
	}

	/**
	 * Returns the type chosen by the user in the robot configuration sub-panel. If more than one
	 * type is chosen, returns one of them if they are all consistent, otherwise return null.
	 * 
	 * @return
	 */
	public TYPE getChosenType()
	{
		return getSycamoreRobotsConfigurationPanel().getChosenType();
	}

	/**
	 * Updates passed engine to make it have the scheduler and the measures conform to what is
	 * chosen by the user in the controls provided by this class. It also make the passed engine
	 * have the robots number and settings consistent with the values selected in the robots
	 * configuration sub-panel .
	 * 
	 * @param newEngine
	 */
	public void updateEngine(SycamoreEngine newEngine)
	{
		if (newEngine != null)
		{
			try
			{
				// prepare the new scheduler
				PluginSelectionComboboxModel<Scheduler> schedulerModel = (PluginSelectionComboboxModel<Scheduler>) getComboBox_selectScheduler().getModel();
				Scheduler scheduler = (Scheduler) schedulerModel.getSelectedItem();

				if (scheduler != null)
				{
					// create, set and start new scheduler
					newEngine.createAndSetNewSchedulerInstance(scheduler);
				}

				getSycamoreRobotsConfigurationPanel().updateEngine(newEngine);
				getAdditionalPluginsPanel().updateEngine(newEngine);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}

	/**
	 * Update the combo box models
	 */
	public void updateModels()
	{
		// apply a PluginSelectionComboboxModel with generics for Schedulers to this combobox
		ArrayList<Scheduler> schedulers = SycamorePluginManager.getSharedInstance().getLoadedSchedulers();
		PluginSelectionComboboxModel<Scheduler> schedulerModel = new PluginSelectionComboboxModel<Scheduler>(schedulers);
		getComboBox_selectScheduler().setModel(schedulerModel);

		// update models in the 2 sub-panels
		getSycamoreRobotsConfigurationPanel().updateModels();
		getAdditionalPluginsPanel().updateModels();
	}

	/**
	 * Updates the label that tells the number of robots in the system
	 */
	public void updateRobotsCountLabel()
	{
		String text = "There are no robots in the system";

		if (appEngine != null)
		{
			int robotsCount = appEngine.getRobotsCount();

			if (robotsCount == 1)
			{
				text = "There is " + robotsCount + " robot in the system";
			}
			else if (robotsCount > 1)
			{
				text = "There are " + robotsCount + " robots in the system";
			}
		}

		getLabel_robotsNumber().setText(text);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.diunipi.volpi.sycamore.gui.SycamorePanel#reset()
	 */
	@Override
	public void reset()
	{
		changeLock = true;
		getComboBox_selectScheduler().setSelectedIndex(-1);
		getSycamoreRobotsConfigurationPanel().reset();
		getAdditionalPluginsPanel().reset();
		changeLock = false;
	}
}
