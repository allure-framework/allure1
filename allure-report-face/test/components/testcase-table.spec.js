import TestcaseTableView from 'components/testcase-table/TestcaseTableView';
import settings from 'util/settings';

describe('TestcaseTable', function() {
    function TableElement(el) {
        this.rows = () => el.find('.table__row .table__col:nth-child(2)').toArray().map(node => node.textContent);
        this.column = (sortField) => el.find(`.table__col[data-sort="${sortField}"]`);
    }

    beforeEach(function() {
        settings.set('visibleStatuses', {PASSED: true, FAILED: true});
        this.view = new TestcaseTableView({
            testCases: [
                {uid: 1, name: 'case 1', time: {duration: 432}, status: 'PASSED'},
                {uid: 2, name: 'case 2', time: {duration: 145}, status: 'PASSED'},
                {uid: 3, name: 'case 3', time: {duration: 370}, status: 'FAILED'}
            ]
        }).render();
        this.el = new TableElement(this.view.$el);
    });

    it('should render test cases list', function() {
        expect(this.el.rows()).toEqual([
            'case 1',
            'case 2',
            'case 3'
        ]);
    });

    it('should filter test cases by status', function() {
        settings.set('visibleStatuses', {PASSED: true});
        expect(this.el.rows()).toEqual([
            'case 1',
            'case 2'
        ]);
    });

    it('should change test case sorting by click', function() {
        this.el.column('time.duration').click();
        expect(this.el.rows()).toEqual([
            'case 2',
            'case 3',
            'case 1'
        ]);
        this.el.column('time.duration').click();
        expect(this.el.rows()).toEqual([
            'case 1',
            'case 3',
            'case 2'
        ]);
    });
});
