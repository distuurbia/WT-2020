<%@ tag trimDirectiveWhitespaces="true" %>
<%@ attribute name="errorCode" required="true" %>
<%@ attribute name="errorMessage" required="true" %>
<%@ taglib uri='http://java.sun.com/jsp/jstl/core' prefix='c' %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<!DOCTYPE html>
<html>
<head>
    <title>${errorCode}</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.servletContext.contextPath}/styles/style.css.css">
</head>
<body>
<jsp:include page="../pages/header.jsp"/>
<main>
    <article class="about-article">
        <div class="name">${errorCode}</div>
        <div class="title">${errorMessage}</div>
    </article>
</main>
<jsp:include page="../pages/footer.jsp"/>
</body>
</html>