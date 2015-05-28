/* globals angular */
angular.module('allure.core.charts.timeline', ['allure.core.charts.util']).directive('timeline', function (d3, d3Util, d3Tooltip, timeFilter) {
    'use strict';
    function Timeline(elm, data, range) {
        var scale = 3,
            barHeight = 6 * scale,
            chartWidth = angular.element(elm).width()/2,
            chartHeight = barHeight * data.reduce(function(total, host) {
                return total + host.threads.length + 1;
            }, 0),
            x = this.x = d3.scale.linear().domain(range).nice().range([0, chartWidth]),
            xAxis = d3.svg.axis().scale(x).orient('bottom').tickFormat(function(d) {
                return timeFilter(d);
            });
        this.svg = new d3Util.SvgViewport(elm, {
            margin: {top: 0},
            width: chartWidth,
            height: chartHeight
        });

        this.svg.select('.x-axis-group.axis').attr({transform: 'translate(0,' + (chartHeight) + ')'}).call(xAxis);

        //create empty set of lines
        var stripesPlaceholder = this.svg.select('.container-group').selectAll('line.x');
        //fill the set real data
        stripesPlaceholder.data(x.ticks()).enter().append('line')
            .attr({ 'class': 'tick-line', x1: x, x2: x, y1: 0, y2: chartHeight });

        this.createHosts(data, barHeight);
        this.tooltip = new d3Tooltip(this.svg.selectAll('.bar'), '<div>{{title}} ({{status | translate}})</div><div><b>{{time.start | time}} - {{time.stop | time}}</b></div>');
    }
    Timeline.prototype  = {
        createHosts: function (data, barHeight) {
            var x = this.x,
                offset = 0;
            this.hosts = data.map(function(host, i) {
                var testcases = host.threads.reduce(function(testcases, thread) {
                        thread.testCases.forEach(function (testcase) {
                            testcase.thread = thread.title;
                            testcases.push(testcase);
                        });
                        return testcases;
                    }, []),
                    height = (host.threads.length+1)*barHeight,
                    result = {
                        y: d3.scale.ordinal().domain([''].concat(host.threads.map(function (d) {
                            return d.title;
                        }))).rangeRoundBands([0, height]),
                        elm: this.svg.select('.container-group')
                            .append('g')
                            .classed('timeline-group', true)
                            .attr('transform', 'translate(0, '+offset+')')
                    };
                var barGap = barHeight / 4,
                    barThickness = barHeight - barGap,
                    bars = result.elm.selectAll('.bar').data(testcases).enter().append('rect');

                bars.attr({
                    'class': function(d) {
                        return 'fill-'+ d.status.toLowerCase();
                    },
                    rx: 1,
                    ry: 1,
                    x: x.range()[0],
                    width: 0,
                    y: function (d) {
                        return result.y(d.thread) + barGap / 2;
                    },
                    height: barThickness
                }).classed('bar', true);
                bars.transition().duration(500).attr({
                    x: function (d) {
                        return x(d.time.start);
                    },
                    width: function (d) {
                        return x(d.time.stop - d.time.start);
                    }
                });
                this.renderHostname(result.elm, host, x, result.y);

                result.elm.append('g').classed('axis', true).call(
                    d3.svg.axis().scale(result.y).orient('left').tickFormat("").tickSize(0)
                );
                offset += height;
                return result;
            }, this);
        },
        renderHostname: function(elm, host, x, y) {
            elm.append('rect').classed('timeline-group-cover', true).attr({
                "x": x.range()[0]+0.5,
                "y": 0,
                "width": x.range()[1] - x.range()[0],
                "height": y.range()[1]
            });
            elm.append('text').text(host.title).attr({
                'class': 'timeline-group-title',
                y: y(0),
                dy: '1.2em',
                dx: '1em'
            });
        },
        destroy: function() {
            this.tooltip.destroy();
            this.svg.remove();
        }
    };

    return {
        scope: {
            timeline: '=',
            range: '=',
            onItemClick: '&'
        },
        link: function ($scope, $element) {
            $scope.$watch('timeline', function(data) {
                if($scope.chart) {
                    $scope.chart.destroy();
                }
                $scope.chart = new Timeline($element[0], data, $scope.range);
                $scope.chart.svg.selectAll('.bar').on('click', function(d) {
                    $scope.onItemClick({item: d});
                });
            });
            $scope.$on('$destroy', function() {
                $scope.chart.destroy();
            });
        }
    };
});
