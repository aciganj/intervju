function updateFollowers(data){
    console.log(data)
    if(data!=null){
        $("#followers").empty();
        for (i = 0; i < data.length; i++) {
            $("#followers").append("<tr><td>" + data[i].name + "</td></tr>");
        }
    }
}

function monitor() {
    $.ajax({
        url: "http://localhost:8080/monitor/" + $("#name").val(),
        success: updateFollowers
    });
}

function database() {
    $.ajax({
        url: "http://localhost:8080/database",
        success: updateFollowers
    });
}



$(function () {
    $("form").on('submit', function (e) {
        e.preventDefault();
    });
    $( "#monitor" ).click(function() { monitor(); });
    $( "#database" ).click(function() { database(); });
});
