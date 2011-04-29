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

package org.simbit.gwt.labyrinth.provider.rest;

import org.simbit.gwt.labyrinth.provider.IProvider;

import com.google.gwt.json.client.JSONObject;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface IRestProvider extends IProvider
{
	<T> void get(String url, AsyncCallback<T> callback);	
	<T> void post(String url, Object data, AsyncCallback<T> callback);	
	<T> void put(String url, Object data, AsyncCallback<T> callback);	
	<T> void delete(String url, AsyncCallback<T> callback);
	<T> void send(String url, AsyncCallback<T> callback);
	<T> void send(String url, HttpMethod method, AsyncCallback<T> callback);
	<T> void send(String url, Object data, AsyncCallback<T> callback);
	<T> void send(String url, HttpMethod method, Object data, AsyncCallback<T> callback);
	void getJsonXss(String url, final AsyncCallback<JSONObject> callback);
}
