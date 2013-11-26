/*global angular*/
angular.module('allure.filters', [])
    .filter('interpolate', ['version', function (version) {
        'use strict';
        return function (text) {
            return String(text).replace(/\%VERSION\%/mg, version);
        };
    }])
    .filter('time', function() {
        'use strict';
        function getTotalHours(time) {
            return Math.floor(time.valueOf()/(3600*1000));
        }
        return function(timeInt) {
            if(!timeInt) {
                return '0';
            }
            var time = new Date(timeInt),
                val = {
                    hours: getTotalHours(time),
                    minutes: time.getUTCMinutes(),
                    seconds: time.getUTCSeconds(),
                    milliseconds: time.getUTCMilliseconds()
                },
                result = [];
            if(val.hours > 0) {
                result.push(val.hours + 'h');
            }
            if(val.hours > 0 || val.minutes > 0) {
                result.push(val.minutes + 'm');
            }
            if(result.length < 2 && (val.minutes > 0 || val.seconds > 0)) {
                result.push(val.seconds + 's');
            }
            if(result.length < 2 && val.milliseconds > 0) {
                result.push(val.milliseconds + 'ms');
            }
            return result.join(' ');
        };
    });
