/**
 * 
 */
package it.diunipi.volpi.sycamore.plugins.agreements;

import it.diunipi.volpi.sycamore.engine.Point3D;
import it.diunipi.volpi.sycamore.engine.SycamoreRobot;
import it.diunipi.volpi.sycamore.engine.SycamoreEngine.TYPE;
import it.diunipi.volpi.sycamore.gui.SycamorePanel;
import it.diunipi.volpi.sycamore.gui.SycamoreSystem;
import it.diunipi.volpi.sycamore.util.PropertyManager;
import it.diunipi.volpi.sycamore.util.SycamoreProperty;

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
public class AbsoluteAgreement3D extends AgreementImpl<Point3D>
{
	private enum AbsoluteAgreement3DProperties implements SycamoreProperty
	{
		ABSOLUTE_AGREEMENT_3D_TRANSLATION_X("Translation X", "" + 0.0),
		ABSOLUTE_AGREEMENT_3D_TRANSLATION_Y("Translation Y", "" + 0.0), 
		ABSOLUTE_AGREEMENT_3D_TRANSLATION_Z("Translation Z", "" + 0.0), 
		ABSOLUTE_AGREEMENT_3D_SCALE_X("Scale X", "" + 1.0), 
		ABSOLUTE_AGREEMENT_3D_SCALE_Y("Scale Y", "" + 1.0), 
		ABSOLUTE_AGREEMENT_3D_SCALE_Z("Scale Y", "" + 1.0), 
		ABSOLUTE_AGREEMENT_3D_ROTATION_X("Rotation X", "" + 0.0), 
		ABSOLUTE_AGREEMENT_3D_ROTATION_Y("Rotation Y", "" + 0.0), 
		ABSOLUTE_AGREEMENT_3D_ROTATION_Z("Rotation Z", "" + 0.0);

		private String	description		= null;
		private String	defaultValue	= null;

		/**
		 * 
		 */
		AbsoluteAgreement3DProperties(String description, String defaultValue)
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
	private static Node						axesNode		= new Node("Axes node");
	private AbsoluteAgreement3DSettingPanel	panel_settings	= null;

	/**
	 * Default constructor
	 */
	public AbsoluteAgreement3D()
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
		return new Vector3f((float) getTranslationX(), (float) getTranslationY(), (float) getTranslationZ());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.diunipi.volpi.sycamore.plugins.agreements.Agreement#getLocalRotation()
	 */
	@Override
	public Quaternion getLocalRotation()
	{
		float angleX = (float) Math.toRadians(getRotationX());
		float angleY = (float) Math.toRadians(getRotationY());
		float angleZ = (float) Math.toRadians(getRotationZ());

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
	 * @return the translationX
	 */
	public static double getTranslationX()
	{
		return PropertyManager.getSharedInstance().getDoubleProperty(AbsoluteAgreement3DProperties.ABSOLUTE_AGREEMENT_3D_TRANSLATION_X);
	}

	/**
	 * @param translationX
	 *            the translationX to set
	 */
	public static void setTranslationX(double translationX)
	{
		PropertyManager.getSharedInstance().putProperty(AbsoluteAgreement3DProperties.ABSOLUTE_AGREEMENT_3D_TRANSLATION_X, translationX);
	}

	/**
	 * @return the translationY
	 */
	public static double getTranslationY()
	{
		return PropertyManager.getSharedInstance().getDoubleProperty(AbsoluteAgreement3DProperties.ABSOLUTE_AGREEMENT_3D_TRANSLATION_Y);
	}

	/**
	 * @param translationY
	 *            the translationY to set
	 */
	public static void setTranslationY(double translationY)
	{
		PropertyManager.getSharedInstance().putProperty(AbsoluteAgreement3DProperties.ABSOLUTE_AGREEMENT_3D_TRANSLATION_Y, translationY);
	}

	/**
	 * @return the translationY
	 */
	public static double getTranslationZ()
	{
		return PropertyManager.getSharedInstance().getDoubleProperty(AbsoluteAgreement3DProperties.ABSOLUTE_AGREEMENT_3D_TRANSLATION_Z);
	}

	/**
	 * @param translationY
	 *            the translationY to set
	 */
	public static void setTranslationZ(double translationZ)
	{
		PropertyManager.getSharedInstance().putProperty(AbsoluteAgreement3DProperties.ABSOLUTE_AGREEMENT_3D_TRANSLATION_Z, translationZ);
	}

	/**
	 * @return the scaleX
	 */
	public static double getScaleX()
	{
		return PropertyManager.getSharedInstance().getDoubleProperty(AbsoluteAgreement3DProperties.ABSOLUTE_AGREEMENT_3D_SCALE_X);
	}

	/**
	 * @param scaleX
	 *            the scaleX to set
	 */
	public static void setScaleX(double scaleX)
	{
		PropertyManager.getSharedInstance().putProperty(AbsoluteAgreement3DProperties.ABSOLUTE_AGREEMENT_3D_SCALE_X, scaleX);
	}

	/**
	 * @return the scaleY
	 */
	public static double getScaleY()
	{
		return PropertyManager.getSharedInstance().getDoubleProperty(AbsoluteAgreement3DProperties.ABSOLUTE_AGREEMENT_3D_SCALE_Y);
	}

	/**
	 * @param scaleY
	 *            the scaleY to set
	 */
	public static void setScaleZ(double scaleZ)
	{
		PropertyManager.getSharedInstance().putProperty(AbsoluteAgreement3DProperties.ABSOLUTE_AGREEMENT_3D_SCALE_Z, scaleZ);
	}

	/**
	 * @return the scaleY
	 */
	public static double getScaleZ()
	{
		return PropertyManager.getSharedInstance().getDoubleProperty(AbsoluteAgreement3DProperties.ABSOLUTE_AGREEMENT_3D_SCALE_Z);
	}

	/**
	 * @param scaleY
	 *            the scaleY to set
	 */
	public static void setScaleY(double scaleY)
	{
		PropertyManager.getSharedInstance().putProperty(AbsoluteAgreement3DProperties.ABSOLUTE_AGREEMENT_3D_SCALE_Y, scaleY);
	}

	/**
	 * @return the rotation
	 */
	public static double getRotationX()
	{
		return PropertyManager.getSharedInstance().getDoubleProperty(AbsoluteAgreement3DProperties.ABSOLUTE_AGREEMENT_3D_ROTATION_X);
	}

	/**
	 * @param rotation
	 *            the rotation to set
	 */
	public static void setRotationX(double rotationX)
	{
		PropertyManager.getSharedInstance().putProperty(AbsoluteAgreement3DProperties.ABSOLUTE_AGREEMENT_3D_ROTATION_X, rotationX);
	}

	/**
	 * @return the rotation
	 */
	public static double getRotationY()
	{
		return PropertyManager.getSharedInstance().getDoubleProperty(AbsoluteAgreement3DProperties.ABSOLUTE_AGREEMENT_3D_ROTATION_Y);
	}

	/**
	 * @param rotation
	 *            the rotation to set
	 */
	public static void setRotationY(double rotationY)
	{
		PropertyManager.getSharedInstance().putProperty(AbsoluteAgreement3DProperties.ABSOLUTE_AGREEMENT_3D_ROTATION_Y, rotationY);
	}

	/**
	 * @return the rotation
	 */
	public static double getRotationZ()
	{
		return PropertyManager.getSharedInstance().getDoubleProperty(AbsoluteAgreement3DProperties.ABSOLUTE_AGREEMENT_3D_ROTATION_Z);
	}

	/**
	 * @param rotation
	 *            the rotation to set
	 */
	public static void setRotationZ(double rotationZ)
	{
		PropertyManager.getSharedInstance().putProperty(AbsoluteAgreement3DProperties.ABSOLUTE_AGREEMENT_3D_ROTATION_Z, rotationZ);
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
		return "Total agreement in 3D. The local coordinates systems are completely agreed.";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.diunipi.volpi.sycamore.plugins.SycamorePlugin#getPluginLongDescription()
	 */
	@Override
	public String getPluginLongDescription()
	{
		return "Total agreement in 3D. The local coordinates systems are completely agreed and there is a single origin, a single measure unit and a single orientation and rotation of axes for all the robots. The local coordinates system is different from the global one, but is the same for all the robots.";
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
			panel_settings = new AbsoluteAgreement3DSettingPanel();
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
