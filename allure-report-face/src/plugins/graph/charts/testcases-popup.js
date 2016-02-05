export default function({testcases}) {
    const LIST_LIMIT = 10;
    const items = testcases.slice(0, LIST_LIMIT);
    const overLimit = testcases.length - items.length;
    return `<b>${value} ${severity.toLowerCase()} test cases ${status.toLowerCase()}</b><br>` +
        `<ul class="popover__list">` +
        items.map(testcase => escape`<li>${testcase.title}</li>`).join('') +
        `</ul>` +
        (overLimit ? `...and ${overLimit} more` : '');
}
