/**
 * 
 */
package it.diunipi.volpi.sycamore.plugins.agreements;

import it.diunipi.volpi.sycamore.engine.Point2D;
import it.diunipi.volpi.sycamore.engine.SycamoreRobot;
import it.diunipi.volpi.sycamore.engine.SycamoreEngine.TYPE;
import it.diunipi.volpi.sycamore.gui.SycamorePanel;
import it.diunipi.volpi.sycamore.gui.SycamoreSystem;
import it.diunipi.volpi.sycamore.util.PropertyManager;
import it.diunipi.volpi.sycamore.util.SycamoreProperty;
import it.diunipi.volpi.sycamore.util.SycamoreUtil;

import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.util.concurrent.Callable;

import net.xeoh.plugins.base.annotations.PluginImplementation;

import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.debug.Arrow;

/**
 * Absolute agreement in 2D. The local coordinate systems of the robots are different from the
 * global coordinate system, but they are all equal with each other. It is possible for the user to
 * define the translation, scale and rotation factors, but these factors will be applied to all the
 * local coordinate systems, with the result of an absolute agreement.
 * 
 * @author Valerio Volpi - vale.v@me.com
 */
@PluginImplementation
public class AbsoluteAgreement2D extends AgreementImpl<Point2D>
{
	/**
	 * Properties related to absolute agreement 2D
	 * 
	 * @author Valerio Volpi - vale.v@me.com
	 */
	private enum AbsoluteAgreement2DProperties implements SycamoreProperty
	{
		ABSOLUTE_AGREEMENT_2D_TRANSLATION_X("Translation X", "" + 0.0), 
		ABSOLUTE_AGREEMENT_2D_TRANSLATION_Y("Translation Y", "" + 0.0), 
		ABSOLUTE_AGREEMENT_2D_SCALE_X("Scale X", "" + 1.0), 
		ABSOLUTE_AGREEMENT_2D_SCALE_Y("Scale Y", "" + 1.0), 
		ABSOLUTE_AGREEMENT_2D_ROTATION("Rotation", "" + 0.0);

		private String	description		= null;
		private String	defaultValue	= null;

		/**
		 * Constructor.
		 */
		AbsoluteAgreement2DProperties(String description, String defaultValue)
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

	private AbsoluteAgreement2DSettingsPanel	panel_settings	= null;

	/**
	 * Default constructor
	 */
	public AbsoluteAgreement2D()
	{
		// create axes node
		SycamoreSystem.enqueueToJME(new Callable<Object>()
		{
			@Override
			public Object call() throws Exception
			{
				// red arrow for x axis
				Arrow arrowX = new Arrow(new Vector3f(2, 0, 0));
				arrowX.setLineWidth(4); // make arrow thicker
				Geometry xAxis = new Geometry("X coordinate axis", arrowX);
				Material matX = new Material(SycamoreSystem.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
				matX.getAdditionalRenderState().setWireframe(true);
				matX.setColor("Color", new ColorRGBA(0.7f, 0, 0, 1));
				xAxis.setMaterial(matX);
				xAxis.setLocalTranslation(Vector3f.ZERO);
				axesNode.attachChild(xAxis);

				// green arrow for y axis
				Arrow arrowY = new Arrow(new Vector3f(0, 2, 0));
				arrowY.setLineWidth(4); // make arrow thicker
				Geometry yAxis = new Geometry("Y coordinate axis", arrowY);
				Material matY = new Material(SycamoreSystem.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
				matY.getAdditionalRenderState().setWireframe(true);
				matY.setColor("Color", new ColorRGBA(0, 0.7f, 0, 1));
				yAxis.setMaterial(matY);
				yAxis.setLocalTranslation(Vector3f.ZERO);
				axesNode.attachChild(yAxis);

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
	public Point2D toLocalCoordinates(Point2D point)
	{
		// prepare awt Point2D points
		java.awt.geom.Point2D sourcePoint = SycamoreUtil.convertPoint2D(point);
		java.awt.geom.Point2D destPoint = new java.awt.geom.Point2D.Float();

		try
		{
			// transform using the inverse transform the source point into the dest point
			computeTransform().inverseTransform(sourcePoint, destPoint);
		}
		catch (NoninvertibleTransformException e)
		{
			e.printStackTrace();
		}

		// return dest point as a Sycamore Point2D object
		return SycamoreUtil.convertPoint2D(destPoint);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * it.diunipi.volpi.sycamore.plugins.agreements.Agreement#toGlobalCoordinates(it.diunipi.volpi
	 * .sycamore.model.SycamoreAbstractPoint, it.diunipi.volpi.sycamore.model.SycamoreAbstractPoint)
	 */
	@Override
	public Point2D toGlobalCoordinates(Point2D point)
	{
		// prepare awt Point2D points
		java.awt.geom.Point2D sourcePoint = SycamoreUtil.convertPoint2D(point);
		java.awt.geom.Point2D destPoint = new java.awt.geom.Point2D.Float();

		// transform the source point into the dest point
		computeTransform().transform(sourcePoint, destPoint);

		// return dest point as a Sycamore Point2D object
		return SycamoreUtil.convertPoint2D(destPoint);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.diunipi.volpi.sycamore.plugins.agreements.Agreement#getLocaTranslation()
	 */
	@Override
	public Vector3f getLocalTranslation()
	{
		return new Vector3f((float) getTranslationX(), (float) getTranslationY(), 0);
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
		float angleZ = (float) Math.toRadians(getRotation());

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
		return new Vector3f((float) getScaleX(), (float) getScaleY(), 0);
	}

	/**
	 * Returns an AffineTransform object that describe the transform of the system
	 * 
	 * @return
	 */
	private AffineTransform computeTransform()
	{
		AffineTransform transform = new AffineTransform();
		transform.translate(getTranslationX(), getTranslationY());
		transform.rotate(Math.toRadians(getRotation()));
		transform.scale(getScaleX(), getScaleY());

		return transform;
	}

	/**
	 * @return the translationX
	 */
	public static double getTranslationX()
	{
		return PropertyManager.getSharedInstance().getDoubleProperty(AbsoluteAgreement2DProperties.ABSOLUTE_AGREEMENT_2D_TRANSLATION_X);
	}

	/**
	 * @param translationX
	 *            the translationX to set
	 */
	public static void setTranslationX(double translationX)
	{
		PropertyManager.getSharedInstance().putProperty(AbsoluteAgreement2DProperties.ABSOLUTE_AGREEMENT_2D_TRANSLATION_X, translationX);
	}

	/**
	 * @return the translationY
	 */
	public static double getTranslationY()
	{
		return PropertyManager.getSharedInstance().getDoubleProperty(AbsoluteAgreement2DProperties.ABSOLUTE_AGREEMENT_2D_TRANSLATION_Y);
	}

	/**
	 * @param translationY
	 *            the translationY to set
	 */
	public static void setTranslationY(double translationY)
	{
		PropertyManager.getSharedInstance().putProperty(AbsoluteAgreement2DProperties.ABSOLUTE_AGREEMENT_2D_TRANSLATION_Y, translationY);
	}

	/**
	 * @return the scaleX
	 */
	public static double getScaleX()
	{
		return PropertyManager.getSharedInstance().getDoubleProperty(AbsoluteAgreement2DProperties.ABSOLUTE_AGREEMENT_2D_SCALE_X);
	}

	/**
	 * @param scaleX
	 *            the scaleX to set
	 */
	public static void setScaleX(double scaleX)
	{
		PropertyManager.getSharedInstance().putProperty(AbsoluteAgreement2DProperties.ABSOLUTE_AGREEMENT_2D_SCALE_X, scaleX);
	}

	/**
	 * @return the scaleY
	 */
	public static double getScaleY()
	{
		return PropertyManager.getSharedInstance().getDoubleProperty(AbsoluteAgreement2DProperties.ABSOLUTE_AGREEMENT_2D_SCALE_Y);
	}

	/**
	 * @param scaleY
	 *            the scaleY to set
	 */
	public static void setScaleY(double scaleY)
	{
		PropertyManager.getSharedInstance().putProperty(AbsoluteAgreement2DProperties.ABSOLUTE_AGREEMENT_2D_SCALE_Y, scaleY);
	}

	/**
	 * @return the rotation
	 */
	public static double getRotation()
	{
		return PropertyManager.getSharedInstance().getDoubleProperty(AbsoluteAgreement2DProperties.ABSOLUTE_AGREEMENT_2D_ROTATION);
	}

	/**
	 * @param rotation
	 *            the rotation to set
	 */
	public static void setRotation(double rotation)
	{
		PropertyManager.getSharedInstance().putProperty(AbsoluteAgreement2DProperties.ABSOLUTE_AGREEMENT_2D_ROTATION, rotation);
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
		return TYPE.TYPE_2D;
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
		return "Absolute agreement in 2D. The local coordinates systems are completely agreed.";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.diunipi.volpi.sycamore.plugins.SycamorePlugin#getPluginLongDescription()
	 */
	@Override
	public String getPluginLongDescription()
	{
		return "Absolute agreement in 2D. The local coordinate systems of the robots are different from the global coordinate system, " +
				"but they are all equal with each other. It is possible for the user to define the translation, scale and rotation factors, " +
				"but these factors will be applied to all the local coordinate systems, with the result of an absolute agreement.";
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
			panel_settings = new AbsoluteAgreement2DSettingsPanel();
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
	public void setRobot(SycamoreRobot<Point2D> owner)
	{
		// Nothing to do
	}
}
