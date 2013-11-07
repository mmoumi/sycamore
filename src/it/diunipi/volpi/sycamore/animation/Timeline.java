package it.diunipi.volpi.sycamore.animation;

import it.diunipi.volpi.sycamore.engine.ComputablePoint;
import it.diunipi.volpi.sycamore.engine.SycamoreAbstractPoint;
import it.diunipi.volpi.sycamore.engine.SycamoreEngine.TYPE;
import it.diunipi.volpi.sycamore.util.SortedList;
import it.diunipi.volpi.sycamore.util.SycamoreUtil;

import java.util.Iterator;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * This class represents a timeline. It contains a certain number of keyframes, stored at a fixed
 * ratio. The user can query the timeline giving a intermediate ratio, and it obtains the position
 * computed by interpolating between two keyframes, considering linear paths. The timeline has its
 * default meaaure unit, it i such that the full lenght of the timeline is always one. By the way,
 * it can be scaled using using an appropriate AffineTransform object, that is provided by the
 * timeline itself.
 * 
 * @author Valerio Volpi - vale.v@me.com
 * 
 */
public class Timeline<P extends SycamoreAbstractPoint & ComputablePoint<P>>
{
	private static final int		DEFAULT_STEP	= 1;
	private SortedList<Keyframe<P>>	keyframes		= null;
	private float					duration		= DEFAULT_STEP;

	/**
	 * Default constructor.
	 */
	public Timeline()
	{
		this.keyframes = new SortedList<Keyframe<P>>();
	}

	/**
	 * @return the duration
	 */
	public synchronized float getDuration()
	{
		return duration;
	}

	/**
	 * Adds a new keyframe with passed position at the end of the timeline. The keyframe is added
	 * with a dinstance in time equal to the default timeline step (1 unit).
	 * 
	 * @param position
	 */
	public synchronized void addKeyframe(P position)
	{
		this.addKeyframe(position, DEFAULT_STEP);
	}

	/**
	 * Adds a new keyframe with passed position at the end of the timeline. The value deltaTime
	 * reprsent the distance in time from the second to last keyframe. If the timeline is empty the
	 * keyframe is added at the beginning, and if the timeline has just one keyframe the new one
	 * will automatically have ratio one, independently from the passed value of deltaTime.
	 * 
	 * @param position
	 */
	public synchronized void addKeyframe(P position, float deltaTime)
	{
		// trivial cases
		if (this.keyframes.isEmpty())
		{
			this.keyframes.add(new Keyframe<P>(position, 0.0f));
			duration = deltaTime;
		}
		else if (this.keyframes.size() == 1)
		{
			this.keyframes.add(new Keyframe<P>(position, deltaTime));
			duration = deltaTime;
		}
		else
		{
			// float extendedRatio = 1 + (ratioDinstance / (1.0f - ratioDinstance));
			float time = duration + deltaTime;

			// the timeline now has lenght 1. The new keyframe is added at ratio extendedRatio
			Keyframe<P> keyframe = new Keyframe<P>(position, time);

			this.keyframes.add(keyframe);

			// update duration
			this.duration = time;
		}
	}

	/**
	 * Adds a pause in the timeline, deltaTime units after the end
	 */
	public synchronized void addPause(float deltaTime)
	{
		if (!this.keyframes.isEmpty())
		{
			// float extendedRatio = 1 + (ratioDinstance / (1.0f - ratioDinstance));
			float time = duration + deltaTime;

			// get the last element
			Keyframe<P> last = this.keyframes.lastElement();

			while (last.isPause())
			{
				this.keyframes.remove(last);
				last = this.keyframes.lastElement();
			}

			// the timeline now has lenght 1. The new keyframe is added at ratio extendedRatio
			Keyframe<P> keyframe = new Keyframe<P>(last.getPosition(), time, true);

			this.keyframes.add(keyframe);

			// update duration
			this.duration = time;
		}
	}

	/**
	 * Edits the i-th keyframe, by giving to it the passed position and the passed time. The
	 * keyframes that follows the requested one, as well as the total duration are adjusted
	 * accordingly.
	 * 
	 * @param index
	 * @param position
	 * @param time
	 * @return
	 */
	public synchronized boolean editKeyframe(int keyframeIndex, P position, float time)
	{
		if (keyframeIndex < 0 || keyframeIndex > this.keyframes.size() - 1)
		{
			return false;
		}
		else
		{
			Keyframe<P> keyframe = this.keyframes.get(keyframeIndex);

			if (keyframeIndex > 0)
			{
				// get the time of the previous keyframe and compute new time
				float previousTime = this.keyframes.get(keyframeIndex - 1).getTime();
				time = previousTime + time;
			}

			// compute the deltaTime
			float originalTime = keyframe.getTime();
			float deltaTime = time - originalTime;

			keyframe.setPosition(position);
			keyframe.setTime(time);

			// adjust following keyframes
			for (int i = keyframeIndex + 1; i < keyframes.size(); i++)
			{
				Keyframe<P> frame = keyframes.get(i);
				frame.setTime(frame.getTime() + deltaTime);
			}

			// adjust duration
			duration = duration + deltaTime;

			return true;
		}
	}

	/**
	 * Returns the ratio of the keyframe with passed index
	 * 
	 * @param keyframe
	 * @return
	 */
	public synchronized float getRatioOfKeyframe(int keyframe)
	{
		if (duration == 0)
		{
			return 0.0f;
		}
		else
		{
			float extendedRatio = this.keyframes.get(keyframe).getTime();
			return (extendedRatio / duration);
		}
	}

	/**
	 * Removes the keyframe with passed index, and adjusts the indexes of the other keyframes
	 * accordingly.
	 * 
	 * @param keyframe
	 */
	public synchronized void removeKeyframe(int keyframe)
	{
		this.keyframes.remove(keyframe);
	}

	/**
	 * Returns the number of keyframes in this timeline.
	 * 
	 * @return
	 */
	public synchronized int getNumKeyframes()
	{
		return this.keyframes.size();
	}

	/**
	 * Returns the full path
	 * 
	 * @return
	 */
	public synchronized SycamoreTimelinePath<P> getFullPath()
	{
		Vector<P> points = new Vector<P>();

		// populate vector
		for (int i = 0; i < this.keyframes.size(); i++)
		{
			points.add(this.keyframes.get(i).getPosition());
		}

		return new SycamoreTimelinePath<P>(points);
	}

	/**
	 * Returns true if there is a keyframe in the timeline whose position corresponds to the passed
	 * one, false otherwise.
	 * 
	 * @param position
	 * @return
	 */
	public synchronized boolean containsKeyframe(P position)
	{
		Keyframe<P> tmp = new Keyframe<P>(position, 0);
		return keyframes.contains(tmp);
	}

	/**
	 * Returns the first point of the timeline
	 * 
	 * @return
	 */
	public synchronized P getFirstPoint()
	{
		if (!this.keyframes.isEmpty())
		{
			return keyframes.firstElement().getPosition();
		}
		else
		{
			return null;
		}
	}

	/**
	 * Returns the last point of the timeline
	 * 
	 * @return
	 */
	public synchronized P getLastPoint()
	{
		if (!this.keyframes.isEmpty())
		{
			return keyframes.lastElement().getPosition();
		}
		else
		{
			return null;
		}
	}

	/**
	 * Returns the point corresponding to the keyframe at passed index
	 * 
	 * @param index
	 * @return
	 */
	public synchronized P getPointAtIndex(int index)
	{
		if (!this.keyframes.isEmpty() && index >= 0 && index < this.keyframes.size())
		{
			return keyframes.get(index).getPosition();
		}
		else
		{
			return null;
		}
	}

	/**
	 * Returns the time corresponding to the keyframe at passed index
	 * 
	 * @param index
	 * @return
	 */
	public synchronized float getTimeAtIndex(int index)
	{
		if (!this.keyframes.isEmpty() && index >= 0 && index < this.keyframes.size())
		{
			return keyframes.get(index).getTime();
		}
		else
		{
			return 0;
		}
	}

	/**
	 * Returns a list of the keyframes that are not pause keyframes.
	 * 
	 * @return
	 */
	private synchronized Vector<Keyframe<P>> getNotPausedKeyframes()
	{
		Vector<Keyframe<P>> notPausedKeyframes = new Vector<Keyframe<P>>();
		for (Keyframe<P> keyframe : this.keyframes)
		{
			if (!keyframe.isPause())
			{
				notPausedKeyframes.add(keyframe);
			}
		}
		return notPausedKeyframes;
	}

	/**
	 * Returns a list of all the points in this timeline
	 * 
	 * @return
	 */
	public synchronized Vector<P> getPoints()
	{
		Vector<P> points = new Vector<P>();
		for (Keyframe<P> keyframe : this.keyframes)
		{
			if (!keyframe.isPause())
			{
				points.add(keyframe.getPosition());
			}
		}
		return points;
	}

	/**
	 * Returns true if the i-th keyframe is a pause keyframe, false otherwise.
	 * 
	 * @param keyframeIndex
	 * @return
	 */
	public synchronized boolean isPause(int index)
	{
		if (!this.keyframes.isEmpty() && index >= 0 && index < this.keyframes.size())
		{
			return keyframes.get(index).isPause();
		}
		else
		{
			return false;
		}
	}

	/**
	 * Returns the point corresponding to the first keyframe that has ratio smaller than the passed
	 * one. If the passed ratio corresponds exactly to the ratio of a keyframe, the point of that
	 * keyframe is returned. If there is no keyframe in the timeline whose ratio is smaller than the
	 * passed ratio value, the first keyframe of the timeline is returned.
	 * 
	 * @param ratio
	 * @return
	 */
	public synchronized P getPointBeforeRatio(float ratio)
	{
		// the internal ratio can have more values, but the user has to query the timeline just in a
		// 0-1 scale.
		if (ratio < 0 || ratio > 1) throw new IllegalArgumentException("The ratio should be between 0 and 1.");

		// extend the ratio to match duration
		float time = ratio * duration;

		// determine the keyframes that are before and after passed ratio
		Vector<Keyframe<P>> notPausedKeyframes = this.getNotPausedKeyframes();
		Keyframe<P> before = notPausedKeyframes.firstElement();

		Iterator<Keyframe<P>> iterator = notPausedKeyframes.iterator();
		while (iterator.hasNext())
		{
			// if the ratio is exatly that one, return
			Keyframe<P> keyframe = iterator.next();
			if (keyframe.getTime() == time)
			{
				return keyframe.getPosition();
			}
			// otherwise determine the before keyframe and the after keyframe
			else if (keyframe.getTime() > time)
			{
				break;
			}
			else
			{
				before = keyframe;
			}
		}

		if (before == null)
		{
			before = this.keyframes.firstElement();
		}
		return before.getPosition();
	}

	/**
	 * Returns the point corresponding to the first keyframe that has ratio higher than the passed
	 * one. If the passed ratio corresponds exactly to the ratio of a keyframe, the point of that
	 * keyframe is returned. If there is no keyframe in the timeline whose ratio is higher than the
	 * passed ratio value, the last keyframe of the timeline is returned.
	 * 
	 * @param ratio
	 * @return
	 */
	public synchronized P getPointAfterRatio(float ratio)
	{
		// the internal ratio can have more values, but the user has to query the timeline just in a
		// 0-1 scale.
		if (ratio < 0 || ratio > 1) throw new IllegalArgumentException("The ratio should be between 0 and 1.");

		// extend the ratio to match duration
		float time = ratio * duration;

		// determine the keyframes that are before and after passed ratio
		Vector<Keyframe<P>> notPausedKeyframes = this.getNotPausedKeyframes();
		Keyframe<P> after = null;

		Iterator<Keyframe<P>> iterator = notPausedKeyframes.iterator();
		while (iterator.hasNext())
		{
			// if the ratio is exatly that one, return
			Keyframe<P> keyframe = iterator.next();
			if (keyframe.getTime() == time)
			{
				if (iterator.hasNext())
				{
					return iterator.next().getPosition();
				}
				else
				{
					return keyframe.getPosition();
				}
			}
			// otherwise determine the before keyframe and the after keyframe
			else if (keyframe.getTime() > time)
			{
				after = keyframe;
				break;
			}
		}

		if (after == null)
		{
			after = this.keyframes.lastElement();
		}
		return after.getPosition();
	}

	/**
	 * Computes the position at passed ratio, interpolating between the preceeding and the following
	 * keyframes. The path is supposed to be linear and the speed uniform. The ratio is supposed to
	 * be between 0 (beginning of the timeline) and 1 (end of the timeline).
	 * 
	 * @param ratio
	 * @return
	 */
	public synchronized P getPosition(float ratio)
	{
		// the internal ratio can have more values, but the user has to query the timeline just in a
		// 0-1 scale.
		if (ratio < 0 || ratio > 1) throw new IllegalArgumentException("The ratio should be between 0 and 1.");

		// case zero
		if (duration == 1 && keyframes.size() == 1)
		{
			return keyframes.firstElement().getPosition();
		}

		// extend the ratio to match duration
		float time = ratio * duration;

		// determine the keyframes that are before and after passed ratio
		Keyframe<P> before = this.keyframes.firstElement();
		Keyframe<P> after = null;

		Iterator<Keyframe<P>> iterator = this.keyframes.iterator();
		while (iterator.hasNext())
		{
			// if the ratio is exatly that one, return
			Keyframe<P> keyframe = iterator.next();
			if (keyframe.getTime() == time)
			{
				return keyframe.getPosition();
			}
			// otherwise determine the before keyframe and the after keyframe
			else if (keyframe.getTime() > time)
			{
				after = keyframe;
				break;
			}
			else
			{
				before = keyframe;
			}
		}

		if (before == null)
		{
			before = this.keyframes.firstElement();
		}
		if (after == null)
		{
			after = this.keyframes.lastElement();
		}

		// interpolate
		return before.interpolateWithKeyframe(after, time);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public synchronized String toString()
	{
		Iterator<Keyframe<P>> iterator = this.keyframes.iterator();
		StringBuilder str = new StringBuilder();
		int i = 0;

		str.append("duration: " + duration + "\n");
		while (iterator.hasNext())
		{
			Keyframe<P> keyframe = iterator.next();
			str.append("Keyframe " + i + ": " + keyframe.toString() + "\n");
			i++;
		}

		return str.toString();
	}

	/**
	 * Reset this timelne, by removing all keyframes
	 */
	public synchronized void clear()
	{
		this.keyframes.removeAllElements();
		duration = 0;
	}

	/**
	 * 
	 */
	public synchronized void clearIntermediate()
	{
		if (keyframes.size() > 3)
		{
			Keyframe<P> first = this.keyframes.firstElement();
			Keyframe<P> secondToLast = this.keyframes.get(this.keyframes.size() - 2);
			Keyframe<P> last = this.keyframes.lastElement();

			this.clear();

			this.keyframes.add(first);
			this.keyframes.add(secondToLast);
			this.keyframes.add(last);
			this.duration = last.getTime();
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
		Element element = document.createElement("Timeline");

		// children
		Element keyframesElem = document.createElement("keyframes");
		for (int i = 0; i < keyframes.size(); i++)
		{
			Element keyframeElem = document.createElement("keyframe");
			Keyframe<P> keyframe = keyframes.get(i);
			keyframeElem.appendChild(keyframe.encode(factory, builder, document));
			keyframesElem.appendChild(keyframeElem);
		}

		Element durationElem = document.createElement("duration");
		durationElem.appendChild(document.createTextNode(duration + ""));

		// append children
		element.appendChild(keyframesElem);
		element.appendChild(durationElem);

		return element;
	}

	/**
	 * @param timelineElem
	 * @param type
	 * @return
	 */
	public synchronized boolean decode(Element element, TYPE type)
	{
		boolean success = true;
		NodeList nodes = element.getElementsByTagName("Timeline");

		// if there is at least a Timeline node, decode it
		if (nodes.getLength() > 0)
		{
			// keyframes
			NodeList keyframes = element.getElementsByTagName("keyframes");
			if (keyframes.getLength() > 0)
			{
				this.keyframes.removeAllElements();
				Element keyframesElem = (Element) keyframes.item(0);

				// single keyframes
				NodeList singleKeyframes = keyframesElem.getElementsByTagName("keyframe");
				for (int i = 0; i < singleKeyframes.getLength(); i++)
				{
					Element keyframeElem = (Element) singleKeyframes.item(i);
					Keyframe<P> newKeyframe = new Keyframe<P>((P) SycamoreUtil.getNewPoint(type), 0);

					if (newKeyframe.decode(keyframeElem, type))
					{
						this.keyframes.add(newKeyframe);
					}
				}
			}

			// duration
			NodeList duration = element.getElementsByTagName("duration");
			if (duration.getLength() > 0)
			{
				Element durationElem = (Element) duration.item(0);
				this.duration = Float.parseFloat(durationElem.getTextContent());
			}
		}

		return success;
	}
}
