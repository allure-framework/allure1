import './styles.css';
import {ItemView} from 'backbone.marionette';
import {className} from '../../decorators';
import template from './LinksView.hbs';

@className('pane__section')
class IssuesView extends ItemView {
    template = template;

    serializeData() {
        const {testId, issues, stories} = this.model.attributes;
        return {
            hasLinks: testId || issues.length > 0 || stories.length > 0,
            testId, issues, stories
        };
    }
}

export default IssuesView;
