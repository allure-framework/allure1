[allure-dashboard-image]: https://github.com/allure-framework/allure-core/blob/master/docs/allure-dashboard.png
[allure-teamcity-release-artifact]: http://teamcity.qatools.ru/repository/download/allure_core_master_release/473:id/index.html
[junit-adaptor]: https://github.com/allure-framework/allure-core/blob/master/allure-junit/allure-junit-adaptor/README.ru.md
[testng-adaptor]: https://github.com/allure-framework/allure-core/blob/master/allure-testng/allure-testng-adaptor/README.ru.md
[pytest-adaptor]: https://github.com/allure-framework/allure-python/README.md

**Allure Framwork** - понятные отчеты автотестов

## Allure Framework

Согласитесь круто, когда результаты тестирования понятны не только автору автотестов, но и всей команде.
Существует множество фреймворков на разных языках, упрощающие написание тестов, но немногие из них могут
похвастаться качественной системой представления их результатов. Именно для этого мы разрабатываем **Allure** -
фреймворк для создания понятных отчетов автотестов. Он с легкостью адаптируется под любой инструмент на любом языке.

### Демонстрационный отчет

![image][allure-dashboard-image]
Посмотреть и покликать отчет можно по [ссылке][allure-teamcity-release-artifact].
Если попросит авторизацию, то нужно нажать по ссылке [Log in as guest] в футере формы авторизации.

### Существующие адаптеры

Если вы используете jUnit и TestNG, то вам несказанно повезло :)
[jUnit Adaptor][junit-adaptor] и [TestNG Adaptor][testng-adaptor] являюся основными и они всегда имеют актуальную версию.

Чтобы подключить Allure к вашим тестам прочитайте следующие статьи:
* [Как подключить Allure к jUnit-тестам?][junit-adaptor]
* [Как подключить Allure к TestNG-тестам?][testng-adaptor]
* [Как подключить Allure к pyTest-тестам?][pytest-adaptor]

