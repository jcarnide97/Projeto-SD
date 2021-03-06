var websocket = null;

window.onload = function() { // URI = ws://10.16.0.165:8080/web/ws

    connect('ws://'+window.location.host+'/web/ws');

    //document.getElementById("chat").focus();
}

function connect(host) { // connect to the host websocket
    if ('WebSocket' in window)
        websocket = new WebSocket(host);
    else if ('MozWebSocket' in window)
        websocket = new MozWebSocket(host);
    else {
        writeToHistory('Get a real browser which supports WebSocket.');
        return;
    }

    //websocket.onopen    = onOpen; // set the 4 event listeners below
    websocket.onclose   = onClose;
    websocket.onmessage = onMessage;
    websocket.onerror   = onError;
}

/*
function onOpen(event) {
    writeToHistory('Connected to ' + window.location.host + '.');
    document.getElementById('chat').onkeydown = function(key) {
        if (key.keyCode == 13)
            doSend(); // call doSend() on enter key press
    };
}
*/

function onClose(event) {
    writeToHistory('WebSocket closed (code ' + event.code + ').');
    //document.getElementById('chat').onkeydown = null;
}

function onMessage(message) { // print the received message
    writeToHistory(message.data);
}

function onError(event) {
    writeToHistory('WebSocket error.');
    //document.getElementById('chat').onkeydown = null;
}

function doSend() {
    var message = document.getElementById('chat').value;
    if (message !== '')
        websocket.send(message); // send the message to the server
    //document.getElementById('chat').value = '';
}

function writeToHistory(text) {
    var history = document.getElementById('history');
    var line = document.createElement('p');
    line.style.wordWrap = 'break-word';
    line.innerHTML = text;
    history.appendChild(line);
    history.scrollTop = history.scrollHeight;
}