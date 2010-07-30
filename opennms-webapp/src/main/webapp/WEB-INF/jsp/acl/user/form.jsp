<%--
//============================================================================
//
// Copyright (c) 2009+ desmax74
// Copyright (c) 2009+ The OpenNMS Group, Inc.
// All rights reserved everywhere.
//
// This program was developed and is maintained by Massimiliano Dessi
// ("the author") and is subject to dual-copyright according to
// the terms set in "The OpenNMS Project Contributor Agreement".
//
// This program is free software; you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation; either version 2 of the License, or
// (at your option) any later version.
//
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with this program; if not, write to the Free Software
// Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307,
// USA.
//
// The author can be contacted at the following email address:
//
//       Massimiliano Dess&igrave;
//       desmax74@yahoo.it
//
//
//-----------------------------------------------------------------------------
// OpenNMS Network Management System is Copyright by The OpenNMS Group, Inc.
//============================================================================
--%>
<%@ include file="/WEB-INF/jsp/acl/taglibs.jsp"%>
<springf:form action="user.edit.page" commandName="user" method="post">
    <table>
        <tr>
        	<td><spring:message code="ui.user.username" />:</td>
        <c:choose>
        	<c:when test="${user.new}">
            <td><springf:input path="username" maxlength="45"/></td>
            <td><springf:errors path="username" cssClass="error-style"/></td>
            </c:when>
        <c:otherwise>
            <td>${user.username}</td>
        </c:otherwise>
        </c:choose>
        </tr>
        <tr>
        <c:choose>
        <c:when test="${user.new}">
            <td><spring:message code="ui.user.password"/>:</td>
            <td><springf:password path="password"/></td>
            <td><springf:errors path="password" cssClass="error-style"/></td>
        </c:when>
        <c:otherwise>
            <td><spring:message code="ui.user.password.new"/>:</td>
            <td><springf:password path="newPassword"/></td>
            <td><springf:errors path="newPassword" cssClass="error-style"/></td>
            <input type="hidden" id="password" name="password" value="${user.password}"/>
        </c:otherwise>
        </c:choose>
        </tr>
         <tr>
            <td><spring:message code="ui.user.password.confirm.new"/>:</td>
            <td><springf:password path="confirmNewPassword"/></td>
            <td><springf:errors path="confirmNewPassword" cssClass="error-style"/></td>
        </tr>
        <tr>
            <td><spring:message code="ui.user.fullname"/>:</td>
            <td><springf:input path="fullname"/></td>
            <td><springf:errors path="fullname" cssClass="error-style"/></td>
        </tr>
        <tr>
            <td><spring:message code="ui.user.comments"/>:</td>
            <td><springf:textarea path="comments"/></td>
            <td><springf:errors path="comments" cssClass="error-style"/></td>
        </tr>
        <tr>
            <td><spring:message code="ui.user.email"/>:</td>
            <td><springf:input path="email"/></td>
            <td><springf:errors path="email" cssClass="error-style"/></td>
        </tr>
        <tr>
            <td><spring:message code="ui.user.numsvc"/>:</td>
            <td><springf:input path="numsvc"/></td>
            <td><springf:errors path="numsvc" cssClass="error-style"/></td>
        </tr>
        <tr>
            <td><spring:message code="ui.user.numpin"/>:</td>
            <td><springf:input path="numpin"/></td>
            <td><springf:errors path="numpin" cssClass="error-style"/></td>
        </tr>
        <tr>
            <td><spring:message code="ui.user.txtsvc"/>:</td>
            <td><springf:input path="txtsvc"/></td>
            <td><springf:errors path="txtsvc" cssClass="error-style"/></td>
        </tr>
        <tr>
            <td><spring:message code="ui.user.txtpin"/>:</td>
            <td><springf:input path="txtpin"/></td>
            <td><springf:errors path="txtpin" cssClass="error-style"/></td>
        </tr>
        <tr>
            <td colspan="3">
                <input type="submit" value="<spring:message code="ui.action.save"/>"/>
                <input type="hidden" id="enabled" name="enabled" value="${user.enabled}"/>
                <input type="hidden" id="new" name="new" value="${user.new}"/>
                <c:if test="${!user.new}">
        	    	<input type="hidden" id="username" name="username" value="${user.username}"/>
        	    </c:if>
                <input type="button" onclick="location.href = 'user.list.page'" value="<spring:message code="user.list"/>"/>
            </td>
        </tr>
    </table>
</springf:form>