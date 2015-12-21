import './styles.css';
import {ItemView} from 'backbone.marionette';
import {className} from '../../decorators';
import template from './HeaderView.hbs';

@className('header')
class HeaderView extends ItemView {
    template = template;
}

export default HeaderView;
