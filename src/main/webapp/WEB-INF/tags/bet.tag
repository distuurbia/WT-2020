<%@ tag language="java" pageEncoding="UTF-8" %>
<%@ tag trimDirectiveWhitespaces="true" %>
<%@ attribute name="bet" type="epam.webtech.model.bet.Bet" required="true" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div class="card">
    <div class="row-between">
        <span>RACE DATE: ${bet.race.date}</span>
        <span>RACE STATUS: ${bet.race.status}</span>
    </div>
    <div>
        <span>Horse: ${bet.horse.name} - ${bet.amount}</span>
    </div>
    <c:choose>
        <c:when test="${bet.race.status.priority eq 2}">
            <h3 class="race-action">WINNER: ${bet.race.winnerHorse.name}!</h3>
        </c:when>
        <c:when test="${bet.race.status.priority eq 0}">
            <h3 class="race-action">Race already started</h3>
        </c:when>
    </c:choose>
</div>