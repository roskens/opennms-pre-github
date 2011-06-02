/*
 * This file is part of the OpenNMS(R) Application.
 *
 * OpenNMS(R) is Copyright (C) 2011 The OpenNMS Group, Inc.  All rights reserved.
 * OpenNMS(R) is a derivative work, containing both original code, included code and modified
 * code that was published under the GNU General Public License. Copyrights for modified
 * and included code are below.
 *
 * OpenNMS(R) is a registered trademark of The OpenNMS Group, Inc.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 *
 * For more information contact:
 *      OpenNMS Licensing       <license@opennms.org>
 *      http://www.opennms.org/
 *      http://www.opennms.com/
 */

package org.opennms.gwt.web.ui.asset.client.tools;

import java.util.Iterator;

import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.event.logical.shared.OpenEvent;
import com.google.gwt.event.logical.shared.OpenHandler;
import com.google.gwt.uibinder.client.UiConstructor;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author <a href="mailto:MarkusNeumannMarkus@gmail.com">Markus Neumann</a>
 * 
 */
public class DisclosurePanelCookie extends Composite implements HasWidgets {

	private DisclosurePanel panel = new DisclosurePanel();

	@UiConstructor
	public DisclosurePanelCookie(final String name) {
		
		panel.setStyleName("DisclosurePanelCookie");
		panel.setAnimationEnabled(true);

		if (Cookies.isCookieEnabled()) {
			// prepare Cookie if not already set
			if (Cookies.getCookie(name + "Open") == null) {
				Cookies.setCookie(name + "Open", "true");
			}

			// check cookie and set open/close by cookie-value
			if (Cookies.getCookie(name + "Open").equals("true")) {
				panel.setOpen(true);
			} else {
				panel.setOpen(false);
			}

			// add openhandler that sets open/true to cookie
			panel.addOpenHandler(new OpenHandler<DisclosurePanel>() {
				@Override
				public void onOpen(OpenEvent<DisclosurePanel> event) {
					Cookies.setCookie(name + "Open", "true");
				}
			});

			// add closehandler that sets close/flase to cookie
			panel.addCloseHandler(new CloseHandler<DisclosurePanel>() {
				@Override
				public void onClose(CloseEvent<DisclosurePanel> event) {
					Cookies.setCookie(name + "Open", "false");
				}
			});
		}
		initWidget(panel);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.google.gwt.user.client.ui.HasWidgets#add(com.google.gwt.user.client
	 * .ui.Widget)
	 */
	@Override
	public void add(Widget widget) {
		if (panel.getHeader() == null) {
			panel.setHeader(widget);
		} else {
			panel.setContent(widget);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.google.gwt.user.client.ui.HasWidgets#clear()
	 */
	@Override
	public void clear() {
		panel.clear();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.google.gwt.user.client.ui.HasWidgets#iterator()
	 */
	@Override
	public Iterator<Widget> iterator() {
		return panel.iterator();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.google.gwt.user.client.ui.HasWidgets#remove(com.google.gwt.user.client
	 * .ui.Widget)
	 */
	@Override
	public boolean remove(Widget widget) {
		return panel.remove(widget);
	}
}
