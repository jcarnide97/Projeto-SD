<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title>Associar Mesa de Voto a Eleição</title>
    <link rel="icon" type="image/png" href="resources/icon.png">
</head>
<body>
<div class="imgcontainer">
    <img src="resources/logo.png" alt="Avatar" class="avatar">
</div>
<div class="container">
    <form action="associarMesaVoto" method="post">
        <h2>Associar Mesa de Voto a Eleição</h2>
        <label><b>Titulo da Eleição:</b></label>
        <s:textfield name="tituloEleicao"/>
        <br>
        <label><b>Departamento da Mesa:</b></label>
        <s:textfield name="nomeDep"/>
        <br><br>
        <button type="submit">Associar</button>
    </form>
    <form action="cancelar">
        <button type="submit">Cancelar</button>
    </form>
</div>
</body>
</html>
