<%@ tag language="java" pageEncoding="UTF-8" %>
<%@ tag trimDirectiveWhitespaces="true" %>
<%@ attribute name="race" type="epam.webtech.model.race.Race" required="true" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:useBean id="authorityLvl" type="java.lang.Integer" scope="session"/>

<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<fmt:setLocale value="${cookie['lng'].value}" />
<fmt:setBundle basename="messages" />

<div class="card">
    <div class="row-between">
        <span><fmt:message key="race.date"/>: ${race.date}</span>
        <span><fmt:message key="race.status"/>: ${race.status}</span>
    </div>
    <div>
        <ul>
            <c:forEach var="horse" items="${race.horses}">
                <li>${horse.name}</li>
            </c:forEach>
        </ul>
    </div>
    <c:choose>
        <c:when test="${race.status.priority eq 2}">
            <h3 class="race-action"><fmt:message key="race.winner"/>: ${race.winnerHorse.name}!</h3>
        </c:when>
        <c:when test="${race.status.priority eq 1}">
            <c:choose>
                <c:when test="${authorityLvl eq 2}">
                    <div>
                        <form action="<c:url value="/races/start?raceid=${race.id}"/>" method="post">
                            <input type="submit" value="<fmt:message key="race.start"/>">
                        </form>
                    </div>
                </c:when>
                <c:otherwise>
                    <div>
                        <a href="<c:url value="/makebet?raceid=${race.id}"/>">
                            <button class="race-action"><fmt:message key="race.makeBet"/></button>
                        </a>
                    </div>
                </c:otherwise>
            </c:choose>
        </c:when>
        <c:otherwise>
            <c:choose>
                <c:when test="${authorityLvl eq 2}">
                    <div>
                        <form action="<c:url value="/races/finish?raceid=${race.id}"/>" method="post">
                            <select id="winnerHorse" name="winnerHorse">
                                <c:forEach var="horse" items="${race.horses}">
                                    <option value="${horse.id}">${horse.name}</option>
                                </c:forEach>
                            </select>
                            <input type="submit" value="<fmt:message key="race.finish"/>">
                        </form>
                    </div>
                </c:when>
                <c:otherwise>
                    <h3 class="race-action"><fmt:message key="race.alreadyStarted"/></h3>
                </c:otherwise>
            </c:choose>
        </c:otherwise>
    </c:choose>
</div>
