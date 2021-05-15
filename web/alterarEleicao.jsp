<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title>Alterar Eleição</title>
    <link rel="icon" type="image/png" href="resources/icon.png">
</head>
<body>
<div class="imgcontainer">
    <img src="resources/logo.png" alt="Avatar" class="avatar">
</div>
<div class="container">
    <form action="alterarEleicao" method="post">
        <h2>Alterar Propriedades de uma Eleição</h2>
        <label><b>Título da Eleição a Alterar:</b></label>
        <s:textfield name="titulo" required="true"/>
        <br>
        <label><b>Novo Título:</b></label>
        <s:textfield name="novoTitulo" required="true"/>
        <br>
        <label><b>Nova Descrição:</b></label>
        <s:textfield name="novaDescricao" required="true"/>
        <br>
        <label><b>Nova Data de Início (dd/MM/yyyy HH:mm):</b></label>
        <s:textfield name="novoInicio" required="true"/>
        <br>
        <label><b>Nova Data de Fim (dd/MM/yyyy HH:mm):</b></label>
        <s:textfield name="novoFim" required="true"/>
        <br><br>
        <button type="submit">Editar</button>
    </form>
    <form action="cancelar">
        <button type="submit">Cancelar</button>
    </form>
</div>
</body>
</html>
