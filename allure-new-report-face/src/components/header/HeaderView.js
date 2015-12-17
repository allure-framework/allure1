import './styles.css';
import {ItemView} from 'backbone.marionette';
import {className, on} from '../../decorators';
import bemRender from '../../util/bemRender';
import router from '../../routes';
import session from '../../util/session';
import template from './HeaderView.hbs';

@className('header')
class HeaderView extends ItemView {
    template = template;
    links = [
        {href: '', name: 'Home'},
        {href: 'search', name: 'Search'}
    ];

    onRender() {
        bemRender(this.$('.header__search'), {
            block: 'input',
            placeholder: 'Find report...',
            mods: {
                type: 'search',
                'has-clear': true,
                theme: 'islands',
                size: 'm'
            }
        });
    }

    @on('submit .header__search')
    onSearch(e) {
        e.preventDefault();
        const query = this.$('.header__search input').val();
        router.to('search', {query});
    }

    isLinkActive(href) {
        var currentUrl = router.getCurrentUrl();
        return href ? currentUrl.indexOf(href) === 0 : currentUrl === href;
    }

    serializeData() {
        return {
            showSearch: !this.isLinkActive('search'),
            user: session.name,
            links: this.links.map((link) => {
                return Object.assign(link, {active: this.isLinkActive(link.href)});
            })
        };
    }
}

export default HeaderView;
