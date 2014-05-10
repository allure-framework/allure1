/*global describe, it, beforeEach, afterEach, expect, spyOn, angular, inject, module */
describe('AllureTable directive', function () {
    'use strict';
    var scope,
        elem;

    function createElement(html, scopeValues) {
        inject(function ($compile, $rootScope) {
            scope = $rootScope.$new();
            angular.extend(scope, scopeValues);
            elem = angular.element(html);
            $compile(elem)(scope);
            scope.$apply();
        });
    }

    function createTable(content, sorting) {
        createElement('<table allure-table sorting="sorting">'+content+'</table>', {sorting: sorting})
    }

    beforeEach(module('allure.table'));
    beforeEach(inject(function($templateCache) {
        $templateCache.put('templates/table/table.html', '<table></table>')
    }));

    it('should break when allureTableCol outside of table', function() {
        expect(function() {createElement('<td allure-table-col="\'alone ranger\'"></td>')}).toThrow();
    });

    it('should create table and transclude content', function() {
        createTable('<tbody>' +
            '<tr><td>1</td><td>red</td></tr>'+
            '<tr><td>2</td><td>yellow</td></tr>'+
        '</tbody>');
        expect(elem.find('tbody tr').length).toBe(2);
    });

    it('should wrap content in tbody automaticaly', function() {
        createTable(
            '<tr><td>1</td><td>red</td></tr>'+
            '<tr><td>2</td><td>yellow</td></tr>'
        );
        expect(elem.find('tr').length).toBe(2);
    });

    it('should break when content is not in table', function() {
        createElement('<div allure-table>'+
            '<tr><td>1</td><td>red</td></tr>'+
            '<tr><td>2</td><td>yellow</td></tr>'+
        '</div>');
        expect(elem.find('tr').length).toBe(0);
    });

    it('should create columns from config', function() {
        createTable('<tbody>' +
            '<tr><td allure-table-col="{heading: \'Number\'}">1</td><td allure-table-col="\'Color\'">red</td></tr>'+
            '<tr><td>2</td><td>yellow</td></tr>'+
            '</tbody>');
        expect(elem.isolateScope().columns).toEqual([{heading: 'Number', reverse: false, flex: 1}, {predicate: 'color', heading: 'Color', reverse: false, flex: 1}]);
    });

    describe('controller', function() {
        var scope, ctrl;
        beforeEach(inject(function($controller, $rootScope) {
            scope = $rootScope.$new();
            ctrl = $controller('AllureTableController', {$scope: scope, $transclude: {}});
        }));

        function setupColumns() {
            ctrl.addColumn('Title');
            ctrl.addColumn({heading: 'Count', predicate: 'count', reverse: true});
        }

        it('should apply defaults to column config', function() {
            ctrl.addColumn({heading: 'Title', predicate: 'title'});
            ctrl.addColumn({heading: 'Count', predicate: 'count', reverse: true});
            expect(scope.columns[0]).toEqual({heading: 'Title', predicate: 'title', reverse: false, flex: 1});
            expect(scope.columns[1]).toEqual({heading: 'Count', predicate: 'count', reverse: true, flex: 1});
        });

        it('should create column config from string', function() {
            ctrl.addColumn('Title');
            expect(scope.columns[0]).toEqual({heading: 'Title', predicate: 'title', reverse: false, flex: 1});
        });

        it('should filter column duplicates', function() {
            ctrl.addColumn('Title');
            ctrl.addColumn('Title');
            expect(scope.columns.length).toBe(1);
        });

        it('should set default reverse when new predicate selected', function() {
            setupColumns();
            scope.setPredicate(scope.columns[0]);
            expect(scope.sorting.reverse).toBe(false);
            scope.setPredicate(scope.columns[1]);
            expect(scope.sorting.reverse).toBe(true);
        });

        it('should revert sort when the current predicate are set again', function() {
            setupColumns();
            scope.setPredicate(scope.columns[0]);
            expect(scope.sorting.reverse).toBe(false);
            scope.setPredicate(scope.columns[0]);
            expect(scope.sorting.reverse).toBe(true);
        });

        it('should cancel sort when predicate is undefined', function() {
            setupColumns();
            scope.setPredicate();
            expect(scope.sorting.reverse).toBeUndefined();
            expect(scope.sorting.predicate).toBeUndefined();
        });

        it('should set column width with default flex equally', function() {
            setupColumns();
            expect(scope.getWidth(scope.columns[0])).toBe('50%');
            expect(scope.getWidth(scope.columns[1])).toBe('50%');
        });

        it('should set the column width in proportion to flex', function() {
            setupColumns();
            ctrl.addColumn({heading: 'Comment', flex: 3});
            expect(scope.getWidth(scope.columns[0])).toBe('20%');
            expect(scope.getWidth(scope.columns[1])).toBe('20%');
            expect(scope.getWidth(scope.columns[2])).toBe('60%');
        })
    });
});