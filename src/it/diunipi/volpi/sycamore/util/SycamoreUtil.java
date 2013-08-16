/**
 * 
 */
package it.diunipi.volpi.sycamore.util;

import it.diunipi.volpi.sycamore.model.ComputablePoint;
import it.diunipi.volpi.sycamore.model.Point2D;
import it.diunipi.volpi.sycamore.model.Point3D;
import it.diunipi.volpi.sycamore.model.SycamoreAbstractPoint;

import java.awt.Color;
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

/**
 * @author Valerio Volpi - volpiv@cli.di.unipi.it
 * 
 */
public class SycamoreUtil
{
	private static class RandomFairnessmanager
	{
		private HashMap<Long, Integer>	counter;

		/**
		 * 
		 */
		public RandomFairnessmanager()
		{
			PropertyManager.getSharedInstance().putProperty(ApplicationProperties.FAIRNESS_COUNT.getName(), 10);

			this.counter = new HashMap<Long, Integer>();
		}

		/**
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
		 * @param id
		 * @return
		 */
		public boolean checkFairness(Long id)
		{
			Integer count = this.counter.get(id);
			if (count != null && count == PropertyManager.getSharedInstance().getIntegerProperty(ApplicationProperties.FAIRNESS_COUNT.getName()))
			{
				this.counter.remove(id);
				return true;
			}
			else
				return false;
		}
	}

	/**
	 * 
	 */
	private static final RandomFairnessmanager	fairnessManager	= new RandomFairnessmanager();

	/**
	 * @return the fairnessManager
	 */
	public static RandomFairnessmanager getFairnessManager()
	{
		return fairnessManager;
	}

	/**
	 * @param start
	 * @param end
	 * @return
	 */
	public static float getRandomFloat(float start, float end)
	{
		return (float) (Math.random() * (end - start) + start);
	}

	/**
	 * @return
	 */
	public static ColorRGBA getRandomColor()
	{
		return new ColorRGBA(getRandomFloat(0, 1), getRandomFloat(0, 1), getRandomFloat(0, 1), 1);
	}

	/**
	 * @param i
	 * @param size
	 * @return
	 */
	public static int getRandomInt(int start, int end)
	{
		Random r = new Random();
		return r.nextInt(end - start) + start;
	}

	/**
	 * @param i
	 * @param size
	 * @return
	 */
	public static long getRandomLong(long start, long end)
	{
		int startInt = (int) start;
		int endInt = (int) end;

		return getRandomInt(startInt, endInt);
	}

	/**
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
	 * @param objects
	 * @return
	 */
	public static <O extends Object> Vector<O> randomSubset(Vector<O> objects)
	{
		Random random = new Random();
		Vector<O> ret = new Vector<O>();

		if (objects.size() > 0)
		{
			// generate the number of objects to be selected
			int number = random.nextInt(objects.size() + 1);

			// randomly select number objects
			for (int i = 0; i < number; i++)
			{
				int index = random.nextInt(objects.size());
				O object = objects.elementAt(index);

				// add object if not present
				if (!ret.contains(object))
				{
					ret.add(object);
				}
			}
		}

		return ret;
	}

	/**
	 * @param objects
	 * @return
	 */
	public static <O extends SubsetFairnessSupporter> Vector<O> randomFairSubset(Vector<O> objects)
	{
		Vector<O> ret = new Vector<O>();
		Random random = new Random();

		if (objects.size() > 0)
		{
			// generate the number of objects to be selected
			int number = random.nextInt(objects.size() + 1);

			// randomly select number objects
			for (int i = 0; i < number; i++)
			{
				int index = random.nextInt(objects.size());
				O object = objects.elementAt(index);

				// add object if not present
				if (!ret.contains(object))
				{
					ret.add(object);
				}
			}

			// store skipped elements
			for (O obj : objects)
			{
				if (!ret.contains(obj))
				{
					getFairnessManager().storeElement(obj.getID());
				}
			}

			// eventually check if there are more elements to add
			for (O obj : objects)
			{
				if (getFairnessManager().checkFairness(obj.getID()))
				{
					if (!ret.contains(obj))
					{
						ret.add(obj);
					}
				}
			}
		}

		return ret;
	}

	/**
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
	 * @param point
	 * @return
	 */
	public static java.awt.geom.Point2D.Float convertPoint2D(Point2D point)
	{
		return new java.awt.geom.Point2D.Float(point.x, point.y);
	}

	/**
	 * @param point
	 * @return
	 */
	public static Point2D convertPoint2D(java.awt.geom.Point2D point)
	{
		return new Point2D((float) point.getX(), (float) point.getY());
	}

	/**
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

	public static void copyFile(File sourceFile, File destFile) throws IOException {
	    if(!destFile.exists()) {
	        destFile.createNewFile();
	    }

	    FileInputStream fis = null;
	    FileOutputStream fos = null;
	    FileChannel source = null;
	    FileChannel destination = null;

	    try {
	        fis = new FileInputStream(sourceFile);
	        source = fis.getChannel();
	        
	        
	        fos = new FileOutputStream(destFile);
	        destination = fos.getChannel();
	        
	        destination.transferFrom(source, 0, source.size());
	    }
		finally
		{
	        if(source != null) {
	            source.close();
	        }
	        if(destination != null) {
	            destination.close();
	        }
	        
	        fis.close();
	        fos.close();
	    }
	}
	
	/**
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
	 * Points on border are considered part of the square
	 * 
	 * @param point
	 * @param square
	 * @return
	 */
	public static boolean isPointInsideSquare(Point2D point, Rectangle2D square)
	{
		if (point.x >= square.getX() && point.x <= (square.getX() + square.getWidth()))
		{
			if (point.y >= (square.getY() - square.getHeight()) && point.y <= square.getY())
			{
				return true;
			}
		}

		return false;
	}
}
