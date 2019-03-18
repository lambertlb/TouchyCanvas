package per.lambert.touchyCanvas.client;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.dom.client.ImageElement;
import com.google.gwt.event.dom.client.LoadEvent;
import com.google.gwt.event.dom.client.LoadHandler;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.event.dom.client.MouseWheelEvent;
import com.google.gwt.event.dom.client.MouseWheelHandler;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.LayoutPanel;

import per.lambert.touchyCanvas.client.touchHelper.DoubleTapEvent;
import per.lambert.touchyCanvas.client.touchHelper.DoubleTapHandler;
import per.lambert.touchyCanvas.client.touchHelper.PanEndEvent;
import per.lambert.touchyCanvas.client.touchHelper.PanEndHandler;
import per.lambert.touchyCanvas.client.touchHelper.PanEvent;
import per.lambert.touchyCanvas.client.touchHelper.PanHandler;
import per.lambert.touchyCanvas.client.touchHelper.PanStartEvent;
import per.lambert.touchyCanvas.client.touchHelper.PanStartHandler;
import per.lambert.touchyCanvas.client.touchHelper.TouchHelper;
import per.lambert.touchyCanvas.client.touchHelper.TouchInformation;
import per.lambert.touchyCanvas.client.touchHelper.ZoomEndEvent;
import per.lambert.touchyCanvas.client.touchHelper.ZoomEndHandler;
import per.lambert.touchyCanvas.client.touchHelper.ZoomEvent;
import per.lambert.touchyCanvas.client.touchHelper.ZoomHandler;
import per.lambert.touchyCanvas.client.touchHelper.ZoomStartEvent;
import per.lambert.touchyCanvas.client.touchHelper.ZoomStartHandler;

/**
 * Panel to home a canvas tat can be scaled, panned and zoomed.
 * 
 * @author LLambert
 *
 */
public class ImageCanvasPanel extends AbsolutePanel implements MouseWheelHandler, MouseDownHandler, MouseMoveHandler, MouseUpHandler {
	/**
	 * Offset for clearing rectangle.
	 */
	private static final int CLEAR_OFFEST = -10;
	/**
	 * Default zoom constant.
	 */
	private static final double DEFAULT_ZOOM = 1.1;
	/**
	 * Image with picture.
	 */
	private Image image;
	/**
	 * image context for drawing.
	 */
	private ImageElement imageElement;
	/**
	 * Main canvas that is drawn.
	 */
	private Canvas canvas;
	/**
	 * background canvas for temporary drawing.
	 */
	private Canvas backCanvas;
	/**
	 * Hidden panel to handle loading the image.
	 */
	private LayoutPanel hiddenPanel;
	/**
	 * Width of actual image.
	 */
	private int imageWidth = 0;
	/**
	 * Height of actual image.
	 */
	private int imageHeight = 0;
	/**
	 * Width of parent window.
	 */
	private int parentWidth = 0;
	/**
	 * Height of parent window.
	 */
	private int parentHeight = 0;
	/**
	 * current zoom factor for image.
	 */
	private double totalZoom = 1;
	/**
	 * Maximum zoom factor. We do not allow zooming out farther than the initial calculated zoom that fills the parent.
	 */
	private double maxZoom = .05;
	/**
	 * Offset of image in the horizontal direction.
	 */
	private double offsetX = 0;
	/**
	 * Offset of image in the vertical direction.
	 */
	private double offsetY = 0;
	/**
	 * Used by pan control.
	 */
	private boolean mouseDown = false;
	/**
	 * X position of mouse down.
	 */
	private double mouseDownXPos = 0;
	/**
	 * Y position of mouse down.
	 */
	private double mouseDownYPos = 0;
	/**
	 * Helper for mobile touches.
	 */
	private TouchHelper touchHelper;

	/**
	 * Constructor.
	 */
	public ImageCanvasPanel() {
		image = new Image();
		image.addLoadHandler(new LoadHandler() {
			public void onLoad(final LoadEvent event) {
				setupImage();
			}
		});
		canvas = Canvas.createIfSupported();
		backCanvas = Canvas.createIfSupported();
		hiddenPanel = new LayoutPanel();
		hiddenPanel.add(image);
		hiddenPanel.setVisible(false);

		canvas.addMouseWheelHandler(this);
		canvas.addMouseMoveHandler(this);
		canvas.addMouseDownHandler(this);
		canvas.addMouseUpHandler(this);

		super.add(canvas, 0, 0);
		super.add(hiddenPanel, -1, -1);
		touchHelper = new TouchHelper(canvas);
		touchHelper.addDoubleTapHandler(new DoubleTapHandler() {
			
			@Override
			public void onDoubleTap(final DoubleTapEvent event) {
				doDoubleTap(event);
			}
		});
		touchHelper.addPanStartHandler(new PanStartHandler() {
			
			@Override
			public void onPanStart(final PanStartEvent event) {
				doPanStart(event);
			}
		});
		touchHelper.addPanEndHandler(new PanEndHandler() {
			
			@Override
			public void onPanEnd(final PanEndEvent event) {
				doPanEnd(event);
			}
		});
		touchHelper.addPanHandler(new PanHandler() {
			
			@Override
			public void onPan(final PanEvent event) {
				doPan(event);
			}
		});
		touchHelper.addZoomHandler(new ZoomHandler() {
			
			@Override
			public void onZoom(final ZoomEvent event) {
				doZoom(event);
			}
		});
		touchHelper.addZoomStartHandler(new ZoomStartHandler() {
			
			@Override
			public void onZoomStart(final ZoomStartEvent event) {
				doZoomStart(event);
			}
		});
		touchHelper.addZoomEndHandler(new ZoomEndHandler() {
			
			@Override
			public void onZoomEnd(final ZoomEndEvent event) {
				doZoomEnd(event);
			}
		});
	}

	/**
	 * Tell image where picture is.
	 * 
	 * @param imageURL URL to picture
	 */
	public void setImage(final String imageURL) {
		image.setUrl(imageURL);
	}

	/**
	 * Called when image is loaded.
	 */
	private void setupImage() {
		totalZoom = 1;
		offsetX = 0;
		offsetY = 0;
		this.imageElement = (ImageElement) image.getElement().cast();
		parentWidthChanged(getParent().getOffsetWidth(), getParent().getOffsetHeight());
	}

	/**
	 * Parent window size has changed.
	 * 
	 * @param widthOfParent new width of window.
	 * @param heightOfParent new height of window.
	 */
	public final void parentWidthChanged(final int widthOfParent, final int heightOfParent) {
		parentWidth = widthOfParent;
		parentHeight = heightOfParent;
		imageWidth = image.getWidth();
		imageHeight = image.getHeight();

		canvas.setWidth(parentWidth + "px");
		canvas.setCoordinateSpaceWidth(parentWidth);
		canvas.setHeight(parentHeight + "px");
		canvas.setCoordinateSpaceHeight(parentHeight);

		backCanvas.setWidth(parentWidth + "px");
		backCanvas.setCoordinateSpaceWidth(parentWidth);
		backCanvas.setHeight(parentHeight + "px");
		backCanvas.setCoordinateSpaceHeight(parentHeight);

		calculateStartingZoom();
		backCanvas.getContext2d().setTransform(totalZoom, 0, 0, totalZoom, 0, 0);
		drawEverything();
	}

	/**
	 * Calculate the starting zoom factor so that one side of the image exactly fills the parent.
	 */
	private void calculateStartingZoom() {
		if (isScaleByWidth()) {
			totalZoom = (double) parentWidth / (double) imageWidth;
		} else {
			totalZoom = (double) parentHeight / (double) imageHeight;
		}
	}

	/**
	 * Should we scale by width.
	 * 
	 * @return true if we should scale by width
	 */
	private boolean isScaleByWidth() {
		double scaleWidth = (double) parentWidth / (double) imageWidth;
		double scaleHeight = (double) parentHeight / (double) imageHeight;
		return scaleWidth < scaleHeight;
	}

	/**
	 * Main method for drawing image.
	 */
	public final void drawEverything() {
		backCanvas.getContext2d().clearRect(CLEAR_OFFEST, CLEAR_OFFEST, imageWidth + 50, imageHeight + 50);
		backCanvas.getContext2d().setTransform(totalZoom, 0, 0, totalZoom, offsetX, offsetY);
		backCanvas.getContext2d().drawImage(imageElement, 0, 0);
		handleAllDrawing();
	}

	/**
	 * Handle drawing everything.
	 */
	public final void handleAllDrawing() {
		canvas.getContext2d().clearRect(CLEAR_OFFEST, CLEAR_OFFEST, parentWidth + 100, parentHeight + 100);
		canvas.getContext2d().drawImage(backCanvas.getCanvasElement(), 0, 0);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onMouseDown(final MouseDownEvent event) {
		mouseDownXPos = event.getRelativeX(image.getElement());
		mouseDownYPos = event.getRelativeY(image.getElement());
		this.mouseDown = true;
	}
	/**
	 * Handle Pan start.
	 * @param event with data
	 */
	protected void doPanStart(final PanStartEvent event) {
		mouseDownXPos = event.getTouchInformation().getPageX();
		mouseDownYPos = event.getTouchInformation().getPageY();
		this.mouseDown = true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onMouseUp(final MouseUpEvent event) {
		this.mouseDown = false;
	}
	/**
	 * Handle Pan end.
	 * @param event with data
	 */
	protected void doPanEnd(final PanEndEvent event) {
		this.mouseDown = false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onMouseMove(final MouseMoveEvent event) {
		if (mouseDown) {
			handleMouseMove(event);
		}
	}
	/**
	 * Handle Pan.
	 * @param event with data
	 */
	protected void doPan(final PanEvent event) {
		double xPos = event.getTouchInformation().getPageX();
		double yPos = event.getTouchInformation().getPageY();
		handleCanvasMove(xPos, yPos);
	}



	/**
	 * Handle mouse move.
	 * 
	 * @param event event data
	 */
	private void handleMouseMove(final MouseMoveEvent event) {
		double xPos = event.getRelativeX(image.getElement());
		double yPos = event.getRelativeY(image.getElement());
		handleCanvasMove(xPos, yPos);
	}

	/**
	 * Handle panning canvas.
	 * 
	 * @param xPos center X of pan
	 * @param yPos center Y of pan
	 */
	private void handleCanvasMove(final double xPos, final double yPos) {
		offsetX += (xPos - mouseDownXPos);
		offsetY += (yPos - mouseDownYPos);
		try {
			drawEverything();
		} catch (Exception ex) {
			mouseDownXPos = xPos;
			mouseDownYPos = yPos;
		}
		mouseDownXPos = xPos;
		mouseDownYPos = yPos;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onMouseWheel(final MouseWheelEvent event) {
		int move = event.getDeltaY();
		double xPos = (event.getRelativeX(canvas.getElement()));
		double yPos = (event.getRelativeY(canvas.getElement()));

		computeZoom(move, xPos, yPos);
	}

	/**
	 * compute zoom.
	 * @param move in or out.
	 * @param xPos center x
	 * @param yPos center y
	 */
	private void computeZoom(final int move, final double xPos, final double yPos) {
		double zoom = DEFAULT_ZOOM;
		if (move >= 0) {
			zoom = 1 / DEFAULT_ZOOM;
		}

		scaleCanvas(xPos, yPos, zoom);
	}
	/**
	 * Distance between fingers.
	 */
	private double distance;

	/**
	 * Handle zoom start event.
	 * @param event with data
	 */
	protected void doZoomStart(final ZoomStartEvent event) {
		distance = event.getZoomInformation().getStartingDistance();
	}
	/**
	 * Handle zoom end event.
	 * @param event with data
	 */
	protected void doZoomEnd(final ZoomEndEvent event) {
	}
	/**
	 * Handle zoom event.
	 * @param event with data
	 */
	protected void doZoom(final ZoomEvent event) {
		double currentDistance = event.getZoomInformation().getCurrentDistance();
		int move = (int)(currentDistance - distance);
		double xPos = event.getZoomInformation().currentCenterX();
		double yPos = event.getZoomInformation().currentCenterY();
		computeZoom(move, xPos, yPos);
		distance = currentDistance;
	}

	/**
	 * Scale canvas base on delta mouse positions.
	 * 
	 * @param xPos current X
	 * @param yPos current Y
	 * @param zoom zoom factor
	 */
	private void scaleCanvas(final double xPos, final double yPos, final double zoom) {
		double newX = (xPos - offsetX) / totalZoom;
		double newY = (yPos - offsetY) / totalZoom;
		double xPosition = (-newX * zoom) + newX;
		double yPosition = (-newY * zoom) + newY;

		double newZoom = zoom * totalZoom;
		if (newZoom < maxZoom) {
			newZoom = maxZoom;
		} else {
			offsetX += (xPosition * totalZoom);
			offsetY += (yPosition * totalZoom);
		}
		totalZoom = newZoom;
		drawEverything();
	}
	/**
	 * Handle double tap.
	 * @param event with data.
	 */
	protected void doDoubleTap(final DoubleTapEvent event) {
	}

}
