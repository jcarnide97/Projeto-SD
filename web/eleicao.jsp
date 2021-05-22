<%@ page import="meta1.classes.Eleicao" %>
<%@ page import="java.util.Date" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title>Partilha Eleição</title>
    <link rel="icon" type="image/png" href="resources/icon.png">
</head>
<body>
<div class="imgcontainer">
    <img src="resources/logo.png" alt="Avatar" class="avatar">
</div>
<div class="container">
    <%
        Eleicao eleicao = (Eleicao)session.getAttribute("eleicao");
        String nome=eleicao.getTitulo();
        Date inicio = eleicao.getDataComeco();
        Date fim = eleicao.getDataFim();
        int votos= eleicao.getNumVotosAtual();
        String descricao = eleicao.getDescricao();
    %>
    <h2><%=nome%></h2>
    <h3>Inicio: <%=inicio%></h3>
    <h3>Fim: <%=fim%></h3>
    <textarea><%=descricao%></textarea>
    <h3>Total de votos: <%=votos%></h3>
</div>
</body>
</html>
