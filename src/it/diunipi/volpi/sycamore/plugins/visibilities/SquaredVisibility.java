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
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Quad;
import com.jme3.texture.Texture;
import com.jme3.util.TangentBinormalGenerator;

/**
 * 2D Visibility with the shape of a square. This visibility is modeled by a square centered in the
 * position of the robot and with a side equal to the visibility range. The border of the square is
 * included in the visible area and any object that is inside the square's area is considered
 * visible.
 * 
 * @author Valerio Volpi - vale.v@me.com
 */
@PluginImplementation
public class SquaredVisibility extends VisibilityImpl<Point2D>
{
	protected Geometry				square			= null;
	private VisibilitySettingsPanel	settingPanel	= null;

	/**
	 * Default constructor.
	 */
	public SquaredVisibility()
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
				Texture skyTexture = SycamoreSystem.getAssetManager().loadTexture("it/diunipi/volpi/sycamore/resources/textures/square.png");
				mat.setTexture("ColorMap", skyTexture);

				// prepare a new quad geometry
				square = new Geometry("Square", new Quad(1, 1));
				square.setLocalScale(getVisibilityRange());
				square.center();

				Vector3f translation = square.getLocalTranslation();
				square.setLocalTranslation(translation.x, translation.y, translation.z - 0.5f);
				square.setModelBound(new BoundingBox());
				square.updateModelBound();

				// apply the texture to the quad and set the material as fully transparent.
				TangentBinormalGenerator.generate(square.getMesh(), true);
				square.setMaterial(mat);
				square.setQueueBucket(Bucket.Transparent);

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
	public boolean isPointVisible(Point2D point)
	{
		Point2D center = robot.getLocalPosition();
		float visibilityRange = getVisibilityRange();

		// build a square around point1
		float x = center.x - (visibilityRange / 2);
		float y = center.y + (visibilityRange / 2);

		if (point.x >= x && point.x <= (x + visibilityRange))
		{
			if (point.y >= (y - visibilityRange) && point.y <= y)
			{
				return true;
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
	public Point2D getPointInside()
	{
		Point2D center = robot.getLocalPosition();
		float visibilityRange = getVisibilityRange();

		float startX = center.x - (visibilityRange / 2);
		float endX = center.x + (visibilityRange / 2);
		float startY = center.y - (visibilityRange / 2);
		float endY = center.y + (visibilityRange / 2);

		return SycamoreUtil.getRandomPoint2D(startX, endX, startY, endY);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.diunipi.volpi.sycamore.plugins.Visibility#filter(java.util.Vector,
	 * it.diunipi.volpi.sycamore.model.SycamoreAbstractPoint)
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
		return square;
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
	 * @see it.diunipi.volpi.sycamore.plugins.SycamorePlugin#getShortDescription()
	 */
	@Override
	public String getPluginShortDescription()
	{
		return "2D Visibility with the shape of a square.";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.diunipi.volpi.sycamore.plugins.SycamorePlugin#getPluginLongDescription()
	 */
	@Override
	public String getPluginLongDescription()
	{
		return "2D Visibility with the shape of a square. This visibility is modeled by a square centered in the "
				+ "position of the robot and with a side equal to the visibility range. The border of the square is "
				+ "included in the visible area and any object that is inside the square's area is considered visible.";
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
				square.setLocalScale(getVisibilityRange());

				float translationFactor = getVisibilityRange() / 2;
				square.setLocalTranslation(-translationFactor, -translationFactor, 0.5f);
				square.updateGeometricState();
				return null;
			}
		});
	}

}
