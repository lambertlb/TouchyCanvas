package per.lambert.touchyCanvas.client.touchHelper;

import java.util.Date;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.HasAllTouchHandlers;
import com.google.gwt.event.dom.client.TouchCancelEvent;
import com.google.gwt.event.dom.client.TouchCancelHandler;
import com.google.gwt.event.dom.client.TouchEndEvent;
import com.google.gwt.event.dom.client.TouchEndHandler;
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
 * It will analyze evernt to see if the represent higher level methods lime pan and zoom.
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
	private enum InteractionType {
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
	private InteractionType interaction = InteractionType.INVALID;

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
		TouchyCanvas.addMessage("Touch Start ");
	}

	/**
	 * Received touch move event.
	 * 
	 * @param event touch move event.
	 */
	public void doTouchMove(final TouchMoveEvent event) {
		TouchyCanvas.addMessage("Touch Move ");
	}

	/**
	 * Received touch end event.
	 * 
	 * @param event touch end event.
	 */
	public void doTouchEnd(final TouchEndEvent event) {
		TouchyCanvas.addMessage("Touch End ");
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
			Element targetElement = null;
			if (event.getNativeEvent() != null) {
				targetElement = event.getNativeEvent().getEventTarget().<Element>cast();
			}
			widgetToTouch.fireEvent(new DoubleTapEvent(event.getChangedTouches().get(0), targetElement));
		}
		if (amountOfFingers == 1) {
			lastTouchStart = time;
		}
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
}
