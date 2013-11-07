package it.diunipi.volpi.sycamore.animation;

import it.diunipi.volpi.sycamore.engine.ComputablePoint;
import it.diunipi.volpi.sycamore.engine.SycamoreAbstractPoint;
import it.diunipi.volpi.sycamore.engine.SycamoreEngine.TYPE;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * This class represents a generic animated object. Any animated object has its own timeline and
 * defines some operations and policies to access it.
 * 
 * @author Valerio Volpi - vale.v@me.com
 * 
 */
public abstract class SycamoreAnimatedObject<P extends SycamoreAbstractPoint & ComputablePoint<P>>
{
	protected P				startingPosition	= null;
	protected Timeline<P>	timeline			= null;
	protected float			currentRatio		= 0;
	protected P				direction			= null;

	/**
	 * @param direction
	 *            the direction to set
	 */
	public void setDirection(P direction)
	{
		this.direction = direction;
	}

	/**
	 * @return the direction
	 */
	public P getDirection()
	{
		return direction;
	}

	/**
	 * @return the currentRatio
	 */
	public float getCurrentRatio()
	{
		return currentRatio;
	}

	/**
	 * @param currentRatio
	 *            the currentRatio to set
	 */
	public synchronized void setCurrentRatio(float currentRatio)
	{
		if (!Float.isNaN(currentRatio) && !Float.isInfinite(currentRatio))
		{
			// set the ratio
			this.currentRatio = currentRatio;

			// eventually compute the direction
			if (currentRatio < 1)
			{
				this.computeCurrentDirection();
			}
		}
		else
		{
			throw new IllegalArgumentException("Passed ratio is NaN or Infinite");
		}
	}

	/**
	 * Computes the current direction based on current ratio. The direction points to the next
	 * keyframe
	 */
	private void computeCurrentDirection()
	{
		this.direction = this.timeline.getPointAfterRatio(currentRatio);
	}

	/**
	 * Returns the position of this robot at passed ratio. The path is supposed to be linear and the
	 * acceleration uniform. The ratio is supposed to be between 0 (beginning of the timeline) and 1
	 * (end of the timeline).
	 * 
	 * @param ratio
	 * @return
	 */
	public P getPosition(float ratio)
	{
		try
		{
			return this.timeline.getPosition(ratio);
		}
		catch (Exception e)
		{
			return startingPosition;
		}
	}

	/**
	 * Returns the full path of this robot.
	 * 
	 * @param ratio
	 * @return
	 */
	public SycamoreTimelinePath<P> getPath()
	{
		return this.timeline.getFullPath();
	}

	/**
	 * Returns the current position of this object
	 * 
	 * @return
	 * @throws TimelineNotAccessibleException
	 */
	public P getLocalPosition()
	{
		try
		{
			return this.timeline.getPosition(currentRatio);
		}
		catch (Exception e)
		{
			return startingPosition;
		}
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
		Element element = document.createElement("SycamoreAnimatedObject");

		// children
		Element startingPositionElem = document.createElement("startingPosition");
		startingPositionElem.appendChild(startingPosition.encode(factory, builder, document));

		Element timelineElem = document.createElement("timeline");
		timelineElem.appendChild(timeline.encode(factory, builder, document));

		Element currentRatioElem = document.createElement("currentRatio");
		currentRatioElem.appendChild(document.createTextNode(currentRatio + ""));

		if (direction != null)
		{
			Element directionElem = document.createElement("direction");
			directionElem.appendChild(direction.encode(factory, builder, document));
			element.appendChild(directionElem);
		}

		// append children
		element.appendChild(startingPositionElem);
		element.appendChild(timelineElem);
		element.appendChild(currentRatioElem);

		return element;
	}

	/**
	 * @param parentElem
	 * @param type
	 * @return
	 */
	public synchronized boolean decode(Element element, TYPE type)
	{
		boolean success = true;
		NodeList nodes = element.getElementsByTagName("SycamoreAnimatedObject");

		// if there is at least a SycamoreAnimatedObject node, decode it
		if (nodes.getLength() > 0)
		{
			// startingPosition
			NodeList startingPosition = element.getElementsByTagName("startingPosition");
			if (startingPosition.getLength() > 0)
			{
				Element startingPositionElem = (Element) startingPosition.item(0);
				success = success && this.startingPosition.decode(startingPositionElem, type);
			}

			// timeline
			NodeList timeline = element.getElementsByTagName("timeline");
			if (timeline.getLength() > 0)
			{
				Element timelineElem = (Element) timeline.item(0);
				success = success && this.timeline.decode(timelineElem, type);
			}

			// currentRatio
			NodeList currentRatio = element.getElementsByTagName("currentRatio");
			if (currentRatio.getLength() > 0)
			{
				Element currentRatioElem = (Element) currentRatio.item(0);
				float ratio = Float.parseFloat(currentRatioElem.getTextContent());

				this.setCurrentRatio(ratio);
			}
		}

		return success;
	}
}
