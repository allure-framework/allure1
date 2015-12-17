import {InputReaders} from 'backbone.syphon';

InputReaders.register('file', function($el) {
    const files = $el.prop('files');
    return ($el.attr('multiple')) ? files : files[0];
});
