package per.lambert.touchyCanvas.client.touchHelper;

import com.google.gwt.event.shared.EventHandler;

/**
 * Handler for pan start.
 * @author LLambert
 *
 */
public interface PanStartHandler extends EventHandler {
	/**
	 * Called when panning starts.
	 * 
	 * @param event with data.
	 */
	void onPanStart(PanStartEvent event);

}
