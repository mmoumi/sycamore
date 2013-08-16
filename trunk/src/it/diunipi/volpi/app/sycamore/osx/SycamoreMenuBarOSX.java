package it.diunipi.volpi.app.sycamore.osx;

import it.diunipi.volpi.app.sycamore.SycamoreMenuBar;

import javax.swing.JSeparator;

import com.apple.eawt.AppEvent.PreferencesEvent;
import com.apple.eawt.Application;
import com.apple.eawt.PreferencesHandler;

/**
 * The Sycamore menu bar for Mac OS X
 * 
 * @author Valerio Volpi - vale.v@me.com
 */
public class SycamoreMenuBarOSX extends SycamoreMenuBar
{
	private static final long	serialVersionUID					= 5893076147267552708L;

	/**
	 * Default constructor
	 */
	public SycamoreMenuBarOSX(SycamoreAppOSX application)
	{
		super(application);
		setupMenuBar();
	}


	/* (non-Javadoc)
	 * @see it.diunipi.volpi.app.sycamore.SycamoreMenuBar#setupMenuBar()
	 */
	@Override
	protected void setupMenuBar()
	{
		// add the preferences menu
		this.setupPreferencesmenu();

		// add menu items under File menu
		getMenu_file().add(getMenuItem_new());
		getMenu_file().add(getMenuItem_newBatch());
		getMenu_file().add(getMenuItem_open());
		getMenu_file().add(getMenu_openRecent());
		getMenu_file().add(new JSeparator());
		getMenu_file().add(getMenuItem_closeWindow());
		getMenu_file().add(getMenuItem_save());
		getMenu_file().add(getMenuItem_saveAs());
		getMenu_file().add(new JSeparator());
		getMenu_file().add(getMenuItem_Import());
		getMenu_file().add(getMenuItem_Export());
		getMenu_file().add(new JSeparator());
		getMenu_file().add(getMenu_switchWorkspace());

		// add menu items under View menu
		getMenu_view().add(getCheckBoxmenuItem_axes());
		getMenu_view().add(getCheckBoxmenuItem_grid());
		getMenu_view().add(getCheckBoxmenuItem_visibilityRange());
		getMenu_view().add(getCheckBoxmenuItem_visibilityGraph());
		getMenu_view().add(getCheckBoxmenuItem_directions());
		getMenu_view().add(getCheckBoxmenuItem_localCoords());
		getMenu_view().add(getCheckBoxmenuItem_baricentrum());
		getMenu_view().add(getCheckBoxmenuItem_lights());
		getMenu_view().add(getCheckBoxmenuItem_visualSupports());

		// add menu items under Help menu
		getMenu_help().add(getMenuItem_help());

		// add menus to menubar
		this.add(getMenu_file());
		this.add(getMenu_view());
		this.add(getMenu_help());
	}

	/* (non-Javadoc)
	 * @see it.diunipi.volpi.app.sycamore.SycamoreMenuBar#setupPreferencesmenu()
	 */
	@Override
	protected void setupPreferencesmenu()
	{
		Application app = Application.getApplication();
		app.setPreferencesHandler(new PreferencesHandler()
		{
			@Override
			public void handlePreferences(PreferencesEvent arg0)
			{
				// TODO Auto-generated method stub
			}
		});
	}
}
