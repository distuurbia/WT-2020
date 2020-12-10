
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<link rel="stylesheet" type="text/css" href="${pageContext.servletContext.contextPath}/styles/style.css">
<c:choose>
    <c:when test="${not empty requestScope.errorMessage}">
        <tags:error errorCode="404" errorMessage="${requestScope.errorMessage}"/>
    </c:when>
    <c:otherwise>
        <c:when test="${not empty requestScope.errorMessage}">
            <tags:error errorCode="404" errorMessage="Resource not found"/>
        </c:when>
    </c:otherwise>
</c:choose>


