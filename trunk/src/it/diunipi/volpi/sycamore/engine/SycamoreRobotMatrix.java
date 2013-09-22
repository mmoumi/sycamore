/**
 * 
 */
package it.diunipi.volpi.sycamore.engine;

import it.diunipi.volpi.sycamore.engine.SycamoreEngine.TYPE;

import java.util.Iterator;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

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
	private enum ROBOT_KIND
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
		public SycamoreRobotIterator(SycamoreRobotMatrix<P> matrix, ROBOT_KIND type)
		{
			// prepare the robots list
			this.robots = new Vector<SycamoreRobot<P>>();
			if (type == ROBOT_KIND.ROBOT)
			{
				Vector<Vector<SycamoreRobot<P>>> list = matrix.getRobots();
				for (Vector<SycamoreRobot<P>> vector : list)
				{
					this.robots.addAll(vector);
				}
			}
			else if (type == ROBOT_KIND.HUMAN_PILOT)
			{
				Vector<Vector<SycamoreRobot<P>>> list = matrix.getHumanPilotRobots();
				for (Vector<SycamoreRobot<P>> vector : list)
				{
					this.robots.addAll(vector);
				}
			}
			else if (type == ROBOT_KIND.BOTH)
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
		return new SycamoreRobotIterator(this, ROBOT_KIND.BOTH);
	}

	/**
	 * Returns an iterator over all the normal robots in this data structure.
	 * 
	 * @return
	 */
	public synchronized Iterator<SycamoreRobot<P>> robotsIterator()
	{
		return new SycamoreRobotIterator(this, ROBOT_KIND.ROBOT);
	}

	/**
	 * Returns an iterator over all the human pilot robots in this data structure.
	 * 
	 * @return
	 */
	public synchronized Iterator<SycamoreRobot<P>> humanPilotsIterator()
	{
		return new SycamoreRobotIterator(this, ROBOT_KIND.HUMAN_PILOT);
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
	 * Returns a vector containing all the human pilot robots in this data structure. This vector
	 * does not contain any information about the disposition of the robots (rows and cols) in this
	 * data structure.
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

	/**
	 * Encode this object to XML format. The encoded Element will contain all data necessary to
	 * re-create and object that is equal to this one.
	 * 
	 * @return an XML Element containing the XML description of this object.
	 */
	public synchronized Element encode(DocumentBuilderFactory factory, DocumentBuilder builder, Document document)
	{
		// create element
		Element element = document.createElement("SycamoreRobotMatrix");

		// children
		Element robotsElem = document.createElement("robots");
		for (int i = 0; i < robots.size(); i++)
		{
			Vector<SycamoreRobot<P>> robotsList = robots.elementAt(i);
			Element robotsListElem = document.createElement("robotsList");

			for (int j = 0; j < robotsList.size(); j++)
			{
				Element robotElem = document.createElement("robot");
				SycamoreRobot<P> robot = robotsList.elementAt(j);
				robotElem.appendChild(robot.encode(factory, builder, document));
				robotsListElem.appendChild(robotElem);
			}

			robotsElem.appendChild(robotsListElem);
		}

		Element humanPilotRobotsElem = document.createElement("humanPilotRobots");
		for (int i = 0; i < humanPilotRobots.size(); i++)
		{
			Vector<SycamoreRobot<P>> humanPilotRobotsList = humanPilotRobots.elementAt(i);
			Element humanPilotRobotsListElem = document.createElement("humanPilotRobotsList");

			for (int j = 0; j < humanPilotRobotsList.size(); j++)
			{
				Element robotElem = document.createElement("humanPilotRobot");
				SycamoreRobot<P> humanPilotrobot = humanPilotRobotsList.elementAt(j);
				robotElem.appendChild(humanPilotrobot.encode(factory, builder, document));
				humanPilotRobotsListElem.appendChild(robotElem);
			}

			humanPilotRobotsElem.appendChild(humanPilotRobotsListElem);
		}

		// append children
		element.appendChild(robotsElem);
		element.appendChild(humanPilotRobotsElem);

		return element;
	}

	/**
	 * @param element
	 */
	public synchronized boolean decode(Element element, TYPE type, SycamoreEngine<P> engine)
	{
		boolean success = true;
		NodeList nodes = element.getElementsByTagName("SycamoreRobotMatrix");

		// if there is at least a SycamoreRobotMatrix node, decode it
		if (nodes.getLength() > 0)
		{
			// robots
			NodeList robots = element.getElementsByTagName("robots");
			if (robots.getLength() > 0)
			{
				this.robots = new Vector<Vector<SycamoreRobot<P>>>();
				Element robotsElem = (Element) robots.item(0);

				// robots list
				NodeList robotsList = robotsElem.getElementsByTagName("robotsList");
				for (int i = 0; i < robotsList.getLength(); i++)
				{
					Vector<SycamoreRobot<P>> list = new Vector<SycamoreRobot<P>>();
					this.robots.add(list);

					Element robotListElem = (Element) robotsList.item(i);

					// robot
					NodeList robot = robotListElem.getElementsByTagName("robot");
					for (int j = 0; j < robot.getLength(); j++)
					{
						Element robotElem = (Element) robot.item(j);
						SycamoreRobot<P> newRobot = null;

						if (type == TYPE.TYPE_2D)
						{
							SycamoreEngine2D engine2D = (SycamoreEngine2D) engine;
							newRobot = (SycamoreRobot<P>) new SycamoreRobot2D(engine2D);
						}
						else
						{
							SycamoreEngine3D engine3D = (SycamoreEngine3D) engine;
							newRobot = (SycamoreRobot<P>) new SycamoreRobot3D(engine3D);
						}

						if (newRobot.decode(robotElem, type))
						{
							list.add(newRobot);
						}
					}
				}

				// humanPilotRobots
				NodeList humanPilotRobots = element.getElementsByTagName("humanPilotRobots");
				if (humanPilotRobots.getLength() > 0)
				{
					this.humanPilotRobots = new Vector<Vector<SycamoreRobot<P>>>();
					Element humanPilotRobotsElem = (Element) humanPilotRobots.item(0);

					// humanPilotRobots list
					NodeList humanPilotRobotsList = humanPilotRobotsElem.getElementsByTagName("humanPilotRobotsList");
					for (int i = 0; i < humanPilotRobotsList.getLength(); i++)
					{
						Vector<SycamoreRobot<P>> list = new Vector<SycamoreRobot<P>>();
						this.humanPilotRobots.add(list);

						Element humanPilotRobotsListElem = (Element) humanPilotRobotsList.item(i);

						// humanPilotRobot
						NodeList humanPilotRobot = humanPilotRobotsListElem.getElementsByTagName("humanPilotRobot");
						for (int j = 0; j < humanPilotRobot.getLength(); j++)
						{
							Element humanPilotRobotElem = (Element) humanPilotRobot.item(j);
							SycamoreRobot<P> newhumanPilotRobot = null;

							if (type == TYPE.TYPE_2D)
							{
								SycamoreEngine2D engine2D = (SycamoreEngine2D) engine;
								newhumanPilotRobot = (SycamoreRobot<P>) new SycamoreRobot2D(engine2D);
							}
							else
							{
								SycamoreEngine3D engine3D = (SycamoreEngine3D) engine;
								newhumanPilotRobot = (SycamoreRobot<P>) new SycamoreRobot3D(engine3D);
							}

							if (newhumanPilotRobot.decode(humanPilotRobotElem, type))
							{
								list.add(newhumanPilotRobot);
							}
						}
					}

				}
			}
		}
		
		return success;
	}
}
