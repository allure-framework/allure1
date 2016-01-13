import BaseChart from './BaseChart';
import d3 from 'd3';

const PAD_LEFT = 30;
const PAD_TOP = 15;
const PAD_BOTTOM = 30;

const severities = ['BLOCKER', 'CRITICAL', 'NORMAL', 'MINOR', 'TRIVIAL'];
const statuses = ['FAILED', 'BROKEN', 'CANCELED', 'PENDING', 'PASSED'];

export default class SeverityChart extends BaseChart {

    initialize() {
        this.x = d3.scale.ordinal().domain(severities);
        this.y = d3.scale.sqrt();
        this.status = d3.scale.ordinal().domain(statuses);
    }

    getChartData() {
        return severities.map(severity =>
            statuses.map(status => {
                const testcases = this.collection.where({status, severity}).map(model => model.toJSON());
                return {
                    value: testcases.length,
                    testcases,
                    severity,
                    status
                };
            })
        );
    }

    onShow() {
        const data = this.getChartData();
        this.$el.height(this.$el.width() * 0.5);
        const width = this.$el.width() - PAD_LEFT;
        const height = this.$el.height() - PAD_BOTTOM - PAD_TOP;

        this.x.rangeRoundBands([0, width], 0.2);
        this.y.range([height, 0], 1);
        this.y.domain([0, d3.max(data, d => d3.max(d, d => d.value))]).nice();
        this.status.rangeRoundBands([0, this.x.rangeBand()]);

        this.svg = this.setupViewport();

        this.makeAxis(this.svg.select('.chart__axis_x'), {
            format: d => d.toLowerCase(),
            top: height + PAD_TOP,
            left: PAD_LEFT,
            orient: 'bottom',
            scale: this.x
        });
        this.makeAxis(this.svg.select('.chart__axis_y'), {
            left: PAD_LEFT,
            top: PAD_TOP,
            orient: 'left',
            scale: this.y
        });
        this.svg.select('.chart__plot').attr({transform: `translate(${PAD_LEFT},${PAD_TOP})`});

        var bars = this.svg.select('.chart__plot').selectAll('.chart__group')
            .data(data).enter()
            .append('g')
            .attr('transform', d => `translate(${this.x(d[0].severity)},0)`)
            .selectAll('.bar')
            .data(d => d).enter()
            .append('rect');

        bars.attr({
            x: d => this.status(d.status),
            y: height,
            height: 0,
            width: this.status.rangeBand(),
            'class': d => 'chart__bar chart__fill_status_' + d.status
        });

        if(this.firstRender) {
            bars = bars.transition().duration(500);
        }

        bars.attr({
            y: d => this.y(d.value),
            height: d => height - this.y(d.value)
        });
        super.onShow();
    }
}
