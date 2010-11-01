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

package org.simbit.gwt.labyrinth.provider;

import org.simbit.gwt.labyrinth.provider.rest.restlet.RestletRestImpl;

// this class could be overriden using gwt dependency injection (aka deferred binding)
public final class ProviderLocator implements IProviderLocator
{		
	public IProvider get(Enum<?> provider)
	{
		switch ((Provider)provider)
		{ 
			case REST: return new RestletRestImpl();
			default: return null;
		}
	}
}
