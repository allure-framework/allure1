angular.module('allure.features', [])
.controller('FeaturesCtrl', function($scope, $state, status, features) {
    function calculatePercents(item) {
        item.percents = status.all.map(function(status) {
            status = status.toLowerCase();
            return {
                count: item.statistic[status],
                value: item.statistic[status]/item.statistic.total*100 || 0,
                status: status
            }
        });
    }
    function getChartData() {
        var unstable = $scope.unstableStories.length,
            total = $scope.allStories.length,
            stable = total - unstable;
        return [
            {value: stable, part: stable/total, name: 'passed'},
            {value: unstable, part: unstable/total, name: 'failed'}];
    }
    function findStory(storyUid) {
        var story;
        $scope.features.some(function(feature) {
            return story = feature.stories.filter(function(story) {
                return story.uid === storyUid;
            })[0];
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
        if(story === $scope.story) {
            $state.go('features');
        } else {
            $state.go('features.story', {storyUid: story.uid});
        }
    };
    $scope.expandFeature = function(feature, expanded) {
        if(!expanded && feature.stories.indexOf($scope.story) !== -1) {
            $state.go('features')
        }
        feature.expanded = expanded;
    };
    $scope.features = features.features;
    $scope.allStories = $scope.features.reduce(function(all, feature) {
        return all.concat(feature.stories);
    }, []);
    $scope.features.forEach(calculatePercents);
    $scope.allStories.forEach(calculatePercents);
    $scope.unstableStories = $scope.allStories.filter(function(story) {
        return story.statistic.passed !== story.statistic.total;
    });
    $scope.chartData = getChartData();
    $scope.chartTooltip = '<div><b>{{value}} {{value == 1 ? "story" : "stories"}} ({{data.part * 100 | number:0}}%)</b></div>' +
        '<div>{{data.name}}</div>';
    $scope.summaryText = [
        'Everything is very bad!',
        'There are many errors!',
        'Some test are not passed',
        'All done. Good job!'
    ][Math.floor($scope.chartData[0].part*3)];
    $scope.testcase = {};
    $scope.$watch('testcase.uid', function(testcaseUid) {
        if(testcaseUid) {
            $state.go('features.story.testcase', {testcaseUid: testcaseUid})
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
