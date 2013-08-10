package it.diunipi.volpi.sycamore.plugins.schedulers;

import it.diunipi.volpi.sycamore.model.ComputablePoint;
import it.diunipi.volpi.sycamore.model.SycamoreAbstractPoint;
import it.diunipi.volpi.sycamore.plugins.SycamorePlugin;

public interface Scheduler<P extends SycamoreAbstractPoint & ComputablePoint<P>> extends SycamorePlugin
{
	/**
	 * 
	 */
	public void runLoop_pre();

	/**
	 * The code that is executed at each iteration of the run loop
	 * 
	 * @return
	 */
	public void runLoopIteration();

	/**
	 * 
	 */
	public void runLoop_post();
	
	/**
	 * 
	 */
	public void updateTimelines();
}
