/*global angular*/
(function() {
    var module = angular.module('allure.behaviors', []);
    module.config(function($stateProvider, allureTabsProvider, testcaseProvider) {
        $stateProvider
            .state('behaviors', {
                url: '/behaviors',
                templateUrl: "plugins/behaviors/tab.tpl.html",
                controller: 'BehaviorsCtrl',
                resolve: {
                    data: function($http) {
                        return $http.get('data/behaviors.json').then(function(response) {
                            return response.data;
                        });
                    }
                }
            })
            .state('behaviors.story', {
                url: '/:storyUid'
            })
            .state('behaviors.story.expanded', {
                url: '/expanded'
            });
        allureTabsProvider.tabs.push({name: 'behaviors', title: 'features.TITLE', icon: 'fa fa-list'});
        allureTabsProvider.addTranslation('behaviors');
        testcaseProvider.attachStates('behaviors.story');
    });
    module.controller('BehaviorsCtrl', function($scope, $state, data, percents, WatchingStore, Collection) {
        "use strict";
        function calculatePercents(item) {
            item.percents = percents(item.statistic);
        }
        function findStory(storyUid) {
            var story;
            $scope.features.some(function(feature) {
                story = feature.stories.filter(function(story) {
                    return story.uid === storyUid;
                })[0];
                return story;
            });
            //noinspection JSUnusedAssignment
            return story;
        }
        function setStory(storyUid) {
            var story = findStory(storyUid);
            $scope.story = story;
            $scope.features.forEach(function(feature) {
                if(feature.stories.indexOf(story) !== -1) {
                    feature.expanded = true;
                }
            });
        }
        $scope.isState = function(statename) {
            return $state.is(statename);
        };
        $scope.setStory = function(story) {
            $state.go('behaviors.story', {storyUid: story.uid});
        };
        $scope.isActive = function(story) {
            return $scope.story === story;
        };
        $scope.expandFeature = function(feature, expanded) {
            if(!expanded && feature.stories.indexOf($scope.story) !== -1) {
                $state.go('behaviors');
            }
            feature.expanded = expanded;
        };
        var store = new WatchingStore('featuresSettings');
        $scope.features = data.features;
        $scope.features.forEach(calculatePercents);
        $scope.features.forEach(function(feature) {
            feature.stories.forEach(calculatePercents);
        });

        $scope.sorting = {
            predicate: store.bindProperty($scope, 'sorting.predicate', 'statistic.failed'),
            reverse: store.bindProperty($scope, 'sorting.reverse', false)
        };
        $scope.$watch('features', function(testsuites) {
            $scope.list = new Collection(testsuites);
        });
        $scope.$watch('sorting', function() {
            $scope.list.sort($scope.sorting);
        }, true);
        $scope.testcase = {};
        $scope.$watch('testcase.uid', function(testcaseUid, oldTestcaseUid) {
            if(testcaseUid && oldTestcaseUid !== testcaseUid) {
                $state.go('behaviors.story.testcase', {testcaseUid: testcaseUid});
            }
        });
        $scope.$on('$stateChangeSuccess', function(event, state, params) {
            delete $scope.story;
            delete $scope.testcase.uid;
            if(params.storyUid) {
                setStory(params.storyUid);
            }
            if(params.testcaseUid) {
                $scope.testcase.uid = params.testcaseUid;
            }
        });
    });
})();
