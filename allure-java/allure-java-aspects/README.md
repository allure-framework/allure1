## Allure Java Aspects

Механизм степов реализован с помощью АОП.

Для вшивания в ваш код аспектов используется фреймворк [AspectJ](http://eclipse.org/aspectj/).

Для подключения, следует добавить модуль с аспектами в зависимости 

``` xml
<dependency>
    <groupId>ru.yandex.qatools.allure</groupId>
    <artifactId>allure-java-aspects</artifactId>
    <version>${allure.adaptor.version}</version>
</dependency>
```

и подключить плагин:

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
