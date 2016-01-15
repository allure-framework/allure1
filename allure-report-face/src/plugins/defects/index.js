import allurePlugins from '../../pluginApi';
import DefectsLayout from './DefectsLayout';
import DefectsWidget from './defects-widget/DefectsWidget';

allurePlugins.addTab('defects', {
    title: 'Defects', icon: 'fa fa-flag',
    route: 'defects(/:defectId)(/:testcaseId)(/:attachmentId)',
    onEnter: (...routeParams) => new DefectsLayout({routeParams})
});
allurePlugins.addWidget('defects', DefectsWidget);
