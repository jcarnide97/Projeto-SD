<%@ page import="meta1.classes.User" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="meta1.classes.Eleicao" %>
<%@ page import="meta1.classes.ListaCandidata" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
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
    <%
        String nome=((User)session.getAttribute("utilizador")).getNome();
        ArrayList<Eleicao> eleicoes = ((ArrayList<Eleicao>)session.getAttribute("eleicoes"));
        Boolean elei = false;
        if((ArrayList<ListaCandidata>)session.getAttribute("listas")!=null){
            elei = true;
            ArrayList<ListaCandidata> listas = (ArrayList<ListaCandidata>)session.getAttribute("listas");
        }
    %>
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
        <button type="submit" value="votarTotal" >Votar</button>
        <br>
    </form>

    <% }
    %>
    <form action="logout">
        <button type="submit" >LogOut</button>
    </form>
</div>
</body>
</html>
