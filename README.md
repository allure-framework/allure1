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


## Contact us
Contact [Charlie](mailto:charlie@yandex-team.ru) if you have questions, ideas or suggestions. **TODO:** add mailing list and public email.

