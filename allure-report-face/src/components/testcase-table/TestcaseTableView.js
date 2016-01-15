import './styles.css';
import {LayoutView} from 'backbone.marionette';
import {region} from '../../decorators';
import settings from '../../util/settings';
import StatusToggleView from '../status-toggle/StatusToggleView';
import template from './TestcaseTableView.hbs';

export default class TestcaseTableView extends LayoutView {
    template = template;

    @region('.testcase-table__statuses')
    statuses;

    initialize() {
        this.listenTo(settings, 'change:visibleStatuses', this.render);
    }

    onRender() {
        this.statuses.show(new StatusToggleView());
    }

    serializeData() {
        const statuses = settings.get('visibleStatuses');
        return {
            baseUrl: this.options.baseUrl,
            totalCount: this.options.testCases.length,
            currentCase: this.options.currentCase,
            testCases: this.options.testCases.filter(({status}) => statuses[status])
        };
    }
}
