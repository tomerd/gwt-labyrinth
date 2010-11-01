package org.simbit.gwt.labyrinth.commands;

public interface ICommandLocator 
{
	<T> ICommand<T> get(Enum<?> command);
}
