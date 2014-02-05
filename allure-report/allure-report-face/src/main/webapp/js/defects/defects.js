/*global angular*/
angular.module('allure.defects', []).controller('DefectsCtrl', function($scope, $state, orderByFilter, status, Collection, defects) {
    "use strict";
    $scope.isState = function(statename) {
        return $state.is(statename);
    };
    $scope.setTestcase = function(testcase) {
        $scope.testcase = testcase;
        $state.go('defects.testcase', {testcaseUid: testcase.uid});
    };
    $scope.select = function(direction) {
        var index = testcases.indexOf($scope.testcase),
            testcase = direction < 0 ? testcases.getPrevious(index) : testcases.getNext(index);
        $scope.setTestcase(testcase);
    };
    $scope.defects = orderByFilter(defects.defectsList, function(defectType) {
        return status.getSortOrder(defectType.status);
    });
    var testcases = new Collection($scope.defects.reduce(function(testcases, type) {
        return type.defects.reduce(function(testcases, defect) {
            return testcases.concat(defect.testCases);
        }, testcases);
    }, []));
    $scope.$on('$stateChangeSuccess', function(event, state, params) {
        delete $scope.testcase;
        if(params.testcaseUid) {
            $scope.setTestcase(testcases.findBy('uid', params.testcaseUid));
        }
    });
});