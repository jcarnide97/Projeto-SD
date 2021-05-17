<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title>Criar Eleição</title>
    <link rel="icon" type="image/png" href="resources/icon.png">
</head>
<body>
    <div class="imgcontainer">
        <img src="resources/logo.png" alt="Avatar" class="avatar">
    </div>
    <div class="container">
        <form action="criarEleicao" method="post">
            <h2>Criar Departamento</h2>
            <label>Título:</label>
            <s:textfield name="titulo"/>
            <br>
            <label>Descrição:</label>
            <s:textfield name="descricao"/>
            <br>
            <label>Data de Início (dd/mm/yyyy HH:MM)</label>
            <s:textfield name="inicio"/>
            <br>
            <label>Data de Fim (dd/mm/yyyy HH:MM)</label>
            <s:textfield name="fim"/>
            <br>
            <label>Tipo de Eleição (estudante/docente/funcionário):</label>
            <s:textfield name="tipo"/>
            <br><br>
            <button type="submit">Criar Eleição</button>
        </form>
        <br>
        <form action="cancelar">
            <button type="submit">Cancelar</button>
        </form>
    </div>
</body>
</html>
