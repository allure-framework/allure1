import './styles.css';
import {ItemView} from 'backbone.marionette';
import template from './ReportStatsWidget.hbs';
import ReportWidgetModel from '../../data/widgets/ReportWidgetModel';

export default class ReportStatsWidget extends ItemView {
    template = template;

    initialize() {
        this.model = new ReportWidgetModel();
        this.model.fetch().then(
            () => {
                if (!this.isDestroyed) {
                    this.render();
                }
            }
        );
    }

    serializeData() {
        return {
            report: this.model.toJSON()
        };
    }
}
