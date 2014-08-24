/*global describe:true, it:true, beforeEach:true, afterEach:true, expect:true, spyOn:true, module:true, inject:true, angular:true, jasmine:true */
describe('Testcase controllers', function() {
    'use strict';
    var $controller, $rootScope,
        collection;

    beforeEach(module('allure.testcase.controllers', function($provide) {
        $provide.value('Collection', function() {
            collection = jasmine.createSpyObj('Collection', ['filter', 'sort', 'limitTo', 'indexOf', 'getNext', 'getPrevious']);
            collection.getNext.andReturn({source:'log'});
            collection.getPrevious.andReturn({source:'log'});
            return collection;
        });
    }));
    beforeEach(inject(function (_$controller_, _$rootScope_) {
        $controller = _$controller_;
        $rootScope = _$rootScope_;
    }));

    describe('StepCtrl', function() {
        function Step(title, attachments, failure) {
            this.title = title;
            this.status = failure ? 'FAILED' : 'PASSED';
            this.summary = {steps: 0};
            this.attachments = attachments || [];
            this.failure = failure;
            this.steps = [];
        }

        function createController(scopeValues) {
            var scope = $rootScope.$new();
            scope = scope.$new();
            angular.extend(scope, scopeValues);
            $controller('StepCtrl', {
                $scope: scope
            });
            return scope;
        }

        it('should expand failed steps' , function() {
            var scope = createController({ step: new Step('Dummy step', [{title: 'file'}], {message: 'Error'}) });
            expect(scope.expanded).toBeTruthy();
        });

        it('should not expand passed steps' , function() {
            var scope = createController({ step: new Step('Dummy step') });
            expect(scope.expanded).toBeFalsy();
        });

        it("should expand step when attachment preset in scope", function() {
            var attachment = {title: 'file'};
            var scope = createController({ step: new Step('Dummy step', [attachment]) });
            $rootScope.attachment = attachment;
            $rootScope.$apply();
            expect(scope.expanded).toBeTruthy();
        });

        it('should detect empty content', function() {
            var scope = createController({ step: new Step('Dummy step') });
            expect(scope.hasContent).toBeFalsy();
        });

        it('should detect existing content', function() {
            var scope = createController({ step: new Step('Dummy step', [{title: 'file'}]) });
            expect(scope.hasContent).toBeTruthy();
        });
    });

    describe('AttachmentPreviewCtrl', function() {
        var backendDefinitions = {
                'report.txt': 'test content',
                'report.xml': '<root>test content xml</root>',
                'report.json': '{"root": {"content":"test content"}}'
            },
            $httpBackend, stateMock;
        beforeEach(inject(function(_$httpBackend_) {
            $httpBackend = _$httpBackend_;
        }));
        afterEach(function() {
            $httpBackend.resetExpectations();
        });
        function createController(attachment) {
            var scope = $rootScope.$new();
            scope = scope.$new();
            angular.extend(scope, {attachment: attachment});
            $controller('AttachmentPreviewCtrl', {
                $scope: scope,
                $state: stateMock = {
                    go: jasmine.createSpy('gotoStateSpy'),
                    is: jasmine.createSpy('isStateSpy')
                }
            });
            scope.$apply();
            return scope;
        }

        //parametrized test example
        function createDetectImageTest(imageType) {
            it('should detect '+imageType+' type', function() {
                var scope = createController({type: imageType, name: 'picture', source:'pic'});
                expect(scope.type).toBe('image');
            });
        }
        createDetectImageTest('image/jpeg');
        createDetectImageTest('image/png');

        function createDetectTextTest(language) {
            it('should detect '+language, function() {
                var filename = 'report.'+language.toLowerCase();
                $httpBackend.expectGET('data/'+filename).respond(backendDefinitions[filename]);
                var scope = createController({type: language, name: 'report', source:'report.'+language.toLowerCase()});
                expect(scope.type).toBe('text');

                $httpBackend.flush();
                expect(scope.attachText).toBe(backendDefinitions['report.'+language.toLowerCase()]);
            });
        }

        createDetectTextTest('text/xml');
        createDetectTextTest('application/json');
        createDetectTextTest('text/plain');

        it("should extract 'json' from 'application/json'", function() {
            $httpBackend.expectGET('data/report.json').respond(backendDefinitions['report.json']);
            var scope = createController({type: 'application/json', name: 'report', source:'report.json'});
            expect(scope.language).toBe('json');
        });

        it('should pass other types', function() {
            var scope = createController({type: '*/*', name: 'config', source:'config.properties'});
            expect(scope.type).toBeUndefined();
        });

        it('should show message when load error has occurred', function() {
            $httpBackend.expectGET('data/report.xml').respond(404);
            var scope = createController({type: 'text/xml', name: 'report', source:'report.xml'});
            $httpBackend.flush();
            expect(scope.notFound).toBeTruthy();
        });

        it('should re-detect type when attachment has changed', function() {
            var scope = createController({type: 'image/png', name: 'picture', source:'pic'});
            expect(scope.type).toBe('image');
            scope.attachment = {type: 'text/html', name: 'report', source:'report.html'};
            scope.$apply();
            expect(scope.type).toBeUndefined();
        });

        describe('expanded state', function() {
            function createExpanedStateTests(expanded, stateName) {
                it('should detect expanded state ['+expanded+']', function() {
                    var scope = createController({type: 'image/png', name: 'picture', source:'pic'});
                    stateMock.is.andReturn(expanded);
                    expect(scope.isExpanded()).toBe(expanded);
                });
                it('should toggle expanded state ['+expanded+']', function() {
                    var scope = createController({type: 'image/png', name: 'picture', source:'pic'});
                    stateMock.is.andReturn(expanded);
                    scope.toggleExpanded();
                    expect(stateMock.go.calls.length).toBe(1);
                    expect(stateMock.go).toHaveBeenCalledWith(stateName);

                });
            }
            createExpanedStateTests(true, 'home.testsuite.testcase.attachment');
            createExpanedStateTests(false, 'home.testsuite.testcase.attachment.expanded');
        });
    });

    describe('TestcaseCtrl', function() {
        var state, treeUtils,
            scope;

        function Step(status, steps) {
            this.status = status;
            this.steps = steps || [];
        }

        function createController(testcase) {
            var scope = $rootScope.$new();
            scope = scope.$new();
            $controller('TestcaseCtrl', {
                $scope: scope,
                testcase: testcase,
                $state: state = {
                    current: {data: {baseState: 'base'}},
                    go: jasmine.createSpy('gotoStateSpy'),
                    is: jasmine.createSpy('isStateSpy')
                },
                treeUtils: treeUtils = {walkAround: jasmine.createSpy('treeWalkSpy').andCallFake(function(testcase, prop, callback) {
                    callback(testcase);
                })}
            });
            scope.$apply();
            return scope;
        }

        describe('state checks', function() {
            beforeEach(function() {
                scope = createController({steps: []});
            });
            it('should add base when checking isState', function() {
                scope.isState('testcase');
                expect(state.is).toHaveBeenCalledWith('base.testcase');
            });
            it('should go to baseState on closing', function() {
                scope.closeTestcase();
                expect(state.go).toHaveBeenCalledWith('base');
            });
            it('should add base when going', function() {
                scope.go('testcase');
                expect(state.go).toHaveBeenCalledWith('base.testcase');
            });
            it('should go to attachment state', function() {
                scope.setAttachment('log');
                expect(state.go).toHaveBeenCalledWith('base.testcase.attachment', {attachmentUid: 'log'});
            });
        });

        describe('state change handling', function() {
            beforeEach(function() {
                scope = createController({attachments: [{uid: 'uid', source: 'log', name: 'console log'}], steps: []});
            });
            it('should set attachment when uid is present', function() {
                scope.$broadcast('$stateChangeSuccess',  null, {attachmentUid: 'uid'});
                expect(scope.attachment).toBeDefined();
            });
            it('should not has attachment on route without uid', function() {
                scope.$broadcast('$stateChangeSuccess',  null, {});
                expect(scope.attachment).toBeUndefined();
            });
        });

        it('should navigate down', function() {
            var scope = createController({attachments: [{source: 'log'}], steps: []});
            scope.select(1);
            expect(collection.getNext).toHaveBeenCalled();
        });

        it('should navigate up', function() {
            var scope = createController({attachments: [{source: 'log'}], steps: []});
            scope.select(-1);
            expect(collection.getPrevious).toHaveBeenCalled();
        });
    });
});
