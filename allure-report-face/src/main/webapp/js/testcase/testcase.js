/*global angular:true */
angular.module('allure.testcase.controllers', [])
    .controller('TestcaseCtrl', function($scope, $state, testcase, treeUtils, Collection) {
        "use strict";
        function isFailed(step) {
            return ['FAILED', 'BROKEN', 'CANCELED'].indexOf(step.status) !== -1;
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
        $scope.select = function(direction) {
            var index = allAttachments.indexOf($scope.attachment);
            setAttachment((direction < 0 ? allAttachments.getPrevious(index) : allAttachments.getNext(index)).uid);
        };
        
        $scope.getIconClass = function(attachmentType) {
            switch (attachmentType) {
                case 'text/plain': return 'fa fa-file-text-o';
                
                case 'image/bmp':
                case 'image/png':
                case 'image/gif':
                case 'image/tiff':
                case 'image/jpeg': return 'fa fa-file-image-o';
                
                case 'text/html':
                case 'application/xml':
                case 'text/xml':
                case 'text/json':
                case 'application/json':
                case 'text/yaml':
                case 'application/yaml':
                case 'application/x-yaml': 
                case 'text/x-yaml': return 'fa fa-file-code-o';
                
                case 'application/pdf': return 'fa fa-file-pdf-o';
                
                case 'application/x-compressed':
                case 'application/x-zip-compressed':
                case 'application/zip':
                case 'multipart/x-zip':
                case 'application/x-gzip':
                case 'application/x-bzip':
                case 'application/x-tar':
                case 'application/x-rar-compressed': return 'fa fa-file-archive-o';
                
                case 'application/msword':
                case 'application/vnd.openxmlformats-officedocument.wordprocessingml.document': return 'fa fa-file-word-o';
                    
                case 'application/excel':
                case 'application/vnd.ms-excel':
                case 'application/x-excel':
                case 'application/x-msexcel':
                case 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet': return 'fa fa-file-excel-o';
                
                case 'application/mspowerpoint':
                case 'application/powerpoint':
                case 'application/vnd.ms-powerpoint':
                case 'application/x-mspowerpoint':
                case 'application/vnd.openxmlformats-officedocument.presentationml.presentation': 
                    return 'fa fa-file-powerpoint-o';
                
                default: return 'fa fa-file-o';
            }
        };

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
            return ['FAILED', 'BROKEN', 'SKIPPED'].indexOf(step.status) !== -1;
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
                result.push(step.summary.steps + stepPlural[$locale.pluralCat(step.summary.steps)]);
            }
            if(step.summary.attachments) {
                result.push(step.summary.attachments + attachmentPlural[$locale.pluralCat(step.summary.attachments)]);
            }
            return '('+result.join(', ')+')';
        };
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

    .controller('AttachmentPreviewCtrl', function ($scope, $http, $state) {
        "use strict";
        function fileGetContents(url) {
            //get raw file content without parsing
            $http.get(url, {transformResponse: []}).then(function(response) {
                $scope.attachText = response.data;
            }, $scope.onError);
        }

        $scope.onError = function() {
            $scope.notFound = true;
        };
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
            $scope.notFound = false;
            delete $scope.language;
            //noinspection FallthroughInSwitchStatementJS
            switch (attachment.type) {
                case 'image/jpeg':
                case 'image/jpg':
                case 'image/png':
                case 'image/*':
                    $scope.type = "image";
                    break;
                case 'text/xml':
                case 'application/xml':
                case 'application/json':
                    $scope.language = attachment.type.split('/').pop();
                //fallthrough
                case 'text/plain':
                case 'text/*':
                    $scope.type = "text";
                    fileGetContents($scope.getSourceUrl(attachment));
                    break;
                default:
                    delete $scope.type;
            }
        });
    });
angular.module('allure.testcase', ['allure.testcase.statusSwitcher', 'allure.testcase.testcasesList', 'allure.testcase.controllers', 'allure.testcase.provider']);
