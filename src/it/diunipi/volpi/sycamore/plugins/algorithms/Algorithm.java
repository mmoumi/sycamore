package it.diunipi.volpi.sycamore.plugins.algorithms;

import it.diunipi.volpi.sycamore.model.ComputablePoint;
import it.diunipi.volpi.sycamore.model.Observation;
import it.diunipi.volpi.sycamore.model.SycamoreAbstractPoint;
import it.diunipi.volpi.sycamore.model.SycamoreObservedRobot;
import it.diunipi.volpi.sycamore.plugins.SycamoreTypedPlugin;

import java.util.Vector;

/**
 * This interface represents an algorithm. It basically offers the <code>compute()</code> method
 * that can be implemented to define the algorithm's behavior. At each call of the
 * <code>compute()</code> method, the system obtains a new destination point that will be reached by
 * the robot during move phase. The compute is called by the system, and the obseravtion that is
 * given to it is obtained by the robot in the look phase. While implementing a plugin, it is not
 * recommended to start directly from the <code>Algorithm</code> interface, but it is suggested to
 * extend the <code>AlgorithmImpl</code> class instead.
 * 
 * @author Valerio Volpi - vale.v@me.com
 */
public interface Algorithm<P extends SycamoreAbstractPoint & ComputablePoint<P>> extends SycamoreTypedPlugin
{
	/**
	 * Returns true if the execution of the algorithm is finished, false otherwise.
	 * 
	 * @return
	 */
	public boolean isFinished();

	/**
	 * The computation method. At each call of the <code>compute()</code> method, the system obtains
	 * a new destination point that will be reached by the robot during move phase. The compute is
	 * called by the system, and the obseravtion that is given to it is obtained by the robot in the
	 * look phase.
	 * 
	 * @param observations
	 * @param calleeInformations
	 * @return
	 */
	public P compute(Vector<Observation<P>> observations, SycamoreObservedRobot<P> callee);

	/**
	 * Returns the path of the file where the original paper is stored
	 * 
	 * @return
	 */
	public String getPaperFilePath();

	/**
	 * Returns the academic references of this algorithm
	 * 
	 * @return
	 */
	public String getReferences();

	/**
	 * Resets the algorithm to its original state.
	 */
	public void reset();

	/**
	 * Returns true if this algorithm is a human pilot, false otherwise.
	 * 
	 * @return
	 */
	public boolean isHumanPilot();

}
