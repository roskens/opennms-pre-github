<%--

//
// This file is part of the OpenNMS(R) Application.
//
// OpenNMS(R) is Copyright (C) 2002-2003 The OpenNMS Group, Inc.  All rights reserved.
// OpenNMS(R) is a derivative work, containing both original code, included code and modified
// code that was published under the GNU General Public License. Copyrights for modified 
// and included code are below.
//
// OpenNMS(R) is a registered trademark of The OpenNMS Group, Inc.
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
// Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
//
// For more information contact:
//      OpenNMS Licensing       <license@opennms.org>
//      http://www.opennms.org/
//      http://www.opennms.com///

--%>

<%@page language="java"
	contentType="text/html"
	session="true"
	import="org.opennms.web.element.*,
		java.util.*,
        org.opennms.web.svclayer.ResourceService,
        org.opennms.web.inventory.InventoryLayer,
        org.springframework.web.context.WebApplicationContext,
        org.springframework.web.context.support.WebApplicationContextUtils,
        org.opennms.web.element.ElementUtil"
%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>


<%!
    private ResourceService m_resourceService;

    public void init() throws ServletException {

	    WebApplicationContext webAppContext = WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());
		m_resourceService = (ResourceService) webAppContext.getBean("resourceService", ResourceService.class);
		
		InventoryLayer.init();
    }

	public static String getStatusStringWithDefault(Node node_db) {
	    String status = ElementUtil.getNodeStatusString(node_db);
	    if (status != null) {
	        return status;
	    } else {
	        return "Unknown";
	    }
	}
%>

<%

	Node node_db = ElementUtil.getNodeByParams(request);
	int nodeId = node_db.getNodeId();

	String elementID = node_db.getLabel();
	
	Map<String, Object> nodeModel = new TreeMap<String, Object>();

	try {	
	    nodeModel = InventoryLayer.getRancidNode(elementID,request);

	} catch (Exception e) {
		//throw new ServletException("Could node get Rancid Node ", e);
	}
    nodeModel.put("id", elementID);
    nodeModel.put("status_general", getStatusStringWithDefault(node_db));
    pageContext.setAttribute("model", nodeModel);
    
%>

<%
String nodeBreadCrumb = "<a href='element/node.jsp?node=" + nodeId  + "'>Node</a>";
%>

<jsp:include page="/includes/header.jsp" flush="false" >
  <jsp:param name="title" value="Rancid" />
  <jsp:param name="headTitle" value="${model.id}" />
  <jsp:param name="headTitle" value="Rancid" />
  <jsp:param name="breadcrumb" value="<a href='element/index.jsp'>Search</a>" />
  <jsp:param name="breadcrumb" value="<%= nodeBreadCrumb %>" />
  <jsp:param name="breadcrumb" value="Rancid" />
</jsp:include>

<h2>Node: ${model.id} </h2>

<div class="TwoColLeft">
	<!-- general info box -->
	<h3>General (Status: ${model.status_general})</h3>
  	<table>
  		<tr>
	  		<th>Node</th>
	  		<th><a href="element/node.jsp?node=<%=nodeId%>"><%=elementID%></a></th>
	  	</tr>
	</table>

	<h3>Rancid info</h3>
	<table>
		<tr>
			<th>Device Name</th>
			<th>${model.id}</th>
		</tr>	
		<tr>
			<th>Device Type</th>
			<th>${model.devicetype}</th>
		</tr>
		<tr>
			<th>Comment</th>
			<th>${model.comment}</th>
		</tr>
		<tr>
			<th>Status</th>
			<th>${model.status}</th>
		</tr>
	</table>

</div>

<div class="TwoColRight">
<!-- general info box -->
	<h3>Associated Elements</h3>
	
	<table>
	<tr>
		<th>Group</th>
		<th>CVS Root repository</th>
		<th>Total revisions</th>
		<th>Head version</th>
		<th>Last Update</th>
	</tr>
	<c:forEach items="${model.grouptable}" var="groupelm" begin ="0" end="9">
		<tr>
			<th>${groupelm.group}</th>
			<th><a href="${groupelm.rootConfigurationUrl}">${model.id}</th>
			<th>${groupelm.totalRevisions} <a href="inventory/rancidList.jsp?node=<%=nodeId%>&groupname=${groupelm.group}">(list)</a></th>
			<th><a href="inventory/invnode.jsp?node=<%=nodeId%>&groupname=${groupelm.group}&version=${groupelm.headRevision}">${groupelm.headRevision}</th>
			<th>${groupelm.creationDate}</th>
		</tr>
	</c:forEach>
		<th colspan="5" ><a href="inventory/rancidList.jsp?node=<%=nodeId%>&groupname=*">entire group list...</a></th>
	</table>
</div>
<jsp:include page="/includes/footer.jsp" flush="false" />