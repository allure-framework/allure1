import {ItemView} from 'backbone.marionette';
import $ from 'jquery';
import attachmentType from '../../util/attachmentType';
import template from './AttachmentView.hbs';

export default class AttachmentView extends ItemView {
    template = template;

    initialize({attachment}) {
        this.attachment = attachment;
        this.type = attachmentType(this.attachment.type);
        this.sourceUrl = 'data/' + this.attachment.source;
    }

    onRender() {
        if(this.needsFetch() && !this.content) {
            this.loadContent().then(this.render);
        }
    }

    loadContent() {
        return $.ajax(this.sourceUrl).then((responseText) => {
            this.content = responseText;
        });
    }

    needsFetch() {
        return ['text', 'code', 'csv'].indexOf(this.type) > -1;
    }

    serializeData() {
        return Object.assign({}, this.attachment, {
            type: this.type,
            content: this.content,
            sourceUrl: this.sourceUrl,
            route: {
                baseUrl: this.options.baseUrl
            }
        });
    }
}
