/* globals angular */
angular.module('allure.charts.timeline', ['allure.charts.util']).directive('timeline', function (d3, d3Util, d3Tooltip, $state, timeFilter) {
    'use strict';
    function Timeline(elm, data) {
        var scale = 3,
            barHeight = 6 * scale,
            chartWidth = angular.element(elm).width()/2,
            chartHeight = barHeight * data.length,
            x = this.x = d3.scale.linear().domain([0, d3.max(data, function (d) {
                return d3.max(d, function(d) {
                    return d.time.stop;
                });
            })]).nice().range([0, chartWidth]),
            y = this.y = d3.scale.ordinal().domain(data.map(function (d, i) {
                return i;
            })).rangeRoundBands([0, chartHeight]),
            xAxis = d3.svg.axis().scale(x).orient('bottom').tickFormat(function(d) {
                return timeFilter(d);
            });
        this.svg = new d3Util.SvgViewport(elm, {
            width: chartWidth,
            height: chartHeight
        });

        this.svg.select('.x-axis-group.axis').attr({transform: 'translate(0,' + (chartHeight) + ')'}).call(xAxis);

        //create empty set of lines
        var stripesPlaceholder = this.svg.select('.container-group').selectAll('line.x');
        //fill the set real data
        stripesPlaceholder.data(x.ticks()).enter().append('line')
            .style({ 'stroke': "#000000", 'stroke-opacity': 0.1, 'stroke-width': '0.5px' })
            .attr({ x1: x, x2: x, y1: 0, y2: chartHeight });

        this.svg.select('.container-group').append("text")
            .attr("class", "x label")
            .attr("text-anchor", "middle")
            .attr("x", chartWidth / 2)
            .attr("y", chartHeight + 30)
            .text("Time from start, ms");

        this.rows = this.svg.select('.chart-group').selectAll('g').data(data).enter().append('g');
        this.bars = this.rows.selectAll('.bar')
            .data(function(d){ return d; }).enter().append('rect');

        var barGap = barHeight / 4,
            barThickness = barHeight - barGap;

        this.bars.attr({
            'class': function(d) {
                return 'fill-'+ d.status.toLowerCase();
            },
            rx: 1,
            ry: 1,
            x: x.range()[0],
            width: 0,
            y: function (d, i, j) {
                return y(j) + barGap / 2;
            },
            height: barThickness
        }).classed('bar', true);
        this.bars.transition().duration(500).attr({
            x: function (d) {
                return x(d.time.start);
            },
            width: function (d) {
                return x(d.time.stop - d.time.start);
            }
        });
        this.tooltip = new d3Tooltip(this.bars, '<div>{{title}} ({{status}})</div><div><b>{{time.start | time}} - {{time.stop | time}}</b></div>');
    }
    Timeline.prototype  = {
        destroy: function() {
            this.tooltip.destroy();
            this.svg.remove();
        }
    };

    return {
        transclude: true,
        replace: true,
        template: '<div ng-transclude=""></div>',
        controller: function($scope, $element, $timeout) {
            var updateTimeout;
            function normalizeTimes(data) {
                if(!data.length) {
                    return data;
                }
                data = data.filter(function(item) {
                    return item.time.duration > 0;
                });
                data.sort(function(a, b) {
                    return a.time.start - b.time.start;
                });
                var minTime = data[0].time.start;
                return data.map(function(item) {
                    item.time.start -= minTime;
                    item.time.stop  -= minTime;
                    return item;
                });
            }
            function groupItems(data) {
                var results = [];
                data.forEach(function(item) {
                    if(!results.some(function(result) {
                        var lastItem = result[result.length-1];
                        if(lastItem.time.stop <= item.time.start) {
                            result.push(item);
                            return true;
                        }
                        return false;
                    })) {
                        results.push([item]);
                    }
                });
                return results;
            }

            var data = [],
                onTimestampChange = function() {
                    if($scope.chart) {
                        $scope.chart.destroy();
                    }
                    var sortedData = normalizeTimes(angular.copy(data));
                    $scope.chart = new Timeline($element[0], groupItems(sortedData));
                };

            this.addTimestamp = function(timestamp) {
                data.push(timestamp);
                if(updateTimeout) {
                    $timeout.cancel(updateTimeout);
                }
                updateTimeout = $timeout(onTimestampChange, 100);
            };
            this.removeTimestamp = function(timestamp) {
                data.splice(data.indexOf(timestamp), 1);
                if(updateTimeout) {
                    $timeout.cancel(updateTimeout);
                }
                updateTimeout = $timeout(onTimestampChange, 100);
            };
        },
        link: function ($scope) {
            $scope.$on('$destroy', function() {
                $scope.chart.destroy();
            });
        }
    };
}).
directive('timestamp', function() {
    'use strict';
    return {
        require: '^timeline',
        restrict: 'E',
        scope: {
            data: '='
        },
        link: function(scope, elem, attr, ctrl) {
            ctrl.addTimestamp(scope.data);
            scope.$on('$destroy', function() {
                ctrl.removeTimestamp(scope.data);
            });
        }
    };
});
