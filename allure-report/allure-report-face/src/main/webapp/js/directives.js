/*global angular, $, Rainbow */
(function() {
'use strict';
angular.module('allure.directives', [])
    .directive('rainbowHighlight', function() {
        return function($scope, elm, attrs) {
            $scope.$watch(attrs.rainbowHighlight, function(code) {
                if(!code) {
                    return;
                }
                Rainbow.color(code, attrs.language, function(highlighted) {
                    elm.html(highlighted);
                });
            });
        };
    })
    .directive('copyButton', function() {
        return {
            restrict: 'E',
            replace: true,
            scope: {
                text: '='
            },
            link: function(scope, elm) {
                var tmpl = '<object classid="clsid:d27cdb6e-ae6d-11cf-96b8-444553540000" width="110" height="14" id="clippy">' +
                    '<embed wmode="opaque" src="flash/clippy.swf" width="120" height="14" name="clippy" quality="high" allowScriptAccess="always" ' +
                    'type="application/x-shockwave-flash" pluginspage="http://www.macromedia.com/go/getflashplayer" ' +
                    'FlashVars="text={{text}}" bgcolor="#FFF" />' +
                '</object>';
                scope.$watch('text', function(text) {
                    elm.html(tmpl.replace('{{text}}', text))
                });
            }
        }
    })
    .directive('textCut', function() {
        return {
            restrict: 'A',
            replace: 'true',
            template: '<div>' +
                '<span ng-bind="beginning" ng-class="{clickable: ending}" ng-click="toggleShow(!show)"></span> ' +
                '<span ng-show="show" ng-bind="ending"></span> ' +
                '<span class="text-right">' +
                    '<span ng-click="toggleShow(!show)" ng-show="ending" class="pull-right expand-btn btn-link clickable" ng-bind="showText"></span>' +
                '</div>' +
            '</div>',
            scope: {
                textCut: '='
            },
            link: function($scope) {
                $scope.toggleShow = function(show) {
                    $scope.show = show;
                    $scope.showText = show ? 'collapse...' : 'show full...';
                };
                var divider = '\n\n';
                $scope.toggleShow(false);
                $scope.$watch('textCut', function(text) {
                    var index = (text || '').indexOf(divider);
                    if(index !== -1) {
                        $scope.beginning = text.substr(0, index - 1);
                        $scope.ending = text.substr(index + 1);
                    }
                    else {
                        $scope.beginning = text;
                        $scope.ending = '';
                    }
                });
            }
        };
    });
})();