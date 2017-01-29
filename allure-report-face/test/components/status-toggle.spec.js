import settings from 'util/settings.js';
import StatusToggleView from 'components/status-toggle/StatusToggleView';

describe('StatusToggle', function () {
    const statistics = {failed: 1, broken: 1, canceled: 1, pending: 1, passed: 1};

    beforeEach(function () {
        settings.set('visibleStatuses', {failed: true, broken: true});
        this.view = new StatusToggleView({statistics}).render();
        this.el = this.view.$el;
    });

    it('should render buttons according to settings', function () {
        expect([...this.el.find('.button')].map(button => button.textContent))
            .toEqual(['Failed (1)', 'Broken (1)', 'Canceled (1)', 'Pending (1)', 'Passed (1)']);
        expect([...this.el.find('.button_active')].map(button => button.textContent))
            .toEqual(['Failed (1)', 'Broken (1)']);
    });

    it('should update model on click', function () {
        const passed = this.el.find('.status-toggle__button_status_passed');
        passed.click();
        expect(settings.get('visibleStatuses')).toEqual({failed: true, broken: true, passed: true});

        passed.click();
        expect(settings.get('visibleStatuses')).toEqual({failed: true, broken: true, passed: false});
    });
});
