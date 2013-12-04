[maven-aspectj-plugin]: https://github.com/allure-framework/allure-core/blob/master/allure-java/allure-java-aspects/README.md
[allure-junit-adaptor]: https://github.com/allure-framework/allure-core/blob/master/allure-junit/allure-junit-adaptor/README.md

## Allure Junit Plugin

Плагин, подключающий Allure к вашим тестам. Чтобы подключить, надо добавить в свой *pom.xml* следующее:

```xml
<plugin>
    <groupId>ru.yandex.qatools.allure</groupId>
    <artifactId>allure-junit-plugin</artifactId>
    <version>${allure.version}</version>
    <executions>
        <execution>
            <phase>test-compile</phase>
            <goals>
                <goal>allure</goal>
            </goals>
        </execution>
    </executions>
</plugin>
```

По сути, все что делает - вызывает сначала [maven-aspectj-plugin][maven-aspectj-plugin], потом в тестовые классы 
(класс не должен быть абстрактным или интерфейсом), в которых хотя бы один *public* метод, помеченный аннотацией
*@org.junit.Test* инструментируются рулы **TestCaseReportRule** и **TestSuiteReportRule**. Подробнее про эти рулы можно прочить
[здесь][allure-junit-adaptor].

По умолчанию инструментируются все **.class** файлы в директориях *${project.build.outputDirectory}*
и *${project.build.testOutputDirectory}*.
