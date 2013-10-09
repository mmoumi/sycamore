/**
 * 
 */
package it.diunipi.volpi.sycamore.plugins.agreements;

import it.diunipi.volpi.sycamore.engine.Point3D;
import it.diunipi.volpi.sycamore.engine.SycamoreEngine.TYPE;
import it.diunipi.volpi.sycamore.engine.SycamoreRobot;
import it.diunipi.volpi.sycamore.gui.SycamorePanel;
import it.diunipi.volpi.sycamore.gui.SycamoreSystem;
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
 * Disorientation in 3D. No assumption can be made on any form of agreement between robots.
 * Basically directions of axes, orientations, measure unit and position of origin can be different
 * between a robot and another. In terms of of transformation factors, all the components
 * (translations, scales, signs of scales, rotations) are different between robots. In addition, the
 * axes could also be shuffled in a way that what is x for a robot corresponds to y for another
 * robot.
 * 
 * @author Valerio Volpi - vale.v@me.com
 */
@PluginImplementation
public class Disorientation3D extends AgreementImpl<Point3D>
{
	// node is static because it is the same for all the robots
	private Node					axesNode		= new Node("Axes node");

	private double					translationX	= SycamoreUtil.getRandomDouble(-4.0, 4.0);
	private double					translationY	= SycamoreUtil.getRandomDouble(-4.0, 4.0);
	private double					translationZ	= SycamoreUtil.getRandomDouble(-4.0, 4.0);

	private double					scaleFactor		= SycamoreUtil.getRandomDouble(0.5, 4);
	private int						scaleXSignum	= SycamoreUtil.getRandomBoolan() ? 1 : -1;
	private int						scaleYSignum	= SycamoreUtil.getRandomBoolan() ? 1 : -1;
	private int						scaleZSignum	= SycamoreUtil.getRandomBoolan() ? 1 : -1;
	
	private double					rotationX		= SycamoreUtil.getRandomDouble(0, 365);
	private double					rotationY		= SycamoreUtil.getRandomDouble(0, 365);
	private double					rotationZ		= SycamoreUtil.getRandomDouble(0, 365);

	private AgreementSettingsPanel	panel_settings	= null;

	/**
	 * Default constructor
	 */
	public Disorientation3D()
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

				// blue arrow for z axis
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
		float angleX = (float) Math.toRadians(rotationX);
		float angleY = (float) Math.toRadians(rotationY);
		float angleZ = (float) Math.toRadians(rotationZ);

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
	 * Returns a JME Transform object that describes the transforms of the system.
	 * 
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
	 * @return the scaleX
	 */
	public double getScaleX()
	{
		if (AgreementImpl.isFixMeasureUnit())
		{
			return scaleXSignum;
		}
		else
		{
			return scaleFactor * scaleXSignum;
		}
	}

	/**
	 * @return the scaleY
	 */
	public double getScaleY()
	{
		if (AgreementImpl.isFixMeasureUnit())
		{
			return scaleYSignum;
		}
		else
		{
			return scaleFactor * scaleYSignum;
		}
	}

	/**
	 * @return the scaleY
	 */
	public double getScaleZ()
	{
		if (AgreementImpl.isFixMeasureUnit())
		{
			return scaleZSignum;
		}
		else
		{
			return scaleFactor * scaleZSignum;
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
		return "Disorientation in 3D. No assumption can be made on any form of agreement between robots.";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.diunipi.volpi.sycamore.plugins.SycamorePlugin#getPluginLongDescription()
	 */
	@Override
	public String getPluginLongDescription()
	{
		return "Disorientation in 3D. No assumption can be made on any form of agreement between robots. Basically directions of axes, " +
				"orientations, measure unit and position of origin can be different between a robot and another. In terms of of transformation factors, " +
				"all the components (translations, scales, signs of scales, rotations) are different between robots. In addition, the axes could also be " +
				"shuffled in a way that what is x for a robot corresponds to y for another robot.";
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
			panel_settings = new AgreementSettingsPanel();
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

	public static void main(String[] args)
	{
		AbsoluteAgreement3D agreement = new AbsoluteAgreement3D();

		AbsoluteAgreement3D.setTranslationX(1);
		AbsoluteAgreement3D.setTranslationY(1);
		AbsoluteAgreement3D.setTranslationZ(1);
		AbsoluteAgreement3D.setScaleX(1);
		AbsoluteAgreement3D.setScaleY(1);
		AbsoluteAgreement3D.setScaleZ(1);
		AbsoluteAgreement3D.setRotationX(0);
		AbsoluteAgreement3D.setRotationY(0);
		AbsoluteAgreement3D.setRotationZ(0);

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
