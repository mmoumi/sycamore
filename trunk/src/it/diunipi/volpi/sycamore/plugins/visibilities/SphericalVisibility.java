/**
 * 
 */
package it.diunipi.volpi.sycamore.plugins.visibilities;

import it.diunipi.volpi.sycamore.engine.SycamoreEngine.TYPE;
import it.diunipi.volpi.sycamore.gui.SycamorePanel;
import it.diunipi.volpi.sycamore.gui.SycamoreSystem;
import it.diunipi.volpi.sycamore.model.Observation;
import it.diunipi.volpi.sycamore.model.Point3D;
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
 * @author Vale
 * 
 */
@PluginImplementation
public class SphericalVisibility extends VisibilityImpl<Point3D>
{
	private Geometry				sphere			= null;
	private VisibilitySettingsPanel	settingPanel	= null;

	/**
	 * 
	 */
	public SphericalVisibility()
	{
		SycamoreSystem.enqueueToJME(new Callable<Object>()
		{
			@Override
			public Object call() throws Exception
			{
				ColorRGBA color = new ColorRGBA(1.0f, 0.0f, 0.0f, 0.15f);
				Material mat = new Material(SycamoreSystem.getAssetManager(), "Common/MatDefs/Light/Lighting.j3md");

				mat.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
				mat.getAdditionalRenderState().setAlphaTest(true);
				mat.getAdditionalRenderState().setAlphaFallOff(0);
				mat.getAdditionalRenderState().setFaceCullMode(FaceCullMode.Off);
				mat.setBoolean("UseMaterialColors", true);
				mat.setColor("Ambient", color);
				mat.setColor("Diffuse", color);
				mat.setColor("Specular", ColorRGBA.White);
				mat.setFloat("Shininess", 50);

				sphere = new Geometry("Sphere", new Sphere(25, 25, 1));
				sphere.setLocalScale(getVisibilityRange());
				sphere.center();

				sphere.setModelBound(new BoundingSphere());
				sphere.updateModelBound();

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
	public boolean isPointVisible(Point3D point1, Point3D point2)
	{
		// if the distance between the point and center is less than the radius, the point is
		// inside the circle
		if (point1.distanceTo(point2) < getVisibilityRange())
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
	public Point3D getPointInside(Point3D center)
	{
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
	public Vector<Observation<Point3D>> filter(Vector<Observation<Point3D>> observations, Point3D calleePosition)
	{
		Vector<Observation<Point3D>> filtered = new Vector<Observation<Point3D>>();

		// filter observations
		for (Observation<Point3D> observation : observations)
		{
			Point3D robotPosition = observation.getRobotPosition();
			// if the distance between the point and center is less than the radius, the point is
			// inside the circle
			if (isPointVisible(calleePosition, robotPosition))
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
	 * @see it.diunipi.volpi.sycamore.plugins.SycamorePlugin#getPluginName()
	 */
	@Override
	public String getPluginName()
	{
		return "SphericalVisibility";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.diunipi.volpi.sycamore.plugins.SycamorePlugin#getPluginShortDescription()
	 */
	@Override
	public String getPluginShortDescription()
	{
		return "Spherical visibility";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.diunipi.volpi.sycamore.plugins.SycamorePlugin#getLongDescription()
	 */
	@Override
	public String getPluginLongDescription()
	{
		return "Visibility bounded by a sphere";
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
