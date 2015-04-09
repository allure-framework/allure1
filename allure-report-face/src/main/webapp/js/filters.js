/*global angular, filesize*/
angular.module('allure.core.filters', ['d3'])
    .filter('interpolate', ['version', function (version) {
        'use strict';
        return function (text) {
            return String(text).replace(/\%VERSION\%/mg, version);
        };
    }])
    .filter('filesize', [function () {
        'use strict';
        return function (size) {
            return size ? filesize(size, {base: 2, round: 1}) : size;
        };
    }])
    .filter('d3time', function(d3) {
        function getTotalHours(time) {
            return Math.floor(time.valueOf()/(3600*1000));
        }
        var formats =[
            [function(date) {return getTotalHours(date)+'h';}, getTotalHours],
            [d3.time.format.utc("%-Mm"), function(d) { return d.getUTCMinutes(); }],
            [d3.time.format.utc("%-Ss"), function(d) { return d.getUTCSeconds(); }],
            [d3.time.format.utc("%-Lms"), function(d) { return d.getUTCMilliseconds(); }]
        ];
        return function(time) {
            if(time.valueOf() === 0) {
                return "0";
            }
            var i = formats.length - 1,
                format = formats[i];
            while (!format[1](time)) {
                i--;
                format = formats[i];
            }
            return format[0](time);
        };
    })
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
    })
    .filter('linky', ['$sce', function($sce) {
        "use strict";
        function addLink(text) {
            return ['<a href="', text, '" target="_blank">', text, '</a>'].join('');
        }
        var LINKY_URL_REGEXP = /^(\w)+:\/\/.*/;

        return function(text) {
            if (!text) {
                return text;
            }
            return $sce.trustAsHtml(LINKY_URL_REGEXP.test(text) ? addLink(text) : text);
        };
    }])
    .filter('trustAsHtml', ['$sce', function($sce) {
        "use strict";
        return function(value) {
            return $sce.trustAsHtml(''+value);
        };
    }]);
