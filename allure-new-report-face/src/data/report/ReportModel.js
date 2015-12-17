import {Model} from 'backbone';
import $ from 'jquery';

function randomKey() {
    return Math.random().toString(36).substr(2, 10);
}

export default class ReportPageModel extends Model {
    urlRoot = '/api/report';

    constructor() {
        super({});
    }

    static upload(projectName, name, file) {
        const data = new FormData();
        data.append('name', name || `report-${randomKey()}`);
        data.append('projectName', projectName);
        data.append('file', file);
        return $.ajax({
            type: 'POST',
            url: '/api/report',
            data: data,
            processData: false,
            contentType: false
        });
    }
}
