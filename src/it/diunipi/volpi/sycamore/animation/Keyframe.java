package it.diunipi.volpi.sycamore.animation;

import it.diunipi.volpi.sycamore.model.ComputablePoint;
import it.diunipi.volpi.sycamore.model.SycamoreAbstractPoint;

/**
 * This class represents a keyframe. It contains informations that are interpolated by the timeline
 * to determine intermediate positions along paths.
 * 
 * @author Valerio Volpi - vale.v@me.com
 * 
 */
public class Keyframe<P extends SycamoreAbstractPoint & ComputablePoint<P>> implements Comparable<Keyframe<P>>
{
	private P		position	= null;
	private float	time		= 0;
	private boolean	pause		= false;

	/**
	 * Default constructor.
	 * 
	 * @param position
	 * @param time
	 */
	public Keyframe(P position, float time)
	{
		this(position, time, false);
	}

	/**
	 * Default constructor.
	 * 
	 * @param position
	 * @param time
	 */
	public Keyframe(P position, float time, boolean pause)
	{
		this.position = position;
		this.time = time;
		this.pause = pause;
	}

	/**
	 * @return the position
	 */
	public synchronized P getPosition()
	{
		return position;
	}

	/**
	 * @param position
	 */
	public synchronized void setPosition(P position)
	{
		this.position = position;
	}

	/**
	 * @return the time
	 */
	public synchronized float getTime()
	{
		return time;
	}

	/**
	 * @param time
	 */
	public synchronized void setTime(float time)
	{
		this.time = time;
	}

	/**
	 * @return the pause
	 */
	public synchronized boolean isPause()
	{
		return pause;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public synchronized String toString()
	{
		return (pause ? "pause " : "") + position.toString() + "@" + time + " sec";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public synchronized int compareTo(Keyframe<P> o)
	{
		return Float.compare(this.time, o.getTime());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj)
	{
		if (obj instanceof Keyframe)
		{
			Keyframe<P> o = (Keyframe<P>) obj;
			if (this.isPause() == o.isPause())
			{
				return this.position.equals(o.position);
			}
			else
			{
				return false;
			}
		}

		return false;
	}

	/**
	 * Given any float value of a ratio, and two other float values that represent the starting
	 * ratio and the final ratio that are seen as the bounds of an interval in which the ratio
	 * passed is contained, this method scales and translates the passed interval on a 0-1 form and
	 * then returns the respective of the passed ratio, projected on the computed interval.
	 * 
	 * @param time
	 * @param beforeTime
	 * @param afterTime
	 * @return
	 */
	private synchronized float projectTimeOnRatio(float time, float beforeTime, float afterTime)
	{
		if (beforeTime <= time && time <= afterTime)
		{
			// translate the ratios back to 0
			afterTime -= beforeTime;
			time -= beforeTime;

			// scale them to have a 1 length and return
			return time / afterTime;
		}
		else
			return 0;
	}

	/**
	 * Given a keyframe and a ratio, computes the P object that represents the position interpolated
	 * using the informations contained in this keyframe and in passed keyframe. The path is
	 * supposed to be linear and the speed uniform. The passed ratio is supposed to be
	 * included in the interval consisting of the ratios of this keyframe and of passed keyframe.
	 * 
	 * @param nextKeyframe
	 * @param time
	 * @return
	 */
	public synchronized P interpolateWithKeyframe(Keyframe<P> nextKeyframe, float time)
	{
		// compute the new position
		P beforePosition = this.getPosition();
		P afterPosition = nextKeyframe.getPosition();

		// get the ratios
		float beforeTime = this.getTime();
		float afterTime = nextKeyframe.getTime();

		if (beforeTime <= time && time <= afterTime)
		{
			// project passed ratio on a 0-1 scale
			float ratio = projectTimeOnRatio(time, beforeTime, afterTime);
			return beforePosition.interpolateWith(afterPosition, ratio);
		}
		else
			return null;
	}
}
