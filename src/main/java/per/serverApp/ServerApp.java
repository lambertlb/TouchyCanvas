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
package per.serverApp;

import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;

/**
 * Application so client can run Jetty server locally to DM a session.
 * 
 * @author LLambert
 *
 */
public final class ServerApp {
	/**
	 * Constructor.
	 */
	private ServerApp() {
	}

	/**
	 * Main entry point.
	 * 
	 * @param args arguments
	 * @throws Exception if error.
	 */
	public static void main(final String[] args) throws Exception {
		buildWelcome();
		File tempDir = new File("./TouchyCanvasTemp");
		Server server = new Server(8088);
		WebAppContext webAppContext = new WebAppContext();
		webAppContext.setContextPath("/");
		if (!tempDir.exists()) {
			webAppContext.setWar("./TouchyCanvas.war");
			webAppContext.setTempDirectory(new File("./TouchyCanvasTemp"));
		} else {
			// use existing directory so war does not overwrite any dungeon changes we made.
			webAppContext.setDescriptor("./TouchyCanvasTemp/webapp/WEB-INF/web.xml");
			webAppContext.setResourceBase("./TouchyCanvasTemp/webapp");
			webAppContext.setTempDirectory(new File("./TouchyCanvasTemp2"));
		}
		webAppContext.setParentLoaderPriority(true);
		webAppContext.setServer(server);
		webAppContext.setClassLoader(ClassLoader.getSystemClassLoader());
		server.setHandler(webAppContext);
		server.start();
	}

	/**
	 * Show pre-amble to user.
	 */
	private static void buildWelcome() {
		String ipAddress = getIpAddress();
		if (ipAddress == null) {
			System.out.println("No ip address found.");
			System.out.println("Must have network card to run this program.");
			return;
		}
		System.out.println("Enter the following URL to access App");
		System.out.println("http://" + ipAddress + ":8088");
		System.out.println("");
		System.out.println("");
	}

	/**
	 * Get ip address of machine.
	 * 
	 * @return address of machine.
	 */
	private static String getIpAddress() {
		InetAddress ip;
		try {
			ip = InetAddress.getLocalHost();
		} catch (UnknownHostException e) {
			return (null);
		}
		return (ip.getHostAddress());
	}
}
