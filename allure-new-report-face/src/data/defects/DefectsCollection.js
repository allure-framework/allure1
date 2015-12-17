import {Collection} from 'backbone';

export default class DefectsCollection extends Collection {
    url = '/data/defects.json';

    parse({defectsList}) {
        return defectsList;
    }
}
