import {ItemView} from 'backbone.marionette';
import {className, on} from '../../decorators';
import bemRender from '../../util/bemRender';
import settings from '../../util/settings';

@className('status-toggle')
class StatusToggleView extends ItemView {
    template() {
        return '';
    }

    onRender() {
        const statuses = settings.get('visibleStatuses');
        bemRender(this.$el, {
            block: 'control-group',
            content: ['Failed', 'Broken', 'Canceled', 'Pending', 'Passed'].map(status => ({
                block: 'checkbox',
                name: status.toUpperCase(),
                text: status,
                mods: {
                    checked: statuses[status.toUpperCase()],
                    type: 'button',
                    status: status.toUpperCase()
                }
            }))
        });
    }

    @on('click .checkbox')
    onCheckChange(e) {
        const {name, checked} = e.currentTarget.querySelector('.checkbox__control');
        const statuses = settings.get('visibleStatuses');
        settings.save('visibleStatuses', Object.assign({}, statuses, {[name]: checked}));
    }
}

export default StatusToggleView;
