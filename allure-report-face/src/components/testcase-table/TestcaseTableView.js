import './styles.css';
import {LayoutView} from 'backbone.marionette';
import {region, behavior} from '../../decorators';
import settings from '../../util/settings';
import {doSort} from '../../util/sorting';
import StatusToggleView from '../status-toggle/StatusToggleView';
import template from './TestcaseTableView.hbs';

@behavior('SortBehavior')
class TestcaseTableView extends LayoutView {
    template = template;

    @region('.testcase-table__statuses')
    statuses;

    initialize() {
        this.listenTo(settings, 'change:visibleStatuses', this.render);
    }

    onRender() {
        this.statuses.show(new StatusToggleView());
    }

    getSettings() {
        return settings.get('testCaseSorting');
    }

    setSettings(sorting) {
        settings.save('testCaseSorting', sorting);
    }

    serializeData() {
        const statuses = settings.get('visibleStatuses');
        const sorting = this.getSettings();
        return {
            baseUrl: this.options.baseUrl,
            sorting: sorting,
            totalCount: this.options.testCases.length,
            currentCase: this.options.currentCase,
            testCases: doSort(
                this.options.testCases.map((testcase, index) => Object.assign(testcase, {index: index + 1})),
                sorting
            ).filter(({status}) => statuses[status])
        };
    }
}

export default TestcaseTableView;
