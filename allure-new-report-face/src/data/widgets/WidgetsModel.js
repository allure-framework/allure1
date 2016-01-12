import {Model} from 'backbone';

export default class WidgetsModel extends Model {
    url = 'data/widgets.json';

    getWidgetData(name) {
        return new Model(this.get('plugins')[name]);
    }
}
