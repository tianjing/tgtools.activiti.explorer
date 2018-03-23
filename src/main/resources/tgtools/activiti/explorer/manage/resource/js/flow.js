var grid = null;
$(function () {
    mini.parse();
    grid = mini.get("datagrid1");
    grid.setUrl("../flow/list");
    gridreload();
});

function gridreload() {
    grid.load();
};

function suspension() {
    var selected = grid.getSelected();
    var url = "../../../../activiti/explorer/manage/flow/suspension/" + selected.id;
    var res = tgtools.net.ajaxData("POST", url, null);
    gridreload();
    alert(res.msg);
};

function activate() {
    var selected = grid.getSelected();
    var url = "../../../../activiti/explorer/manage/flow/activate/" + selected.id;
    var res = tgtools.net.ajaxData("POST", url, null);
    gridreload();
    alert(res.msg);
};

function viewXml() {
    var selected = grid.getSelected();
    var url = "../../../../activiti/explorer/manage/flow/bpmn/xml?deployid="+selected.deploymentId;
    window.open(url);
};

function viewPng() {
    var selected = grid.getSelected();
    var url = "../../../../activiti/explorer/manage/flow/bpmn/png?deployid="+selected.deploymentId;
    window.open(url);
};