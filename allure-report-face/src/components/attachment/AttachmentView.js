import './styles.css';
import highlight from '../../util/highlight';
import {ItemView} from 'backbone.marionette';
import $ from 'jquery';
import router from '../../router';
import {className, on, behavior} from '../../decorators';
import attachmentType from '../../util/attachmentType';
import template from './AttachmentView.hbs';

@className('attachment')
@behavior('TooltipBehavior', {position: 'bottom'})
class AttachmentView extends ItemView {
    template = template;

    initialize({attachment}) {
        this.attachment = attachment;
        this.attachmentHelper = attachmentType(this.attachment.type);
        this.sourceUrl = 'data/' + this.attachment.source;
    }

    onRender() {
        if(this.needsFetch() && !this.content) {
            this.loadContent().then(this.render);
        } else if(this.attachmentHelper.type === 'code') {
            const codeBlock = this.$('.attachment__code');
            codeBlock.addClass('language-' + this.attachment.type.split('/').pop());
            highlight.highlightBlock(codeBlock[0]);
        }
    }

    @on('click .attachment__media')
    onImageClick() {
        const expanded = router.getUrlParams().expanded === 'true' ? null : true;
        router.setSearch({expanded});
    }

    loadContent() {
        return $.ajax(this.sourceUrl, {dataType: 'text'}).then((responseText) => {
            var parser = this.attachmentHelper.parser;
            this.content = parser(responseText);
        });
    }

    needsFetch() {
        return 'parser' in this.attachmentHelper;
    }

    serializeData() {
        return {
            type: this.attachmentHelper.type,
            content: this.content,
            sourceUrl: this.sourceUrl,
            attachment: this.attachment,
            route: {
                baseUrl: this.options.baseUrl
            }
        };
    }
}

export default AttachmentView;
