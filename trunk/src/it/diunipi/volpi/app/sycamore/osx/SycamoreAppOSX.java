package it.diunipi.volpi.app.sycamore.osx;

import it.diunipi.volpi.app.sycamore.SycamoreApp;

import java.awt.Window;
import java.lang.reflect.Method;

import com.apple.eawt.Application;
import com.apple.eawt.QuitStrategy;

/**
 * The Mac OS X version of Sycamore. Contains OSX specific code and does not run on any system
 * different from Mac OS X.
 * 
 * @author Valerio Volpi - vale.v@me.com
 * 
 */
public class SycamoreAppOSX extends SycamoreApp
{
	private static final long	serialVersionUID	= 710781679614398606L;
	private SycamoreMenuBarOSX	menuBar_main		= null;

	/**
	 * Main method. Creates and runs a new SycamoreApp
	 * 
	 * @param args
	 */
	public static void main(String[] args)
	{
		new SycamoreAppOSX();
	}

	/**
	 * Constructor for SycamoreApp
	 */
	public SycamoreAppOSX()
	{
		super();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.diunipi.volpi.app.sycamore.SycamoreApp#initialize_pre()
	 */
	@Override
	protected void initialize_pre()
	{
		super.initialize_pre();

		System.setProperty("apple.laf.useScreenMenuBar", "true");
		this.enableOSXFullscreen(this);
		
		Application.getApplication().setQuitStrategy(QuitStrategy.CLOSE_ALL_WINDOWS);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.diunipi.volpi.app.sycamore.SycamoreApp#initialize()
	 */
	@Override
	protected void initialize()
	{
		super.initialize();

		// apply the menubar
		setJMenuBar(getMenuBar_main());
	}

	/**
	 * Returns the main menu bar
	 * 
	 * @return
	 */
	@Override
	protected SycamoreMenuBarOSX getMenuBar_main()
	{
		if (menuBar_main == null)
		{
			menuBar_main = new SycamoreMenuBarOSX(this);
		}
		return menuBar_main;
	}

	/**
	 * Enables full screen feature on Mac OS X.
	 * 
	 * @param window
	 */
	private void enableOSXFullscreen(Window window)
	{
		if (window != null)
		{
			try
			{
				Class<?> util = Class.forName("com.apple.eawt.FullScreenUtilities");
				Class<?> params[] = new Class[]
				{ Window.class, Boolean.TYPE };
				Method method = util.getMethod("setWindowCanFullScreen", params);
				method.invoke(util, window, true);
			}
			catch (ClassNotFoundException e1)
			{
				e1.printStackTrace();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}
}
