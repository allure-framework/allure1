angular.module('ui.router', []).provider('$state', function() {
    "use strict";
    var $stateProvider = jasmine.createSpyObj('stateProvider', ['state', '$get']);
    $stateProvider.state.andReturn($stateProvider);
    return $stateProvider;
});
