let stompClient;

function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    if (connected) {
        $("#conversation").show();
    } else {
        $("#conversation").hide();
    }
    $("#greetings").html("");
}

function connect() {
    stompClient = new StompJs.Client({
        brokerURL: 'ws://localhost:8080/v1/api/attendences/ws/' + $("#attendenceId").val(),
        onConnect: (frame) => {
            setConnected(true);
            console.log('Connected: ' + frame);
            stompClient.subscribe('/user/' + $("#courseId").val() + '/topic/' + $("#attendenceId").val(), (greeting) => {
                showGreeting(greeting.body);
            })
        },

        onWebSocketError: (error) => {
            console.error('Error with websocket', error);
        },

        onStompError: (frame) => {
            console.error('Broker reported error: ' + frame.headers['message']);
            console.error('Additional details: ' + frame.body);
        }
    });

    stompClient.activate();
}

function disconnect() {
    stompClient.deactivate();
    setConnected(false);
    console.log("Disconnected");
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
    $("#send").click(() => sendName());
});