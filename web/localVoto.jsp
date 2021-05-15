<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title>Local de Voto de Eleitores</title>
    <link rel="icon" type="image/png" href="resources/icon.png">
</head>
<body>
<div class="imgcontainer">
    <img src="resources/logo.png" alt="Avatar" class="avatar">
</div>
<div class="container">
    <form action="localVoto" method="post">
        <h2>Local de Voto de Eleitores</h2>
        <br>
        <label><b>Número do eleitor:</b></label>
        <s:textfield name="numero"/>
        <br>
        <button type="submit">Confirmar</button>
        <br>
        <s:textarea style="width: 500px; height: 200px;" value="%{listaLocais}"/>
    </form>
    <br>
    <form action="cancelar">
        <button type="submit">Cancelar</button>
    </form>
</div>
</body>
</html>
