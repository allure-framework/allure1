import WidgetsModel from './WidgetsModel';

export default class ReportWidgetModel extends WidgetsModel {
    url = 'data/report.json';

    fetch(...args) {
        if (!this.fetchPromise) {
            this.fetchPromise = super.fetch(...args);
        }
        return new Promise((res, rej) => this.fetchPromise.then(res, rej));
    }
}
