/**
 * 
 */
package it.diunipi.volpi.sycamore.plugins.agreements;

import it.diunipi.volpi.sycamore.engine.Point3D;
import it.diunipi.volpi.sycamore.engine.SycamoreEngine.TYPE;
import it.diunipi.volpi.sycamore.engine.SycamoreRobot;
import it.diunipi.volpi.sycamore.gui.SycamorePanel;
import it.diunipi.volpi.sycamore.gui.SycamoreSystem;
import it.diunipi.volpi.sycamore.util.PropertyManager;
import it.diunipi.volpi.sycamore.util.SycamoreProperty;
import it.diunipi.volpi.sycamore.util.SycamoreUtil;

import java.util.concurrent.Callable;

import net.xeoh.plugins.base.annotations.PluginImplementation;

import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Transform;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.debug.Arrow;

/**
 * @author Vale
 * 
 */
@PluginImplementation
public class TwoAxes3D extends AgreementImpl<Point3D>
{
	private enum TwoAxes3DProperties implements SycamoreProperty
	{
		TWO_AXES_3D_AXES("Axes", "X,Y"), 
		TWO_AXES_3D_OTATION("Rotation", "" + 0.0);

		private String	description		= null;
		private String	defaultValue	= null;

		/**
		 * 
		 */
		TwoAxes3DProperties(String description, String defaultValue)
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

	// node is static because it is the same for all the robots
	private Node					axesNode		= new Node("Axes node");

	private double					translationX	= SycamoreUtil.getRandomDouble(-4.0, 4.0);
	private double					translationY	= SycamoreUtil.getRandomDouble(-4.0, 4.0);
	private double					translationZ	= SycamoreUtil.getRandomDouble(-4.0, 4.0);

	private double					scaleFactor		= SycamoreUtil.getRandomDouble(0.5, 4);
	private int						scaleXSignum	= SycamoreUtil.getRandomBoolan() ? 1 : -1;
	private int						scaleYSignum	= SycamoreUtil.getRandomBoolan() ? 1 : -1;
	private int						scaleZSignum	= SycamoreUtil.getRandomBoolan() ? 1 : -1;

	private TwoAxes3DSettingsPanel	panel_settings	= null;

	/**
	 * Default constructor
	 */
	public TwoAxes3D()
	{
		SycamoreSystem.enqueueToJME(new Callable<Object>()
		{
			@Override
			public Object call() throws Exception
			{
				Arrow arrowX = new Arrow(new Vector3f(2, 0, 0));
				arrowX.setLineWidth(4); // make arrow thicker
				Geometry xAxis = new Geometry("X coordinate axis", arrowX);
				Material matX = new Material(SycamoreSystem.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
				matX.getAdditionalRenderState().setWireframe(true);
				matX.setColor("Color", new ColorRGBA(0.7f, 0, 0, 1));
				xAxis.setMaterial(matX);
				xAxis.setLocalTranslation(Vector3f.ZERO);
				axesNode.attachChild(xAxis);

				Arrow arrowY = new Arrow(new Vector3f(0, 2, 0));
				arrowY.setLineWidth(4); // make arrow thicker
				Geometry yAxis = new Geometry("Y coordinate axis", arrowY);
				Material matY = new Material(SycamoreSystem.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
				matY.getAdditionalRenderState().setWireframe(true);
				matY.setColor("Color", new ColorRGBA(0, 0.7f, 0, 1));
				yAxis.setMaterial(matY);
				yAxis.setLocalTranslation(Vector3f.ZERO);
				axesNode.attachChild(yAxis);

				Arrow arrowZ = new Arrow(new Vector3f(0, 0, 2));
				arrowZ.setLineWidth(4); // make arrow thicker
				Geometry zAxis = new Geometry("Z coordinate axis", arrowZ);
				Material matZ = new Material(SycamoreSystem.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
				matZ.getAdditionalRenderState().setWireframe(true);
				matZ.setColor("Color", new ColorRGBA(0, 0, 0.7f, 1));
				zAxis.setMaterial(matZ);
				zAxis.setLocalTranslation(Vector3f.ZERO);
				axesNode.attachChild(zAxis);

				axesNode.updateGeometricState();

				return null;
			}
		});
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * it.diunipi.volpi.sycamore.plugins.agreements.Agreement#toLocalCoordinates(it.diunipi.volpi
	 * .sycamore.model.SycamoreAbstractPoint, it.diunipi.volpi.sycamore.model.SycamoreAbstractPoint)
	 */
	@Override
	public Point3D toLocalCoordinates(Point3D point)
	{
		Vector3f ret = Vector3f.ZERO;
		computeTransform().transformInverseVector(point.toVector3f(), ret);

		return new Point3D(ret);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * it.diunipi.volpi.sycamore.plugins.agreements.Agreement#toGlobalCoordinates(it.diunipi.volpi
	 * .sycamore.model.SycamoreAbstractPoint, it.diunipi.volpi.sycamore.model.SycamoreAbstractPoint)
	 */
	@Override
	public Point3D toGlobalCoordinates(Point3D point)
	{
		Vector3f ret = Vector3f.ZERO;
		computeTransform().transformVector(point.toVector3f(), ret);

		return new Point3D(ret);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.diunipi.volpi.sycamore.plugins.agreements.Agreement#getLocaTranslation()
	 */
	@Override
	public Vector3f getLocalTranslation()
	{
		return new Vector3f((float) translationX, (float) translationY, (float) translationZ);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.diunipi.volpi.sycamore.plugins.agreements.Agreement#getLocalRotation()
	 */
	@Override
	public Quaternion getLocalRotation()
	{
		float angleX = 0;
		float angleY = 0;
		float angleZ = 0;

		if (!getAxes().contains("X"))
		{
			angleX = (float) Math.toRadians(getRotation());
		}
		else if (!getAxes().contains("Y"))
		{
			angleY = (float) Math.toRadians(getRotation());
		}
		else if (!getAxes().contains("Z"))
		{
			angleZ = (float) Math.toRadians(getRotation());
		}

		return new Quaternion(new float[]
		{ angleX, angleY, angleZ });
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.diunipi.volpi.sycamore.plugins.agreements.Agreement#getLocalScale()
	 */
	@Override
	public Vector3f getLocalScale()
	{
		return new Vector3f((float) getScaleX(), (float) getScaleY(), (float) getScaleZ());
	}

	/**
	 * @return
	 */
	private Transform computeTransform()
	{
		Transform transform = new Transform();
		transform.setTranslation(getLocalTranslation());
		transform.setRotation(getLocalRotation());
		transform.setScale(getLocalScale());
		return transform;
	}

	/**
	 * @param rotation
	 *            the rotation to set
	 */
	public static void setAxes(String axis)
	{
		PropertyManager.getSharedInstance().putProperty(TwoAxes3DProperties.TWO_AXES_3D_AXES.name(), axis);
	}

	/**
	 * @return the rotation
	 */
	public static String getAxes()
	{
		String axis = PropertyManager.getSharedInstance().getProperty(TwoAxes3DProperties.TWO_AXES_3D_AXES.name());
		if (axis == null)
		{
			axis = "X";
		}

		return axis;
	}

	/**
	 * @return the rotation
	 */
	public static double getRotation()
	{
		return PropertyManager.getSharedInstance().getDoubleProperty(TwoAxes3DProperties.TWO_AXES_3D_OTATION);
	}

	/**
	 * @param rotation
	 *            the rotation to set
	 */
	public static void setRotation(double rotation)
	{
		PropertyManager.getSharedInstance().putProperty(TwoAxes3DProperties.TWO_AXES_3D_OTATION, rotation);
	}

	/**
	 * @return
	 */
	private int getSignumX()
	{
		return (getAxes().contains("X") ? 1 : scaleXSignum);
	}
	
	/**
	 * @return
	 */
	private int getSignumY()
	{
		return (getAxes().contains("Y") ? 1 : scaleYSignum);
	}

	/**
	 * @return
	 */
	private int getSignumZ()
	{
		return (getAxes().contains("Z") ? 1 : scaleZSignum);
	}

	/**
	 * @return the scaleX
	 */
	public double getScaleX()
	{
		if (AgreementImpl.isFixMeasureUnit())
		{
			return getSignumX();
		}
		else
		{
			return scaleFactor * (getSignumX());
		}
	}

	/**
	 * @return the scaleY
	 */
	public double getScaleY()
	{
		if (AgreementImpl.isFixMeasureUnit())
		{
			return getSignumY();
		}
		else
		{
			return scaleFactor * (getSignumY());
		}
	}

	/**
	 * @return the scaleY
	 */
	public double getScaleZ()
	{
		if (AgreementImpl.isFixMeasureUnit())
		{
			return getSignumZ();
		}
		else
		{
			return scaleFactor * (getSignumZ());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.diunipi.volpi.sycamore.plugins.agreements.Agreement#getAxesNode()
	 */
	@Override
	public Node getAxesNode()
	{
		return axesNode;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.diunipi.volpi.sycamore.plugins.SycamoreTypedPlugin#getType()
	 */
	@Override
	public TYPE getType()
	{
		return TYPE.TYPE_3D;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.diunipi.volpi.sycamore.plugins.SycamorePlugin#getAuthor()
	 */
	@Override
	public String getAuthor()
	{
		return "Valerio Volpi";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.diunipi.volpi.sycamore.plugins.SycamorePlugin#getPluginShortDescription()
	 */
	@Override
	public String getPluginShortDescription()
	{
		return "Agreement on two axes in 3D. Only four coordinate systems above north and south, east and west and up and down are agreed.";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.diunipi.volpi.sycamore.plugins.SycamorePlugin#getPluginLongDescription()
	 */
	@Override
	public String getPluginLongDescription()
	{
		return "Agreement on two axes in 3D. Each robot has its own coordinates system with its own origin, but the direction of two axes is agreed. This means that four cardinal points above north and south, east and west and up and down are agrreed and the orientation of the other two cardinal points could be different from one robot to another.";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.diunipi.volpi.sycamore.plugins.SycamorePlugin#getPanel_settings()
	 */
	@Override
	public SycamorePanel getPanel_settings()
	{
		if (panel_settings == null)
		{
			panel_settings = new TwoAxes3DSettingsPanel();
		}
		return panel_settings;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * it.diunipi.volpi.sycamore.plugins.agreements.Agreement#setOwner(it.diunipi.volpi.sycamore
	 * .model.SycamoreRobot)
	 */
	@Override
	public void setRobot(SycamoreRobot<Point3D> owner)
	{
		// Nothing to do
	}
}
