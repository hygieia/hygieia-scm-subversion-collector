## Due to lack of activity, this repo is currently not being supported and is archived as of 07/06/22. Please note since archival, the project is not maintained and will be available in a read-only state. Please reach out to hygieia2@capitalone.com should you have any questions.
<!---
 Licensed to the Apache Software Foundation (ASF) under one or more
 contributor license agreements.  See the NOTICE file distributed with
 this work for additional information regarding copyright ownership.
 The ASF licenses this file to You under the Apache License, Version 2.0
 (the "License"); you may not use this file except in compliance with
 the License.  You may obtain a copy of the License at

      http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
-->

Hygieia SCM Subversion Collector
================================

[![Gitter Chat](https://badges.gitter.im/capitalone/hygieia.svg)](https://gitter.im/capitalone/Hygieia?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)
[![Build Status](https://travis-ci.org/Hygieia/hygieia-scm-subversion-collector.svg?branch=master)](https://travis-ci.org/Hygieia/hygieia-scm-subversion-collector)
[![Coverage Status](https://coveralls.io/repos/github/Hygieia/hygieia-scm-subversion-collector/badge.svg?branch=master)](https://coveralls.io/github/Hygieia/hygieia-scm-subversion-collector?branch=master)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.capitalone.dashboard/subversion-collector/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.capitalone.dashboard/subversion-collector/)
[![Javadocs](https://javadoc.io/badge/com.capitalone.dashboard/subversion-collector/3.0.2.svg)](https://javadoc.io/doc/com.capitalone.dashboard/subversion-collector/3.0.2)

Configure the Subversion Collector to display and monitor information (related to code contribution activities) on the Hygieia Dashboard, from the Subversion repository. Hygieia uses Spring Boot to package the collector as an executable JAR file with dependencies.

# Table of Contents
* [Setup Instructions](#setup-instructions)
* [Sample Application Properties](#sample-application-properties)
* [Developing](#developing)
* [Run collector with Docker](#run-collector-with-docker)

### Setup Instructions

To configure the Subversion Collector, execute the following steps:

*	**Step 1 - Artifact Preparation:**

	Please review the two options in Step 1 to find the best fit for you. 

	***Option 1 - Download the artifact:***

	You can download the SNAPSHOTs from the SNAPSHOT directory [here](https://oss.sonatype.org/content/repositories/snapshots/com/capitalone/dashboard/subversion-collector/) or from the maven central repository [here](https://search.maven.org/artifact/com.capitalone.dashboard/subversion-collector).  

	***Option 2 - Build locally:***

	To configure the Subversion Collector, git clone the [subversion collector repo](https://github.com/Hygieia/hygieia-scm-subversion-collector).  Then, execute the following steps:

	To package the subversion collector source code into an executable JAR file, run the maven build from the `\hygieia-scm-subversion-collector` directory of your source code installation:

	```bash
	mvn install
	```

	The output file `[collector name].jar` is generated in the `hygieia-scm-subversion-collector\target` folder.

	Once you have chosen an option in Step 1, please proceed: 


*   **Step 2: Set Parameters in Application Properties File**

Set the configurable parameters in the `application.properties` file to connect to the Dashboard MongoDB database instance, including properties required by the Subversion Collector.

For information about sourcing the application properties file, refer to the [Spring Boot Documentation](http://docs.spring.io/spring-boot/docs/current-SNAPSHOT/reference/htmlsingle/#boot-features-external-config-application-property-files).

To configure parameters for the Subversion Collector, refer to the sample [application.properties](#sample-application-properties) section.

*   **Step 3: Deploy the Executable File**

To deploy the `subversion-collector.jar` file, change directory to `subversion\target`, and then execute the following from the command prompt:

```bash
java -jar subversion-collector.jar --spring.config.name=subversion --spring.config.location=[path to application.properties file]
```

### Sample Application Properties

The sample `application.properties` lists parameter values to configure the Subversion Collector. Set the parameters based on your environment setup.

```properties
		# Database Name
		dbname=dashboarddb

		# Database HostName - default is localhost
		dbhost=localhost

		# Database Port - default is 27017
		dbport=27017

		# MongoDB replicaset
		dbreplicaset=[false if you are not using MongoDB replicaset]
		dbhostport=[host1:port1,host2:port2,host3:port3]

		# Database Username - default is blank
		dbusername=dashboarduser

		# Database Password - default is blank
		dbpassword=dbpassword

		# Logging File location
		logging.file=./logs/subversion.log

		# Collector schedule (required)
		subversion.cron=0 0/5 * * * *

		# Shared subversion username and password
		subversion.username=foo
		subversion.password=bar
		subversion.host=my.subversion.com

		# Maximum number of previous days from current date, when fetching commits
		subversion.commitThresholdDays=15
```

# Developing.

Before making any contributions, we suggest that you read the [CONTRIBUTING.md](CONTRIBUTING.md) and the 
[Development Docs](./src) (housed in `./src/docs/README.md`). But for the most part, running:

```
mvn clean test install site
``` 

will test and generate the project metrics (navigable from the locally built `./target/site/index.html` 
-- see "Project Reports" in the left nav for all of the available reports). We generally want these 
reports to look good.

## Run collector with Docker

You can install Hygieia by using a docker image from docker hub. This section gives detailed instructions on how to download and run with Docker. 

*	**Step 1: Download**

	Navigate to the docker hub location of your collector [here](https://hub.docker.com/u/hygieiadoc) and download the latest image (most recent version is preferred).  Tags can also be used, if needed.

*	**Step 2: Run with Docker**

	```Docker run -e SKIP_PROPERTIES_BUILDER=true -v properties_location:/hygieia/config image_name```
	
	- <code>-e SKIP_PROPERTIES_BUILDER=true</code>  <br />
	indicates whether you want to supply a properties file for the java application. If false/omitted, the script will build a properties file with default values
	- <code>-v properties_location:/hygieia/config</code> <br />
	if you want to use your own properties file that located outside of docker container, supply the path here. 
		- Example: <code>-v /Home/User/Document/application.properties:/hygieia/config</code>

