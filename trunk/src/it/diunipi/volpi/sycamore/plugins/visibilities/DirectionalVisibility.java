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

import java.awt.Polygon;
import java.util.Vector;
import java.util.concurrent.Callable;

import net.xeoh.plugins.base.annotations.PluginImplementation;

import com.jme3.bounding.BoundingBox;
import com.jme3.material.Material;
import com.jme3.material.RenderState.BlendMode;
import com.jme3.material.RenderState.FaceCullMode;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Quad;
import com.jme3.texture.Texture;
import com.jme3.util.TangentBinormalGenerator;

/**
 * 2D visibility with the peculiarity of being directional. With this visibility, in fact, the robot
 * can see just in front of it but it has absolutely no visibility on its sides or behind. The shape
 * of this visibility is an isosceles triangle with the vertex placed in the robot's position and
 * the height corresponding to the half line describing robot's direction.
 * 
 * @author Valerio Volpi - vale.v@me.com
 */
@PluginImplementation
public class DirectionalVisibility extends VisibilityImpl<Point2D>
{
	private final int				angle			= 40;
	protected Geometry				geometry		= null;
	private VisibilitySettingsPanel	settingPanel	= null;
	private Polygon					triangle		= null;
	private Point2D					centroid		= null;

	/**
	 * Default constructor.
	 */
	public DirectionalVisibility()
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
				Texture skyTexture = SycamoreSystem.getAssetManager().loadTexture("it/diunipi/volpi/sycamore/resources/textures/triangle.png");
				mat.setTexture("ColorMap", skyTexture);

				// prepare a new quad geometry
				geometry = new Geometry("Triangle", new Quad(1, 1));
				geometry.setLocalScale(2 * getVisibilityRange());
				geometry.center();

				Vector3f translation = geometry.getLocalTranslation();
				geometry.setLocalTranslation(translation.x, translation.y, translation.z - 0.5f);
				geometry.setModelBound(new BoundingBox());
				geometry.updateModelBound();

				// apply the texture to the quad and set the material as fully transparent.
				TangentBinormalGenerator.generate(geometry.getMesh(), true);
				geometry.setMaterial(mat);
				geometry.setQueueBucket(Bucket.Transparent);

				return null;
			}
		});
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * it.diunipi.volpi.sycamore.plugins.visibilities.Visibility#isPointVisible(it.diunipi.volpi
	 * .sycamore.engine.SycamoreAbstractPoint)
	 */
	@Override
	public boolean isPointVisible(Point2D point)
	{
		// obtain the angle of rotation
		Quaternion rotation = robot.getRobotNode().getLocalRotation();
		float[] angles = rotation.toAngles(null);

		// one point of the triangle is the robot's position
		Point2D position = robot.getLocalPosition();
		Point2D x = null;
		Point2D y = null;

		double eta;
		double delta;

		// theta is the rotation around z axis
		// JME Quaternion returns angles in a particular format, so here are normalized
		double theta = angles[2];

		// if rotation around x and y axes is zero, we are in the 1st or 4th quadrant
		if (angles[0] == 0 && angles[1] == 0)
		{
			if (theta >= 0)
			{
				// from 0 to PI/2 (90 degrees) the angles are returned well by Quaternion
				eta = theta + Math.toRadians(angle / 2);
				delta = eta - Math.toRadians(angle);
				float a = getVisibilityRange();

				x = new Point2D(position.x + (a * (float) Math.cos(eta)), position.y + (a * (float) Math.sin(eta)));
				y = new Point2D(position.x + (a * (float) Math.cos(delta)), position.y + (a * (float) Math.sin(delta)));
			}
			else
			{
				// from 4/3PI (270 degrees) to 2PI (360 degrees), Quaternion returns values between
				// - PI/2 and zero.
				theta = -theta;

				eta = theta + Math.toRadians(angle / 2);
				delta = eta - Math.toRadians(angle);
				float a = getVisibilityRange();

				// this time I subtract from position the computed x and the computed y
				y = new Point2D(position.x + (a * (float) Math.cos(eta)), position.y - (a * (float) Math.sin(eta)));
				x = new Point2D(position.x + (a * (float) Math.cos(delta)), position.y - (a * (float) Math.sin(delta)));
			}
		}
		else
		{
			// if rotation around x and y axes is not zero, we are in the 2nd or 3rd quadrant

			if (theta >= 0)
			{
				// from PI/2 (90 degrees) to PI (180 degrees), Quaternion returns values between
				// PI/2 and zero, so they are considered specular with the case of first quadrant

				eta = theta + Math.toRadians(angle / 2);
				delta = eta - Math.toRadians(angle);
				float a = getVisibilityRange();

				// this time I subtract from position the computed x, but sum the y values
				y = new Point2D(position.x - (a * (float) Math.cos(eta)), position.y + (a * (float) Math.sin(eta)));
				x = new Point2D(position.x - (a * (float) Math.cos(delta)), position.y + (a * (float) Math.sin(delta)));
			}
			else
			{
				// from PI (180 degrees) to 4/3PI (270 degrees), Quaternion returns values between
				// zero and -PI/2, but the angles around x and y are not zero.
				theta = -theta;

				eta = theta + Math.toRadians(angle / 2);
				delta = eta - Math.toRadians(angle);
				float a = getVisibilityRange();

				// this time I subtract from position the computed x and the computed y
				x = new Point2D(position.x - (a * (float) Math.cos(eta)), position.y - (a * (float) Math.sin(eta)));
				y = new Point2D(position.x - (a * (float) Math.cos(delta)), position.y - (a * (float) Math.sin(delta)));
			}
		}

		this.triangle = new Polygon();

		Point2D a = new Point2D(position.x, position.y);
		Point2D b = new Point2D(x.x, x.y);
		Point2D c = new Point2D(y.x, y.y);
		
		
		triangle.addPoint((int) a.x, (int) a.y);
		triangle.addPoint((int) b.x, (int) b.y);
		triangle.addPoint((int) c.x, (int) c.y);
		
		this.centroid = new Point2D((a.x + b.x + c.x) / 3, (a.y + b.y + c.y) / 3);

		boolean visible = triangle.contains(SycamoreUtil.convertPoint2D(point));
		return visible;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.diunipi.volpi.sycamore.plugins.visibilities.Visibility#getPointInside()
	 */
	@Override
	public Point2D getPointInside()
	{
		// take the segment between the robot and the direction
		Point2D center = robot.getLocalPosition();
		Point2D direction = robot.getDirection();

		float distance = center.distanceTo(direction);

		// compute a random length between 0 and visibility range
		float length = SycamoreUtil.getRandomFloat(0, getVisibilityRange());

		if (distance <= length)
		{
			return direction;
		}
		else
		{
			// return the point on the segment
			float ratio = distance / length;
			return new Point2D(direction.x / ratio, direction.y / ratio);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.diunipi.volpi.sycamore.plugins.visibilities.Visibility#filter(java.util.Vector)
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
	 * @see it.diunipi.volpi.sycamore.plugins.visibilities.Visibility#getVisibilityRangeGeometry()
	 */
	@Override
	public Geometry getVisibilityRangeGeometry()
	{
		return geometry;
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
		return "2D directinal visibility. The robot can see just in front of it.";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.diunipi.volpi.sycamore.plugins.SycamorePlugin#getPluginLongDescription()
	 */
	@Override
	public String getPluginLongDescription()
	{
		return "2D visibility with the peculiarity of being directional. With this visibility, in fact, the robot can see just in front "
				+ "of it but it has absolutely no visibility on its sides or behind. The shape of this visibility is an isosceles triangle with "
				+ "the vertex placed in the robot's position and the height corresponding to the half line describing robot's direction.";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.diunipi.volpi.sycamore.plugins.SycamorePlugin#getPanel_settings()
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
				geometry.setLocalScale(2 * getVisibilityRange());

				// translate the geometry to be centered in the robot's position again.
				float translationFactor = getVisibilityRange();
				geometry.setLocalTranslation(-translationFactor, -translationFactor, 0.5f);
				geometry.updateGeometricState();
				return null;
			}
		});
	}

	/**
	 * @return the triangle
	 */
	public Polygon getTriangle()
	{
		return triangle;
	}
	
	/**
	 * @return the centroid
	 */
	public Point2D getCentroid()
	{
		return centroid;
	}
}
