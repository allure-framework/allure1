import {LayoutView} from 'backbone.marionette';
import template from './DefectsListView.hbs';

export default class DefectsListView extends LayoutView {
    template = template;
    serializeData() {
        return { defectTypes: this.collection.toJSON() };
    }
}
