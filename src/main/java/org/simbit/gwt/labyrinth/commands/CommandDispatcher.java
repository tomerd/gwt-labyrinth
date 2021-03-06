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

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

public final class CommandDispatcher
{			
	public interface IBusyMarker
	{
		public void markBusy(boolean busy);
	}
	
	private static IBusyMarker _busyMarker = null;
	
	public static void setBusyMarker(IBusyMarker marker)
	{
		_busyMarker = marker;	
	}
	
	public static <T> void dispatch(Enum<?> commandId, AsyncCallback<T> callback)
	{		
		dispatch(commandId, null, callback);	
	}
			
	public static <T> void dispatch(Enum<?> commandId, Object data, final AsyncCallback<T> callback)
	{
		try
		{
			// CommandLocator is to be overriden using gwt dependency injection (aka deferred binding)
			// TODO: look into changing this to be based on a compile time generator
			ICommandLocator locator = GWT.create(CommandLocator.class);
			ICommand<T> command = locator.get(commandId);
			if (null == command) throw (new Exception("command not found or no command is mapped to " + commandId + ", check commands mapping."));
			
			markBusy(true);
			command.execute(data, new AsyncCallback<T>() 
			{
				public void onSuccess(T result) 
				{
					markBusy(false);
					if (null != callback) callback.onSuccess(result);
				}
				
				public void onFailure(Throwable caught) 
				{
					markBusy(false);
					if (null != callback) callback.onFailure(caught);
				}				
			});			
		}
		catch (Exception e)
		{
			markBusy(false);
			if (null != callback) callback.onFailure(e);
		}
	}
	
	private static void markBusy(boolean busy)
	{
		if (null == _busyMarker) return;
		_busyMarker.markBusy(busy);
	}
}
