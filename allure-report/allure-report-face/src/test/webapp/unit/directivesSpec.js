/*global describe, it, beforeEach, afterEach, expect, spyOn, angular, inject, module */
describe('Allure directive', function () {
    'use strict';
    var scope,
        elem;

    function createElement(html, scopeValues) {
        inject(function ($compile, $rootScope) {
            scope = $rootScope.$new();
            angular.extend(scope, scopeValues);
            elem = angular.element(html);
            $compile(elem)(scope);
            scope.$digest();
        });
    }

    beforeEach(module('allure.directives'));

    describe('textCut', function() {
        function getBeginning() {
            return elem.find('[ng-bind="beginning"]');
        }
        function getToggleButton() {
            return elem.find('[ng-bind="showText"]');
        }
        function getEnding() {
            return elem.find('[ng-bind="ending"]');
        }
        it('should convert undefined to empty string', function() {
            createElement('<div text-cut="text"></div>', {});
            expect(getBeginning().text()).toBe('');
        });
        it('should split text and hide ending', function() {
            createElement('<div text-cut="text"></div>', {text: 'line\n\n2nd line'});
            expect(getToggleButton()).not.toHaveClass('ng-hide');
            expect(getEnding()).toHaveClass('ng-hide');
        });

        it('should hide cut if text is not splitted', function() {
            createElement('<div text-cut="text"></div>', {text: 'single line'});
            expect(getToggleButton()).toHaveClass('ng-hide');
            expect(getEnding()).toHaveClass('ng-hide');
        });

        it('should toggle all text by button', function() {
            createElement('<div text-cut="text"></div>', {text: 'line\n\n2nd line'});
            getToggleButton().click();
            expect(getEnding()).not.toHaveClass('ng-hide');
            getToggleButton().click();
            expect(getEnding()).toHaveClass('ng-hide');
        });
    });

    describe('onKeynav', function() {
        function triggerKeydown(element, keyCode) {
            var e = jQuery.Event("keydown");
            e.keyCode = keyCode;
            element.trigger(e);
        }
        it('should call function when up arrow have been pressed', function() {
            createElement('<div on-keynav="handler($direction)">', {
                handler: jasmine.createSpy('handler')
            });
            triggerKeydown(elem, 40 /*Down*/);
            expect(scope.handler).toHaveBeenCalledWith(1);
        });
        it('should call function when up arrow have been pressed', function() {
            createElement('<div on-keynav="handler($direction)">', {
                handler: jasmine.createSpy('handler')
            });
            triggerKeydown(elem, 38 /*Up*/);
            expect(scope.handler).toHaveBeenCalledWith(-1);
        });
        it('should ignore other keycodes', function() {
            createElement('<div on-keynav="handler($direction)">', {
                handler: jasmine.createSpy('handler')
            });
            triggerKeydown(elem, 13 /*Enter*/);
            expect(scope.handler).not.toHaveBeenCalled();
        });
    });
});