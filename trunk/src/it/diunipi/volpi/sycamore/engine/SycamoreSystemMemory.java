/**
 * 
 */
package it.diunipi.volpi.sycamore.engine;

import it.diunipi.volpi.sycamore.engine.SycamoreEngine.TYPE;
import it.diunipi.volpi.sycamore.util.SycamoreUtil;

import java.util.Vector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * This class represents the memory of the system. It is owned by a robot, and it contains, for that
 * robot, all the past positions and observations. It can be accessed only through an instance of a
 * Memory plugin. If no such plugin exists, this object is not filled with data.
 * 
 * @author Valerio Volpi - vale.v@me.com
 * 
 * @param <P>
 */
public class SycamoreSystemMemory<P extends SycamoreAbstractPoint & ComputablePoint<P>>
{
	private Vector<P>			selfPositions	= null;
	private Vector<Vector<P>>	snapshots		= null;

	/**
	 * Default constructor.
	 */
	public SycamoreSystemMemory()
	{
		this.selfPositions = new Vector<P>();
		this.snapshots = new Vector<Vector<P>>();
	}

	/**
	 * @return the selfPositions
	 */
	public Vector<P> getSelfPositions()
	{
		return selfPositions;
	}

	/**
	 * @return the selfSnapshots
	 */
	public Vector<Vector<P>> getSnapshots()
	{
		return snapshots;
	}

	/**
	 * Clones passed point by returning an exact copy of it, but on a different instance.
	 * 
	 * @param point
	 * @return
	 */
	private P clonePoint(P point)
	{
		try
		{
			// clone
			return (P) point.clone();
		}
		catch (CloneNotSupportedException e)
		{
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Adds passed position to the positions vector
	 * 
	 * @param position
	 */
	public void addSelfPosition(P position)
	{
		this.selfPositions.add(this.clonePoint(position));
	}

	/**
	 * Adds passed snapshot to the snapshots vector
	 * 
	 * @param position
	 */
	public void addSnapshot(Vector<Observation<P>> snapshot)
	{
		Vector<P> newSnapshot = new Vector<P>();

		for (Observation<P> observation : snapshot)
		{
			newSnapshot.add(this.clonePoint(observation.getRobotPosition()));
		}

		this.snapshots.add(newSnapshot);
	}

	/**
	 * Encode this object to XML format. The encoded Element will contain all data necessary to
	 * re-create and object that is equal to this one.
	 * 
	 * @return an XML Element containing the XML description of this object.
	 */
	public Element encode(DocumentBuilderFactory factory, DocumentBuilder builder, Document document)
	{
		// create element
		Element element = document.createElement("SycamoreSystemMemory");

		// children
		Element selfPositionsElem = document.createElement("selfPositions");
		for (int i = 0; i < selfPositions.size(); i++)
		{
			Element selfpositionElem = document.createElement("selfPosition");

			P position = selfPositions.elementAt(i);
			selfpositionElem.appendChild(position.encode(factory, builder, document));
			selfPositionsElem.appendChild(selfpositionElem);
		}

		Element snapshotsElem = document.createElement("snapshots");
		for (int i = 0; i < snapshots.size(); i++)
		{
			Element snapshotElem = document.createElement("snapshot");
			Vector<P> points = snapshots.elementAt(i);
			for (int j = 0; j < points.size(); j++)
			{
				Element positionElem = document.createElement("position");

				P position = points.elementAt(j);
				positionElem.appendChild(position.encode(factory, builder, document));
				snapshotElem.appendChild(positionElem);
			}

			snapshotsElem.appendChild(snapshotElem);
		}

		// append children
		element.appendChild(selfPositionsElem);
		element.appendChild(snapshotsElem);

		return element;
	}

	/**
	 * Decode the fields in this memory by taking them from passed XML element. TYPE parameter is
	 * used to determine the type (2D or 3D) of the decoded object.
	 * 
	 * @param documentElement
	 */
	public boolean decode(Element element, TYPE type)
	{
		boolean success = true;
		NodeList nodes = element.getElementsByTagName("SycamoreSystemMemory");

		// if there is at least a Keyframe node, decode it
		if (nodes.getLength() > 0)
		{
			// selfPositions
			NodeList selfPositions = element.getElementsByTagName("selfPositions");
			this.selfPositions.removeAllElements();

			if (selfPositions.getLength() > 0)
			{
				Element selfPositionsElem = (Element) selfPositions.item(0);
				NodeList selfPosition = selfPositionsElem.getElementsByTagName("selfPosition");

				// positions
				for (int i = 0; i < selfPosition.getLength(); i++)
				{
					Element pointElem = (Element) selfPosition.item(i);
					P point = SycamoreUtil.getNewPoint(type);

					if (point.decode(pointElem, type))
					{
						this.selfPositions.add(point);
					}
				}
			}

			// snapshots
			NodeList snapshots = element.getElementsByTagName("snapshots");
			this.snapshots.removeAllElements();

			if (snapshots.getLength() > 0)
			{
				Element snapshotsElem = (Element) snapshots.item(0);
				NodeList snapshot = snapshotsElem.getElementsByTagName("snapshot");

				// single snapshot
				for (int i = 0; i < snapshot.getLength(); i++)
				{
					Vector<P> data = new Vector<P>();
					Element snapshotElem = (Element) snapshot.item(i);
					NodeList position = snapshotElem.getElementsByTagName("position");

					// positions
					for (int j = 0; j < position.getLength(); j++)
					{
						Element positionElem = (Element) position.item(j);
						P point = SycamoreUtil.getNewPoint(type);

						if (point.decode(positionElem, type))
						{
							data.add(point);
						}
					}

					this.snapshots.add(data);
				}
			}
		}

		return success;
	}

	/**
	 * Reset the memory by deleting every data in it
	 */
	public synchronized void reset()
	{
		this.snapshots.clear();
		this.selfPositions.clear();
	}
}
