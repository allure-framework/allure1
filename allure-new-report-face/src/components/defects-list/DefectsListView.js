import {LayoutView} from 'backbone.marionette';
import {className} from '../../decorators';
import template from './DefectsListView.hbs';

@className('panes-container')
class DefectsListView extends LayoutView {
    template = template;
    serializeData() {
        return { defectTypes: this.collection.toJSON() };
    }
}

export default DefectsListView;
