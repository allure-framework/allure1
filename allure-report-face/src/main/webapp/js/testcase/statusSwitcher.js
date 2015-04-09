/*global angular*/
angular.module('allure.core.testcase.statusSwitcher', []).directive('statusSwitcher', function($storage, status) {
    "use strict";
    function StatusesStore() {
        function defaults(value, defaultVal) {
            return value !== null ? value : defaultVal;
        }
        var store = this.store = $storage('visibleStatuses');
        this.statuses = {
            PASSED:  defaults(store.getItem('PASSED'), false),
            PENDING: defaults(store.getItem('PENDING'), false),
            CANCELED: defaults(store.getItem('CANCELED'), true),
            BROKEN:  defaults(store.getItem('BROKEN'), true),
            FAILED:  defaults(store.getItem('FAILED'), true)
        };
    }
    StatusesStore.prototype.toggleStatus = function(status) {
        var value = !this.statuses[status];
        this.statuses[status] = value;
        this.store.setItem(status, value);
    };
    var store = new StatusesStore();
    return {
        templateUrl: 'templates/testcase/status-switcher.html',
        scope: {
            statuses: '=',
            statistic: '='
        },
        link: function($scope) {
            $scope.statuses = store.statuses;
            $scope.sortedStatuses = angular.copy(status.all);
            $scope.toggle = function(status) {
                store.toggleStatus(status);
            };
            $scope.getStatistic = function(status) {
                return $scope.statistic ? '('+$scope.statistic[status.toLowerCase()]+')' : '';
            };
            $scope.getClassName = function(status) {
                var cls = ['btn-status-'+status.toLowerCase()];
                if($scope.statuses[status]) {
                    cls.push('active');
                }
                return cls.join(' ');
            };
        }
    };
});
