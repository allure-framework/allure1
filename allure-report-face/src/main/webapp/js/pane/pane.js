angular.module('allure.pane', []).directive('paneSet', function() {
    return {
        controller: 'PanesController'
    }
}).directive('pane', function() {
    function Pane(elem) {
        this.elem = elem;
    }
    Pane.prototype.setPosition = function(left, width) {
        this.elem.css({left: left+'%', width: width+'%'})
    };
    Pane.prototype.isExpanded = function() {
        return false;
    };
    return {
        require: '^paneSet',
        link: function(scope, elem, attrs, PanesCtrl) {
            var pane = new Pane(elem);
            pane.isExpanded = function() {
                return scope.$eval(attrs.paneExpanded);
            };
            PanesCtrl.addPane(pane);
            scope.$watch(attrs.paneExpanded, function(expanded, oldExpanded) {
                if(expanded !== oldExpanded) {
                    PanesCtrl.updatePositions();
                }
            });
            scope.$on('$destroy', function() {
                PanesCtrl.removePane(pane);
            })
        }
    };
}).controller('PanesController', function($scope, screenTest) {
    var panes = [];
    this.addPane = function(pane) {
        panes.push(pane);
        this.updatePositions();
    };
    this.removePane = function(pane) {
        panes.splice(panes.indexOf(pane), 1);
        this.updatePositions();
    };
    this.updatePositions = function() {
        panes.forEach(setPanePosition);
    };

    function paneNeedOverlay(index) {
        return index < panes.length-2 || (index < panes.length-1 && panes[panes.length-1].isExpanded())
    }

    function paneShouldBeAtRight(index, expanded) {
        return index !== 0 && index === panes.length-1 && !expanded;
    }

    function setPanePosition(pane) {
        var index = panes.indexOf(pane),
            expanded = pane.isExpanded(),
            offset = 5*index,
            width = (expanded ? 100 : 50) - offset;
        pane.elem[index === panes.length-1 ? 'addClass' : 'removeClass']('pane_col_last');
        pane.elem[index === 0 ? 'addClass' : 'removeClass']('pane_col_first');
        pane.elem[paneNeedOverlay(index) ? 'addClass' : 'removeClass']('pane_col-overlay');
        if (paneShouldBeAtRight(index, expanded)) {
            if(screenTest.isNarrow()) {
                offset+=25;
                width = 100-offset;
                pane.setPosition(offset, width)
            } else {
                pane.setPosition(50, 50)
            }

        } else {
            pane.setPosition(offset, width)
        }
    }
    $scope.$on('screenTestChange', this.updatePositions);
}).factory('screenTest', function($document, $rootScope, $window) {
    function testVisibility(cls) {
        var el = angular.element('<div />').addClass(cls).appendTo('body'),
            visible = el.css('display') !== 'none';
        el.remove();
        return visible;
    }
    angular.element($window).on('resize', function() {
        angular.forEach(breakpoints, function(value, point) {
            breakpoints[point] = testVisibility('visible-'+point);
        });
        $rootScope.$broadcast('screenTestChange', breakpoints);
    });

    var body = $document.find('body'),
        breakpoints = {
            xs: testVisibility('visible-xs'),
            sm: testVisibility('visible-sm'),
            md: testVisibility('visible-md'),
            lg: testVisibility('visible-lg')
        };

    return {
        size: breakpoints,
        isNarrow: function() {
            return breakpoints.xs || breakpoints.sm;
        }
    }
});
