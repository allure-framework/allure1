import TestcaseModel from '../data/testcase/TestcaseModel';
import TestcaseView from '../components/testcase-view/TestcaseView';
import AttachmentView from '../components/attachment/AttachmentView';

export default class TestcasePanes {
    constructor(state, paneView) {
        this.state = state;
        this.paneView = paneView;
        this.testcase = new TestcaseModel();
    }

    updateState(baseUrl, changed) {
        if(changed.hasOwnProperty('testcase')) {
            if(!changed.testcase) {
                this.paneView.removePane('testcase');
                this.testcase.clear();
            } else if(this.testcase.id !== changed.testcase) {
                this.testcase.set({uid: changed.testcase});
                this.testcase.fetch().then(() => this.updateState(baseUrl, changed));
                return;
            } else {
                this.paneView.addPane('testcase', new TestcaseView({
                    baseUrl,
                    state: this.state,
                    model: this.testcase
                }));
            }
        }
        if(changed.hasOwnProperty('attachment')) {
            if(!changed.attachment) {
                this.paneView.removePane('attachment');
            } else {
                this.paneView.addPane('attachment', new AttachmentView({
                    baseUrl: baseUrl + '/' + this.state.get('testcase'),
                    attachment: this.testcase.getAttachment(changed.attachment),
                    state: this.state
                }));
            }
        }
    }

}
