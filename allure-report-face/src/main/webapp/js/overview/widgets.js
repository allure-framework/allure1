(function() {
    var module = angular.module('allure.core.widgets', []);
    module.directive('widget', function($controller, $compile) {
        "use strict";
       return {
           scope: {
               widget: '=',
               data: '='
           },
           link: function(scope, elm) {
               var template = scope.widget.templateUrl ? '<ng-include src="\'' + scope.widget.templateUrl + '\'"/>' : scope.widget.template;
               var content;
               if(template) {
                   content = angular.element(template);
                   elm.append(content);
                   scope = scope.$new();
                   $compile(content)(scope);
               }
               if(scope.widget.controller) {
                   $controller(scope.widget.controller, {
                       $scope: scope,
                       $element: content,
                       data: scope.data
                   });
               }
           }
       };
    });
    module.controller('TotalWidgetController', function($scope, percents) {
        "use strict";
        $scope.percents = percents($scope.data.statistic);
    });
    module.controller('KeyValueWidgetController', ['$scope', function($scope) {
        $scope.ctrl = this;
        this.dataLimit = 10;
        this.showAll = function() {
            this.dataLimit = $scope.data.length;
        };
    }]);
})();
