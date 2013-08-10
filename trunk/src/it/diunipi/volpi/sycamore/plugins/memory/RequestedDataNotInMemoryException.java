package it.diunipi.volpi.sycamore.plugins.memory;
import javax.naming.OperationNotSupportedException;

/**
 * @author Vale
 * 
 */
public class RequestedDataNotInMemoryException extends OperationNotSupportedException
{
	private static final long	serialVersionUID	= -7869816867859048041L;

	/**
	 * @param string
	 */
	public RequestedDataNotInMemoryException(String string)
	{
		super(string);
	}
}
