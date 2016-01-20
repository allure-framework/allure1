import allurePlugins from '../../pluginApi';
import settings from '../../util/settings';
import XUnitLayout from './XUnitLayout';
import XUnitWidget from './XUnitWidget';

if(!settings.get('xUnitSettings')) {
    settings.set('xUnitSettings', {field: 'title', order: 'asc'});
}

allurePlugins.addTab('xUnit', {
    title: 'xUnit', icon: 'fa fa-briefcase',
    route: 'xUnit(/:defectId)(/:testcaseId)(/:attachmentId)',
    onEnter: (...routeParams) => new XUnitLayout({routeParams})
});
allurePlugins.addWidget('xunit', XUnitWidget);
