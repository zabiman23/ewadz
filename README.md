# EWADZ - TAK-X Plugin

**Last Update:** 4 October 2024

**Readme Contents**

- [EWADZ - TAK-X Plugin](#ewadz---tak-x-plugin)
  - [Overview](#overview)
  - [Build Instructions](#build-instructions)
  - [Docker Container Instructions](#docker-container-instructions)
  - [Sample Usage](#sample-usage)

Overview
------------
The EWADZ plugin is an example plugin that is meant to interact with a fictitious radar that moves around and identifies targets. This version of the plugin can be built without a tak.gov developer account.

The repo includes the following plugins:
 - Connection plugin to establish a TCP/IP connection to the simulator
 - Device plugin to represent the EWADZ device and support command and control of the Raven
 - Device plugin to represent the identified targets
 - App plugin to aggregate the targets in a list and monitor the status of the connection
 - Import/export plugin to support drag and drop of the EWADZ file directly on the globe

Although it has capabilities for all of these different plugin types, we will not necessarily being using all components of the plugin for our functionality. This will continue to be built out as capabilities are fleshed out properly.

Build Instructions
------------
1. Setup Java 17:
    1. Download and install the latest Java 17 JDK for your system from [Adoptium](https://adoptium.net/?variant=openjdk17).
    2. Add a `JAVA_HOME` environment variable with the location of your JDK if you didn't set it with the JDK installer.
       For example: _C:\Program Files\Eclipse Adoptium\jdk-21.0.2_
    3. Test your setup by opening a command prompt and executing `java -version`. The correct Java version should be displayed.
2. Update the properties in the `ext` block of the _build.gradle_ to reference your version of TAKX and your
   installation directory. If you are using Mac or Linux, comment out the Windows path and the Windows `platform` and 
   uncomment the appropriate variable definitions for your system instead.
3. Build this repo by executing `./gradlew install` in the root of the repo. This will build the plugin jar and copy it to
   the plugin directory in your TAKX installation directory.
   1. `./gradlew tasks` - will show you a list of tasks that can be run and their functionalities from the root folder
   2. Must have a local instance of TAK-X on machine available, or install and build processes will fail.


Docker Container Instructions
------------
System Requirements: `-- docker >= 27.0.0`
In the root directory,
   * `./build.sh`
   * `./run.sh`
   * You can explore the file system by running `docker exec -it ewadz-plugin bash`
     *  `cd app` will take you to directory where relevant build files/process occurs
   * Exit the container via the `exit` command
   * Stop the container: `docker compose down`



**Note:** Gradle can only support JDK version 8 or higher. For this JDK Version (JDK17), must have Gradle 7.3 to properly support and be compatible. If either JDK or Gradle version is changed, the other has to also be changed/updated to support the version that is being used. 

[Gradle Compatibility Matrix](https://docs.gradle.org/current/userguide/compatibility.html)


Sample Usage
------------
