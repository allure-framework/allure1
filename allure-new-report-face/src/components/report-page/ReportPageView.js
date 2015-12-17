import './styles.css';
import '../../blocks/well/styles.css';
import {LayoutView} from 'backbone.marionette';
import bemRender from '../../util/bemRender';
import router from '../../routes';
import {className, region, on} from '../../decorators';
import template from './ReportPageView.hbs';
import ProjectNavView from '../project-nav/ProjectNavView';
import ReportModel from '../../data/report/ReportModel';

@className('report-page')
class ReportPageView extends LayoutView {
    template = template;

    @region('.report-page__nav')
    nav;

    initialize() {
        this.model = new ReportModel();
    }

    onRender() {
        bemRender(this.$('.report-page__actions'), {
            block: 'button',
            text: 'Delete',
            mix: {block: 'report-page', elem: 'delete'},
            mods: {size: 'l', theme: 'islands', view: 'danger'}
        });
    }

    serializeData() {
        return Object.assign({cls: this.className}, super.serializeData());
    }

    @on('click .report-page__delete')
    onDeleteReportClick() {
        this.model.destroy().then(() => {
            router.to(this.options.projectName);
        });
    }
}

export default ReportPageView;
