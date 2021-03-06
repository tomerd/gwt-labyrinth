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

package org.simbit.gwt.labyrinth.provider.rest.restlet;

/*
import java.util.ArrayList;
import java.util.logging.Logger;

import org.restlet.client.Request;
import org.restlet.client.Response;
import org.restlet.client.Uniform;
import org.restlet.client.data.Form;
import org.restlet.client.data.MediaType;
import org.restlet.client.data.Parameter;
import org.restlet.client.ext.json.JsonRepresentation;
import org.restlet.client.ext.xml.DomRepresentation;
import org.restlet.client.representation.EmptyRepresentation;
import org.restlet.client.representation.Representation;
import org.restlet.client.representation.StringRepresentation;
import org.restlet.client.resource.ClientResource;
import java.util.logging.Logger;
*/

import org.simbit.gwt.labyrinth.provider.rest.HttpMethod;
import org.simbit.gwt.labyrinth.provider.rest.IRestProvider;

import com.google.gwt.core.client.JavaScriptObject;

import com.google.gwt.json.client.JSONObject;
import com.google.gwt.jsonp.client.JsonpRequestBuilder;
import com.google.gwt.user.client.rpc.AsyncCallback;
//import com.google.gwt.xml.client.Document;

public final class RestletRestImpl implements IRestProvider
{
	public <T> void get(String url, AsyncCallback<T> callback)
	{
		send(url, HttpMethod.GET, callback);
	}
	
	public <T> void post(String url, Object data, AsyncCallback<T> callback)
	{
		send(url, HttpMethod.POST, data, callback);
	}
	
	public <T> void put(String url, Object data, AsyncCallback<T> callback)
	{
		send(url, HttpMethod.PUT, data, callback);
	}

	public <T> void delete(String url, AsyncCallback<T> callback)
	{
		send(url, HttpMethod.DELETE, callback);
	}
	
	public <T> void send(String url, AsyncCallback<T> callback)
	{
		send(url, HttpMethod.UNKNOWN, null, callback);
	}
		
	public <T> void send(String url, HttpMethod method, AsyncCallback<T> callback)
	{
		send(url, method, null, callback);
	}
	
	public <T> void send(String url, Object data, AsyncCallback<T> callback)
	{		
		send(url, HttpMethod.UNKNOWN, data, callback);	
	}	
		
	public <T> void send(String url, HttpMethod method, Object data, AsyncCallback<T> callback)
	{							
		/*
		if (null == url) return;
		
		// common sense defaults
		if (HttpMethod.UNKNOWN == method) method = (null != data) ? HttpMethod.POST : HttpMethod.GET;
		
		if (HttpMethod.GET == method)
		{
			Logger.getLogger(this.getClass().getName()).finest("requesting " + url);
		}
		else
		{
			Logger.getLogger(this.getClass().getName()).finest("posting to " + url + "\n" + data);
		}
				
		ClientResource resource = new ClientResource(url);
		resource.setOnResponse(new ResponseHandler<T>(callback));
				
		// TODO: find a nicer way to do this
		try
		{
			switch (method)
			{
				case GET:
				{
					resource.get();
					break;
				}
				case POST:
				{
					resource.post(dataToRepresetation(data));
					break;
				}
				case PUT:
				{
					resource.put(dataToRepresetation(data));
					break;
				}
				case DELETE:
				{
					resource.delete();
					break;
				}	
				default:
				{
					throw (new Exception("unknown http method " + method));
				}
			}
		}
		catch(Exception e)
		{
			Logger.getLogger(this.getClass().getName()).severe("internal error performing request " + e);
		} 
		*/
	}
	
	/*	
	// FIXME, seems like restlet is not doing this properly, so hacking around it
	private Representation dataToRepresetation(Object data)
	{
		if (data instanceof String)
		{
			// TODO: test this scenario			
			return new StringRepresentation((String)data);
		}
		else if (data instanceof String[][])
		{
			Form form = buildForm((String[][])data);
			return form.getWebRepresentation();
		}
		else if (data instanceof JSONObject)
		{	
			return new JsonRepresentation(((JSONObject)data).toString());
		}
		else if (data instanceof Document)
		{
			// TODO: test this scenario
			return new DomRepresentation(MediaType.APPLICATION_XML, (Document)data);
		}
		else
		{			
			return new EmptyRepresentation();
		}
	}
	*/
	
	/*
	private final Form buildForm(String[][] fields)
	{
		ArrayList<Parameter> list = new ArrayList<Parameter>();
		
		for (int index=0; index < fields.length; index ++)
		{
			if (2 != fields[index].length) continue;
			list.add(new Parameter(fields[index][0], fields[index][1]));
		}
		
		return new Form(list);
	}	
	*/
			
	/*
	*** ResponseHandler
	 */
		
	/*
	private class ResponseHandler<T> implements Uniform
	{
		private AsyncCallback<T> _callback = null;
		
		public ResponseHandler(AsyncCallback<T> callback)
		{
			_callback = callback;
		}
		
		public void handle(Request request, Response response)
		{
			if (null == _callback) return;
			
			try
			{
				if (null == response.getEntity()) throw new Exception("invalid (null) response");
					
				T result = castResult(response.getEntity());
				if (response.getStatus().isSuccess()) 
				{
					_callback.onSuccess(result);
				} 
				else 
				{
					_callback.onFailure(new Throwable("server error " +  response.getStatus().getCode() + ": " + response.getStatus().getDescription()));
				}
			}
			catch (Exception e)
			{
				_callback.onFailure(e);
				//Log.error(description);
				Logger.getLogger(this.getClass().getName()).severe("internal server error: " + (null != e.getMessage() ? e.getMessage() : e.toString()));
			}
		}	
		
		// TODO: handle more media types here		
		// in case the response type does not match the expected result type we will get a bad cast exception here		
		@SuppressWarnings("unchecked")
		private T castResult(Representation representation) throws Exception
		{					
			try
			{
				MediaType mediaType = representation.getMediaType();
				
				if (MediaType.APPLICATION_XML.equals(mediaType))
				{
					DomRepresentation dom = new DomRepresentation(representation);
					return T)dom.getDocument();
				}
				else if (MediaType.APPLICATION_JSON.equals(mediaType))
				{
					JsonRepresentation json = new JsonRepresentation(representation);
					return T)json.getJsonObject();
				}
				else
				{
					return T)representation.getText();
				}	
			}
			catch (ClassCastException e)
			{
				throw new Exception("server response does not match expected result type, response is: \n" + representation.getText());
			}
		}	
	}
	*/
	
	// this is a custom json xss request, using gwt's jsponp hack
	public void getJsonXss(String url, final AsyncCallback<JSONObject> callback)
	{    	    	
    	JsonpRequestBuilder builder = new JsonpRequestBuilder();
    	builder.requestObject(url, new AsyncCallback<JavaScriptObject>() 
    	{			
			public void onSuccess(JavaScriptObject response) 
			{	
				callback.onSuccess(new JSONObject(response));
			}
			
			public void onFailure(Throwable caught) 
			{
				callback.onFailure(caught);
			}
		});
	}
		
}



