package per.lambert.touchyCanvas.client.touchHelper;

import com.google.gwt.event.shared.EventHandler;

/**
 * Handler for double tap.
 * @author LLambert
 *
 */
public interface DoubleTapHandler extends EventHandler {
	/**
	 * Called when double tap happens.
	 * @param event with data.
	 */
	void onDoubleTap(DoubleTapEvent event);
}
