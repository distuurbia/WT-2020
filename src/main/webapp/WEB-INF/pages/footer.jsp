
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!-- Footer -->
<footer class="footer">
    <form id="lng_form" name="lng_form" action="<c:url value="/lng"/>" method="POST">
        <select id="lng" name="lng" onchange="this.form.submit()">
            <option value="en" <c:if test="${cookie['lng'].value.equals('en')}">selected</c:if>>English</option>
            <option value="ru" <c:if test="${cookie['lng'].value.equals('ru')}">selected</c:if>>Russian</option>
        </select>
        <input type="text" name="source_url" value="${requestScope['javax.servlet.forward.request_uri']}" style="display: none">
    </form>
    <div>
        <form action="<c:url value="/logout"/>" method="post">
            <input type="submit" value="Log out">
        </form>
    </div>
</footer>
<!-- Footer -->