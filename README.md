# Allure

[<img src="http://img.shields.io/github/release/allure-framework/allure-core.png?style=flat">](https://github.com/allure-framework/allure-core/releases/latest)

![image](https://raw.github.com/allure-framework/allure-core/master/allure-dashboard.png)

A flexible, lightweight multi-language framework for writing **self-documenting tests**, with the ability to store attachments, such as screenshots, logs and so on.

## Who uses Allure
Currently Allure Framework is widely used in internal testing of [Yandex](http://yandex.com/) software products. Hundreds of software testers every day are giving a high note to their experience with Allure.

## Framework structure
Allure consists of several core components including:
* **[Adapter](#adapter)** - a plugin to some test framework which provide information about testing process in XML format.
* **[Report generator](#report-generator)** - generates report data from the test results data.
* **[Report face](#report-face)** - web application which visualizes report data.
The modules above are independent one from another and interact only through the files. This allows to replace any of these modules by your own implementation, use an adapter for your preferred programming language or change resulting report look.

### Adapter
An adapter should be written for each concrete testing framework and must capture information about test cases and their results. Adapter saves results to an XML file, which is then read by universal generator in order to create report data. Every adapter should be very simple to use in testing framework and should collect as much information as possible. Minimum requirements to the collected data are:
* **Test suite** start and finish time
* **Test case** start, finish times and result (PASSED, SKIPPED, FAILED)
* For failed **test cases** we also need error message and stack trace
An adapter can optionally provide the following information:
* A set of **steps** in test case 
* A set of **attached files** related to step or test case
* Test case severity, description and title
We already have adapters for Java ([jUnit](#junit-adapter), [testNG](#testNG-adapter)), Python ([pyTest]) and Javascript ([Karma]). You may want to write a new adapter for any other testing framework.

### Report generator
Report generator converts XML file coming from adapter to JSON format which is then used in report face.

### Report face
Allure report face is a single HTML-page application which visualizes JSON data outputted by report generator.


## Tools based on Allure
### Allure Maven Plugin
Allows to generate Allure report using XML input data. To use the plugin simply add its as dependency to reporting section of your pom.xml file:
```xml
<reporting>
    <excludeDefaults>true</excludeDefaults>
    <plugins>
        <plugin>
            <groupId>ru.yandex.qatools.allure</groupId>
            <artifactId>allure-maven-plugin</artifactId>
            <version>${allure.version}</version>
            <configuration>
                <reportName>Custom Report Name</reportName>
                <reportGenerator>TODO: what the hell is it?</reportGenerator>
                <reportFace>
                    <groupId>com.example</groupId>
                    <artifactId>your-cool-face</artifactId>
                    <version>1.2.3</version>
                    <packaging>jar</packaging>
                </reportFace>
                <outputDirectory>/path/to/generated/report</outputDirectory>
                <allureResultsDirectory>/path/to/XML</allureResultsDirectory>
            </configuration>
        </plugin>
    </plugins>
</reporting>
```
**TODO:** add more config examples.
### Allure Command Line Tool
TODO: add description.
### Allure Jenkins Plugin
Allows to generate Allure report as Jenkins build artifact. TODO: add link to plugin.
### Allure Teamcity Plugin
Allows to generate Allure report as Teamcity build artifact. TODO: add link to plugin.

## FAQ and Troubleshooting
* **Should step/attachment name be unique?** - No. They are only display names.
* **I see nothing when opening report page in my browser. What's wrong?** - That means that report data failed to load. Possible solutions:
 * If you're opening file from local drive ensure that you have a web server installed (**file://** URLs are not supported)
 * If you get 404 error from remote server then check logs in order to verify that report was build successfully
 * If you still experience problems please email us **Debug** section contents and we'll help you to resolve the problem

## Contributing and Developing
### How can I contribute?
You could contribute in several different ways:
* Contribute to Allure Framework code by sending a pull-request
* Create an adapter for you preferred testing framework or language
* Contribute to project documentation
* Test the framework and post your review somewhere in the Web or just tweet the link to Allure

#### Contribute to Allure Framework
In order to build Allure you need:
* [JDK](http://java.sun.com/) 1.7 or above (tested with Oracle JDK on different platforms) to compile, target runtime version is still 1.5 or above
* [Maven](http://maven.apache.org/) 3.0 or above to build
* [PhantomJS](http://phantomjs.org/) 1.9 or above to run unit tests
When editing the code please configure your preferred IDE to use UTF-8 for all files and 4 spaces indentation. To build project you need to run:
```
$ mvn clean install
```
#### Create an adapter
If you're using Allure with your preferred testing framework which have no official adapter we'll be glad to review and officially publish your adapter code. Writing an adapter is as easy as outputting test properties to a particular XML format. We check XML with an XSD file so you can do the same in your language in order to check whether it's correct. See details below.
#### Contribute to project documentation
What about writing documentation in your native language? Feel free to send us pull-requests with README.*<language>*.md translations.
#### Post you review
We would like to make Allure a well known and widely used product. It would be great if you contribute by publishing an article with your opinion about Allure. Please email us a link to your article so we could consider it.

## Contact us
Contact [Charlie](mailto:charlie@yandex-team.ru) if you have questions, ideas or suggestions. **TODO:** add mailing list and public email.

## Changelog
TBD

## Implementation details
### Report generation
Report generator is located in **allure-report-data** module. Every time you run generator the following happens:
 * Generator validates input files using **XSD** schema
 * It converts this data to  **JSON** using **XSL** transformation. You can find respective XSLT file in this repository.
 * It adds some calculated data to the same JSON, e.g. failed tests count per **suite**
Input data directory for generator usually contains the following files:
 * {uid}-testsuite.xml which contains information about tests and attachment files paths
 * {uid}-attachment.* - attachment files which can have any extension
Output directory then contains:
 * {uid}-attachment.* - copied attachments
 * {uid}-testsuite.json - input XML files converted to JSON with added statistical information
### Report Face
Allure report face is a single page **angular.js** application. Please refer to its documentation for details.
### Aspect-oriented programming
Steps feature is implemented using aspect-oriented approach with the help of [AspectJ Framework](http://eclipse.org/aspectj/). All aspects are stored in **allure-java-aspects** module:
```xml
<dependency>
    <groupId>ru.yandex.qatools.allure</groupId>
    <artifactId>allure-java-aspects</artifactId>
    <version>${allure.adaptor.version}</version>
</dependency>
```
In order to reuse them you also need to add the following plugin to your **pom.xml**:
``` xml
<plugin>
    <groupId>org.codehaus.mojo</groupId>
    <artifactId>aspectj-maven-plugin</artifactId>
    <version>1.4</version>
    <configuration>
        <source>${compiler.version}</source>
        <target>${compiler.version}</target>
        <complianceLevel>${compiler.version}</complianceLevel>
        <aspectLibraries>
            <aspectLibrary>
                <groupId>ru.yandex.qatools.allure</groupId>
                <artifactId>allure-java-aspects</artifactId>
            </aspectLibrary>
        </aspectLibraries>
    </configuration>
    <executions>
        <execution>
            <goals>
                <goal>compile</goal>
                <goal>test-compile</goal>
            </goals>
        </execution>
    </executions>
    <dependencies>
        <dependency>
            <groupId>org.aspectj</groupId>
            <artifactId>aspectjtools</artifactId>
            <version>1.7.3</version>
        </dependency>
    </dependencies>
</plugin>
```
