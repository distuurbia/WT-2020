<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<fmt:setLocale value="${cookie['lng'].value}" />
<fmt:setBundle basename="messages" />

<!DOCTYPE html>
<html lang="${cookie['lng'].value}">
<head>
    <title><fmt:message key="login.login"/></title>
    <link rel="stylesheet" type="text/css" href="${pageContext.servletContext.contextPath}/styles/style.css">
</head>
<body>
<jsp:include page="header.jsp"/>
<main>
    <form id="loginForm" method="POST" action="<c:url value="/profile"/>">
        <section class="loginSection">
            <c:if test="${not empty param.errorMsg}">
                <p style="color:red">${param.errorMsg}</p>
            </c:if>
            <div class="element">
                <label><fmt:message key="login.name"/></label>
                <input type="text" name="name" placeholder="<fmt:message key="login.name"/>" required>
            </div>
            <div class="element">
                <label><fmt:message key="login.password"/></label>
                <input type="password" name="password" placeholder="<fmt:message key="login.password"/>" required>
            </div>
            <input type="submit" value="submit">
        </section>
    </form>
    <h2><fmt:message key="login.registration"/></h2>
    <form id="registerForm" method="POST" action="<c:url value="/login"/>">
        <section class="loginSection">
            <c:if test="${not empty param.errorMsg}">
                <p style="color:red">${param.errorMsg}</p>
            </c:if>
            <div class="element">
                <label><fmt:message key="login.name"/></label>
                <input type="text" name="name" placeholder="<fmt:message key="login.name"/>" required>
            </div>
            <div class="element">
                <label><fmt:message key="login.password"/></label>
                <input type="password" name="password" placeholder="<fmt:message key="login.password"/>" required>
            </div>
            <input type="submit" value="submit">
        </section>
    </form>
</main>
<jsp:include page="footer.jsp"/>
</body>
</html>