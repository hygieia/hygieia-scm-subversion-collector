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

Development Documentation.
==========================

### Prerequisites and sanity checks.

It is suggested that you have:

* [Java 8](https://docs.aws.amazon.com/corretto/latest/corretto-8-ug/downloads-list.html)
* [Maven 3.6.X](https://maven.apache.org/download.cgi)

installed before attempting to build the project. For the sake of building, testing and seeing the project reports, it 
is suggested that you regularly run `mvn clean test install site` and view the maven generated site that gets built into 
`${basedir}/target/site` (navigating to the `index.html` in here will serve the static site locally), where `${basedir}` 
represents the directory containing the project's root locally cloned from github. Note, the site should look analogous to 
https://hygieia.github.io/hygieia-subversion-scm-collector/ the github page.

### Maven plugins.

We have a variety of plugins installed for generating reports. Notice the [reports page on the site](https://hygieia.github.io/hygieia-subversion-scm-collector/project-reports.html) it has:

* javadoc
* test javadoc
* [checkstyle](https://maven.apache.org/plugins/maven-checkstyle-plugin/) - static analysis that validates the google style guide; helpful for readability
  * Note, the `checkstyle.xml` (the rules that govern the projects output) resides in `src/devops/checkstyle.xml` and is essentially 
    verbatim the google style guide.
* [jacoco](https://www.eclemma.org/jacoco/trunk/doc/maven.html) - code coverage. This is indeed important. We like to see numbers greater than 90% and are happy when we surpass 95%.
* [japicmp](http://siom79.github.io/japicmp/) - a tool for doing java API comparison, so that we can see what changes between versions. 
  This tool will tell us when we've made "binary compatibility" (BC for short) non-breaking or breaking changes. When we break BC, it is 
  highly advised that the version of the component be major version number upgraded.
* [rat plugin](http://creadur.apache.org/rat/apache-rat-plugin/) - the Apache "RAT" plugin validates that the Apache 2.0 License header is
  included in every individual file in the project. Note that every file is a released artifact as it is served by github. Thus, every file 
  need be individually licensed. Note, we accommodate for exceptions to this rule when files contain data that necesarily resides in a 
  format that precludes comments (e.g. JSON or CSV). Much like with the japicmp plugin, we highly suggest that the rules of licensing be 
  adhered to
* spotbugs - static analysis. (take with a grain of salt, use it as a reason to re-read your code)
* pmd - static anslysis (take with a grain of salt, use it as a reason to re-read your code)
* [changes](https://maven.apache.org/plugins/maven-changes-plugin/) - a release notes generator assuming that the `src/changes/changes.xml` 
  is sufficiently maintained.
  * This plugin both generates a change report for the site as well as generating a `RELEASE-NOTES.txt` or `announcement.vm` if run properly: `mvn changes:announcement-generate`. Do read 
    the site as it contains substancially more information.
    
### Maintaining the `changes.xml`

As stipulated above, the `maven-changes-plugin`, gives us the mechnism to generate both the
release history for the maven site as well as generate `RELEASE-NOTES.txt`. Our configuration is
such that we use the github issues for tracking our changes. Notice in the 
[changes.xml](../changes/changes.xml), we have releases with individual `action`'s under them.
Each action should correspond to a github issue.

We're afforded the luxury now of having a variety of reports that can be built. For the 
site's changes report, all we need worry about is the `mvn clean install site` command, and
the report will get build into the site available at `./target/site/index.html`.

Regarding generating the `RELEASE-NOTES.txt`, we run `mvn changes:announcement-generate`, and
we get an `announcement.vm` file generated at `./target/announcement/announcement.vm`. We
suggest that the release manager take this file and append it to the beginning of 
`./RELEASE-NOTES.txt`, which if it does not exist, we suggest you create.

