describe("widget directive", function() {
    "use strict";
    beforeEach(module('allure.core.widgets'));
    beforeEach(inject(function($compile, $rootScope) {
        this.createWidget = function(options, data) {
            var element = angular.element('<div widget="options" data="data"></div>');
            var scope = $rootScope.$new();
            scope.options = options;
            scope.data = data;
            $compile(element)(scope);
            scope.$apply();
            return element;
        };
    }));

    it("should render widget with inline template", function() {
        var element = this.createWidget({
            template: '<div>{{ctrl.value}} {{data}}</div>',
            controller: function($scope) {
                $scope.ctrl = this;
                this.value = 'from controller';
            }
        }, 'hello');
        expect(element.find('div').html()).toEqual('from controller hello');
    });

    it("should load template by url", inject(function($templateCache) {
        $templateCache.put('widgetContent.html', '{{widget.name}}');
        var element = this.createWidget({
            templateUrl: 'widgetContent.html',
            name: 'hello'
        });
        expect(element.text()).toEqual('hello');
    }));
});
