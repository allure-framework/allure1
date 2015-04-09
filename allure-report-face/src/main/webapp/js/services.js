/*global angular:true, $:true */
(function() {
'use strict';
function getObjectValues(object) {
    return Object.keys(object).map(function(key) {
        return object[key];
    });
}
angular.module('allure.core.services', [])
    .value('severity', {
        all: ['BLOCKER', 'CRITICAL', 'NORMAL', 'MINOR', 'TRIVIAL'],
        getSortOrder: function (severity) {
            return this.all.indexOf(severity);
        }
    })

    .value('status', {
        all: ['FAILED', 'BROKEN', 'CANCELED', 'PENDING', 'PASSED'],
        getSortOrder: function (status) {
            return this.all.indexOf(status);
        }
    })

    .factory('WatchingStore', function($storage) {
        function defaults(value, defaultVal) {
            return value !== null ? value : defaultVal;
        }
        function WatchingStore(storeName) {
            this.store = $storage(storeName);
        }
        WatchingStore.prototype.bindProperty = function($scope, prop, defaultVal) {
            var store = this.store,
                value = defaults(store.getItem(prop), defaultVal);
            $scope.$watch(prop, function(val) {
                if (val === defaultVal) {
                    store.removeItem(prop);
                } else {
                    store.setItem(prop, val);
                }
            });
            return value;
        };
        return WatchingStore;
    })

    .factory('Collection', function(filterFilter, orderByFilter, limitToFilter) {
        function Collection(items) {
            this._items = items;
            this.items = items;
        }
        Collection.prototype._update = function() {
            this.items = this._items;
            if(this.filterFn) {
                this.items = filterFilter(this.items, this.filterFn);
            }
            if(this.sorting) {
                this.items = orderByFilter(this.items, this.sorting.predicate, this.sorting.reverse);
            }
            if(this.limit) {
                this.items = limitToFilter(this.items, this.limit);
            }
            return this.items;
        };
        Collection.prototype.filter = function(func) {
            this.filterFn = func;
            this._update();
        };
        Collection.prototype.sort = function(sorting) {
            this.sorting = sorting;
            this._update();
        };
        Collection.prototype.limitTo = function(limit) {
            this.limit = limit;
            this._update();
        };
        Collection.prototype.findBy = function(key, value) {
            return this.items[this.getIndexBy(key, value)];
        };
        Collection.prototype.getIndexBy = function(key, value) {
            var index = -1;
            this.items.some(function(item, i) {
                if(item[key] === value) {
                    index = i;
                    return true;
                }
                return false;
            });
            return index;
        };
        Collection.prototype.indexOf = function(item) {
            return this.items.indexOf(item);
        };
        Collection.prototype.getNext = function(index) {
            if(index < this.items.length-1) {
                index++;
            }
            return this.items[index];
        };
        Collection.prototype.getPrevious = function(index) {
            if(index > 0) {
                index--;
            }
            return this.items[index];
        };
        return Collection;
    })

    .factory('treeUtils', function() {
        function walkAround(tree, childProp, callback) {
            var children = tree[childProp];
            if(callback(tree) === false) {
                return false;
            }
            else if(children && children.length > 0) {
                return children.every(function(child) {
                    return walkAround(child, childProp, callback) !== false;
                });
            }
            return true;
        }
        return {
            walkAround: walkAround,
            getItemsCount: function(tree, childProp, itemsProp) {
                var result = 0;
                walkAround(tree, childProp, function(child) {
                    result += child[itemsProp].length;
                });
                return result;
            }
        };
    })
    .factory('percents', function(status) {
        function getTotalPercents(percents) {
            return percents.reduce(function(total, current) {
                return total+current.value;
            }, 0);
        }
        function calculatePercents(statistic) {
            var percents = status.all.map(function(status) {
                status = status.toLowerCase();
                var value = statistic[status]/statistic.total*100 || 0;
                return {
                    count: statistic[status],
                    ratio: value,
                    value: value > 0 ? Math.max(value, 3) : 0,
                    status: status
                };
            });
            if(getTotalPercents(percents) > 100) {
                var highValue = percents.reduce(function(max, current) {
                    return max.value > current.value ? max : current;
                }, {value:0});
                highValue.value = percents.reduce(function(value, current) {
                    return current === highValue ? value : value - current.value;
                }, 100);
            }
            return percents;
        }
        return calculatePercents;
    });
})();
