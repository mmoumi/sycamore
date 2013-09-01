/**
 * 
 */
package it.diunipi.volpi.sycamore.util;

/**
 * @author Vale
 * 
 */
public enum ApplicationProperties implements SycamoreProperty
{
	WINDOW_X("X position of the app window", 0 + ""),
	WINDOW_Y("Y position of the app window", 0 + ""),
	WINDOW_WIDTH("Width of the app window", 1024 + ""),
	WINDOW_HEIGHT("Height of the app window", 600 + ""),
	GRID_VISIBLE("Grid in 3D scene is visible", true + ""),
	AXES_VISIBLE("Grid in 3D scene is visible", true + ""),
	BARICENTRUM_VISIBLE("Grid in 3D scene is visible", false + ""),
	ROBOTS_LIGHTS_VISIBLE("Grid in 3D scene is visible", true + ""),
	VISIBILITY_RANGES_VISIBLE("Grid in 3D scene is visible", true + ""),
	MOVEMENT_DIRECTIONS_VISIBLE("Grid in 3D scene is visible", false + ""),
	VISIBILITY_GRAPH_VISIBLE("Grid in 3D scene is visible", false + ""),
	LOCAL_COORDINATE_SYSTEMS_VISIBLE("Grid in 3D scene is visible", false + ""),
	VISUAL_ELEMENTS_VISIBLE("Grid in 3D scene is visible", true + ""),	
	N_KNOWN("n is known to robots", true + ""),
	FAIRNESS_COUNT("Fairness count", 10 + ""),
	EPSILON("Epsilon value", 0.0001 + ""),
	DEFAULT_ROBOT_SPEED("Default robot speed", 0.1 + ""),
	INITIAL_POSITION_MIN_X("Initial position min x", -10 + ""),
	INITIAL_POSITION_MAX_X("Initial position max x", 10 + ""),
	INITIAL_POSITION_MIN_Y("Initial position min y", -10 + ""),
	INITIAL_POSITION_MAX_Y("Initial position max y", 10 + ""),
	INITIAL_POSITION_MIN_Z("Initial position min z", -10 + ""),
	INITIAL_POSITION_MAX_Z("Initial position max z", 10 + ""),
	WORKSPACE_DIR("Workspace dir", System.getProperty("user.home") + System.getProperty("file.separator") + "Documents"
			+ System.getProperty("file.separator") + "Sycamore");

	private String	description			= null;
	private String	defaultValue	= null;

	/**
	 * 
	 */
	ApplicationProperties(String description, String defaultValue)
	{
		this.description = description;
		this.defaultValue = defaultValue;
	}

	/**
	 * @return the description
	 */
	public String getDescription()
	{
		return description;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.diunipi.volpi.sycamore.util.SycamoreProperty#getDefaultValue()
	 */
	@Override
	public String getDefaultValue()
	{
		return defaultValue;
	}

	/**
	 * Given a name, return the property with that name, or null
	 * 
	 * @param name
	 * @return
	 */
	public static ApplicationProperties getByName(String name)
	{
		ApplicationProperties[] values = ApplicationProperties.values();

		for (ApplicationProperties prop : values)
		{
			if (prop.name().equals(name))
			{
				return prop;
			}
		}

		return null;
	}
}
