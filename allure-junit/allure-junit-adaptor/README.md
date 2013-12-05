[aspect-j]: http://eclipse.org/aspectj/
[allure-junit-plugin]: https://github.com/allure-framework/allure-core/blob/master/allure-junit/allure-junit-plugin/README.md
[junit-rules]: https://github.com/junit-team/junit/wiki/Rules
[allure-java-aspects]: https://github.com/allure-framework/allure-core/blob/master/allure-java/allure-java-aspects/README.md

##Allure-junit-adaptor

За основу взят механизм [*JUnit Rules*][junit-rules], а если точнее TestWatcher.
Реализованы две рулы - *TestCaseReportRule* и *TestSuiteReportRule*. Первая отвечает за сохранение данных о выполнении
*тесткейсов*, вторая - о *тестсуитов*. Чтобы подключить **allure**, достаточно добавить их в ваши тесты.

``` java
@ClassRule
public static TestSuiteReportRule testSuiteReportRule = new TestSuiteReportRule();

@Rule
public TestCaseReportRule testCaseReportRule = new TestCaseReportRule(testSuiteReportRule);
```

Либо вы можете воспользоваться [плгином][allure-junit-plugin].

### Аннотации.

Name | Target | Parameters | Description
--- | --- | --- | ---
**@Step** | method | String value() | Аннотация, которой помечаются степовые методы. Значение - имя соответствующего степа, может содержать в себе переменные *{method}, {0}, {1}*, etc. - имя метода и его аргументы соответственно. По умолчанию значение *{method}*.
**@Attach** | method | String name, AttachmentType type, String suffix| Аннотация, которой помечаются методы, сохраняющие attachments. Имя может содержать в себе переменные *{method}, {0}, {1}*, etc. - имя метода и его аргументы соответственно, тип может быть TXT, XML, HTML, PNG, JPG, OTHER. Метод должен возвращать *java.lang.String* или *java.io.File*. TXT, XML, HTML в отчете будут в iframe, PNG, JPG - в теге img. Для типа OTHER - только ссылка для скачивания файла. По умолчанию имя - *{method}*, тип - *AttachmentType.TXT*.
**@Title** | TestCase, TestSuite | String value() | Меняет имя теста.
**@Description** | TestCase, TestSuite | String value() | Добавляет описание.
**@Severity** | TestCase | SeverityType value() | С помощью этой аннотации изменить установить важность теста. Возможные значения - *TRIVIAL*, *MINOR*, *NORMAL*, *CRITICAL* и *BLOCKER*. По умолчанию -  *NORMAL*.
