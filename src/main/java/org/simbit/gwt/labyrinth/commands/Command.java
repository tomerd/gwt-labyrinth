package org.simbit.gwt.labyrinth.commands;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.RootPanel;

public abstract class Command <T> implements ICommand<T>, AsyncCallback<T>
{
	private AsyncCallback<T> _callback = null;
	private boolean _setBusy = true;
		
	public final void execute(Object data, AsyncCallback<T> callback) 
	{
		this.execute(data, callback, true);
	}
	
	public void execute(Object data, AsyncCallback<T> callback, boolean setBusy) 
	{
		_callback = callback;
		_setBusy = setBusy;
		
		if (_setBusy)
		{
			DOM.setStyleAttribute(RootPanel.getBodyElement(), "cursor", "wait");
		}
	}
		
	public void onSuccess(T result) 
	{	
		//Log.fine("command " + this.toString() + " returned:\n" + result, "protocol");
		
		if (_setBusy)
		{
			DOM.setStyleAttribute(RootPanel.getBodyElement(), "cursor", "default");
		}

		if (null != _callback) _callback.onSuccess(result);
	}
	
	public void onFailure(Throwable error) 
	{
		//Log.warning("command " + this.toString() + " returned: " + error.getMessage(), "protocol");
		
		if (_setBusy)
		{
			DOM.setStyleAttribute(RootPanel.getBodyElement(), "cursor", "default");
		}
		
		if (null != _callback) _callback.onFailure(error);
	}
	
	protected final Object getDataElement(Object data, String name)
	{
		if (!(data instanceof Object[][])) return (null);
		
		Object[][] array = (Object[][])data;
		for (int index=0; index < array.length; index++)
		{
			if (2 != array[index].length) continue;
			if (name == array[index][0]) return (array[index][1]);
		}
		return (null);
	}
	
	protected final String getStringDataElement(Object data, String name)
	{
		Object value = this.getDataElement(data, name);
		return (value != null) ? value.toString() : null;
	}
	
	protected final Long getLongDataElement(Object data, String name)
	{
		String value = this.getStringDataElement(data, name);
		return (null != value) ? Long.parseLong(value) : Long.MIN_VALUE;
	}
	
	protected final boolean getSetBusy()
	{
		return _setBusy;
	}
}
