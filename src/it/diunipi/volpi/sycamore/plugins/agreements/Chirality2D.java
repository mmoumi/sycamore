/**
 * 
 */
package it.diunipi.volpi.sycamore.plugins.agreements;

import it.diunipi.volpi.sycamore.engine.Point2D;
import it.diunipi.volpi.sycamore.engine.SycamoreEngine.TYPE;
import it.diunipi.volpi.sycamore.engine.SycamoreRobot;
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
 * Agreement on Chirality (orientation) of axes in 2D. The respective orientation of the axes is
 * fixed (i.e the x axis is placed at 90 degrees in clockwise direction from the y axis), but the
 * directions of the axes are not. In terms of transformation factors, the only element that is
 * agreed between the robots is the sign of the scale factor along each axis. All the other
 * elements (translation factor, scale factor, rotation factor) are different between a robot and
 * another.
 * 
 * @author Valerio Volpi - vale.v@me.com
 */
@PluginImplementation
public class Chirality2D extends AgreementImpl<Point2D>
{
	/**
	 * Properties related to chirality agreement 2D
	 * 
	 * @author Valerio Volpi - vale.v@me.com
	 */
	private enum Chirality2DProperties implements SycamoreProperty
	{
		CHIRALITY_2D_FLIP_X("FlipX", false + ""), 
		CHIRALITY_2D_FLIP_Y("FlipY", false + "");

		private String	description		= null;
		private String	defaultValue	= null;

		/**
		 * Constructor.
		 */
		Chirality2DProperties(String description, String defaultValue)
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

	private Node						axesNode		= new Node("Axes node");

	private double						translationX	= SycamoreUtil.getRandomDouble(-4.0, 4.0);
	private double						translationY	= SycamoreUtil.getRandomDouble(-4.0, 4.0);

	private double						scaleFactor		= SycamoreUtil.getRandomDouble(0.5, 4);
	private double						rotation		= SycamoreUtil.getRandomDouble(0, 365);

	private Chirality2DSettingsPanel	panel_settings	= null;

	/**
	 * Default constructor
	 */
	public Chirality2D()
	{
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
		return new Vector3f((float) translationX, (float) translationY, 0);
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
		float angleZ = (float) Math.toRadians(rotation);

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
		transform.translate(translationX, translationY);
		transform.rotate(Math.toRadians(rotation));
		transform.scale(getScaleX(), getScaleY());

		return transform;
	}

	/**
	 * @return the signum of the scale on x axis
	 */
	private int getSignumX()
	{
		return (isFlipX() ? -1 : 1);
	}

	/**
	 * @return the signum of the scale on y axis
	 */
	private int getSignumY()
	{
		return (isFlipY() ? -1 : 1);
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
	 * @return the rotation
	 */
	public static boolean isFlipX()
	{
		return PropertyManager.getSharedInstance().getBooleanProperty(Chirality2DProperties.CHIRALITY_2D_FLIP_X);
	}

	/**
	 * @param rotation
	 *            the rotation to set
	 */
	public static void setFlipX(Boolean flipX)
	{
		PropertyManager.getSharedInstance().putProperty(Chirality2DProperties.CHIRALITY_2D_FLIP_X, flipX);
	}

	/**
	 * @return the rotation
	 */
	public static boolean isFlipY()
	{
		return PropertyManager.getSharedInstance().getBooleanProperty(Chirality2DProperties.CHIRALITY_2D_FLIP_Y);
	}

	/**
	 * @param rotation
	 *            the rotation to set
	 */
	public static void setFlipY(Boolean flipY)
	{
		PropertyManager.getSharedInstance().putProperty(Chirality2DProperties.CHIRALITY_2D_FLIP_Y, flipY);
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
		return "Agreement on Chirality (orientation) of axes in 2D. Just the mutual orientation of the axes is agreed.";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.diunipi.volpi.sycamore.plugins.SycamorePlugin#getPluginLongDescription()
	 */
	@Override
	public String getPluginLongDescription()
	{
		return "Agreement on Chirality (orientation) of axes in 2D. The respective orientation of the axes is fixed (i.e the x axis is placed at 90 degrees in clockwise direction from the y axis), " +
				"but the directions of the axes are not. In terms of transformation factors, the only element that is agreed between the robots is the sign of the scale factor along each axis. All " +
				"the others elements (translation factor, scale factor, rotation factor) are different between a robot and another.";
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
			panel_settings = new Chirality2DSettingsPanel();
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
