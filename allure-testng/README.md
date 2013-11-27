[allure-junit-pom-example]: https://github.com/allure-framework/allure/blob/master/docs/allure-junit-pom-example.md
[steps-and-attachments]: https://github.com/allure-framework/allure/blob/master/docs/steps-and-attachments.md

## Allure TestNG integration module

First of all you should add following code to your pom.xml:

``` xml
<!--Allure version, needed here for allure-maven-plugin. It can be moved to parent pom.-->
<properties>
    <allure.version>1.2.2</allure.version>
</properties>

<!--This dependency is necessary for Allure TestNG plugin. It can be moved to parent pom.-->
<dependencies>
    <dependency>
        <groupId>ru.yandex.qatools.allure</groupId>
        <artifactId>allure-testng-adaptor</artifactId>
        <version>${allure.version}</version>
    </dependency>
</dependencies>

<!--Allure TestNG plugin. It can be moved to parent pom. -->
<build>
    <plugins>
        <plugin>
            <groupId>ru.yandex.qatools.allure</groupId>
            <artifactId>allure-testng-plugin</artifactId>
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

        <!--Without surefire plugin TestNG test tests will run in one Suite with name "Common Suite" -->
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-surefire-plugin</artifactId>
            <version>2.16</version>
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

Then, you should add AllureTestListener to all your TestNG tests:

``` java
@Listeners(AllureTestListener.class)
public class SimpleTest {
    ...
}

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