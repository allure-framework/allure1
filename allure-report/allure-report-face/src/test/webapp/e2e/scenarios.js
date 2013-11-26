'use strict';

/* http://docs.angularjs.org/guide/dev_guide.e2e-testing */

describe('Allure Main Page', function() {

  beforeEach(function() {
    browser().navigateTo('/main/index.html');
  });


  it('should automatically redirect to /home when location hash/fragment is empty', function() {
    expect(browser().location().url()).toBe("/home");
  });

});
