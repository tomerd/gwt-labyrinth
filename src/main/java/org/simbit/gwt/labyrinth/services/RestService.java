package org.simbit.gwt.labyrinth.services; 

import java.util.ArrayList;
import java.util.List;

import org.restlet.client.data.Form;
import org.restlet.client.data.Parameter;
import org.simbit.gwt.labyrinth.provider.IProviderLocator;
import org.simbit.gwt.labyrinth.provider.Provider;
import org.simbit.gwt.labyrinth.provider.ProviderLocator;
import org.simbit.gwt.labyrinth.provider.rest.IRestProvider;

import com.google.gwt.core.client.GWT;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.user.client.rpc.AsyncCallback;

public abstract class RestService
{	
	private String _baseUrl = null;
	
	private IRestProvider _provider = null;
	
	public RestService(String baseUrl)
	{
		_baseUrl = baseUrl;
	}	
				
	protected final <T> void get(String url, AsyncCallback<T> callback)
	{
		getProvider().get(normalizeUrl(url), callback);
	}
	
	protected final <T> void post(String url, Object data, AsyncCallback<T> callback)
	{
		getProvider().post(normalizeUrl(url), callback, data);
	}
	
	protected final <T> void put(String url, Object data, AsyncCallback<T> callback)
	{
		getProvider().put(normalizeUrl(url), callback, data);
	}
	
	protected final <T> void delete(String url, AsyncCallback<T> callback)
	{
		getProvider().delete(normalizeUrl(url), callback);
	}
		
	protected final void getJsonXss(String url, AsyncCallback<JSONObject> callback)
	{
		getProvider().getJsonXss(url, callback);
	}
		
	protected final Object buildForm(String[][] fields)
	{
		List<Parameter> list = new ArrayList<Parameter>();
		
		for (int index=0; index < fields.length; index ++)
		{
			if (2 != fields[index].length) continue;
			list.add(new Parameter(fields[index][0], fields[index][1]));
		}
		
		return (new Form(list));
	}
	
	private String normalizeUrl(String url)
	{
		if (null == _baseUrl) return url;
		if ((_baseUrl.length() > 0) && (url.indexOf(_baseUrl) < 0)) url = _baseUrl + "/" + url;
		return url;
	}
	
	private IRestProvider getProvider()
	{
		// the provider locator can be overriden by using gwt dependency injection (aka deferred binding)
		if (null == _provider) 
		{
			IProviderLocator locator = GWT.create(ProviderLocator.class);
			_provider = (IRestProvider)locator.get(Provider.REST);
		}
		return _provider;
	}	
}
