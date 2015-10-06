/*global describe:true, it:true, beforeEach:true, afterEach:true, expect:true, spyOn:true, module:true, inject:true, angular:true, jasmine:true */
describe('Testcase provider', function() {
    'use strict';
    var stateProvider;

    beforeEach(module(function($provide) {
        $provide.provider('$state', function() {
            return stateProvider = {
                state: jasmine.createSpy('stateProviderState').and.callFake(function() {
                    return this;
                }),
                $get: angular.noop
            };
        });
    }));
    beforeEach(module('allure.core.testcase.provider'));


    it('should register states to selected base', function() {
        module(function(testcaseProvider) {
            testcaseProvider.attachStates('base');
        });
        inject();
        expect(stateProvider.state).toHaveBeenCalled();
    });

});
