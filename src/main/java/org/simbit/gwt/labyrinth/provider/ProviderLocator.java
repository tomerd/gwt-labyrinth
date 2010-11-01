package org.simbit.gwt.labyrinth.provider;

import org.simbit.gwt.labyrinth.provider.rest.restlet.RestletRestImpl;

// this class could be overriden using gwt dependency injection (aka deferred binding)
public final class ProviderLocator implements IProviderLocator
{		
	public IProvider get(Enum<?> provider)
	{
		switch ((Provider)provider)
		{ 
			case REST: return new RestletRestImpl();
			default: return null;
		}
	}
}
