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

import java.util.Date;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.dom.client.Touch;
import com.google.gwt.event.dom.client.HasAllTouchHandlers;
import com.google.gwt.event.dom.client.TouchCancelEvent;
import com.google.gwt.event.dom.client.TouchCancelHandler;
import com.google.gwt.event.dom.client.TouchEndEvent;
import com.google.gwt.event.dom.client.TouchEndHandler;
import com.google.gwt.event.dom.client.TouchEvent;
import com.google.gwt.event.dom.client.TouchMoveEvent;
import com.google.gwt.event.dom.client.TouchMoveHandler;
import com.google.gwt.event.dom.client.TouchStartEvent;
import com.google.gwt.event.dom.client.TouchStartHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Widget;

/**
 * Helper to encapsulate functions for managing touches.
 * 
 * It will analyze touch events to see if they represent higher level methods like pan and zoom.
 * 
 * @author LLambert
 *
 */
public class TouchHelper {
	/**
	 * Types of interactions.
	 * 
	 * @author LLambert
	 *
	 */
	private enum Action {
		/**
		 * Invalid interaction.
		 */
		INVALID,
		/**
		 * We are zooming.
		 */
		ZOOM,
		/**
		 * We are panning.
		 */
		PAN
	}

	/**
	 * Amount of time between touches for detecting double tap
	 */
	private static final int DoubleTapDetectTime = 300;
	/**
	 * Widget that was touched.
	 */
	private Widget widgetToTouch;
	/**
	 * Is this the first move after touch.
	 */
	private boolean firstMove = false;
	/**
	 * Amount of fingers in touch.
	 */
	private int amountOfFingers;
	/**
	 * Time of last touch start.
	 */
	private long lastTouchStart;
	/**
	 * Current interaction.
	 */
	private Action currentAction = Action.INVALID;
	/**
	 * Information for where finger 1 first touched screen.
	 */
	private TouchInformation startingFinger1;
	/**
	 * Information for where finger 2 first touched screen.
	 */
	private TouchInformation startingFinger2;

	/**
	 * Constructor.
	 * 
	 * @param widgetToTouch widget with base touch events.
	 */
	public TouchHelper(final Widget widgetToTouch) {
		this.widgetToTouch = widgetToTouch;
		if (!(widgetToTouch instanceof HasAllTouchHandlers)) {
			addTouchDOMHandlers(widgetToTouch);
			return;
		}
		HasAllTouchHandlers touchHandlers = (HasAllTouchHandlers) widgetToTouch;

		touchHandlers.addTouchStartHandler(new TouchStartHandler() {
			@Override
			public void onTouchStart(final TouchStartEvent event) {
				doTouchStart(event);
			}
		});
		touchHandlers.addTouchEndHandler(new TouchEndHandler() {
			@Override
			public void onTouchEnd(final TouchEndEvent event) {
				doTouchEnd(event);
			}
		});
		touchHandlers.addTouchMoveHandler(new TouchMoveHandler() {
			@Override
			public void onTouchMove(final TouchMoveEvent event) {
				doTouchMove(event);
			}
		});
		touchHandlers.addTouchCancelHandler(new TouchCancelHandler() {
			@Override
			public void onTouchCancel(final TouchCancelEvent event) {
				doTouchCancel(event);
			}
		});
	}

	/**
	 * Add touch handlers.
	 * @param widgetToTouch widget that accepts touches.
	 */
	private void addTouchDOMHandlers(final Widget widgetToTouch) {
		widgetToTouch.addDomHandler(new TouchStartHandler() {
			@Override
			public void onTouchStart(final TouchStartEvent event) {
				doTouchStart(event);
			}
		}, TouchStartEvent.getType());
		widgetToTouch.addDomHandler(new TouchEndHandler() {
			@Override
			public void onTouchEnd(final TouchEndEvent event) {
				doTouchEnd(event);
			}
		}, TouchEndEvent.getType());
		widgetToTouch.addDomHandler(new TouchMoveHandler() {
			@Override
			public void onTouchMove(final TouchMoveEvent event) {
				doTouchMove(event);
			}
		}, TouchMoveEvent.getType());
		widgetToTouch.addDomHandler(new TouchCancelHandler() {
			@Override
			public void onTouchCancel(final TouchCancelEvent event) {
				doTouchCancel(event);
			}
		}, TouchCancelEvent.getType());
	}

	/**
	 * Received touch start event.
	 * 
	 * @param event touch start event.
	 */
	public void doTouchStart(final TouchStartEvent event) {
		firstMove = true;
		amountOfFingers = event.getTouches().length();
		detectDoubleTap(event);
	}

	/**
	 * Received touch move event.
	 * 
	 * @param event touch move event.
	 */
	public void doTouchMove(final TouchMoveEvent event) {
		if (firstMove) {
			getStartingTouches(event);
			computeAction(event);
		} else {
			if (currentAction == Action.PAN) {
				widgetToTouch.fireEvent(new PanEvent(event.getTouches().get(0), computeTargetElement(event)));
			} else if (currentAction == Action.ZOOM) {
				widgetToTouch.fireEvent(new ZoomEvent(startingFinger1, startingFinger2, event));
			}
		}
		firstMove = false;
		cancelEvent(event.getNativeEvent());
	}

	/**
	 * Get starting finger positions.
	 * 
	 * @param event with data
	 */
	private void getStartingTouches(final TouchMoveEvent event) {
		startingFinger1 = new TouchInformation(event.getTouches().get(0));
		startingFinger2 = null;
		if (event.getTouches().length() > 1) {
			startingFinger2 = new TouchInformation(event.getTouches().get(1));
		}
	}

	/**
	 * Figure out what action needs to be done.
	 * 
	 * @param event with data
	 */
	@SuppressWarnings("rawtypes")
	private void computeAction(final TouchEvent event) {
		Action newAction = Action.INVALID;
		if (amountOfFingers == 2) {
			newAction = Action.ZOOM;
		} else if (amountOfFingers == 1) {
			newAction = Action.PAN;
		}
		setAction(newAction, event);
	}

	/**
	 * Transition to new action.
	 * 
	 * @param newAction action we are moving to
	 * @param event with data
	 */
	@SuppressWarnings("rawtypes")
	private void setAction(final Action newAction, final TouchEvent event) {
		if (newAction == currentAction) {
			return;
		}
		closeoutOldAction(event);
		startupNewAction(newAction, event);
		currentAction = newAction;
	}

	/**
	 * Startup new action.
	 * 
	 * @param newAction to start
	 * @param event with data
	 */
	@SuppressWarnings("rawtypes")
	private void startupNewAction(final Action newAction, final TouchEvent event) {
		if (newAction == Action.PAN) {
			widgetToTouch.fireEvent(new PanStartEvent(((Touch) event.getTouches().get(0)), computeTargetElement(event)));
		} else if (newAction == Action.ZOOM) {
			widgetToTouch.fireEvent(new ZoomStartEvent(startingFinger1, startingFinger2, event));
		}
	}

	/**
	 * Close out old action.
	 * 
	 * @param event with data
	 */
	@SuppressWarnings("rawtypes")
	private void closeoutOldAction(final TouchEvent event) {
		if (currentAction == Action.PAN) {
			widgetToTouch.fireEvent(new PanEndEvent(computeTargetElement(event)));
		} else if (currentAction == Action.ZOOM) {
			widgetToTouch.fireEvent(new ZoomEndEvent(event));
		}
	}

	/**
	 * Received touch end event.
	 * 
	 * @param event touch end event.
	 */
	public void doTouchEnd(final TouchEndEvent event) {
		setAction(Action.INVALID, event);
	}

	/**
	 * Received touch cancel event.
	 * 
	 * @param event touch cancel event.
	 */
	public void doTouchCancel(final TouchCancelEvent event) {
		cancelEvent(event.getNativeEvent());
	}

	/**
	 * See if this is a double tap.
	 * 
	 * @param event with information
	 */
	private void detectDoubleTap(final TouchStartEvent event) {
		long time = new Date().getTime();
		if (amountOfFingers > 1) {
			lastTouchStart = 0;
		}
		if (time - lastTouchStart < DoubleTapDetectTime) {
			cancelEvent(event.getNativeEvent());
			widgetToTouch.fireEvent(new DoubleTapEvent(event.getTouches().get(0), computeTargetElement(event)));
		}
		if (amountOfFingers == 1) {
			lastTouchStart = time;
		}
	}

	/**
	 * Compute target element.
	 * 
	 * @param event with target
	 * @return target element or null
	 */
	@SuppressWarnings("rawtypes")
	public static Element computeTargetElement(final TouchEvent event) {
		Element targetElement = null;
		if (event.getNativeEvent() != null) {
			targetElement = event.getNativeEvent().getEventTarget().<Element>cast();
		}
		return targetElement;
	}

	/**
	 * Cancel further handling of event.
	 * 
	 * @param event to cancel
	 */
	private void cancelEvent(final NativeEvent event) {
		if (event != null) {
			event.stopPropagation();
			event.preventDefault();
		}
	}

	/**
	 * Add handler to target.
	 * 
	 * @param handler to add
	 * @return registration
	 */
	public HandlerRegistration addDoubleTapHandler(final DoubleTapHandler handler) {
		return (widgetToTouch.addHandler(handler, DoubleTapEvent.getType()));
	}

	/**
	 * Add handler to target.
	 * 
	 * @param handler to add
	 * @return registration
	 */
	public HandlerRegistration addPanStartHandler(final PanStartHandler handler) {
		return (widgetToTouch.addHandler(handler, PanStartEvent.getType()));
	}

	/**
	 * Add handler to target.
	 * 
	 * @param handler to add
	 * @return registration
	 */
	public HandlerRegistration addPanEndHandler(final PanEndHandler handler) {
		return (widgetToTouch.addHandler(handler, PanEndEvent.getType()));
	}

	/**
	 * Add handler to target.
	 * 
	 * @param handler to add
	 * @return registration
	 */
	public HandlerRegistration addPanHandler(final PanHandler handler) {
		return (widgetToTouch.addHandler(handler, PanEvent.getType()));
	}

	/**
	 * Add handler to target.
	 * 
	 * @param handler to add
	 * @return registration
	 */
	public HandlerRegistration addZoomStartHandler(final ZoomStartHandler handler) {
		return (widgetToTouch.addHandler(handler, ZoomStartEvent.getType()));
	}

	/**
	 * Add handler to target.
	 * 
	 * @param handler to add
	 * @return registration
	 */
	public HandlerRegistration addZoomEndHandler(final ZoomEndHandler handler) {
		return (widgetToTouch.addHandler(handler, ZoomEndEvent.getType()));
	}

	/**
	 * Add handler to target.
	 * 
	 * @param handler to add
	 * @return registration
	 */
	public HandlerRegistration addZoomHandler(final ZoomHandler handler) {
		return (widgetToTouch.addHandler(handler, ZoomEvent.getType()));
	}
}
