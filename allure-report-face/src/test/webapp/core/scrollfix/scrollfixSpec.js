/*global describe, beforeEach, module, inject, it, spyOn, expect, $ */
describe('uiScrollfix', function () {
  'use strict';

  var scope, $compile, $window;
  beforeEach(module('allure.core.scrollfix'));
  beforeEach(inject(function (_$rootScope_, _$compile_, _$window_) {
    scope = _$rootScope_.$new();
    $compile = _$compile_;
    $window = _$window_;
  }));

  describe('compiling this directive', function () {
    it('should bind and unbind to window "scroll" event in the absence of a uiScrollfixTarget', function () {
      spyOn($.fn, 'on').and.callThrough();
      $compile('<div ui-scrollfix="100"></div>')(scope);
      expect($.fn.on).toHaveBeenCalled();
      expect($.fn.on.calls.mostRecent().args[0]).toBe('scroll');
      spyOn($.fn, 'off').and.callThrough();
      scope.$destroy();
      expect($.fn.off).toHaveBeenCalled();
    });
    it('should bind and unbind to a parent uiScrollfixTarget element "scroll" event', function() {
      var $elm = $compile('<div ui-scrollfix-target><div ui-scrollfix="100"></div></div>')(scope);
      expect($._data($elm[0], 'events')).toBeDefined();
      expect($._data($elm[0], 'events').scroll.length).toBe(1);
      scope.$destroy();
      expect($._data($elm[0], 'events')).toBeUndefined();
    });
  });
  describe('scrolling the window', function () {
    it('should add the ui-scrollfix class if the offset is greater than specified', function () {
      var element = $compile('<div ui-scrollfix="-100"></div>')(scope);
      angular.element($window).trigger('scroll');
      expect(element.hasClass('ui-scrollfix')).toBe(true);
    });
    it('should remove the ui-scrollfix class if the offset is less than specified (using relative coord)', function () {
      var element = $compile('<div ui-scrollfix="+100"></div>')(scope);
      angular.element($window).trigger('scroll');
      expect(element.hasClass('ui-scrollfix')).toBe(false);
    });
  });
});
