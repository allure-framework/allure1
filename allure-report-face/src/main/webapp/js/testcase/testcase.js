/*global angular:true */
angular.module('allure.core.testcase.controllers', ['d3'])
    .factory('attachmentType', function() {
        "use strict";
        return function(type) { //NOSONAR
            switch (type) {
                case 'image/bmp':
                case 'image/gif':
                case 'image/tiff':
                case 'image/jpeg':
                case 'image/jpg':
                case 'image/png':
                case 'image/*':
                    return "image";
                case 'text/xml':
                case 'application/xml':
                case 'application/json':
                case 'text/json':
                case 'text/yaml':
                case 'application/yaml':
                case 'application/x-yaml':
                case 'text/x-yaml':
                    return "code";
                case 'text/plain':
                case 'text/*':
                    return "text";
                case 'text/html':
                    return "html";
                case 'text/csv':
                    return "csv";
                case 'image/svg+xml':
                    return "svg";
                default:
            }
        };
    })
    .controller('TestcaseCtrl', function($scope, $state, testcase, attachmentType, treeUtils, Collection) {
        "use strict";
        function isFailed(step) {
            return ['FAILED', 'BROKEN', 'CANCELED', 'PENDING'].indexOf(step.status) !== -1;
        }

        function getAllAttachments() {
            var attachments = [];
            treeUtils.walkAround($scope.testcase, 'steps', function(item) {
                if(item !== $scope.testcase) {
                    attachments = attachments.concat(item.attachments);
                }
            });
            return attachments.concat($scope.testcase.attachments);
        }

        function setAttachment(uid) {
            $scope.attachment = getAllAttachments().filter(function(attachment) {
                return attachment.uid === uid;
            })[0];
        }

        $scope.testcase = testcase;
        var baseState = $state.current.data.baseState,
            allAttachments = new Collection(getAllAttachments());
        $scope.failure = testcase.failure;

        function findFailedStep(step) {
            var hasFailed = step.steps.some(findFailedStep);
            if(isFailed(step) && !hasFailed) {
                step.failure = $scope.failure;
                return true;
            }
            return hasFailed;
        }

        findFailedStep($scope.testcase);

        $scope.testcaseArguments = testcase.parameters ? testcase.parameters.filter(function (el) {
            return el.kind === "ARGUMENT";
        }) : [];

        $scope.isState = function(state) {
            return $state.is(baseState + '.' + state);
        };
        $scope.closeTestcase = function() {
            $state.go(baseState);
        };
        $scope.go = function(state) {
            $state.go(baseState + '.' + state);
        };
        $scope.setAttachment = function(attachmentUid) {
            $state.go(baseState + '.testcase.attachment', {attachmentUid: attachmentUid});
        };
        $scope.select = function(direction) {
            var index = allAttachments.indexOf($scope.attachment);
            $scope.setAttachment((direction < 0 ? allAttachments.getPrevious(index) : allAttachments.getNext(index)).uid);
        };

        $scope.getIconClass = function(type) { //NOSONAR
            switch (attachmentType(type)) {
                case 'text':
                    return 'fa fa-file-text-o';
                case 'image':
                case 'svg':
                    return 'fa fa-file-image-o';
                case 'code':
                    return 'fa fa-file-code-o';
                case 'csv':
                    return 'fa fa-table';
                default:
                    return 'fa fa-file-o';
            }
        };

        $scope.$on('$stateChangeSuccess', function(event, state, params) {
            delete $scope.attachment;
            if(params.attachmentUid) {
                setAttachment(params.attachmentUid);
            }
        });
    })
    .controller('StepCtrl', function($scope) {
        "use strict";
        function isFailed(step) {
            return ['FAILED', 'BROKEN', 'CANCELED', 'PENDING'].indexOf(step.status) !== -1;
        }

        $scope.getStepClass = function(step) {
            if(isFailed(step)) {
                return 'text-status-' + step.status.toLowerCase();
            }
            return '';
        };
        $scope.isFailed = isFailed;
        $scope.toggleExpand = function() {
            $scope.expanded = !$scope.expanded;
        };
        $scope.expanded = isFailed($scope.step);
        function getAttachments(step) {
            var attachments = step.attachments;
            step.steps.forEach(function(step) {
                attachments = attachments.concat(getAttachments(step));
            });
            return attachments;
        }

        var nestedAttachments = getAttachments($scope.step);
        $scope.$watch('attachment', function(attachment) {
            if(!$scope.expanded) {
                $scope.expanded = nestedAttachments.indexOf(attachment) > -1;
            }
        });


        $scope.hasContent = $scope.step.summary.steps > 0 || $scope.step.attachments.length > 0 || $scope.step.failure;
    })

    .controller('AttachmentPreviewCtrl', function($scope, $http, $state, attachmentType, d3) {
        "use strict";
        function fileGetContents(url) {
            //get raw file content without parsing
            $http.get(url, {transformResponse: []}).then(function(response) {
                $scope.attachText = response.data;
                if($scope.type === 'csv') {
                    $scope.rows = d3.csv.parseRows(response.data);
                }
            }, $scope.onError);
        }

        $scope.onError = function() {
            $scope.notFound = true;
        };
        $scope.getSourceUrl = function(attachment) {
            return 'data/' + attachment.source;
        };
        $scope.isExpanded = function() {
            return $state.is($state.current.data.baseState + '.testcase.attachment.expanded');
        };
        $scope.toggleExpanded = function() {
            $state.go($state.current.data.baseState + '.testcase.attachment' +
                ($scope.isExpanded() ? '' : '.expanded')
            );
        };
        $scope.$watch('attachment', function(attachment) {
            $scope.notFound = false;
            $scope.type = attachmentType(attachment.type);
            $scope.language = $scope.type === 'code' ? attachment.type.split('/').pop() : undefined;
            if($scope.type === 'text' || $scope.type === 'code' || $scope.type === 'csv') {
                fileGetContents($scope.getSourceUrl(attachment));
            }
        });
    });
angular.module('allure.core.testcase', ['allure.core.testcase.statusSwitcher', 'allure.core.testcase.testcasesList', 'allure.core.testcase.controllers', 'allure.core.testcase.provider']);
