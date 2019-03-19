package per.lambert.touchyCanvas.client.touchHelper;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.TouchEvent;
import com.google.gwt.event.shared.GwtEvent;

/**
 * Event for handling zoom end.
 * 
 * @author LLambert
 *
 */
public class ZoomEndEvent extends GwtEvent<ZoomEndHandler> {


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
	private static Type<ZoomEndHandler> eventType = new Type<ZoomEndHandler>();

	/**
	 * Get event type.
	 * 
	 * @return event type
	 */
	public static Type<ZoomEndHandler> getType() {
		return eventType;
	}

	/**
	 * Constructor.
	 * 
	 * @param targetElement element that was targeted.
	 */
	public ZoomEndEvent(final TouchEvent targetElement) {
		this.targetElement = TouchHelper.computeTargetElement(targetElement);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Type<ZoomEndHandler> getAssociatedType() {
		return eventType;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void dispatch(final ZoomEndHandler handler) {
		handler.onZoomEnd(this);
	}

}
