<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<fmt:setLocale value="${cookie['lng'].value}" />
<fmt:setBundle basename="messages" />

<jsp:useBean id="race" type="epam.webtech.model.race.Race" scope="request"/>

<!DOCTYPE html>
<html lang="${cookie['lng'].value}">
<head>
    <title><fmt:message key="makebet.title"/></title>
    <link rel="stylesheet" type="text/css" href="${pageContext.servletContext.contextPath}/styles/style.css">
</head>
<body>
<jsp:include page="header.jsp"/>
<main>
    <div class="row-between">
        <span><fmt:message key="race.date"/>: ${race.date}</span>
        <span><fmt:message key="race.status"/>: ${race.status}</span>
    </div>
    <div>
        <form method="post" action="<c:url value="/makebet?raceid=${race.id}"/>">
            <h2><fmt:message key="makebet.chooseHorse"/></h2>
            <select id="horse" name="horse">
                <c:forEach var="horse" items="${race.horses}">
                    <option value="${horse.id}">${horse.name}</option>
                </c:forEach>
            </select>
            <c:if test="${not empty param.errorMessage}">
                <p style="color:red"><fmt:message key="makebet.notEnoughMoney"/></p>
            </c:if>
            <span><fmt:message key="makebet.money"/></span>
            <input type="number" name="amount">
            <input type="submit" value="Make bet">
        </form>
    </div>
</main>
<jsp:include page="footer.jsp"/>
</body>
</html>