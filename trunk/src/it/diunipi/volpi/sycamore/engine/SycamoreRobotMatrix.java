/**
 * 
 */
package it.diunipi.volpi.sycamore.engine;

import java.util.Iterator;
import java.util.Vector;

/**
 * This class is a data structure to store robots. It can store both the normal robots and the human
 * pilot robots. The robots are divided into the two sets yet described, and they are then stored in
 * a matrix data structure. It is so possible to access them by asking for row and column, but the
 * default instrument to access robots is the iterator. It returns all the robots of a set in a
 * sequential way.
 * 
 * @author Valerio Volpi - vale.v@me.com
 */
public class SycamoreRobotMatrix<P extends SycamoreAbstractPoint & ComputablePoint<P>>
{
	/**
	 * The types of the matrix. It can contain just robots, just human pilots or both of them.
	 * 
	 * @author Valerio Volpi - vale.v@me.com
	 */
	private enum TYPE
	{
		ROBOT, HUMAN_PILOT, BOTH;
	}

	/**
	 * This class is an iterator over the robots in the data structure. It returns all the robots of
	 * a set in a sequential way. A set can contain just robots or just human pilot robots.
	 * 
	 * @author Valerio Volpi - vale.v@me.com
	 */
	private class SycamoreRobotIterator implements Iterator<SycamoreRobot<P>>
	{
		private int							returned	= 0;
		private Vector<SycamoreRobot<P>>	robots		= null;

		/**
		 * Default constructor.
		 */
		public SycamoreRobotIterator(SycamoreRobotMatrix<P> matrix, TYPE type)
		{
			// prepare the robots list
			this.robots = new Vector<SycamoreRobot<P>>();
			if (type == TYPE.ROBOT)
			{
				Vector<Vector<SycamoreRobot<P>>> list = matrix.getRobots();
				for (Vector<SycamoreRobot<P>> vector : list)
				{
					this.robots.addAll(vector);
				}
			}
			else if (type == TYPE.HUMAN_PILOT)
			{
				Vector<Vector<SycamoreRobot<P>>> list = matrix.getHumanPilotRobots();
				for (Vector<SycamoreRobot<P>> vector : list)
				{
					this.robots.addAll(vector);
				}
			}
			else if (type == TYPE.BOTH)
			{
				// if the tye is BOTH, add all the robots
				Vector<Vector<SycamoreRobot<P>>> list = matrix.getRobots();
				for (Vector<SycamoreRobot<P>> vector : list)
				{
					this.robots.addAll(vector);
				}

				list = matrix.getHumanPilotRobots();
				for (Vector<SycamoreRobot<P>> vector : list)
				{
					this.robots.addAll(vector);
				}
			}
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.util.Iterator#hasNext()
		 */
		@Override
		public boolean hasNext()
		{
			return returned < robots.size();
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.util.Iterator#next()
		 */
		@Override
		public SycamoreRobot<P> next()
		{
			if (hasNext())
			{
				SycamoreRobot<P> robot = robots.elementAt(returned);
				returned++;
				return robot;
			}
			else
				return null;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.util.Iterator#remove()
		 */
		@Override
		public void remove()
		{
			// Not supported
		}
	}

	private Vector<Vector<SycamoreRobot<P>>>	robots				= null;
	private Vector<Vector<SycamoreRobot<P>>>	humanPilotRobots	= null;

	/**
	 * Default constructor
	 */
	public SycamoreRobotMatrix()
	{
		this.robots = new Vector<Vector<SycamoreRobot<P>>>();
		this.humanPilotRobots = new Vector<Vector<SycamoreRobot<P>>>();
	}

	/**
	 * Returns the total number of rows in the matrices (normal robots + human pilot robots)
	 * 
	 * @return
	 */
	public synchronized int rowCount()
	{
		return robots.size() + humanPilotRobots.size();
	}

	/**
	 * Returns the number of rows of the set of normal robots
	 * 
	 * @return
	 */
	public synchronized int robotsRowCount()
	{
		return robots.size();
	}

	/**
	 * Returns the number of rows of the set of human pilot robots
	 * 
	 * @return
	 */
	public synchronized int humanPilotRowCount()
	{
		return humanPilotRobots.size();
	}

	/**
	 * Returns the total number of robots in the matrices (normal robots + human pilot robots)
	 * 
	 * @return
	 */
	public synchronized int size()
	{
		return robotsCount() + humanPilotsCount();
	}

	/**
	 * Returns the total number of normal robots in the matrix
	 * 
	 * @return
	 */
	public synchronized int robotsCount()
	{
		int size = 0;
		for (Vector<SycamoreRobot<P>> robotsList : robots)
		{
			size += robotsList.size();
		}

		return size;
	}

	/**
	 * Returns the total number of human pilots robots in the matrix
	 * 
	 * @return
	 */
	public synchronized int humanPilotsCount()
	{
		int size = 0;
		for (Vector<SycamoreRobot<P>> robotsList : humanPilotRobots)
		{
			size += robotsList.size();
		}

		return size;
	}

	/**
	 * Given a robot, returns true if either the set of normal robots or the set of human pilot
	 * robots contains that robot.
	 * 
	 * @param robot
	 * @return
	 */
	public synchronized boolean contains(SycamoreRobot<P> robot)
	{
		// look in robots
		for (Vector<SycamoreRobot<P>> robotsList : robots)
		{
			if (robotsList.contains(robot))
			{
				return true;
			}
		}

		// look in human pilots
		for (Vector<SycamoreRobot<P>> robotsList : humanPilotRobots)
		{
			if (robotsList.contains(robot))
			{
				return true;
			}
		}

		return false;
	}

	/**
	 * Returns true if the 2 robot matrices are empty
	 * 
	 * @return
	 */
	public synchronized boolean isEmpty()
	{
		return this.size() == 0;
	}

	/**
	 * Given a robot, adds it in requested row of the set of normal robots
	 * 
	 * @param row
	 * @param column
	 */
	public synchronized void addRobot(int row, SycamoreRobot<P> robot)
	{
		if (row >= rowCount())
		{
			for (int i = rowCount() - 1; i < row; i++)
			{
				addRobotRow();
			}
		}

		robots.elementAt(row).add(robot);
	}

	/**
	 * Given a robot, adds it in requested row of the set of human pilot robots
	 * 
	 * @param row
	 * @param column
	 */
	public synchronized void addHumanPilot(int row, SycamoreRobot<P> robot)
	{
		if (row >= rowCount())
		{
			for (int i = rowCount() - 1; i < row; i++)
			{
				addHumanPilotRow();
			}
		}

		humanPilotRobots.elementAt(row).add(robot);
	}

	/**
	 * Returns the robot located at the requested row and column in the set of normal robots.
	 * Returns null if such robot does not exist.
	 * 
	 * @param row
	 * @param column
	 */
	public synchronized SycamoreRobot<P> getRobot(int row, int column)
	{
		if (row < 0 || row >= rowCount())
		{
			return null;
		}
		else
		{
			Vector<SycamoreRobot<P>> robotsList = robots.elementAt(row);
			if (column < 0 || column >= robotsList.size())
			{
				return null;
			}
			else
			{
				return robotsList.elementAt(column);
			}
		}
	}

	/**
	 * Returns the robot located at the requested row and column in the set of human pilot robots.
	 * Returns null if such robot does not exist.
	 * 
	 * @param row
	 * @param column
	 */
	public synchronized SycamoreRobot<P> getHumanPilot(int row, int column)
	{
		if (row < 0 || row >= rowCount())
		{
			return null;
		}
		else
		{
			Vector<SycamoreRobot<P>> robotsList = humanPilotRobots.elementAt(row);
			if (column < 0 || column >= robotsList.size())
			{
				return null;
			}
			else
			{
				return robotsList.elementAt(column);
			}
		}
	}

	/**
	 * Remove passed robot from the set of normal robots of this data structure. If such robot is
	 * not in the system, it does not perform any operation.
	 * 
	 * @param robot
	 */
	public synchronized void removeRobot(SycamoreRobot<P> robot)
	{
		if (robot != null)
		{
			for (Vector<SycamoreRobot<P>> robotsList : robots)
			{
				if (robotsList.contains(robot))
				{
					robotsList.remove(robot);
					return;
				}
			}
		}
	}

	/**
	 * Remove passed robot from the set of human pilot robots of this data structure. If such robot
	 * is not in the system, it does not perform any operation.
	 * 
	 * @param robot
	 */
	public synchronized void removeHumanPilot(SycamoreRobot<P> robot)
	{
		if (robot != null)
		{
			for (Vector<SycamoreRobot<P>> robotsList : humanPilotRobots)
			{
				if (robotsList.contains(robot))
				{
					robotsList.remove(robot);
					return;
				}
			}
		}
	}

	/**
	 * Remove the normal robot located at passed row and column from the set of normal robots of
	 * this data structure. The removed robot is returned. If the system does not contain requested
	 * robot, this method does not perform any operation and returns null.
	 * 
	 * @param row
	 * @param column
	 * @return
	 */
	public synchronized SycamoreRobot<P> removeRobot(int row, int column)
	{
		SycamoreRobot<P> robot = this.getRobot(row, column);
		this.removeRobot(robot);
		return robot;
	}

	/**
	 * Remove the human pilot located at passed row and column from the set of human pilot robots of
	 * this data structure. The removed robot is returned. If the system does not contain requested
	 * robot, this method does not perform any operation and returns null.
	 * 
	 * @param row
	 * @param column
	 * @return
	 */
	public synchronized SycamoreRobot<P> removeHumanPilot(int row, int column)
	{
		SycamoreRobot<P> robot = this.getHumanPilot(row, column);
		this.removeHumanPilot(robot);
		return robot;
	}

	/**
	 * Adds a new row in the matrix of normal robots.
	 */
	public synchronized void addRobotRow()
	{
		this.robots.add(new Vector<SycamoreRobot<P>>());
	}

	/**
	 * Adds a new row in the matrix of human pilot robots.
	 */
	public synchronized void addHumanPilotRow()
	{
		this.humanPilotRobots.add(new Vector<SycamoreRobot<P>>());
	}

	/**
	 * Returns the row of the matrix of normal robot identified by passed index.
	 * 
	 * @param index
	 * @return
	 */
	public synchronized Vector<SycamoreRobot<P>> getRobotRow(int index)
	{
		if (index < 0 || index >= this.robots.size())
		{
			return null;
		}
		else
		{
			return this.robots.elementAt(index);
		}
	}

	/**
	 * Returns the row of the matrix of human pilot robot identified by passed index.
	 * 
	 * @param index
	 * @return
	 */
	public synchronized Vector<SycamoreRobot<P>> getHumanPilotRow(int index)
	{
		if (index < 0 || index >= this.humanPilotRobots.size())
		{
			return null;
		}
		else
		{
			return this.humanPilotRobots.elementAt(index);
		}
	}

	/**
	 * Removes the row of the matrix of normal robot identified by passed index.
	 * 
	 * @param index
	 * @return
	 */
	public synchronized void removeRobotRow(int index)
	{
		if (index > 0 && index < this.robots.size())
		{
			this.robots.remove(index);
		}
	}

	/**
	 * Removes the row of the matrix of human pilot robot identified by passed index.
	 * 
	 * @param index
	 * @return
	 */
	public synchronized void removeHumanPilotRow(int index)
	{
		if (index > 0 && index < this.humanPilotRobots.size())
		{
			this.humanPilotRobots.remove(index);
		}
	}

	/**
	 * Clears this data structure, by removing all the robots contained in the two sets.
	 */
	public synchronized void clear()
	{
		this.robots.clear();
		this.humanPilotRobots.clear();
	}

	/**
	 * Returns an iterator over all the robots in this data structure, both normal robots and human
	 * pilot robots.
	 * 
	 * @return
	 */
	public synchronized Iterator<SycamoreRobot<P>> iterator()
	{
		return new SycamoreRobotIterator(this, TYPE.BOTH);
	}

	/**
	 * Returns an iterator over all the normal robots in this data structure.
	 * 
	 * @return
	 */
	public synchronized Iterator<SycamoreRobot<P>> robotsIterator()
	{
		return new SycamoreRobotIterator(this, TYPE.ROBOT);
	}

	/**
	 * Returns an iterator over all the human pilot robots in this data structure.
	 * 
	 * @return
	 */
	public synchronized Iterator<SycamoreRobot<P>> humanPilotsIterator()
	{
		return new SycamoreRobotIterator(this, TYPE.HUMAN_PILOT);
	}

	/**
	 * Returns a vector containing all the robots in this data structure, both normal robots and
	 * human pilot robots. Normal robots comes before human pilot robots. This vector does not
	 * contain any information about the disposition of the robots (rows and cols) in this data
	 * structure.
	 * 
	 * @return
	 */
	public synchronized Vector<SycamoreRobot<P>> toVector()
	{
		Vector<SycamoreRobot<P>> ret = new Vector<SycamoreRobot<P>>();

		for (Vector<SycamoreRobot<P>> robotsList : this.robots)
		{
			ret.addAll(robotsList);
		}

		for (Vector<SycamoreRobot<P>> robotsList : this.humanPilotRobots)
		{
			ret.addAll(robotsList);
		}

		return ret;
	}

	/**
	 * Returns a vector containing all the normal robots in this data structure. This vector does
	 * not contain any information about the disposition of the robots (rows and cols) in this data
	 * structure.
	 * 
	 * @return
	 */
	public Vector<SycamoreRobot<P>> toRobotsVector()
	{
		Vector<SycamoreRobot<P>> ret = new Vector<SycamoreRobot<P>>();

		for (Vector<SycamoreRobot<P>> robotsList : this.robots)
		{
			ret.addAll(robotsList);
		}

		return ret;
	}

	/**
	 * Returns a vector containing all the human pilot robots in this data structure. This vector does
	 * not contain any information about the disposition of the robots (rows and cols) in this data
	 * structure.
	 * 
	 * @return
	 */
	public Vector<SycamoreRobot<P>> toHumanPilotsVector()
	{
		Vector<SycamoreRobot<P>> ret = new Vector<SycamoreRobot<P>>();

		for (Vector<SycamoreRobot<P>> robotsList : this.humanPilotRobots)
		{
			ret.addAll(robotsList);
		}

		return ret;
	}

	/**
	 * @return the robots
	 */
	private synchronized Vector<Vector<SycamoreRobot<P>>> getRobots()
	{
		return robots;
	}

	/**
	 * @return the humanPilotRobots
	 */
	private synchronized Vector<Vector<SycamoreRobot<P>>> getHumanPilotRobots()
	{
		return humanPilotRobots;
	}
}
