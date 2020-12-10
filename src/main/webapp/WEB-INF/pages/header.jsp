<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:useBean id="authorityLvl" type="java.lang.Integer" scope="session"/>

<!-- Header -->
<header class="row-between">
    <a href="<c:url value="/races"/>">
        <h1>10XBET</h1>
    </a>
    <div>
        <div>
            <div>
                <c:if test="${authorityLvl eq 2}">
                    <a href="<c:url value="/addrace"/>"></a>
                </c:if>
            </div>
            <a href="<c:url value="/profile"/>">
                <img class="icon" src="${pageContext.servletContext.contextPath}/images/user_icon.png">
            </a>
        </div>
    </div>
</header>
<!-- Header -->