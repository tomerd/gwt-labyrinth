package org.simbit.gwt.labyrinth.provider.rest;

import org.simbit.gwt.labyrinth.provider.IProvider;

import com.google.gwt.json.client.JSONObject;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface IRestProvider extends IProvider
{
	<T> void get(String url, AsyncCallback<T> callback);	
	<T> void post(String url, AsyncCallback<T> callback, Object data);	
	<T> void put(String url, AsyncCallback<T> callback, Object data);	
	<T> void delete(String url, AsyncCallback<T> callback);
	<T> void send(String url, AsyncCallback<T> callback);
	<T> void send(String url, AsyncCallback<T> callback, HttpMethod method);
	<T> void send(String url, AsyncCallback<T> callback, Object data);
	<T> void send(String url, AsyncCallback<T> callback, HttpMethod method, Object data);
	void getJsonXss(String url, final AsyncCallback<JSONObject> callback);
}
