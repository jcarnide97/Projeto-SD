<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="date" uri="/struts-tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Estado da Eleição</title>
    <link rel="icon" type="image/png" href="resources/icon.png">
    <link rel="stylesheet" type="text/css" href="css/tempoReal.css">
    <script src="js/tempoReal.js"></script>
</head>
<body>
<noscript>JavaScript must be enabled for WebSockets to work.</noscript>
<div class="imgcontainer">
    <img src="resources/logo.png" alt="Avatar" class="avatar">
</div>
<div>
    <h2>Dados das Eleições em Tempo Real</h2>
    <br>
    <div id="container">
        <div id="history"></div>
    </div>
    <br>
    <form action="cancelar">
        <button type="submit" >Cancelar</button>
    </form>
</div>
</body>
</html>