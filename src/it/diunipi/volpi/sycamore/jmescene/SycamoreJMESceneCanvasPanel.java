package it.diunipi.volpi.sycamore.jmescene;

import it.diunipi.volpi.sycamore.engine.SycamoreEngine;
import it.diunipi.volpi.sycamore.engine.SycamoreEngine.TYPE;
import it.diunipi.volpi.sycamore.gui.SycamorePanel;
import it.diunipi.volpi.sycamore.model.SycamoreRobot;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.Vector;
import java.util.concurrent.Callable;

import javax.swing.SwingUtilities;

import com.jme3.app.SimpleApplication;
import com.jme3.system.AppSettings;
import com.jme3.system.JmeCanvasContext;

/**
 * The panel that contains the canvas in which is represented the JME Scene.
 * 
 * @author Valerio Volpi - vale.v@me.com
 * 
 */
public class SycamoreJMESceneCanvasPanel extends SycamorePanel
{
	private static final long		serialVersionUID	= 4905756826517506842L;
	private JmeCanvasContext		context				= null;
	private Canvas					canvas				= null;
	private SycamoreJMEScene		scene				= null;
	private int						width				= 640;
	private int						height				= 480;
	private SycamoreEngine			appEngine			= null;
	private Vector<ActionListener>	listeners			= null;

	/**
	 * Default constructor.
	 */
	public SycamoreJMESceneCanvasPanel()
	{
		this.listeners = new Vector<ActionListener>();
		initialize();
	}

	/**
	 * Init the Gui
	 */
	private void initialize()
	{
		this.setLayout(new BorderLayout());
		this.addComponentListener(new ComponentListener()
		{

			// this listeners resizes the canvas properly
			public void componentShown(ComponentEvent arg0)
			{
				width = getWidth();
				height = getHeight();
				resizeCanvas(width, height);
			}

			public void componentResized(ComponentEvent arg0)
			{
				width = getWidth();
				height = getHeight();
				resizeCanvas(width, height);
			}

			public void componentMoved(ComponentEvent arg0)
			{

			}

			public void componentHidden(ComponentEvent arg0)
			{

			}
		});

		// wait a bit to let the canvas start
		try
		{
			Thread.sleep(500);
		}
		catch (InterruptedException ex)
		{
		}
	}

	/**
	 * Initializes the JME Scene
	 */
	public void initJMEScene()
	{
		SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
				createCanvas();

				add(canvas, BorderLayout.CENTER);

				startApp();
			}
		});
	}
	
	/**
	 * Stop the JME Scene
	 */
	public void disposeJMEScene()
	{
		scene.stop(true);
	}
	
	/**
	 * Reset the JME Scene
	 */
	public void resetJMEScene()
	{
		scene.reset();
	}

	/**
	 * Adds passed robot in scene
	 */
	public void addRobotInScene(SycamoreRobot<?> robot)
	{
		scene.addRobotInScene(robot);
	}

	/**
	 * Removes passed robot from scene
	 */
	public void removeRobotFromScene(SycamoreRobot<?> robot)
	{
		scene.removeRobotFromScene(robot);
	}
	
	/**
	 * Put the camera on the origin of the system
	 */
	public void cameraOnOrigin()
	{
		scene.cameraOnOrigin();
	}
	
	/**
	 * Put the camera on the baricentrum of the system
	 */
	public void cameraOnBaricentrum()
	{
		scene.cameraOnBaricentrum();
	}

	/**
	 * Creates the JME canvas
	 */
	private void createCanvas()
	{
		AppSettings settings = new AppSettings(true);
		settings.setWidth(width);
		settings.setHeight(height);

		scene = new SycamoreJMEScene();
		scene.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				// forward event
				fireActionEvent(e);
			}
		});
		
		// setup
		scene.setPauseOnLostFocus(false);
		scene.setSettings(settings);
		scene.createCanvas();

		context = (JmeCanvasContext) scene.getContext();
		canvas = context.getCanvas();
		canvas.setSize(settings.getWidth(), settings.getHeight());
	}

	/**
	 * Resizes the canvas
	 * 
	 * @param the
	 *            new witdth
	 * @param the
	 *            new height
	 */
	public void resizeCanvas(int newWidth, int newHeight)
	{
		canvas.setSize(newWidth, newHeight);
	}

	/**
	 * Start the JME application
	 */
	private void startApp()
	{
		scene.enqueue(new Callable<Void>()
		{
			public Void call()
			{
				if (scene instanceof SimpleApplication)
				{
					SimpleApplication simpleApp = (SimpleApplication) scene;
					simpleApp.getFlyByCamera().setDragToRotate(true);
				}
				return null;
			}
		});

	}

	/**
	 * Adds an <code>ActionListener</code> to the button.
	 * 
	 * @param listener
	 *            the <code>ActionListener</code> to be added
	 */
	public void addActionListener(ActionListener listener)
	{
		this.listeners.add(listener);
	}

	/**
	 * Removes an <code>ActionListener</code> from the button. If the listener is the currently set
	 * <code>Action</code> for the button, then the <code>Action</code> is set to <code>null</code>.
	 * 
	 * @param listener
	 *            the listener to be removed
	 */
	public void removeActionListener(ActionListener listener)
	{
		this.listeners.remove(listener);
	}

	/**
	 * Fires passed ActionEvent to all registered listeners, by calling <code>ActionPerformed</code>
	 * method on all of them.
	 * 
	 * @param e
	 */
	private void fireActionEvent(ActionEvent e)
	{
		for (ActionListener listener : this.listeners)
		{
			listener.actionPerformed(e);
		}
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

	/* (non-Javadoc)
	 * @see it.diunipi.volpi.sycamore.gui.SycamorePanel#setAppEngine(it.diunipi.volpi.sycamore.engine.SycamoreEngine)
	 */
	@Override
	public void setAppEngine(SycamoreEngine appEngine)
	{
		this.appEngine = appEngine;
		scene.setAppEngine(appEngine);
	}

	/* (non-Javadoc)
	 * @see it.diunipi.volpi.sycamore.gui.SycamorePanel#updateGui()
	 */
	@Override
	public void updateGui()
	{

	}

	/**
	 * Setup the JME scene to adapt to passed type
	 * 
	 * @param type
	 */
	public void setupScene(TYPE type)
	{
		scene.setupScene(type);
	}

	/**
	 * Shows/ hides the grid in the JME scene
	 * 
	 * @param visible
	 */
	public void setGridVisible(boolean visible)
	{
		scene.setGridVisible(visible);
	}

	/**
	 * Shows/ hides the axes in the JME scene
	 * 
	 * @param selected
	 */
	public void setAxesVisible(boolean visible)
	{
		scene.setAxesVisible(visible);
	}

	/**
	 * Shows/ hides the baricentrum in the JME scene
	 * 
	 * @param selected
	 */
	public void setBaricentrumVisible(boolean visible)
	{
		scene.setBaricentrumVisible(visible);
	}
	
	/* (non-Javadoc)
	 * @see it.diunipi.volpi.sycamore.gui.SycamorePanel#reset()
	 */
	@Override
	public void reset()
	{
		this.resetJMEScene();
	}
}
