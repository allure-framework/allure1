/*global angular:true */
angular.module('allure.testcase.controllers', [])
    .controller('TestcaseCtrl', function($scope, $state, testcase, treeUtils) {
        function setAttachment(source) {
            var attachment;
            treeUtils.walkAround($scope.testcase, 'steps', function(item) {
                attachment = item.attachments.filter(function(attachment) {
                    return attachment.source === source;
                })[0];
                return !attachment;
            });
            //noinspection JSUnusedAssignment
            $scope.attachment = attachment;
        }
        $scope.isState = function(state) {
            return $state.is(baseState+'.'+state);
        };
        $scope.closeTestcase = function() {
            $state.go(baseState);
        };
        $scope.go = function(state) {
            $state.go(baseState+'.'+state);
        };
        $scope.setAttachment = function(attachmentUid) {
            $state.go(baseState+'.testcase.attachment', {attachmentUid: attachmentUid});
        };
        var baseState = $state.current.data.baseState;
        $scope.testcase = testcase;

        $scope.$on('$stateChangeSuccess', function(event, state, params) {
            delete $scope.attachment;
            if(params.attachmentUid) {
                setAttachment(params.attachmentUid);
            }
        });
    })
    .controller('StepCtrl', function($scope, $locale) {
        "use strict";
        function isFailed(step) {
            return ['FAILED', 'BROKEN'].indexOf(step.status) !== -1;
        }
        var stepPlural = {
                one: ' sub-step',
                other: ' sub-steps'
            },
            attachmentPlural = {
                one: ' attachment',
                other: ' attachments'
            };
        $scope.getStepClass = function(step) {
            if(isFailed(step)) {
                return 'text-status-'+step.status.toLowerCase();
            }
            return '';
        };
        $scope.formatSummary = function(step) {
            var result = [];
            if(step.summary.steps + step.summary.attachments === 0) {
                return '';
            }
            if(step.summary.steps) {
                result.push(step.summary.steps + stepPlural[$locale.pluralCat(step.summary.steps)])
            }
            if(step.summary.attachments) {
                result.push(step.summary.attachments + attachmentPlural[$locale.pluralCat(step.summary.attachments)])
            }
            return '('+result.join(', ')+')';
        };
        $scope.expanded = isFailed($scope.step);
        $scope.hasContent = $scope.step.summary.steps > 0 || $scope.step.attachments.length > 0;
    })

    .controller('AttachmentPreviewCtrl', function ($scope, $http, $state) {
        "use strict";
        function fileGetContents(url) {
            //get raw file content without parsing
            $http.get(url, {transformResponse: []}).then(function(resonse) {
                $scope.attachText = resonse.data;
            });
        }
        $scope.getSourceUrl = function(attachment) {
            return 'data/'+attachment.source;
        };
        $scope.isExpanded = function() {
            return $state.is('home.testsuite.testcase.attachment.expanded');
        };
        $scope.toggleExpanded = function() {
            $state.go('home.testsuite.testcase.attachment'+
                ($scope.isExpanded() ? '' : '.expanded')
            );
        };
        $scope.$watch('attachment', function(attachment) {
            //noinspection FallthroughInSwitchStatementJS
            switch (attachment.type) {
                case 'JPG':
                case 'PNG':
                    $scope.type = "image";
                    break;
                case 'TXT':
                    $scope.type = "text";
                    fileGetContents($scope.getSourceUrl(attachment));
                    break;
                case 'XML':
                case 'JSON':
                    $scope.type = "code";
                    fileGetContents($scope.getSourceUrl(attachment));
                    break;
            }
        });
    });
angular.module('allure.testcase', ['allure.testcase.statusSwitcher', 'allure.testcase.testcasesList', 'allure.testcase.controllers', 'allure.testcase.provider']);
