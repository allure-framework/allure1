import {Model} from 'backbone';

export default class TestcaseModel extends Model {
    get idAttribute() {
        return 'uid';
    }

    url() {
        return `data/${this.id}-testcase.json`;
    }
}
