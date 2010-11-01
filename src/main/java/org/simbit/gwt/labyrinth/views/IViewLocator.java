package org.simbit.gwt.labyrinth.views;

import com.google.gwt.user.client.ui.Widget;

public interface IViewLocator 
{
	Widget get(Enum<?> view);
}
