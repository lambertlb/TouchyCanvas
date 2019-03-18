package per.lambert.touchyCanvas.client.touchHelper;

import com.google.gwt.event.shared.EventHandler;

/**
 * Handler for pan.
 * @author LLambert
 *
 */
public interface PanHandler extends EventHandler {
	/**
	 * Called when panning ends.
	 * 
	 * @param event with data.
	 */
	void onPan(PanEvent event);

}
