import typeByMime from '../util/attachmentType';

export default function(type) {
    switch (typeByMime(type)) {
        case 'text':
            return 'fa fa-file-text-o';
        case 'image':
        case 'svg':
            return 'fa fa-file-image-o';
        case 'code':
            return 'fa fa-file-code-o';
        case 'csv':
        case 'tab-separated-values':
            return 'fa fa-table';
        default:
            return 'fa fa-file-o';
    }
}
