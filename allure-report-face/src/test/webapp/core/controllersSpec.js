/*global describe:true, it:true, beforeEach:true, afterEach:true, expect:true, spyOn:true, module:true, inject:true, angular:true, jasmine:true */
describe('controllers', function () {
    'use strict';
    var $controller, $rootScope;

    beforeEach(module('allure.core.controllers'));
    beforeEach(inject(function (_$controller_, _$rootScope_) {
        $controller = _$controller_;
        $rootScope = _$rootScope_;
    }));

    describe('NavbarCtrl', function() {
        var $translate, $storage;
        function createController() {
            var scope = $rootScope.$new();
            scope = scope.$new();
            $controller('NavbarCtrl', {
                $scope: scope,
                $translate: $translate = jasmine.createSpyObj('$translate', ['use']),
                $storage: function() {
                    return $storage;
                },
                $window: {
                    navigator: {
                        language: 'en-US'
                    }
                }
            });
            return scope;
        }

        beforeEach(function() {
            $storage = jasmine.createSpyObj('$storage', ['getItem', 'setItem']);
        });

        it('should load report info', inject(function($httpBackend) {
            $httpBackend.expectGET('data/report.json').respond({size: 123});
            var scope = createController();
            $httpBackend.flush();
            expect(scope.report).toEqual({size: 123});
        }));

        it("should get locale from storage", function() {
            $storage.getItem.andReturn('ru');
            var scope = createController();
            expect(scope.selectedLang).toBe('ru');
        });

        it("should set english by default", function() {
            var scope = createController();
            expect(scope.selectedLang).toBe('en');
        });
    });
});
