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
 * @author Vale
 * 
 */
public class SycamoreSystemMemory<P extends SycamoreAbstractPoint & ComputablePoint<P>>
{
	private Vector<P>			selfPositions	= null;
	private Vector<Vector<P>>	snapshots		= null;

	/**
	 * 
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
	 * @param point
	 * @return
	 */
	private P clonePoint(P point)
	{
		try
		{
			return (P) point.clone();
		}
		catch (CloneNotSupportedException e)
		{
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * @param position
	 */
	public void addSelfPosition(P position)
	{
		this.selfPositions.add(this.clonePoint(position));
	}

	/**
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
	 * @param systemMemoryElem
	 * @param type
	 * @return
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
}
