/**
 * 
 */
package it.diunipi.volpi.sycamore.plugins.agreements;

import it.diunipi.volpi.sycamore.model.ComputablePoint;
import it.diunipi.volpi.sycamore.model.SycamoreAbstractPoint;

/**
 * @author Vale
 * 
 */
public abstract class DynamicAgreement<P extends SycamoreAbstractPoint & ComputablePoint<P>> extends AgreementImpl<P>
{
	/*
	 * (non-Javadoc)
	 * 
	 * @see it.diunipi.volpi.sycamore.plugins.agreements.Agreement#isDynamic()
	 */
	@Override
	public boolean isDynamic()
	{
		return true;
	}
}
