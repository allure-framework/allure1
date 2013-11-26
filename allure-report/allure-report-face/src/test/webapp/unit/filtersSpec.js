/*global describe, it, beforeEach, afterEach, expect, spyOn, angular, inject, module */
describe('filter', function () {
    'use strict';
    beforeEach(module('allure.filters'));

    describe('interpolate', function () {
        beforeEach(module(function ($provide) {
            $provide.value('version', 'TEST_VER');
        }));


        it('should replace VERSION', inject(function (interpolateFilter) {
            expect(interpolateFilter('before %VERSION% after')).toEqual('before TEST_VER after');
        }));
    });

    describe('time', function() {
        it('should format time', inject(function(timeFilter) {
            expect(timeFilter(false)).toBe('0');
            expect(timeFilter(null)).toBe('0');
            expect(timeFilter(0)).toBe('0');
            expect(timeFilter(345)).toBe('345ms');
            expect(timeFilter(1000)).toBe('1s');
            expect(timeFilter(1100)).toBe('1s 100ms');
            expect(timeFilter(60000)).toBe('1m 0s');
            expect(timeFilter(140000)).toBe('2m 20s');
            expect(timeFilter(4201000)).toBe('1h 10m');
            expect(timeFilter(3601000)).toBe('1h 0m');
            expect(timeFilter(25*3600*1000+62000)).toBe('25h 1m');
        }));
    });
});
