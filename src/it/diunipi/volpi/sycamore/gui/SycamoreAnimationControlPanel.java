package it.diunipi.volpi.sycamore.gui;

import it.diunipi.volpi.sycamore.engine.SycamoreEngine;
import it.diunipi.volpi.sycamore.util.SycamoreFiredActionEvents;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.JToggleButton;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * The panel that contains the buttons to play/stop and control the animation
 * 
 * @author Valerio Volpi - vale.v@me.com
 */
public class SycamoreAnimationControlPanel extends SycamorePanel
{
	private static final long			serialVersionUID					= 2005516416916780325L;
	private JToggleButton				button_play							= null;
	private JButton						button_stop							= null;
	private SycamoreRoundedBorderPanel	roundedBorderPanel_controlContainer	= null;
	private JLabel						label_animationControl				= null;
	private JSlider						slider_animationControl				= null;
	private JLabel						label_animationSpeed				= null;
	private JSlider						slider_animationSpeed				= null;
	private SycamoreEngine				appEngine							= null;
	private JLabel						label_fine_tune						= null;
	private JSpinner					spinner_fine_tune					= null;
	private JPanel						panel_controls;

	/**
	 * Default constructor
	 */
	public SycamoreAnimationControlPanel()
	{
		setPreferredSize(new Dimension(1000, 85));
		setMinimumSize(new Dimension(200, 85));
		initialize();
	}

	/**
	 * Init the GUI
	 */
	private void initialize()
	{
		setBackground(new Color(0.69f, 0.69f, 0.69f));

		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]
		{ 0, 0, 0, 0, 0, 0 };
		gridBagLayout.rowHeights = new int[]
		{ 0, 0, 0 };
		gridBagLayout.columnWeights = new double[]
		{ 0.0, 1.0, 5.0, 1.0, 0.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[]
		{ 1.0, 1.0, Double.MIN_VALUE };
		setLayout(gridBagLayout);

		GridBagConstraints gbc_button_play = new GridBagConstraints();
		gbc_button_play.gridheight = 2;
		gbc_button_play.insets = new Insets(5, 50, 0, 5);
		gbc_button_play.gridx = 0;
		gbc_button_play.gridy = 0;
		add(getButton_play(), gbc_button_play);
		
		GridBagConstraints gbc_button_stop = new GridBagConstraints();
		gbc_button_stop.gridheight = 2;
		gbc_button_stop.anchor = GridBagConstraints.WEST;
		gbc_button_stop.insets = new Insets(5, 5, 0, 5);
		gbc_button_stop.gridx = 1;
		gbc_button_stop.gridy = 0;
		add(getButton_stop(), gbc_button_stop);

		GridBagConstraints gbc_roundedBorderPanel_controlContainer = new GridBagConstraints();
		gbc_roundedBorderPanel_controlContainer.gridheight = 2;
		gbc_roundedBorderPanel_controlContainer.fill = GridBagConstraints.BOTH;
		gbc_roundedBorderPanel_controlContainer.insets = new Insets(5, 5, 5, 5);
		gbc_roundedBorderPanel_controlContainer.gridx = 2;
		gbc_roundedBorderPanel_controlContainer.gridy = 0;
		add(getRoundedBorderPanel_controlContainer(), gbc_roundedBorderPanel_controlContainer);

		GridBagConstraints gbc_label_animationSpeed = new GridBagConstraints();
		gbc_label_animationSpeed.anchor = GridBagConstraints.SOUTH;
		gbc_label_animationSpeed.insets = new Insets(5, 5, 0, 50);
		gbc_label_animationSpeed.gridx = 4;
		gbc_label_animationSpeed.gridy = 0;
		add(getLabel_animationSpeed(), gbc_label_animationSpeed);

		GridBagConstraints gbc_slider_animationSpeed = new GridBagConstraints();
		gbc_slider_animationSpeed.insets = new Insets(0, 5, 5, 50);
		gbc_slider_animationSpeed.fill = GridBagConstraints.HORIZONTAL;
		gbc_slider_animationSpeed.anchor = GridBagConstraints.NORTH;
		gbc_slider_animationSpeed.gridx = 4;
		gbc_slider_animationSpeed.gridy = 1;
		add(getSlider_animationSpeed(), gbc_slider_animationSpeed);
	}

	/**
	 * @return the button_play
	 */
	protected JToggleButton getButton_play()
	{
		if (button_play == null)
		{
			button_play = new JToggleButton();
			URL url = getClass().getResource("/it/diunipi/volpi/sycamore/resources/play_50x50.png");
			button_play.setIcon(new ImageIcon(url));
			button_play.setOpaque(false);
			button_play.setToolTipText("Play animation");
			button_play.setMinimumSize(new Dimension(60, 60));
			button_play.setMaximumSize(new Dimension(60, 60));
			button_play.setPreferredSize(new Dimension(60, 60));
			button_play.setBorder(new EmptyBorder(getInsets()));
			button_play.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent e)
				{
					if (appEngine != null)
					{
						if (button_play.isSelected())
						{
							playImpl();
						}
						else
						{
							pauseImpl(false);
						}
					}
				}
			});
		}
		return button_play;
	}

	/**
	 * Perform actions after a play command
	 */
	protected void playImpl()
	{
		// during play, show pause icon
		URL url = getClass().getResource("/it/diunipi/volpi/sycamore/resources/pause_50x50.png");
		button_play.setIcon(new ImageIcon(url));
		button_play.setToolTipText("Pause animation");

		appEngine.clearRatioSnapshot();
		getSlider_animationControl().setEnabled(false);
		getSlider_animationControl().setValue(100);
		getSpinner_fine_tune().setEnabled(false);

		SycamoreSystem.getSchedulerThread().play();
		SycamoreSystem.getHumanPilotSchedulerThread().play();

		fireActionEvent(new ActionEvent(this, 0, SycamoreFiredActionEvents.PLAY_ANIMATION.name()));
	}

	/**
	 * Perform actions after a pause command. Eventually reset the scene
	 * 
	 * @param doReset
	 */
	protected void pauseImpl(boolean finishSimulation)
	{
		// in pause, show play icon
		URL url = getClass().getResource("/it/diunipi/volpi/sycamore/resources/play_50x50.png");
		button_play.setIcon(new ImageIcon(url));
		button_play.setToolTipText("Play animation");
		button_play.setSelected(false);

		if (finishSimulation)
		{
			appEngine.performMeasuresSimulationEnd();
			appEngine.manageSimulationFinished(true);

			SycamoreSystem.getSchedulerThread().pause();
			SycamoreSystem.getHumanPilotSchedulerThread().pause();

			getSlider_animationControl().setEnabled(false);
			getSpinner_fine_tune().setEnabled(false);
		}
		else
		{
			SycamoreSystem.getSchedulerThread().pause();
			SycamoreSystem.getHumanPilotSchedulerThread().pause();

			appEngine.makeRatioSnapshot();
			getSlider_animationControl().setEnabled(true);
			getSpinner_fine_tune().setEnabled(true);

			fireActionEvent(new ActionEvent(this, 0, SycamoreFiredActionEvents.PAUSE_ANIMATION.name()));
		}
	}

	/**
	 * Pause the animation.
	 */
	public void pauseAnimation()
	{
		JToggleButton button_play = getButton_play();
		if (button_play.isSelected())
		{
			button_play.doClick();
		}
	}

	/**
	 * @return the button_stop
	 */
	protected JButton getButton_stop()
	{
		if (button_stop == null)
		{
			button_stop = new JButton();
			button_stop.setOpaque(false);
			URL url = getClass().getResource("/it/diunipi/volpi/sycamore/resources/stop_50x50.png");
			button_stop.setIcon(new ImageIcon(url));
			button_stop.setToolTipText("Stop animation");
			button_stop.setMinimumSize(new Dimension(60, 60));
			button_stop.setMaximumSize(new Dimension(60, 60));
			button_stop.setPreferredSize(new Dimension(60, 60));
			button_stop.setBorder(new EmptyBorder(getInsets()));
			button_stop.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent e)
				{
					pauseImpl(true);
				}
			});
		}
		return button_stop;
	}

	/**
	 * @return the roundedBorderPanel_controlContainer
	 */
	private SycamoreRoundedBorderPanel getRoundedBorderPanel_controlContainer()
	{
		if (roundedBorderPanel_controlContainer == null)
		{
			roundedBorderPanel_controlContainer = new SycamoreRoundedBorderPanel();
			roundedBorderPanel_controlContainer.setMinimumSize(new Dimension(200, 40));
			roundedBorderPanel_controlContainer.setPreferredSize(new Dimension(450, 60));
			roundedBorderPanel_controlContainer.setOpaque(false);
			roundedBorderPanel_controlContainer.setShadowVisible(false);

			GridBagLayout gbl_roundedBorderPanel_controlContainer = new GridBagLayout();
			gbl_roundedBorderPanel_controlContainer.columnWidths = new int[]
			{ 0, 0 };
			gbl_roundedBorderPanel_controlContainer.rowHeights = new int[]
			{ 0 };
			gbl_roundedBorderPanel_controlContainer.columnWeights = new double[]
			{ 1.0, Double.MIN_VALUE };
			gbl_roundedBorderPanel_controlContainer.rowWeights = new double[]
			{ 1.0 };
			roundedBorderPanel_controlContainer.setLayout(gbl_roundedBorderPanel_controlContainer);
			GridBagConstraints gbc_panel_controls = new GridBagConstraints();
			gbc_panel_controls.insets = new Insets(2, 30, 2, 30);
			gbc_panel_controls.fill = GridBagConstraints.BOTH;
			gbc_panel_controls.gridx = 0;
			gbc_panel_controls.gridy = 0;
			roundedBorderPanel_controlContainer.add(getPanel_controls(), gbc_panel_controls);
		}
		return roundedBorderPanel_controlContainer;
	}

	/**
	 * @return the label_animationControl
	 */
	private JLabel getLabel_animationControl()
	{
		if (label_animationControl == null)
		{
			label_animationControl = new JLabel("Animation control (%):");
			label_animationControl.setFont(new Font("Lucida Grande", Font.BOLD, 14));
			label_animationControl.setForeground(Color.DARK_GRAY);
		}
		return label_animationControl;
	}

	/**
	 * @return the slider_animationControl
	 */
	protected JSlider getSlider_animationControl()
	{
		if (slider_animationControl == null)
		{
			slider_animationControl = new JSlider();
			slider_animationControl.setMaximumSize(new Dimension(32767, 28));
			slider_animationControl.setMinimumSize(new Dimension(36, 28));
			slider_animationControl.setPreferredSize(new Dimension(190, 28));
			slider_animationControl.setPaintTicks(true);
			slider_animationControl.setMinimum(0);
			slider_animationControl.setMaximum(100);
			slider_animationControl.setMinorTickSpacing(2);
			slider_animationControl.setMajorTickSpacing(10);
			slider_animationControl.setValue(100);
			slider_animationControl.setPaintLabels(true);
			slider_animationControl.addChangeListener(new ChangeListener()
			{
				@Override
				public void stateChanged(ChangeEvent e)
				{
					int value = slider_animationControl.getValue();
					appEngine.setRobotRatioPercentage(value);

					getSpinner_fine_tune().setValue((double) value);
				}
			});
		}
		return slider_animationControl;
	}

	/**
	 * @return the label_animationSpeed
	 */
	private JLabel getLabel_animationSpeed()
	{
		if (label_animationSpeed == null)
		{
			label_animationSpeed = new JLabel("Animation speed:");
			label_animationSpeed.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return label_animationSpeed;
	}

	/**
	 * @return the slider_animationSpeed
	 */
	private JSlider getSlider_animationSpeed()
	{
		if (slider_animationSpeed == null)
		{
			slider_animationSpeed = new JSlider();
			slider_animationSpeed.setOpaque(false);
			slider_animationSpeed.setPaintTicks(true);
			slider_animationSpeed.setMinimum(0);
			slider_animationSpeed.setMaximum(200);
			slider_animationSpeed.setMinorTickSpacing(10);
			slider_animationSpeed.setMajorTickSpacing(50);
			slider_animationSpeed.setValue(50);
			slider_animationSpeed.addChangeListener(new ChangeListener()
			{
				public void stateChanged(ChangeEvent arg0)
				{
					if (!changeLock && getAppEngine() != null)
					{
						getAppEngine().setAnimationSpeedMultiplier(getAnimationSpeedMultiplier());
					}
				}
			});
		}
		return slider_animationSpeed;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
	 */
	@Override
	protected void paintComponent(Graphics g)
	{
		super.paintComponent(g);

		int height = getHeight();
		int width = getWidth();

		g.setColor(Color.DARK_GRAY);
		g.drawLine(0, height - 1, width, height - 1);

		g.setColor(Color.GRAY);
		g.drawLine(0, height - 2, width, height - 2);
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
	 * it.diunipi.volpi.sycamore.gui.SycamorePanel#setAppEngine(it.diunipi.volpi.sycamore.engine
	 * .SycamoreEngine)
	 */
	@Override
	public void setAppEngine(SycamoreEngine appEngine)
	{
		this.appEngine = appEngine;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.diunipi.volpi.sycamore.gui.SycamorePanel#updateGui()
	 */
	@Override
	public void updateGui()
	{
		changeLock = true;
		if (this.appEngine != null && this.appEngine.isValid())
		{
			getButton_play().setEnabled(true);
			getButton_stop().setEnabled(true);

			getSlider_animationControl().setEnabled(true);
			getSpinner_fine_tune().setEnabled(true);

			getSlider_animationSpeed().setEnabled(true);
			getSlider_animationSpeed().setValue((int) appEngine.getAnimationSpeedMultiplier());
		}
		else
		{
			getButton_play().setEnabled(false);
			getButton_stop().setEnabled(false);

			getSlider_animationControl().setEnabled(false);
			getSpinner_fine_tune().setEnabled(false);

			getSlider_animationSpeed().setEnabled(false);
			getSlider_animationSpeed().setValue((int) SycamoreEngine.getDefaultAnimationSpeedMultiplier());
		}
		changeLock = false;
	}

	/**
	 * Setup the gui after the ending of a simulation.
	 */
	public void setupAfterSimulationFinished()
	{
		// in pause, show play icon
		URL url = getClass().getResource("/it/diunipi/volpi/sycamore/resources/play_50x50.png");
		button_play.setIcon(new ImageIcon(url));
		button_play.setToolTipText("Play animation");
		button_play.setSelected(false);

		getSlider_animationControl().setEnabled(true);
		getSpinner_fine_tune().setEnabled(true);
	}

	/**
	 * Returns the value selected in the animation speed slider.
	 * 
	 * @return
	 */
	public float getAnimationSpeedMultiplier()
	{
		return slider_animationSpeed.getValue();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.diunipi.volpi.sycamore.gui.SycamorePanel#reset()
	 */
	@Override
	public void reset()
	{
		// in pause, show play icon
		URL url = getClass().getResource("/it/diunipi/volpi/sycamore/resources/play_50x50.png");
		getButton_play().setIcon(new ImageIcon(url));
		getButton_play().setToolTipText("Play animation");
		getButton_play().setSelected(false);

		getSlider_animationControl().setValue(100);
		getSlider_animationControl().setEnabled(true);

		getSpinner_fine_tune().setValue(100.0);
		getSpinner_fine_tune().setEnabled(true);
	}

	/**
	 * @return label_fine_tune
	 */
	private JLabel getLabel_fine_tune()
	{
		if (label_fine_tune == null)
		{
			label_fine_tune = new JLabel("Fine-tune (%):");
			label_fine_tune.setFont(new Font("Lucida Grande", Font.BOLD, 14));
			label_fine_tune.setForeground(Color.DARK_GRAY);
		}
		return label_fine_tune;
	}

	/**
	 * @return spinner_fine_tune
	 */
	protected JSpinner getSpinner_fine_tune()
	{
		if (spinner_fine_tune == null)
		{
			spinner_fine_tune = new JSpinner();
			spinner_fine_tune.setModel(new SpinnerNumberModel(100.0, 0, 100.0, 0.1));
			spinner_fine_tune.addChangeListener(new ChangeListener()
			{
				@Override
				public void stateChanged(ChangeEvent e)
				{
					double value = (Double) getSpinner_fine_tune().getValue();
					appEngine.setRobotRatioPercentage((float) value);

					getSlider_animationControl().setValue((int) value);
				}
			});
		}
		return spinner_fine_tune;
	}

	/**
	 * @return panel_controls
	 */
	private JPanel getPanel_controls()
	{
		if (panel_controls == null)
		{
			panel_controls = new JPanel();
			GridBagLayout gbl_panel_controls = new GridBagLayout();
			gbl_panel_controls.columnWidths = new int[]
			{ 0, 0, 0 };
			gbl_panel_controls.rowHeights = new int[]
			{ 0, 0, 0 };
			gbl_panel_controls.columnWeights = new double[]
			{ 1.0, 0.0, Double.MIN_VALUE };
			gbl_panel_controls.rowWeights = new double[]
			{ 0.0, 1.0, Double.MIN_VALUE };
			panel_controls.setLayout(gbl_panel_controls);
			GridBagConstraints gbc_label_animationControl = new GridBagConstraints();
			gbc_label_animationControl.insets = new Insets(2, 2, 2, 2);
			gbc_label_animationControl.gridx = 0;
			gbc_label_animationControl.gridy = 0;
			panel_controls.add(getLabel_animationControl(), gbc_label_animationControl);
			GridBagConstraints gbc_label_fine_tune = new GridBagConstraints();
			gbc_label_fine_tune.insets = new Insets(2, 2, 2, 2);
			gbc_label_fine_tune.gridx = 1;
			gbc_label_fine_tune.gridy = 0;
			panel_controls.add(getLabel_fine_tune(), gbc_label_fine_tune);
			GridBagConstraints gbc_slider_animationControl = new GridBagConstraints();
			gbc_slider_animationControl.fill = GridBagConstraints.BOTH;
			gbc_slider_animationControl.insets = new Insets(2, 25, 2, 25);
			gbc_slider_animationControl.gridx = 0;
			gbc_slider_animationControl.gridy = 1;
			panel_controls.add(getSlider_animationControl(), gbc_slider_animationControl);
			GridBagConstraints gbc_spinner_fine_tune = new GridBagConstraints();
			gbc_spinner_fine_tune.insets = new Insets(2, 2, 2, 2);
			gbc_spinner_fine_tune.gridx = 1;
			gbc_spinner_fine_tune.gridy = 1;
			panel_controls.add(getSpinner_fine_tune(), gbc_spinner_fine_tune);
		}
		return panel_controls;
	}
}
