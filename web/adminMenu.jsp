<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title>Consola de Administração</title>
    <link rel="icon" type="image/png" href="resources/icon.png">
</head>
<body>
    <form>
        <div class="imgcontainer">
            <img src="resources/logo.png" alt="Avatar" class="avatar">
        </div>
        <h2>Consola de Administração</h2>
        <div class="container">
            <form action="button0.action">
                <!--<button>Criar Departamento</button>-->
            </form>
            <form action="button1.action">
                <button>Criar Departamento</button>
            </form>
            <form action="button2.action">
                <button>Registar Utilizador</button>
            </form>
            <form action="button3.action">
                <button>Criar Eleição</button>
            </form>
            <form action="button4.action">
                <button>Gerir Listas de Candidatos a uma Eleição</button>
            </form>
            <form action="button5.action">
                <button>Gerir Mesas de Voto</button>
            </form>
            <form action="button6.action">
                <button>Associar Mesa de Voto a Eleição</button>
            </form>
            <form action="button7.action">
                <button>Alterar Propriedades de uma Eleição</button>
            </form>
            <form action="button8.action">
                <button>Saber Local de Voto dos Eleitores</button>
            </form>
            <form action="button9.action">
                <button>Ver Estado das Mesas de Voto</button>
            </form>
            <form action="button10.action">
                <button>Mostrar Eleições em Tempo Real</button>
            </form>
            <form action="button11.action">
                <button>Consultar Resultados de Eleições Passadas</button>
            </form>
            <br><br>
            <form action="logout.action">
                <button>Logout</button>
            </form>
        </div>
    </form>
</body>
</html>
