import './styles.css';
import {LayoutView} from 'backbone.marionette';
import {region, on} from '../../decorators';
import settings from '../../util/settings';
import {doSort, updateSort} from '../../util/sorting';
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

    @on('click [data-sort]')
    onSortClick(e) {
        const el = this.$(e.currentTarget);
        const sorting = settings.get('testCaseSorting');
        settings.save('testCaseSorting', updateSort(el.data('sort'), sorting));
        this.render();
    }

    serializeData() {
        const statuses = settings.get('visibleStatuses');
        const sorting = settings.get('testCaseSorting');
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
