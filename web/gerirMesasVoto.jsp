<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title>Gerir Mesas de Voto</title>
    <link rel="icon" type="image/png" href="resources/icon.png">
</head>
<body>
<div class="imgcontainer">
    <img src="resources/logo.png" alt="Avatar" class="avatar">
</div>
<div class="container">
    <form action="gerirMesasVoto" method="post">
        <h2>Gerir Mesas de Voto</h2>
        <h5>
            (crie ou elimine mesas que dependendo do nome dos departamentos)
            <br>
            (preencha apenas um dos campos que desejar)
        </h5>
        <br>
        <label><b>Criar Mesa de Voto:</b></label>
        <s:textfield name="criaMesa"/>
        <br><br>
        <label><b>Apagar Mesa de Voto:</b></label>
        <s:textfield name="removeMesa"/>
        <br><br>
        <button type="submit">Confirmar</button>
    </form>
    <br>
    <form action="cancelar">
        <button type="submit">Cancelar</button>
    </form>
</div>
</body>
</html>
