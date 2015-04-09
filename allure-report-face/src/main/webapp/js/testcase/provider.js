/*global angular*/
angular.module('allure.core.testcase.provider', []).provider('testcase', function($stateProvider) {
    "use strict";
    function processResponse(response) {
        return response.data;
    }
    return {
        attachStates: function(baseState) {
            var viewName = 'testcase@'+baseState.split('.')[0],
                state = {
                    url: '/:testcaseUid',
                    views: {},
                    data: {
                        baseState: baseState
                    },
                    resolve: {
                        testcase: function($http, $stateParams) {
                            return $http.get('data/'+$stateParams.testcaseUid+'-testcase.json').then(processResponse);
                        }
                    }

                };
            state.views[viewName] = {
                templateUrl: 'templates/testcase/testcaseView.html',
                controller: 'TestcaseCtrl'
            };
            $stateProvider.state(baseState+'.testcase', state)
                .state(baseState+'.testcase.expanded', {
                    url: '/expanded'
                })
                .state(baseState+'.testcase.attachment', {
                    url: '/:attachmentUid'
                })
                .state(baseState+'.testcase.attachment.expanded', {
                    url: '/expanded'
                });
        },
        $get: [function() {}]
    };
});
