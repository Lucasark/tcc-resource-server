let ws;

function setConnected(connected) {
    console.log("CONECTADO")
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    if (connected) {
        $("#conversation").show();
    } else {
        $("#conversation").hide();
    }
    $("#greetings").html("");
}

function setCookie(cname, cvalue, exdays) {
    const d = new Date();
    d.setTime(d.getTime() + (exdays * 24 * 60 * 60 * 1000));
    let expires = "expires=" + d.toUTCString();
    document.cookie = cname + "=" + cvalue + ";" + expires + ";path=/";
}

function connect() {

    ws = new WebSocket("ws://localhost:8080/v1/api/attendences/ws/courses/" + $("#courseId").val() + "/dates/" + $("#dates").val());

    ws.onopen = function (e) {
        console.log("OPEN: ", e)
        setConnected(true);
    };

    ws.onmessage = function (e) {
        showGreeting(e.data)
    };

    ws.onclose = function (e) {
        console.log("CLOSE: " + e.reason)
        disconnect();
    };

    ws.onerror = function (e) {
        console.log("ERROR")
    }
}

function disconnect() {
    ws.close();
    setConnected(false);
    console.log("Disconnected");

    $("#code").html("");
    $("#qrcode").html("")

}

function showGreeting(message) {
    $("#code").html("<h1>" + message + "</h1>");
    $("#qrcode").html("")
    new QRCode(document.getElementById("qrcode"), message);
}

$(function () {
    $("form").on('submit', (e) => e.preventDefault());
    $("#connect").click(() => connect());
    $("#disconnect").click(() => disconnect());
    $("#send").click(() => setCookie("token", $("#token").val(), 10));
});