package per.lambert.touchyCanvas.client;

/*
 * Copyright 2010 Google Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

import com.google.gwt.canvas.dom.client.Context;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.CanvasElement;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.PartialSupport;
import com.google.gwt.event.dom.client.TouchCancelHandler;
import com.google.gwt.event.dom.client.TouchEndHandler;
import com.google.gwt.event.dom.client.TouchMoveHandler;
import com.google.gwt.event.dom.client.TouchStartHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.FocusWidget;
import com.google.gwt.user.client.ui.RootPanel;
import com.googlecode.mgwt.dom.client.event.tap.HasTapHandlers;
import com.googlecode.mgwt.dom.client.event.tap.TapEvent;
import com.googlecode.mgwt.dom.client.event.tap.TapHandler;
import com.googlecode.mgwt.dom.client.event.touch.HasTouchHandlers;
import com.googlecode.mgwt.dom.client.event.touch.TouchHandler;
import com.googlecode.mgwt.dom.client.recognizer.longtap.HasLongTapHandlers;
import com.googlecode.mgwt.dom.client.recognizer.longtap.LongTapEvent;
import com.googlecode.mgwt.dom.client.recognizer.longtap.LongTapHandler;
import com.googlecode.mgwt.dom.client.recognizer.pinch.HasPinchHandlers;
import com.googlecode.mgwt.dom.client.recognizer.pinch.PinchEvent;
import com.googlecode.mgwt.dom.client.recognizer.pinch.PinchHandler;
import com.googlecode.mgwt.dom.client.recognizer.swipe.HasSwipeHandlers;
import com.googlecode.mgwt.dom.client.recognizer.swipe.SwipeEndEvent;
import com.googlecode.mgwt.dom.client.recognizer.swipe.SwipeEndHandler;
import com.googlecode.mgwt.dom.client.recognizer.swipe.SwipeMoveEvent;
import com.googlecode.mgwt.dom.client.recognizer.swipe.SwipeMoveHandler;
import com.googlecode.mgwt.dom.client.recognizer.swipe.SwipeStartEvent;
import com.googlecode.mgwt.dom.client.recognizer.swipe.SwipeStartHandler;
import com.googlecode.mgwt.ui.client.widget.touch.GestureUtility;
import com.googlecode.mgwt.ui.client.widget.touch.TouchWidgetImpl;

/**
 * A widget representing a &lt;canvas&gt; element.
 * 
 * This widget may not be supported on all browsers.
 */
@PartialSupport
public class ImageCanvas extends FocusWidget implements HasTouchHandlers, HasTapHandlers, HasPinchHandlers, HasSwipeHandlers, HasLongTapHandlers {
	private static final TouchWidgetImpl impl = GWT.create(TouchWidgetImpl.class);

	protected final GestureUtility gestureUtility;

	/**
	 * Return a new {@link Canvas} if supported, and null otherwise.
	 * 
	 * @return a new {@link Canvas} if supported, and null otherwise
	 */
	public static ImageCanvas createIfSupported() {
		CanvasElement element = Document.get().createCanvasElement();
		return new ImageCanvas(element);
	}

	/**
	 * Wrap an existing canvas element. The element must already be attached to the document. If the element is removed from the document, you must call {@link RootPanel#detachNow(Widget)}. Note: This method can return null if there is no support for canvas by
	 * the current browser.
	 *
	 * @param element the element to wrap
	 * @return the {@link Canvas} widget or null if canvas is not supported by the current browser.
	 */
	public static ImageCanvas wrap(CanvasElement element) {
		if (!isSupported(element)) {
			return null;
		}
		assert Document.get().getBody().isOrHasChild(element);
		ImageCanvas canvas = new ImageCanvas(element);

		// Mark it attached and remember it for cleanup.
		canvas.onAttach();
		RootPanel.detachOnWindowClose(canvas);

		return canvas;
	}

	/**
	 * Runtime check for whether the canvas element is supported in this browser.
	 * 
	 * @return whether the canvas element is supported
	 */
	public static boolean isSupported() {
		return isSupported(Document.get().createCanvasElement());
	}

	private static boolean isSupported(CanvasElement element) {
		return true;
	}

	/**
	 * Protected constructor. Use {@link #createIfSupported()} to create a Canvas.
	 */
	private ImageCanvas(CanvasElement element) {
		setElement(element);
		gestureUtility = new GestureUtility(this);
	}

	/**
	 * Returns the attached Canvas Element.
	 * 
	 * @return the Canvas Element
	 */
	public CanvasElement getCanvasElement() {
		return this.getElement().cast();
	}

	/**
	 * Gets the rendering context that may be used to draw on this canvas.
	 * 
	 * @param contextId the context id as a String
	 * @return the canvas rendering context
	 */
	public Context getContext(String contextId) {
		return getCanvasElement().getContext(contextId);
	}

	/**
	 * Returns a 2D rendering context.
	 * 
	 * This is a convenience method, see {@link #getContext(String)}.
	 * 
	 * @return a 2D canvas rendering context
	 */
	public Context2d getContext2d() {
		return getCanvasElement().getContext2d();
	}

	/**
	 * Gets the height of the internal canvas coordinate space.
	 * 
	 * @return the height, in pixels
	 * @see #setCoordinateSpaceHeight(int)
	 */
	public int getCoordinateSpaceHeight() {
		return getCanvasElement().getHeight();
	}

	/**
	 * Gets the width of the internal canvas coordinate space.
	 * 
	 * @return the width, in pixels
	 * @see #setCoordinateSpaceWidth(int)
	 */
	public int getCoordinateSpaceWidth() {
		return getCanvasElement().getWidth();
	}

	/**
	 * Sets the height of the internal canvas coordinate space.
	 * 
	 * @param height the height, in pixels
	 * @see #getCoordinateSpaceHeight()
	 */
	public void setCoordinateSpaceHeight(int height) {
		getCanvasElement().setHeight(height);
	}

	/**
	 * Sets the width of the internal canvas coordinate space.
	 * 
	 * @param width the width, in pixels
	 * @see #getCoordinateSpaceWidth()
	 */
	public void setCoordinateSpaceWidth(int width) {
		getCanvasElement().setWidth(width);
	}

	/**
	 * Returns a data URL for the current content of the canvas element.
	 * 
	 * @return a data URL for the current content of this element.
	 */
	public String toDataUrl() {
		return getCanvasElement().toDataUrl();
	}

	/**
	 * Returns a data URL for the current content of the canvas element, with a specified type.
	 * 
	 * @param type the type of the data url, e.g., image/jpeg or image/png.
	 * @return a data URL for the current content of this element with the specified type.
	 */
	public String toDataUrl(String type) {
		return getCanvasElement().toDataUrl(type);
	}

	@Override
	public HandlerRegistration addTouchStartHandler(TouchStartHandler handler) {
		return impl.addTouchStartHandler(this, handler);
	}

	@Override
	public HandlerRegistration addTouchMoveHandler(TouchMoveHandler handler) {
		return impl.addTouchMoveHandler(this, handler);
	}

	@Override
	public HandlerRegistration addTouchCancelHandler(TouchCancelHandler handler) {
		return impl.addTouchCancelHandler(this, handler);
	}

	@Override
	public HandlerRegistration addTouchEndHandler(TouchEndHandler handler) {
		return impl.addTouchEndHandler(this, handler);
	}

	@Override
	public HandlerRegistration addTouchHandler(TouchHandler handler) {
		return impl.addTouchHandler(this, handler);
	}

	@Override
	public HandlerRegistration addTapHandler(TapHandler handler) {
		gestureUtility.ensureTapRecognizer();
		return addHandler(handler, TapEvent.getType());
	}

	@Override
	public HandlerRegistration addSwipeStartHandler(SwipeStartHandler handler) {
		gestureUtility.ensureSwipeRecognizer();
		return addHandler(handler, SwipeStartEvent.getType());
	}

	@Override
	public HandlerRegistration addSwipeMoveHandler(SwipeMoveHandler handler) {
		gestureUtility.ensureSwipeRecognizer();
		return addHandler(handler, SwipeMoveEvent.getType());
	}

	@Override
	public HandlerRegistration addSwipeEndHandler(SwipeEndHandler handler) {
		gestureUtility.ensureSwipeRecognizer();
		return addHandler(handler, SwipeEndEvent.getType());
	}

	@Override
	public HandlerRegistration addPinchHandler(PinchHandler handler) {
		gestureUtility.ensurePinchRecognizer(this);
		return addHandler(handler, PinchEvent.getType());
	}

	@Override
	public HandlerRegistration addLongTapHandler(LongTapHandler handler) {
		gestureUtility.ensureLongTapHandler();
		return addHandler(handler, LongTapEvent.getType());
	}
}
