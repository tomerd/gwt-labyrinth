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

package org.simbit.gwt.labyrinth.services; 

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
		get(url, false, callback);
	}
	
	protected final <T> void get(String url, boolean secure, AsyncCallback<T> callback)
	{
		getProvider().get(normalizeUrl(url, secure), callback);
	}
	
	protected final <T> void post(String url, Object data, AsyncCallback<T> callback)
	{
		post(url, data, false, callback);
	}
	
	protected final <T> void post(String url, Object data, boolean secure, AsyncCallback<T> callback)
	{
		getProvider().post(normalizeUrl(url, secure), data, callback);
	}
	
	protected final <T> void put(String url, Object data, AsyncCallback<T> callback)
	{
		put(url, data, false, callback);
	}
	
	protected final <T> void put(String url, Object data, boolean secure, AsyncCallback<T> callback)
	{
		getProvider().put(normalizeUrl(url, secure), data, callback);
	}
	
	protected final <T> void delete(String url, AsyncCallback<T> callback)
	{
		delete(url, false, callback);
	}
	
	protected final <T> void delete(String url, boolean secure, AsyncCallback<T> callback)
	{
		getProvider().delete(normalizeUrl(url, secure), callback);
	}
		
	protected final void getJsonXss(String url, boolean secure, AsyncCallback<JSONObject> callback)
	{
		getProvider().getJsonXss(normalizeUrl(url, secure), callback);
	}
	
	protected final String normalizeUrl(String url)
	{
		return normalizeUrl(url, false);
	}
	
	protected final String normalizeUrl(String url, boolean secure)
	{
		// FIXME: handle secure (HTTPS) requests here
		if (null == _baseUrl) return url;
		if ((_baseUrl.length() > 0) && (url.indexOf(_baseUrl) < 0)) url = _baseUrl + "/" + url;
		return url;
	}
	
	private IRestProvider getProvider()
	{
		// the provider locator can be overridden by using gwt dependency injection (aka deferred binding)
		if (null == _provider) 
		{
			IProviderLocator locator = GWT.create(ProviderLocator.class);
			_provider = (IRestProvider)locator.get(Provider.REST);
		}
		return _provider;
	}	
}
