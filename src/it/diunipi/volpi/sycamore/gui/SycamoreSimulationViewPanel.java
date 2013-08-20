package it.diunipi.volpi.sycamore.gui;

import it.diunipi.volpi.sycamore.engine.SycamoreEngine;
import it.diunipi.volpi.sycamore.engine.SycamoreEngine.TYPE;
import it.diunipi.volpi.sycamore.jmescene.SycamoreJMESceneCanvasPanel;
import it.diunipi.volpi.sycamore.model.SycamoreRobot;
import it.diunipi.volpi.sycamore.util.SycamoreFiredActionEvents;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

/**
 * The panel that contains the JME scene and the buttons to manage the camera or the visible
 * elements.
 * 
 * @author Valerio Volpi - vale.v@me.com
 */
public class SycamoreSimulationViewPanel extends SycamorePanel
{
	private static final long			serialVersionUID		= 4726655040850087282L;
	private SycamoreViewButtonsPanel	panel_viewButtons		= null;
	private SycamoreCameraControlPanel	panel_sceneControl		= null;
	private SycamoreJMESceneCanvasPanel	panel_sceneContainer	= null;
	private SycamoreEngine				appEngine				= null;
	private JPanel						panel_border			= null;

	/**
	 * Default constructor
	 */
	public SycamoreSimulationViewPanel()
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
		{ 0 };
		gridBagLayout.rowHeights = new int[]
		{ 0, 0, 0 };
		gridBagLayout.columnWeights = new double[]
		{ 1.0 };
		gridBagLayout.rowWeights = new double[]
		{ 0.0, 1.0, Double.MIN_VALUE };
		setLayout(gridBagLayout);

		GridBagConstraints gbc_sycamoreShowButtonsPanel = new GridBagConstraints();
		gbc_sycamoreShowButtonsPanel.fill = GridBagConstraints.HORIZONTAL;
		gbc_sycamoreShowButtonsPanel.insets = new Insets(0, 0, 5, 0);
		gbc_sycamoreShowButtonsPanel.anchor = GridBagConstraints.NORTH;
		gbc_sycamoreShowButtonsPanel.gridx = 1;
		gbc_sycamoreShowButtonsPanel.gridy = 0;
		add(getPanel_viewButtons(), gbc_sycamoreShowButtonsPanel);

		GridBagConstraints gbc_sycamoreSceneControlPanel = new GridBagConstraints();
		gbc_sycamoreSceneControlPanel.fill = GridBagConstraints.BOTH;
		gbc_sycamoreSceneControlPanel.insets = new Insets(0, 0, 5, 5);
		gbc_sycamoreSceneControlPanel.anchor = GridBagConstraints.NORTH;
		gbc_sycamoreSceneControlPanel.gridx = 0;
		gbc_sycamoreSceneControlPanel.gridy = 0;
		add(getPanel_sceneControl(), gbc_sycamoreSceneControlPanel);
		GridBagConstraints gbc_panel_border = new GridBagConstraints();
		gbc_panel_border.fill = GridBagConstraints.BOTH;
		gbc_panel_border.gridwidth = 2;
		gbc_panel_border.insets = new Insets(0, 0, 0, 5);
		gbc_panel_border.gridx = 0;
		gbc_panel_border.gridy = 1;
		add(getPanel_border(), gbc_panel_border);
	}

	/**
	 * Init the JME scene
	 */
	public void initJMEScene()
	{
		getPanel_sceneContainer().initJMEScene();
	}

	/**
	 * Dispose the JME Scene
	 */
	public void disposeJMEScene()
	{
		getPanel_sceneContainer().disposeJMEScene();
	}

	/**
	 * Reset the JME Scene
	 */
	public void resetJMEScene()
	{
		getPanel_sceneContainer().resetJMEScene();
	}

	/**
	 * @return the sceneControlPanel
	 */
	private SycamoreCameraControlPanel getPanel_sceneControl()
	{
		if (panel_sceneControl == null)
		{
			panel_sceneControl = new SycamoreCameraControlPanel();
			panel_sceneControl.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent e)
				{
					if (e.getActionCommand().equals(SycamoreFiredActionEvents.CAMERA_ON_ORIGIN.name()))
					{
						getPanel_sceneContainer().cameraOnOrigin();
					}
					else if (e.getActionCommand().equals(SycamoreFiredActionEvents.CAMERA_ON_BARICENTRUM.name()))
					{
						getPanel_sceneContainer().cameraOnBaricentrum();
					}
				}
			});
		}
		return panel_sceneControl;
	}

	/**
	 * @return the showButtonsPanel
	 */
	private SycamoreViewButtonsPanel getPanel_viewButtons()
	{
		if (panel_viewButtons == null)
		{
			panel_viewButtons = new SycamoreViewButtonsPanel();
			panel_viewButtons.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent e)
				{								
					// just forward the event
					fireActionEvent(e);
				}
			});
		}
		return panel_viewButtons;
	}

	/**
	 * @return the sceneContainer
	 */
	private SycamoreJMESceneCanvasPanel getPanel_sceneContainer()
	{
		if (panel_sceneContainer == null)
		{
			panel_sceneContainer = new SycamoreJMESceneCanvasPanel();
			panel_sceneContainer.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent e)
				{
					// forward event
					fireActionEvent(e);
				}
			});
		}
		return panel_sceneContainer;
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
	 * @see javax.swing.JComponent#setEnabled(boolean)
	 */
	@Override
	public void setEnabled(boolean enabled)
	{
		getPanel_sceneControl().setEnabled(enabled);
		getPanel_viewButtons().setEnabled(enabled);
	}

	/**
	 * Changes the current app engine replacing it with passed one.
	 * 
	 * @param appEngine
	 */
	public void setAppEngine(SycamoreEngine appEngine)
	{
		this.appEngine = appEngine;

		getPanel_sceneControl().setAppEngine(appEngine);
		getPanel_viewButtons().setAppEngine(appEngine);
		getPanel_sceneContainer().setAppEngine(appEngine);
	}

	/**
	 * Updates the GUI in order to reflect changes in the engine
	 */
	public void updateGui()
	{
		getPanel_sceneControl().updateGui();
		getPanel_viewButtons().updateGui();
		getPanel_sceneContainer().updateGui();
	}

	/**
	 * Adds passed robot in the scene.
	 */
	public void addRobotInScene(SycamoreRobot<?> robot)
	{
		getPanel_sceneContainer().addRobotInScene(robot);
	}

	/**
	 * Removes passed robot from teh scene.
	 */
	public void removeRobotFromScene(SycamoreRobot<?> robot)
	{
		getPanel_sceneContainer().removeRobotFromScene(robot);
	}

	/**
	 * @param type
	 */
	public void setupScene(TYPE type)
	{
		getPanel_sceneContainer().setupScene(type);
	}

	/**
	 * @return
	 */
	private JPanel getPanel_border()
	{
		if (panel_border == null)
		{
			panel_border = new JPanel();
			panel_border.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, new Color(0.69f, 0.69f, 0.69f)));
			GridBagLayout gbl_panel_border = new GridBagLayout();
			gbl_panel_border.columnWidths = new int[]
			{ 0, 0, 0 };
			gbl_panel_border.rowHeights = new int[]
			{ 0, 0 };
			gbl_panel_border.columnWeights = new double[]
			{ 1.0, 0.0, Double.MIN_VALUE };
			gbl_panel_border.rowWeights = new double[]
			{ 1.0, Double.MIN_VALUE };
			panel_border.setLayout(gbl_panel_border);
			GridBagConstraints gbc_panel_sceneContainer = new GridBagConstraints();
			gbc_panel_sceneContainer.fill = GridBagConstraints.BOTH;
			gbc_panel_sceneContainer.gridwidth = 2;
			gbc_panel_sceneContainer.insets = new Insets(2, 2, 2, 2);
			gbc_panel_sceneContainer.gridx = 0;
			gbc_panel_sceneContainer.gridy = 0;
			panel_border.add(getPanel_sceneContainer(), gbc_panel_sceneContainer);
		}
		return panel_border;
	}

	/**
	 * @param visible
	 */
	public void setGridVisible(boolean visible)
	{
		getPanel_sceneContainer().setGridVisible(visible);
	}

	/**
	 * @param visible
	 */
	public void setAxesVisible(boolean visible)
	{
		getPanel_sceneContainer().setAxesVisible(visible);
	}

	/**
	 * @param visible
	 */
	public void setBaricentrumVisible(boolean visible)
	{
		getPanel_sceneContainer().setBaricentrumVisible(visible);
	}
	
	/**
	 * @param visible
	 */
	public void setLocalCoordinatesVisible(boolean visible)
	{
		getPanel_sceneContainer().setLocalCoordinatesVisible(visible);
	}

	/**
	 * 
	 */
	public void manageAgreementChange()
	{
		getPanel_sceneContainer().manageAgreementChange();
	}
	
	/**
	 * 
	 */
	public void updateAgreementsGraphics()
	{
		getPanel_sceneContainer().updateAgreementsGraphics();
	}
	
	/* (non-Javadoc)
	 * @see it.diunipi.volpi.sycamore.gui.SycamorePanel#reset()
	 */
	@Override
	public void reset()
	{
		getPanel_sceneContainer().reset();
		getPanel_sceneControl().reset();
	}
}
