import './styles.css';
import { LayoutView } from 'backbone.marionette';
import { region, className } from '../../decorators';
import template from './AppLayout.hbs';
import HeaderView from '../../components/header/HeaderView';
import SideNav from '../../components/side-nav/SideNavView';

@className('app')
class AppLayout extends LayoutView {
    template = template;

    @region('.app__header')
    header;
    @region('.app__content')
    content;
    @region('.app__nav')
    nav;

    onRender() {
        this.header.show(new HeaderView());
        const dataPromise = this.loadData();
        if(dataPromise) {
            dataPromise.then(() => {
                this.content.show(this.getContentView());
            });
        } else {
            this.content.show(this.getContentView());
        }
        this.nav.show(new SideNav());
    }

    loadData() {}

    getContentView() {
        throw new Error('attempt to call abstract method');
    }
}

export default AppLayout;
