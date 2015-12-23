import {ItemView} from 'backbone.marionette';
import template from './AttachmentView.hbs';

export default class AttachmentView extends ItemView {
    template = template;

    serializeData() {
        return Object.assign({
            route: {
                baseUrl: this.options.baseUrl
            }
        }, this.options.attachment);
    }
}
