package it.diunipi.volpi.app.sycamore;

import it.diunipi.volpi.sycamore.gui.SycamoreMainPanel;
import it.diunipi.volpi.sycamore.gui.SycamoreSplashScreen;
import it.diunipi.volpi.sycamore.gui.SycamoreWorkspaceSelectionPanel;
import it.diunipi.volpi.sycamore.gui.SycamoreSplashScreen.SPLASH_STATES;
import it.diunipi.volpi.sycamore.gui.SycamoreSystem;
import it.diunipi.volpi.sycamore.util.ApplicationProperties;
import it.diunipi.volpi.sycamore.util.PropertyManager;
import it.diunipi.volpi.sycamore.util.SycamoreFiredActionEvents;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 * The Sycamore Application
 * 
 * @author Valerio Volpi - vale.v@me.com
 */
public abstract class SycamoreApp extends JFrame
{
	/**
	 * The thread that cares of showing the splash screen
	 * 
	 * @author Valerio Volpi - vale.v@me.com
	 */
	private class SplashThread extends Thread
	{
		private SycamoreSplashScreen	splashScreen	= null;

		/**
		 * Default constructor.
		 */
		public SplashThread()
		{
			splashScreen = new SycamoreSplashScreen();
		}

		/**
		 * @return the splashScreen
		 */
		public SycamoreSplashScreen getSplashScreen()
		{
			return splashScreen;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Thread#run()
		 */
		@Override
		public void run()
		{
			super.run();
			splashScreen.showSplash();
		}
	}

	private static final long	serialVersionUID	= 6480681140397534382L;

	private SycamoreMainPanel	sycamoreMainPanel	= null;
	private SplashThread		splashThread		= null;

	/**
	 * Constructor for SycamoreApp
	 */
	public SycamoreApp()
	{
		setMinimumSize(new Dimension(850, 500));
		// performs the operations preliminary to initialization
		initialize_pre();

		// performs the operations of initialization
		initialize();

		// performs the operations after the initialization
		initialize_post();
	}

	/**
	 * This method contains the operations that are performed before the creation of the GUI
	 * elements
	 */
	protected void initialize_pre()
	{
		// try loading workspace path
		String workspace = PropertyManager.getSharedInstance().getProperty(ApplicationProperties.WORKSPACE_DIR.getName());
		if (workspace == null)
		{
			int retVal = JOptionPane.showOptionDialog(this, new SycamoreWorkspaceSelectionPanel(), "Select workspace", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, null, null);
			if (retVal != JOptionPane.OK_OPTION)
			{
				System.exit(0);
			}
		}
		
		
		// init system
		SycamoreSystem.initialize();

		// configure panel behavior on closing
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.addWindowListener(new WindowAdapter()
		{
			@Override
			public void windowClosed(WindowEvent e)
			{
				disposeApplication();
				System.exit(0);
			}
		});
	}
	
	/**
	 * Dispose the Sycamore application
	 */
	private void disposeApplication()
	{
		// stop 3D scene on window closing
		getSycamoreMainPanel().disposeJMEScene();
		PropertyManager.getSharedInstance().dispose();
	}

	/**
	 * This method creates the GUI elements
	 */
	protected void initialize()
	{
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWeights = new double[]
		{ 1.0 };
		gridBagLayout.rowWeights = new double[]
		{ 1.0 };
		getContentPane().setLayout(gridBagLayout);

		GridBagConstraints gbc_sycamoreMainPanel = new GridBagConstraints();
		gbc_sycamoreMainPanel.insets = new Insets(0, 0, 0, 0);
		gbc_sycamoreMainPanel.fill = GridBagConstraints.BOTH;
		gbc_sycamoreMainPanel.gridheight = 1;
		gbc_sycamoreMainPanel.gridx = 0;
		gbc_sycamoreMainPanel.gridy = 0;
		getContentPane().add(getSycamoreMainPanel(), gbc_sycamoreMainPanel);
	}

	/**
	 * @return the sideBarpanel
	 */
	protected SycamoreMainPanel getSycamoreMainPanel()
	{
		if (sycamoreMainPanel == null)
		{
			sycamoreMainPanel = new SycamoreMainPanel();
			sycamoreMainPanel.setEnabled(false);
			sycamoreMainPanel.initJMEScene();
			sycamoreMainPanel.addActionListener(new java.awt.event.ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent e)
				{
					if (e.getActionCommand().equals(SycamoreFiredActionEvents.JME_SCENE_READY.name()))
					{
						// when scene 3D is ready, close the splash screen
						splashThread.getSplashScreen().setCurrentSplashState(SPLASH_STATES.DONE);
						sycamoreMainPanel.setEnabled(true);
					}
				}
			});
		}
		return sycamoreMainPanel;
	}

	/**
	 * This method contains the operations that are performed after the creation of the GUI elements
	 */
	protected void initialize_post()
	{
		setSize(new Dimension(1280, 800));
		setVisible(true);

		// show splash screen over GUI panel
		this.splashThread = new SplashThread();
		this.splashThread.start();

		// sets default engine to created app
		this.setTitle("Sycamore - Workspace at " + SycamoreSystem.getWorkspace().getAbsolutePath());
		this.updateGui();
		this.getSycamoreMainPanel().refreshPlugins();

		try
		{
			// Sleep a bit to let the splash screen initialize
			Thread.sleep(200);
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}

		// set splash screen state
		splashThread.getSplashScreen().setCurrentSplashState(SPLASH_STATES.PREPARING_3D);

		SycamoreSystem.setMainFrame(this);
	}

	/**
	 * Updates the GUI in order to reflect changes in the engine
	 */
	public void updateGui()
	{
		getSycamoreMainPanel().updateGui();
	}
	
	/**
	 * Reboot the Application
	 */
	public void reboot()
	{
		disposeApplication();
		System.exit(0);
	}
}
