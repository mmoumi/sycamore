/**
 * 
 */
package it.diunipi.volpi.sycamore.engine;

/**
 * This interface represents a pont that is computable, so that is possible to perform computations
 * on it.
 * 
 * @author Valerio Volpi - vale.v@me.com
 */
public interface ComputablePoint<P extends SycamoreAbstractPoint>
{
	/**
	 * Returns the distance from this point and passed point.
	 * 
	 * @param point
	 *            the point to which compute the distance.
	 * @return the distance from this point and passed point.
	 */
	public float distanceTo(P point);

	/**
	 * Tells if this and passed point are equal or if they differ for a quantity smaller than
	 * epsilon. Formally: (this.distanceTo(point) < epsilon).
	 * 
	 * @param point
	 *            the point to check.
	 * @return true iff (this.distanceTo(point) < epsilon).
	 */
	public boolean differsModuloEpsilon(P point);

	/**
	 * Sums the coordinates of this and passed point and returns the new resulting point.
	 * 
	 * @param point
	 *            the point to sum with this.
	 * @return the point resulting from the sum of this point with passed one.
	 */
	public P sumWith(P point);

	/**
	 * Divides all the coordinates of this point for the passed value and returns the new resulting
	 * point.
	 * 
	 * @param value
	 *            the vaue to divide this point for.
	 * @return the point resulting from the division of the coordinates of this point for passed
	 *         value.
	 */
	public P divideFor(float value);

	/**
	 * Given a point and a ratio, computes the P object that represents the position interpolated
	 * using the positions of this point and passed point. The path is supposed to be linear and the
	 * speed uniform. The passed ratio is supposed to be included in the interval between 0 (this
	 * point) and 1 (passed point).
	 * 
	 * @param point
	 *            the point with wich to interpolate this
	 * @param ratio
	 *            a float value between 0 and 1 that represents the interpolation ratio.
	 * @return the point resulting from the interpolation of this with passed point at given ratio.
	 */
	public P interpolateWith(P point, float ratio);

	/**
	 * Returns pitch, roll, yaw of the angle between the vector starting from this point and going
	 * to destination and the 3 axes vectors.
	 * 
	 * @param destination
	 *            the point used to build the vector angles
	 * @return pitch, roll, yaw of the angle between the vector starting from this point and going
	 *         to destination and the 3 axes vectors.
	 */
	public float[] getRotationAngles(P destination);
}
