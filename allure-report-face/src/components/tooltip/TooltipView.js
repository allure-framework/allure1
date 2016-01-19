import './styles.css';
import {View} from 'backbone';
import bem from 'b_';
import $ from 'jquery';
import {defaults} from 'underscore';

export const POSITION = {
    'right': function({top, left, height, width}, {offset}, tipSize) {
        return {
            top: top + height / 2 - tipSize.height / 2,
            left: left + width + offset
        };
    }
};

export default class TooltipView extends View {
    static container = $(document.body);

    initialize(options) {
        this.options = options;
        defaults(this.options, {offset: 10});

    }

    render() {
        this.constructor.container.append(this.$el);
    }

    show(text, anchor) {
        const {position} = this.options;
        this.$el.text(text);
        this.$el.addClass(bem('tooltip', {position}));
        this.render();
        this.$el.css(POSITION[position](
            anchor[0].getBoundingClientRect(),
            {offset: this.options.offset},
            this.$el[0].getBoundingClientRect()
        ));
    }

    hide() {
        this.$el.remove();
    }
}
