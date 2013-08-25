package it.diunipi.volpi.sycamore.engine;

import it.diunipi.volpi.sycamore.gui.SycamoreSystem;

import com.jme3.material.Material;
import com.jme3.material.RenderState.BlendMode;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.debug.Arrow;
import com.jme3.scene.shape.Sphere;
import com.jme3.util.TangentBinormalGenerator;

/**
 * @see SycamoreRobot
 * 
 *      This class represents an 2 dimensional robot. It is a concrete implementation of the robot
 *      that fixes its type to be 2 dimensional. It uses a TYPE_2D enum element to identify the
 *      type, and a Point2D object to satisfy the generic.
 * 
 * @author Valerio Volpi - vale.v@me.com
 */
public class SycamoreRobot3D extends SycamoreRobot<Point3D>
{
	/**
	 * Default constructor.
	 * 
	 * @param algorithm
	 * @param engine
	 * @param startingPosition
	 */
	public SycamoreRobot3D(SycamoreEngine<Point3D> engine, Point3D startingPosition, float speed, ColorRGBA color, int maxLights)
	{
		super(engine, startingPosition, speed, color, maxLights);
	}
	
	/* (non-Javadoc)
	 * @see it.diunipi.volpi.sycamore.model.SycamoreRobot#createNewLightInstance()
	 */
	@Override
	protected SycamoreRobotLight<Point3D> createNewLightInstance()
	{
		return new SycamoreRobotLight3D(glassColor, getNewLightGeometry(glassColor));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.diunipi.volpi.sycamore.model.SycamoreRobot#setupGeometry()
	 */
	@Override
	protected void setupGeometry()
	{
		Material mat = new Material(SycamoreSystem.getAssetManager(), "Common/MatDefs/Light/Lighting.j3md");

		if (color == null)
		{
			color = ColorRGBA.Red;
		}
		
		mat.setBoolean("UseMaterialColors", true);
		mat.setColor("Ambient", color);
		mat.setColor("Diffuse", color);
		mat.setColor("Specular", ColorRGBA.White);
		mat.setFloat("Shininess", 50);

		this.sceneGeometry = new Geometry("Robot", new Sphere(25, 25, geometrySize));

		TangentBinormalGenerator.generate(this.sceneGeometry.getMesh(), true);
		this.sceneGeometry.setMaterial(mat);

		Arrow arrow = new Arrow(new Vector3f(1, 0, 0));
		arrow.setLineWidth(4); // make arrow thicker

		this.directionGeometry = new Geometry("direction arrow", arrow);
		Material arrowMat = new Material(SycamoreSystem.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
		arrowMat.getAdditionalRenderState().setWireframe(true);
		arrowMat.setColor("Color", color);
		this.directionGeometry.setMaterial(arrowMat);
		
		this.robotNode = new Node();
		this.robotNode.attachChild(sceneGeometry);
		
		if (SycamoreSystem.isMovementDirectionsVisible())
		{
			this.robotNode.attachChild(this.directionGeometry);
		}
		this.robotNode.setLocalTranslation(startingPosition.toVector3f());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.diunipi.volpi.sycamore.model.SycamoreRobot#getNewLightGeometry()
	 */
	@Override
	protected Geometry getNewLightGeometry(ColorRGBA color)
	{
		Material mat = new Material(SycamoreSystem.getAssetManager(), "Common/MatDefs/Light/Lighting.j3md");

		mat.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
		mat.setBoolean("UseMaterialColors", true);
		mat.setColor("Ambient", color);
		mat.setColor("Diffuse", color);
		mat.setColor("Specular", ColorRGBA.White);
		mat.setFloat("Shininess", 50);

		Geometry lightGeometry = new Geometry("Light", new Sphere(25, 25, lightSize));

		TangentBinormalGenerator.generate(lightGeometry.getMesh(), true);
		lightGeometry.setMaterial(mat);
		lightGeometry.setQueueBucket(Bucket.Transparent);

		Vector3f translation = sceneGeometry.getLocalTranslation().clone();
		translation.y = translation.y + (geometrySize + lightSize) * (getLights().size() + 1);

		lightGeometry.setLocalTranslation(translation);

		return lightGeometry;
	}
}
