<%@ page import="meta1.classes.User" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="meta1.classes.Eleicao" %>
<%@ page import="meta1.classes.ListaCandidata" %>
<%@ page import="java.util.Date" %>
<%@ page import="java.time.Duration" %>
<%@ page import="java.util.Calendar" %>
<%@ page import="java.util.GregorianCalendar" %>
<%@ page import="meta1.MulticastLibrary" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="date" uri="/struts-tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Estado da Eleição</title>
    <link rel="icon" type="image/png" href="resources/icon.png">
    <link rel="stylesheet" type="text/css" href="css/tempoReal.css">
    <script type="text/javascript">

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

    </script>
</head>
<body>
<noscript>JavaScript must be enabled for WebSockets to work.</noscript>
<div class="imgcontainer">
    <img src="resources/logo.png" alt="Avatar" class="avatar">
</div>
<div>
    <h2>Dados das Eleições em Tempo Real</h2>
    <br><br>
    <div id="container"><div id="history"></div></div>
    <br>
    <form action="cancelar">
        <button type="submit" >Cancelar</button>
    </form>
    <!--<p><input type="text" placeholder="type to chat" id="chat"></p>-->
</div>
</body>
</html>