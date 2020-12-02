# Touchy Canvas

This project is to show how to use mobile gestures in a GWT APP.
I made a Touch Helper class to encapsulate the functionality is takes
to translate gesture primitive actions into higher level ones.
It currently supports PAN, ZOOM and DOUBLE TAP.

If you are interested in how to add overlays to the canvas look at my project [ElectronicBattleMat](https://github.com/lambertlb/ElectronicBattleMat).
File [BattleMatCanvas.java](https://github.com/lambertlb/ElectronicBattleMat/blob/master/src/main/java/per/lambert/ebattleMat/client/battleMatDisplay/BattleMatCanvas.java)
shows how I used overlays on the canvas.

## Building
This project is based on [Google Web Toolkit](http://www.gwtproject.org/). 
This allows the application to be written in Java and then compiled to javascript.

### Building with Maven
To build with maven just set console default to main folder and type.

    mvn clean package

### Building with Eclipse
This project is also an eclipse project. I found it easier to develop and debug using 
a standard eclipse project instead of a Maven one. To build it using eclipse you will obviously 
need to setup a few things first.
1. Install Java JDK. I found JDK 1.8 works best. Eclipse and Jetty seem to have issues 
   with newer versions. You can get the JDK @ [Oracle Java JDK](https://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
2. Install Eclipse. I use the enterprise addition@ [Eclipse IDE for Enterprise Java Developers](https://www.eclipse.org/downloads/packages/release/2019-03/r/eclipse-ide-enterprise-java-developers)
3. From eclipse marketplace install checkstyle.
4. From eclipse marketplace install gwt plugin.
5. Follow the instruction @ [Download and Install the GWT SDK](http://www.gwtproject.org/gettingstarted.html). 
After un-zipping gwt 2.9.0 you need to go to preferences in eclipse and add the path 
to gwt under Window->Preferences->GWT>GWT Settings hit the add button and navigate to 
where you un-zipped the SDK.
6. From eclipse you then just need to import the project General->Import Existing Project 
   into Workspace.

### License
This project is licensed under the [APACHE V2](LICENSE.TXT) license.
