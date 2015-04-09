/* globals angular, d3 */
angular.module('allure.core.charts.util', [],function ($provide) {
        'use strict';
        $provide.constant('d3', window.d3);
    }).config(function() {
        'use strict';
        if (!Function.prototype.bind) {
            //add polyfill for PhantomJS
            Function.prototype.bind = function (oThis) {
                if (typeof this !== "function") {
                    // closest thing possible to the ECMAScript 5 internal IsCallable function
                    throw new TypeError("Function.prototype.bind - what is trying to be bound is not callable");
                }
                var aArgs = Array.prototype.slice.call(arguments, 1),
                    fToBind = this,
                    fNOP = function () {},
                    fBound = function () {
                        return fToBind.apply(
                            this instanceof fNOP && oThis ? this : oThis,
                            aArgs.concat(Array.prototype.slice.call(arguments))
                        );
                    };
                fNOP.prototype = this.prototype;
                fBound.prototype = new fNOP();
                return fBound;
            };
        }
    })
    .factory('d3Util', function (d3, $window, $rootScope) {
        'use strict';
        function SvgViewport(elm, config) {
            config = angular.extend({}, config);
            config.margin = angular.extend({left: 20, right: 20, top: 20, bottom: 20}, config.margin);
            var $element = angular.element(elm),
                viewportHeight = config.height + config.margin.top + config.margin.bottom,
                viewportWidth = config.width + config.margin.left + config.margin.right,
                ratio = viewportWidth/viewportHeight;

            var svg = d3.select(elm).classed('svg-viewport', true).append('svg').attr({
                    viewBox: "0 0 " + viewportWidth + " " + viewportHeight
                }),
                $svg = angular.element(svg.node());
            $rootScope.$watch(function() {
                return $element.width();
            }, updateSize);
            angular.element($window).on('resize', updateSize);

            var container = svg.append('g').classed('container-group', true).attr({transform: 'translate(' + config.margin.left + ',' + config.margin.top + ')'});
            container.append('g').classed('chart-group', true);
            container.append('g').classed('x-axis-group axis', true);
            container.append('g').classed('y-axis-group axis', true);
            return svg;

            // explicitly declare height of element, because chrome can't do it itself
            function updateSize() {
                $svg.remove();
                var width = $element.width(),
                    height = $element.height(),
                    elmRatio = width/height;
                if (!height || elmRatio < ratio) {
                    height = width / ratio;
                } else {
                    width = height * ratio;
                }
                $svg.appendTo($element);
                $svg.css({width: width, height: height});
            }
        }

        function Legend(elm, data, config) {
            config = angular.extend({
                top: 0,
                left: 0,
                width: 100,
                columns: 1,
                rowPadding: 5,
                rectWidth: 20,
                rectHeight: 15
            }, config);
            this.items = elm.append('g').classed('legend', true).attr({
                transform: 'translate('+config.left+', ' + config.top + ')'
            }).selectAll('.legend.item').data(data).enter().append("g");
            this.items.attr('transform', function (d, i) {
                var x = (i % config.columns) * config.width / config.columns,
                    y = Math.floor(i / config.columns) * (config.rectHeight + config.rowPadding);
                return 'translate(' + x + ',' + y + ')';
            });
            this.items.append('rect').attr({
                'rx': 2, 'ry': 2,
                'width': config.rectWidth,
                'height': config.rectHeight
            });
            this.items.append('text').attr({
                x: config.rectWidth + 5,
                y: config.rectHeight - 2
            }).text(function (d) {
                return d.name;
            }).style('font-size', config.rectHeight+'px');
        }
        return {
            SvgViewport: SvgViewport,
            Legend: Legend
        };
    })
    .factory('d3Tooltip', function(d3, $window, $document, $compile, $rootScope) {
        'use strict';
        function setContentAndCompile(element, template, values) {
            var scope = $rootScope.$new();
            angular.extend(scope, values);
            element.html(template);
            $compile(angular.element(element.node()))(scope);
            scope.$digest();
        }
        function Tooltip(elements, format, overrides) {
            angular.extend(this, overrides);
            this.tooltip = d3.select($document.find("body")[0]).append("div").attr("class", this.tooltipCls).style("opacity", 0);
            this.$tooltip = angular.element(this.tooltip.node());
            this.format = format;
            elements.on('mouseover.tooltip', this.onMouseOver.bind(this))
                .on("mouseout.tooltip", this.onMouseOut.bind(this));
        }
        Tooltip.prototype.tooltipCls = "d3-tooltip";
        Tooltip.prototype.onMouseOver = function (d) {
            this.show(d, d3.event.pageX + 20, d3.event.pageY - 28 );
        };
        Tooltip.prototype.hide = function() {
            this.tooltip.style("opacity", 0);
        };
        Tooltip.prototype.onMouseOut = function () {
            this.hide();
        };
        Tooltip.prototype.show = function(data, x, y) {
            this.tooltip.style("opacity", 0.9);
            setContentAndCompile(this.tooltip, this.format, data);
            x = Math.min(x, $window.innerWidth-this.$tooltip.outerWidth());
            y = Math.min(y, $window.innerHeight-this.$tooltip.outerHeight());
            this.tooltip.style("left", x + "px")
                .style("top", y + "px");
        };
        Tooltip.prototype.destroy = function() {
            this.tooltip.remove();
        };
        return Tooltip;
    });
angular.module('allure.core.charts', ['allure.core.charts.timeline', 'allure.core.charts.pie', 'allure.core.charts.severityMap', 'allure.core.charts.duration']);



