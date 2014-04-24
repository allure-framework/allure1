# Allure
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

## Glossary
Allure follows [xUnit](http://en.wikipedia.org/wiki/XUnit) testing approach with minor additions.
1. **Test case** - test method. It has the following fields:
 * **title** - test case name
 * **status** - test execution result, see **Status** section for details.
 * **times** - test timestamps, see **Times** section for details.
 * **attachments** - list of attachments, see **Attachment** section for details.
2. **Test suite** - test class including a set of test cases.
3. **Test pack** -  a set of test suites. All **test suites** which were run in the same test launch are grouped in one test pack.
4. **Step**. Steps are any actions which constitute testing scenario. Steps can be used in different testing scenarios. They can be parametrized, can do some checks, can have nested steps and create attachments. Each step has a name.    
5. **Attachment**. A file with additional information captured during a test such as log, screenshot, log file, dump, server response and so on. When some test fails attachments help to understand the reason of failure faster.
6. **Status** - result of step or test case, which has one of the following values:
 * **passed** - all assertions have passed right
 * **failed** - assertion hasn't passed
 * **broken** - test broken, unexpected exception was thrown during test run
 * **skipped** - test skipped
7. **Severity** - *importance* of test case. It helps to sort them by importance. Can be one of the following values (in importance ascending order):
 * **TRIVIAL**
 * **MINOR**
 * **NORMAL**
 * **CRITICAL**
 * **BLOCKER**
8. **Times** - timestamps of test run, denotes beginning and ending of test step, test case or test suite. This data is being stored in **start**, **end** and **duration** fields respectively. Timestaps are used to when displaying test cases on report **timeline**
9. **Features** and **Stories**. Allure gives you an ability to create tests based on functional requirements. You can define requirements for your application and create tests which check concrete functionality or user story. We provide behavior-driven development (BDD) terms for help your behaviors definition. Your user scenarios can be grouped by stories, then stories are grouped by features. If you are using BDD approach, you need to do the following:
 * Split project into features
 * Find out some user stories for every feature
 * Define and implement testing scenarios
Then you will get a report clearly stating which features in the project have bugs. To learn how to use this API in your language see respective docs.

## Usage
### Java testing frameworks
#### How to define steps
In order to define steps in Java code you need to annotate respective methods with **@Step** annotation. When not specified step name equals to annotated method name converted to human readable format. To define explicit step name:
```java
@Step("Open '{0}' page.")
public void openPageByAddress(String pageAddress) {
     ...
 }
```
Step name can contain placeholders: any {i} will be replaced by ith method argument and {method} - with method name.
#### Attachments
Similarly an attachment is simply a method annotated with **@Attach** returns either String or File which should be added to report:
```java
@Attach
public String performedActions(ActionSequence actionSequence) {
    return actionSequence.toString();
}
@Attach(name = "Page screenshot", type = AttachmentType.JPG)
public static File saveScreenshot(File screenShot) {
    return screenShot;
}
```
Like with **@Step** annotation you can use *{method}* and  *{i}* placeholders. Type can be one of *TXT*, *XML*, *HTML*, *PNG*, *JPG*, *OTHER*. Attachments having TXT, XML or HTML type will be loaded in **iframe**, PNG, JPG - will be wrapped by **img** HTML tag. When type = OTHER method should return an URL to download the file.
#### Features and stories
In order to group your tests by **features** and **stories** simply annotate test suite or test case with **@Features** or **@Stories** annotations. Each of these annotations can take either one string or a string array because one test case can relate to several features or stories:
```java
@Features("My Feature")
@Stories({"Story1", "Story2"})
@Test
public void myTest() {
    myStep();
}
```
#### Parameters
You can add parameters to your tests and they will be shown in Allure report:
```java
public void myTest() {
    ...
    Allure.LIFECYCLE.fire(new AddParameterEvent("some_name", "some_value"));
    ...
}
```
#### Other annotations
Name | Target | Parameters | Description
--- | --- | --- | ---
**@Step** | method | String value | Defines a step
**@Attach** | method | String name, AttachmentType type, String suffix| Defines an attachment
**@Title** | TestCase, TestSuite | String value() | Меняет имя теста.
**@Description** | TestCase, TestSuite | String value() | Добавляет описание.
**@Severity** | TestCase | SeverityType value() | С помощью этой аннотации изменить установить важность теста. Возможные значения - *TRIVIAL*, *MINOR*, *NORMAL*, *CRITICAL* и *BLOCKER*. По умолчанию -  *NORMAL*.
#### Using Allure with JUnit
In order to use Allure with JUnit you need to add some dependencies to your Maven project. First add Allure JUnit adapter itself:
```xml
<properties>
    <allure.version>{latest allure version}</allure.version>
</properties>
<dependency>
    <groupId>ru.yandex.qatools.allure</groupId>
    <artifactId>allure-junit-adaptor</artifactId>
    <version>${allure.version}</version>
</dependency>
```
Then add **Maven Surefire Plugin** with the following configuration:
```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-surefire-plugin</artifactId>
    <version>2.14</version>
    <configuration>
        <argLine>-javaagent:${settings.localRepository}/org/aspectj/aspectjweaver/${aspectj.version}/aspectjweaver-${aspectj.version}.jar</argLine>
        <properties>
            <property>
                <name>listener</name>
                <value>ru.yandex.qatools.allure.junit.AllureRunListener</value>
            </property>
        </properties>
    </configuration>
    <dependencies>
        <dependency>
            <groupId>org.aspectj</groupId>
            <artifactId>aspectjweaver</artifactId>
            <version>${aspectj.version}</version>
        </dependency>
    </dependencies>
</plugin>
```
Finally add **allure-maven-plugin** to reporting section:
```xml
<reporting>
    <excludeDefaults>true</excludeDefaults>
    <plugins>
        <plugin>
            <groupId>ru.yandex.qatools.allure</groupId>
            <artifactId>allure-maven-plugin</artifactId>
            <version>${allure.version}</version>
        </plugin>
    </plugins>
</reporting>
```
#### Using Allure with TestNG
Similarly to JUnit you need to add the **allure-testng-adaptor** to your Maven project:
```xml
<properties>
    <allure.version>{latest allure version}</allure.version>
</properties>
<dependencies>
    <dependency>
        <groupId>ru.yandex.qatools.allure</groupId>
        <artifactId>allure-testng-adaptor</artifactId>
        <version>${allure.version}</version>
    </dependency>
</dependencies>
```
Also add **Maven Surefire Plugin**:
```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-surefire-plugin</artifactId>
    <version>2.14</version>
    <configuration>
        <argLine>
            -javaagent:${settings.localRepository}/org/aspectj/aspectjweaver/${aspectj.version}/aspectjweaver-${aspectj.version}.jar
        </argLine>
        <properties>
            <property>
                <name>listener</name>
                <value>ru.yandex.qatools.allure.testng.AllureTestListener</value>
            </property>
        </properties>
    </configuration>
    <dependencies>
        <dependency>
            <groupId>org.aspectj</groupId>
            <artifactId>aspectjweaver</artifactId>
            <version>${aspectj.version}</version>
        </dependency>
    </dependencies>
</plugin>
```
Finally add **allure-report-plugin** to reporting section:
```xml
<reporting>
    <excludeDefaults>true</excludeDefaults>
    <plugins>
        <plugin>
            <groupId>ru.yandex.qatools.allure</groupId>
            <artifactId>allure-maven-plugin</artifactId>
            <version>${allure.version}</version>
        </plugin>
    </plugins>
</reporting>
```
### Python
#### Using Allure with PyTest
See [this document](https://github.com/allure-framework/allure-python/README.md) for details.

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