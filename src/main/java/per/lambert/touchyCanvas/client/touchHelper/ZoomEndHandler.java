package per.lambert.touchyCanvas.client.touchHelper;

import com.google.gwt.event.shared.EventHandler;

/**
 * Handler for zoom end.
 * @author LLambert
 *
 */
public interface ZoomEndHandler extends EventHandler {
	/**
	 * Called when panning starts.
	 * 
	 * @param event with data.
	 */
	void onZoomEnd(ZoomEndEvent event);

}
