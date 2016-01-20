import './styles.css';
import {LayoutView} from 'backbone.marionette';
import {reduce} from 'underscore';
import StatusToggleView from '../../../components/status-toggle/StatusToggleView';
import {region, behavior} from '../../../decorators';
import {doSort} from '../../../util/sorting';
import settings from '../../../util/settings';
import template from './TestsuitesListView.hbs';

@behavior('SortBehavior')
class TestsuitesListView extends LayoutView {
    template = template;

    @region('.testsuites-list__statuses')
    statuses;

    initialize({state}) {
        this.state = state;
        this.listenTo(this.state, 'change:testsuite', this.render);
        this.listenTo(settings, 'change:visibleStatuses', this.render);
    }

    onRender() {
        this.statuses.show(new StatusToggleView());
    }

    getSettings() {
        return settings.get('xUnitSettings');
    }

    setSettings(sorting) {
        settings.set('xUnitSettings', sorting);
    }

    serializeData() {
        const statuses = settings.get('visibleStatuses');
        const sorting = this.getSettings();
        return {
            baseUrl: 'xUnit',
            currentSuite: this.state.get('testsuite'),
            sorting: sorting,
            time: this.collection.time,
            statistic: this.collection.statistic,
            suites: doSort(
                this.collection.toJSON().filter(suite => {
                    return reduce(
                        suite.statistic,
                        (visible, value, status) => visible || (statuses[status.toUpperCase()] && value > 0),
                        false
                    );
                }),
                sorting
            )
        };
    }
}

export default TestsuitesListView;
