/**
 * 
 */
package it.diunipi.volpi.sycamore.gui;

import it.diunipi.volpi.app.sycamore.SycamoreApp.APP_MODE;
import it.diunipi.volpi.sycamore.engine.SycamoreRobot;
import it.diunipi.volpi.sycamore.util.SycamoreFiredActionEvents;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.net.URL;
import java.util.Iterator;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * A version of the animation control panel specific for Visualzer. Its appearence is exactly the
 * same, but its behavior is not. In particular, whith this panel a pressure of the stop button does
 * not reset the timelines as the same button in <code>SycamoreAnimationControlPanel</code> does,
 * but simply sets the ratios of all the robots to zero.
 * 
 * @author Valerio Volpi - vale.v@me.com
 */
public class SycamoreVisualizerAnimationControlPanel extends SycamoreAnimationControlPanel
{
	private static final long	serialVersionUID		= -7279951210499783719L;
	private JLabel				label_animationControl	= null;
	private JSlider				slider_animationControl	= null;
	private JLabel				label_fine_tune			= null;
	private JSpinner			spinner_fine_tune		= null;
	private JPanel				panel_controls			= null;

	/**
	 * @param appMode
	 */
	public SycamoreVisualizerAnimationControlPanel(APP_MODE appMode)
	{
		super(appMode);
	}

	
	/*
	 * (non-Javadoc)
	 * 
	 * @see it.diunipi.volpi.sycamore.gui.SycamoreAnimationControlPanel#
	 * getRoundedBorderPanel_controlContainer()
	 */
	@Override
	protected SycamoreRoundedBorderPanel getRoundedBorderPanel_controlContainer()
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
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see it.diunipi.volpi.sycamore.gui.SycamoreAnimationControlPanel#playImpl()
	 */
	@Override
	protected void playImpl()
	{
		// during play, show pause icon
		URL url = getClass().getResource("/it/diunipi/volpi/sycamore/resources/pause_50x50.png");
		getButton_play().setIcon(new ImageIcon(url));
		getButton_play().setToolTipText("Pause animation");

		getAppEngine().clearRatioSnapshot();

		getSlider_animationControl().setEnabled(false);
		getSpinner_fine_tune().setEnabled(false);

		SycamoreSystem.getVisualizerThread().play();

		fireActionEvent(new ActionEvent(this, 0, SycamoreFiredActionEvents.PLAY_ANIMATION.name()));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.diunipi.volpi.sycamore.gui.SycamoreAnimationControlPanel#pauseImpl(boolean)
	 */
	@Override
	protected void pauseImpl(boolean finishSimulation)
	{
		// in pause, show play icon
		URL url = getClass().getResource("/it/diunipi/volpi/sycamore/resources/play_50x50.png");
		getButton_play().setIcon(new ImageIcon(url));
		getButton_play().setToolTipText("Play animation");
		getButton_play().setSelected(false);

		getSlider_animationControl().setEnabled(true);
		getSpinner_fine_tune().setEnabled(true);

		SycamoreSystem.getVisualizerThread().pause();
		getAppEngine().makeRatioSnapshot();

		if (finishSimulation)
		{
			Iterator<SycamoreRobot> iterator = getAppEngine().getRobots().robotsIterator();

			while (iterator.hasNext())
			{
				SycamoreRobot robot = iterator.next();
				robot.setCurrentRatio(0);
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see it.diunipi.volpi.sycamore.gui.SycamoreAnimationControlPanel#setupAfterSimulationFinished()
	 */
	@Override
	public void setupAfterSimulationFinished()
	{
		super.setupAfterSimulationFinished();

		getSlider_animationControl().setEnabled(true);
		getSpinner_fine_tune().setEnabled(true);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.diunipi.volpi.sycamore.gui.SycamoreAnimationControlPanel#reset()
	 */
	@Override
	public void reset()
	{
		getSlider_animationControl().setValue(100);
		getSlider_animationControl().setEnabled(true);

		getSpinner_fine_tune().setValue(100.0);
		getSpinner_fine_tune().setEnabled(true);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.diunipi.volpi.sycamore.gui.SycamoreAnimationControlPanel#updateGui()
	 */
	@Override
	public void updateGui()
	{
		getSlider_animationControl().setEnabled(true);
		getSpinner_fine_tune().setEnabled(true);
	}
}
