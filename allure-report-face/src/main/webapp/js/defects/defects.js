/*global angular*/
angular.module('allure.defects', []).controller('DefectsCtrl', function($scope, $state, orderByFilter, status, WatchingStore, Collection, defects) {
    "use strict";
    $scope.defects = orderByFilter(defects.defectsList, function(defectType) {
        return status.getSortOrder(defectType.status);
    });
    var store = new WatchingStore('defectsSettings'),
        defectList = new Collection($scope.defects.reduce(function(defects, type) {
            type.defects.forEach(function(defect) {
                defect.type = type.status;
            });
            return defects.concat(type.defects);
        }, []));
    $scope.isState = function(statename) {
        return $state.is(statename);
    };
    $scope.setDefect = function(defect) {
        $state.go('defects.defect', {defectUid: defect.uid});
    };
    $scope.select = function(direction) {
        var index = defectList.indexOf($scope.defect),
            defect = direction < 0 ? defectList.getPrevious(index) : defectList.getNext(index);
        $scope.setDefect(defect);
    };
    $scope.showStatuses = {};
    $scope.testcase = {};
    $scope.sorting = {
        predicate: store.bindProperty($scope, 'sorting.predicate', 'statistic.failed'),
        reverse: store.bindProperty($scope, 'sorting.reverse', false)
    };
    status.all.forEach(function(status) {
        $scope.showStatuses[status] = true;
    });
    $scope.$watch('testcase.uid', function(testcaseUid, oldUid) {
        if(testcaseUid && testcaseUid !== oldUid) {
            $state.go('defects.defect.testcase', {testcaseUid: testcaseUid});
        }
    });
    $scope.$on('$stateChangeSuccess', function(event, state, params) {
        delete $scope.testcase.uid;
        delete $scope.defect;
        if(params.defectUid) {
            $scope.defect = defectList.findBy('uid', params.defectUid);
        }
        if(params.testcaseUid) {
            $scope.testcase.uid = params.testcaseUid;
        }
    });
});
