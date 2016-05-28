export default function splitUriList(uri) {
    var os = require('os');
    return uri.split(os.EOL).filter(function (el) {
        return el.trim().length > 0 && !el.trim().startsWith('#');
    });
}
