import './styles.css';
import {ItemView} from 'backbone.marionette';
import template from './OverviewWidget.hbs';
import c3 from 'c3';

export default class OverviewWidget extends ItemView {
    template = template;

    initialize() {
        setTimeout(() => {
            if (!this.isDestroyed) {
                this.render();
            }
        }, 200);
    }

    onDestroy() {
        this.chart = this.chart.destroy();
    }

    onRender() {
        const statuses = {
            failed: '#fd5a3e',
            broken: '#ffd963',
            canceled: '#ccc',
            pending: '#d35ebe',
            passed: '#97cc64'
        };

        let fill = [];
        let colors = [];
        for (let status in statuses) {
            fill.push([status, this.model.get('statistic')[status]]);
            colors.push(statuses[status]);
        }

        this.chart = c3.generate({
            bindto: '.overview-widget-graph',
            color: {
                pattern: colors
            },
            size: {
                height: 150,
                width: 150
            },
            padding: {
                left: 30,
                right: 15,
                top: 0,
                bottom: 0
            },
            data: {
                type: 'donut',
                columns: fill
            },
            legend: {
                show: false
            },
            donut: {
                width: 10,
                title: `${(this.model.get('statistic')['passed'] / this.model.get('statistic')['total'] * 100).toFixed(1)}%`,
                label: {
                    threshold: 0.0,
                    show: false
                }
            },
            tooltip: {
                grouped: false,
                format: {
                    value: function (value, ratio) {
                        return `${value} (${(ratio * 100).toFixed(1)}%)`;
                    }
                }
            }
        });
    }
}
