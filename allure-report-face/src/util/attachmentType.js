import d3 from 'd3';

export default function typeByMime(type) {
    switch (type) {
        case 'image/bmp':
        case 'image/gif':
        case 'image/tiff':
        case 'image/jpeg':
        case 'image/jpg':
        case 'image/png':
        case 'image/*':
            return {
                type: 'image'
            };
        case 'text/xml':
        case 'application/xml':
        case 'application/json':
        case 'text/json':
        case 'text/yaml':
        case 'application/yaml':
        case 'application/x-yaml':
        case 'text/x-yaml':
            return {
                type: 'code',
                parser: d => d
            };
        case 'text/plain':
        case 'text/*':
            return {
                type: 'text',
                parser: d => d
            };
        case 'text/html':
            return {
                type: 'html'
            };
        case 'text/csv':
            return {
                type: 'table',
                parser: d => d3.csv.parseRows(d)
            };
        case 'text/tab-separated-values':
            return {
                type: 'table',
                parser: d => d3.tsv.parseRows(d)
            };
        case 'image/svg+xml':
            return {
                type: 'svg'
            };
        case 'video/mp4':
        case 'video/ogg':
        case 'video/webm':
            return {
                type: 'video'
            };
        case 'text/uri-list':
            return {
                type: 'uri',
                parser: d => d.split('\n')
                                 .map(line => line.trim())
                                 .filter(line => line.length > 0)
                                 .map(line => ({
                                      comment: line.startsWith('#'),
                                      text: line
                                 }))
            };
        default:
    }
}
