/*
 * Copyright (C) 2019 Leon Lambert.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package per.lambert.touchyCanvas.client.touchHelper;

import com.google.gwt.dom.client.Touch;
import com.google.gwt.event.dom.client.TouchEvent;

/**
 * worker for handling zoom information.
 * 
 * @author LLambert
 *
 */
public class ZoomInformation {
	/**
	 * Information for where finger 1 first touched screen.
	 */
	private TouchInformation startingFinger1;

	/**
	 * Get starting finger 1 position.
	 * 
	 * @return position
	 */
	public TouchInformation getStartingFinger1() {
		return startingFinger1;
	}

	/**
	 * Information for where finger 2 first touched screen.
	 */
	private TouchInformation startingFinger2;

	/**
	 * Get starting finger 2 position.
	 * 
	 * @return position
	 */
	public TouchInformation getStartingFinger2() {
		return startingFinger2;
	}

	/**
	 * Information for where finger 1 currently is.
	 */
	private TouchInformation currentFinger1;

	/**
	 * Information for where finger 1 currently is.
	 * 
	 * @return position
	 */
	public TouchInformation getCurrentFinger1() {
		return currentFinger1;
	}

	/**
	 * Information for where finger 2 currently is.
	 */
	private TouchInformation currentFinger2;

	/**
	 * Information for where finger 2 currently is.
	 * 
	 * @return position
	 */
	public TouchInformation getCurrentFinger2() {
		return currentFinger2;
	}

	/**
	 * Constructor.
	 * 
	 * @param startingFinger1 starting finger1 position.
	 * @param startingFinger2 starting finger2 position.
	 * @param targetElement element that was targeted.
	 */
	@SuppressWarnings("rawtypes")
	public ZoomInformation(final TouchInformation startingFinger1, final TouchInformation startingFinger2, final TouchEvent targetElement) {
		this.startingFinger1 = startingFinger1.clone();
		this.startingFinger2 = startingFinger2.clone();
		currentFinger1 = new TouchInformation((Touch) targetElement.getTouches().get(0));
		currentFinger2 = new TouchInformation((Touch) targetElement.getTouches().get(1));
	}

	/**
	 * Center X of starting fingers.
	 * 
	 * @return center X for starting fingers
	 */
	public int startingCenterX() {
		return ((startingFinger1.getPageX() + startingFinger2.getPageX()) / 2);
	}

	/**
	 * Center y of starting fingers.
	 * 
	 * @return center Y for starting fingers
	 */
	public int startingCenterY() {
		return ((startingFinger1.getPageY() + startingFinger2.getPageY()) / 2);
	}

	/**
	 * Center X of current fingers.
	 * 
	 * @return center X for current fingers
	 */
	public int currentCenterX() {
		return ((currentFinger1.getPageX() + currentFinger2.getPageX()) / 2);
	}

	/**
	 * Center y of current fingers.
	 * 
	 * @return center Y for current fingers
	 */
	public int currentCenterY() {
		return ((currentFinger1.getPageY() + currentFinger2.getPageY()) / 2);
	}

	/**
	 * Get starting distance between fingers.
	 * 
	 * @return starting distance.
	 */
	public double getStartingDistance() {
		return (Math.sqrt(Math.pow(startingFinger1.getPageX() - startingFinger2.getPageX(), 2) + Math.pow(startingFinger1.getPageY() - startingFinger2.getPageY(), 2)));
	}

	/**
	 * Get current distance between fingers.
	 * 
	 * @return current distance.
	 */
	public double getCurrentDistance() {
		return (Math.sqrt(Math.pow(currentFinger1.getPageX() - currentFinger2.getPageX(), 2) + Math.pow(currentFinger1.getPageY() - currentFinger2.getPageY(), 2)));
	}
}
