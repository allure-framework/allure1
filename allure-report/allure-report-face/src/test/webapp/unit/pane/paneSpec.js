/*global describe:true, it:true, beforeEach:true, afterEach:true, expect:true, spyOn:true, module:true, inject:true, angular:true, jasmine:true */
describe('Pane set', function () {
    'use strict';
    var scope, elem;

    beforeEach(module('allure.pane', function($provide) {
        $provide.value('screenTest', {
            isNarrow: function() { return false;}
        });
    }));


    function createElement(html, scopeValues) {
        inject(function ($compile, $rootScope) {
            scope = $rootScope.$new();
            angular.extend(scope, scopeValues);
            elem = angular.element(html);
            $compile(elem)(scope);
            scope.$digest();
        });
    }

    function assertPanePosition(pane, left, width) {
        expect(pane[0].style.left).toBe(left+'%');
        expect(pane[0].style.width).toBe(width+'%');
    }

    it('should put single pane to left', function() {
        createElement('<div pane-set><div pane></div></div>');
        assertPanePosition(elem.find('[pane]'), 0, 50)
    });

    it('should pull last pane to right', function() {
        createElement('<div pane-set><div pane></div><div pane></div><div pane></div></div>');
        assertPanePosition(elem.find('[pane]').last(), 50, 50)
    });

    it('should layout panes', function() {
        createElement('<div pane-set><div pane></div><div pane></div><div pane></div><div pane></div></div>');
        var panePos = [[0, 50], [5, 45], [10, 40], [50, 50]];
        elem.find('[pane]').toArray().map(function(node, index) {
            var pos = panePos[index];
            assertPanePosition(angular.element(node), pos[0], pos[1])
        });

    });

    it('should add classes to first and last pane', function() {
        createElement('<div pane-set><div pane></div><div pane></div><div pane></div><div pane></div></div>');
        expect(elem.find('[pane]').first()).toHaveClass('pane_col_first');
        expect(elem.find('[pane]').last()).toHaveClass('pane_col_last');
    });

    it('should update layout when pane set changes', function() {
        createElement('<div pane-set><div pane></div><div pane></div><div pane></div><div ng-if="active" pane></div></div>');
        var pane = elem.find('[pane]').last();
        assertPanePosition(pane, 50, 50);
        scope.$apply('active=true');
        assertPanePosition(pane, 10, 40);
        assertPanePosition(elem.find('[pane]').last(), 50, 50);
    });

    describe('expandable', function() {
        beforeEach(function() {
            createElement('<div pane-set><div pane></div><div pane></div><div pane pane-expanded="paneExpanded"></div><div ng-if="active" pane></div></div>');
        });
        it('should expand pane', function() {
            var pane = elem.find('[pane-expanded]');
            assertPanePosition(pane, 50, 50);
            scope.$apply('paneExpanded=true');
            assertPanePosition(pane, 10, 90);
        });

        it('should collapse pane', function() {
            var pane = elem.find('[pane-expanded]');
            scope.$apply('paneExpanded=true');
            assertPanePosition(pane, 10, 90);
            scope.$apply('paneExpanded=false');
            assertPanePosition(pane, 50, 50);
        });
    });

    describe('narrow screens', function() {
        beforeEach(inject(function(screenTest) {
            screenTest.isNarrow = function() {return true;}
        }));

        it('should overlap last pane on previous', function() {
            createElement('<div pane-set><div pane></div><div pane></div></div>');
            assertPanePosition(elem.find('[pane]').last(), 30, 70);
        })
    });
});
