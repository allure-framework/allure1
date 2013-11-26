module.exports = function (config) {
    config.set({
        basePath: '../../',

        files: [
            'main/webapp/lib/angular/angular.js',
            'main/webapp/lib/angular/angular-*.js',
            'main/webapp/js/**/*.js',

            'test/webapp/lib/angular/angular-mocks.js',
            'test/webapp/unit/**/*.js'
        ],

        autoWatch: true,

        frameworks: ['jasmine'],

        browsers: ['Chrome'],

        plugins: [
            'karma-junit-reporter',
            'karma-chrome-launcher',
            'karma-firefox-launcher',
            'karma-jasmine'
        ],

        junitReporter: {
            outputFile: 'test_out/unit.xml',
            suite: 'unit'
        }

    })
};
