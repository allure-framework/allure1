import './styles.css';
import {LayoutView} from 'backbone.marionette';
import bemRender from '../../util/bemRender';
import {on, region} from '../../decorators';
import allurePlugins from '../../pluginApi';
import StepsView from '../steps/StepsView';
import template from './TestcaseView.hbs';

class TestcaseView extends LayoutView {
    template = template;

    @region('.testcase__steps')
    steps;

    initialize({state}) {
        this.state = state;
        this.plugins = [];
        this.listenTo(this.state, 'change:attachment', this.highlightSelectedAttachment, this);
    }

    onRender() {
        bemRender(this.$('.testcase__trace-toggle'), {
            block: 'button',
            mods: {view: 'pseudo', inverse: true},
            text: 'Show trace'
        });
        this.showTestcasePlugins(this.$('.testcase__content_before'), allurePlugins.testcaseBlocks.before);
        this.steps.show(new StepsView({
            baseUrl: this.options.baseUrl + '/' + this.model.id,
            model: this.model
        }));
        this.highlightSelectedAttachment();
        this.showTestcasePlugins(this.$('.testcase__content_after'), allurePlugins.testcaseBlocks.after);
    }

    onDestroy() {
        this.plugins.forEach(plugin => plugin.destroy());
    }

    highlightSelectedAttachment() {
        const currentAttachment = this.state.get('attachment');
        this.$('.attachment-row').removeClass('attachment-row_selected');
        this.$(`.attachment-row[data-uid="${currentAttachment}"]`).addClass('attachment-row_selected');
    }

    showTestcasePlugins(container, plugins) {
        plugins.forEach((Plugin) => {
            const plugin = new Plugin({model: this.model});
            plugin.$el.appendTo(container);
            this.plugins.push(plugin);
            plugin.render();
        });
    }

    serializeData() {
        return Object.assign({
            route: {
                baseUrl: this.options.baseUrl
            }
        }, super.serializeData());
    }

    @on('click .testcase__trace-toggle')
    onStacktraceClick() {
        this.$('.testcase__trace').toggleClass('testcase__trace_visible');
    }
}

export default TestcaseView;
