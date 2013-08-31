/**
 * 
 */
package it.diunipi.volpi.sycamore.gui;

import java.awt.*;
import javax.swing.*;

/**
 * The splash screen view. Appears over the GUI during the first loading phase of the application
 * 
 * @author Valerio Volpi - vale.v@me.com
 */
public class SycamoreSplashScreen extends JWindow
{
	/**
	 * The states of the splash screen. They represent the message that is displayed.
	 * 
	 * @author Valerio Volpi - vale.v@me.com
	 */
	public enum SPLASH_STATES
	{
		INITIALIZING("Initializing application..."), PREPARING_3D("Preparing 3D scene..."), DONE("Done Initializing.");

		private String	description	= null;

		/**
		 * Constructor.
		 * 
		 * @param description
		 */
		SPLASH_STATES(String description)
		{
			this.description = description;
		}

		/**
		 * @return the desription
		 */
		public String getDescription()
		{
			return description;
		}
	}

	private static final long	serialVersionUID		= 116887102959412078L;
	private SPLASH_STATES		currentSplashState		= SPLASH_STATES.INITIALIZING;
	private JLabel				label_icon				= null;
	private JLabel				label_loadingOperation	= null;
	private JProgressBar		progressBar_loading		= null;

	/**
	 * Constructor.
	 */
	public SycamoreSplashScreen()
	{
		// Set the window's bounds, centering the window
		int width = 500;
		int height = 250;
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (screen.width - width) / 2;
		int y = (screen.height - height) / 2;
		setBounds(x, y, width, height);

		initialize();
	}

	/**
	 * Init the GUI.
	 */
	private void initialize()
	{
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]
		{ 0, 0 };
		gridBagLayout.rowHeights = new int[]
		{ 0, 0, 0, 0 };
		gridBagLayout.columnWeights = new double[]
		{ 1.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[]
		{ 1.0, 0.0, 0.0, Double.MIN_VALUE };
		getContentPane().setLayout(gridBagLayout);
		getContentPane().setBackground(Color.WHITE);

		GridBagConstraints gbc_label_icon = new GridBagConstraints();
		gbc_label_icon.insets = new Insets(0, 0, 5, 0);
		gbc_label_icon.gridx = 0;
		gbc_label_icon.gridy = 0;
		getContentPane().add(getLabel_icon(), gbc_label_icon);

		GridBagConstraints gbc_label_loadingOperation = new GridBagConstraints();
		gbc_label_loadingOperation.anchor = GridBagConstraints.WEST;
		gbc_label_loadingOperation.insets = new Insets(2, 2, 2, 2);
		gbc_label_loadingOperation.gridx = 0;
		gbc_label_loadingOperation.gridy = 1;
		getContentPane().add(getLabel_loadingOperation(), gbc_label_loadingOperation);

		GridBagConstraints gbc_progressBar_loading = new GridBagConstraints();
		gbc_progressBar_loading.insets = new Insets(2, 2, 2, 2);
		gbc_progressBar_loading.fill = GridBagConstraints.HORIZONTAL;
		gbc_progressBar_loading.gridx = 0;
		gbc_progressBar_loading.gridy = 2;
		getContentPane().add(getProgressBar_loading(), gbc_progressBar_loading);
	}

	/**
	 * @return the label_Icon
	 */
	private JLabel getLabel_icon()
	{
		if (label_icon == null)
		{
			label_icon = new JLabel();
			label_icon.setOpaque(false);
			label_icon.setIcon(new ImageIcon(getClass().getResource("/it/diunipi/volpi/sycamore/resources/splash.png")));
		}
		return label_icon;
	}

	/**
	 * @return the label_loadingOperation
	 */
	private JLabel getLabel_loadingOperation()
	{
		if (label_loadingOperation == null)
		{
			label_loadingOperation = new JLabel("Loading Sycamore application...");
		}
		return label_loadingOperation;
	}

	/**
	 * @return the progressBar_loading
	 */
	private JProgressBar getProgressBar_loading()
	{
		if (progressBar_loading == null)
		{
			progressBar_loading = new JProgressBar();
			progressBar_loading.setIndeterminate(true);
		}
		return progressBar_loading;
	}

	/**
	 * @param currentSplashState
	 *            the currentSplashState to set
	 */
	public void setCurrentSplashState(SPLASH_STATES currentSplashState)
	{
		this.currentSplashState = currentSplashState;
		getLabel_loadingOperation().setText(currentSplashState.getDescription());
	}

	/**
	 * Show the splash screen. It will remain visible until someone sets a SPLASH_STATES.DONE as
	 * current state.
	 */
	public void showSplash()
	{
		// Display it
		setVisible(true);

		while (currentSplashState != SPLASH_STATES.DONE)
		{
			// wait a bit and check the state
			try
			{
				Thread.sleep(100);
			}
			catch (InterruptedException e)
			{
			}
		}

		// when state is SPLASH_STATES.DONE, the splash disappears.
		setVisible(false);
	}
}
