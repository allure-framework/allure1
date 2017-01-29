import './styles.css';
import {ItemView} from 'backbone.marionette';
import capitalize from 'underscore.string/capitalize';
import {on} from '../../decorators';
import settings from '../../util/settings';
import template from './StatusToggleView.hbs';

class StatusToggleView extends ItemView {
    template = template;

    initialize({statistics}) {
        this.statistics = statistics;
    }

    serializeData() {
        const statuses = settings.get('visibleStatuses');
        return {
            statistics: ['failed', 'broken', 'canceled', 'pending', 'passed'].map( status => ({
                status: status,
                active: !!statuses[status],
                title: capitalize(status.toString()),
                count: this.statistics[status]
            }))
        };
    }

    @on('click .button')
    onCheckChange(e) {
        const el = this.$(e.currentTarget);
        el.toggleClass('button_active');
        const name = el.data('status');
        const checked = el.hasClass('button_active');
        const statuses = settings.get('visibleStatuses');
        settings.save('visibleStatuses', Object.assign({}, statuses, {[name]: checked}));
    }
}

export default StatusToggleView;
