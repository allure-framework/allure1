import settings from 'util/settings.js';
import StatusToggleView from 'components/status-toggle/StatusToggleView';

describe('StatusToggle', function () {
    const statistics = {FAILED: 1, BROKEN: 1, CANCELED: 1, PENDING: 1, PASSED: 1};

    beforeEach(function () {
        settings.set('visibleStatuses', {FAILED: true, BROKEN: true});
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
        const passed = this.el.find('.status-toggle__button_status_PASSED');
        passed.click();
        expect(settings.get('visibleStatuses')).toEqual({FAILED: true, BROKEN: true, PASSED: true});

        passed.click();
        expect(settings.get('visibleStatuses')).toEqual({FAILED: true, BROKEN: true, PASSED: false});
    });
});
