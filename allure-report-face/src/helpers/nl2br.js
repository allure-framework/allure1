import {SafeString} from 'handlebars/runtime';

export default function nl2br(text) {
     var nl2br = (text + '').replace(/([^>\r\n]?)(\r\n|\n\r|\r|\n)/g,
                                     '$1' + '<br />' + '$2');
     return new SafeString(nl2br);
}
