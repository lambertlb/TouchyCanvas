package per.lambert.touchyCanvas.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.Window.Location;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.gwt.user.client.ui.RootLayoutPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class TouchyCanvas implements EntryPoint {
	/**
	 * Root layout panel.
	 */
	private RootLayoutPanel rootLayoutPanel;
	/**
	 * Panel to hold canvas.
	 */
	private LayoutPanel imagePanel;
	/**
	 * Cavas with inage.
	 */
	private ImageCanvasPanel imageCanvas;
	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		rootLayoutPanel = RootLayoutPanel.get();
		imagePanel = new LayoutPanel();
		imagePanel.setSize("100%", "100%");
		imageCanvas = new ImageCanvasPanel();
		imagePanel.add(imageCanvas);
		rootLayoutPanel.add(imagePanel);
		imageCanvas.setImage(constructPath() + "FallPanorama.jpg");
	}
	/**
	 * Get proper web path.
	 * 
	 * @return proper web path
	 */
	private String constructPath() {
		String path = Location.getPath();
		if (path.toLowerCase().endsWith(".html")) {
			int index = path.lastIndexOf("/");
			path = path.substring(0, index + 1);
		}
		return path;
	}

}
