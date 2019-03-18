package per.lambert.touchyCanvas.client.touchHelper;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Touch;
import com.google.gwt.event.dom.client.TouchEvent;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.GwtEvent.Type;

/**
 * Event for handling zoom start.
 * 
 * @author LLambert
 *
 */
public class ZoomStartEvent extends GwtEvent<ZoomStartHandler> {

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
	 * Type of event.
	 */
	private static Type<ZoomStartHandler> eventType = new Type<ZoomStartHandler>();

	/**
	 * Get event type.
	 * 
	 * @return event type
	 */
	public static Type<ZoomStartHandler> getType() {
		return eventType;
	}

	/**
	 * Constructor.
	 * 
	 * @param startingFinger1 starting finger1 position.
	 * @param startingFinger2 starting finger2 position.
	 * @param targetElement element that was targeted.
	 */
	public ZoomStartEvent(final TouchInformation startingFinger1, final TouchInformation startingFinger2, final TouchEvent targetElement) {
		this.targetElement = TouchHelper.computeTargetElement(targetElement);
		zoomInformation = new ZoomInformation(startingFinger1, startingFinger2, targetElement);
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Type<ZoomStartHandler> getAssociatedType() {
		return eventType;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void dispatch(final ZoomStartHandler handler) {
		handler.onZoomStart(this);
	}

}
