import './styles.css';
import {LayoutView} from 'backbone.marionette';
import {on, region} from '../../decorators';
import router from '../../router';
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

    @on('click .testcase-table__item')
    onTestcaseClick(e) {
        const testcase = this.$(e.currentTarget).data('uid');
        router.to(this.options.baseUrl + '/' + testcase);
    }

    onRender() {
        this.statuses.show(new StatusToggleView());
    }

    serializeData() {
        const statuses = settings.get('visibleStatuses');
        return {
            totalCount: this.options.testCases.length,
            currentCase: this.options.currentCase,
            testCases: this.options.testCases.filter(({status}) => statuses[status])
        };
    }
}
