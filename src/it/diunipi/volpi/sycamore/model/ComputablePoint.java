/**
 * 
 */
package it.diunipi.volpi.sycamore.model;

/**
 * This interface represents a pont that is computable, so that is possible to perform computations
 * on it.
 * 
 * @author Valerio Volpi - vale.v@me.com
 */
public interface ComputablePoint<P extends SycamoreAbstractPoint>
{
	/**
	 * Returns the dinstance from this point and passed point
	 * 
	 * @param p
	 * @return
	 */
	public float distanceTo(P point);

	/**
	 * Tells if ths and passed point are equal or if they deiffer for a quantity smaller than
	 * epsilon
	 * 
	 * @param p
	 * @return
	 */
	public boolean differsModuloEpsilon(P point);

	/**
	 * Sums p1 and p2 and returns the new resulting point
	 * 
	 * @param p
	 * @return
	 */
	public P sumWith(P point);

	/**
	 * Sums p1 and p2 and returns the new resulting point
	 * 
	 * @param p
	 * @return
	 */
	public P divideFor(float value);

	/**
	 * Given a point and a ratio, computes the P object that represents the position interpolated
	 * using the positions of this point and passed point. The path is supposed to be linear and the
	 * speed uniform. The passed ratio is supposed to be included in the interval between 0 (this
	 * point) and 1 (passed point).
	 * 
	 * @param point
	 * @param ratio
	 * @return
	 */
	public P interpolateWith(P point, float ratio);

	/**
	 * Returns pitch, roll, yaw of the angle between the vector starting from this point and going
	 * to destination and the 3 axes vectors.
	 * 
	 * @param destination
	 * @return
	 */
	public float[] getRotationAngles(P destination);
}
