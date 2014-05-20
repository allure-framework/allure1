/*global angular*/
angular.module('allure.defects', []).controller('DefectsCtrl', function($scope, $state, orderByFilter, status, WatchingStore, Collection, defects) {
    "use strict";
    var store = new WatchingStore('defectsSettings');
    $scope.isState = function(statename) {
        return $state.is(statename);
    };
    $scope.setDefect = function(defect) {
        //TODO @charlie: here should be:
        //$state.go('defects.defect', {defectUid: defect.uid});
        $state.go('defects.defect', {defectUid: defect.failure.message});
    };
    $scope.select = function(direction) {
        var index = defectList.indexOf($scope.defect),
            defect = direction < 0 ? defectList.getPrevious(index) : defectList.getNext(index);
        $scope.setDefect(defect);
    };
    $scope.defects = orderByFilter(defects.defectsList, function(defectType) {
        return status.getSortOrder(defectType.status);
    });
    $scope.showStatuses = {};
    $scope.testcase = {};
    $scope.sorting = {
        predicate: store.bindProperty($scope, 'sorting.predicate', 'statistic.failed'),
        reverse: store.bindProperty($scope, 'sorting.reverse', false)
    };
    status.all.forEach(function(status) {
        $scope.showStatuses[status] = true;
    });
    var defectList = new Collection($scope.defects.reduce(function(defects, type) {
        type.defects.forEach(function(defect) {
            defect.type = type.status;
        });
        return defects.concat(type.defects);
    }, []));
    $scope.$watch('testcase.uid', function(testcaseUid, oldUid) {
        if(testcaseUid && testcaseUid !== oldUid) {
            $state.go('defects.defect.testcase', {testcaseUid: testcaseUid});
        }
    });
    $scope.$on('$stateChangeSuccess', function(event, state, params) {
        delete $scope.testcase.uid;
        delete $scope.defect;
        if(params.defectUid) {
            //TODO @charlie: here should be:
            //$scope.defect = defectList.findBy('uid', params.defectUid);
            $scope.defect = defectList.items.filter(function(defect) {
                return defect.failure.message === params.defectUid;
            })[0];
        }
        if(params.testcaseUid) {
            $scope.testcase.uid = params.testcaseUid;
        }
    });
});
