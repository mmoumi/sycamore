/**
 * 
 */
package it.diunipi.volpi.sycamore.plugins.agreements;

import it.diunipi.volpi.sycamore.engine.Point3D;
import it.diunipi.volpi.sycamore.engine.SycamoreRobot;
import it.diunipi.volpi.sycamore.engine.SycamoreEngine.TYPE;
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
 * @author Vale
 * 
 */
@PluginImplementation
public class NoAgreementStatic3D extends StaticAgreement<Point3D>
{
	// node is static because it is the same for all the robots
	private Node								axesNode		= new Node("Axes node");

	private double								translationX	= SycamoreUtil.getRandomDouble(-2.0, 2.0);
	private double								translationY	= SycamoreUtil.getRandomDouble(-2.0, 2.0);
	private double								translationZ	= SycamoreUtil.getRandomDouble(-2.0, 2.0);
	private double								scaleX			= (SycamoreUtil.getRandomBoolan() ? 1 : -1);
	private double								scaleY			= (SycamoreUtil.getRandomBoolan() ? 1 : -1);
	private double								scaleZ			= (SycamoreUtil.getRandomBoolan() ? 1 : -1);
	private double								rotationX		= SycamoreUtil.getRandomDouble(0, 365);
	private double								rotationY		= SycamoreUtil.getRandomDouble(0, 365);
	private double								rotationZ		= SycamoreUtil.getRandomDouble(0, 365);

	/**
	 * Default constructor
	 */
	public NoAgreementStatic3D()
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
		return new Vector3f((float) scaleX, (float) scaleY, (float) scaleZ);
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
		return null;
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
}
