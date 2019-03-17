package per.lambert.touchyCanvas.client.touchHelper;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Touch;
import com.google.gwt.event.shared.GwtEvent;

/**
 * Event for handling double tap.
 * 
 * @author LLambert
 *
 */
public class DoubleTapEvent extends GwtEvent<DoubleTapHandler> {

	/**
	 * Information about touch.
	 */
	private TouchInformation touchInformation;

	/**
	 * Get touch information.
	 * 
	 * @return touch information
	 */
	public TouchInformation getTouchInformation() {
		return touchInformation;
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
	private static Type<DoubleTapHandler> doublTapEventType = new Type<DoubleTapHandler>();
	/**
	 * Get event type.
	 * @return event type
	 */
	public static Type<DoubleTapHandler> getType() {
		return doublTapEventType;
	}

	/**
	 * Constructor.
	 * 
	 * @param touchData data about touch
	 * @param targetElement element that was targeted.
	 */
	public DoubleTapEvent(final Touch touchData, final Element targetElement) {
		touchInformation = new TouchInformation(touchData);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Type<DoubleTapHandler> getAssociatedType() {
		return doublTapEventType;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void dispatch(final DoubleTapHandler handler) {
		handler.onDoubleTap(this);
	}

}
