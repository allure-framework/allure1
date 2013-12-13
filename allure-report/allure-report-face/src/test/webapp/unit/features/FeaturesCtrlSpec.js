/*global describe:true, it:true, beforeEach:true, afterEach:true, expect:true, spyOn:true, module:true, inject:true, angular:true, jasmine:true */
describe('FeaturesCtrl', function () {
    'use strict';
    var $rootScope, $controller,
        state, scope;
    beforeEach(module('allure.features'));
    beforeEach(inject(function (_$controller_, _$rootScope_) {
        $controller = _$controller_;
        $rootScope = _$rootScope_;
    }));

    function Story(uid, title, statistic) {
        this.uid = uid;
        this.title = title;
        this.statistic = {
            failed: statistic[0],
            broken: statistic[1],
            skipped: statistic[2],
            passed: statistic[3],
            total: statistic.reduce(function(r, i) { return r+i; }, 0)
        };
        this.testCases = range(this.statistic.total).map(function(_, index) {
            return {uid: uid+'_'+index}
        });
    }

    function Feature(title, stories) {
        this.title = title;
        this.stories = stories;
        this.statistic = this.stories.reduce(function(stat, story) {
            ['passed', 'skipped', 'broken', 'failed', 'total'].forEach(function(status) {
                stat[status] += story.statistic[status];
            });
            return stat;
        }, {passed: 0, skipped:0, broken:0, failed:0, total:0})
    }

    function range(count) {
        return Array.apply(null, new Array(count));
    }

    function createController(features) {
        var scope = $rootScope.$new();
        scope = scope.$new();
        $controller('FeaturesCtrl', {
            $scope: scope,
            $state: state = jasmine.createSpyObj('state', ['go']),
            status: {
                all: ['FAILED', 'BROKEN', 'SKIPPED', 'PASSED']
            },
            features: {features: features}
        });
        return scope;
    }

    beforeEach(function() {
        scope = createController([
            new Feature('simple feature', [new Story(1, 'simple story', [0,1,0,3])]),
            new Feature('complex feature', [
                new Story(2, 'success story', [0,0,0,5]),
                new Story(3, 'bad story',     [3,1,1,0])
            ])
        ]);
    });

    it('should convert statistics to percents in features', function() {
        expect(scope.features[1].percents.map(function(percent) {
            return percent.value;
        })).toEqual([30, 10, 10, 50]);
    });

    it('should convert statistics to percents in stories', function() {
        expect(scope.features[0].stories[0].percents.map(function(percent) {
            return percent.value;
        })).toEqual([0, 25, 0, 75]);
    });

    it('should create data for pieChart', function() {
        expect(scope.chartData).toEqual([{value : 1, part : 1/3, name : 'passed'}, {value : 2, part : 2/3, name : 'failed'}]);
    });

    it('should create list with all unstable stories', function() {
        expect(scope.allStories.map(function(story) {
            return story.title;
        })).toEqual(['simple story', 'success story', 'bad story']);
    });

    it('should deselect story when corresponding feature has closed', function() {
        scope.story = scope.allStories[0];
        scope.expandFeature(scope.features[0], false);
        expect(state.go).toHaveBeenCalled();
    });

    it('should not deselect story when closed another feature', function() {
        scope.story = scope.allStories[0];
        scope.expandFeature(scope.features[1], false);
        expect(state.go).not.toHaveBeenCalled();
    });

    it('should select and deselect story', function() {
        var story = scope.allStories[1];
        scope.setStory(story);
        expect(state.go).toHaveBeenCalledWith('features.story', {storyUid: story.uid});

        scope.story = story;
        scope.setStory(story);
        expect(state.go).toHaveBeenCalledWith('features');
    });

    it('should call testcase route when it has been selected', function() {
        scope.testcase.uid = '1_2';
        scope.$apply();
        expect(state.go).toHaveBeenCalledWith('features.story.testcase', {testcaseUid: '1_2'});
    });

    describe('state change', function() {
        it('should deselect all on home', function() {
            scope.$broadcast('$stateChangeSuccess', null, {});
            expect(scope.story).toBeUndefined();
            expect(scope.testcase.uid).toBeUndefined();
        });

        it('should expand feature and select story on story', function() {
            scope.$broadcast('$stateChangeSuccess', null, {storyUid: 2});
            expect(scope.features[1].expanded).toBeTruthy();
            expect(scope.features.filter(function(feature) {
                return feature.expanded
            }).length).toBe(1);
        });

        it('should save testcaseUid when it is present', function() {
            scope.$broadcast('$stateChangeSuccess', null, {testcaseUid: '2_2'});
            expect(scope.testcase.uid).toBe('2_2');
        });
    });
});