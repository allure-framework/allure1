[model]: #
[jUnit]: #
[pytest]: #
[Karma]: #
[Maven]: #
[Jenkins]: #
[angular-js]: http://angularjs.org/
[bootstrap]: http://getbootstrap.com/

## How it works

Report generator has a module structure:

1.	**[Adapter](#adapter)**. Plugin to some of test framework, which provide information about test process
2.	**[Model](#model)**. Structure description of provided data and generated report
3.	**[Report generator](#report-generation)**. Creates a report from a test results data
4.	**[Report face](#report-face)**. Client-side application which represents the data in a human readable form

This modules are independent. They interaction only through reported data. It allows to replace any of these modules and use an adapter for **your preferred programming language** or **change view of resulting report**.



### Adapter

Adapter writes specially for the concrete test framework and must capture information about the test cases and their results. Adapter saves results in to XML, which then will been read by generator to create report. It is important that adapter must be easy to use and collect as much information as possible.

Minimum requirements to the collected data by adapter are:

* **Test suite** start and finish
* **Test case** start, finish and result (PASSED, SKIPPED, FAILED)
* For failed **test cases** need error message and stack trace

Optionally adapter can provide additional info:
* **Steps** in test case 
* **Attached files** referecned to step or test case
* Assign testcase severity, descrtiption, title 
* *new features coming soon*

Now we have adapters to Java ([jUnit]), Python ([pyTest]) and Javascript ([Karma]). You may write new adapter to any other test framework, it is very simple.

### Model.

Model has two layers

* Test results - the minimum necessary data which is need to build reports. Adapters save data in this format. More info about [test model][model]
* Test report - the more expanded results, with additional info such as statistics, duration of tests (computed from start and stop times). This format generates from test results in *report generator* and are used in report view.

This approach allows to split functionality and eliminates need to make a lot of calculations in adapters or face. All conversions are concentrated in the report generator. It allows to avoid duplicate of functionality in the different modules and just make conversions through **XSL**-transformation.


### Report generation

The second phase after test run &mdash; it is report generation. Generator's logic is described in XSLT format and can be implemented in any environment which support it. Now we offer plugins to [Maven] and [Jenkins] which are in fact wraps xslt-stylesheet. Include one of these plugins in to your test process and you will get detailed report about test results.

### Report face

This is owr frontend &mdash; an single-page application, which loads json-data from generator and create human-readble report using client-side templating. Face build with [AngularJS][angular-js] and allows you to see the results in the form convenient for you. We work with modern browser features such as local storage and client-side routing, so you can save your preferences about report view


