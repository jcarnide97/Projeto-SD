<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title>eVoting</title>
    <link rel="icon" type="image/png" href="resources/icon.png">
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
    <form action="login" method="post">
        <div class="imgcontainer">
            <img src="resources/logo.png">
        </div>
        <br>
        <div class="container">
            <label><b>Nome</b></label>
            <s:textfield name="nome" /> <br>
            <label><b>Password</b></label>
            <s:textfield name="password" type="password"/> <br>
            <button type="submit" value="login">Login</button>
        </div>
    </form>
</body>
</html>
