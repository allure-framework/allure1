import {Collection} from 'backbone';

export default class XUnitCollection extends Collection {
    url = 'data/xunit.json';

    parse({time, testSuites}) {
        this.time = time;
        this.statistic = testSuites.reduce((statistic, testsuite) => {
            ['PASSED', 'PENDING', 'CANCELED', 'BROKEN', 'FAILED', 'TOTAL'].forEach(function(status) {
                if(!statistic[status]) {
                    statistic[status] = 0;
                }
                statistic[status] += testsuite.statistic[status.toLowerCase()];
            });
            return statistic;
        }, {});
        return testSuites;
    }
}
