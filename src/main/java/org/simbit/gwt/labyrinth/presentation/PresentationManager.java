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

package org.simbit.gwt.labyrinth.presentation;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.simbit.gwt.labyrinth.views.IEventHandler;
import org.simbit.gwt.labyrinth.views.IViewLocator;
import org.simbit.gwt.labyrinth.views.ViewLocator;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

public class PresentationManager
{		
	private static HasWidgets _contianer = null;
	
	private static HandlerManager _eventBus = null;
	
	private static Map<String, HistoryItem> _history = null;
	
	public static void bind(HasWidgets contianer)
	{
		_contianer = contianer;
	}
		
	public static void navigateTo(Enum<?> viewId) 
	{
		navigateTo(viewId, null);
	}
	
	public static void navigateTo(Enum<?> viewId, Object data) 
	{
		try
		{		
			if (null == _history)
			{
				_history = new HashMap<String, HistoryItem>();
				History.addValueChangeHandler(getHistoryValueChangedHandler());	
			}
			
			Widget widget = getInitializedWidget(viewId, data); 
					
			String token = new Long(new Date().getTime()).toString();
			_history.put(token, new HistoryItem(viewId, data));
			History.newItem(token, false);				
			
			setCurrentWidget(widget);
		}
		catch(Exception e)
		{
			//Log.error("failed navigating to " + viewId + ":"  + e
			GWT.log("failed navigating to " + viewId + ":"  + e);
		}
	}
	
	private static ValueChangeHandler<String> getHistoryValueChangedHandler()
	{
		return new ValueChangeHandler<String>() 
		{
			public void onValueChange(ValueChangeEvent<String> event)
			{
				try
				{
					if (null == _history) throw new Exception("history not initilaized");
					if (!_history.containsKey(event.getValue())) throw new Exception("unknown history token " + event.getValue());				
					HistoryItem item = _history.get(event.getValue());
					Widget widget = getInitializedWidget(item.view, item.data);
					setCurrentWidget(widget);
				}
				catch (Exception e)
				{
					//Log.warning("failed handling history change event: " + e, "navigation");
					GWT.log("failed handling history change event: " + e);
				}
			}
		};
	}
	
	private static Widget getInitializedWidget(Enum<?> viewId, Object data) throws Exception
	{
		// ViewLocator to be override by application using gwt dependency injection (aka deferred binding)
		IViewLocator locator = GWT.create(ViewLocator.class);
		Widget widget = locator.get(viewId);
		if (null == viewId) throw (new Exception("view not found or no view is mapped to " + viewId + ", check views mapping."));
		//if (widget instanceof IDataContainer) ((IDataContainer)widget).setData(data);
		if (widget instanceof IEventHandler) ((IEventHandler)widget).setEventBus(getEventBus());
		return widget;
	}
	
	private static HasWidgets getContainer()
	{
		if (null == _contianer) _contianer = RootPanel.get();
		return _contianer;
	}
	
	private static HandlerManager getEventBus()
	{
		if (null == _eventBus) _eventBus = new HandlerManager(null);
		return _eventBus;
	}	
	
	private static void setCurrentWidget(Widget widget)
	{				
		getContainer().clear();
		getContainer().add(widget);
	}
		
	private static class HistoryItem
	{
		public final Enum<?> view;
		public final Object data;
		
		public HistoryItem(Enum<?> view, Object data)
		{
			this.view = view;
			this.data = data;
		}
	}

}
