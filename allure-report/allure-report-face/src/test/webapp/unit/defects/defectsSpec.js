/*global describe:true, it:true, beforeEach:true, afterEach:true, expect:true, spyOn:true, module:true, inject:true, angular:true, jasmine:true */
describe('DefectsCtrl', function () {
    "use strict";
    var scope;

    function DefectType(title, defects) {

    }
    function Defect(message, testcases) {
        this.failure = {message: message};
        this.testCases = testcases;
    }

    beforeEach(module('allure.defects'));
    beforeEach(module('allure.services'));
    beforeEach(inject(function ($controller, $rootScope) {
        this.$state = jasmine.createSpyObj('$state', ['go', 'is']);
        this.createController = function(defects) {
            var scope = $rootScope.$new();
            $controller('DefectsCtrl', {
                $scope: scope,
                $state: this.$state,
                defects: {defectsList: defects}
            });
            return scope;
        };
    }));

    beforeEach(function() {
        scope =this.createController([{
            title:'Test', status: 'BROKEN',
            defects: [
                new Defect('FileNotFoundException: No file found', [{uid: 4}]),
                new Defect('ArrayIndexOutOfBoundsException: 100500', [{uid: 5}])
            ]
        }, {
            title:'Product', status: 'FAILED',
            defects: [
                new Defect('Assertion Error: expected 3 to be 4', [{uid: 1}]),
                new Defect('Assertion Error: expected true to be false', [{uid: 2}]),
                new Defect('Assertion Error: expected value to be defined', [{uid: 3}])
            ]
        }]);
    });

    it('should order defect types by status', function() {
        expect(scope.defects[0].title).toBe('Product');
        expect(scope.defects[1].title).toBe('Test');
    });

    it('should not select testcase by default', function() {
        scope.$broadcast('$stateChangeSuccess', null, {});
        expect(scope.testcase).toBeUndefined();
    });

    it('should find and select testcase by uid', function() {
        scope.$broadcast('$stateChangeSuccess', null, {testcaseUid: 3});
        expect(scope.testcase.uid).toBe(3);
    });
});
