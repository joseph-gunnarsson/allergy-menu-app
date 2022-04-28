





function get(url){
var csrftoken = getCookie('csrftoken');
$.ajax({
    type:"GET", 
    url: url,
    headers:{
        "X-CSRFToken": csrftoken
    }, 
    success: function(data) {
             ;
        }, 
    error: function(jqXHR, textStatus, errorThrown) {
            alert("sa");
        },
   dataType: "text"})
};

function post (url,data){
var csrftoken = getCookie('csrftoken');
$.ajax({
    type: "POST",
    url: url,
    headers:{
        "X-CSRFToken": csrftoken
    }, 
    // The key needs to match your method's input parameter (case-sensitive).
    data: data,
    contentType: "application/json; charset=utf-8",
    dataType: "text",
    async: false,
    success: function(data){return JSON.parse(data.replace(/'/g, '"')).message;},
    failure: function(errMsg) {
        alert(errMsg);
    }
});
};


function getCookie(name) {
    var cookieValue = null;
    if (document.cookie && document.cookie !== '') {
        var cookies = document.cookie.split(';');
        for (var i = 0; i < cookies.length; i++) {
            var cookie = cookies[i].trim();
            // Does this cookie string begin with the name we want?
            if (cookie.substring(0, name.length + 1) === (name + '=')) {
                cookieValue = decodeURIComponent(cookie.substring(name.length + 1));
                break;
            }
        }
    }
    return cookieValue;
}

function removeItemOnce(arr, value) { 
    var index = arr.indexOf(value);
    if (index > -1) {
        arr.splice(index, 1);
    }
    return arr;
}
