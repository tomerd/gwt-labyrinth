package org.simbit.gwt.labyrinth.provider;

public interface IProviderLocator 
{
	IProvider get(Enum<?> provider);
}
