/*global angular*/
angular.module('allure.features', [])
.controller('FeaturesCtrl', function($scope, $state, features, percents, WatchingStore, Collection) {
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
        $state.go('features.story', {storyUid: story.uid});
    };
    $scope.isActive = function(story) {
        return $scope.story === story;
    };
    $scope.expandFeature = function(feature, expanded) {
        if(!expanded && feature.stories.indexOf($scope.story) !== -1) {
            $state.go('features');
        }
        feature.expanded = expanded;
    };
    var store = new WatchingStore('featuresSettings');
    $scope.features = features.features;
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
            $state.go('features.story.testcase', {testcaseUid: testcaseUid});
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
