const stompClient = new StompJs.Client({
    brokerURL: 'ws://localhost:9000/ws',
    debug: function (str) {
        console.log(str);
    },
    reconnectDelay: 5000,
    heartbeatIncoming: 4000,
    heartbeatOutgoing: 4000,
});

stompClient.onConnect = (frame) => {
    setConnected(true);
    console.log('Connected: ' + frame);
    stompClient.subscribe('/topic/dm/1', (message) => {
        showGreeting(JSON.parse(message.body).chat, 'dm');
    });
    stompClient.subscribe('/topic/group/1', (message) => {
        showGreeting(JSON.parse(message.body).chat, 'group');
    });
};

stompClient.onWebSocketError = (error) => {
    console.error('Error with websocket', error);
};

stompClient.onStompError = (frame) => {
    console.error('Broker reported error: ' + frame.headers['message']);
    console.error('Additional details: ' + frame.body);
};

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
    stompClient.activate();
}

function disconnect() {
    stompClient.deactivate();
    setConnected(false);
    console.log("Disconnected");
}

function sendDirectMessage() {
    const message = {
        channelId: 1,
        senderId: 1,
        chat: $("#directMessage").val()
    };
    stompClient.publish({
        destination: "/app/dm/1",
        body: JSON.stringify(message)
    });
}

function sendGroupMessage() {
    const message = {
        channelId: 1,
        senderId: 1,
        chat: $("#groupMessage").val()
    };
    stompClient.publish({
        destination: "/app/group/1",
        body: JSON.stringify(message)
    });
}

function showGreeting(message, type) {
    $("#greetings").append("<tr><td>" + type.toUpperCase() + ": " + message + "</td></tr>");
}

$(function () {
    $("form").on('submit', (e) => e.preventDefault());
    $("#connect").click(() => connect());
    $("#disconnect").click(() => disconnect());
    $("#sendDirectMessage").click(() => sendDirectMessage());
    $("#sendGroupMessage").click(() => sendGroupMessage());
});