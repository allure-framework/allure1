/* globals angular */
angular.module('allure.core.charts.pie', ['allure.core.charts.util']).directive('pieChart', function (d3, d3Util, d3Tooltip) {
    "use strict";
    function PieChart(elm, $scope, data) {
        var radius = angular.element(elm).width()/4,
            width = radius * 2,
            height = radius * 2,
            svg = new d3Util.SvgViewport(elm, {
                width: width,
                height: height,
                margin: {left: radius/2, right: radius*1.5, top: 40}
            }),
            arc = d3.svg.arc().outerRadius(1).innerRadius(0),
            pie = d3.layout.pie().sort(null).value(function (d) {
                return d.value;
            });

        var container = svg.select('.container-group').append("g").attr("transform", "translate(" + radius + "," + radius + ")");
        this.$scope = $scope;
        this.sectors = container.selectAll(".arc").data(pie(data)).enter().append("g").attr("class", "arc").append("path");
        this.sectors.attr("d", arc).attr('class', function(d) {
            return 'fill-'+d.data.name;
        }).style({
            'stroke': '#FFF',
            'stop-opacity': 0
        });
        this.sectors.transition().duration(750).attrTween("d", function(a) {
            var radiusFunc = d3.interpolate(10, radius - 10),
                startAngleFunc = d3.interpolate(0, a.startAngle),
                endAngleFunc = d3.interpolate(0, a.endAngle);
            return function(t) {
                return arc.outerRadius(radiusFunc(t)).startAngle(endAngleFunc(t)).endAngle(startAngleFunc(t))(a);
            };
        });
        this.sectors.on('click', this.onSectorClick.bind(this));
        this.legend = new d3Util.Legend(
            svg.select('.container-group'),
            data,
            {
                top: height/3,
                left: 2.5*radius,
                rectWidth: radius/7,
                rectHeight: radius/10,
                columns: 1
            }
        );

        this.legend.items.selectAll('rect').attr('class', function(d) {
            return 'fill-'+d.name;
        });
        this.legend.items.on('mouseover', this.onLegendHover.bind(this))
            .on('mouseout', this.onLegendOut.bind(this))
            .on('click', this.onLegendClick.bind(this));
    }
    PieChart.prototype.onLegendClick = function(legendData) {
        var sector = this.getSectorByName(legendData.name);
        this.toggleSector(sector.node(), !sector.data()[0].selected);
        this.$scope.$apply();
    };
    PieChart.prototype.onLegendHover = function(legendData) {
        var activeItem = this.getSectorByName(legendData.name).classed('active', true),
            offset = angular.element(activeItem.node()).offset(),
            size = activeItem.node().getBoundingClientRect();
        this.tooltip.show(activeItem.data()[0], offset.left+size.width/2, offset.top+size.height/2);
    };
    PieChart.prototype.onLegendOut = function() {
        this.sectors.classed('active', false);
        this.tooltip.hide();
    };
    PieChart.prototype.onSectorClick = function (d) {
        this.toggleSector(d3.event.target, !d.selected);
        this.$scope.$apply();
    };
    PieChart.prototype.toggleSector = function(sector, selected) {
        this.deactivateAllSectors();
        if(selected) {
            this.activateSector(sector);
        }
    };
    PieChart.prototype.getSectorByName = function(name) {
        return this.sectors.filter(function (data) {
            return data.data.name === name;
        });
    };
    PieChart.prototype.activateSector = function(sector) {
        var data = d3.select(sector).data()[0],
            r = 10,
            angle = (data.endAngle + data.startAngle) / 2,
            x = r * Math.sin(angle),
            y = -r * Math.cos(angle);
        this.sectors.filter(function() {
                return this === sector;
            })
            .transition().duration(500)
            .attr({
                transform: 'translate(' + x + ',' + y + ')'
            });
        data.selected = true;
        this.$scope.selected = data.data.name.toUpperCase();
    };
    PieChart.prototype.deactivateAllSectors = function() {
        this.sectors.each(function(d) {
                d.selected = false;
            })
            .transition().duration(500)
            .attr({
                transform: 'translate(0, 0)'
            });
        delete this.$scope.selected;
    };
    PieChart.prototype.setTooltipFormat = function(format) {
        if(!this.tooltip) {
            this.tooltip = new d3Tooltip(this.sectors, format);
        }
        else {
            this.tooltip.format = format;
        }
    };
    PieChart.prototype.destroy = function() {
        this.tooltip.destroy();
    };

    return {
        restrict: 'EA',
        scope: {
            tooltipTpl: '=',
            data: '=',
            selected: '='
        },
        link: function ($scope, elm) {
            $scope.$watch('data', function (data) {
                var format = $scope.tooltipTpl || '<div><b>{{ \'graph.TESTS\' | translate:"{ amount: value }":"messageformat" }} ({{data.part * 100 | number:0}}%)</b></div>' +
                    '<div>{{data.name | translate}}</div>';
                $scope.chart = new PieChart(elm[0], $scope, data);
                $scope.chart.setTooltipFormat(format);
            });
            $scope.$on('$destroy', function() {
                $scope.chart.destroy();
            });
        }
    };
});
