<%@ page import="meta1.classes.User" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="meta1.classes.Eleicao" %>
<%@ page import="meta1.classes.ListaCandidata" %>
<%@ page import="java.util.Date" %>
<%@ page import="java.time.Duration" %>
<%@ page import="java.util.Calendar" %>
<%@ page import="java.util.GregorianCalendar" %>
<%@ page import="java.net.URL" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="date" uri="/struts-tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title>Votar</title>
    <link rel="icon" type="image/png" href="resources/icon.png">
</head>
<body>
<div class="imgcontainer">
    <img src="resources/logo.png" alt="Avatar" class="avatar">
</div>
<div class="container">
    <script type="text/javascript">
        var websocket = null;
        connect('ws://'+window.location.host+'/web/ws');
        window.onload = function() {
            <%
                String nome=((User)session.getAttribute("utilizador")).getNome();
                ArrayList<Eleicao> eleicoes = ((ArrayList<Eleicao>)session.getAttribute("eleicoes"));
                Boolean elei = false;
                String nomeEleicao = "";
                int votos = 0;
                if((ArrayList<ListaCandidata>)session.getAttribute("listas")!=null){
                    elei = true;
                    ArrayList<ListaCandidata> listas = (ArrayList<ListaCandidata>)session.getAttribute("listas");
                    nomeEleicao = ((Eleicao)session.getAttribute("eleicao")).getTitulo();
                    votos = ((Eleicao)session.getAttribute("eleicao")).getNumVotosAtual() + 1;
                }
            %>
        }
        var nomeEleicao="";
        var nome;
        var texto="";
        nome = "<%=nome%>";
        var textoSair = nome+" deu logout";
        var textoVotos = "";
        if("<%=elei%>".toString()=="false"){
            websocket.onopen=()=>doSend("<%=nome%>"+" deu login");
        }
        else if("<%=nomeEleicao%>"!=""){
            nomeEleicao = "<%=nomeEleicao%>";
            texto = nome + " votou na eleição "+nomeEleicao;
            textoVotos = "Votos atuais da eleição "+nomeEleicao+":"+"<%=votos%>".toString()+"\n";
        }

        function sendTodo(){
            doSend(texto);
            doSend(textoVotos);
            doSend(textoSair);
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
        }

        function doSend(mensagem) {
            var message = mensagem;
            if (message != '')
                websocket.send(message); // send the message to the server
        }

    </script>
    <h2>Hello User: <%=nome%></h2>

    <form action="votar" method="post">
        <h2>Eleições Disponíveis: </h2>
        <c:forEach items="${eleicoes}" var="element">
            <h2>
                <td>${element.getTitulo()}</td>
                <input type="radio" name="eleicao" value="${element.getTitulo()}">
                <br>
            </h2>
        </c:forEach>
        <button type="submit" value="votar" >Confirmar</button>
        <br>
    </form>
    <br>
    <%
        if (elei == true)
        {
    %>
    <form action="votarTotal" method="post">
        <h2>Listas Disponíveis: </h2>
        <c:forEach items="${listas}" var="element">
            <h2>
                <td>${element.getNome()}</td>
                <input type="radio" name="lista" value="${element.getNome()}">
                <br>
            </h2>
        </c:forEach>
        <button type="submit" value="votarTotal" onclick="sendTodo()">Votar</button>
        <br>
    </form>

    <% }
    %>
    <form action="facebook">
        <button type="submit" >Associar Facebook</button>
    </form>

    <form action="logout">
        <button type="submit" onclick="doSend(textoSair)">LogOut</button>
    </form>


</div>
</body>
</html>
