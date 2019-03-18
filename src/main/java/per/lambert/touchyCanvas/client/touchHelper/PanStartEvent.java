package per.lambert.touchyCanvas.client.touchHelper;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Touch;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.GwtEvent.Type;

/**
 * Event for handling pan start.
 * 
 * @author LLambert
 *
 */
public class PanStartEvent extends GwtEvent<PanStartHandler> {

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
	private static Type<PanStartHandler> eventType = new Type<PanStartHandler>();

	/**
	 * Get event type.
	 * 
	 * @return event type
	 */
	public static Type<PanStartHandler> getType() {
		return eventType;
	}

	/**
	 * Constructor.
	 * 
	 * @param touchData data about touch
	 * @param targetElement element that was targeted.
	 */
	public PanStartEvent(final Touch touchData, final Element targetElement) {
		touchInformation = new TouchInformation(touchData);
		this.targetElement = targetElement;
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Type<PanStartHandler> getAssociatedType() {
		return eventType;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void dispatch(final PanStartHandler handler) {
		handler.onPanStart(this);
	}

}
