/**
 * 
 */
package it.diunipi.volpi.sycamore.engine;

import javax.naming.OperationNotSupportedException;

/**
 * An exception that is thrown when some robot tries to turn on a light that it does not have.
 * 
 * @author Valerio Volpi - vale.v@me.com
 */
public class TooManyLightsException extends OperationNotSupportedException
{
	private static final long	serialVersionUID	= 1684953236948894235L;

	/**
	 * Default constructor.
	 */
	public TooManyLightsException(String message)
	{
		super(message);
	}
}
