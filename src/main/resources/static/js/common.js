
function get(url) {
    var result = {};
    $.ajax({
        type: "GET",
        url: url,
        dataType: "json",
        async: false,
        success: function (data) {
            result = data;
        }
    });
    return result;
}
function post(url, data) {
    var result = {};
    $.ajax({
        type: "POST",
        url: url,
        data: data,
        dataType: "json",
        async: false,
        success: function (data) {
            result = data;
        }
    });
    return result;
}
function postJSON(url, data) {
    var result = {};
    $.ajax({
        type: "POST",
        url: url,
        contentType: "application/json; charset=utf-8",
        data: JSON.stringify(data),
        dataType: "json",
        async: false,
        success: function (data) {
            result = data;
        }
    });
    return result;
}