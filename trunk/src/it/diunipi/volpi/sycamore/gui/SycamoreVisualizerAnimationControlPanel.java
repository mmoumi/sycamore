/**
 * 
 */
package it.diunipi.volpi.sycamore.gui;

import it.diunipi.volpi.sycamore.engine.SycamoreRobot;
import it.diunipi.volpi.sycamore.util.SycamoreFiredActionEvents;

import java.awt.event.ActionEvent;
import java.net.URL;
import java.util.Iterator;

import javax.swing.ImageIcon;

/**
 * @author Vale
 * 
 */
public class SycamoreVisualizerAnimationControlPanel extends SycamoreAnimationControlPanel
{
	private static final long	serialVersionUID	= -7279951210499783719L;

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

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.diunipi.volpi.sycamore.gui.SycamoreAnimationControlPanel#reset()
	 */
	@Override
	public void reset()
	{

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.diunipi.volpi.sycamore.gui.SycamoreAnimationControlPanel#updateGui()
	 */
	@Override
	public void updateGui()
	{

	}
}
