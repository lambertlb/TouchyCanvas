/*
 * Copyright (C) 2019 Leon Lambert.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package per.lambert.touchyCanvas.client.touchHelper;

import com.google.gwt.dom.client.Touch;

/**
 * Information about touch.
 * 
 * @author LLambert
 *
 */
public class TouchInformation {
	/**
	 * the touch x-position within the browser document.
	 */
	private int pageX;
	/**
	 * the touch y-position within the browser document.
	 */
	private int pageY;
	/**
	 * the touch x-position within the browser window's client area.
	 */
	private int clientX;
	/**
	 * the touch y-position within the browser window's client area.
	 */
	private int clientY;
	/**
	 * the touch x-position on the user's display.
	 */
	private int screenX;
	/**
	 * the touch y-position on the user's display.
	 */
	private int screenY;

	/**
	 * Constructor.
	 * 
	 * @param touchInformation touch information
	 */
	public TouchInformation(final Touch touchInformation) {
		this.pageX = touchInformation.getPageX();
		this.pageY = touchInformation.getPageY();
		this.clientX = touchInformation.getClientX();
		this.clientY = touchInformation.getClientY();
		this.screenX = touchInformation.getScreenX();
		this.screenY = touchInformation.getScreenY();
	}

	/**
	 * Constructor.
	 */
	private TouchInformation() {
	}

	/**
	 * {@inheritDoc}
	 */
	public TouchInformation clone() {
		TouchInformation clone = new TouchInformation();
		clone.pageX = pageX;
		clone.pageY = pageY;
		clone.clientX = clientX;
		clone.clientY = clientY;
		clone.screenX = screenX;
		clone.screenY = screenY;
		return (clone);
	}

	/**
	 * Get the touch x-position within the browser document.
	 * 
	 * @return the touch x-position within the browser document.
	 */
	public int getPageX() {
		return pageX;
	}

	/**
	 * Get the touch y-position within the browser document.
	 * 
	 * @return the touch y-position within the browser document.
	 */
	public int getPageY() {
		return pageY;
	}

	/**
	 * get the touch x-position within the browser window's client area.
	 * 
	 * @return the touch x-position within the browser window's client area.
	 */
	public int getClientX() {
		return clientX;
	}

	/**
	 * get the touch y-position within the browser window's client area.
	 * 
	 * @return the touch y-position within the browser window's client area.
	 */
	public int getClientY() {
		return clientY;
	}

	/**
	 * Get the touch x-position on the user's display.
	 * 
	 * @return the touch x-position on the user's display.
	 */
	public int getScreenX() {
		return screenX;
	}

	/**
	 * Get the touch y-position on the user's display.
	 * 
	 * @return the touch y-position on the user's display.
	 */
	public int getScreenY() {
		return screenY;
	}
}
