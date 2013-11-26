## Allure Maven Plugin

Maven plugin, строющий **Allure report** на основе сгенерированных данных. 

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

### Configuration

Имеет следующие настройки

1. **reportName** - позволяет изменять имя сгенерированного отчета. 
2. **reportFace** - позволяет подключать другой *report-face*. Настройка должна содержать данные о артефакте:
  * *groupId*
  * *artifactId*
  * *version*
  * *packaging*
3. **reportGenerator** - позволяет выбрать кастомный репорт генератор. 
