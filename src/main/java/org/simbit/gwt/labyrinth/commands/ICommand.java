package org.simbit.gwt.labyrinth.commands;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ICommand<T> 
{	
	void execute(Object data, AsyncCallback<T> callback);
	void execute(Object data, AsyncCallback<T> callback, boolean setBusy);
}
