angular.module('allure.xUnit.controllers', ['allure.xUnit.statusSwitcher'])
    .controller('HomeCtrl', function ($scope, $state, testsuites, treeUtils) {
        'use strict';
        function setTestsuite(testsuiteUid) {
            var testsuite = $scope.testsuites.filter(function(testsuite) {
                return testsuite.uid === testsuiteUid
            })[0];
            if($scope.testsuite !== testsuite) {
                $scope.testsuite = testsuite;
                $scope.testcases = testsuite.testCases;
            }
        }
        function setTestcase(testcaseUid) {
            $scope.testcase = $scope.testcases.filter(function(testcase) {
                return testcase.uid === testcaseUid;
            })[0];
        }
        function setAttachment(attachmentUid) {
            var attachment;
            treeUtils.walkAround($scope.testcase, 'steps', function(item) {
                attachment = item.attachments.filter(function(attachment) {
                    return attachment.source === attachmentUid;
                })[0];
                return !attachment;
            });
            //noinspection JSUnusedAssignment
            $scope.attachment = attachment;
        }

        $scope.setTestsuite = function(testsuiteUid) {
            $state.go('home.testsuite', {testsuiteUid: testsuiteUid});
        };
        $scope.setTestcase = function(testcaseUid) {
            $state.go('home.testsuite.testcase', {testcaseUid: testcaseUid});
        };
        $scope.setAttachment = function(attachmentUid) {
            $state.go('home.testsuite.testcase.attachment', {attachmentUid: attachmentUid});
        };
        $scope.isState = function(statename) {
            var parts = statename.split('.');
            if(parts[parts.length-1] === "*") {
                parts.pop();
                statename = parts.join('.');
                return $state.includes(statename) && !$state.is(statename);
            }
            return $state.is(statename);
        };
        $scope.testsuites = testsuites.testSuites;
        $scope.time = testsuites.time;
        $scope.statistic = $scope.testsuites.reduce(function(statistic, testsuite) {
            ['passed', 'skipped', 'broken', 'failed', 'total'].forEach(function(status) {
                statistic[status] += testsuite.statistic[status]
            });
            return statistic;
        }, {
            passed: 0, skipped: 0, failed: 0, broken: 0, total: 0
        });
        $scope.$on('$stateChangeSuccess', function(event, state, params) {
            delete $scope.testsuite;
            delete $scope.testcase;
            delete $scope.attachment;
            if(params.testsuiteUid) {
                setTestsuite(params.testsuiteUid);
            }
            if(params.testcaseUid) {
                setTestcase(params.testcaseUid);
            }
            if(params.attachmentUid) {
                setAttachment(params.attachmentUid);
            }
        });
    })

    .controller('TestSuitesCtrl', function($scope, WatchingStore) {
        'use strict';
        var store = new WatchingStore('testSuitesSettings');
        $scope.statusFilter = function(testsuite) {
            var visible = false;
            angular.forEach(testsuite.statistic, function(value, status) {
                visible = visible || ($scope.showStatuses[status.toUpperCase()] && value > 0);
            });
            return visible;
        };
        $scope.showStatuses = {};
        $scope.sorting = {
            predicate: store.bindProperty($scope, 'sorting.predicate', 'statistic.failed'),
            reverse: store.bindProperty($scope, 'sorting.reverse', false)
        };
    })

    .controller('TestCasesCtrl', ['$scope' , 'WatchingStore', 'status', 'severity', function ($scope, WatchingStore, status, severity) {
        'use strict';
        var store = new WatchingStore('testCasesSettings');
        $scope.bySeverity = function(testcase) {
            return severity.getSortOrder(testcase.severity);
        };
        $scope.byStatus = function (testcase) {
            return status.getSortOrder(testcase.status);
        };
        $scope.byStatusAndSeverity = [$scope.byStatus, $scope.bySeverity];

        $scope.statusFilter = function (testcase) {
            return $scope.showStatuses[testcase.status];
        };
        $scope.testcases.sort(function (caseA, caseB) {
            return caseA.time.start - caseB.time.start;
        }).forEach(function (testcase, index) {
            testcase.order = index + 1;
        });
        $scope.getVisibleCount = function() {
            var count = 0;
            angular.forEach($scope.testsuite.statistic, function(value, status) {
                if($scope.showStatuses[status.toUpperCase()]) {
                    count += value;
                }
            });
            return count;
        };
        $scope.testcasesLimit = 100;
        $scope.showStatuses = {};
        $scope.sorting = {
            predicate: store.bindProperty($scope, 'sorting.predicate', $scope.byStatusAndSeverity),
            reverse: store.bindProperty($scope, 'sorting.reverse', false)
        };
    }]);
