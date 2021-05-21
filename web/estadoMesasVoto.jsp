<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title>Estado das Mesas de Voto</title>
    <link rel="icon" type="image/png" href="resources/icon.png">
    <link rel="stylesheet" type="text/css" href="css/tempoReal.css">
</head>
<body>
<div class="imgcontainer">
    <img src="resources/logo.png" alt="Avatar" class="avatar">
</div>
<div class="container">
    <h2>Estado das Mesas de Voto</h2>
    <br>
    <div id="container">
        <div id="history"></div>
    </div>
    <form action="cancelar">
        <button type="submit">Cancelar</button>
    </form>
</div>
</body>
</html>
