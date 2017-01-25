import './styles.css';
import DataGridView from '../data-grid/DataGridView';
import {region} from '../../decorators';
import settings from '../../util/settings';
import StatusToggleView from '../status-toggle/StatusToggleView';
import template from './TestcaseTableView.hbs';
import {countBy} from 'underscore';

class TestcaseTableView extends DataGridView {
    template = template;
    settingsKey = 'testCaseSorting';

    @region('.testcase-table__statuses')
    statuses;

    initialize() {
        this.listenTo(settings, 'change:visibleStatuses', this.render);
        this.testCases = this.options.testCases.map((testcase, index) => Object.assign(testcase, {index: index + 1}));
        this.statistics = countBy(this.testCases, testCase => testCase.status.toLowerCase());
    }

    onRender() {
        const statistics = this.statistics;
        this.highlightItem(this.options.currentCase);
        this.statuses.show(new StatusToggleView({statistics}));
    }

    serializeData() {
        const statuses = settings.get('visibleStatuses');
        const sorting = this.getSettings();
        return {
            baseUrl: this.options.baseUrl,
            sorting: sorting,
            totalCount: this.options.testCases.length,
            testCases: this.applySort(this.testCases).filter(({status}) => statuses[status])
        };
    }
}

export default TestcaseTableView;
