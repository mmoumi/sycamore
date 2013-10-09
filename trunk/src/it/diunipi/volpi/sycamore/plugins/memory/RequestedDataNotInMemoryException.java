package it.diunipi.volpi.sycamore.plugins.memory;
import javax.naming.OperationNotSupportedException;

/**
 * This exception is thrown when a data that is not stored in memory is requested.
 * 
 * @author Valerio Volpi - vale.v@me.com
 */
public class RequestedDataNotInMemoryException extends OperationNotSupportedException
{
	private static final long	serialVersionUID	= -7869816867859048041L;

	/**
	 * Constructor.
	 * 
	 * @param string
	 */
	public RequestedDataNotInMemoryException(String string)
	{
		super(string);
	}
}
