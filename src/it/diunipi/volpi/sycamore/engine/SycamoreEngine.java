package it.diunipi.volpi.sycamore.engine;

import it.diunipi.volpi.sycamore.gui.SycamoreSystem;
import it.diunipi.volpi.sycamore.model.ComputablePoint;
import it.diunipi.volpi.sycamore.model.Observation;
import it.diunipi.volpi.sycamore.model.SycamoreAbstractPoint;
import it.diunipi.volpi.sycamore.model.SycamoreRobot;
import it.diunipi.volpi.sycamore.model.SycamoreRobotMatrix;
import it.diunipi.volpi.sycamore.plugins.agreements.Agreement;
import it.diunipi.volpi.sycamore.plugins.algorithms.Algorithm;
import it.diunipi.volpi.sycamore.plugins.humanpilot.HumanPilotScheduler;
import it.diunipi.volpi.sycamore.plugins.initialconditions.InitialConditions;
import it.diunipi.volpi.sycamore.plugins.measures.Measure;
import it.diunipi.volpi.sycamore.plugins.memory.Memory;
import it.diunipi.volpi.sycamore.plugins.schedulers.Scheduler;
import it.diunipi.volpi.sycamore.plugins.schedulers.SchedulerImpl;
import it.diunipi.volpi.sycamore.plugins.visibilities.Visibility;
import it.diunipi.volpi.sycamore.plugins.visibilities.VisibilityImpl;
import it.diunipi.volpi.sycamore.util.SycamoreFiredActionEvents;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;
import java.util.concurrent.Callable;

import com.jme3.math.ColorRGBA;

/**
 * The Sycamore engine. Contains all the data related to the current simulation: the robots, the
 * schedulers and all the others plugins. Cares about the management of all the objects in the model
 * and about the state of the system, for instance which objects are present and which others are
 * visible. The robots, during the simulation, can query the engine to obtain the snapshot of the
 * system and also the user interface of the application and the OpenGL scene can query the engine
 * to know what data to draw. Every observation is made by the engine, that is the only object in
 * the system that knows the local and global coordinates of any visible object. An engine is valid
 * if there is at least a robot, and if its current scheduler is set (not null).
 * 
 * @author Valerio Volpi - vale.v@me.com
 * 
 * @param <P>
 *            the type of point on which this engine and all the contained objects (robots,
 *            plugins...) are build.
 */
public abstract class SycamoreEngine<P extends SycamoreAbstractPoint & ComputablePoint<P>>
{
	/**
	 * The possible types. These are not only used in engine, but also in robots and typed plugins.
	 * 
	 * @author Valerio Volpi - vale.v@me.com
	 */
	public static enum TYPE
	{
		TYPE_2D("2D"), TYPE_3D("3D");

		private String	shortDescription	= null;

		TYPE(String shortDescription)
		{
			this.shortDescription = shortDescription;
		}

		/**
		 * @return the shortDescription
		 */
		public String getShortDescription()
		{
			return shortDescription;
		}
	}

	// robots
	protected SycamoreRobotMatrix<P>			robots						= null;

	// plugins
	protected InitialConditions<P>				initialConditions			= null;
	private Scheduler<P>						currentScheduler			= null;
	private Vector<Measure>						currentMeasures				= null;
	private HumanPilotScheduler<P>				humanPilotScheduler			= null;

	// auxiliary data
	private HashMap<SycamoreRobot<P>, Float>	ratioSnapshot				= null;
	private float								animationSpeedMultiplier	= 10;
	private Vector<ActionListener>				listeners					= null;

	/**
	 * Default constructor.
	 */
	public SycamoreEngine()
	{
		this.robots = new SycamoreRobotMatrix<P>();

		this.humanPilotScheduler = new HumanPilotScheduler<P>();
		this.humanPilotScheduler.setAppEngine(this);

		this.currentMeasures = new Vector<Measure>();
		this.listeners = new Vector<ActionListener>();

		this.ratioSnapshot = new HashMap<SycamoreRobot<P>, Float>();
	}

	/**
	 * Adds an <code>ActionListener</code> to the button.
	 * 
	 * @param listener
	 *            the <code>ActionListener</code> to be added
	 */
	public void addActionListener(ActionListener listener)
	{
		this.listeners.add(listener);
	}

	/**
	 * Removes an <code>ActionListener</code> from the button. If the listener is the currently set
	 * <code>Action</code> for the button, then the <code>Action</code> is set to <code>null</code>.
	 * 
	 * @param listener
	 *            the listener to be removed
	 */
	public void removeActionListener(ActionListener listener)
	{
		this.listeners.remove(listener);
	}

	/**
	 * Fires passed ActionEvent to all registered listeners, by calling <code>ActionPerformed</code>
	 * method on all of them.
	 * 
	 * @param e
	 */
	private void fireActionEvent(ActionEvent e)
	{
		for (ActionListener listener : this.listeners)
		{
			listener.actionPerformed(e);
		}
	}

	/**
	 * @return the robots vectors
	 */
	public SycamoreRobotMatrix<P> getRobots()
	{
		return this.robots;
	}

	/**
	 * @return the robots with passed index. if the asked element is the "size + 1"-th, creates it
	 */
	public Vector<SycamoreRobot<P>> getRobots(int index)
	{
		return robots.getRobotRow(index);
	}

	/**
	 * @return the animationSpeedMultiplier
	 */
	public float getAnimationSpeedMultiplier()
	{
		return animationSpeedMultiplier;
	}

	/**
	 * @param animationSpeedMultiplier
	 *            the animationSpeedMultiplier to set
	 */
	public void setAnimationSpeedMultiplier(float animationSpeedMultiplier)
	{
		this.animationSpeedMultiplier = animationSpeedMultiplier;
	}

	/**
	 * @param selected
	 */
	public void setRobotLightsVisible(boolean visible)
	{
		Iterator<SycamoreRobot<P>> iterator = this.robots.iterator();
		while (iterator.hasNext())
		{
			SycamoreRobot<P> robot = iterator.next();
			robot.updateLightsVisible();
		}
	}

	/**
	 * @return the initialConditions
	 */
	public InitialConditions<P> getInitialConditions()
	{
		return initialConditions;
	}

	/**
	 * @return the currentScheduler
	 */
	public Scheduler<P> getCurrentScheduler()
	{
		return currentScheduler;
	}

	/**
	 * @param currentScheduler
	 *            the currentScheduler to set
	 */
	protected void setCurrentScheduler(Scheduler<P> currentScheduler)
	{
		this.currentScheduler = currentScheduler;
	}

	/**
	 * @return the currentMeasures
	 */
	public Vector<Measure> getCurrentMeasures()
	{
		return currentMeasures;
	}

	/**
	 * @param currentMeasures
	 *            the currentMeasures to set
	 */
	protected void setCurrentMeasures(Vector<Measure> currentMeasures)
	{
		this.currentMeasures = currentMeasures;
	}

	/**
	 * @return the humanPilotRobots
	 */
	public Vector<SycamoreRobot<P>> getHumanPilotRobots(int index)
	{
		return robots.getHumanPilotRow(index);
	}

	/**
	 * @return the humanPilotScheduler
	 */
	public HumanPilotScheduler<P> getHumanPilotScheduler()
	{
		return humanPilotScheduler;
	}

	/**
	 * @return The positions of all the robots in the system
	 * @throws TimelineNotAccessibleException
	 */
	public Observation<P> getObservation(SycamoreRobot<P> robot, SycamoreRobot<P> callee)
	{
		P position = robot.getGlobalPosition();
	
		// translate position to callee's local coords
		if (callee.getAgreement() != null)
		{
			position = callee.getAgreement().toLocalCoordinates(position);
		}
		
		return new Observation<P>(position, robot.getLights(), robot.getAlgorithm().isHumanPilot());
	}

	/**
	 * @return true if the simulation is finished, false otherwise. The simulation is finished if
	 *         every robot is finished.
	 */
	public synchronized boolean isSimulationFinished()
	{
		boolean simulationFinished = true;
		// the simulation is finished if every robot is finished.

		Iterator<SycamoreRobot<P>> iterator = this.robots.iterator();
		while (iterator.hasNext())
		{
			SycamoreRobot<P> robot = iterator.next();
			if (!robot.isFinished())
			{
				simulationFinished = false;
			}
		}

		return simulationFinished;
	}

	/**
	 * Returns a list of <code>Observation</code> objects, that contains informations about the
	 * positions and the lights of every robot in the system, excluding the callee of the method.
	 * 
	 * @return
	 * @throws TimelineNotAccessibleException
	 *             if someone tries to access a timeline without permissions.
	 */
	public Vector<Observation<P>> getObservations(SycamoreRobot<P> callee)
	{
		Vector<Observation<P>> observations = new Vector<Observation<P>>();

		Iterator<SycamoreRobot<P>> iterator = this.robots.iterator();
		while (iterator.hasNext())
		{
			SycamoreRobot<P> robot = iterator.next();

			// ask the robot for the obsevation.
			Observation<P> observation = this.getObservation(robot, callee);

			if (observation != null && robot != callee)
			{
				observations.add(observation);
			}
		}

		// get visibility and filter
		Visibility<P> visibility = callee.getVisibility();

		if (visibility == null)
		{
			return observations;
		}
		else
		{
			return visibility.filter(observations, callee.getLocalPosition());
		}
	}

	/**
	 * Dispose this engine and all its contents.
	 */
	public void dispose()
	{
		this.robots.clear();

		this.currentMeasures.clear();
		this.currentScheduler = null;
	}

	/**
	 * Add a new element in the lists of robots
	 */
	public void addNewRobotListElement()
	{
		this.getRobots().addRobotRow();
		this.getRobots().addHumanPilotRow();
	}

	/**
	 * @param index
	 * @return
	 */
	public Vector<SycamoreRobot<P>> removeRobotListElement(int index)
	{
		// save robots before removal
		Vector<SycamoreRobot<P>> robotsToRemove = robots.getRobotRow(index);
		robotsToRemove.addAll(robots.getHumanPilotRow(index));

		// remove them
		robots.removeRobotRow(index);
		robots.removeHumanPilotRow(index);

		return robotsToRemove;
	}

	/**
	 * Computes a starting point for a new robot
	 * 
	 * @param pointType
	 * @return
	 */
	protected abstract P computeStartingPoint();

	/**
	 * Creates a new robot with passed algorithm and color, and adds it to the index-th list.
	 * 
	 * @param algorithm
	 * @param color
	 * @param index
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	public abstract SycamoreRobot<P> createAndAddNewRobotInstance(boolean isHumanPilot, int index, ColorRGBA color, int numLights);

	/**
	 * Removes a robot (any) from the index-th list
	 * 
	 * @param index
	 * @return
	 */
	public SycamoreRobot<P> removeRobot(boolean isHumanPilot, int index)
	{
		if (isHumanPilot)
		{
			return robots.removeHumanPilot(index, 0);
		}
		else
		{
			return robots.removeRobot(index, 0);
		}
	}

	/**
	 * Creates a new scheduler instance using passed interface, and sets such instance as the
	 * current scheduler in this engine.
	 * 
	 * @param scheduler
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	public void creatAndSetNewSchedulerInstance(Scheduler scheduler) throws IllegalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException
	{
		if (scheduler != null)
		{
			// create a new instance of the scheduler
			Class<? extends Scheduler> schedulerClass = scheduler.getClass();
			Constructor<?> constructor = schedulerClass.getConstructors()[0];
			SchedulerImpl<P> newInstance = (SchedulerImpl<P>) constructor.newInstance();

			newInstance.setAppEngine(this);
			this.setCurrentScheduler(newInstance);
		}
		else
		{
			this.setCurrentScheduler(null);
		}
	}

	/**
	 * Creates a new initial conditions instance using passed interface, and sets such instance as
	 * the current initial conditions in this engine.
	 * 
	 * @param initialConditions
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	public abstract void createAndSetNewInitialConditionsInstance(InitialConditions<P> initialConditions) throws IllegalArgumentException, InstantiationException, IllegalAccessException,
			InvocationTargetException;

	/**
	 * Creates a new visibility instance using passed interface, and sets such instance as the
	 * visibility in all the robots in the engine. This visibility will be applied also the newly
	 * created robots, since this moment.
	 * 
	 * @param visibility
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	public abstract void createAndSetNewVisibilityInstance(Visibility<P> visibility) throws IllegalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException;

	/**
	 * Creates a new memory instance using passed interface, and sets such instance as the memory in
	 * all the robots in the engine. This memory will be applied also the newly created robots,
	 * since this moment.
	 * 
	 * @param algorithm
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	public abstract void createAndSetNewMemoryInstance(Memory<P> memory) throws IllegalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException;

	/**
	 * Creates a new algorithm instance using passed interface, and sets such instance as the
	 * algorithm in the robots of the list identified with passed index. This algorithm will be
	 * applied also the newly created robots of such list, since this moment.
	 * 
	 * @param algorithm
	 * @param index
	 * @throws IllegalArgumentException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public abstract void createAndSetNewAlgorithmInstance(Algorithm<P> algorithm, int index) throws IllegalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException;

	/**
	 * Creates a new agreement instance using passed interface, and sets such instance as the
	 * agreement in all the robots in the engine. This agreement will be applied also the newly
	 * created robots, since this moment.
	 * 
	 * @param agreement
	 * @throws IllegalArgumentException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public abstract void createAndSetNewAgreementInstance(Agreement<P> agreement) throws IllegalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException;

	/**
	 * Returns true if this engine is valid. An engine is valid if there is at least a robot, and if
	 * its current scheduler is set (not null).
	 * 
	 * @return
	 */
	public boolean isValid()
	{
		if (currentScheduler != null)
		{
			return true;
		}
		return false;
	}

	/**
	 * Reset the simulation data to bring it to the initial configuration
	 */
	public void reset()
	{
		SycamoreSystem.enqueueToJME(new Callable<Object>()
		{
			@Override
			public synchronized Object call() throws Exception
			{
				// reset robots
				Iterator<SycamoreRobot<P>> iterator = robots.iterator();
				while (iterator.hasNext())
				{
					SycamoreRobot<P> robot = iterator.next();
					robot.reset();
				}

				return null;
			}
		});
	}

	/**
	 * Performs all the operations necessary to manage the ending of a simulation. Concretely it
	 * fires an event to the GUI and eventually resets the system, depending on the value of passed
	 * flag.
	 */
	public void manageSimulationFinished(boolean doReset)
	{
		fireActionEvent(new ActionEvent(this, 0, SycamoreFiredActionEvents.SIMULATION_FINISHED.name()));
		if (doReset)
		{
			this.reset();
		}
	}

	/**
	 * Call the <code>onSimulationStart()</code> method on all the measures registed in this engine.
	 */
	public void performMeasuresSimulationStart()
	{
		for (Measure measure : this.currentMeasures)
		{
			Iterator<SycamoreRobot<P>> iterator = this.robots.iterator();
			while (iterator.hasNext())
			{
				SycamoreRobot<P> robot = iterator.next();
				measure.onSimulationStart(robot);
			}
		}
	}

	/**
	 * Call the <code>onSimulationStep()</code> method on all the measures registed in this engine.
	 */
	public void performMeasuresSimulationStep()
	{
		for (Measure measure : this.currentMeasures)
		{
			Iterator<SycamoreRobot<P>> iterator = this.robots.iterator();
			while (iterator.hasNext())
			{
				SycamoreRobot<P> robot = iterator.next();
				measure.onSimulationStep(robot);
			}
		}
	}

	/**
	 * Call the <code>onSimulationEnd()</code> method on all the measures registed in this engine.
	 */
	public void performMeasuresSimulationEnd()
	{
		for (Measure measure : this.currentMeasures)
		{
			Iterator<SycamoreRobot<P>> iterator = this.robots.iterator();
			while (iterator.hasNext())
			{
				SycamoreRobot<P> robot = iterator.next();
				measure.onSimulationEnd(robot);
			}
		}
	}

	/**
	 * @param visibilityRangesVisible
	 *            the visibilityRangesVisible to set
	 */
	public void setVisibilityRangesVisible(boolean visibilityRangesVisible)
	{
		// update visibility range in robots
		Iterator<SycamoreRobot<P>> iterator = this.robots.iterator();
		while (iterator.hasNext())
		{
			SycamoreRobot<P> robot = iterator.next();
			robot.updateVisibilityRangesVisible();
		}
	}

	/**
	 * @param movementDirectionsVisible
	 *            the movementDirectionsVisible to set
	 */
	public void setMovementDirectionsVisible(boolean movementDirectionsVisible)
	{
		// update movement directions in robots
		Iterator<SycamoreRobot<P>> iterator = this.robots.iterator();
		while (iterator.hasNext())
		{
			SycamoreRobot<P> robot = iterator.next();
			robot.updateMovementDirectionsVisible();
		}
	}

	/**
	 * Updates the visibility ranges in all the robots in this engine
	 */
	public void updateVisibilityRange()
	{
		// update visibilities in all robots
		Iterator<SycamoreRobot<P>> iterator = this.robots.iterator();
		while (iterator.hasNext())
		{
			SycamoreRobot<P> robot = iterator.next();
			VisibilityImpl<P> visibilityImpl = (VisibilityImpl<P>) robot.getVisibility();
			if (visibilityImpl != null)
			{
				visibilityImpl.updateVisibilityGeometry();
			}
		}
	}

	/**
	 * Returns the TYPE enum element corresponding to the type of this engine.
	 * 
	 * @return
	 */
	public abstract TYPE getType();

	/**
	 * Returns the number of robots that are in this engine
	 * 
	 * @return
	 */
	public int getRobotsCount()
	{
		return this.robots.size();
	}

	/**
	 * Sets passed color as the color of all the robots in the i-th list. The isHumanPilot flag is
	 * used to determine which robots set is to be used.
	 * 
	 * @param i
	 * @param color
	 * @param isHumanPilot
	 */
	public void setRobotsColor(int i, ColorRGBA color, boolean isHumanPilot)
	{
		Vector<SycamoreRobot<P>> robotsList = isHumanPilot ? robots.getHumanPilotRow(i) : this.robots.getRobotRow(i);
		for (SycamoreRobot<P> robot : robotsList)
		{
			robot.setColor(color);
		}
	}

	/**
	 * Sets passed value as the max number of supported lights in all the robots in the i-th list.
	 * The isHumanPilot flag is used to determine which robots set is to be used.
	 * 
	 * @param i
	 * @param value
	 */
	public void setRobotsMaxLights(int i, Integer value, boolean isHumanPilot)
	{
		Vector<SycamoreRobot<P>> robotsList = isHumanPilot ? robots.getHumanPilotRow(i) : this.robots.getRobotRow(i);
		for (SycamoreRobot<P> robot : robotsList)
		{
			robot.setMaxLights(value);
		}
	}

	/**
	 * Performs a snapshot of all the ratios of all the timelines in the system. This snapshot is
	 * used by the manual-control of the system to correctly compute the percentages of the ratios.
	 */
	public synchronized void makeRatioSnapshot()
	{
		Iterator<SycamoreRobot<P>> iterator = this.robots.iterator();
		while (iterator.hasNext())
		{
			SycamoreRobot<P> robot = iterator.next();
			this.ratioSnapshot.put(robot, robot.getCurrentRatio());
		}
	}

	/**
	 * Clears the data structure that contains the snapshots of the ratios.
	 */
	public synchronized void clearRatioSnapshot()
	{
		this.ratioSnapshot.clear();
	}

	/**
	 * Modifies all the ratios of all the animated objects in the system, by putting as their
	 * current ratio a percentage of the ratio that they have at the moment of the call of this
	 * method. In order to have this method behave correctly, it is important that a snapshot of the
	 * original ratios is made before a sequence of calls to the<code>setRobotRatioPercentage</code>
	 * method. The <code>makeRatioSnapshot()</code> method takes care of this.
	 * 
	 * @param percentage
	 */
	public synchronized void setRobotRatioPercentage(float percentage)
	{
		Iterator<SycamoreRobot<P>> iterator = this.robots.iterator();
		while (iterator.hasNext())
		{
			SycamoreRobot<P> robot = iterator.next();
			Float originalRatio = this.ratioSnapshot.get(robot);
			if (originalRatio != null)
			{
				float newRatio = (originalRatio / 100.0f) * percentage;
				robot.setCurrentRatio(newRatio);
			}
		}
	}
}
