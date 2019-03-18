package per.lambert.touchyCanvas.client.touchHelper;

import com.google.gwt.event.shared.EventHandler;

/**
 * Handler for zoom start.
 * @author LLambert
 *
 */
public interface ZoomStartHandler extends EventHandler {
	/**
	 * Called when panning starts.
	 * 
	 * @param event with data.
	 */
	void onZoomStart(ZoomStartEvent event);

}
