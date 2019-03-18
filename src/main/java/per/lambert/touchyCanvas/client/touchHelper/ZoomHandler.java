package per.lambert.touchyCanvas.client.touchHelper;

import com.google.gwt.event.shared.EventHandler;

/**
 * Handler for zoom.
 * @author LLambert
 *
 */
public interface ZoomHandler extends EventHandler {
	/**
	 * Called when panning starts.
	 * 
	 * @param event with data.
	 */
	void onZoom(ZoomEvent event);

}
