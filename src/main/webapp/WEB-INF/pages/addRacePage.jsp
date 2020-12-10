<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="horses" type="java.util.ArrayList<epam.webtech.model.horse.Horse>" scope="request"/>

<!DOCTYPE html>
<html>
<head>
    <title>Add race</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.servletContext.contextPath}/styles/style.css">
</head>
<body>
<jsp:include page="header.jsp"/>
<main>
    <form class="add-race" method="post" action="<c:url value="/races/add"/>">
        <input type="date" name="race_date">
        <h3>Choose horses</h3>
        <div class="column">
            <c:forEach var="horse" items="${horses}">
                <div class="item">
                    <input type="checkbox" name="${horse.name}">
                    <label for="${horse.name}">${horse.name}</label>
                </div>
            </c:forEach>
        </div>
        <div>
            <input type="submit" value="Add race">
        </div>
    </form>
</main>
<jsp:include page="footer.jsp"/>
</body>
</html>