package it.diunipi.volpi.sycamore.engine;

import it.diunipi.volpi.sycamore.animation.SycamoreAnimatedObject;
import it.diunipi.volpi.sycamore.animation.Timeline;
import it.diunipi.volpi.sycamore.gui.SycamoreSystem;
import it.diunipi.volpi.sycamore.plugins.agreements.Agreement;
import it.diunipi.volpi.sycamore.plugins.agreements.AgreementImpl;
import it.diunipi.volpi.sycamore.plugins.algorithms.Algorithm;
import it.diunipi.volpi.sycamore.plugins.memory.Memory;
import it.diunipi.volpi.sycamore.plugins.memory.SycamoreSystemMemory;
import it.diunipi.volpi.sycamore.plugins.visibilities.Visibility;
import it.diunipi.volpi.sycamore.util.SubsetFairnessSupporter;

import java.security.SecureRandom;
import java.util.Vector;
import java.util.concurrent.Callable;

import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;

/**
 * This class represents a robot in the system. It has a timeline and it cam nove in the enviroment.
 * It is represented in the JME system with a spatial. A robots has an algorithm that it executes.
 * It also have some lights, a visibility, a memory and a speed. The robot can have some states,
 * defined in an enumeration
 * 
 * @author Valerio Volpi - vale.v@me.com
 * 
 * @param <P>
 * @param <O>
 */
public abstract class SycamoreRobot<P extends SycamoreAbstractPoint & ComputablePoint<P>> extends SycamoreAnimatedObject<P> implements SubsetFairnessSupporter, SycamoreObservedRobot<P>
{
	/**
	 * The possible states of the robot
	 * 
	 * @author Valerio Volpi - volpiv@cli.di.unipi.it
	 */
	public static enum ROBOT_STATE
	{
		READY_TO_LOOK, LOOKING, READY_TO_COMPUTE, COMPUTING, READY_TO_MOVE, MOVING;
	}

	// constants
	protected static final float				geometrySize		= 0.25f;
	protected static final float				lightSize			= 0.1f;
	protected static final ColorRGBA			glassColor			= new ColorRGBA(1.0f, 1.0f, 1.0f, 0.75f);

	// utility data
	private final SycamoreSystemMemory<P>		systemMemory;
	private final Vector<SycamoreRobotLight<P>>	lights;
	private final long							ID;

	// model data
	protected SycamoreEngine<P>					engine				= null;
	protected Algorithm<P>						algorithm			= null;
	protected Visibility<P>						visibility			= null;
	protected Memory<P>							memory				= null;
	protected Agreement<P>						agreement			= null;

	// JME data
	protected Node								robotNode			= null;
	protected Geometry							sceneGeometry		= null;
	protected Geometry							directionGeometry	= null;
	protected ColorRGBA							color				= null;

	// state informations
	protected int								maxLights			= 0;
	protected float								speed				= 0;
	private ROBOT_STATE							currentState		= ROBOT_STATE.READY_TO_LOOK;
	private int									currentLights		= 0;

	// intermedate operations data
	private Vector<Observation<P>>				snapshot			= null;

	/**
	 * Default constructor.
	 * 
	 * @param algorithm
	 */
	public SycamoreRobot(SycamoreEngine<P> engine, P startingPosition, ColorRGBA color, int maxLights)
	{
		this(engine, startingPosition, SycamoreSystem.DEFAULT_ROBOT_SPEED, color, maxLights);
	}

	/**
	 * Default constructor.
	 * 
	 * @param algorithm
	 */
	public SycamoreRobot(final SycamoreEngine<P> engine, P startingPosition, float speed, final ColorRGBA color, final int maxLights)
	{
		this.engine = engine;
		this.maxLights = maxLights;
		this.systemMemory = new SycamoreSystemMemory<P>();
		this.lights = new Vector<SycamoreRobotLight<P>>();

		this.speed = speed;

		// setup timeline
		this.timeline = new Timeline<P>();
		
		this.startingPosition = startingPosition;
		this.timeline.addKeyframe(startingPosition);
		this.setCurrentRatio(0);

		this.color = color;

		SycamoreSystem.enqueueToJME(new Callable<Object>()
		{
			/*
			 * (non-Javadoc)
			 * 
			 * @see java.util.concurrent.Callable#call()
			 */
			@Override
			public Object call() throws Exception
			{
				// stup geometries
				setupGeometry();

				// add default black lights
				for (int i = 0; i < maxLights; i++)
				{
					final SycamoreRobotLight light = createNewLightInstance();
					lights.add(light);

					if (SycamoreSystem.isRobotsLightsVisible())
					{
						robotNode.attachChild(light.getLightGeometry());
					}
				}

				return null;
			}
		});

		// assign random ID
		SecureRandom random = new SecureRandom();
		this.ID = random.nextLong();
	}
	
	protected abstract SycamoreRobotLight<P> createNewLightInstance();

	public P getGlobalPosition()
	{
		if (this.agreement != null)
		{
			return this.agreement.toGlobalCoordinates(getLocalPosition());
		}
		else
		{
			return this.getLocalPosition();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.diunipi.volpi.sycamore.animation.SycamoreAnimatedObject#setCurrentRatio(float)
	 */
	@Override
	public synchronized void setCurrentRatio(float currentRatio)
	{
		super.setCurrentRatio(currentRatio);
		if (currentRatio < 1)
		{
			updateDirectionGeometry();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * it.diunipi.volpi.sycamore.animation.SycamoreAnimatedObject#setDirection(it.diunipi.volpi.
	 * sycamore.model.SycamoreAbstractPoint)
	 */
	@Override
	public void setDirection(P direction)
	{
		super.setDirection(direction);
		updateDirectionGeometry();
	}

	/**
	 * Updates the geometry that represents the direction (the arrow) in order to reflect a change
	 * in the direction vector.
	 */
	private void updateDirectionGeometry()
	{
		if (SycamoreSystem.isMovementDirectionsVisible() && getDirection() != null && this.sceneGeometry != null)
		{
			float[] angles = this.getLocalPosition().getRotationAngles(getDirection());
			final Quaternion rotation = new Quaternion(angles);

			SycamoreSystem.enqueueToJME(new Callable<Object>()
			{
				@Override
				public Object call() throws Exception
				{
					directionGeometry.setLocalRotation(rotation);
					return null;
				}
			});
		}
	}

	/**
	 * @return the directionGeometry
	 */
	public Geometry getDirectionGeometry()
	{
		return directionGeometry;
	}

	/**
	 * @param maxLights
	 *            the maxLights to set
	 */
	public synchronized void setMaxLights(final int maxLights)
	{
		final int oldMaxLights = this.maxLights;
		this.maxLights = maxLights;

		if (maxLights > oldMaxLights)
		{
			SycamoreSystem.enqueueToJME(new Callable<Object>()
			{
				/*
				 * (non-Javadoc)
				 * 
				 * @see java.util.concurrent.Callable#call()
				 */
				@Override
				public Object call() throws Exception
				{
					int lightsNum = maxLights - oldMaxLights;

					// add missing off lights
					for (int i = 0; i < lightsNum; i++)
					{
						final SycamoreRobotLight light = createNewLightInstance();
						lights.add(light);

						if (SycamoreSystem.isRobotsLightsVisible())
						{
							robotNode.attachChild(light.getLightGeometry());
						}
					}

					return null;
				}
			});
		}
		else
		{
			SycamoreSystem.enqueueToJME(new Callable<Object>()
			{
				/*
				 * (non-Javadoc)
				 * 
				 * @see java.util.concurrent.Callable#call()
				 */
				@Override
				public Object call() throws Exception
				{
					int lightsNum = oldMaxLights - maxLights;

					// add missing off lights
					for (int i = 0; i < lightsNum; i++)
					{
						SycamoreRobotLight light = lights.remove(lights.size() - 1);
						robotNode.detachChild(light.getLightGeometry());
					}

					return null;
				}
			});
		}
	}

	/**
	 * Setup the geometry that represents this robot.
	 */
	protected abstract void setupGeometry();

	/**
	 * Returns a geometry that represents a light of this robot Returns a new instance at each call
	 */
	protected abstract Geometry getNewLightGeometry(ColorRGBA color);

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.diunipi.volpi.sycamore.util.SubsetFairnessSupporter#getID()
	 */
	@Override
	public long getID()
	{
		return ID;
	}

	/**
	 * @return algorithm
	 */
	public Algorithm<P> getAlgorithm()
	{
		return algorithm;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.diunipi.volpi.sycamore.model.SycamoreObservedRobot#getVisibility()
	 */
	@Override
	public Visibility<P> getVisibility()
	{
		return visibility;
	}

	/**
	 * @return the memory
	 */
	public Memory<P> getMemory()
	{
		return memory;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.diunipi.volpi.sycamore.model.SycamoreObservedRobot#getLights()
	 */
	@Override
	public Vector<SycamoreRobotLight<P>> getLights()
	{
		return lights;
	}

	/**
	 * @return the agreement
	 */
	public Agreement<P> getAgreement()
	{
		return agreement;
	}

	/**
	 * @param algorithm
	 *            the algorithm to set
	 */
	public synchronized void setAlgorithm(Algorithm<P> algorithm)
	{
		this.algorithm = algorithm;
	}

	/**
	 * @param visibilitytoSet
	 *            the visibility to set
	 */
	public synchronized void setVisibility(final Visibility<P> visibilitytoSet)
	{
		// detach current geometry
		if (this.visibility != null)
		{
			final Visibility<P> backupVisibility = this.visibility;
			SycamoreSystem.enqueueToJME(new Callable<Object>()
			{
				/*
				 * (non-Javadoc)
				 * 
				 * @see java.util.concurrent.Callable#call()
				 */
				@Override
				public Object call() throws Exception
				{
					robotNode.detachChild(backupVisibility.getVisibilityRangeGeometry());
					return null;
				}
			});

		}

		// set visibility
		this.visibility = visibilitytoSet;

		SycamoreSystem.enqueueToJME(new Callable<Object>()
		{
			/*
			 * (non-Javadoc)
			 * 
			 * @see java.util.concurrent.Callable#call()
			 */
			@Override
			public Object call() throws Exception
			{

				if (SycamoreSystem.isVisibilityRangesVisible() && visibilitytoSet != null)
				{
					robotNode.attachChild(visibilitytoSet.getVisibilityRangeGeometry());
				}

				return null;
			}
		});
	}

	/**
	 * @param memory
	 *            the memory to set
	 */
	public synchronized void setMemory(Memory<P> memory)
	{
		memory.setSystemMemory(systemMemory);
		this.memory = memory;
	}

	/**
	 * @param agreement
	 *            the agreement to set
	 */
	public void setAgreement(AgreementImpl<P> agreement)
	{
		this.agreement = agreement;
		if (agreement != null)
		{
			this.agreement.setOwner(this);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.diunipi.volpi.sycamore.model.SycamoreObservedRobot#isFinished()
	 */
	@Override
	public boolean isFinished()
	{
		if (algorithm != null)
		{
			return algorithm.isFinished();
		}
		else
			return true;
	}

	/**
	 * @return speed
	 */
	public float getSpeed()
	{
		return speed;
	}

	/**
	 * @return the robotNode
	 */
	public Node getRobotNode()
	{
		return robotNode;
	}

	/**
	 * @return the duration of the timeline
	 */
	public float getTimelineDuration()
	{
		return this.timeline.getDuration();
	}

	/**
	 * @return currentState
	 */
	public ROBOT_STATE getCurrentState()
	{
		return currentState;
	}

	/**
	 * @param currentState
	 *            the currentState to set
	 */
	public synchronized void setCurrentState(ROBOT_STATE currentState)
	{
		this.currentState = currentState;
	}

	/**
	 * @param color
	 *            the color to set
	 */
	public synchronized void setColor(final ColorRGBA color)
	{
		this.color = color;

		SycamoreSystem.enqueueToJME(new Callable<Object>()
		{
			@Override
			public Object call() throws Exception
			{

				Material mat = sceneGeometry.getMaterial();
				mat.setColor("Ambient", color);
				mat.setColor("Diffuse", color);
				mat.setColor("Specular", ColorRGBA.White);

				return null;
			}
		});
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.diunipi.volpi.sycamore.model.SycamoreObservedRobot#addLight(com.jme3.math.ColorRGBA)
	 */
	@Override
	public synchronized void turnLightOn(ColorRGBA color) throws TooManyLightsException
	{
		if (currentLights == maxLights)
		{
			throw new TooManyLightsException("The number of lights added to robots exceed the defined maximum.");
		}
		else
		{
			SycamoreRobotLight light = lights.elementAt(currentLights);
			light.setColor(color);
			currentLights++;
		}
	}

	/**
	 * Performs a look. Stores the vector of observations of the other robots in the system inside
	 * the robots temporary storage, and changes the robot state properly.
	 */
	public synchronized void doLook()
	{
		if (getCurrentState() == ROBOT_STATE.READY_TO_LOOK)
		{
			setCurrentState(ROBOT_STATE.LOOKING);

			this.snapshot = engine.getObservations(this);
			this.systemMemory.addSelfPosition(getLocalPosition());
			this.systemMemory.addSnapshot(snapshot);

			setCurrentState(ROBOT_STATE.READY_TO_COMPUTE);
		}
	}

	/**
	 * Performs a compute. Uses the observations of the system obtained in last call of the
	 * <code>doLook()</code> method to compute a new destination point for the robot. The
	 * destination point is computed by the Algorithm object stored in this robot. After the
	 * destination has been computed, this method modifies the timeline and the state of the robot
	 * properly.
	 */
	public synchronized void doCompute()
	{
		if (algorithm != null && getCurrentState() == ROBOT_STATE.READY_TO_COMPUTE && !isFinished())
		{
			setCurrentState(ROBOT_STATE.COMPUTING);

			P destination = algorithm.compute(snapshot, this);

			// if destination is not null, add it to the timeline
			if (destination != null)
			{
				P lastPoint = timeline.getLastPoint();
				float distance = lastPoint.distanceTo(destination);

				if (Math.abs(distance) < SycamoreSystem.getEpsilon())
				{
					distance = 0;
				}

				if (distance > 0)
				{
					this.timeline.addKeyframe(destination, (distance / this.getSpeed()));

					int keyframes = this.timeline.getNumKeyframes();
					if (keyframes == 1 || keyframes == 2)
					{
						this.setCurrentRatio(0);
					}
					else
					{
						// for sure there are 3 or more keyframes,
						this.setCurrentRatio(this.timeline.getRatioOfKeyframe(keyframes - 2));
					}
				}
			}

			setCurrentState(ROBOT_STATE.READY_TO_MOVE);
		}
	}

	/**
	 * Performs a move. Uses the destination computed in last call of the <code>doCompute()</code>
	 * method, alteady stored into teh robot's timeline, and starts following the timeline in order
	 * to reach such destination.
	 */
	public synchronized void doMove()
	{
		if (this.currentState == ROBOT_STATE.READY_TO_MOVE)
		{
			this.snapshot.clear();
			this.snapshot = null;

			setCurrentState(ROBOT_STATE.MOVING);
		}
	}

	/**
	 * Returns true if the robot is moving, false otherwise.
	 * 
	 * @return
	 */
	public boolean isMoving()
	{
		return this.currentState == ROBOT_STATE.MOVING;
	}

	/**
	 * Performs the next operation, that is look, compute or move depending on current state
	 */
	public synchronized void nextOperation()
	{
		if (currentState == ROBOT_STATE.READY_TO_LOOK)
		{
			doLook();
		}
		else if (currentState == ROBOT_STATE.READY_TO_COMPUTE)
		{
			doCompute();
		}
		else
		{
			doMove();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		String description = "SycamoreRobot " + this.ID + "\n";
		String timeline = "Timeline: \n" + this.timeline.toString() + "\n";

		return description + timeline;
	}

	/**
	 * Reset the robot by bringing it to the initial configurations. The timeline is reset also.
	 */
	public synchronized void reset()
	{
		this.setCurrentRatio(0);

		if (algorithm != null)
		{
			algorithm.reset();
		}

		timeline.reset();

		timeline = new Timeline<P>();
		timeline.addKeyframe(startingPosition);
		setCurrentRatio(0);

		currentLights = 0;
		for (SycamoreRobotLight light : lights)
		{
			light.setColor(glassColor);
		}

		currentState = ROBOT_STATE.READY_TO_LOOK;
	}

	/**
	 * Updates the lights geometries by attaching or detaching them from JME scene if they are
	 * declared visible or not in the engine.
	 */
	public synchronized void updateLightsVisible()
	{
		SycamoreSystem.enqueueToJME(new Callable<Object>()
		{
			@Override
			public Object call() throws Exception
			{
				if (!SycamoreSystem.isRobotsLightsVisible())
				{
					for (SycamoreRobotLight light : lights)
					{
						Geometry lightGeom = light.getLightGeometry();
						if (robotNode.hasChild(lightGeom))
						{
							robotNode.detachChild(lightGeom);
						}
					}
				}
				else
				{
					for (SycamoreRobotLight light : lights)
					{
						Geometry lightGeom = light.getLightGeometry();
						if (!robotNode.hasChild(lightGeom))
						{
							robotNode.attachChild(lightGeom);
						}
					}
				}

				return null;
			}
		});
	}

	/**
	 * Updates the visibility rages geometries by attaching or detaching them from JME scene if they
	 * are declared visible or not in the engine.
	 */
	public synchronized void updateVisibilityRangesVisible()
	{
		SycamoreSystem.enqueueToJME(new Callable<Object>()
		{
			/*
			 * (non-Javadoc)
			 * 
			 * @see java.util.concurrent.Callable#call()
			 */
			@Override
			public Object call() throws Exception
			{
				if (engine != null && visibility != null)
				{
					if (SycamoreSystem.isVisibilityRangesVisible())
					{
						robotNode.attachChild(visibility.getVisibilityRangeGeometry());
					}
					else
					{
						robotNode.detachChild(visibility.getVisibilityRangeGeometry());
					}
				}
				return null;
			}
		});
	}

	/**
	 * Updates the movement directions geometries by attaching or detaching them from JME scene if
	 * they are declared visible or not in the engine.
	 */
	public synchronized void updateMovementDirectionsVisible()
	{
		SycamoreSystem.enqueueToJME(new Callable<Object>()
		{
			/*
			 * (non-Javadoc)
			 * 
			 * @see java.util.concurrent.Callable#call()
			 */
			@Override
			public Object call() throws Exception
			{
				if (engine != null)
				{
					if (SycamoreSystem.isMovementDirectionsVisible())
					{
						robotNode.attachChild(directionGeometry);
					}
					else
					{
						robotNode.detachChild(directionGeometry);
					}
				}
				return null;
			}
		});
	}

	/**
	 * Adds a pause of delta time unit in the timeline of this robot.
	 * 
	 * @param delta
	 */
	public synchronized void addPause(float delta)
	{
		this.timeline.addPause(delta);
	}

	/**
	 * Returns the whole timeline of this robot. Be careful with this method since a wrong
	 * modification of the timeline could lead the system in an unconsisten state.
	 * 
	 * @return
	 */
	public synchronized Timeline<P> getTimeline()
	{
		return timeline;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.diunipi.volpi.sycamore.model.SycamoreObservedRobot#getN()
	 */
	@Override
	public int getN() throws NNotKnownException
	{
		return SycamoreSystem.getN();
	}
}
