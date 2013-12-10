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

    function repeat(str, times) {
        return new Array(times+1).join(str);
    }

    var paneSimple = '<div pane></div>',
        paneExpandable = '<div pane pane-expanded="paneExpanded"></div>',
        paneAdditonal = '<div ng-if="active" pane></div>';

    function createPane(panes, scope) {
        createElement('<div pane-set>'+panes+'</div>', scope);
    }

    function assertPanePosition(pane, left, width) {
        expect(pane[0].style.left).toBe(left+'%');
        expect(pane[0].style.width).toBe(width+'%');
    }

    it('should put single pane to left', function() {
        createPane(paneSimple);
        assertPanePosition(elem.find('[pane]'), 0, 50)
    });

    it('should pull last pane to right', function() {
        createPane(repeat(paneSimple, 3));
        assertPanePosition(elem.find('[pane]').last(), 50, 50)
    });

    it('should layout panes', function() {
        createPane(repeat(paneSimple, 4));
        var panePos = [[0, 50], [5, 45], [10, 40], [50, 50]];
        elem.find('[pane]').toArray().map(function(node, index) {
            var pos = panePos[index];
            assertPanePosition(angular.element(node), pos[0], pos[1])
        });

    });

    it('should add classes to first and last pane', function() {
        createPane(repeat(paneSimple, 4));
        expect(elem.find('[pane]').first()).toHaveClass('pane_col_first');
        expect(elem.find('[pane]').last()).toHaveClass('pane_col_last');
    });

    it('should update layout when pane set changes', function() {
        createPane(repeat(paneSimple, 3)+paneAdditonal);
        var pane = elem.find('[pane]').last();
        assertPanePosition(pane, 50, 50);
        scope.$apply('active=true');
        assertPanePosition(pane, 10, 40);
        assertPanePosition(elem.find('[pane]').last(), 50, 50);
    });

    it('should add overlay class except last and last but one panes', function() {
        createPane(repeat(paneSimple, 2)+paneAdditonal);
        var pane = elem.find('[pane]').first();
        expect(pane).not.toHaveClass('pane_col-overlay');
        scope.$apply('active=true');
        expect(pane).toHaveClass('pane_col-overlay');
    });

    describe('expandable', function() {
        beforeEach(function() {
            createPane('' +
                repeat(paneSimple, 3)+paneExpandable,
                {paneExpanded: true}
            );
        });
        it('should expand pane', function() {
            var pane = elem.find('[pane-expanded]');
            assertPanePosition(pane, 15, 85);
        });

        it('should collapse pane', function() {
            var pane = elem.find('[pane-expanded]');
            scope.$apply('paneExpanded=false');
            assertPanePosition(pane, 50, 50);
        });

        it('should add overlay to last but one pane', function() {
            var pane = elem.find('[pane]').eq(2);
            expect(pane).toHaveClass('pane_col-overlay');
            scope.$apply('paneExpanded=false');
            expect(pane).not.toHaveClass('pane_col-overlay');
        });
    });

    describe('narrow screens', function() {
        beforeEach(inject(function(screenTest) {
            screenTest.isNarrow = function() {return true;}
        }));

        it('should overlap last pane on previous', function() {
            createPane(repeat(paneSimple, 2));
            assertPanePosition(elem.find('[pane]').last(), 30, 70);
        })
    });
});
