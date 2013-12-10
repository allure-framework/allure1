angular.module('allure.pane', []).directive('paneSet', function() {
    return {
        controller: 'PanesController'
    }
}).directive('pane', function() {
    return {
        require: '^paneSet',
        link: function(scope, elem, attrs, PanesCtrl) {
            PanesCtrl.addPane(elem);
            attrs.$observe('paneExpanded', function(expanded, oldExpanded) {
                if(expanded !== oldExpanded) {
                    PanesCtrl.expandPane(elem, expanded);
                }
            });
            scope.$on('$destroy', function() {
                PanesCtrl.removePane(elem);
            })
        }
    };
}).controller('PanesController', function() {
    var panes = [];
    this.addPane = function(pane) {
        panes.push(pane);
        updatePositions();
    };
    this.removePane = function(pane) {
        panes.splice(panes.indexOf(pane), 1);
        updatePositions();
    };
    this.expandPane = function(pane, expanded) {
        var index = panes.indexOf(pane),
            offset = 5*index,
            width = (expanded ? 100 : 50) - offset;
        setPosition(pane, offset, width)
    };

    function setPosition(pane, left, width) {
        pane.css({left: left+'%', width: width+'%'})
    }

    function updatePositions() {
        var length = panes.length;
        if(length === 1) {
            setPosition(panes[0], 0, 100);
        }
        else {
            panes.forEach(function(pane, index) {
                if(index < length-1) {
                    var offset = 5*index;
                    setPosition(pane, offset, 50-offset)
                }
                else {
                    setPosition(pane, 50, 50)
                }
            });
        }
    }
});
