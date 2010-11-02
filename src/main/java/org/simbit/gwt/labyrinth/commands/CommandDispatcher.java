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
import com.google.gwt.gen2.logging.shared.Log;
import com.google.gwt.user.client.rpc.AsyncCallback;

public final class CommandDispatcher
{			
	public static <T> void dispatch(Enum<?> commandId, AsyncCallback<T> callback)
	{		
		dispatch(commandId, null, true, callback);	
	}
	
	public static <T> void dispatch(Enum<?> commandId, AsyncCallback<T> callback, boolean setBusy)
	{		
		dispatch(commandId, null, setBusy, callback);	
	}	
	
	public static <T> void dispatch(Enum<?> commandId, Object data, AsyncCallback<T> callback)
	{
		dispatch(commandId, data, true, callback);
	}
	
	public static <T> void dispatch(Enum<?> commandId, Object data, boolean setBusy, AsyncCallback<T> callback)
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
			Log.severe("error invoking commnd " + commandId + ", " + e);
		}
	}
}
