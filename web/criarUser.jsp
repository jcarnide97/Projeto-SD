<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title>Criar Utilizador</title>
    <link rel="icon" type="image/png" href="resources/icon.png">
</head>
<body>
    <div class="imgcontainer">
        <img src="resources/logo.png" alt="Avatar" class="avatar">
    </div>
    <div class="container">
        <form action="criarUser">
            <label><h2>Criar Utilizador</h2></label>
            <br>
            <label><b>Nome:</b></label>
            <s:textfield name="nome"/>
            <br>
            <label><b>Número:</b></label>
            <s:textfield name="numero"/>
            <br>
            <label><b>Departamento:</b></label>
            <s:textfield name="departamento"/>
            <br>
            <label><b>Telefone:</b></label>
            <s:textfield name="telefone"/>
            <br>
            <label><b>Morada:</b></label>
            <s:textfield name="morada"/>
            <br>
            <label><b>Validade do Cartão de Cidadão:</b></label>
            <s:textfield name="validadeCC"/>
            <br>
            <label><b>Tipo de Utilizador (estudante, docente ou funcionário):</b></label>
            <s:textfield name="tipo"/>
            <br>
            <label><b>Password:</b></label>
            <s:textfield name="password" type="password"/>
            <br><br>
            <button type="submit">Criar User</button>
        </form>
        <br>
        <form action="cancelar">
            <button type="submit">Cancelar</button>
        </form>
    </div>
</body>
</html>
