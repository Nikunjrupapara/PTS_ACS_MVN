# ACS Custom Hosted Pages

This project implements custom hosted pages for clients where the ACS Landing Pages capability is not able to support
the requirements.  In production & QA the application is deployed as a WAR in a standalone Tomcat server, but for
development you can run it in the embedded Tomcat of your IDE, e.g. IntelliJ.

This document provides a guide for how to set get started for local development & testing of custom hosted pages.

Instructions about how to implement custom hosted pages are hosted on Confluence (TBD).

## Software Requirements

* Java Development Kit 11 or higher
* Local MongoDb
* IntelliJ Idea Community Edition (recommended) or Eclipse/NetBeans (neither of these have been tested).

## Configuration

There is some configuration required to be able to run the hosted pages app locally.

### Spring Profiles

The application defines the following Spring profiles:

* ```development``` - for development in your IDE or local Tomcat server
* ```qa``` - for running in the QA environment
* ```production``` - for running in production

For the initial release of the application the most important reason for using profiles is to enable the environment
specific configuration of domains and Microservices JWTs.  This will become less important once the AppDetails API is
built and ready to use, in conjunction with the App Details UI in the Microservices application Admin UI.

### Domains

The domain used for a hosted pages request is used to identify the company and brand which are in use.  For this to work
correctly you need to set up test domains on your workstation for the client you are working with by editing your local
hosts file (on a Mac this is ```/etc/hosts```).  For example, for Resideo I used the following domains:

```
127.0.0.1   test.resideo.com
127.0.0.1   test.honeywellhome.com
```

To make the changes
```
sudo vi /etc/hosts
```
Flush the DNS cache to make sure the changes are immediately available
```
sudo dscacheutil -flushcache
```
On Windows I believe it should be sufficient to edit the hosts file, but I have not tried this.

You also need to edit the file ```src/main/resources/application-development.yml``` file to add the domains.

### JSON Web Tokens for Microservices API

*TODO: this section needs to be updated now that tokens are defined in environment specific application.yml variants*

Requests to the Microservices API require a JSON Web Token for authentication.  The JWT is generated as part of creating
a form config in the Microservices Admin UI.  However, until the Hosted Pages App Details API is created, we also need
to store these tokens in the configuration of this application.  As with the domains, the tokens are stored in the
environment specific configuration files, e.g. ```application-development.yml``` or ```application-qa.yml```.

### application-development.yml

This file is unique to each developer's workstation as it contains information, specifically JWTs but also the flags for
stubbed microservices and, possibly, domains.  **Therefore, please treat the committed version as a template to modify for
your own environment and try to avoid committing changes to this file to GitHub.**

You can do this by running the following command after cloning the repository, which will make git ignore any changes to
the file:

```
git update-index --skip-worktree acscustompages/src/main/resources/application-development.yml
```

For more information see here: https://stackoverflow.com/a/16954184/8684496

### Microservices API
The application sends requests to the PTS ACS Microservices API to perform ACS data processes.  You can configure the
MS API service that you are connected to by setting the property ```microservices.base-url``` in the config file
```application-development.yml```.  However, for initial development and testing of new Hosted Pages I recommend using
the stubbed microservices instead, as this makes testing and prototyping much faster.  You can enable the stubbed
services by setting the flags under ```microservices.useStubbedServices``` to ```true``` for each required service.

### Runnning in IntelliJ Idea
To run the application in ItelliJ Idea you need to set up a new Application Runtime Configuration by going to
Run > Edit Configurations:

* Click the '+' symbol in the dialog's toolbar and choose 'Application'. 
* Make sure the correct JDK version (11) is selected
* Set the main class to ```com.dataaxle.pts.acscustompages.AcsCustomPagesApplication```
* Add the string ```-Dspring.profiles.active=development``` in the "VM Options" field
* Make sure the option "Include dependencies with provided scope" is checked (exactly where this is located depends
  on which version of IntelliJ you are using)
  
Now you should be able to run the application successfully.

### Deploying in a local Tomcat server
The following instructions assume you are running on a Mac.

Build the WAR file by running ```mvn package -DskipTests``` (-DskipTests is optional, but faster).

Copy the template file ```tomcat/bin/setenv.sh``` to your Tomcat installation's ```{CATALINA_BASE}/bin``` directory.
Modify it to customise it to your local environment.  Most important is to set the ```LOG_PATH```, everything else is
optional.  Make sure it is executable: ```chmod +x setenv.sh```

You may need to change the port number Tomcat is running on from 8080 to a different one to avoid clashing with the
local Microservices API (if you are running one).  To do this edit your ```{CATALINA_BASE}/conf/server.xml``` file and
change the port number used by the Connector listening to port 8080 to 8081 (or your preferred value).

Set up a user in the ```{CATALINA_HOME}/conf/tomcat-users.xml``` file.  Make sure the user has ```manager-gui``` and 
```manager-status``` roles

Start the server by running ```{CATALINA_BASE}/bin/startup.sh```

To deploy your application:
* Open ```http://localhost:8081/manager/html``` in your browser
* Log in using the credentials you set up above
* In the Deploy section look for the "WAR file to deploy" section, click "Choose file", navigate to the project's target
directory created by your IDE and choose ```acscustompages.war```.  Click "Deploy".

To deploy updates to the WAR build a new WAR using maven and then copy it to ```{CATALINA_HOME}/webapps```, which
will overwrite the existing WAR and automatically deploy the new version.

