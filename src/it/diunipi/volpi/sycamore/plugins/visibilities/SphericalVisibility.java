/**
 * 
 */
package it.diunipi.volpi.sycamore.plugins.visibilities;

import it.diunipi.volpi.sycamore.engine.Observation;
import it.diunipi.volpi.sycamore.engine.Point3D;
import it.diunipi.volpi.sycamore.engine.SycamoreEngine.TYPE;
import it.diunipi.volpi.sycamore.gui.SycamorePanel;
import it.diunipi.volpi.sycamore.gui.SycamoreSystem;
import it.diunipi.volpi.sycamore.util.SycamoreUtil;

import java.util.Vector;
import java.util.concurrent.Callable;

import net.xeoh.plugins.base.annotations.PluginImplementation;

import com.jme3.bounding.BoundingSphere;
import com.jme3.material.Material;
import com.jme3.material.RenderState.BlendMode;
import com.jme3.material.RenderState.FaceCullMode;
import com.jme3.math.ColorRGBA;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Sphere;
import com.jme3.util.TangentBinormalGenerator;

/**
 * 3D Visibility with the shape of a sphere. This visibility is modeled by a sphere centered in the
 * position of the robot and with a diameter equal to the visibility range. The surface of the
 * sphere is included in the visible area. Any object whose radial distance from the robot is less
 * than half the visibility range is considered visible.
 * 
 * @author Valerio Volpi - vale.v@me.com
 */
@PluginImplementation
public class SphericalVisibility extends VisibilityImpl<Point3D>
{
	private Geometry				sphere			= null;
	private VisibilitySettingsPanel	settingPanel	= null;

	/**
	 * Default constructor.
	 */
	public SphericalVisibility()
	{
		SycamoreSystem.enqueueToJME(new Callable<Object>()
		{
			@Override
			public Object call() throws Exception
			{
				// setup visibility range geometry
				ColorRGBA color = new ColorRGBA(1.0f, 0.0f, 0.0f, 0.15f);
				Material mat = new Material(SycamoreSystem.getAssetManager(), "Common/MatDefs/Light/Lighting.j3md");

				// setup material parameters for transparency and face culling
				mat.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
				mat.getAdditionalRenderState().setAlphaTest(true);
				mat.getAdditionalRenderState().setAlphaFallOff(0);
				mat.getAdditionalRenderState().setFaceCullMode(FaceCullMode.Off);
				mat.setBoolean("UseMaterialColors", true);
				mat.setColor("Ambient", color);
				mat.setColor("Diffuse", color);
				mat.setColor("Specular", ColorRGBA.White);
				mat.setFloat("Shininess", 50);

				// prepare a new sphere geormetry
				sphere = new Geometry("Sphere", new Sphere(25, 25, 1));
				sphere.setLocalScale(getVisibilityRange());
				sphere.center();

				sphere.setModelBound(new BoundingSphere());
				sphere.updateModelBound();

				// set the material to the sphere
				TangentBinormalGenerator.generate(sphere.getMesh(), true);
				sphere.setMaterial(mat);
				sphere.setQueueBucket(Bucket.Transparent);

				return null;
			}
		});
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
	 * @see
	 * it.diunipi.volpi.sycamore.plugins.Visibility#canPointsSee(it.diunipi.volpi.sycamore.model
	 * .SycamoreAbstractPoint, it.diunipi.volpi.sycamore.model.SycamoreAbstractPoint)
	 */
	@Override
	public boolean isPointVisible(Point3D point)
	{
		// if the distance between the point and center is less than the radius, the point is
		// inside the circle
		Point3D center = robot.getLocalPosition();
		if (center.distanceTo(point) < getVisibilityRange())
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.diunipi.volpi.sycamore.plugins.visibilities.Visibility#getPointInside()
	 */
	@Override
	public Point3D getPointInside()
	{
		Point3D center = robot.getLocalPosition();

		// get a random radius and a random angle
		float radius = SycamoreUtil.getRandomFloat(0, (getVisibilityRange() / 2));

		float phi = (float) (Math.random() * Math.PI);
		float theta = (float) (Math.random() * Math.PI * 2f);
		float tempsetX = center.x + ((float) (radius * Math.cos(theta) * Math.sin(phi)));
		float tempsetY = center.y + ((float) (radius * Math.sin(theta) * Math.sin(phi)));
		float tempsetZ = center.z + ((float) (radius * Math.cos(phi)));

		return new Point3D(tempsetX, tempsetY, tempsetZ);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.diunipi.volpi.sycamore.plugins.Visibility#filter(java.util.Vector)
	 */
	@Override
	public Vector<Observation<Point3D>> filter(Vector<Observation<Point3D>> observations)
	{
		Vector<Observation<Point3D>> filtered = new Vector<Observation<Point3D>>();

		// filter observations
		for (Observation<Point3D> observation : observations)
		{
			Point3D robotPosition = observation.getRobotPosition();
			// if the distance between the point and center is less than the radius, the point is
			// inside the circle
			if (isPointVisible(robotPosition))
			{
				filtered.add(observation);
			}
		}

		return filtered;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.diunipi.volpi.sycamore.plugins.Visibility#getVisibilityRangeGeometry()
	 */
	@Override
	public Geometry getVisibilityRangeGeometry()
	{
		return sphere;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.diunipi.volpi.sycamore.plugins.SycamorePlugin#getSettings()
	 */
	@Override
	public SycamorePanel getPanel_settings()
	{
		if (settingPanel == null)
		{
			settingPanel = new VisibilitySettingsPanel();
		}
		return settingPanel;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.diunipi.volpi.sycamore.plugins.SycamorePlugin#getPluginShortDescription()
	 */
	@Override
	public String getPluginShortDescription()
	{
		return "3D Visibility with the shape of a sphere.";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.diunipi.volpi.sycamore.plugins.SycamorePlugin#getLongDescription()
	 */
	@Override
	public String getPluginLongDescription()
	{
		return "3D Visibility with the shape of a sphere. This visibility is modeled by a sphere centered in the "
				+ "position of the robot and with a diameter equal to the visibility range. The surface of the sphere "
				+ "is included in the visible area. Any object whose radial distance from the robot is less than half " 
				+ "the visibility range is considered visible.";
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
	 * @see it.diunipi.volpi.sycamore.plugins.visibilities.VisibilityImpl#updateVisibilityGeometry()
	 */
	@Override
	public void updateVisibilityGeometry()
	{
		SycamoreSystem.enqueueToJME(new Callable<Object>()
		{
			/*
			 * (non-Javadoc)
			 * 
			 * @see java.util.concurrent.Callable#call()
			 */
			@Override
			public Object call() throws Exception
			{
				sphere.setLocalScale(getVisibilityRange());
				sphere.center();
				sphere.updateGeometricState();
				return null;
			}
		});
	}
}
