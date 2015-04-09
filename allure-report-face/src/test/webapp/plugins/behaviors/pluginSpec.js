/*global describe:true, it:true, beforeEach:true, afterEach:true, expect:true, spyOn:true, module:true, inject:true, angular:true, jasmine:true */
describe('BehaviorsCtrl', function () {
    'use strict';
    var $rootScope, $controller,
        state, scope, watchingStoreSpy;
    jasmine.qatools.fakePluginApi();
    beforeEach(module('ui.router'));
    beforeEach(module('allure.core.services'));
    beforeEach(module('allure.behaviors'));
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
            canceled: statistic[2],
            passed: statistic[3],
            pending: statistic[4],
            total: statistic.reduce(function(r, i) { return r+i; }, 0)
        };
        this.testCases = range(this.statistic.total).map(function(_, index) {
            return {uid: uid+'_'+index};
        });
    }

    function Feature(title, stories) {
        this.title = title;
        this.stories = stories;
        this.statistic = this.stories.reduce(function(stat, story) {
            ['passed', 'canceled', 'broken', 'failed', 'pending', 'total'].forEach(function(status) {
                stat[status] += story.statistic[status];
            });
            return stat;
        }, {passed: 0, canceled:0, broken:0, failed:0, pending: 0, total:0});
    }

    function range(count) {
        return Array.apply(null, new Array(count));
    }

    function createController(features) {
        var scope = $rootScope.$new();
        scope = scope.$new();
        $controller('BehaviorsCtrl', {
            $scope: scope,
            $state: state = jasmine.createSpyObj('state', ['go']),
            WatchingStore: function() {
                watchingStoreSpy = jasmine.createSpyObj('WatchingStore', ['bindProperty']);
                return watchingStoreSpy;
            },
            data: {features: features}
        });
        scope.$apply();
        return scope;
    }

    beforeEach(function() {
        scope = createController([
            new Feature('simple feature', [new Story(1, 'simple story', [0,1,0,3,0])]),
            new Feature('complex feature', [
                new Story(2, 'success story', [0,0,0,6,0]),
                new Story(3, 'bad story',     [192,1,1,0,0])
            ])
        ]);
    });

    it('should deselect story when corresponding feature has closed', function() {
        scope.story = scope.features[0].stories[0];
        scope.expandFeature(scope.features[0], false);
        expect(state.go).toHaveBeenCalled();
    });

    it('should not deselect story when closed another feature', function() {
        scope.story = scope.features[0].stories[0];
        scope.expandFeature(scope.features[1], false);
        expect(state.go).not.toHaveBeenCalled();
    });

    it('should select', function() {
        var story = scope.features[1].stories[1];
        scope.setStory(story);
        expect(state.go).toHaveBeenCalledWith('behaviors.story', {storyUid: story.uid});
    });

    it('should call testcase route when it has been selected', function() {
        scope.testcase.uid = '1_2';
        scope.$apply();
        expect(state.go).toHaveBeenCalledWith('behaviors.story.testcase', {testcaseUid: '1_2'});
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
                return feature.expanded;
            }).length).toBe(1);
        });

        it('should save testcaseUid when it is present', function() {
            scope.$broadcast('$stateChangeSuccess', null, {testcaseUid: '2_2'});
            expect(scope.testcase.uid).toBe('2_2');
        });
    });
});
