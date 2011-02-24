package org.simbit.gwt.labyrinth.provider.rest.gwt;

import java.util.logging.Logger;

import org.simbit.gwt.labyrinth.provider.rest.HttpMethod;
import org.simbit.gwt.labyrinth.provider.rest.IRestProvider;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.Response;
import com.google.gwt.http.client.URL;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.jsonp.client.JsonpRequestBuilder;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.XMLParser;

public class GwtRestImpl implements IRestProvider 
{
	public <T> void get(String url, AsyncCallback<T> callback)
	{
		send(url, callback, HttpMethod.GET);
	}
	
	public <T> void post(String url, AsyncCallback<T> callback, Object data)
	{
		send(url, callback, HttpMethod.POST, data);
	}
	
	public <T> void put(String url, AsyncCallback<T> callback, Object data)
	{
		send(url, callback, HttpMethod.PUT, data);
	}

	public <T> void delete(String url, AsyncCallback<T> callback)
	{
		send(url, callback, HttpMethod.DELETE);
	}
	
	public <T> void send(String url, AsyncCallback<T> callback)
	{
		send(url, callback, HttpMethod.UNKNOWN, null);
	}
		
	public <T> void send(String url, AsyncCallback<T> callback, HttpMethod method)
	{
		send(url, callback, method, null);
	}
	
	public <T> void send(String url, AsyncCallback<T> callback, Object data)
	{		
		send(url, callback, HttpMethod.UNKNOWN, data);	
	}	
		
	public <T> void send(String url, AsyncCallback<T> callback, HttpMethod method, Object data)
	{								
		if (null == url) return;
		
		// common sense defaults
		if (HttpMethod.UNKNOWN == method) method = (null != data) ? HttpMethod.POST : HttpMethod.GET;
								
		try
		{	
			if (HttpMethod.GET == method)
			{
				Logger.getLogger(this.getClass().getName()).finest("requesting " + url);
			}
			else
			{
				Logger.getLogger(this.getClass().getName()).finest("posting to " + url + "\n" + data);
			}
			
			RequestBuilder builder = new RequestBuilder(getHttpMethod(method), url);
			if (isFormData(data)) builder.setHeader("Content-type", "application/x-www-form-urlencoded");
			String formattedData = formatData(data);
            builder.setRequestData(formattedData);           
			builder.setCallback(new ResponseHandler<T>(callback));
			builder.send();
		}
		catch(Exception e)
		{
			Logger.getLogger(this.getClass().getName()).severe("internal error performing request " + e);
		} 
	}
	
	private RequestBuilder.Method getHttpMethod(HttpMethod method) throws Exception
	{
		switch (method)
		{
			case GET: return RequestBuilder.GET;
			case POST: return RequestBuilder.POST;
			case PUT: return RequestBuilder.PUT;
			case DELETE: return RequestBuilder.DELETE;
			case HEAD: return RequestBuilder.HEAD;
			default: throw new Exception("unknown http method " + method);		
		}
	}
	
	private Boolean isFormData(Object data)
	{
		// TODO: support map, etc
		if (data instanceof String[][]) return true;
		if (data instanceof Object[][]) return true;
		return false;
		
	}
	
	private String formatData(Object data)
	{
		if (data instanceof String)
		{			
			// TODO: test this scenario
			return (String)data;
		}
		else if (data instanceof String[][])
		{
			return buildForm((String[][])data);
		}
		else if (data instanceof Object[][])
		{
			return buildForm((Object[][])data);
		}
		// TODO: support map, etc
		else if (data instanceof JSONObject)
		{	
			return ((JSONObject)data).toString();
		}
		else if (data instanceof Document)
		{
			// TODO: test this scenario
			return ((Document)data).toString();
		}
		else
		{			
			return null;
		}
	}
	
	private final String buildForm(Object[][] fields)
	{
		StringBuilder builder = new StringBuilder();
		
		for (int index=0; index < fields.length; index ++)
		{
			if (2 != fields[index].length) continue;
			if (index > 0) builder.append("&");
			builder.append(URL.encode(fields[index][0].toString()));
			builder.append("=");
			builder.append(URL.encode(fields[index][1].toString()));
		}
		
		return builder.toString();
	}
	
	private class ResponseHandler<T> implements RequestCallback
	{
		private AsyncCallback<T> _callback = null;
		
		public ResponseHandler(AsyncCallback<T> callback)
		{
			_callback = callback;
		}

		public void onResponseReceived(Request request, Response response) 
		{
			if (null == _callback) return;
			
			try
			{									
				if (200 == response.getStatusCode()) 
				{
					T result = castReponse(response);
					_callback.onSuccess(result);
				} 
				else 
				{
					_callback.onFailure(new Throwable("server error " +  response.getStatusCode() + ": " + response.getStatusText()));
				}
			}
			catch (Exception e)
			{
				_callback.onFailure(e);
				Logger.getLogger(this.getClass().getName()).severe("internal server error: " + (null != e.getMessage() ? e.getMessage() : e.toString()));
			}			
		}

		public void onError(Request request, Throwable exception) 
		{
			if (null == _callback) return;
			_callback.onFailure(exception);
		}		
		
		// FIXME: find a nicer way to do this, perhaps check content type headers?		
		// in case the response type does not match the expected result type we will get a bad cast exception here		
		@SuppressWarnings("unchecked")
		private T castReponse(Response response) throws Exception
		{					
			try
			{
				JSONValue json = null;
				Document xml = null;
					
				try
				{
					json = JSONParser.parseStrict(response.getText());
				}
				catch(Exception e)
				{	
					json = null;
				}
				
				if (null != json) return (T)json;				
				
				try
				{
					xml = XMLParser.parse(response.getText());
				}
				catch(Exception e)
				{
					xml = null;
				}				
				
				if (null != xml) return (T)xml;				
				
				return (T)response.getText();
			}
			catch (ClassCastException e)
			{
				throw new Exception("server response does not match expected result type, response is: \n" + response.getText());
			}
		}			
	}
	
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
