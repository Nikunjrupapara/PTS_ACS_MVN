# PTS ACS Hosted Pages
## About this project
This project is a Spring Boot and React application that provides the following capabilities:

* Exposes micro services for use in PTS built hosted pages that use the ACS API (in the microservices subdirectory)
* Provides a UI (microservices/frontend) for the following:
   * Managing ACS API credentials for clients
   * Testing ACS transactional messaging for clients
   * Registering landing pages built for clients and generating the JSON Web Token required to secure those pages.
* Integration test suite for the Microservices API (integration) based on Cucumber & Gherkin.
   
## Development Workflow
The project will use the workflow described here: https://nvie.com/posts/a-successful-git-branching-model/

In summary:

* All new code must be developed on a new branch from the `develop` branch, e.g.

`git checkout develop`

`git pull`

`git checkout -b task/{new branch name}`, where new branch name might be a JIRA number or a short but meaningful reference to the task

* Write integration (see below for more details) & unit tests

* Write code

  * It is recommended to commit little and often when working in your branch to keep changesets to a manageable size.
  
  * Use meaningful commit messages, always written in present tense

  * If your task is taking some time to complete and there are other changes to `develop` it is a good idea to periodically
  merge changes from `develop` into your branch.  This will help to minimise conflicts when merging your branch back to `develop`
  using a PR.

* Make sure all tests (including previously written ones) pass. 

* Push code to GitHub `git push`

* Create a pull request (PR) in GitHub to merge the changes from your branch to the develop branch.
  * PRs must be reviewed and approved by another developer before they can be merged into develop
  * After the PR is reviewed and approved, merge it into develop using the Squash & Merge option
  * Delete the branch used to develop the task or feature

## Integration Tests
Having a comprehensive suite of integration tests is vital to smooth completion of the Change Management process for
new deployments of the application.  Each business requirement (user story or feature) should have a set of Cucumber integration tests defined
in the integration module.  A Cucumber test consists of the following components:

* Feature definitions
  * Defined in the directory integration/src/test/resources/integrationtests/{component}/{story name}.feature
  * Lists a set of scenarios for the feature using the BDD Given ... When ... Then ... layout
  * The scenarios should be written in plain English using domain language, so they are comprehensible to a business user,
  rather than a technical audience
* Step definitions
  *  Defined in the directory integration/src/test/java/integrationtests/{component}/{story name}.java
  * Cucumber will generate the step definitions for you based upon the Feature definition.
  * A developer then provides the Java code required to set up and execute the requests to the Microservices API when running the tests
  * There is some utility code provided in the com.yesmarketing.ptsacs.utils package to make it easier to write the step
  definitions and the assertions more easily.
  
#### Running the Integration Tests

To run the integration tests do the following:

* Build the Microservices API project
* Start the Microservices API by executing the script microservices/start.sh from a Terminal window
  * Make sure that you have set the active Spring profile to be "integrationtest".  This will use a different MongoDb instance
    to create the test configuration required for the integration tests and protect your local test data in the standard
    "ptsacs" database.
* Execute the Java class ```com.yesmarketing.ptsacs.integrationtests.RunCucumberTest``` (located in integration/src/test/java)   

## Getting Started
Checkout the `develop` branch from GitHub

To build and run the server application run this command:

```
cd pts-acs-hosted-pages/microservices
./mvnw spring-boot:run
```
or

```
cd pts-acs-hosted-pages/microservices
mvn clean compile package
./start.sh
```

To run the UI locally:

```
cd pts-acs-hosted-pages/frontend
npm start
```

Open the URL http://localhost:3000/#/ in your browser, if it does not open automatically.

Set the admin password for your local environment using the ```set_env.sh``` file or by adding ```-Dadmin.ui.pwd={password}```
to the Java command line.

Note that when running locally you need to use an insecure authentication cookie unless you use a self-signed SSL certificate
to permit use of https.
If you make the Spring Active Profile "development" then the application will return an insecure cookie.  Do this by:
* setting SPRING_PROFILES_ACTIVE to "development" in your local ```set_env.sh``` file
* specify ```-Dspring.profiles.active=development``` on the Java command line to start the application

## Releases
Releases will be created by merging `develop` to `master` and then creating a release branch named like this: `release-{version}`.

More to follow when required...

