/**
 * 
 */
package it.diunipi.volpi.sycamore.plugins.algorithms;

import it.diunipi.volpi.sycamore.engine.Observation;
import it.diunipi.volpi.sycamore.engine.Point2D;
import it.diunipi.volpi.sycamore.engine.SycamoreObservedRobot;
import it.diunipi.volpi.sycamore.engine.SycamoreEngine.TYPE;
import it.diunipi.volpi.sycamore.gui.SycamorePanel;
import it.diunipi.volpi.sycamore.plugins.visibilities.VisibilityImpl;

import java.util.Collections;
import java.util.Vector;

import net.xeoh.plugins.base.annotations.PluginImplementation;

/**
 * TESTING STUFF NOT WORKING
 */
@PluginImplementation
public class GatheringAndoSuzuki extends AlgorithmImpl<Point2D>
{
	/**
	 *
	 */
	private class Circle
	{
		private float	radius;
		private Point2D	center;

		/**
		 * @param x
		 * @param y
		 * @param radius
		 */
		public Circle(float x, float y, float radius)
		{
			center = new Point2D(x, y);
			this.radius = radius;
		}

		/**
		 * @param p
		 * @return
		 */
		public boolean isInCircle(Point2D p)
		{
			return center.distanceTo(p) <= radius;

		}

		/**
		 * @return
		 */
		public Point2D getCenter()
		{
			return center;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString()
		{
			return "Circle{" + "radius=" + radius + ", center=" + center + '}';
		}
	}

	private class Welzl
	{

		private Point2D[]	points;
		private Point2D[]	boundary;

		public Welzl(Vector<Point2D> p)
		{
			// save given points
			setPoints(p);
		}

		/*
		 * Convert new points to array
		 */
		public void setPoints(Vector<Point2D> p)
		{
			points = null;
			points = new Point2D[p.size()];
			p.copyInto(points);
		}

		/*
		 * Calculates the minimal enclosing circle. Prepares data and calls the recursive function.
		 */
		public Circle calcMec()
		{
			if (points.length == 0) return null;

			// random permutation of point set
			int pos;
			Point2D temp;
			for (int i = 0; i < points.length; i++)
			{
				pos = (int) (Math.random() * points.length);
				temp = points[i];
				points[i] = points[pos];
				points[pos] = temp;
			}

			boundary = new Point2D[3];
			return mec(points, points.length, boundary, 0);
		}

		/*
		 * The main recursive function to calculate the minmal enclosing circle. Call it initially
		 * with boundary array empty and b = 0.
		 */
		private Circle mec(Point2D[] points, int n, Point2D[] boundary, int b)
		{
			Circle localCircle = null;

			// terminal cases
			if (b == 3)
				localCircle = calcCircle3(boundary[0], boundary[1], boundary[2]);
			else if (n == 1 && b == 0)
				localCircle = new Circle(points[0].x, points[0].y, 0);
			else if (n == 0 && b == 2)
				localCircle = calcCircle2(boundary[0], boundary[1]);
			else if (n == 1 && b == 1)
				localCircle = calcCircle2(boundary[0], points[0]);
			else
			{
				localCircle = mec(points, n - 1, boundary, b);
				if (!localCircle.isInCircle(points[n - 1]))
				{
					boundary[b++] = points[n - 1];
					localCircle = mec(points, n - 1, boundary, b);
				}
			}
			return localCircle;
		}

		/*
		 * Calculates the circle through the given 3 points. They must not lie on a common line!
		 */
		public Circle calcCircle3(Point2D p1, Point2D p2, Point2D p3)
		{
			float a = p2.x - p1.x;
			float b = p2.y - p1.y;
			float c = p3.x - p1.x;
			float d = p3.y - p1.y;
			float e = a * (p2.x + p1.x) * 0.5f + b * (p2.y + p1.y) * 0.5f;
			float f = c * (p3.x + p1.x) * 0.5f + d * (p3.y + p1.y) * 0.5f;
			float det = a * d - b * c;

			float cx = (d * e - b * f) / det;
			float cy = (-c * e + a * f) / det;

			Circle circle = new Circle(cx, cy, (float) Math.sqrt((p1.x - cx) * (p1.x - cx) + (p1.y - cy) * (p1.y - cy)));

			return circle;
		}

		/*
		 * Calculates a circle through the given two points.
		 */
		public Circle calcCircle2(Point2D p1, Point2D p2)
		{
			float cx = 0.5f * (p1.x + p2.x);
			float cy = 0.5f * (p1.y + p2.y);
			Circle circle = new Circle(cx, cy, (float) Math.sqrt((p1.x - cx) * (p1.x - cx) + (p1.y - cy) * (p1.y - cy)));
			return circle;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.diunipi.volpi.sycamore.plugins.algorithms.Algorithm#compute(java.util.Vector,
	 * it.diunipi.volpi.sycamore.model.SycamoreObservedRobot)
	 */
	@Override
	public Point2D compute(Vector<Observation<Point2D>> observations, SycamoreObservedRobot<Point2D> callee)
	{
		Point2D executingRobotPosition = callee.getLocalPosition();

		// no observations
		if (observations.isEmpty())
		{
			setFinished(true);
			return executingRobotPosition;
		}

		Vector<Point2D> positions = new Vector<Point2D>();
		for (Observation<Point2D> observation : observations)
		{
			positions.add(observation.getRobotPosition());
		}

		Welzl w = new Welzl(positions);
		Point2D center = w.calcMec().getCenter();

		double distance;
		double angle;
		Vector<Double> l = new Vector<Double>();

		for (Observation<Point2D> obseravtion : observations)
		{
			distance = executingRobotPosition.distanceTo(obseravtion.getRobotPosition());

			if (distance != 0)
			{
				angle = calculateAngle(center, executingRobotPosition, obseravtion.getRobotPosition());
				l.add((distance / 2.0) * Math.cos(angle) + Math.sqrt(Math.pow((VisibilityImpl.getVisibilityRange() / 2.0), 2) - Math.pow((distance / 2.0) * Math.sin(angle), 2)));
			}
		}

		double move = Math.min(Collections.min(l), center.distanceTo(executingRobotPosition));

		double cosine = center.y / center.distanceTo(executingRobotPosition);
		double sine = center.x / center.distanceTo(executingRobotPosition);

		float newY = (float) (cosine * move);
		float newX = (float) (sine * move);
		Point2D ret = new Point2D(newX, newY);
		
		// check if finished
		boolean allClose = true;
		for (Observation<Point2D> observation : observations)
		{
			if (observation.getRobotPosition().differsModuloEpsilon(ret))
			{
				allClose = false;
			}
		}
		if (allClose)
		{
			System.out.println("Finished!");
			setFinished(true);
		}
		
		if (ret.differsModuloEpsilon(executingRobotPosition))
		{
			return ret;
		}
		else
		{
			return executingRobotPosition;
		}
	}

	/**
	 * @param A
	 * @param B
	 * @param C
	 * @return
	 */
	private float calculateAngle(Point2D A, Point2D B, Point2D C)
	{
		return getAbsoluteAngle(A.x, A.y, B.x, B.y, C.x, C.y);
	}

	/**
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @param x3
	 * @param y3
	 * @return
	 */
	public float getAbsoluteAngle(float x1, float y1, float x2, float y2, float x3, float y3)
	{
		float angle1 = getHorizontalAngle(x2, y2, x1, y1);
		float angle2 = getHorizontalAngle(x2, y2, x3, y3);
		angle1 = (float) ((angle2 - angle1 + (Math.PI * 2)) % ((Math.PI * 2)));

		if (angle1 < Math.PI)
		{
			return angle1;
		}
		else
		{
			return (float) ((Math.PI * 2) - angle1);
		}
	}

	/**
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @return
	 */
	public float getHorizontalAngle(float x1, float y1, float x2, float y2)
	{
		return (float) ((Math.atan2(y2 - y1, x2 - x1) + (Math.PI * 2)) % ((Math.PI * 2)));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.diunipi.volpi.sycamore.plugins.algorithms.Algorithm#getReferences()
	 */
	@Override
	public String getReferences()
	{
		return "Distributed Memoryless Point Convergence Algorithm for Mobile Robots with Limited Visibility, H.Ando, I.Suzuki, 1999";
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
	 * @see it.diunipi.volpi.sycamore.plugins.SycamorePlugin#getPluginName()
	 */
	@Override
	public String getPluginName()
	{
		return "GatheringAndoSuzuki";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.diunipi.volpi.sycamore.plugins.SycamorePlugin#getAuthor()
	 */
	@Override
	public String getAuthor()
	{
		return "H.Ando, I.Suzuki";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.diunipi.volpi.sycamore.plugins.SycamorePlugin#getPluginShortDescription()
	 */
	@Override
	public String getPluginShortDescription()
	{
		return "Gathering with limited visibility in semi-syncronous settings";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.diunipi.volpi.sycamore.plugins.SycamorePlugin#getPluginLongDescription()
	 */
	@Override
	public String getPluginLongDescription()
	{
		return "The algorithm tries to achieve the following two subgoals at any time \n1) the robots mutually visible get closer;\n2) robots that are mutually visible at time t remain within distance V of each other.";
	}

}
