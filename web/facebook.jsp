<%@ page import="meta1.classes.User" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="meta1.classes.Eleicao" %>
<%@ page import="meta1.classes.ListaCandidata" %>
<%@ page import="java.util.Date" %>
<%@ page import="java.time.Duration" %>
<%@ page import="java.util.Calendar" %>
<%@ page import="java.util.GregorianCalendar" %>
<%@ page import="java.net.URL" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="date" uri="/struts-tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title>Facebook</title>
    <link rel="icon" type="image/png" href="resources/icon.png">
</head>
<body>
<div class="imgcontainer">
    <img src="resources/logo.png" alt="Avatar" class="avatar">
</div>
<div class="container">
    <script type="text/javascript">
    </script>
    <br>
    <br>
    <br>
    <a target="_blank" href = ${authorizationUrl}>TEST</a>
    <form action="facebook2" method="post">
        <br>
        <label><b>Insira o código</b></label>
        <s:textfield name="code" /> <br>
        <button type="submit">Concluir</button>
    </form>

</div>
</body>
</html>
