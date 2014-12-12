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
    beforeEach(module('allure.filters'));

    describe('textCut', function() {
        function getBeginning() {
            return elem.find('.text-cut-beginning');
        }
        function getToggleButton() {
            return elem.find('[ng-bind="showText"]');
        }
        function getEnding() {
            return elem.find('.text-cut-ending');
        }
        it('should convert undefined to empty string', function() {
            createElement('<div text-cut="text"></div>', {});
            expect(getBeginning().text()).toBe('');
        });

        it('should escape text by default', function() {
            createElement('<div text-cut="text"></div>', {text: '<br/>\n<div/>'});
            expect(getBeginning().html()).toBe('&lt;br/&gt;');
            expect(getEnding().html()).toBe('&lt;div/&gt;');
        });

        it('should not escape text when specified', function() {
            createElement('<div text-cut="text" escape-html="false"></div>', {text: '<br/>\n<div></div>'});
            expect(getBeginning().html()).toBe('<br>');
            expect(getEnding().html()).toBe('<div></div>');
        });

        it('should split text and hide ending', function() {
            createElement('<div text-cut="text"></div>', {text: 'line\n2nd line'});
            expect(getToggleButton()).not.toHaveClass('ng-hide');
            expect(getEnding()).toHaveClass('ng-hide');
        });

        it('should hide cut if text is not splitted', function() {
            createElement('<div text-cut="text"></div>', {text: 'single line'});
            expect(getToggleButton()).toHaveClass('ng-hide');
            expect(getEnding()).toHaveClass('ng-hide');
        });

        it('should toggle all text by button', function() {
            createElement('<div text-cut="text"></div>', {text: 'line\n2nd line'});
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
            createElement('<div on-keynav="handler($direction)"></div>', {
                handler: jasmine.createSpy('handler')
            });
            triggerKeydown(elem, 40 /*Down*/);
            expect(scope.handler).toHaveBeenCalledWith(1);
        });
        it('should call function when up arrow have been pressed', function() {
            createElement('<div on-keynav="handler($direction)"></div>', {
                handler: jasmine.createSpy('handler')
            });
            triggerKeydown(elem, 38 /*Up*/);
            expect(scope.handler).toHaveBeenCalledWith(-1);
        });
        it('should ignore other keycodes', function() {
            createElement('<div on-keynav="handler($direction)"></div>', {
                handler: jasmine.createSpy('handler')
            });
            triggerKeydown(elem, 13 /*Enter*/);
            expect(scope.handler).not.toHaveBeenCalled();
        });
    });

    describe('inherit width', function() {
        beforeEach(function() {
            this.fakeWindow = angular.element('<div>');
            module({ $window: this.fakeWindow });
        });
        beforeEach(function() {
            createElement('<div><div inherit-width style="position: absolute"></div></div>');
            this.wrapper = elem;
            this.elem = this.wrapper.children();
            this.wrapper.appendTo('body');
        });
        afterEach(function() {
            this.wrapper.remove();
        });

        it('should sync width with parent', function() {
            this.wrapper.width(300);
            scope.$apply();
            expect(this.elem.width()).toBe(300);
        });

        it('should change on window resize', function() {
            elem.css('transition', 'width 0.1s');
            elem.css('width', 300);
            this.fakeWindow.trigger('resize');
            expect(this.elem.width()).toBe(300);
        });
    });
});
