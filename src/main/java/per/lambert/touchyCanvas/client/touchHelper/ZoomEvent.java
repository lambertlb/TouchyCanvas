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

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.TouchEvent;
import com.google.gwt.event.shared.GwtEvent;

/**
 * Event for handling zoom.
 * 
 * @author LLambert
 *
 */
public class ZoomEvent extends GwtEvent<ZoomHandler> {

	/**
	 * Information about zoom operation.
	 */
	private ZoomInformation zoomInformation;
	
	/**
	 * Information about zoom operation.
	 * @return zoom information
	 */
	public ZoomInformation getZoomInformation() {
		return zoomInformation;
	}

	/**
	 * Element that was targeted.
	 */
	private Element targetElement;

	/**
	 * Get target element.
	 * 
	 * @return target element.
	 */
	public Element getTargetElement() {
		return targetElement;
	}

	/**
	 * Type of event.
	 */
	private static Type<ZoomHandler> eventType = new Type<ZoomHandler>();

	/**
	 * Get event type.
	 * 
	 * @return event type
	 */
	public static Type<ZoomHandler> getType() {
		return eventType;
	}

	/**
	 * Constructor.
	 * 
	 * @param startingFinger1 starting finger1 position.
	 * @param startingFinger2 starting finger2 position.
	 * @param targetElement element that was targeted.
	 */
	@SuppressWarnings("rawtypes")
	public ZoomEvent(final TouchInformation startingFinger1, final TouchInformation startingFinger2, final TouchEvent targetElement) {
		this.targetElement = TouchHelper.computeTargetElement(targetElement);
		zoomInformation = new ZoomInformation(startingFinger1, startingFinger2, targetElement);
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Type<ZoomHandler> getAssociatedType() {
		return eventType;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void dispatch(final ZoomHandler handler) {
		handler.onZoom(this);
	}

}
