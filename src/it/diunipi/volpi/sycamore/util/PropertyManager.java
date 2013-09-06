/**
 * 
 */
package it.diunipi.volpi.sycamore.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;

/**
 * @author Vale
 * 
 */
public class PropertyManager
{
	private static final String		propertyFileName	= "Properties.prop";
	private static String			propertyPath;
	private static PropertyManager	sharedInstance		= null;

	private HashMap<String, String>	properties			= null;

	/**
	 * Private onstructor
	 */
	private PropertyManager()
	{
		String OS = System.getProperty("os.name");
		if (OS.contains("Mac") || OS.contains("OS X"))
		{
			propertyPath = System.getProperty("user.home") + "/Library/Sycamore/";
		}
		else if (OS.contains("Windows"))
		{
			propertyPath = System.getenv("APPDATA") + "\\Sycamore\\";
			// propertyPath = System.getProperty("user.home") + "\\Documents\\Sycamore\\Preferences\\";
		}
		else
		{
			propertyPath = System.getProperty("user.home") + "/.config/Sycamore/";
		}

		this.properties = new HashMap<String, String>();
		this.loadProperties();
	}

	/**
	 * 
	 */
	private void loadProperties()
	{
		this.putDefaultsProperties();

		try
		{
			ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(propertyPath + System.getProperty("file.separator") + propertyFileName));

			this.properties = (HashMap<String, String>) objectInputStream.readObject();

			objectInputStream.close();
		}
		catch (Exception e)
		{
			System.err.println("No properties file found. Fallback to default values...");
		}
	}

	/**
	 * 
	 */
	private void putDefaultsProperties()
	{
		ApplicationProperties[] properties = ApplicationProperties.values();

		for (ApplicationProperties prop : properties)
		{
			// default workspace is not loaded, but is asked
			if (prop != ApplicationProperties.WORKSPACE_DIR)
			{
				this.properties.put(prop.name(), prop.getDefaultValue());
			}
		}
	}

	/**
	 * 
	 */
	public void dispose()
	{
		this.storeProperties();
		this.properties.clear();
		this.properties = null;
	}

	/**
	 * 
	 */
	private void storeProperties()
	{
		try
		{
			File file = new File(propertyPath);
			if (!file.exists() || !file.isDirectory())
			{
				file.mkdir();
			}

			ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(propertyPath + System.getProperty("file.separator") + propertyFileName));

			objectOutputStream.writeObject(properties);

			objectOutputStream.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * @param name
	 * @return
	 */
	public String getProperty(String name)
	{
		String val = this.properties.get(name);
		if (val != null)
		{
			return val;
		}
		else
		{
			// look in default app properties
			ApplicationProperties[] appProperties = ApplicationProperties.values();
			for (int i = 0; i < appProperties.length; i++)
			{
				if (appProperties[i].name().equals(name))
				{
					return appProperties[i].getDefaultValue();
				}
			}

			// return no value
			return null;
		}
	}

	/**
	 * @param name
	 * @return
	 */
	public int getIntegerProperty(String name)
	{
		String val = this.properties.get(name);
		if (val != null)
		{
			// look in properties
			return Integer.parseInt(val);
		}
		else
		{
			// look in default app properties
			ApplicationProperties[] appProperties = ApplicationProperties.values();
			for (int i = 0; i < appProperties.length; i++)
			{
				if (appProperties[i].name().equals(name))
				{
					return Integer.parseInt(appProperties[i].getDefaultValue());
				}
			}

			// return no value
			return -1;
		}
	}

	/**
	 * @param name
	 * @return
	 */
	public float getFloatProperty(String name)
	{
		String val = this.properties.get(name);
		if (val != null)
		{
			return Float.parseFloat(val);
		}
		else
		{
			// look in default app properties
			ApplicationProperties[] appProperties = ApplicationProperties.values();
			for (int i = 0; i < appProperties.length; i++)
			{
				if (appProperties[i].name().equals(name))
				{
					return Float.parseFloat(appProperties[i].getDefaultValue());
				}
			}

			return 1.0f / 0.0f;
		}
	}

	/**
	 * @param name
	 * @return
	 */
	public double getDoubleProperty(String name)
	{
		String val = this.properties.get(name);
		if (val != null)
		{
			return Double.parseDouble(val);
		}
		else
		{
			// look in default app properties
			ApplicationProperties[] appProperties = ApplicationProperties.values();
			for (int i = 0; i < appProperties.length; i++)
			{
				if (appProperties[i].name().equals(name))
				{
					return Double.parseDouble(appProperties[i].getDefaultValue());
				}
			}

			return 1.0f / 0.0f;
		}
	}

	/**
	 * @param name
	 * @return
	 */
	public boolean getBooleanProperty(String name)
	{
		String val = this.properties.get(name);
		if (val != null)
		{
			return Boolean.parseBoolean(val);
		}
		else
		{
			// look in default app properties
			ApplicationProperties[] appProperties = ApplicationProperties.values();
			for (int i = 0; i < appProperties.length; i++)
			{
				if (appProperties[i].name().equals(name))
				{
					return Boolean.parseBoolean(appProperties[i].getDefaultValue());
				}
			}

			return false;
		}
	}

	/**
	 * @param name
	 * @param value
	 */
	public void putProperty(final String name, final String value)
	{
		this.properties.put(name, value);
	}

	/**
	 * @param name
	 * @param value
	 */
	public void putProperty(String name, int value)
	{
		this.properties.put(name, value + "");
	}

	/**
	 * @param name
	 * @param value
	 */
	public void putProperty(String name, float value)
	{
		this.properties.put(name, value + "");
	}

	/**
	 * @param name
	 * @param value
	 */
	public void putProperty(String name, double value)
	{
		this.properties.put(name, value + "");
	}

	/**
	 * @param name
	 * @param value
	 */
	public void putProperty(String name, boolean value)
	{
		this.properties.put(name, value + "");
	}

	/**
	 * @return the sharedInstance
	 */
	public static PropertyManager getSharedInstance()
	{
		if (sharedInstance == null)
		{
			sharedInstance = new PropertyManager();
		}
		return sharedInstance;
	}

	/**
	 * @return
	 */
	public String getPropertyFilePath()
	{
		return propertyPath + propertyFileName;
	}

}
