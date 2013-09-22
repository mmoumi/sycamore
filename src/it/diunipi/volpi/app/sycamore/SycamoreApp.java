package it.diunipi.volpi.app.sycamore;

import it.diunipi.volpi.sycamore.engine.SycamoreEngine;
import it.diunipi.volpi.sycamore.engine.SycamoreEngine.TYPE;
import it.diunipi.volpi.sycamore.gui.SycamoreMainPanel;
import it.diunipi.volpi.sycamore.gui.SycamoreSplashScreen;
import it.diunipi.volpi.sycamore.gui.SycamoreSplashScreen.SPLASH_STATES;
import it.diunipi.volpi.sycamore.gui.SycamoreSystem;
import it.diunipi.volpi.sycamore.gui.SycamoreWorkspaceSelectionPanel;
import it.diunipi.volpi.sycamore.util.ApplicationProperties;
import it.diunipi.volpi.sycamore.util.PropertyManager;
import it.diunipi.volpi.sycamore.util.SycamoreFiredActionEvents;

import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FilenameFilter;
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.Locale;

import javax.swing.AbstractButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

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
			splashScreen.setAlwaysOnTop(true);
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
	private File				loadedProject		= null;
	private String				originalString		= null;

	/**
	 * Constructor for SycamoreApp
	 */
	public SycamoreApp()
	{
		setMinimumSize(new Dimension(400, 300));

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
		String workspace = PropertyManager.getSharedInstance().getProperty(ApplicationProperties.WORKSPACE_DIR, true);
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
				closeApplication();
			}
		});
	}

	/**
	 * Close application
	 */
	public void closeApplication()
	{
		saveApplicationState();
		disposeApplication();
		System.exit(0);
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
	 * Save the current state of the application
	 */
	private void saveApplicationState()
	{
		PropertyManager.getSharedInstance().putProperty(ApplicationProperties.WINDOW_X, getBounds().x);
		PropertyManager.getSharedInstance().putProperty(ApplicationProperties.WINDOW_Y, getBounds().y);
		PropertyManager.getSharedInstance().putProperty(ApplicationProperties.WINDOW_WIDTH, getBounds().width);
		PropertyManager.getSharedInstance().putProperty(ApplicationProperties.WINDOW_HEIGHT, getBounds().height);
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

		// call event of main panel
		getMenuBar_main().addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				if (e.getActionCommand().equals(SycamoreFiredActionEvents.LOAD_PLUGIN.name()))
				{
					getSycamoreMainPanel().loadPluginFromFileSystem();
				}
				else
				{
					getSycamoreMainPanel().updateVisibleElements((AbstractButton) e.getSource(), e.getActionCommand());
				}
			}
		});
	}

	/**
	 * Returns the main menu bar
	 * 
	 * @return
	 */
	protected abstract SycamoreMenuBar getMenuBar_main();

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
					else if (e.getActionCommand().equals(SycamoreFiredActionEvents.UPDATE_GUI.name()))
					{
						getMenuBar_main().updateGui();
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
		// window bounds
		int x = PropertyManager.getSharedInstance().getIntegerProperty(ApplicationProperties.WINDOW_X);
		int y = PropertyManager.getSharedInstance().getIntegerProperty(ApplicationProperties.WINDOW_Y);
		int width = PropertyManager.getSharedInstance().getIntegerProperty(ApplicationProperties.WINDOW_WIDTH);
		int height = PropertyManager.getSharedInstance().getIntegerProperty(ApplicationProperties.WINDOW_HEIGHT);

		setBounds(x, y, width, height);

		SycamoreSystem.setMainFrame(this);

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
			Thread.sleep(800);
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}

		setVisible(true);

		try
		{
			// Sleep a bit to let the splash screen initialize
			Thread.sleep(1000);
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}

		// set splash screen state
		splashThread.getSplashScreen().setCurrentSplashState(SPLASH_STATES.PREPARING_3D);
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

	/**
	 * Reset the application to its initial state
	 */
	public synchronized void reset()
	{
		boolean dirtyFlag = checkDirtyFlag();

		if (dirtyFlag)
		{
			String pt1 = "<html><body><p>Do you want to save the current project before starting a new simulation?<br>";
			String pt2 = "All unsaved data will be lost.</p></body></html>";
			String s = pt1 + pt2;

			int retVal = JOptionPane.showOptionDialog(null, s, "Save project?", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, null, null, null);
			if (retVal == JOptionPane.YES_OPTION)
			{
				if (loadedProject == null)
				{
					saveProject();
				}
				else
				{
					saveProject(loadedProject);
				}
			}
		}

		SycamoreSystem.reset();
		getSycamoreMainPanel().reset();

		loadedProject = null;
		originalString = null;
	}

	/**
	 * @return
	 */
	private boolean checkDirtyFlag()
	{
		SycamoreEngine engine = getSycamoreMainPanel().getAppEngine();
		if (loadedProject == null || engine == null)
		{
			return false;
		}
		else
		{
			try
			{
				// check dirty flag
				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
				DocumentBuilder builder = factory.newDocumentBuilder();
				Document document = builder.newDocument();

				// current project
				Element currentProject = engine.encode(factory, builder, document);
				document.appendChild(currentProject);

				TransformerFactory transformerFactory = TransformerFactory.newInstance();
				Transformer transformer = transformerFactory.newTransformer();
				DOMSource source = new DOMSource(document);
				StringWriter writer = new StringWriter();

				StreamResult result = new StreamResult(writer);
				transformer.transform(source, result);

				String currentString = writer.toString();

				// check
				if (currentString.equals(originalString))
				{
					return false;
				}
				else
				{
					return true;
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
				return true;
			}
		}
	}

	/**
	 * Returns a textual description of the application version
	 * 
	 * @return
	 */
	public static String getVersion()
	{
		return "2.0 beta 2";
	}

	/**
	 * Returns the build number of the application
	 * 
	 * @return
	 */
	public static String getBuildNumber()
	{
		return "20130905";
	}

	/**
	 * Returns the build date of the application
	 * 
	 * @return
	 */
	public static Date getBuildDate()
	{
		DateFormat format = DateFormat.getDateInstance(DateFormat.SHORT, Locale.ITALIAN);
		try
		{
			return format.parse("05/09/2013");
		}
		catch (ParseException e)
		{
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * @return the loadedProject
	 */
	public File getLoadedProject()
	{
		return loadedProject;
	}

	/**
	 * Opens a fileDialog to save a project
	 */
	public void saveProject()
	{
		// show a file dialog
		FileDialog dialog = new FileDialog((Frame) null, "Select the name of the project to be saved", FileDialog.SAVE);
		dialog.setDirectory(PropertyManager.getSharedInstance().getProperty(ApplicationProperties.WORKSPACE_DIR) + System.getProperty("file.separator") + "Projects");
		dialog.setFile("Project.xml");
		dialog.setVisible(true);

		// get the file
		File file = new File(dialog.getDirectory() + System.getProperty("file.separator") + dialog.getFile());
		if (file != null)
		{
			saveProject(file);
		}
	}

	/**
	 * Saves a project on passed file
	 */
	public void saveProject(File file)
	{
		try
		{
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

			Document doc = docBuilder.newDocument();

			doc.appendChild(encode(docFactory, docBuilder, doc));

			// write the content into xml file
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);

			StringWriter stringWriter = new StringWriter();
			StreamResult resultString = new StreamResult(stringWriter);
			StreamResult result = new StreamResult(file);

			transformer.transform(source, result);
			transformer.transform(source, resultString);

			loadedProject = file;
			originalString = resultString.toString();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * @param docFactory
	 * @param docBuilder
	 * @param doc
	 * @return
	 */
	private synchronized Node encode(DocumentBuilderFactory factory, DocumentBuilder builder, Document document)
	{
		// create element
		Element element = document.createElement("Sycamore");
		element.setAttribute("Version", getVersion());
		element.setAttribute("BuildNumber", getBuildNumber());
		element.setAttribute("type", getSycamoreMainPanel().getAppEngine().getType() + "");

		// children
		SycamoreEngine engine = getSycamoreMainPanel().getAppEngine();

		if (engine != null)
		{
			element.appendChild(engine.encode(factory, builder, document));
		}

		return element;
	}

	/**
	 * 
	 */
	public synchronized void loadProject()
	{
		// show a file dialog
		FileDialog dialog = new FileDialog((Frame) null, "Select the name of the project to be load", FileDialog.LOAD);
		dialog.setDirectory(PropertyManager.getSharedInstance().getProperty(ApplicationProperties.WORKSPACE_DIR) + System.getProperty("file.separator") + "Projects");
		dialog.setFilenameFilter(new FilenameFilter()
		{
			@Override
			public boolean accept(File dir, String name)
			{
				return name.endsWith(".xml");
			}
		});
		dialog.setVisible(true);

		// get the file
		File file = new File(dialog.getDirectory() + System.getProperty("file.separator") + dialog.getFile());
		if (file != null)
		{
			try
			{
				this.reset();
				
				DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
				Document doc = dBuilder.parse(file);

				doc.getDocumentElement().normalize();
				
				NodeList sycamoreNodes = doc.getElementsByTagName("Sycamore");
				
				// take just the first Sycamore Node
				Element sycamore = (Element) sycamoreNodes.item(0);
				
				String typeString = sycamore.getAttribute("type");
				TYPE type = TYPE.valueOf(typeString);
				
				SycamoreEngine engine = getSycamoreMainPanel().initEngine(type);
				if (!engine.decode(doc.getDocumentElement(), type))
				{
					JOptionPane.showMessageDialog(null, "Failed opening passed file.", "Open Failed", JOptionPane.ERROR_MESSAGE);
				}
				else
				{
					SycamoreSystem.getSchedulerThread().setEngine(engine);
					SycamoreSystem.getHumanPilotSchedulerThread().setEngine(engine);
					
					getSycamoreMainPanel().setAppEngine(engine);
					getSycamoreMainPanel().update3DScene(type);
					engine.makeRatioSnapshot();
					
					getSycamoreMainPanel().updateGui();	
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}
}
