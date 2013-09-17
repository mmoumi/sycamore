/**
 * 
 */
package it.diunipi.volpi.sycamore.plugins.memory;

import it.diunipi.volpi.sycamore.engine.ComputablePoint;
import it.diunipi.volpi.sycamore.engine.Observation;
import it.diunipi.volpi.sycamore.engine.Point3D;
import it.diunipi.volpi.sycamore.engine.SycamoreAbstractPoint;
import it.diunipi.volpi.sycamore.engine.SycamoreRobotLight;

import java.io.File;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

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
			P position = selfPositions.elementAt(i);
			selfPositionsElem.appendChild(position.encode(factory, builder, document));
		}

		Element snapshotsElem = document.createElement("snapshots");
		for (int i = 0; i < snapshots.size(); i++)
		{
			Element snapshotElem = document.createElement("snapshot");
			Vector<P> points = snapshots.elementAt(i);
			for (int j = 0; j < points.size(); j++)
			{
				P position = points.elementAt(j);
				snapshotElem.appendChild(position.encode(factory, builder, document));
			}
			
			snapshotsElem.appendChild(snapshotElem);
		}

		// append children
		element.appendChild(selfPositionsElem);
		element.appendChild(snapshotsElem);

		return element;
	}

	public static void main(String[] args)
	{
		SycamoreSystemMemory<Point3D> memory = new SycamoreSystemMemory<Point3D>();
		memory.addSelfPosition(new Point3D(3, 4, 5));

		Observation<Point3D> o1 = new Observation<Point3D>(new Point3D(6, 7, 8), new Vector<SycamoreRobotLight<Point3D>>(), false);
		Observation<Point3D> o2 = new Observation<Point3D>(new Point3D(9, 10, 11), new Vector<SycamoreRobotLight<Point3D>>(), false);
		Observation<Point3D> o3 = new Observation<Point3D>(new Point3D(12, 13, 14), new Vector<SycamoreRobotLight<Point3D>>(), false);

		Vector<Observation<Point3D>> obs = new Vector<Observation<Point3D>>();
		obs.add(o1);
		obs.add(o2);
		obs.add(o3);
		memory.addSnapshot(obs);
		memory.addSelfPosition(new Point3D(15, 16, 17));

		try
		{
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

			Document doc = docBuilder.newDocument();
			doc.appendChild(memory.encode(docFactory, docBuilder, doc));

			// write the content into xml file
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File("/Users/Vale/Desktop/file.xml"));

			// Output to console for testing
			// StreamResult result = new StreamResult(System.out);

			transformer.transform(source, result);

			System.out.println("File saved!");
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
