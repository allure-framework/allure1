/*global describe:true, it:true, beforeEach:true, afterEach:true, expect:true, spyOn:true, module:true, inject:true, angular:true, jasmine:true */
describe('service', function () {
    'use strict';
    beforeEach(module('allure.core.services'));

    describe('status', function () {
        it('should return correct order of statuses', inject(function (status) {
            expect(status.getSortOrder('FAILED')).toEqual(0);
            expect(status.getSortOrder('BROKEN')).toEqual(1);
            expect(status.getSortOrder('CANCELED')).toEqual(2);
            expect(status.getSortOrder('PENDING')).toEqual(3);
            expect(status.getSortOrder('PASSED')).toEqual(4);
        }));
    });

    describe('WatchingStore', function() {
        var store, scope, watchingStore;
        beforeEach(module(function($provide) {
            store = {predicate: 'title'};
            $provide.value('$storage', function() {
                return {
                    getItem: function (key) {
                        return angular.isDefined(store[key]) ? store[key] : null;
                    },
                    setItem: function (key, value) {
                        store[key] = value;
                    },
                    removeItem: function(key) {
                        delete store[key];
                    }
                };
            });
        }));
        beforeEach(inject(function(_WatchingStore_, $rootScope) {
            watchingStore = new _WatchingStore_();
            scope = $rootScope.$new();
        }));

        it('should return default value when store is empty', function() {
            scope.showPassed = watchingStore.bindProperty(scope, 'showPassed', true);
            expect(scope.showPassed).toBe(true);
        });

        it('should return value from store when it is present', function() {
            scope.showPassed = watchingStore.bindProperty(scope, 'predicate', 'status');
            expect(scope.showPassed).toBe('title');
        });

        it('should update store when value is changed', function() {
            scope.showPassed = watchingStore.bindProperty(scope, 'predicate', 'status');
            scope.$apply();
            scope.$apply('predicate=\'date\'');
            expect(store.predicate).toBe('date');
        });

        it('should remove value from store when it set to default', function() {
            scope.showPassed = watchingStore.bindProperty(scope, 'predicate', 'status');
            scope.$apply();
            scope.$apply('predicate=\'status\'');
            expect(store.predicate).toBeUndefined();
        });

        it('should not write to store when value is not changed', function() {
            var storeDump = angular.copy(store);
            scope.showPassed = watchingStore.bindProperty(scope, 'showPassed', true);
            scope.$apply();
            expect(storeDump).toEqualData(store);
        });
    });

    describe('treeUtils', function() {
        var treeUtils;
        beforeEach(inject(function(_treeUtils_) {
            treeUtils = _treeUtils_;
        }));

        it('should walk around tree', function() {
            var callbackSpy = jasmine.createSpy('callbackSpy');
            treeUtils.walkAround({
                items: [],
                children: [{}, {}]
            }, 'children', callbackSpy);
            expect(callbackSpy.calls.count()).toBe(3);
        });

        it('should stop iteration when returned false', function() {
            var callbackSpy = jasmine.createSpy('callbackSpy').and.callFake(function() {
                return callbackSpy.calls.count() < 5;
            });
            treeUtils.walkAround({
                items: [],
                children: [{
                    children:[{children: [{children:[]}]}, {}]
                }, {}]
            }, 'children', callbackSpy);
            expect(callbackSpy.calls.count()).toBe(5);
        });

        it('should find items count', function() {
            expect(treeUtils.getItemsCount({items: []}, 'children', 'items')).toBe(0);
            expect(treeUtils.getItemsCount({
                items: [],
                children: {items: []}
            }, 'children', 'items')).toBe(0);
            expect(treeUtils.getItemsCount({
                items: [],
                children: [{items: []}, {items: []}]
            }, 'children', 'items')).toBe(0);
            expect(treeUtils.getItemsCount({
                items: [],
                children: [{items: []}, {items: []}]
            }, 'children', 'items')).toBe(0);
            expect(treeUtils.getItemsCount({
                items: [1, 2],
                children: [{items: []}, {items: []}]
            }, 'children', 'items')).toBe(2);
            expect(treeUtils.getItemsCount({
                items: [1, 2],
                children: [{items: [3]}, {items: []}]
            }, 'children', 'items')).toBe(3);
            expect(treeUtils.getItemsCount({
                items: [1, 2],
                children: [{
                    items: [],
                    children: [{items: [3]}]
                }, {items: []}]
            }, 'children', 'items')).toBe(3);

            expect(treeUtils.getItemsCount({
                items: [1, 2],
                children: [{
                    children: [{
                        children: [],
                        items: [3]
                    }]
                }, {children: []}, {children: []}]
            }, 'children', 'children')).toBe(4);
        });
    });

    describe('Collection', function() {
        var collection, items;
        beforeEach(inject(function(Collection) {
            items = [0,1,2,3,4,5];
            collection = new Collection(items);
        }));

        it('should apply filter', function() {
            collection.filter(function(item) { return item > 3;});
            expect(collection.items).toEqual([4,5]);
        });

        it('should apply sorting', function() {
            collection.sort({
                predicate: function(item) { return -item;}
            });
            expect(collection.items).toEqual([5,4,3,2,1,0]);
        });

        it('should apply limit', function() {
            collection.limitTo(4);
            expect(collection.items).toEqual([0,1,2,3]);
        });

        it('should get index by key value', function() {
            collection.items.push({key: 'value'});
            expect(collection.getIndexBy('key', 'value')).toBe(6);
        });

        it('should find item by key value', function() {
            collection.items.push({key: 'value'});
            expect(collection.findBy('key', 'value')).toEqual(collection.items[6]);
        });

        it('should index by item', function() {
            expect(collection.indexOf(4)).toBe(4);
        });

        it('should navigate within items', function() {
            expect(collection.getPrevious(4)).toBe(items[3]);
            expect(collection.getPrevious(0)).toBe(items[0]);
            expect(collection.getNext(3)).toBe(items[4]);
            expect(collection.getNext(5)).toBe(items[5]);
        });
    });

    describe('percents', function() {
        var percents;
        beforeEach(inject(function(_percents_) {
            percents = _percents_;
        }));

        it('should convert statistics to percents', function() {
            expect(percents({
                total: 4,
                passed: 1,
                failed: 1,
                broken: 1,
                canceled: 1,
                pending: 0
            }).map(function(percent) {
                return percent.ratio;
            })).toEqual([25, 25, 25, 0, 25]);
        });

        it('should limit min percent value in  statistics', function() {
            var result = percents({
                total: 200,
                passed: 192,
                failed: 1,
                broken: 1,
                canceled: 6,
                pending: 0
            });
            expect(result.map(function(r) {return r.ratio;})).toEqual([0.5, 0.5, 3, 0, 96]);
            expect(result.map(function(r) {return r.value;})).toEqual([3, 3, 3, 0, 91]);
        });
    });
});
