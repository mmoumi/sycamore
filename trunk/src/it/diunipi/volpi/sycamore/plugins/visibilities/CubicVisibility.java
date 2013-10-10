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
import com.jme3.scene.shape.Box;
import com.jme3.util.TangentBinormalGenerator;

/**
 * 3D Visibility with the shape of a cube. This visibility is modeled by a cube centered in the
 * position of the robot and with a side equal to the visibility range. The surface of the cube is
 * included in the visible area and any object that is inside the cube's volume is considered
 * visible.
 * 
 * @author Valerio Volpi - vale.v@me.com
 */
@PluginImplementation
public class CubicVisibility extends VisibilityImpl<Point3D>
{
	private Geometry				cube			= null;
	private VisibilitySettingsPanel	settingPanel	= null;

	/**
	 * Default constructor.
	 */
	public CubicVisibility()
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

				// prepare a new cube geormetry
				cube = new Geometry("Cube", new Box(1, 1, 1));
				cube.setLocalScale(getVisibilityRange());
				cube.center();
				cube.setModelBound(new BoundingSphere());
				cube.updateModelBound();

				// set the material to the cube
				TangentBinormalGenerator.generate(cube.getMesh(), true);
				cube.setMaterial(mat);
				cube.setQueueBucket(Bucket.Transparent);

				return null;
			}
		});
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
		Point3D center = robot.getLocalPosition();
		float visibilityRange = getVisibilityRange();

		// build a cube around center
		float x = center.x - (visibilityRange / 2);
		float y = center.y - (visibilityRange / 2);
		float z = center.z - (visibilityRange / 2);

		if (point.x > x && point.x < (x + visibilityRange))
		{
			if (point.y > y && point.y < (y + visibilityRange))
			{
				if (point.z > y && point.z < (z + visibilityRange))
				{
					return true;
				}
			}
		}

		return false;
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
		float visibilityRange = getVisibilityRange();

		float startX = center.x - (visibilityRange / 2);
		float endX = center.x + (visibilityRange / 2);
		float startY = center.y - (visibilityRange / 2);
		float endY = center.y + (visibilityRange / 2);
		float startZ = center.z - (visibilityRange / 2);
		float endZ = center.z + (visibilityRange / 2);

		return SycamoreUtil.getRandomPoint3D(startX, endX, startY, endY, startZ, endZ);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.diunipi.volpi.sycamore.plugins.Visibility#filter(java.util.Vector,
	 * it.diunipi.volpi.sycamore.model.SycamoreAbstractPoint)
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
		return cube;
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
		return "3D Visibility with the shape of a cube.";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.diunipi.volpi.sycamore.plugins.SycamorePlugin#getPluginLongDescription()
	 */
	@Override
	public String getPluginLongDescription()
	{
		return "3D Visibility with the shape of a cube. This visibility is modeled by a cube centered in the "
				+ "position of the robot and with a side equal to the visibility range. The surface of the cube is "
				+ "included in the visible area and any object that is inside the cube's volume is considered visible.";
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
				cube.setLocalScale(getVisibilityRange());
				cube.center();
				cube.updateGeometricState();
				return null;
			}
		});
	}
}
