import {Router, history} from 'backbone';
import urlLib from 'url';

class AppRouter extends Router {
    constructor() {
        super({
            routes: {
                '': 'home',
                'defects(/:defectId)(/:testcaseId)(/:attachmentId)': 'defects',
                '*default': 'notFound'
            }
        });
    }

    getCurrentUrl() {
        return history.getFragment();
    }

    to(pathname, query, options) {
        const url = urlLib.format({pathname, query});
        return this.toUrl(url, options);
    }

    toUrl(url, options) {
        return this.navigate(url, Object.assign({trigger: true}, options));
    }

    setSearch(search) {
        const {pathname} = urlLib.parse(this.getCurrentUrl());
        return this.to(pathname, search, {trigger: false});
    }

    getUrlParams() {
        const parsed = urlLib.parse(this.getCurrentUrl(), true);
        return parsed ? parsed.query : {};
    }
}

export default new AppRouter();
