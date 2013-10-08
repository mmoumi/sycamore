/**
 * 
 */
package it.diunipi.volpi.sycamore.engine;

import it.diunipi.volpi.sycamore.plugins.agreements.Agreement;
import it.diunipi.volpi.sycamore.plugins.agreements.AgreementImpl;
import it.diunipi.volpi.sycamore.plugins.algorithms.Algorithm;
import it.diunipi.volpi.sycamore.plugins.algorithms.AlgorithmImpl;
import it.diunipi.volpi.sycamore.plugins.initialconditions.InitialConditions;
import it.diunipi.volpi.sycamore.plugins.memory.Memory;
import it.diunipi.volpi.sycamore.plugins.memory.MemoryImpl;
import it.diunipi.volpi.sycamore.plugins.visibilities.Visibility;
import it.diunipi.volpi.sycamore.plugins.visibilities.VisibilityImpl;
import it.diunipi.volpi.sycamore.util.ApplicationProperties;
import it.diunipi.volpi.sycamore.util.PropertyManager;
import it.diunipi.volpi.sycamore.util.SycamoreUtil;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Vector;

import com.jme3.math.ColorRGBA;

/**
 * @see SycamoreEngine
 * 
 *      The 2D version of the Sycamore engine. It is a concrete implementation of the engine that
 *      fixes its type to be 2 dimensional. It uses a TYPE_2D enum element to identify the type, and
 *      a Point2D object to satisfy the generic.
 * 
 * @author Valerio Volpi - vale.v@me.com
 */
public class SycamoreEngine2D extends SycamoreEngine<Point2D>
{
	/*
	 * (non-Javadoc)
	 * 
	 * @see it.diunipi.volpi.sycamore.engine.SycamoreEngine#createAndAddNewRobotInstance(int,
	 * java.lang.Class)
	 */
	@Override
	public SycamoreRobot<Point2D> createAndAddNewRobotInstance(boolean isHumanPilot, int index, ColorRGBA color, int maxLights, float speed)
	{
		{
			// create a new oblivious robot
			SycamoreRobot2D robot = new SycamoreRobot2D(this, computeStartingPoint(), speed, color, maxLights);

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
			Visibility visibility = this.getCurrentVisibility();
			if (visibility != null && visibility.getType() == TYPE.TYPE_2D)
			{
				try
				{
					createAndSetNewVisibilityInstance(visibility, robot);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}

			// check if some memory plugins have to be added
			Memory memory = this.getCurrentMemory();
			if (memory != null && memory.getType() == TYPE.TYPE_2D)
			{
				try
				{
					createAndSetNewMemoryInstance(memory, robot);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}

			// check if some agreement plugins have to be added
			Agreement agreement = this.getCurrentAgreement();
			if (agreement != null && agreement.getType() == TYPE.TYPE_2D)
			{
				try
				{
					createAndSetNewAgreementInstance(agreement, robot);
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
	public void createAndSetNewAlgorithmInstance(Algorithm<Point2D> algorithm, int index) throws IllegalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException
	{
		boolean isHumanPilot = algorithm.isHumanPilot();
		Vector<SycamoreRobot<Point2D>> robotList = isHumanPilot ? robots.getHumanPilotRow(index) : this.robots.getRobotRow(index);

		for (SycamoreRobot<Point2D> robot : robotList)
		{
			this.createAndSetNewAlgorithmInstance(algorithm, robot);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * it.diunipi.volpi.sycamore.engine.SycamoreEngine#createAndSetNewAlgorithmInstance(it.diunipi
	 * .volpi.sycamore.plugins.algorithms.Algorithm, it.diunipi.volpi.sycamore.engine.SycamoreRobot)
	 */
	@Override
	public void createAndSetNewAlgorithmInstance(Algorithm<Point2D> algorithm, SycamoreRobot<Point2D> robot) throws IllegalArgumentException, InstantiationException, IllegalAccessException,
			InvocationTargetException
	{
		// create a new instance of the algorithm
		Class<? extends Algorithm> algorithmClass = algorithm.getClass();
		Constructor<?> constructor = algorithmClass.getConstructors()[0];

		AlgorithmImpl<Point2D> newInstance = (AlgorithmImpl<Point2D>) constructor.newInstance();
		newInstance.init();
		robot.setAlgorithm(newInstance);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * it.diunipi.volpi.sycamore.engine.SycamoreEngine#createAndSetNewVisibilityInstance(it.diunipi
	 * .volpi.sycamore.plugins.visibilities.Visibility,
	 * it.diunipi.volpi.sycamore.engine.SycamoreRobot)
	 */
	@Override
	public void createAndSetNewVisibilityInstance(Visibility<Point2D> visibility, SycamoreRobot<Point2D> robot) throws IllegalArgumentException, InstantiationException, IllegalAccessException,
			InvocationTargetException
	{
		Constructor<?> constructor = null;
		if (visibility != null)
		{
			// create a new instance of the visibility
			Class<? extends Visibility> visibilityClass = visibility.getClass();
			constructor = visibilityClass.getConstructors()[0];

			if (constructor != null)
			{
				// assign visibilty to each robot
				VisibilityImpl<Point2D> newInstance = (VisibilityImpl<Point2D>) constructor.newInstance();
				
				newInstance.setRobot(robot);
				robot.setVisibility(newInstance);
				return;
			}
		}		

		// If no Visibility is set, set null
		robot.setVisibility(null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * it.diunipi.volpi.sycamore.engine.SycamoreEngine#createAndSetNewMemoryInstance(it.diunipi.
	 * volpi.sycamore.plugins.memory.Memory, it.diunipi.volpi.sycamore.engine.SycamoreRobot)
	 */
	@Override
	public void createAndSetNewMemoryInstance(Memory<Point2D> memory, SycamoreRobot<Point2D> robot) throws IllegalArgumentException, InstantiationException, IllegalAccessException,
			InvocationTargetException
	{
		Constructor<?> constructor = null;
		if (memory != null)
		{
			// create a new instance of the memory
			Class<? extends Memory> memoryClass = memory.getClass();
			constructor = memoryClass.getConstructors()[0];

			if (constructor != null)
			{
				// assign memory to each robot
				MemoryImpl<Point2D> newInstance = (MemoryImpl<Point2D>) constructor.newInstance();
				robot.setMemory(newInstance);
				return;
			}
		}
		
		// If no Memory is set, set null
		robot.setMemory(null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * it.diunipi.volpi.sycamore.engine.SycamoreEngine#creatAndSetNewInitialConditionsInstance(it
	 * .diunipi.volpi.sycamore.plugins.Visibility)
	 */
	@Override
	public void createAndSetNewInitialConditionsInstance(InitialConditions<Point2D> initialConditions) throws IllegalArgumentException, InstantiationException, IllegalAccessException,
			InvocationTargetException
	{
		Constructor<?> constructor = null;
		if (initialConditions != null)
		{
			// create a new instance of the InitialConditions
			Class<? extends InitialConditions> initialConditionsClass = initialConditions.getClass();
			constructor = initialConditionsClass.getConstructors()[0];

			if (constructor != null)
			{
				InitialConditions<Point2D> newInstance = (InitialConditions<Point2D>) constructor.newInstance();
				this.initialConditions = newInstance;
				return;
			}
		}

		// if no InitialConditions has been set, set null
		this.initialConditions = null;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * it.diunipi.volpi.sycamore.engine.SycamoreEngine#createAndSetNewAgreementInstance(it.diunipi
	 * .volpi.sycamore.plugins.agreements.Agreement, it.diunipi.volpi.sycamore.engine.SycamoreRobot)
	 */
	@Override
	public void createAndSetNewAgreementInstance(Agreement<Point2D> agreement, SycamoreRobot<Point2D> robot) throws IllegalArgumentException, InstantiationException, IllegalAccessException,
			InvocationTargetException
	{
		Constructor<?> constructor = null;
		if (agreement != null)
		{
			// create a new instance of the agreement
			Class<? extends Agreement> agreementClass = agreement.getClass();
			constructor = agreementClass.getConstructors()[0];

			if (constructor != null)
			{
				// assign memory to each robot
				AgreementImpl<Point2D> newInstance = (AgreementImpl<Point2D>) constructor.newInstance();
				robot.setAgreement(newInstance);
				return;
			}
		}

		// if no Agreement has been set, set null
		robot.setAgreement(null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.diunipi.volpi.sycamore.engine.SycamoreEngine#getTypeClass()
	 */
	@Override
	public TYPE getType()
	{
		return TYPE.TYPE_2D;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.diunipi.volpi.sycamore.engine.SycamoreEngine#computeStartingPoint()
	 */
	@Override
	protected Point2D computeStartingPoint()
	{
		if (initialConditions == null)
		{
			int minX = PropertyManager.getSharedInstance().getIntegerProperty(ApplicationProperties.INITIAL_POSITION_MIN_X);
			int maxX = PropertyManager.getSharedInstance().getIntegerProperty(ApplicationProperties.INITIAL_POSITION_MAX_X);
			int minY = PropertyManager.getSharedInstance().getIntegerProperty(ApplicationProperties.INITIAL_POSITION_MIN_Y);
			int maxY = PropertyManager.getSharedInstance().getIntegerProperty(ApplicationProperties.INITIAL_POSITION_MAX_Y);

			return SycamoreUtil.getRandomPoint2D(minX, maxX, minY, maxY);
		}
		else
		{
			return initialConditions.nextStartingPoint(this.robots);
		}
	}
}
