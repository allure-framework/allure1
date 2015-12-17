<!doctype html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta http-equiv="Cache-Control" content="no-cache, no-store, must-revalidate" />
    <meta http-equiv="Pragma" content="no-cache" />
    <meta http-equiv="Expires" content="0" />
    <meta http-equiv="X-UA-Compatible" content="IE=edge" /> 
    <title>Allure Dashboard</title>
    <link rel="shortcut icon" href="img/favicon.ico" type="image/x-icon">
    <link rel="icon" href="img/favicon.ico" type="image/x-icon">
    <link rel="stylesheet" href="css/app.css"/>

    <script src="allure-core.min.js"></script>

    <#--Plugins-->
    <#list plugins as plugin>
        <script src="plugins/${plugin}/script.js"></script>
    </#list>

</head>
<body class="b-page" ng-app="allure">
<header class="b-page__nav navbar navbar-static-top" role="navigation" ng-controller="NavbarCtrl">
    <div class="nav navbar-header">
        <a class="navbar-brand allure-logo" href="#">Allure</a>
    </div>
    <div class="nav navbar-text">
        <a ng-href="{{report.url}}" ng-if="report.url" target="_top">
            {{report.name}}
        </a>
        <span ng-if="!report.url">
            {{report.name}}
        </span>
    </div>
    <ul class="nav navbar-nav navbar-right">
        <li class="dropdown feedback">
            <a class="clickable" dropdown-toggle>{{'index.FEEDBACK' | translate}} <b class="caret"></b></a>
            <div class="b-popup b-island b-island_mode_fly b-popup_show_bottom-center" role="menu">
                <div class="b-popup__content text-center">
                    <a class="feedback_item" target="_blank" href="https://github.com/allure-framework/allure-core/wiki/FAQ-and-Troubleshooting">FAQ</a>
                    <a class="feedback_item" target="_blank" href="https://github.com/allure-framework/allure-core/issues/new">GitHub</a>
                    <a class="feedback_item" href="mailto:allure@yandex-team.ru">{{'index.MAIL' | translate}}</a>
                </div>
                <div class="b-popup__tail"></div>
            </div>
        </li>
    </ul>
    <div class="nav navbar-nav navbar-right">
        <li class="dropdown">
            <a class="clickable" dropdown-toggle>{{(langs | filter:{locale: selectedLang})[0].name}}<b class="caret"></b></a>
            <div class="b-popup b-island b-island_mode_fly b-popup_show_bottom-center" role="menu">
                <div class="b-popup__content text-left">
                    <a ng-repeat="lang in langs" class="feedback_item clickable" data-ng-click="setLang(lang)" ng-class="{'feedback_item-checked': lang.locale == selectedLang}">
                        <span class="fa fa-check"></span>{{lang.name}}
                    </a>
                </div>
                <div class="b-popup__tail"></div>
            </div>
        </li>
    </div>
    <div class="nav navbar-text navbar-right">
        <strong>{{'index.VERSION' | translate}}</strong> ${version}
    </div>
    <div class="nav navbar-text navbar-right">
        <strong>{{'index.DATA_SIZE' | translate}}</strong> {{report.size | filesize}}
    </div>
    <div class="nav navbar-text navbar-right">
        <strong>{{'index.GENERATED' | translate}}</strong> {{report.time | time}}
    </div>
</header>
<div class="loader"></div>
<div class="dashboard" ng-controller="TabsController">
    <div class="b-vert" ng-class="{'b-vert_collapsed': isCollapsed() }">
        <ul class="list-unstyled">
            <li ng-repeat="tab in tabs" class="b-vert__item"
                title="{{tab.title | translate}}"
                ng-class="{'b-vert__item_state_selected': isCurrent(tab.name)}">
                <a ui-sref="{{tab.name}}" class="b-vert__title">
                    <div class="b-vert__icon" ng-class="tab.icon"></div>
                    {{tab.title | translate}}
                </a>
            </li>
        </ul>
        <div title="{{'index.COLLAPSE' | translate}}" class="b-vert__item b-vert__collapse"  ng-click="toggleCollapsed()">
            <div class="b-vert__title">
                <div class="b-vert__icon"></div>
            </div>
        </div>
    </div>
    <div ui-view class="tab-content"></div>
</div>
<div class="alert alert-warning error-message" ng-show="error">
    <p>Your report seems to be broken! Check out
        <a href="https://github.com/allure-framework/allure-core/wiki/FAQ-and-Troubleshooting">our FAQ</a> to find a reason
    </p>
    <p>Message: {{error.status}}</p>
    <hr/>
    <p>Debug: <span class="long-line" ng-bind="error|json"></span></p>
</div>
</body>
</html>
