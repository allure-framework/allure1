import './styles.css';
import {ItemView} from 'backbone.marionette';
import {on, className} from '../../../decorators';
import template from './BehaviorsTreeView.hbs';

@className('behaviors-list')
class BehaviorsTreeView extends ItemView {
    template = template;

    initialize({state}) {
        this.listenTo(state, 'change:behavior', this.highlightBehavior, this);
    }

    onRender() {
        const {state} = this.options;
        this.highlightBehavior(state, state.get('behavior'));
    }

    highlightBehavior(state, story) {
        this.$el.find('.behaviors-list__story').removeClass('table__row_active');
        if(story) {
            const activeStory = this.$el.find('.behaviors-list__story[data-uid="' + story + '"]');
            activeStory.addClass('table__row_active');
            activeStory.parent().find(`.${this.className}__feature-row`)
                .addClass(`${this.className}__feature-row_expanded`);
        }
    }

    @on('click .behaviors-list__feature-row')
    onFeatureClick(e) {
        this.$(e.currentTarget).toggleClass('behaviors-list__feature-row_expanded');
    }
}

export default BehaviorsTreeView;
