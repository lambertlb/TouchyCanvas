package per.lambert.touchyCanvas.client.touchHelper;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Touch;
import com.google.gwt.event.shared.GwtEvent;

/**
 * Event for handling pan.
 * 
 * @author LLambert
 *
 */
public class PanEvent extends GwtEvent<PanHandler> {

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
	private static Type<PanHandler> eventType = new Type<PanHandler>();

	/**
	 * Get event type.
	 * 
	 * @return event type
	 */
	public static Type<PanHandler> getType() {
		return eventType;
	}

	/**
	 * Constructor.
	 * 
	 * @param touchData data about touch
	 * @param targetElement element that was targeted.
	 */
	public PanEvent(final Touch touchData, final Element targetElement) {
		touchInformation = new TouchInformation(touchData);
		this.targetElement = targetElement;
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Type<PanHandler> getAssociatedType() {
		return eventType;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void dispatch(final PanHandler handler) {
		handler.onPan(this);
	}

}
