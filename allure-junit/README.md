[allure-junit-pom-example]: https://github.com/allure-framework/allure/blob/master/docs/allure-junit-pom-example.md
[steps-and-attachments]: https://github.com/allure-framework/allure/blob/master/docs/steps-and-attachments.md

## Allure JUnit integration module

To use **Allure** with **JUnit**, just add following code to your **pom.xml**:

``` xml
<!--Allure version, needed here for allure-maven-plugin. It can be moved to parent pom.-->
<properties>
    <allure.version>1.2.2</allure.version>
</properties>

<!--This dependency is necessary for Allure JUnit plugin. It can be moved to parent pom.-->
<dependencies>
    <dependency>
        <groupId>ru.yandex.qatools.allure</groupId>
        <artifactId>allure-junit-adaptor</artifactId>
        <version>${allure.version}</version>
    </dependency>
</dependencies>

<!--Allure JUnit plugin. It can be moved to parent pom. -->
<build>
    <plugins>
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
    </plugins>
</build>

<!--Allure Maven Plugin. It can be moved to parent pom-->
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

### Steps and attachments

You can add steps to your tests just annotate step-methods with
**@ru.yandex.qatools.allure.annotations.Step** annotation:

```java
@Step
public void openPageByAddress(String pageAddress) {
     ...
 }
```

If you need to save attachment until the test is performed you can create attach-method.
This method should be annotated with **@ru.yandex.qatools.allure.annotations.Attach** and
return **java.lang.String** or **java.io.File**.

Returned value will be copied and added to your Allure Report as attachment.

```java
@Attach
public String saveLog(Logger logger) {
    ...
}
```

Click [here][steps-and-attachments] to see more information about steps and attachments.