/**
 * 
 */
package it.diunipi.volpi.sycamore.model;

import javax.naming.OperationNotSupportedException;

/**
 * An exception that is thrown when some robot tries to acces the value of n, but this is unknown.
 * 
 * @author Valerio Volpi - vale.v@me.com
 */
public class NNotKnownException extends OperationNotSupportedException
{
	private static final long	serialVersionUID	= 4791016404874299845L;

	/**
	 * Default constructor.
	 */
	public NNotKnownException(String message)
	{
		super(message);
	}
}
