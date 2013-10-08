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
import java.util.Set;

/**
 * The PropertyManager, the class that lets the objects access anywhere to properties, read them,
 * store them, modify them and get their default value.
 * 
 * @author Valerio Volpi - vale.v@me.com
 */
public class PropertyManager
{
	/**
	 * A generic property
	 * 
	 * @author Valerio Volpi - vale.v@me.com
	 */
	private static class GenericProperty implements SycamoreProperty
	{
		private static final long	serialVersionUID	= -141504471505098922L;
		private String				description			= null;
		private String				defaultValue		= null;

		/**
		 * Default constructor.
		 */
		public GenericProperty(String description, String defaultValue)
		{
			this.description = description;
			this.defaultValue = defaultValue;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see it.diunipi.volpi.sycamore.util.SycamoreProperty#getDescription()
		 */
		@Override
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
	}

	private static final String					propertyFileName	= "Properties.prop";
	private static String						propertyPath;
	private static PropertyManager				sharedInstance		= null;

	private HashMap<SycamoreProperty, String>	properties			= null;

	/**
	 * Private constructor.
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
		}
		else
		{
			propertyPath = System.getProperty("user.home") + "/.config/Sycamore/";
		}

		this.properties = new HashMap<SycamoreProperty, String>();
		this.loadProperties();
	}

	/**
	 * Load properties from the property file. If it fails in loading the properties, tries a
	 * fallback to default values for the properties it has.
	 */
	private void loadProperties()
	{
		try
		{
			ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(propertyPath + System.getProperty("file.separator") + propertyFileName));

			// read hashtable form property file
			this.properties = (HashMap<SycamoreProperty, String>) objectInputStream.readObject();

			objectInputStream.close();
		}
		catch (Exception e)
		{
			System.err.println("No properties file found. Fallback to default values...");
		}
	}

	/**
	 * Dispose the manger, deleting all the properties
	 */
	public void dispose()
	{
		this.storeProperties();
		this.properties.clear();
		this.properties = null;
	}

	/**
	 * Save the current properties in the property file. The old property file is overridden.
	 */
	private void storeProperties()
	{
		try
		{
			// get and eventually create the property path
			File file = new File(propertyPath);
			if (!file.exists() || !file.isDirectory())
			{
				file.mkdir();
			}

			ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(propertyPath + System.getProperty("file.separator") + propertyFileName));

			// write the property file
			objectOutputStream.writeObject(properties);

			objectOutputStream.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Returns the String value of the property with passed name
	 * 
	 * @param name
	 * @return
	 */
	public String getProperty(String name)
	{
		Set<SycamoreProperty> keys = this.properties.keySet();
		for (SycamoreProperty property : keys)
		{
			if (property.getDescription().equals(name))
			{
				return this.getProperty(property);
			}
		}

		return null;
	}

	/**
	 * Returns the String value of the property with passed name. It is possible to force this
	 * method to skip the fallback to default values if it does not found any value for the
	 * property.
	 * 
	 * @param name
	 * @return
	 */
	public String getProperty(SycamoreProperty property, boolean skipFallback)
	{
		return this.properties.get(property);
	}

	/**
	 * Returns the String value of the passed property
	 * 
	 * @param name
	 * @return
	 */
	public String getProperty(SycamoreProperty property)
	{
		String val = this.properties.get(property);
		if (val != null)
		{
			return val;
		}
		else
		{
			return property.getDefaultValue();
		}
	}

	/**
	 * Returns the integer value of the passed property. It is possible to force this method to skip
	 * the fallback to default values if it does not found any value for the property.
	 * 
	 * @param name
	 * @return
	 */
	public int getIntegerProperty(SycamoreProperty property, boolean skipFallback)
	{
		return Integer.parseInt(this.properties.get(property));
	}

	/**
	 * Returns the integer value of the passed property.
	 * 
	 * @param name
	 * @return
	 */
	public int getIntegerProperty(SycamoreProperty property)
	{
		String val = this.properties.get(property);
		if (val != null)
		{
			// look in properties
			return Integer.parseInt(val);
		}
		else
		{
			return Integer.parseInt(property.getDefaultValue());
		}
	}

	/**
	 * Returns the float value of the passed property. It is possible to force this method to skip
	 * the fallback to default values if it does not found any value for the property.
	 * 
	 * @param name
	 * @return
	 */
	public float getFloatProperty(SycamoreProperty property, boolean skipFallback)
	{
		return Float.parseFloat(this.properties.get(property));
	}

	/**
	 * Returns the float value of the passed property.
	 * 
	 * @param name
	 * @return
	 */
	public float getFloatProperty(SycamoreProperty property)
	{
		String val = this.properties.get(property);
		if (val != null)
		{
			return Float.parseFloat(val);
		}
		else
		{
			return Float.parseFloat(property.getDefaultValue());
		}
	}

	/**
	 * Returns the double value of the passed property. It is possible to force this method to skip
	 * the fallback to default values if it does not found any value for the property.
	 * 
	 * @param name
	 * @return
	 */
	public double getDoubleProperty(SycamoreProperty property, boolean skipFallback)
	{
		return Double.parseDouble(this.properties.get(property));
	}

	/**
	 * Returns the double value of the passed property.
	 * 
	 * @param name
	 * @return
	 */
	public double getDoubleProperty(SycamoreProperty property)
	{
		String val = this.properties.get(property);
		if (val != null)
		{
			return Double.parseDouble(val);
		}
		else
		{
			return Double.parseDouble(property.getDefaultValue());
		}
	}

	/**
	 * Returns the boolean value of the passed property. It is possible to force this method to skip
	 * the fallback to default values if it does not found any value for the property.
	 * 
	 * @param name
	 * @return
	 */
	public boolean getBooleanProperty(SycamoreProperty property, boolean skipFallback)
	{
		return Boolean.parseBoolean(this.properties.get(property));
	}

	/**
	 * Returns the boolean value of the passed property.
	 * 
	 * @param name
	 * @return
	 */
	public boolean getBooleanProperty(SycamoreProperty property)
	{
		String val = this.properties.get(property);
		if (val != null)
		{
			return Boolean.parseBoolean(val);
		}
		else
		{
			return Boolean.parseBoolean(property.getDefaultValue());
		}
	}

	/**
	 * Stores passed value associated to property with passed name
	 * 
	 * @param name
	 * @return
	 */
	public void putProperty(final String name, final String value)
	{
		GenericProperty property = new GenericProperty(name, value);
		this.putProperty(property, value);
	}

	/**
	 * Stores passed value associated to passed property
	 * 
	 * @param name
	 * @param value
	 */
	public void putProperty(final SycamoreProperty property, final String value)
	{
		this.properties.put(property, value);
	}

	/**
	 * Stores passed value associated to passed property
	 * 
	 * @param name
	 * @param value
	 */
	public void putProperty(SycamoreProperty property, int value)
	{
		this.properties.put(property, value + "");
	}

	/**
	 * Stores passed value associated to passed property
	 * 
	 * @param name
	 * @param value
	 */
	public void putProperty(SycamoreProperty property, float value)
	{
		this.properties.put(property, value + "");
	}

	/**
	 * Stores passed value associated to passed property
	 * 
	 * @param name
	 * @param value
	 */
	public void putProperty(SycamoreProperty property, double value)
	{
		this.properties.put(property, value + "");
	}

	/**
	 * Stores passed value associated to passed property
	 * 
	 * @param name
	 * @param value
	 */
	public void putProperty(SycamoreProperty property, boolean value)
	{
		this.properties.put(property, value + "");
	}

	/**
	 * Returns the only instance of the PropertyManager that exists in the system. This method is
	 * accessible from within any method of the application. It is also accessible to plugins.
	 * 
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
	 * Returns the path of the property file.
	 * 
	 * @return
	 */
	public String getPropertyFilePath()
	{
		return propertyPath + propertyFileName;
	}

}
