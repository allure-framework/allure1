import './styles.css';
import {ItemView} from 'backbone.marionette';
import {Model} from 'backbone';
import {className} from '../../../decorators';
import template from './DescriptionView.hbs';

@className('description')
class DescriptionView extends ItemView {
    template = template;

    serializeData() {
        return this.model.get('description');
    }
}

export default DescriptionView;
