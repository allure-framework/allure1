/*global angular:true, $:true */
(function() {
'use strict';
function getObjectValues(object) {
    return Object.keys(object).map(function(key) {
        return object[key];
    });
}
angular.module('allure.services', [])
    .value('severity', {
        all: ['BLOCKER', 'CRITICAL', 'NORMAL', 'MINOR', 'TRIVIAL'],
        getSortOrder: function (severity) {
            return this.all.indexOf(severity);
        }
    })

    .value('status', {
        all: ['FAILED', 'BROKEN', 'SKIPPED', 'PASSED'],
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
    });
})();