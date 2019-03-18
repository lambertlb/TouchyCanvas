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
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Widget;

import per.lambert.touchyCanvas.client.TouchyCanvas;

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
	 * Time of last start.
	 */
	private long lastTouchStart;
	/**
	 * Current interaction.
	 */
	private Action currentAction = Action.INVALID;

	/**
	 * Constructor.
	 * 
	 * @param widgetToTouch widget with base touch events.
	 */
	public TouchHelper(final Widget widgetToTouch) {
		HasAllTouchHandlers touchHandlers;
		if (!(widgetToTouch instanceof HasAllTouchHandlers)) {
			Window.alert("Does not handle touches");
			return;
		}
		this.widgetToTouch = widgetToTouch;
		touchHandlers = (HasAllTouchHandlers) widgetToTouch;

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
			computeAction(event);
		} else {
			if (currentAction == Action.PAN) {
				widgetToTouch.fireEvent(new PanEvent(event.getChangedTouches().get(0), computeTargetElement(event.getNativeEvent())));
				cancelEvent(event.getNativeEvent());
			}
		}
		firstMove = false;
	}

	/**
	 * Figure out what action needs to be done.
	 * 
	 * @param event with data
	 */
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
	private void startupNewAction(final Action newAction, final TouchEvent event) {
		if (newAction == Action.PAN) {
			widgetToTouch.fireEvent(new PanStartEvent(((Touch) event.getChangedTouches().get(0)), computeTargetElement(event.getNativeEvent())));
			cancelEvent(event.getNativeEvent());
		}
	}

	/**
	 * Close out old action.
	 * 
	 * @param event with data
	 */
	private void closeoutOldAction(final TouchEvent event) {
		if (currentAction == Action.PAN) {
			widgetToTouch.fireEvent(new PanEndEvent(((Touch) event.getChangedTouches().get(0)), computeTargetElement(event.getNativeEvent())));
		}
	}

	/**
	 * Received touch end event.
	 * 
	 * @param event touch end event.
	 */
	public void doTouchEnd(final TouchEndEvent event) {
		amountOfFingers = event.getTouches().length();
		computeAction(event);
	}

	/**
	 * Received touch cancel event.
	 * 
	 * @param event touch cancel event.
	 */
	public void doTouchCancel(final TouchCancelEvent event) {
		TouchyCanvas.addMessage("Touch Cancel ");
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
		if (time - lastTouchStart < 300) {
			cancelEvent(event.getNativeEvent());
			widgetToTouch.fireEvent(new DoubleTapEvent(event.getChangedTouches().get(0), computeTargetElement(event.getNativeEvent())));
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
	private Element computeTargetElement(final NativeEvent event) {
		Element targetElement = null;
		if (event != null) {
			targetElement = event.getEventTarget().<Element>cast();
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
}
