/**
 * 
 */
package it.diunipi.volpi.sycamore.engine;

import it.diunipi.volpi.sycamore.gui.SycamoreSystem;
import it.diunipi.volpi.sycamore.plugins.agreements.Agreement;
import it.diunipi.volpi.sycamore.plugins.agreements.AgreementImpl;
import it.diunipi.volpi.sycamore.plugins.algorithms.Algorithm;
import it.diunipi.volpi.sycamore.plugins.algorithms.AlgorithmImpl;
import it.diunipi.volpi.sycamore.plugins.initialconditions.InitialConditions;
import it.diunipi.volpi.sycamore.plugins.memory.Memory;
import it.diunipi.volpi.sycamore.plugins.memory.MemoryImpl;
import it.diunipi.volpi.sycamore.plugins.visibilities.Visibility;
import it.diunipi.volpi.sycamore.plugins.visibilities.VisibilityImpl;
import it.diunipi.volpi.sycamore.util.SycamoreUtil;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import java.util.Vector;

import com.jme3.math.ColorRGBA;

/**
 * @see SycamoreEngine
 * 
 *      The 3D version of the Sycamore engine. It is a concrete implementation of the engine that
 *      fixes its type to be 3 dimensional. It uses a TYPE_3D enum element to identify the type, and
 *      a Point3D object to satisfy the generic.
 * 
 * @author Valerio Volpi - vale.v@me.com
 */
public class SycamoreEngine3D extends SycamoreEngine<Point3D>
{
	/**
	 * Default constructor.
	 */
	public SycamoreEngine3D()
	{
		super();

		// initial conditions
		InitialConditions initialCondition = SycamoreSystem.getInitialCondition();
		if (initialCondition != null && initialCondition.getType() == TYPE.TYPE_3D)
		{
			try
			{
				createAndSetNewInitialConditionsInstance(initialCondition);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.diunipi.volpi.sycamore.engine.SycamoreEngine#createAndAddNewRobotInstance(int,
	 * java.lang.Class)
	 */
	@Override
	public SycamoreRobot<Point3D> createAndAddNewRobotInstance(boolean isHumanPilot, int index, ColorRGBA color, int maxLights)
	{
		{
			float speed = SycamoreSystem.DEFAULT_ROBOT_SPEED;
			if (isHumanPilot)
			{
				speed = speed / 2.0f;
			}
			
			// create a new oblivious robot
			SycamoreRobot3D robot = new SycamoreRobot3D(this, computeStartingPoint(), speed, color, maxLights);

			if (isHumanPilot)
			{
				this.robots.addHumanPilot(index, robot);
			}
			else
			{
				// add in vector
				this.robots.addRobot(index, robot);
			}

			// check if some visibility plugins have to be added
			Visibility visibility = SycamoreSystem.getVisibility();
			if (visibility != null && visibility.getType() == TYPE.TYPE_3D)
			{
				try
				{
					createAndSetNewVisibilityInstance(visibility);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}

			// check if some memory plugins have to be added
			Memory memory = SycamoreSystem.getMemory();
			if (memory != null && memory.getType() == TYPE.TYPE_3D)
			{
				try
				{
					createAndSetNewMemoryInstance(memory);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}

			return robot;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * it.diunipi.volpi.sycamore.engine.SycamoreEngine#createAndSetNewAlgorithmInstance(it.diunipi
	 * .volpi.sycamore.plugins.algorithms.Algorithm, int)
	 */
	@Override
	public void createAndSetNewAlgorithmInstance(Algorithm<Point3D> algorithm, int index) throws IllegalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException
	{
		// create a new instance of the scheduler
		Class<? extends Algorithm> algorithmClass = algorithm.getClass();
		Constructor<?> constructor = algorithmClass.getConstructors()[0];

		boolean isHumanPilot = algorithm.isHumanPilot();
		Vector<SycamoreRobot<Point3D>> robotList = isHumanPilot ? robots.getHumanPilotRow(index) : robots.getRobotRow(index);

		for (SycamoreRobot<Point3D> robot : robotList)
		{
			AlgorithmImpl<Point3D> newInstance = (AlgorithmImpl<Point3D>) constructor.newInstance();
			robot.setAlgorithm(newInstance);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * it.diunipi.volpi.sycamore.engine.SycamoreEngine#creatAndSetNewVisibilityInstance(it.diunipi
	 * .volpi.sycamore.plugins.Visibility)
	 */
	@Override
	public void createAndSetNewVisibilityInstance(Visibility<Point3D> visibility) throws IllegalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException
	{
		Constructor<?> constructor = null;
		if (visibility != null)
		{
			// create a new instance of the scheduler
			Class<? extends Visibility> visibilityClass = visibility.getClass();
			constructor = visibilityClass.getConstructors()[0];
		}

		// set visibility range in robots
		Iterator<SycamoreRobot<Point3D>> iterator = this.robots.iterator();
		while (iterator.hasNext())
		{
			SycamoreRobot<Point3D> robot = iterator.next();
			if (constructor != null)
			{
				// assign visibilty to each robot
				VisibilityImpl<Point3D> newInstance = (VisibilityImpl<Point3D>) constructor.newInstance();
				robot.setVisibility(newInstance);
			}
			else
			{
				robot.setVisibility(null);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * it.diunipi.volpi.sycamore.engine.SycamoreEngine#createAndSetNewMemoryInstance(it.diunipi.
	 * volpi.sycamore.plugins.memory.Memory)
	 */
	@Override
	public void createAndSetNewMemoryInstance(Memory<Point3D> memory) throws IllegalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException
	{
		Constructor<?> constructor = null;
		if (memory != null)
		{
			// create a new instance of the scheduler
			Class<? extends Memory> memoryClass = memory.getClass();
			constructor = memoryClass.getConstructors()[0];
		}

		// set visibility range in robots
		Iterator<SycamoreRobot<Point3D>> iterator = this.robots.iterator();
		while (iterator.hasNext())
		{
			SycamoreRobot<Point3D> robot = iterator.next();
			if (constructor != null)
			{
				// assign memory to each robot
				MemoryImpl<Point3D> newInstance = (MemoryImpl<Point3D>) constructor.newInstance();
				robot.setMemory(newInstance);
			}
			else
			{
				robot.setMemory(null);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * it.diunipi.volpi.sycamore.engine.SycamoreEngine#creatAndSetNewInitialConditionsInstance(it
	 * .diunipi.volpi.sycamore.plugins.Visibility)
	 */
	@Override
	public void createAndSetNewInitialConditionsInstance(InitialConditions<Point3D> initialConditions) throws IllegalArgumentException, InstantiationException, IllegalAccessException,
			InvocationTargetException
	{
		Constructor<?> constructor = null;
		if (initialConditions != null)
		{
			// create a new instance of the scheduler
			Class<? extends InitialConditions> initialConditionsClass = initialConditions.getClass();
			constructor = initialConditionsClass.getConstructors()[0];

			InitialConditions<Point3D> newInstance = (InitialConditions<Point3D>) constructor.newInstance();
			this.initialConditions = newInstance;
		}
		else
		{
			this.initialConditions = null;
		}
	}
	
	/* (non-Javadoc)
	 * @see it.diunipi.volpi.sycamore.engine.SycamoreEngine#createAndSetNewAgreementInstance(it.diunipi.volpi.sycamore.plugins.agreements.Agreement)
	 */
	@Override
	public void createAndSetNewAgreementInstance(Agreement<Point3D> agreement) throws IllegalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException
	{
		Constructor<?> constructor = null;
		if (agreement != null)
		{
			// create a new instance of the agreement
			Class<? extends Agreement> agreementClass = agreement.getClass();
			constructor = agreementClass.getConstructors()[0];
		}

		// set visibility range in robots
		Iterator<SycamoreRobot<Point3D>> iterator = this.robots.iterator();
		while (iterator.hasNext())
		{
			SycamoreRobot<Point3D> robot = iterator.next();
			if (constructor != null)
			{
				// assign memory to each robot
				AgreementImpl<Point3D> newInstance = (AgreementImpl<Point3D>) constructor.newInstance();
				robot.setAgreement(newInstance);
			}
			else
			{
				robot.setAgreement(null);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.diunipi.volpi.sycamore.engine.SycamoreEngine#getTypeClass()
	 */
	@Override
	public TYPE getType()
	{
		return TYPE.TYPE_3D;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.diunipi.volpi.sycamore.engine.SycamoreEngine#computeStartingPoint()
	 */
	@Override
	protected Point3D computeStartingPoint()
	{
		if (initialConditions == null)
		{
			return SycamoreUtil.getRandomPoint3D(-5, 5, -5, 5, -5, 5);
		}
		else
		{
			return initialConditions.nextStartingPoint(this.robots);
		}
	}
}
