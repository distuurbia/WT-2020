<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="user" type="epam.webtech.model.user.User" scope="request"/>
<jsp:useBean id="bets" type="java.util.ArrayList<epam.webtech.model.bet.Bet>" scope="request"/>

<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<fmt:setLocale value="${cookie['lng'].value}" />
<fmt:setBundle basename="messages" />

<!DOCTYPE html>
<html lang="${cookie['lng'].value}">
<head>
    <title><fmt:message key="profile.title"/></title>
    <link rel="stylesheet" type="text/css" href="${pageContext.servletContext.contextPath}/styles/style.css">
</head>
<body>
<jsp:include page="header.jsp"/>
<main>
    <h2><fmt:message key="profile.title"/></h2>
    <div><fmt:message key="profile.name"/>: ${user.name}</div>
    <div><fmt:message key="profile.money"/>: ${user.bank}</div>
    <div>
        <c:choose>
            <c:when test="${bets.size() eq 0}">
                <h1><fmt:message key="profile.noBets"/></h1>
            </c:when>
            <c:otherwise>
                <h1><fmt:message key="profile.bets"/>:</h1>
                <c:forEach var="bet" items="${bets}">
                    <tags:bet bet="${bet}"/>
                </c:forEach>
            </c:otherwise>
        </c:choose>
    </div>
</main>
<jsp:include page="footer.jsp"/>
</body>
</html>