<%@ tag body-content="empty" %>
<%@ attribute name="date" required="true" type="java.util.Date" %>
<%@ attribute name="blank" required="false" %>
<%@ attribute name="dateStyle" required="false" %>
<%@ attribute name="timeStyle" required="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<c:if test="${empty fn:replace(dateStyle,' ','')}"><c:set var="dateStyle" value="SHORT" /></c:if>
<c:if test="${empty fn:replace(timeStyle,' ','')}"><c:set var="timeStyle" value="MEDIUM" /></c:if>
<c:choose>
  <c:when test="${date != null}">
    <fmt:formatDate value="${date}" type="both" dateStyle="${dateStyle}" timeStyle="${timeStyle}" />
  </c:when>
  <c:when test="${blank != null}">
    <c:out value="${blank}" escapeXml="false"/>
  </c:when>
  <c:otherwise>&nbsp;</c:otherwise>
</c:choose>
