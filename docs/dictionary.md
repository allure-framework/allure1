[xUnit]: http://en.wikipedia.org/wiki/XUnit
## Allure glossary

Allure represents extended [xUnit] schema with following additions.

**Test suite** - test class, includes test methods (test cases)

**TestCase** - test method (like in [xUnit]). It has these fields:
 1. **title** - test case name.
 3. **status** - result of execution, see **status**.
 4. **times** - test timestamps, see **times**.
 5. **attachments** - list of attachments, see **attachment**.

**Test pack** - test classes set. All **test suites** which have run in the test launch are grouping in one test pack.

**Step** - test phase, test case can include one or more steps.

**Status** - result of step or testcase, which has these values:
 1. **passed** - all assertions have passed right
 2. **failed** - assertion hasn't passed
 3. **broken** - test broken, unexpected exception was thrown during test run
 4. **skipped** - test skipped

**Severity** - *importance* of test case. It helps to sort their by importance. Can be one of these values (ascending of importance): *TRIVIAL*, *MINOR*, *NORMAL*, *CRITICAL* и *BLOCKER*.

**Times** - timestamps of testrun, denotes beginning and ending of step, testcase or testsuite.
This data stores in start, end and duration fields respectively. Timestaps using to display testcases on **timeline**

**Attachment** - file with additional information captured during a test such as log, screenshot, dump etc