import allurePlugins from '../../pluginApi';

import OverviewWidget from '../../components/widget-overview/OverviewWidget';
import ReportStatsWidget from '../../components/widget-report-stats/ReportStatsWidget';
import OverviewLayout from '../../layouts/overview/OverivewLayout';

allurePlugins.addWidget('total', OverviewWidget);
allurePlugins.addWidget('report-stats', ReportStatsWidget);

allurePlugins.addTab('', {
    title: 'Overview', icon: 'fa fa-home',
    route: '',
    onEnter: () => new OverviewLayout()
});
