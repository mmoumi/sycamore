package it.diunipi.volpi.sycamore.animation;

import it.diunipi.volpi.sycamore.engine.ComputablePoint;
import it.diunipi.volpi.sycamore.engine.SycamoreAbstractPoint;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

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
			this.currentRatio = currentRatio;
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
	public Element encode(DocumentBuilderFactory factory, DocumentBuilder builder, Document document)
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
		
		Element directionElem = document.createElement("direction");
		directionElem.appendChild(direction.encode(factory, builder, document));
		
		// append children
		element.appendChild(startingPositionElem);
		element.appendChild(timelineElem);
		element.appendChild(currentRatioElem);
		element.appendChild(directionElem);
		
		return element;
	}
}
