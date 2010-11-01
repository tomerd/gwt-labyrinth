package org.simbit.gwt.labyrinth.commands;

// this class is to be overriden using gwt dependency injection (aka deferred binding)
public final class CommandLocator implements ICommandLocator
{		
	public <T> ICommand<T> get(Enum<?> command)
	{
		return (null);
	}
}
