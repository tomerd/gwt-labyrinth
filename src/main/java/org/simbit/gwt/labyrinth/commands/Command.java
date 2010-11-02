/**
 * GWT-Labyrinth, a GWT micro-architecture
 * Copyright (C) 2010 simbit
 * www.simbit.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.simbit.gwt.labyrinth.commands;

import com.google.gwt.gen2.logging.shared.Log;
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
		Log.fine("command " + this.toString() + " returned:\n" + result, "protocol");
		
		if (_setBusy)
		{
			DOM.setStyleAttribute(RootPanel.getBodyElement(), "cursor", "default");
		}

		if (null != _callback) _callback.onSuccess(result);
	}
	
	public void onFailure(Throwable caught) 
	{
		Log.warning("command " + this + " returned: " + caught, "protocol");
		
		if (_setBusy)
		{
			DOM.setStyleAttribute(RootPanel.getBodyElement(), "cursor", "default");
		}
		
		if (null != _callback) _callback.onFailure(caught);
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
