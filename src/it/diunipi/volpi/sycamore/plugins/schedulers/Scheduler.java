package it.diunipi.volpi.sycamore.plugins.schedulers;

import it.diunipi.volpi.sycamore.animation.Timeline;
import it.diunipi.volpi.sycamore.engine.ComputablePoint;
import it.diunipi.volpi.sycamore.engine.SycamoreAbstractPoint;
import it.diunipi.volpi.sycamore.plugins.SycamorePlugin;

/**
 * This plugin is the most important of the system: the scheduler. It is the most powerful plugin,
 * since it is the one that actually drives the simulation. The scheduler, in fact, is the one that
 * decides when a robot can look, when it can compute and when it can move. It can also decide if
 * the computed destination point is actually reached by the moving robot and at which time this
 * happens. In the system there is always and only one scheduler at the same time. The simulation
 * can run if and only if there is a scheduler set in the system. The system contains a Thread
 * object, called scheduler thread, that owns the selected scheduler and decides the frequency of
 * scheduler's steps. The behavior that the scheduler has at each step is instead delegated to the
 * plugin. In order to describe such behavior, the plugin has the availability of the following
 * methods:
 * <ul>
 * <li> <code>runLoop_pre()</code> This method is called before the starting of the simulation. The
 * scheduler can perform setup operations here.</li>
 * <li> <code>runLoopIteration()</code> This method is called at each step. In this method the
 * scheduler can decide what to do in a step. It can manage all robots, just one, or even none. It
 * can decide which of the three operations LOOK, COMPUTE, MOVE are executed on which robots. In
 * this method the Scheduler has the complete power over the robot's level of synchrony.</li>
 * <li> <code>updateTimelines()</code> This method is called after each call of the previous
 * <code>runLoopIteration()</code> method. Its purpose it to actually move the robots. Each robot,
 * in fact, owns a {@link Timeline} object that describes the robot's movement. This object is
 * accessible to the scheduler that has the power to modify it. A simple modification could be just
 * to update the ratio value, that represents a point of the overall movement; changing the ratio
 * means moving a robot. Anyway the scheduler has a complete power over the timeline of the robot,
 * so it can change the points reached by the robots or alter their speed.</li>
 * <li> <code>runLoop_post()</code> This method is called after the ending of the simulation. The
 * scheduler can perform cleaning and disposing operations here.</li>
 * </ul>
 * 
 * @author Valerio Volpi - vale.v@me.com
 */
public interface Scheduler<P extends SycamoreAbstractPoint & ComputablePoint<P>> extends SycamorePlugin
{
	/**
	 * Performs all the operations that are preliminary to the simulation. It is supposed to be
	 * called by the scheduler thread before the starting of the simulation.
	 */
	public void runLoop_pre();

	/**
	 * The code that is executed at each iteration of the run loop. It can take any number of robot
	 * and perform any operation on them. The code executed in this method has a deep influence o
	 * the behavior of the robots during the simulation. It, in fact, compleely defines the level of
	 * synchrony between the robots in the system.
	 */
	public void runLoopIteration();

	/**
	 * Performs all the operations that are successive to the simulation. It is supposed to be
	 * called by the scheduler thread after the ending of the simulation.
	 */
	public void runLoop_post();
	
	/**
	 * Updates the timelines of the robots. This method could just move the robots by updating their
	 * ratio ({@link Timeline}), or it could actually change the timelines in order to modify the
	 * destination point of the robot or increase or decrease its speed.
	 */
	public void moveRobots();
}
