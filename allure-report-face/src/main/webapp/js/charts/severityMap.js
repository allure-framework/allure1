/* globals angular */
angular.module('allure.core.charts.severityMap', ['allure.core.charts.util']).directive('severityMap', function (d3, d3Util, d3Tooltip, status, severity) {
    'use strict';
    function SeverityMap(elm, severities, data) {
        var itemWidth = angular.element(elm).width()/14,
            width = severities.length * itemWidth,
            height = 200,
            svg = new d3Util.SvgViewport(elm, {
                width: width,
                height: height,
                margin: {left: 60, bottom: 30}
            }),
            x = d3.scale.ordinal().rangeRoundBands([0, width], 0.2),
            y = d3.scale.sqrt().range([height, 0 ], 1),
            status = d3.scale.ordinal();

        x.domain(severities);
        y.domain([0, d3.max(data, function(d) {
            return d3.max(d, function(d) {
                return d.testcases.length;
            });
        })]).nice();
        status.domain(data[0].map(function(d) {
            return d.status;
        })).rangeRoundBands([0, x.rangeBand()]);

        svg.select('.x-axis-group.axis').call(
            d3.svg.axis().scale(x).orient('bottom').tickFormat(function (d) {
                return d.toLowerCase();
            })
        );
        svg.select('.y-axis-group.axis').call(
            d3.svg.axis().scale(y).orient('left')
        );
        svg.selectAll('.x-axis-group.axis, .chart-group').attr({transform: 'translate(0,' + (height) + ')'});

        var gridY = function (d) {
                return y(d);
            };
        svg.select('.container-group').selectAll('line.y').data(y.ticks()).enter().insert('line', ':first-child')
            .style({ 'stroke': "#000000", 'stroke-opacity': 0.1, 'stroke-width': '0.5px' })
            .attr({ x1: 0, x2: width, y1: gridY, y2: gridY });

        this.bars = svg.select('.chart-group').selectAll('.status').data(data).enter()
            .append('g').attr("transform", function(d) { return "translate(" + x(d[0].severity) + ",0)"; });
        this.bars.selectAll('.bar').data(function(d) {
                return d;
            })
            .enter().append('rect')
            .attr({
                x: function(d) {return status(d.status);},
                y: 0,
                height: 0,
                width: status.rangeBand(),
                'class': function (d) {
                    return 'bar fill-' + d.status.toLowerCase();
                }
            })
            .transition().duration(500)
            .attr({
                height: function(d) { return y.range()[0] - y(d.testcases.length); },
                y: function(d) { return -y.range()[0] + y(d.testcases.length); }
            });

        this.tooltip = new d3Tooltip(this.bars.selectAll('.bar'),
            '<div><strong><span>{{ \'graph.CASES\' | translate:"{ amount: testcases.length }":"messageformat" }}</span> {{status | lowercase | translate}}</strong></div>' +
                '<ul><li ng-repeat="testcase in testcases | limitTo:10">{{testcase.title}}</li></ul>' +
                '<div ng-if="testcases.length > 10"><i>and {{testcases.length-10}} more</i></div>',
            {tooltipCls: 'd3-tooltip d3-tooltip-list'}
        );
    }
    SeverityMap.prototype.destroy = function () {
        this.tooltip.destroy();
    };
    return {
        scope: {
            data: '=',
            selected: '='
        },
        link: function ($scope, elm) {
            $scope.$watch('data', function (data) {
                var map = severity.all.map(function (severity) {
                        return status.all.map(function (status) {
                            return {severity: severity, status: status, testcases: []};
                        });
                    }),
                    severities = severity.all;
                data.forEach(function (testcase) {
                    map[severity.all.indexOf(testcase.severity)][status.all.indexOf(testcase.status)].testcases.push(testcase);
                });
                $scope.chart = new SeverityMap(elm[0], severities, map);
            });
            $scope.$on('$destroy', function () {
                $scope.chart.destroy();
            });
        }
    };
});
