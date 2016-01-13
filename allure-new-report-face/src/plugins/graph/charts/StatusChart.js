import BaseChart from './BaseChart';
import d3 from 'd3';

export default class StatusChart extends BaseChart {

    initialize() {
        this.arc = d3.svg.arc().innerRadius(0);
        this.pie = d3.layout.pie().sort(null).value(d => d.value);
    }

    getChartData() {
        const stats = this.collection.reduce((stats, testcase) => {
            stats[testcase.get('status')]++;
            return stats;
        }, {
            FAILED: 0,
            BROKEN: 0,
            CANCELED: 0,
            PENDING: 0,
            PASSED: 0
        });
        return Object.keys(stats).map(key => ({
            name: key,
            value: stats[key],
            part: stats[key] / this.collection.length
        }));
    }

    onShow() {
        const data = this.getChartData();
        const width = this.$el.width();
        const radius = width / 4;
        this.$el.height(radius * 2);
        this.arc.outerRadius(radius);

        this.svg = this.setupViewport();

        var sectors = this.svg.select('.chart__plot')
            .attr({transform: `translate(${width/2},${radius})`})
            .selectAll('.chart__arc').data(this.pie(data))
            .enter()
            .append('path')
            .attr('class', d => 'chart__arc chart__fill_status_' + d.data.name);

        if(this.firstRender) {
             sectors.transition().duration(750).attrTween('d', d => {
                const radiusFn = d3.interpolate(10, radius);
                const startAngleFn = d3.interpolate(0, d.startAngle);
                const endAngleFn = d3.interpolate(0, d.endAngle);
                return t =>
                    this.arc.outerRadius(radiusFn(t))({startAngle: startAngleFn(t), endAngle: endAngleFn(t)});
            });
        } else {
            sectors.attr('d', d => this.arc(d));
        }
        super.onShow();
    }
}
