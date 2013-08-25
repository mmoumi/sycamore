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
public class TotalAgreementStatic3D extends StaticAgreement<Point3D>
{
	private enum TotalStaticAgreementProperties implements SycamoreProperty
	{
		TRANSLATION_X("Translation X", "" + 0.0), TRANSLATION_Y("Translation Y", "" + 0.0), TRANSLATION_Z("Translation Z", "" + 0.0), SCALE_X("Scale X", "" + 1.0), SCALE_Y("Scale Y", "" + 1.0), SCALE_Z(
				"Scale Y", "" + 1.0), ROTATION_X("Rotation X", "" + 0.0), ROTATION_Y("Rotation Y", "" + 0.0), ROTATION_Z("Rotation Z", "" + 0.0);

		private String	description		= null;
		private String	defaultValue	= null;

		/**
		 * 
		 */
		TotalStaticAgreementProperties(String description, String defaultValue)
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
	private static Node							axesNode		= new Node("Axes node");
	private TotalAgreementStatic3DSettingPanel	panel_settings	= null;

	/**
	 * Default constructor
	 */
	public TotalAgreementStatic3D()
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
		double translationX = PropertyManager.getSharedInstance().getDoubleProperty(TotalStaticAgreementProperties.TRANSLATION_X.name());
		if (Double.isInfinite(translationX))
		{
			translationX = Double.parseDouble(TotalStaticAgreementProperties.TRANSLATION_X.getDefaultValue());
		}

		return translationX;
	}

	/**
	 * @param translationX
	 *            the translationX to set
	 */
	public static void setTranslationX(double translationX)
	{
		PropertyManager.getSharedInstance().putProperty(TotalStaticAgreementProperties.TRANSLATION_X.name(), translationX);
	}

	/**
	 * @return the translationY
	 */
	public static double getTranslationY()
	{
		double translationY = PropertyManager.getSharedInstance().getDoubleProperty(TotalStaticAgreementProperties.TRANSLATION_Y.name());
		if (Double.isInfinite(translationY))
		{
			translationY = Double.parseDouble(TotalStaticAgreementProperties.TRANSLATION_Y.getDefaultValue());
		}

		return translationY;
	}

	/**
	 * @param translationY
	 *            the translationY to set
	 */
	public static void setTranslationY(double translationY)
	{
		PropertyManager.getSharedInstance().putProperty(TotalStaticAgreementProperties.TRANSLATION_Y.name(), translationY);
	}

	/**
	 * @return the translationY
	 */
	public static double getTranslationZ()
	{
		double translationZ = PropertyManager.getSharedInstance().getDoubleProperty(TotalStaticAgreementProperties.TRANSLATION_Z.name());
		if (Double.isInfinite(translationZ))
		{
			translationZ = Double.parseDouble(TotalStaticAgreementProperties.TRANSLATION_Z.getDefaultValue());
		}

		return translationZ;
	}

	/**
	 * @param translationY
	 *            the translationY to set
	 */
	public static void setTranslationZ(double translationZ)
	{
		PropertyManager.getSharedInstance().putProperty(TotalStaticAgreementProperties.TRANSLATION_Z.name(), translationZ);
	}

	/**
	 * @return the scaleX
	 */
	public static double getScaleX()
	{
		double scaleX = PropertyManager.getSharedInstance().getDoubleProperty(TotalStaticAgreementProperties.SCALE_X.name());
		if (Double.isInfinite(scaleX))
		{
			scaleX = Double.parseDouble(TotalStaticAgreementProperties.SCALE_X.getDefaultValue());
		}

		return scaleX;
	}

	/**
	 * @param scaleX
	 *            the scaleX to set
	 */
	public static void setScaleX(double scaleX)
	{
		PropertyManager.getSharedInstance().putProperty(TotalStaticAgreementProperties.SCALE_X.name(), scaleX);
	}

	/**
	 * @return the scaleY
	 */
	public static double getScaleY()
	{
		double scaleY = PropertyManager.getSharedInstance().getDoubleProperty(TotalStaticAgreementProperties.SCALE_Y.name());
		if (Double.isInfinite(scaleY))
		{
			scaleY = Double.parseDouble(TotalStaticAgreementProperties.SCALE_Y.getDefaultValue());
		}

		return scaleY;
	}

	/**
	 * @param scaleY
	 *            the scaleY to set
	 */
	public static void setScaleZ(double scaleZ)
	{
		PropertyManager.getSharedInstance().putProperty(TotalStaticAgreementProperties.SCALE_Z.name(), scaleZ);
	}

	/**
	 * @return the scaleY
	 */
	public static double getScaleZ()
	{
		double scaleZ = PropertyManager.getSharedInstance().getDoubleProperty(TotalStaticAgreementProperties.SCALE_Z.name());
		if (Double.isInfinite(scaleZ))
		{
			scaleZ = Double.parseDouble(TotalStaticAgreementProperties.SCALE_Z.getDefaultValue());
		}

		return scaleZ;
	}

	/**
	 * @param scaleY
	 *            the scaleY to set
	 */
	public static void setScaleY(double scaleY)
	{
		PropertyManager.getSharedInstance().putProperty(TotalStaticAgreementProperties.SCALE_Y.name(), scaleY);
	}

	/**
	 * @return the rotation
	 */
	public static double getRotationX()
	{
		double rotationX = PropertyManager.getSharedInstance().getDoubleProperty(TotalStaticAgreementProperties.ROTATION_X.name());
		if (Double.isInfinite(rotationX))
		{
			rotationX = Double.parseDouble(TotalStaticAgreementProperties.ROTATION_X.getDefaultValue());
		}

		return rotationX;
	}

	/**
	 * @param rotation
	 *            the rotation to set
	 */
	public static void setRotationX(double rotationX)
	{
		PropertyManager.getSharedInstance().putProperty(TotalStaticAgreementProperties.ROTATION_X.name(), rotationX);
	}

	/**
	 * @return the rotation
	 */
	public static double getRotationY()
	{
		double rotationY = PropertyManager.getSharedInstance().getDoubleProperty(TotalStaticAgreementProperties.ROTATION_Y.name());
		if (Double.isInfinite(rotationY))
		{
			rotationY = Double.parseDouble(TotalStaticAgreementProperties.ROTATION_Y.getDefaultValue());
		}

		return rotationY;
	}

	/**
	 * @param rotation
	 *            the rotation to set
	 */
	public static void setRotationY(double rotationY)
	{
		PropertyManager.getSharedInstance().putProperty(TotalStaticAgreementProperties.ROTATION_Y.name(), rotationY);
	}

	/**
	 * @return the rotation
	 */
	public static double getRotationZ()
	{
		double rotationZ = PropertyManager.getSharedInstance().getDoubleProperty(TotalStaticAgreementProperties.ROTATION_Z.name());
		if (Double.isInfinite(rotationZ))
		{
			rotationZ = Double.parseDouble(TotalStaticAgreementProperties.ROTATION_Z.getDefaultValue());
		}

		return rotationZ;
	}

	/**
	 * @param rotation
	 *            the rotation to set
	 */
	public static void setRotationZ(double rotationZ)
	{
		PropertyManager.getSharedInstance().putProperty(TotalStaticAgreementProperties.ROTATION_Z.name(), rotationZ);
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
		return "Total static agreement in 3D. The local coordinates systems are static";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.diunipi.volpi.sycamore.plugins.SycamorePlugin#getPluginLongDescription()
	 */
	@Override
	public String getPluginLongDescription()
	{
		return "Total agreement in 3D. The local coordinates systems are static, so they don't change during the simulation";
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
			panel_settings = new TotalAgreementStatic3DSettingPanel();
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
	public void setOwner(SycamoreRobot<Point3D> owner)
	{
		// Nothing to do
	}

	public static void main(String[] args)
	{
		TotalAgreementStatic3D agreement = new TotalAgreementStatic3D();

		TotalAgreementStatic3D.setTranslationX(1);
		TotalAgreementStatic3D.setTranslationY(1);
		TotalAgreementStatic3D.setTranslationZ(1);
		TotalAgreementStatic3D.setScaleX(1);
		TotalAgreementStatic3D.setScaleY(1);
		TotalAgreementStatic3D.setScaleZ(1);
		TotalAgreementStatic3D.setRotationX(0);
		TotalAgreementStatic3D.setRotationY(0);
		TotalAgreementStatic3D.setRotationZ(0);

		Point3D p1 = new Point3D(0, 0, 0);
		Point3D p2 = agreement.toLocalCoordinates(p1);
		Point3D p3 = agreement.toGlobalCoordinates(p2);

		System.out.println(p1 + " expressed in global coordinates");
		System.out.println("local: " + p2 + " -> global: " + p3);
		System.out.println();

		p1 = new Point3D(1, 0, 0);
		p2 = agreement.toLocalCoordinates(p1);
		p3 = agreement.toGlobalCoordinates(p2);

		System.out.println(p1 + " expressed in global coordinates");
		System.out.println("local: " + p2 + " -> global: " + p3);
		System.out.println();

		p1 = new Point3D(0, 1, 0);
		p2 = agreement.toLocalCoordinates(p1);
		p3 = agreement.toGlobalCoordinates(p2);

		System.out.println(p1 + " expressed in global coordinates");
		System.out.println("local: " + p2 + " -> global: " + p3);
		System.out.println();

		p1 = new Point3D(0, 0, 1);
		p2 = agreement.toLocalCoordinates(p1);
		p3 = agreement.toGlobalCoordinates(p2);

		System.out.println(p1 + " expressed in global coordinates");
		System.out.println("local: " + p2 + " -> global: " + p3);
		System.out.println();

		p1 = new Point3D(0, 0, 0);
		p2 = agreement.toGlobalCoordinates(p1);
		p3 = agreement.toLocalCoordinates(p2);

		System.out.println(p1 + " expressed in local coordinates");
		System.out.println("global: " + p2 + " -> local: " + p3);
		System.out.println();

		p1 = new Point3D(1, 0, 0);
		p2 = agreement.toGlobalCoordinates(p1);
		p3 = agreement.toLocalCoordinates(p2);

		System.out.println(p1 + " expressed in local coordinates");
		System.out.println("global: " + p2 + " -> local: " + p3);
		System.out.println();

		p1 = new Point3D(0, 1, 0);
		p2 = agreement.toGlobalCoordinates(p1);
		p3 = agreement.toLocalCoordinates(p2);

		System.out.println(p1 + " expressed in local coordinates");
		System.out.println("global: " + p2 + " -> local: " + p3);
		System.out.println();

		p1 = new Point3D(0, 0, 1);
		p2 = agreement.toGlobalCoordinates(p1);
		p3 = agreement.toLocalCoordinates(p2);

		System.out.println(p1 + " expressed in local coordinates");
		System.out.println("global: " + p2 + " -> local: " + p3);
		System.out.println();
	}
}
