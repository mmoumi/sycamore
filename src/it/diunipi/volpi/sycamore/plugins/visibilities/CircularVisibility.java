/**
 * 
 */
package it.diunipi.volpi.sycamore.plugins.visibilities;

import it.diunipi.volpi.sycamore.engine.Observation;
import it.diunipi.volpi.sycamore.engine.Point2D;
import it.diunipi.volpi.sycamore.engine.SycamoreEngine.TYPE;
import it.diunipi.volpi.sycamore.gui.SycamorePanel;
import it.diunipi.volpi.sycamore.gui.SycamoreSystem;
import it.diunipi.volpi.sycamore.util.SycamoreUtil;

import java.util.Vector;
import java.util.concurrent.Callable;

import net.xeoh.plugins.base.annotations.PluginImplementation;

import com.jme3.bounding.BoundingBox;
import com.jme3.material.Material;
import com.jme3.material.RenderState.BlendMode;
import com.jme3.material.RenderState.FaceCullMode;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Quad;
import com.jme3.texture.Texture;
import com.jme3.util.TangentBinormalGenerator;

/**
 * 2D Visibility with the shape of a circle. This visibility is modeled by a circle centered in the
 * position of the robot and with a diameter equal to the visibility range. The border of the circle
 * is included in the visible area. Any object whose radial distance from the robot is less than
 * half the visibility range is considered visible.
 * 
 * @author Valerio Volpi - vale.v@me.com
 */
@PluginImplementation
public class CircularVisibility extends VisibilityImpl<Point2D>
{
	private Geometry				quad			= null;
	private VisibilitySettingsPanel	settingPanel	= null;

	/**
	 * Default constructor.
	 */
	public CircularVisibility()
	{
		SycamoreSystem.enqueueToJME(new Callable<Object>()
		{
			@Override
			public Object call() throws Exception
			{
				// setup visibility range geometry
				Material mat = new Material(SycamoreSystem.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");

				// setup material parameters for transparency and face culling
				mat.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
				mat.getAdditionalRenderState().setAlphaTest(true);
				mat.getAdditionalRenderState().setAlphaFallOff(0);
				mat.getAdditionalRenderState().setFaceCullMode(FaceCullMode.Off);

				// setup the texture
				Texture texture = SycamoreSystem.getAssetManager().loadTexture("it/diunipi/volpi/sycamore/resources/textures/circle.png");
				mat.setTexture("ColorMap", texture);

				// prepare a new quad geometry
				quad = new Geometry("Cylinder", new Quad(1, 1));
				quad.setLocalScale(getVisibilityRange());
				quad.center();
				quad.setModelBound(new BoundingBox());
				quad.updateModelBound();

				// apply the texture to the quad and set the material as fully transparent.
				TangentBinormalGenerator.generate(quad.getMesh(), true);
				quad.setMaterial(mat);
				quad.setQueueBucket(Bucket.Transparent);

				return null;
			}
		});
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
				quad.setLocalScale(getVisibilityRange());

				// translate the geometry to be centered in the robot's position again.
				float translationFactor = getVisibilityRange() / 2;
				quad.setLocalTranslation(-translationFactor, -translationFactor, 0.5f);
				quad.updateGeometricState();
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
		return TYPE.TYPE_2D;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * it.diunipi.volpi.sycamore.plugins.Visibility#canPointsSee(it.diunipi.volpi.sycamore.model
	 * .SycamoreAbstractPoint, it.diunipi.volpi.sycamore.model.SycamoreAbstractPoint)
	 */
	@Override
	public boolean isPointVisible(Point2D point)
	{
		// if the distance between the point and center is less than the radius, the point is
		// inside the circle
		float circleRadius = getVisibilityRange() / 2;

		Point2D center = robot.getLocalPosition();
		if (center.distanceTo(point) < circleRadius)
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
	public Point2D getPointInside()
	{
		Point2D center = robot.getLocalPosition();

		// get a random radius and a random angle
		float radius = SycamoreUtil.getRandomFloat(0, (getVisibilityRange() / 2));
		double angle = Math.random() * Math.PI * 2;

		double x = center.x + (Math.cos(angle) * radius);
		double y = center.y + (Math.sin(angle) * radius);

		return new Point2D((float) x, (float) y);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.diunipi.volpi.sycamore.plugins.Visibility#filter(java.util.Vector)
	 */
	@Override
	public Vector<Observation<Point2D>> filter(Vector<Observation<Point2D>> observations)
	{
		Vector<Observation<Point2D>> filtered = new Vector<Observation<Point2D>>();

		// filter observations
		for (Observation<Point2D> observation : observations)
		{
			Point2D robotPosition = observation.getRobotPosition();
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
		return quad;
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
		return "2D Visibility with the shape of a circle.";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.diunipi.volpi.sycamore.plugins.SycamorePlugin#getLongDescription()
	 */
	@Override
	public String getPluginLongDescription()
	{
		return "2D Visibility with the shape of a circle. This visibility is modeled by a circle centered in the "
				+ "position of the robot and with a diameter equal to the visibility range. The border of the circle "
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
}
