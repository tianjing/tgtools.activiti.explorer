var grid = null;
$(function () {
    mini.parse();
    grid = mini.get("datagrid1");
    grid.setUrl("../../../model/list");
    gridreload();
});


function gridreload() {
    grid.load();
};

function deploy() {
    var selected = grid.getSelected();
    var url = "../../../../activiti/model/deploy/" + selected.id;
    var res = tgtools.net.ajaxData("POST", url, null);
    alert(res.msg);
};

function add() {
    var url = "../../../../activiti/model/add";
    var id = tgtools.net.ajaxData("GET", url, null);
    gridreload();
    window.open("../../../../activiti/resource/modeler.html?modelId=" + id);
};

function edit() {
    var selected = grid.getSelected();
    window.open("../../../../activiti/resource/modeler.html?modelId=" + selected.id);
};

function remove() {
    var selected = grid.getSelected();
    var url = "../../../../activiti/model/remove/" + selected.id;
    var res = tgtools.net.ajaxData("POST", url, null);
    if (res.code == 0) {
        gridreload();
    } else {
        alert(res.msg);
    }
};