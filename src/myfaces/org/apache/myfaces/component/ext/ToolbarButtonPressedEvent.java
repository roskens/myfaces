/**
 * MyFaces - the free JSF implementation
 * Copyright (C) 2003, 2004  The MyFaces Team (http://myfaces.sourceforge.net)
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */
package net.sourceforge.myfaces.component.ext;

import javax.faces.component.UIComponent;
import javax.faces.event.FacesEvent;
import javax.faces.event.FacesListener;

/**
 * @author Dimitry D'hondt
 *
 * This event is fired for a button on the toolbar that is pressed.
 * It indicated the index of the button that was pressed.
 */
public class ToolbarButtonPressedEvent extends FacesEvent {

	private ToolbarButton button = null;

	public ToolbarButtonPressedEvent(UIComponent toolbar) {
		super(toolbar);
	}

	/**
	 * @see javax.faces.event.FacesEvent#isAppropriateListener(javax.faces.event.FacesListener)
	 */
	public boolean isAppropriateListener(FacesListener listener) {
		return (listener instanceof ToolbarButtonPressedListener);
	}

	/**
	 * @see javax.faces.event.FacesEvent#processListener(javax.faces.event.FacesListener)
	 */
	public void processListener(FacesListener listener) {
		ToolbarButtonPressedListener l = (ToolbarButtonPressedListener) listener;
		l.buttonPressed(this);
	}
	/**
	 * @return
	 */
	public ToolbarButton getButton() {
		return button;
	}

	/**
	 * @param i
	 */
	public void setButton(ToolbarButton button) {
		this.button = button;
	}
}
