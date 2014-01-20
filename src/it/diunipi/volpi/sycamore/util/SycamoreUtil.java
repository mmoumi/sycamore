/**
 * 
 */
package it.diunipi.volpi.sycamore.util;

import it.diunipi.volpi.sycamore.engine.ComputablePoint;
import it.diunipi.volpi.sycamore.engine.Point2D;
import it.diunipi.volpi.sycamore.engine.Point3D;
import it.diunipi.volpi.sycamore.engine.SycamoreAbstractPoint;
import it.diunipi.volpi.sycamore.engine.SycamoreEngine.TYPE;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.Polygon;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.awt.geom.PathIterator;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.Vector;

import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;

/**
 * Utility class with a lot of static utility methods.
 * 
 * @author Valerio Volpi - volpiv@cli.di.unipi.it
 */
public class SycamoreUtil
{
	/**
	 * The fairness manager. It cares of returning random siubsets of a vector, but after a fixed
	 * number of calls it forces an object that was never returned to be placed inside the returning
	 * vector.
	 * 
	 * @author Valerio Volpi - vale.v@me.com
	 */
	private static class RandomFairnessmanager
	{
		private HashMap<Long, Integer>	counter;

		/**
		 * Constructor.
		 */
		public RandomFairnessmanager()
		{
			PropertyManager.getSharedInstance().putProperty(ApplicationProperties.FAIRNESS_COUNT, 10);

			this.counter = new HashMap<Long, Integer>();
		}

		/**
		 * Store the element with passed ID
		 * 
		 * @param id
		 */
		public void storeElement(Long id)
		{
			Integer count = this.counter.get(id);
			if (count == null)
			{
				count = -1;
			}

			int newCount = (count + 1);
			this.counter.put(id, newCount);
		}

		/**
		 * Returns the number of steps when the passed id has not been returned
		 * 
		 * @param id
		 * @return
		 */
		public int getFairnessCount(Long id)
		{
			return this.counter.get(id);
		}
	}

	private static final RandomFairnessmanager	fairnessManager	= new RandomFairnessmanager();

	/**
	 * @return the fairnessManager
	 */
	public static RandomFairnessmanager getFairnessManager()
	{
		return fairnessManager;
	}

	/**
	 * Returns a random float between passed values
	 * 
	 * @param start
	 * @param end
	 * @return
	 */
	public static float getRandomFloat(float start, float end)
	{
		return (float) (Math.random() * (end - start) + start);
	}

	/**
	 * Returns a random double between passed values
	 * 
	 * @param start
	 * @param end
	 * @return
	 */
	public static double getRandomDouble(double start, double end)
	{
		return Math.random() * (end - start) + start;
	}

	/**
	 * Returns a random color
	 * 
	 * @return
	 */
	public static ColorRGBA getRandomColor()
	{
		return new ColorRGBA(getRandomFloat(0, 1), getRandomFloat(0, 1), getRandomFloat(0, 1), 1);
	}

	/**
	 * Returns a random integer between passed values
	 * 
	 * @param start
	 * @param end
	 * @return
	 */
	public static int getRandomInt(int start, int end)
	{
		Random r = new Random();
		return r.nextInt(end - start) + start;
	}

	/**
	 * Returns a random long between passed values
	 * 
	 * @param start
	 * @param end
	 * @return
	 */
	public static long getRandomLong(long start, long end)
	{
		int startInt = (int) start;
		int endInt = (int) end;

		return getRandomInt(startInt, endInt);
	}

	/**
	 * Returns a random Point2D object whose coordinates are between passed values.
	 * 
	 * @param startX
	 * @param endX
	 * @param startY
	 * @param endY
	 * @return
	 */
	public static Point2D getRandomPoint2D(float startX, float endX, float startY, float endY)
	{
		return new Point2D(getRandomFloat(startX, endX), getRandomFloat(startY, endY));
	}

	/**
	 * Returns a random Point3D object whose coordinates are between passed values.
	 * 
	 * @param startX
	 * @param endX
	 * @param startY
	 * @param endY
	 * @param startZ
	 * @param endZ
	 * @return
	 */
	public static Point3D getRandomPoint3D(float startX, float endX, float startY, float endY, float startZ, float endZ)
	{
		return new Point3D(getRandomFloat(startX, endX), getRandomFloat(startY, endY), getRandomFloat(startZ, endZ));
	}

	/**
	 * Returns a random subset of passed objects. No fair is guaranteed.
	 * 
	 * @param objects
	 * @return
	 */
	public static <O extends Object> Vector<O> randomSubset(Vector<O> objects)
	{
		Vector<O> ret = new Vector<O>();

		if (objects.size() > 0)
		{
			// generate the number of objects to be selected
			double ro = Math.random();
			double probability = ro * (1.0 - (1.0 / 2.0));

			for (O obj : objects)
			{
				// compute the value for obj and eventually add it
				double value = Math.random();
				if (value < probability)
				{
					ret.add(obj);
				}
			}
		}

		return ret;
	}

	/**
	 * Returns a random subset of passed objects. No fair is guaranteed. The fairness manager is
	 * used to guarantee that any object is always returned at least after a fixed number of calls
	 * to this method.
	 * 
	 * @param objects
	 * @return
	 */
	public static <O extends SubsetFairnessSupporter> Vector<O> randomFairSubset(Vector<O> objects)
	{
		Vector<O> ret = new Vector<O>();

		if (objects.size() > 0)
		{
			// generate the number of objects to be selected
			double ro = Math.random();

			for (O obj : objects)
			{
				RandomFairnessmanager farnFairnessmanager = getFairnessManager();
				Long id = obj.getID();
				
				double n = farnFairnessmanager.getFairnessCount(id);
				double probability = ro * (1.0 - (1.0 / n));
				
				// compute the value for obj and eventually add it
				double value = Math.random();
				if (value < probability)
				{
					ret.add(obj);
				}
				else
				{
					farnFairnessmanager.storeElement(id);
				}
			}
		}

		return ret;
	}

	/**
	 * Returns a random point that is on the segment conecting passed points.
	 * 
	 * @param point1
	 * @param point2
	 * @return
	 */
	public static <P extends SycamoreAbstractPoint & ComputablePoint<P>> P getRandomIntermediatePoint(P point1, P point2)
	{
		float randomRatio = getRandomFloat(0.0f, 1.0f);
		return point1.interpolateWith(point2, randomRatio);
	}

	/**
	 * @author Valerio Volpi - volpiv@cli.di.unipi.it
	 * 
	 */
	static class Number implements SubsetFairnessSupporter
	{
		long	id;

		/**
		 * 
		 */
		public Number(long id)
		{
			this.id = id;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see it.diunipi.volpi.sycamore.util.SubsetFairnessSupporter#getID()
		 */
		@Override
		public long getID()
		{
			return id;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString()
		{
			return id + "";
		}

	}

	/**
	 * Converts a Sycamore Point2D to an awt Point2D
	 * 
	 * @param point
	 * @return
	 */
	public static java.awt.geom.Point2D.Float convertPoint2D(Point2D point)
	{
		if (point != null)
		{
			return new java.awt.geom.Point2D.Float(point.x, point.y);
		}
		else
		{
			return null;
		}
	}

	/**
	 * Converts an awt Point2D to a Sycamore Point2D
	 * 
	 * @param point
	 * @return
	 */
	public static Point2D convertPoint2D(java.awt.geom.Point2D point)
	{
		return new Point2D((float) point.getX(), (float) point.getY());
	}

	/**
	 * If swap is on, return a Point2D whose coordinates are swapped. Otherwise returns the passed
	 * point.
	 * 
	 * @param src
	 * @param swap
	 * @return
	 */
	public static Point2D shufflePoint2D(Point2D src, boolean swap)
	{
		if (swap)
		{
			return new Point2D(src.y, src.x);
		}
		else
			return src;
	}

	/**
	 * If swap flags are on, return a Point3D whose coordinates are swapped according with the
	 * flags. Otherwise returns the passed point.
	 * 
	 * @param src
	 * @param swap
	 * @return
	 */
	public static Point3D shufflePoint3D(Point3D src, boolean swapXY, boolean swapXZ, boolean swapYZ)
	{
		float x = src.x;
		float y = src.y;
		float z = src.z;

		if (swapXY)
		{
			float tmp = x;
			x = y;
			y = tmp;
		}
		if (swapXZ)
		{
			float tmp = x;
			x = z;
			z = tmp;
		}
		if (swapYZ)
		{
			float tmp = y;
			y = z;
			z = tmp;
		}

		return new Point3D(x, y, z);
	}

	/**
	 * If swap flags are on, return a Point3D whose coordinates are swapped according with the
	 * flags. Otherwise returns the passed point. The coordinates are swapped in the inverse order
	 * as the <code>shufflePoint3D</code> method does.
	 * 
	 * @param src
	 * @param swap
	 * @return
	 */
	public static Point3D shuffleInversePoint3D(Point3D src, boolean swapXY, boolean swapXZ, boolean swapYZ)
	{
		float x = src.x;
		float y = src.y;
		float z = src.z;

		if (swapYZ)
		{
			float tmp = y;
			y = z;
			z = tmp;
		}
		if (swapXZ)
		{
			float tmp = x;
			x = z;
			z = tmp;
		}
		if (swapXY)
		{
			float tmp = x;
			x = y;
			y = tmp;
		}

		return new Point3D(x, y, z);
	}

	/**
	 * Converts a ColorRGBA in an awt Color
	 * 
	 * @param passedColor
	 * @return
	 */
	public static Color convertColor(ColorRGBA passedColor)
	{
		float red = passedColor.r;
		float green = passedColor.g;
		float blue = passedColor.b;
		float alpha = passedColor.a;

		return new Color(red, green, blue, alpha);
	}

	/**
	 * Copies sourceFile into destFile
	 * 
	 * @param sourceFile
	 * @param destFile
	 * @throws IOException
	 */
	public static void copyFile(File sourceFile, File destFile) throws IOException
	{
		if (!destFile.exists())
		{
			destFile.createNewFile();
		}

		FileInputStream fis = null;
		FileOutputStream fos = null;
		FileChannel source = null;
		FileChannel destination = null;

		try
		{
			fis = new FileInputStream(sourceFile);
			source = fis.getChannel();

			fos = new FileOutputStream(destFile);
			destination = fos.getChannel();

			destination.transferFrom(source, 0, source.size());
		}
		finally
		{
			if (source != null)
			{
				source.close();
			}
			if (destination != null)
			{
				destination.close();
			}

			fis.close();
			fos.close();
		}
	}

	/**
	 * Computes the baricentrum between the passed points
	 * 
	 * @param points
	 * @return
	 */
	public static <P extends SycamoreAbstractPoint> P computeBaricentrum(Vector<P> points)
	{
		if (points != null && !points.isEmpty())
		{
			P result = points.elementAt(0);
			// check concrete type
			if (result instanceof Point2D)
			{
				Vector<Point2D> newPoints = new Vector<Point2D>();
				newPoints.addAll((Collection<? extends Point2D>) points);

				// sum all points
				Point2D newResult = newPoints.elementAt(0);
				for (int i = 1; i < newPoints.size(); i++)
				{
					newResult = newResult.sumWith(newPoints.elementAt(i));
				}

				// divide for their number
				return (P) newResult.divideFor(newPoints.size());
			}
			else if (result instanceof Point3D)
			{
				Vector<Point3D> newPoints = new Vector<Point3D>();
				newPoints.addAll((Collection<? extends Point3D>) points);

				// sum all points
				Point3D newResult = newPoints.elementAt(0);
				for (int i = 1; i < newPoints.size(); i++)
				{
					newResult = newResult.sumWith(newPoints.elementAt(i));
				}

				// divide for their number
				return (P) newResult.divideFor(newPoints.size());
			}
		}

		return null;
	}

	/**
	 * Returns the set of awt Point2D objects that are corresponding to the intersection between
	 * passed Path2D and passed Line2D.
	 * 
	 * @param path
	 * @param line
	 * @return
	 * @throws Exception
	 */
	public static Set<java.awt.geom.Point2D> getIntersections(final Path2D path, final Line2D line) throws Exception
	{
		if (path != null && line != null)
		{
			// Getting an iterator along the path
			final PathIterator pathIterator = path.getPathIterator(null);

			// Double array with length 6 needed by iterator
			final double[] coords = new double[6];

			// First point (needed for closing polygon path)
			final double[] firstCoords = new double[2];

			// Previously visited point
			final double[] lastCoords = new double[2];

			// List to hold found
			final Set<java.awt.geom.Point2D> intersections = new HashSet<java.awt.geom.Point2D>();

			// intersections
			// Getting the first coordinate pair
			pathIterator.currentSegment(firstCoords);

			// Priming the previous coordinate pair
			lastCoords[0] = firstCoords[0];
			lastCoords[1] = firstCoords[1];

			pathIterator.next();
			while (!pathIterator.isDone())
			{
				final int type = pathIterator.currentSegment(coords);
				final Line2D currentLine;
				switch (type)
				{
				case PathIterator.SEG_LINETO:
					// build current path line
					currentLine = new Line2D.Double(lastCoords[0], lastCoords[1], coords[0], coords[1]);
					if (currentLine.intersectsLine(line))
					{
						// eventually add intersections
						intersections.add(getIntersection(currentLine, line));

					}

					// update ccordinates for new line
					lastCoords[0] = coords[0];
					lastCoords[1] = coords[1];
					break;

				case PathIterator.SEG_CLOSE:
					// build current path line
					currentLine = new Line2D.Double(coords[0], coords[1], firstCoords[0], firstCoords[1]);
					if (currentLine.intersectsLine(line))
					{
						// eventually add intersections
						intersections.add(getIntersection(currentLine, line));
					}
					break;

				default:
					throw new Exception("Unsupported PathIterator segment type.");
				}
				pathIterator.next();
			}
			return intersections;
		}
		else
		{
			return null;
		}

	}

	/**
	 * Returns the set of awt Point2D objects that are corresponding to the intersection between the
	 * two passed Line2D objects.
	 * 
	 * @param line1
	 * @param line2
	 * @return
	 */
	public static java.awt.geom.Point2D getIntersection(final Line2D line1, final Line2D line2)
	{

		final double x1, y1, x2, y2, x3, y3, x4, y4;
		x1 = line1.getX1();
		y1 = line1.getY1();
		x2 = line1.getX2();
		y2 = line1.getY2();
		x3 = line2.getX1();
		y3 = line2.getY1();
		x4 = line2.getX2();
		y4 = line2.getY2();

		final double x = ((x2 - x1) * (x3 * y4 - x4 * y3) - (x4 - x3) * (x1 * y2 - x2 * y1)) / ((x1 - x2) * (y3 - y4) - (y1 - y2) * (x3 - x4));
		final double y = ((y3 - y4) * (x1 * y2 - x2 * y1) - (y1 - y2) * (x3 * y4 - x4 * y3)) / ((x1 - x2) * (y3 - y4) - (y1 - y2) * (x3 - x4));

		return new java.awt.geom.Point2D.Double(x, y);
	}

	/**
	 * Returns the angle between the two passed Line2D objects, in radiants.
	 * 
	 * @param line1
	 * @param line2
	 * @return
	 */
	public static double angleBetween2Lines(final Line2D line1, final Line2D line2)
	{
		if (line1.getX1() == line2.getX1() && line1.getX2() == line2.getX2() && line1.getY1() == line2.getY1() && line1.getY2() == line2.getY2())
		{
			// same lines
			return 0;
		}
		final double angle1 = Math.atan2(-line1.getY1() + line1.getY2(), line1.getX1() - line1.getX2());
		final double angle2 = Math.atan2(-line2.getY1() + line2.getY2(), line2.getX1() - line2.getX2());
		return (Math.PI * 2) - angle1 - angle2;
	}

	/**
	 * Returns true if passed point is inside passed rectangle, false otherwise. Points on border
	 * are considered part of the rectangle. Pay attention to the fact that rect is an awt
	 * Rectangle2D object, while point is a Sycamore Point2D object.
	 * 
	 * @param point
	 * @param rect
	 * @return
	 */
	public static boolean isPointInsideRectangle(Point2D point, Rectangle2D rect)
	{
		return rect.contains(SycamoreUtil.convertPoint2D(point));
	}

	/**
	 * Returns true if passed point is inside the triangle composed of the three passed points,
	 * false otherwise. Points on border are considered part of the triangle.
	 * 
	 * @param point
	 * @param a
	 * @param b
	 * @param c
	 * @return
	 */
	public static boolean isPointInsideTriangle(Point2D point, Point2D a, Point2D b, Point2D c)
	{
		Polygon triangle = new Polygon();

		triangle.addPoint((int) a.x, (int) a.y);
		triangle.addPoint((int) b.x, (int) b.y);
		triangle.addPoint((int) c.x, (int) c.y);

		return triangle.contains(SycamoreUtil.convertPoint2D(point));
	}

	/**
	 * Returns a random boolean
	 * 
	 * @return
	 */
	public static boolean getRandomBoolan()
	{
		return new Random().nextBoolean();
	}

	/**
	 * Computes the absolute value of passed vector and returns it.
	 * 
	 * @param scale
	 * @return
	 */
	public static Vector3f vectorAbs(Vector3f vec)
	{
		return new Vector3f(FastMath.abs(vec.x), FastMath.abs(vec.y), FastMath.abs(vec.z));
	}

	/**
	 * Return a new blank point of passed type.
	 * 
	 * @return
	 */
	public static <P extends SycamoreAbstractPoint & ComputablePoint<P>> P getNewPoint(TYPE type)
	{
		if (type == TYPE.TYPE_2D)
		{
			return (P) new Point2D();
		}
		else
		{
			return (P) new Point3D();
		}
	}
	
	/**
	 * @param positions
	 * @return
	 */
	public static Point2D getCentroid2D(Vector<Point2D> points)
	{
		float sumX = 0;
		float sumY = 0;
		
		for (Point2D point : points)
		{
			sumX += point.x;
			sumY += point.y;
		}
		
		return new Point2D(sumX / points.size(), sumY / points.size());
	}
	
	/**
	 * @param positions
	 * @return
	 */
	public static Point3D getCentroid3D(Vector<Point3D> points)
	{
		float sumX = 0;
		float sumY = 0;
		float sumZ = 0;
		
		for (Point3D point : points)
		{
			sumX += point.x;
			sumY += point.y;
			sumZ += point.z;
		}
		
		return new Point3D(sumX / points.size(), sumY / points.size(), sumZ / points.size());
	}

	/**
	 * Tries to open a file with its editor. If it fails, tries to launch it in the browser.
	 */
	public static void editFile(File file)
	{
		Desktop desktop = Desktop.getDesktop();
		try
		{
			desktop.open(file);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * @param color
	 * @param green
	 * @return
	 */
	public static boolean areColorsEqual(ColorRGBA color1, ColorRGBA color2)
	{
		if (color1.r == color2.r)
		{
			if (color1.g == color2.g)
			{
				if (color1.b == color2.b)
				{
					if (color1.a == color2.a)
					{
						return true;
					}
				}
			}
		}

		return false;
	}
}
