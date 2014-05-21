/*global angular, $, hljs */
angular.module('allure.directives', [])
    .directive('highlight', function() {
        'use strict';
        return function($scope, elm, attrs) {
            $scope.$watch(attrs.highlight, function(code) {
                if(!code) {
                    return;
                }
                var result;
                if(attrs.language) {
                    result = hljs.highlight(code, attrs.language).value;
                }
                else {
                    result = hljs.highlightAuto(code).value;
                }
                elm.html(result);
            });
        };
    })
    .directive('copyButton', function() {
        'use strict';
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
                    elm.html(tmpl.replace('{{text}}', text));
                });
            }
        };
    })
    .directive('textCut', function() {
        'use strict';
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
    })
    .directive('onKeynav', function($parse) {
        'use strict';
        return function(scope, element, attr) {
            var fn = $parse(attr.onKeynav),
                UP = 38,
                DOWN = 40;
            element.attr('tabIndex', '0');
            element.on('keydown', function(event) {
                var keyCode = event.keyCode;
                if(keyCode === UP || keyCode === DOWN) {
                    scope.$apply(function() {
                        fn(scope, {$direction: keyCode === UP ? -1 : 1});
                    });
                    event.preventDefault();
                }
            });
        };
    })
    .directive('inheritWidth', function($window, $document) {
        "use strict";
        return function(scope, elm) {
            function updateWidth() {
                elm.css('width', elm.parent().width());
            }
            $document.on('webkitTransitionEnd transitionend', updateWidth);
            angular.element($window).on('resize', updateWidth);
            scope.$watch(function() {
                return elm.parent().width();
            }, updateWidth);
        };
    })
    .directive('onError', function() {
        "use strict";
        return function(scope, elm, attrs) {
            elm.on('error', function(event) {
                scope.$apply(function() {
                    scope.$eval(attrs.onError, {$event: event});
                });
            });
        };
    });
