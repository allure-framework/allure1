import AppLayout from '../application/AppLayout';
import WidgetsGridView from '../../components/widgets-grid/WidgetsGridView';

export default class OverivewLayout extends AppLayout {

    getContentView() {
        return new WidgetsGridView();
    }
}
