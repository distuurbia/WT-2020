<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<jsp:useBean id="authorityLvl" type="java.lang.Integer" scope="session"/>
<jsp:useBean id="races" type="java.util.ArrayList<epam.webtech.model.race.Race>" scope="request"/>

<fmt:setLocale value="${cookie['lng'].value}" />
<fmt:setBundle basename="messages" />

<!DOCTYPE html>
<html lang="${cookie['lng'].value}">
<head>
    <title><fmt:message key="races.title"/></title>
    <link rel="stylesheet" type="text/css" href="${pageContext.servletContext.contextPath}/styles/style.css">
</head>
<body>
<jsp:include page="header.jsp"/>
<main>
    <h2><fmt:message key="races.title"/></h2>
    <c:forEach var="race" items="${races}">
        <tags:race race="${race}"/>
    </c:forEach>
</main>
<jsp:include page="footer.jsp"/>
</body>
</html>