package per.lambert.touchyCanvas.client.touchHelper;

import com.google.gwt.dom.client.Touch;

/**
 * Information about touch.
 * 
 * @author LLambert
 *
 */
public class TouchInformation {
	/**
	 * the touch x-position within the browser document.
	 */
	private final int pageX;
	/**
	 * the touch y-position within the browser document.
	 */
	private final int pageY;
	/**
	 * the touch x-position within the browser window's client area.
	 */
	private final int clientX;
	/**
	 * the touch y-position within the browser window's client area.
	 */
	private final int clientY;
	/**
	 * the touch x-position on the user's display.
	 */
	private final int screenX;
	/**
	 * the touch y-position on the user's display.
	 */
	private final int screenY;

	/**
	 * Constructor.
	 * @param touchInformation touch information
	 */
	public TouchInformation(final Touch touchInformation) {
		this.pageX = touchInformation.getPageX();
		this.pageY = touchInformation.getPageY();
		this.clientX = touchInformation.getClientX();
		this.clientY = touchInformation.getClientY();
		this.screenX = touchInformation.getScreenX();
		this.screenY = touchInformation.getScreenY();
	}

	/**
	 * Get the touch x-position within the browser document.
	 * @return the touch x-position within the browser document.
	 */
	public int getPageX() {
		return pageX;
	}

	/**
	 * Get the touch y-position within the browser document.
	 * @return the touch y-position within the browser document.
	 */
	public int getPageY() {
		return pageY;
	}

	/**
	 * get the touch x-position within the browser window's client area.
	 * @return the touch x-position within the browser window's client area.
	 */
	public int getClientX() {
		return clientX;
	}

	/**
	 * get the touch y-position within the browser window's client area.
	 * @return the touch y-position within the browser window's client area.
	 */
	public int getClientY() {
		return clientY;
	}

	/**
	 * Get the touch x-position on the user's display.
	 * @return the touch x-position on the user's display.
	 */
	public int getScreenX() {
		return screenX;
	}

	/**
	 * Get the touch y-position on the user's display.
	 * @return the touch y-position on the user's display.
	 */
	public int getScreenY() {
		return screenY;
	}
}
