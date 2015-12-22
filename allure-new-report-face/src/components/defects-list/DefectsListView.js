import './styles.css';
import {LayoutView} from 'backbone.marionette';
import {on} from '../../decorators';
import router from '../../router';
import template from './DefectsListView.hbs';

class DefectsListView extends LayoutView {
    template = template;

    @on('click .defects-list__item')
    onDefectClick(e) {
        const defectId = this.$(e.currentTarget).data('uid');
        router.to('defects/'+defectId);
    }

    serializeData() {
        const currentDefect = this.options.params.defect;
        return {
            defectTypes: this.collection.toJSON().map(type =>
                Object.assign({}, type, {
                    defects: type.defects.map(defect =>
                        Object.assign({}, defect, {active: defect.uid === currentDefect})
                    )
                })
            )
        };
    }
}

export default DefectsListView;
