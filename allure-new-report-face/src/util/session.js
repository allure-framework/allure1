import $ from 'jquery';
const session = {};

export function loadSession() {
    return new Promise((resolve) => {
        $.getJSON('/api/session').then((response) => {
            Object.assign(session, response);
        }, () => {
            session.username = 'Guest';
            session.guest = true;
        }).always(() => {
            resolve(session);
        });
    });
}

export default session;
