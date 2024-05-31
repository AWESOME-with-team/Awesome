let stompClient = null;
let chatId = "1"; // Assuming we are using chatId 1 for the test

function connect() {
    const socket = new SockJS('http://localhost:9000/ws');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/group/' + chatId, function (message) {
            showMessage(JSON.parse(message.body));
        });
    });
}

function sendMessage() {
    const nick = document.getElementById('nick').value;
    const message = document.getElementById('message').value;
    const chatMessage = {
        sender: nick,
        content: message,
        chatId: chatId
    };
    stompClient.send("/app/group/{groupId}/sendMessage", {}, JSON.stringify(chatMessage));
}

function showMessage(message) {
    const messagesDiv = document.getElementById('messages');
    const messageDiv = document.createElement('div');
    messageDiv.textContent = message.sender + ": " + message.content;
    messagesDiv.appendChild(messageDiv);
}

window.onload = function() {
    connect();
};