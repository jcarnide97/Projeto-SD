<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title>Eleições Passadas</title>
    <link rel="icon" type="image/png" href="resources/icon.png">
</head>
<body>
<div class="imgcontainer">
    <img src="resources/logo.png" alt="Avatar" class="avatar">
</div>
<div class="container">
    <form action="eleicoesPassadas" method="post">
        <h2>Consultar Eleições Passadas</h2>
        <label><b>Título da Eleição:</b></label>
        <s:textfield name="titulo"/>
        <br><br>
        <label><b>Resultados:</b></label>
        <s:textarea style="width: 500px; height: 200px;" value="%{resultados}" />
        <br><br>
        <button type="submit">Pesquisar</button>
    </form>
    <br>
    <form action="cancelar">
        <button type="submit">Cancelar</button>
    </form>
</div>
</body>
</html>
