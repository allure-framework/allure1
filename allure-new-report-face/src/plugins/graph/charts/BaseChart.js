import {View} from 'backbone';
import d3 from 'd3';

export default class BaseChart extends View {

    constructor(options) {
        super(options);
        this.firstRender = true;
    }

    onRender() {
        d3.select(window).on('resize.' + this.cid, this.onShow.bind(this));
    }

    onDestroy() {
        d3.select(window).on('resize.' + this.cid, null);
    }

    onShow() {
        this.firstRender = false;
    }

    setupViewport() {
        this.$el.html(`<svg class="chart__svg">
            <g class="chart__axis chart__axis_x"></g>
            <g class="chart__axis chart__axis_y"></g>
            <g class="chart__plot"></g>
        </svg>`);
        return d3.select(this.$el[0]).select('svg');
    }

    makeAxis(element, {scale, orient, format, left, top} = {}) {
        return element.call(
            d3.svg.axis()
                .scale(scale)
                .orient(orient)
                .tickFormat(format)
        ).attr({
            transform: `translate(${left},${top})`
        });
    }
}
