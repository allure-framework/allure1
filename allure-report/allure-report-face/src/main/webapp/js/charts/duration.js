/* globals angular */
angular.module('allure.charts.duration', ['allure.charts.util']).directive('duration', function (d3, d3Util, d3Tooltip, timeFilter) {
    function timeFormat() {
        var formats = [
            [d3.time.format("%d"), function(d) { return d.getUTCDay() && d.getUTCDate() != 1; }],
            [d3.time.format("%I:%M"), function(d) { return d.getUTCHours(); }],
            [d3.time.format("%Mm"), function(d) { return d.getUTCMinutes(); }],
            [d3.time.format("%-Ss"), function(d) { return d.getUTCSeconds(); }],
            [d3.time.format("%Lms"), function(d) { return d.getUTCMilliseconds(); }]
        ];
        return function(date) {
            if(date.valueOf() === 0) {
                return "0";
            }
            var i = formats.length - 1, f = formats[i];
            while (!f[1](date)) f = formats[--i];
            return f[0](date);
        }
    }
    function DurationBars(element, data) {
        var width = angular.element(element).width()/2.2,
            height = width*0.6,
            svg = new d3Util.SvgViewport(element, {
                width: width,
                height: height,
                margin: {left: 60, bottom: 30}
            }),
            x = d3.time.scale.utc().range([0, width]),
            y = d3.scale.sqrt().range([height, 0 ], 1),
            bins;

        x.domain([0, Math.max(d3.max(data, function(d) {return d.time.duration;}), 1)]);
        bins = d3.layout.histogram().value(function(d) {
            return d.time.duration;
        }).bins(x.ticks())(data).map(function(bin) {
            return {
                x: bin.x,
                y: bin.y,
                dx: bin.dx,
                testcases: bin
            };
        });
        y.domain([0, d3.max(bins, function(d) {return d.y;})]).nice();

        svg.select('.x-axis-group.axis').call(
            d3.svg.axis().scale(x).orient('bottom').tickFormat(timeFormat())
        );
        svg.select('.y-axis-group.axis').call(
            d3.svg.axis().scale(y).orient('left')
        );
        svg.selectAll('.x-axis-group.axis, .chart-group').attr({transform: 'translate(0,' + (height) + ')'});

        var container = svg.select('.container-group'),
            median = y(d3.median(bins, function(d) {return d.y;})),
            bars = container.selectAll(".bar").data(bins).enter().append('rect').classed('bar fill-default', true);

        bars.attr({
            x: function(d) { return x(d.x)+1 },
            width: x(bins[0].dx)-2,
            y: median,
            height: height - median
        }).transition().duration(500).attr({
            y: function(d) { return y(d.y) },
            height: function(d) {return height - y(d.y);}
        });

        var gridY = function (d) {
            return y(d);
        };
        container.selectAll('line.y').data(y.ticks()).enter().insert('line', ':first-child')
            .style({ 'stroke': "#000000", 'stroke-opacity': 0.1, 'stroke-width': '0.5px' })
            .attr({ x1: 0, x2: width, y1: gridY, y2: gridY });

        container.append("text")
            .attr({
                "class": "y label",
                "transform": "translate(-30" + "," + height / 2 + ") rotate(-90)",
                "text-anchor": "middle"
            }).text("Testcases");

        this.tooltip = new d3Tooltip(bars,
            '<div><strong><span ng-pluralize count="testcases.length" when="{\'one\': \'{} testcase\', \'other\': \'{} testcases\'}"></span> {{status | lowercase}}</strong></div>' +
                '<ul><li ng-repeat="testcase in testcases | limitTo:10">{{testcase.title}}</li></ul>' +
                '<div ng-if="testcases.length > 10"><i>and {{testcases.length-10}} more</i></div>',
            {tooltipCls: 'd3-tooltip d3-tooltip-list'}
        );
    }
    DurationBars.prototype.destroy = function() {
        this.tooltip.destroy();
    };
    return {
        scope: {
            data: '='
        },
        link: function ($scope, elm) {
            $scope.$watch('data', function (data) {
                $scope.chart = new DurationBars(elm[0], data);
            });
            $scope.$on('$destroy', function() {
                $scope.chart.destroy();
            });
        }
    };
});
