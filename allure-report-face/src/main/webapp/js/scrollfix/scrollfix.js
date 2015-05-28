/*global angular*/
angular.module('allure.core.scrollfix', []).directive('uiScrollfix', ['$window', function ($window) {
    "use strict";
    return {
        require: '^?uiScrollfixTarget',
        link: function (scope, elm, attrs, uiScrollfixTarget) {
            var $target = uiScrollfixTarget && uiScrollfixTarget.$element || angular.element($window),
                top;

            function getTop() {
                var top = elm[0].offsetTop;
                if (attrs.uiScrollfix && typeof attrs.uiScrollfix === 'string') {
                    var direction = attrs.uiScrollfix.charAt(0) === '-' ? -1 : 1;
                    top += direction*parseFloat(attrs.uiScrollfix.substr(1));
                }
                return top;
            }

            function onScroll() {
                var offset = $target.scrollTop(),
                    scrollfix = elm.hasClass('ui-scrollfix');
                if (!scrollfix) {
                    top = getTop();
                }
                if (!elm.hasClass('ui-scrollfix') && offset > top) {
                    elm.addClass('ui-scrollfix');
                } else if (elm.hasClass('ui-scrollfix') && offset < top) {
                    elm.removeClass('ui-scrollfix');
                }
            }

            $target.on('scroll', onScroll);
            scope.$on('$destroy', function () {
                $target.off('scroll', onScroll);
            });
        }
    };
}]).directive('uiScrollfixTarget', [function () {
    "use strict";
    return {
        controller: [
            '$element',
            function ($element) {
                this.$element = $element;
            }
        ]
    };
}]);
