package org.simbit.gwt.labyrinth.commands;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

public final class CommandUtil 
{			
	public static <T> void send(Enum<?> commandId, AsyncCallback<T> callback)
	{		
		send(commandId, callback, null, true);	
	}
	
	public static <T> void send(Enum<?> commandId, AsyncCallback<T> callback, boolean setBusy)
	{		
		send(commandId, callback, null, setBusy);	
	}	
	
	public static <T> void send(Enum<?> commandId, AsyncCallback<T> callback, Object data)
	{
		send(commandId, callback, data, true);
	}
	
	public static <T> void send(Enum<?> commandId, AsyncCallback<T> callback, Object data, boolean setBusy)
	{
		try
		{
			// CommandLocator is to be overriden using gwt dependency injection (aka deferred binding)
			ICommandLocator locator = GWT.create(CommandLocator.class);
			ICommand<T> command = locator.get(commandId);
			if (null == command) throw (new Exception("command not found or no command is mapped to " + commandId + ", check commands mapping."));
			command.execute(data, callback, setBusy);			
		}
		catch (Exception e)
		{
			//Log.error("error invoking commnd " + commandId + ", " + e);
		}
	}
}
