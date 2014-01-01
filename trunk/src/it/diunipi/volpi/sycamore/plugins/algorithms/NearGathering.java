/**
 * 
 */
package it.diunipi.volpi.sycamore.plugins.algorithms;

import it.diunipi.volpi.sycamore.engine.NNotKnownException;
import it.diunipi.volpi.sycamore.engine.Observation;
import it.diunipi.volpi.sycamore.engine.Point2D;
import it.diunipi.volpi.sycamore.engine.SycamoreObservedRobot;
import it.diunipi.volpi.sycamore.engine.TooManyLightsException;
import it.diunipi.volpi.sycamore.engine.SycamoreEngine.TYPE;
import it.diunipi.volpi.sycamore.gui.SycamorePanel;
import it.diunipi.volpi.sycamore.gui.SycamoreSystem;
import it.diunipi.volpi.sycamore.plugins.visibilities.SquaredVisibility;
import it.diunipi.volpi.sycamore.plugins.visibilities.Visibility;
import it.diunipi.volpi.sycamore.plugins.visibilities.VisibilityImpl;
import it.diunipi.volpi.sycamore.util.SortedList;
import it.diunipi.volpi.sycamore.util.SycamoreUtil;

import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import javax.swing.JOptionPane;

import net.xeoh.plugins.base.annotations.PluginImplementation;

import com.jme3.math.ColorRGBA;

/**
 * Algorithm implementation for Near Gathering: "In this paper we study the Near-Gathering problem
 * for a set of asynchronous, anonymous, oblivious and autonomous mobile robots with limited
 * visibility moving in Look-Compute-Move (LCM) cycles: In this problem, the robots have to get
 * close enough to each other, so that every robot can see all the others, without touching (i.e.,
 * colliding) with any other robot. The importance of this problem might not be clear at a first
 * sight: Solving the Near-Gathering problem, it is possible to overcome the limitations of having
 * robots with limited visibility, and it is therefore possible to exploit all the studies (the
 * majority, actually) done on this topic, in the unlimited visibility setting. In fact, after the
 * robots get close enough, they are able to see all the robots in the system, a scenario similar to
 * the one where the robots have unlimited visibility. Here, we present a collision-free algorithm
 * for the Near-Gathering problem, the first to our knowledge, that allows a set of autonomous
 * mobile robots to nearly gather within finite time. The collision-free feature of our solution is
 * crucial in order to combine it with an unlimited visibility protocol. In fact, the majority of
 * the algorithms that can be found on the topic assume that all robots occupy distinct positions at
 * the beginning. Hence, only providing a collision-free Near-Gathering algorithm, as the one
 * presented here, is it possible to successfully combine it with an unlimited visibility protocol,
 * hence overcoming the natural limitations of the limited visibility scenario. In our model,
 * distances are induced by the infinity norm. A discussion on how to extend our algorithm to models
 * with different distance functions, including the usual Euclidean distance, is also presented."
 * 
 * @author Valerio Volpi - vale.v@me.com
 * 
 */
@PluginImplementation
public class NearGathering extends AlgorithmImpl<Point2D>
{
	/**
	 * A comparator for points. Sorts the points by their Y value. If the Y values are equal, sorts
	 * the points by the X value (eventually flipped, if requested by the caller).
	 * 
	 * @author Valerio Volpi - vale.v@me.com
	 */
	private static class Point2DComparatorOnY implements Comparator<Point2D>
	{
		private boolean	flipX	= false;

		/**
		 * Default constructor.
		 * 
		 * @param b
		 *            whether to flip the x eventually
		 */
		public Point2DComparatorOnY(boolean flipX)
		{
			this.flipX = flipX;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
		 */
		@Override
		public int compare(Point2D o1, Point2D o2)
		{
			if (o1.y != o2.y)
			{
				// sort by y coordinate, descending
				return -Float.compare(o1.y, o2.y);
			}
			else if (o1.x != o2.x)
			{
				// if ys are equal, sort by x, ascending (eventually flipped)
				if (flipX)
				{
					return Float.compare(-o1.x, -o2.x);
				}
				else
				{
					return Float.compare(o1.x, o2.x);
				}
			}
			else
			{
				// otherwise points are equal
				return 0;
			}
		}
	}

	/**
	 * A comparator for points. Sorts the points by their X value. If the X values are equal, sorts
	 * the points by the Y value (eventually flipped, if requested by the caller).
	 * 
	 * @author Valerio Volpi - vale.v@me.com
	 */
	private static class Point2DComparatorOnX implements Comparator<Point2D>
	{
		private boolean	flipY	= false;

		/**
		 * Default constructor.
		 * 
		 * @param b
		 *            whether to flip the y eventually
		 */
		public Point2DComparatorOnX(boolean flipY)
		{
			this.flipY = flipY;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
		 */
		@Override
		public int compare(Point2D o1, Point2D o2)
		{
			if (o1.y != o2.y)
			{
				// sort by x coordinate, ascending
				return Float.compare(o1.x, o2.x);
			}
			else if (o1.y != o2.y)
			{
				// if xes are equal, sort by y, descending (eventually flipped)
				if (flipY)
				{
					return Float.compare(o1.y, o2.y);
				}
				else
				{
					return Float.compare(-o1.y, -o2.y);
				}
			}
			else
			{
				// otherwise points are equal
				return 0;
			}
		}
	}

	/**
	 * This class defines the contour: The Contour of a robot r at time t, denoted by CT(r,t), is
	 * the boundary of the set U MS(s,t), where s ranges through all the robots in NW(r,t) U NE(r,t)
	 * U SE(r,t). We will call a peak of the contour any convex corner of CT (r); the concave
	 * corners will be called valleys.
	 * 
	 * @author Valerio Volpi - vale.v@me.com
	 */
	private static class Contour
	{
		private SortedList<Point2D>	points		= null;
		private Path2D				path		= null;
		private Rectangle2D			nwQuadrant	= null;
		private Rectangle2D			neQuadrant	= null;
		private Rectangle2D			seQuadrant	= null;

		/**
		 * Constructor.
		 * 
		 * @param points
		 */
		public Contour(Rectangle2D nwQuadrant, Rectangle2D neQuadrant, Rectangle2D seQuadrant, Vector<Point2D> nwPoints, Vector<Point2D> nePoints, Vector<Point2D> sePoints)
		{
			this.nwQuadrant = nwQuadrant;
			this.neQuadrant = neQuadrant;
			this.seQuadrant = seQuadrant;
			this.points = new SortedList<Point2D>(new Point2DComparatorOnY(false));

			this.computeContourShape(nwPoints, nePoints, sePoints);
			this.computePath();
		}

		/**
		 * Given a set of points, computes the contour shape, by adding points int the sorted list.
		 * 
		 * @param pointsNW
		 * @param pointsNE
		 * @param pointsSE
		 */
		private void computeContourShape(Vector<Point2D> pointsNW, Vector<Point2D> pointsNE, Vector<Point2D> pointsSE)
		{
			computeContourNW(pointsNW);
			computeContourSE(pointsSE);
			computeContourNE(pointsNW, pointsNE, pointsSE);
		}

		/**
		 * Computes the portion of the contour for NW quadrant.
		 * 
		 * @param pointsNW
		 */
		private void computeContourNW(Vector<Point2D> pointsNW)
		{
			if (pointsNW != null && !pointsNW.isEmpty())
			{
				Point2D lastAddedPoint = null;

				// sort points by x
				Collections.sort(pointsNW, new Point2DComparatorOnX(true));

				// get the point with smallest x
				Point2D smallestX = pointsNW.firstElement();

				// add smallest
				this.points.add(smallestX);
				lastAddedPoint = smallestX;

				// process other points
				for (int i = 1; i < pointsNW.size(); i++)
				{
					Point2D point = pointsNW.elementAt(i);
					if (point.y < lastAddedPoint.y)
					{
						this.points.add(point);
						lastAddedPoint = point;
					}
				}
			}
		}

		/**
		 * Computes the portion of the contour for SE quadrant.
		 * 
		 * @param pointsSE
		 */
		private void computeContourSE(Vector<Point2D> pointsSE)
		{
			if (pointsSE != null && !pointsSE.isEmpty())
			{
				Point2D lastAddedPoint = null;

				// sort points by y
				Collections.sort(pointsSE, new Point2DComparatorOnY(true));

				// get the point with largest x
				Point2D largestY = pointsSE.lastElement();

				// add smallest
				this.points.add(largestY);
				lastAddedPoint = largestY;

				// process other points
				for (int i = pointsSE.size() - 2; i >= 0; i--)
				{
					Point2D point = pointsSE.elementAt(i);
					if (point.x < lastAddedPoint.x)
					{
						this.points.add(point);
						lastAddedPoint = point;
					}
				}
			}
		}

		/**
		 * Compute NW and SE contours before computing this! Computes the portion of the contour for
		 * NE quadrant.
		 * 
		 * @param pointsNE
		 * @param pointsSE
		 */
		private void computeContourNE(Vector<Point2D> pointsNW, Vector<Point2D> pointsNE, Vector<Point2D> pointsSE)
		{
			Point2D smallestY = null;
			if (pointsNW != null && !pointsNW.isEmpty())
			{
				// compute highest y point in NW
				Collections.sort(pointsNW, new Point2DComparatorOnY(true));
				smallestY = pointsNW.lastElement();
			}

			Point2D smallestX = null;
			if (pointsSE != null && !pointsSE.isEmpty())
			{
				// compute smallest x point in SE
				Collections.sort(pointsSE, new Point2DComparatorOnX(true));
				smallestX = pointsSE.firstElement();
			}

			// add all points that are smallest than these
			for (Point2D point : pointsNE)
			{
				if (smallestX != null && smallestY != null && point.x < smallestX.x && point.y < smallestY.y)
				{
					this.points.add(point);
				}
				else if (smallestX == null && smallestY != null && point.y < smallestY.y)
				{
					this.points.add(point);
				}
				else if (smallestY == null && smallestX != null && point.x < smallestX.x)
				{
					this.points.add(point);
				}
				else if (smallestY == null && smallestX == null)
				{
					this.points.add(point);
				}
			}
		}

		/**
		 * Computes the path object that represents the contour.
		 * 
		 * @param point
		 * @return
		 */
		private void computePath()
		{
			// get valleys
			Vector<Point2D> valleys = getValleys();
			if (valleys == null)
			{
				// create a new vector to avoid NullPointerExceptions
				valleys = new Vector<Point2D>();
			}

			// create a vector of points and valleys
			Vector<Point2D> pathPoints = new Vector<Point2D>();
			for (int i = 0; i < points.size() + valleys.size(); i++)
			{
				List<Point2D> src = ((i % 2 == 0) ? points : valleys);
				int index = i / 2;

				pathPoints.add(src.get(index));
			}

			// convert all points to awt points
			Vector<java.awt.geom.Point2D> awtPoints = new Vector<java.awt.geom.Point2D>();
			for (Point2D point : pathPoints)
			{
				awtPoints.add(SycamoreUtil.convertPoint2D(point));
			}

			// create a Path2D. The first point is the projection on the NW quadrant border of the
			// first point of the contour
			if (!awtPoints.isEmpty())
			{
				this.path = new Path2D.Double();
				java.awt.geom.Point2D firstPoint = awtPoints.firstElement();
				java.awt.geom.Point2D projectionFirst = new java.awt.geom.Point2D.Double(firstPoint.getX(), nwQuadrant.getY());

				this.path.moveTo(projectionFirst.getX(), projectionFirst.getY());

				// add all points
				for (int i = 0; i < awtPoints.size(); i++)
				{
					java.awt.geom.Point2D point = awtPoints.elementAt(i);
					this.path.lineTo(point.getX(), point.getY());
				}

				// add the projection on the SE quadrant border of the last point of the contour
				java.awt.geom.Point2D lastPoint = awtPoints.lastElement();
				java.awt.geom.Point2D projectionLast = new java.awt.geom.Point2D.Double(seQuadrant.getX() + seQuadrant.getWidth(), lastPoint.getY());

				this.path.lineTo(projectionLast.getX(), projectionLast.getY());

				// add the quadrants corner
				java.awt.geom.Point2D corner = new java.awt.geom.Point2D.Double(projectionLast.getX(), projectionFirst.getY());
				this.path.lineTo(corner.getX(), corner.getY());

				// close
				this.path.lineTo(projectionFirst.getX(), projectionFirst.getY());
				this.path.closePath();
			}
		}

		/**
		 * Returns the intersection point between the contour and the passed line
		 * 
		 * @param line
		 * @return
		 */
		public Point2D intersectWith(Line2D line)
		{
			try
			{
				Set<java.awt.geom.Point2D> intersections = SycamoreUtil.getIntersections(path, line);

				if (intersections != null)
				{
					Iterator<java.awt.geom.Point2D> iterator = intersections.iterator();
					if (intersections.size() > 0)
					{
						if (intersections.size() == 1)
						{
							java.awt.geom.Point2D intersection = iterator.next();
							return SycamoreUtil.convertPoint2D(intersection);
						}
						else
						{
							// more than one intersection. return the one with lowest y coord
							Vector<java.awt.geom.Point2D> points = new Vector<java.awt.geom.Point2D>();
							while (iterator.hasNext())
							{
								points.add(iterator.next());
							}

							Collections.sort(points, new Comparator<java.awt.geom.Point2D>()
							{
								@Override
								public int compare(java.awt.geom.Point2D o1, java.awt.geom.Point2D o2)
								{
									if (o1.getY() != o2.getY())
									{
										// sort by y coordinate
										return -Double.compare(o1.getY(), o2.getY());
									}
									else if (o1.getX() != o2.getX())
									{
										// if ys are equal, sort by x
										return -Double.compare(o1.getX(), o2.getX());
									}
									else
									{
										// otherwise points are equal
										return 0;
									}
								}
							});

							return SycamoreUtil.convertPoint2D(points.lastElement());
						}
					}
				}

				return null;
			}
			catch (Exception e)
			{
				e.printStackTrace();
				return null;
			}
		}

		/**
		 * Returns the contour valleys.
		 * 
		 * @param point
		 * @return
		 */
		public Vector<Point2D> getValleys()
		{
			// if points has n elements, there are n-1 valleys
			if (points.size() > 1)
			{
				// compute all valleys
				Vector<Point2D> valleys = new Vector<Point2D>();

				for (int i = 0; i < points.size() - 1; i++)
				{
					Point2D prev = points.get(i);
					Point2D next = points.get(i + 1);

					// the valley has the x of next and the y of prev
					valleys.add(new Point2D(next.x, prev.y));
				}

				return valleys;
			}
			else
			{
				// no valley
				return null;
			}
		}

		/**
		 * Returns the valley that is in NE quadrant and that is closest to passed point.
		 * 
		 * @param point
		 * @return
		 */
		public Point2D getClosestNEValley(final Point2D point)
		{
			Vector<Point2D> valleys = getValleys();
			Vector<Point2D> neValleys = new Vector<Point2D>();

			if (valleys != null)
			{
				// consider only valleys that are in NE
				for (Point2D valley : valleys)
				{
					if (SycamoreUtil.isPointInsideRectangle(point, neQuadrant))
					{
						neValleys.add(valley);
					}
				}

				// sort valleys by distance from p
				Collections.sort(neValleys, new Comparator<Point2D>()
				{
					/*
					 * (non-Javadoc)
					 * 
					 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
					 */
					@Override
					public int compare(Point2D o1, Point2D o2)
					{
						float dinst1 = point.distanceTo(o1);
						float dinst2 = point.distanceTo(o2);

						return Float.compare(dinst1, dinst2);
					}
				});

				if (!neValleys.isEmpty())
				{
					// after sorting, first element contains closest valley
					return neValleys.firstElement();
				}
				else
				{
					// if no valley is found, return the current point
					return point;
				}
			}
			else
			{
				return null;
			}
		}
	}

	private Rectangle2D		quadrantNW	= null;
	private Rectangle2D		quadrantNE	= null;
	private Rectangle2D		quadrantSE	= null;
	private Rectangle2D		quadrantSW	= null;

	private Vector<Point2D>	robotsNW	= new Vector<Point2D>();
	private Vector<Point2D>	robotsNE	= new Vector<Point2D>();
	private Vector<Point2D>	robotsSE	= new Vector<Point2D>();
	private Vector<Point2D>	robotsSW	= new Vector<Point2D>();

	/**
	 * Default Constructor.
	 */
	public NearGathering()
	{
		try
		{
			URL file = this.getClass().getResource("/it/diunipi/volpi/sycamore/plugins/algorithms/resources/2012-SIROCCO.pdf");
			this.setPaperFilePath(new File(file.toURI()));
		}
		catch (URISyntaxException e)
		{
			e.printStackTrace();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.diunipi.volpi.sycamore.plugins.algorithms.Algorithm#init()
	 */
	@Override
	public void init(SycamoreObservedRobot<Point2D> robot)
	{
		// try to check if n is known
		try
		{
			SycamoreSystem.getN();
		}
		catch (NNotKnownException e)
		{
			JOptionPane.showMessageDialog(null, "Warning. The value of n is not known. The algorithm will not work.", "N not known", JOptionPane.WARNING_MESSAGE);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.diunipi.volpi.sycamore.plugins.Algorithm#compute(java.util.Vector,
	 * it.diunipi.volpi.sycamore.model.SycamoreObservedRobot)
	 */
	@Override
	public Point2D compute(Vector<Observation<Point2D>> snapshot, SycamoreObservedRobot<Point2D> caller)
	{
		Visibility<Point2D> visibility = caller.getVisibility();
		if (visibility != null && visibility instanceof SquaredVisibility)
		{

			// fill quadrants
			fillQuadrants(snapshot, caller);

			try
			{
				if (snapshot.isEmpty() || snapshot.size() + 1 == SycamoreSystem.getN())
				{
					System.out.println("Num: " + (snapshot.size() + 1) + ", n: " + SycamoreSystem.getN());
					System.out.println("Finished!");

					// if i see n-1 robots i'm done
					setFinished(true);
					caller.turnLightOn(ColorRGBA.Yellow);

					clearQuadrants();
					return caller.getLocalPosition();
				}
				else
				{
					// compute the contour
					Contour contour = new Contour(quadrantNW, quadrantNE, quadrantSE, robotsNW, robotsNE, robotsSE);

					Point2D dp = null; // destination point

					if (isNeClear() && isNwClear() && isSeClear())
					{
						// if I don't see robots in NW, NE, SE, no move
						dp = caller.getLocalPosition();
					}
					else
					{
						if (isNeClear() && isSeClear())
						{
							// if I see robots only in NW SW Then
							// l = Half-line from me going North;

							Point2D lPoint = new Point2D(caller.getLocalPosition().x, (float) quadrantNE.getY());

							java.awt.geom.Point2D start = SycamoreUtil.convertPoint2D(caller.getLocalPosition());
							java.awt.geom.Point2D end = SycamoreUtil.convertPoint2D(lPoint);

							dp = contour.intersectWith(new Line2D.Double(start, end));
						}
						else if (isNwClear() && isNeClear())
						{
							// If I see robots only in SE SW Then
							// l = Half-line from me going East;

							Point2D lPoint = new Point2D((float) (quadrantSE.getX() + quadrantSE.getWidth()), caller.getLocalPosition().y);

							java.awt.geom.Point2D start = SycamoreUtil.convertPoint2D(caller.getLocalPosition());
							java.awt.geom.Point2D end = SycamoreUtil.convertPoint2D(lPoint);

							dp = contour.intersectWith(new Line2D.Double(start, end));
						}
						else
						{
							if (!isNeClear())
							{
								// If There is at least one robot in NE Then
								// l = Half-line from me to the closest robot in NE;

								// find the closest
								Point2D closest = null;
								float minDinstance = Float.MAX_VALUE;
								for (Point2D point : robotsNE)
								{
									float dinstance = point.distanceTo(caller.getLocalPosition());
									if (dinstance < minDinstance)
									{
										closest = point;
									}
								}

								// now i have the closest robot in NE.
								java.awt.geom.Point2D start = SycamoreUtil.convertPoint2D(caller.getLocalPosition());
								java.awt.geom.Point2D end = SycamoreUtil.convertPoint2D(closest);

								dp = contour.intersectWith(new Line2D.Double(start, end));
								if (dp == null)
								{
									dp = closest;
								}
							}
							else
							{
								// l = Half-line from me to the only valley of CT in NE;
								dp = contour.getClosestNEValley(caller.getLocalPosition());
							}
						}

						// consider the triangle:
						// dp
						// ****
						// ********
						// B ************ A

						// B is the current position
						Point2D B = caller.getLocalPosition();

						// A is the intersection between the horizontal axis and the contour
						Point2D lPoint = new Point2D((float) (quadrantSE.getX() + quadrantSE.getWidth()), caller.getLocalPosition().y);
						java.awt.geom.Point2D start = SycamoreUtil.convertPoint2D(caller.getLocalPosition());
						java.awt.geom.Point2D end = SycamoreUtil.convertPoint2D(lPoint);
						Point2D A = contour.intersectWith(new Line2D.Double(start, end));

						if (A == null)
						{
							// if there is no intersection between l and the contour,
							// consider the intersection with border
							A = SycamoreUtil.convertPoint2D(end);
						}

						double beta = 0;
						if (!B.equals(dp))
						{
							// beta is the angle between (B-A) and (B-dp)
							Line2D BA = new Line2D.Double(B.x, B.y, A.x, A.y);
							Line2D Bdp = new Line2D.Double(B.x, B.y, dp.x, dp.y);

							// beta varies from zero to PI/2 (90 degrees)
							beta = SycamoreUtil.angleBetween2Lines(BA, Bdp);
						}

						// now consider the parallel to l that intersects the observed points
						// and compute the distance between each point and the border of the
						// visibility
						// area
						// consider only the smallest
						double distance = Double.MAX_VALUE;
						for (Observation<Point2D> observation : snapshot)
						{
							double pointDistance;
							// consider the triangle:

							// Ar -------- Br
							// | ********
							// | ***
							// Cr

							// Br is the observed robot position
							Point2D Br = observation.getRobotPosition();

							// Ar is the projection on the left border of Br
							Point2D Ar = new Point2D((float) quadrantSW.getX(), Br.y);

							// beta is the same
							// I want the dinstance Br-Cr (a = c / cos (beta))

							// the measure c is the dinstance Ar-Br
							double c = Ar.distanceTo(Br);

							double cosBeta = Math.cos(beta);

							// approx cosBeta to th 3rd decimal cypher
							long y = (long) (cosBeta * 1000);
							cosBeta = (double) y / 1000;

							if (cosBeta != 0)
							{
								double a = c / cosBeta;

								// b is the other side of the triangle (Ar-Cr)
								double b = a * Math.sin(beta);

								Point2D Cr = new Point2D(Ar.x, (float) (Ar.y - b));

								// Cr can be either on the border or outside the visible area
								float yBorder = (float) (quadrantSW.getY() - quadrantSW.getHeight());

								if (Cr.y >= yBorder)
								{
									// Cr is on border
									pointDistance = a;
								}
								else
								{
									// pointDistance is the clamping of a on the border
									Line2D aLine = new Line2D.Double(Br.x, Br.y, Cr.x, Cr.y);
									Line2D border = new Line2D.Double(quadrantSW.getX(), yBorder, quadrantSE.getX(), yBorder);
									java.awt.geom.Point2D intersection = SycamoreUtil.getIntersection(aLine, border);

									Point2D borderPont = SycamoreUtil.convertPoint2D(intersection);
									pointDistance = Br.distanceTo(borderPont);
								}
							}
							else
							{
								// case beta = 90 degrees. Simply project vertically on lower border

								float yBorder = (float) (quadrantSW.getY() - quadrantSW.getHeight());
								Point2D lowerProjection = new Point2D(Br.x, yBorder);
								pointDistance = Br.distanceTo(lowerProjection);
							}

							if (pointDistance < distance)
							{
								distance = pointDistance;
							}
						}

						// now consider the smallest between:
						// - distance between position and computed dp
						// - just computed "distance"

						double posDpDinst = caller.getLocalPosition().distanceTo(dp);
						double minDinstance = Math.min(posDpDinst, distance);
						double finalDinstance = minDinstance / 2;

						// final computation of dp: starting from currentposition, follow l for
						// a distance of finalDistance
						// compute the projection of this point on x axis
						// c = a cos (beta)
						// b = a sin (beta)

						double cosBeta = Math.cos(beta);

						// approx cosBeta to th 3rd decimal cyher
						long y = (long) (cosBeta * 1000);
						cosBeta = (double) y / 1000;

						double sinBeta = Math.sin(beta);

						// approx sinBeta to th 3rd decimal cyher
						long z = (long) (sinBeta * 1000);
						sinBeta = (double) z / 1000;

						double c = finalDinstance * cosBeta;
						double b = finalDinstance * sinBeta;

						// the new dp coordinates will be:
						float dpX = caller.getLocalPosition().x + (float) c;
						float dpY = caller.getLocalPosition().y + (float) b;

						dp = new Point2D(dpX, dpY);

						// if dp is too close to the current position, bring it back
						if (!dp.differsModuloEpsilon(caller.getLocalPosition()))
						{
							dp = caller.getLocalPosition();
						}
					}

					clearQuadrants();
					return dp;
				}
			}
			catch (NNotKnownException e)
			{
				e.printStackTrace();

				setFinished(true);

				clearQuadrants();
				return null;
			}
			catch (TooManyLightsException e)
			{
				e.printStackTrace();

				setFinished(true);

				clearQuadrants();
				return null;
			}
		}
		return caller.getLocalPosition();
	}

	/**
	 * Fill NW, NE, SE, SO quadrants by placing each robot into the right quadrant
	 */
	private void fillQuadrants(Vector<Observation<Point2D>> snapshot, SycamoreObservedRobot<Point2D> caller)
	{
		Point2D callerPosition = caller.getLocalPosition();
		float visibilityRange = VisibilityImpl.getVisibilityRange();

		float x = callerPosition.x - (visibilityRange / 2);
		float y = callerPosition.y + (visibilityRange / 2);

		// compute quadrants
		this.quadrantNW = new Rectangle2D.Float(x, y, (visibilityRange / 2), (visibilityRange / 2));
		this.quadrantNE = new Rectangle2D.Float(x + (visibilityRange / 2), y, (visibilityRange / 2), (visibilityRange / 2));
		this.quadrantSE = new Rectangle2D.Float(x + (visibilityRange / 2), y - (visibilityRange / 2), (visibilityRange / 2), (visibilityRange / 2));
		this.quadrantSW = new Rectangle2D.Float(x, y - (visibilityRange / 2), (visibilityRange / 2), (visibilityRange / 2));

		// check if some quadrant is clear
		for (Observation<Point2D> observation : snapshot)
		{
			Point2D point = observation.getRobotPosition();
			if (SycamoreUtil.isPointInsideRectangle(point, quadrantNW))
			{
				this.robotsNW.add(observation.getRobotPosition());
			}
			else if (SycamoreUtil.isPointInsideRectangle(point, quadrantNE))
			{
				this.robotsNE.add(observation.getRobotPosition());
			}
			else if (SycamoreUtil.isPointInsideRectangle(point, quadrantSE))
			{
				this.robotsSE.add(observation.getRobotPosition());
			}
			else if (SycamoreUtil.isPointInsideRectangle(point, quadrantSW))
			{
				this.robotsSW.add(observation.getRobotPosition());
			}
		}
	}

	/**
	 * Clear quandrants
	 */
	private void clearQuadrants()
	{
		this.quadrantNW = null;
		this.quadrantNE = null;
		this.quadrantSE = null;
		this.quadrantSW = null;

		this.robotsNW.removeAllElements();
		this.robotsNE.removeAllElements();
		this.robotsSE.removeAllElements();
		this.robotsSW.removeAllElements();
	}

	/**
	 * @return the nwClear
	 */
	public boolean isNwClear()
	{
		return robotsNW.isEmpty();
	}

	/**
	 * @return the neClear
	 */
	public boolean isNeClear()
	{
		return robotsNE.isEmpty();
	}

	/**
	 * @return the seClear
	 */
	public boolean isSeClear()
	{
		return robotsSE.isEmpty();
	}

	/**
	 * @return the swClear
	 */
	public boolean isSwClear()
	{
		return robotsSW.isEmpty();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.diunipi.volpi.sycamore.plugins.Algorithm#getReferences()
	 */
	@Override
	public String getReferences()
	{
		return "Getting Close without Touching, SIROCCO 2012";
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
	 * @see it.diunipi.volpi.sycamore.plugins.SycamorePlugin#getSettings()
	 */
	@Override
	public SycamorePanel getPanel_settings()
	{
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.diunipi.volpi.sycamore.plugins.SycamorePlugin#getAuthor()
	 */
	@Override
	public String getAuthor()
	{
		return "Linda Pagli, Giuseppe Prencipe, Giovanni Viglietta";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.diunipi.volpi.sycamore.plugins.SycamorePlugin#getPluginShortDescription()
	 */
	@Override
	public String getPluginShortDescription()
	{
		return "Near gathering that assumes sqared limited visibility";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.diunipi.volpi.sycamore.plugins.SycamorePlugin#getPluginLongDescription()
	 */
	@Override
	public String getPluginLongDescription()
	{
		return "In this paper we study the Near-Gathering problem for a set of asynchronous, anonymous, oblivious and autonomous "
				+ "mobile robots with limited visibility moving in Look-Compute-Move (LCM) cy- cles: In this problem, the robots have "
				+ "to get close enough to each other, so that every robot can see all the others, without touching (i.e., colliding) "
				+ "with any other robot. The importance of this problem might not be clear at a first sight: Solving the Near-Gathering problem, "
				+ "it is possible to overcome the limitations of having robots with limited visibility, and it is therefore possible to exploit "
				+ "all the studies (the majority, actually) done on this topic, in the unlimited visibility setting. In fact, after the robots get "
				+ "close enough, they are able to see all the robots in the system, a scenario similar to the one where the robots have unlimited "
				+ "visibility. Here, we present a collision-free algorithm for the Near-Gathering problem, the first to our knowledge, that allows a "
				+ "set of autonomous mobile robots to nearly gather within finite time. The collision-free feature of our solution is crucial "
				+ "in order to combine it with an unlimited visibility protocol. In fact, the majority of the algorithms that can be found on "
				+ "the topic assume that all robots occupy distinct positions at the beginning. Hence, only providing a collision-free Near-Gathering "
				+ "algorithm, as the one presented here, is it possible to successfully combine it with an unlimited visibility protocol, "
				+ "hence overcoming the natural limitations of the limited visibility scenario. In our model, distances are induced by the infinity norm. "
				+ "A discussion on how to extend our algorithm to models with different distance functions, including the usual Euclidean distance, is also presented.";
	}
}
