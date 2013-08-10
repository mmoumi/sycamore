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
	N_KNOWN("n is known to robots", true + ""),
	FAIRNESS_COUNT("Fairness count", 10 + ""), 
	WORKSPACE_DIR("Workspace dir", System.getProperty("user.home") + System.getProperty("file.separator") + "Documents"
			+ System.getProperty("file.separator") + "Sycamore");

	private String	name			= null;
	private String	defaultValue	= null;

	/**
	 * 
	 */
	ApplicationProperties(String name, String defaultValue)
	{
		this.name = name;
		this.defaultValue = defaultValue;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.diunipi.volpi.sycamore.util.SycamoreProperty#getName()
	 */
	@Override
	public String getName()
	{
		return name;
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
			if (prop.getName().equals(name))
			{
				return prop;
			}
		}

		return null;
	}
}
