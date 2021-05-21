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
            <h2>Criar Eleição</h2>
            <label><b>Título:</b></label>
            <s:textfield name="titulo"/>
            <br>
            <label><b>Descrição:</b></label>
            <s:textfield name="descricao"/>
            <br>
            <label><b>Data de Início (dd/mm/yyyy HH:MM):</b></label>
            <s:textfield name="inicio"/>
            <br>
            <label><b>Data de Fim (dd/mm/yyyy HH:MM):</b></label>
            <s:textfield name="fim"/>
            <br>
            <label><b>Tipo de Eleição (estudante/docente/funcionário):</b></label>
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
