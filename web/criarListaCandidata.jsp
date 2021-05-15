<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title>Criar Lista Candidata</title>
    <link rel="icon" type="image/png" href="resources/icon.png">
</head>
<body>
<div class="imgcontainer">
    <img src="resources/logo.png" alt="Avatar" class="avatar">
</div>
<div class="container">
    <form action="criarListaCandidata" method="post">
        <h2>Criar Lista Candidata</h2>
        <label><b>Nome:</b></label>
        <s:textfield name="nomeLista"/>
        <br>
        <label><b>Título da Eleição:</b></label>
        <s:textfield name="tituloEleicao"/>
        <br>
        <button type="submit">Criar Lista</button>
    </form>
    <br>
    <form action="cancelar">
        <button type="submit">Cancelar</button>
    </form>
</div>
</body>
</html>
