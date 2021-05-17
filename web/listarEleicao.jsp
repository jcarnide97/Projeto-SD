<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title>listar Eleições</title>
    <link rel="icon" type="image/png" href="resources/icon.png">
</head>
<body>
<div class="imgcontainer">
    <img src="resources/logo.png" alt="Avatar" class="avatar">
</div>
<div class="container">
    <form action="listarEleicao" method="post">
        <h2>Consultar Detalhes de Eleições</h2>
        <br>
        <label><b>Título da Eleição:</b></label>
        <s:textfield name="titulo"/>
        <br>
        <button type="submit">Ver Detalhes</button>
        <br>
        <s:textarea style="width: 500px; height: 200px;" readonly="true" value="%{detalhesEleicao}"/>
    </form>
    <br>
    <form action="cancelar">
        <button type="submit">Cancelar</button>
    </form>
</div>
</body>
</html>
