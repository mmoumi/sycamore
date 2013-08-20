/**
 * 
 */
package it.diunipi.volpi.sycamore.plugins.agreements;

import it.diunipi.volpi.sycamore.engine.SycamoreEngine.TYPE;
import it.diunipi.volpi.sycamore.gui.SycamorePanel;
import it.diunipi.volpi.sycamore.gui.SycamoreSystem;
import it.diunipi.volpi.sycamore.model.Point2D;
import it.diunipi.volpi.sycamore.model.SycamoreRobot;
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
 * @author Vale
 * 
 */
@PluginImplementation
public class TotalAgreementStatic2D extends StaticAgreement<Point2D>
{
	private enum TotalStaticAgreementProperties implements SycamoreProperty
	{
		TRANSLATION_X("Translation X", "" + 0.0), 
		TRANSLATION_Y("Translation Y", "" + 0.0), 
		SCALE_X("Scale X", "" + 1.0), 
		SCALE_Y("Scale Y", "" + 1.0), 
		ROTATION("Rotation", "" + 0.0);

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

	private TotalStaticAgreementSettingPanel	panel_settings	= null;

	/**
	 * Default constructor
	 */
	public TotalAgreementStatic2D()
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
		java.awt.geom.Point2D sourcePoint = SycamoreUtil.convertPoint2D(point);
		java.awt.geom.Point2D destPoint = new java.awt.geom.Point2D.Float();

		computeTransform().transform(sourcePoint, destPoint);

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
		java.awt.geom.Point2D sourcePoint = SycamoreUtil.convertPoint2D(point);
		java.awt.geom.Point2D destPoint = new java.awt.geom.Point2D.Float();
		
		try
		{
			computeTransform().inverseTransform(sourcePoint, destPoint);
		}
		catch (NoninvertibleTransformException e)
		{
			e.printStackTrace();
		}
		
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
	public static void setScaleY(double scaleY)
	{
		PropertyManager.getSharedInstance().putProperty(TotalStaticAgreementProperties.SCALE_Y.name(), scaleY);
	}

	/**
	 * @return the rotation
	 */
	public static double getRotation()
	{
		double rotation = PropertyManager.getSharedInstance().getDoubleProperty(TotalStaticAgreementProperties.ROTATION.name());
		if (Double.isInfinite(rotation))
		{
			rotation = Double.parseDouble(TotalStaticAgreementProperties.ROTATION.getDefaultValue());
		}
		
		return rotation;
	}

	/**
	 * @param rotation
	 *            the rotation to set
	 */
	public static void setRotation(double rotation)
	{
		PropertyManager.getSharedInstance().putProperty(TotalStaticAgreementProperties.ROTATION.name(), rotation);
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
		return "Total static agreement in 2D. The local coordinates systems are static";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.diunipi.volpi.sycamore.plugins.SycamorePlugin#getPluginLongDescription()
	 */
	@Override
	public String getPluginLongDescription()
	{
		return "Total agreement in 2D. The local coordinates systems are static, so they don't change during the simulation";
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
			panel_settings = new TotalStaticAgreementSettingPanel();
		}
		return panel_settings;
	}
	
	/* (non-Javadoc)
	 * @see it.diunipi.volpi.sycamore.plugins.agreements.Agreement#setOwner(it.diunipi.volpi.sycamore.model.SycamoreRobot)
	 */
	@Override
	public void setOwner(SycamoreRobot<Point2D> owner)
	{
		// Nothing to do
	}
}
