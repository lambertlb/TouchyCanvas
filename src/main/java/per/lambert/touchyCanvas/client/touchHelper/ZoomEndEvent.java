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
	 * @param startingFinger1 starting finger1 position.
	 * @param startingFinger2 starting finger2 position.
	 * @param targetElement element that was targeted.
	 */
	public ZoomEndEvent(final TouchInformation startingFinger1, final TouchInformation startingFinger2, final TouchEvent targetElement) {
		this.targetElement = TouchHelper.computeTargetElement(targetElement);
		zoomInformation = new ZoomInformation(startingFinger1, startingFinger2, targetElement);
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
