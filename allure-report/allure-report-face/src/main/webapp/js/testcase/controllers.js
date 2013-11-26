/*global angular:true */
angular.module('allure.testcase.controllers', [])
    .controller('StepCtrl', function($scope) {
        "use strict";
        $scope.expanded = false;
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
        //noinspection FallthroughInSwitchStatementJS
        switch ($scope.attachment.type) {
            case 'JPG':
            case 'PNG':
                $scope.type = "image";
            break;
            case 'TXT':
                $scope.type = "text";
                fileGetContents($scope.getSourceUrl($scope.attachment));
            break;
            case 'XML':
            case 'JSON':
                $scope.type = "code";
                fileGetContents($scope.getSourceUrl($scope.attachment));
            break;
        }
    });