<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title>Criar Departamento</title>
    <link rel="icon" type="image/png" href="resources/icon.png">
</head>
<body>
    <div class="imgcontainer">
        <img src="resources/logo.png" alt="Avatar" class="avatar">
    </div>
    <div class="container">
        <form action="criarDepartamento" method="post">
            <h2>Criar Departamento</h2>
            <label><b>Nome:</b></label>
            <s:textfield name="nomeDep"/>
            <br><br>
            <button type="submit">Criar Departamento</button>
        </form>
        <form action="cancelar">
            <button type="submit">Cancelar</button>
        </form>
    </div>
</body>
</html>
