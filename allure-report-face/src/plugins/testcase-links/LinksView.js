import './styles.css';
import {ItemView} from 'backbone.marionette';
import {className} from '../../decorators';
import template from './LinksView.hbs';

@className('pane__section')
class IssuesView extends ItemView {
    template = template;

    serializeData() {
        const {testIds, issues} = this.model.attributes;
        return {
            hasLinks: testIds.length > 0 || issues.length > 0,
            testIds, issues
        };
    }
}

export default IssuesView;
