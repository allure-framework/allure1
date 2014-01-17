[allure-junit-pom-example]: https://github.com/allure-framework/allure-core/blob/master/docs/allure-junit-pom-example.md
[steps-and-attachments]: https://github.com/allure-framework/allure-core/blob/master/docs/steps-and-attachments.md
[behaviors]: https://github.com/allure-framework/allure-core/blob/master/docs/behaviors.md

## Allure JUnit integration module

First of all add allure version property and dependency of **allure-junit-adaptor**:

```xml
<properties>
    <allure.version>1.3.0.RC2</allure.version>
</properties>

<dependency>
    <groupId>ru.yandex.qatools.allure</groupId>
    <artifactId>allure-junit-adaptor</artifactId>
    <version>${allure.version}</version>
</dependency>
```

then, add **maven surefire plugin** with next configuration:

```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-surefire-plugin</artifactId>
    <version>2.16</version>
    <configuration>
        <argLine>
            -javaagent:${settings.localRepository}/org/aspectj/aspectjweaver/${aspectj.version}/aspectjweaver-${aspectj.version}.jar
        </argLine>
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

In the end you need to add **allure-report-plugin** to reporting section:

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

### BDD

Also you have ability to group your test by **features** and **stories** (BDD-like). Just annotate test
sutie or test case with **@ru.yandex.qatools.allure.annotations.Fratures** or
**@ru.yandex.qatools.allure.annotations.Stories** annotations%

```java
@Features("My Feature")
@Stories("My Story")
@Test
public void myTest() {
    myStep();
}
```

Click [here][behaviors] to see more information about behaviors.
