<%--
/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2006-2012 The OpenNMS Group, Inc.
 * OpenNMS(R) is Copyright (C) 1999-2012 The OpenNMS Group, Inc.
 *
 * OpenNMS(R) is a registered trademark of The OpenNMS Group, Inc.
 *
 * OpenNMS(R) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published
 * by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version.
 *
 * OpenNMS(R) is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with OpenNMS(R).  If not, see:
 *      http://www.gnu.org/licenses/
 *
 * For more information contact:
 *     OpenNMS(R) Licensing <license@opennms.org>
 *     http://www.opennms.org/
 *     http://www.opennms.com/
 *******************************************************************************/

--%>

<%@page import="com.google.common.base.Strings"%>
<%@page import="org.opennms.web.snmpinfo.SnmpInfo"%>
<%@page import="com.google.common.base.Charsets"%>
<%@page import="java.nio.charset.Charset"%>
<%@page import="com.google.common.io.Files"%>
<%@page import="org.opennms.netmgt.config.SnmpPeerFactory"%>
<%@page language="java" contentType="text/html" session="true"%>


<jsp:include page="/includes/header.jsp" flush="false">
	<jsp:param name="title" value="Configure SNMP Parameters per polled IP" />
	<jsp:param name="headTitle" value="SNMP Configuration" />
	<jsp:param name="headTitle" value="Admin" />
	<jsp:param name="location" value="admin" />
	<jsp:param name="breadcrumb"
		value="<a href='admin/index.jsp'>Admin</a>" />
	<jsp:param name="breadcrumb" value="Configure SNMP by IP" />
	<jsp:param name="script"
		value="<script type='text/javascript' src='js/ipv6/ipv6.js'></script>" />
	<jsp:param name="script"
		value="<script type='text/javascript' src='js/ipv6/lib/jsbn.js'></script>" />
	<jsp:param name="script"
		value="<script type='text/javascript' src='js/ipv6/lib/jsbn2.js'></script>" />
	<jsp:param name="script"
		value="<script type='text/javascript' src='js/ipv6/lib/sprintf.js'></script>" />
</jsp:include>

<script type="text/javascript">
	function verifySnmpConfig() {
		var ipValue = new String("");

		// validate Ip-Address
		ipValue = new String(document.snmpConfigForm.firstIPAddress.value);
		if (ipValue == "") {
			alert("Please enter a valid first IP address!");
			return false;
		}
		if (!isValidIPAddress(ipValue)) {
			alert(ipValue + " is not a valid IP address!");
			return false;
		}
		ipValue = new String(document.snmpConfigForm.lastIPAddress.value);
		if (ipValue != "" && !isValidIPAddress(ipValue)) {
			alert(ipValue + " is not a valid IP address!");
			return false;
		}

		//validate timeout
		var timeout = new String(document.snmpConfigForm.timeout.value);
		if (timeout != "" && (!isNumber(timeout) || parseInt(timeout) <= 0)) {
			alert(timeout
					+ " is not a valid timeout. Please enter a number greater than 0 or leave it empty.");
			return false;
		}

		//validate retryCount
		var retryCount = new String(document.snmpConfigForm.retryCount.value);
		if (retryCount != ""
				&& (!isNumber(retryCount) || parseInt(retryCount) <= 0)) {
			alert(retryCount
					+ " is not a valid Retry Count. Please enter a number greater than 0 or leave it empty.");
			return false;
		}

		// validate port
		var port = new String(document.snmpConfigForm.port.value);
		if (port != "" && (!isNumber(port) || parseInt(port) <= 0)) {
			alert(port
					+ " is not a valid Port. Please enter a number greater than 0 or leave it empty.");
			return false;
		}

		// validate maxRequestSize
		var maxRequestSize = new String(
				document.snmpConfigForm.maxRequestSize.value);
		if (maxRequestSize != ""
				&& (!isNumber(maxRequestSize) || parseInt(maxRequestSize) <= 0)) {
			alert(maxRequestSize
					+ " is not a valid Max Request Size. Please enter a number greater than 0 or leave it empty.");
			return false;
		}

		// validate maxVarsPerPdu
		var maxVarsPerPdu = new String(
				document.snmpConfigForm.maxVarsPerPdu.value);
		if (maxVarsPerPdu != ""
				&& (!isNumber(maxVarsPerPdu) || parseInt(maxVarsPerPdu) <= 0)) {
			alert(maxVarsPerPdu
					+ " is not a valid Max Vars Per Pdu. Please enter a number greater than 0 or leave it empty.");
			return false;
		}

		// validate maxRepetitions
		var maxRepetitions = new String(
				document.snmpConfigForm.maxRepetitions.value);
		if (maxRepetitions != ""
				&& (!isNumber(maxRepetitions) || parseInt(maxRepetitions) <= 0)) {
			alert(maxRepetitions
					+ " is not a valid Max Repetitions. Please enter a number greater than 0 or leave it empty.");
			return false;
		}
		return true;
	}
<%/*  checks if the given parameter is a number, so we assume it can be parsed as an integer*/%>
	function isNumber(input) {
		return !isNaN(input - 0) && input != null && input !== null
				&& input !== "" && input !== false;
	}

	/*
	 * On Version change only the specificy section is shown.
	 */
	function onVersionChange() {
		var versionElements = new Array(document.getElementById("v1v2"),
				document.getElementById("v3"));

		//  determine selected element
		if (document.getElementById("version").value == "v1"
				|| document.getElementById("version").value == "v2c")
			var selectedElement = document.getElementById("v1v2");
		if (document.getElementById("version").value == "v3")
			var selectedElement = document.getElementById("v3");

		// hide all not selected elements and show selected Element
		for ( var elementIndex in versionElements) {
			var element = versionElements[elementIndex];
			if (element == selectedElement) { // show
				element.style.visibility = null;
				element.style.display = "block";
			} else { // hide
				element.style.visibility = "hidden";
				element.style.display = "none";
			}
		}
	}

	function cancel() {
		document.snmpConfigForm.action = "admin/index.jsp";
		document.snmpConfigForm.submit();
	}
</script>

<%!// does Null Pointer handling
	public String getValue(Object input) {
		if (input == null) return "";
		return input.toString();
	}

	public String getOptions(String selectedOption, String defaultOption, String... options) {
		// prevent Nullpointer
		if (defaultOption == null) defaultOption = "";
		// ensure that there is a default :)
		if (Strings.isNullOrEmpty(selectedOption)) selectedOption = defaultOption;

		final String optionTemplate = "<option %s>%s</option>";
		String optionsString = "";
		for (String eachOption : options) {
			optionsString += String.format(optionTemplate, eachOption.equals(selectedOption) ? "selected" : "",
					eachOption);
			optionsString += "\n";
		}
		return optionsString.trim();
	}%>

<%
	Object obj = request.getAttribute("snmpConfigForIp");
	SnmpInfo snmpInfo = obj == null ? new SnmpInfo() : (SnmpInfo) obj;

	String firstIpAddress = getValue(request.getAttribute("firstIPAddress"));
	String version = getValue(snmpInfo.getVersion());
	String timeout = getValue(snmpInfo.getTimeout());
	String retryCount = getValue(snmpInfo.getRetries());
	String port = getValue(snmpInfo.getPort());
	String maxRequestSize = getValue(snmpInfo.getMaxRequestSize());
	String maxVarsPerPdu = getValue(snmpInfo.getMaxVarsPerPdu());
	String maxRepetitions = getValue(snmpInfo.getMaxRepetitions());
	String readCommunityString = getValue(snmpInfo.getReadCommunity());
	String writeCommunityString = getValue(snmpInfo.getWriteCommunity());
	String securityName = getValue(snmpInfo.getSecurityName());
	String securityLevel = getValue(snmpInfo.getSecurityLevel());
	String authPassPhrase = getValue(snmpInfo.getAuthPassPhrase());
	String authProtocol = getValue(snmpInfo.getAuthProtocol());
	String privPassPhrase = getValue(snmpInfo.getPrivPassPhrase());
	String privProtocol = getValue(snmpInfo.getPrivProtocol());
	String engineId = getValue(snmpInfo.getEngineId());
	String contextEngineId = getValue(snmpInfo.getContextEngineId());
	String contextName = getValue(snmpInfo.getContextName());
	String enterpriseId = getValue(snmpInfo.getEnterpriseId());
%>

<body onload="onVersionChange()">
	<div class="TwoColLAdmin">
		<form method="post" name="snmpConfigGetForm"
			action="admin/snmpConfig?action=get">
			<h3>SNMP Config Lookup</h3>
			<div>
				<table>
					<tr>
						<td width="25%">IP Address:</td>
						<td width="50%"><input type="text" name="ipAddress"
							<%=firstIpAddress%> /></td>
					</tr>
					<tr>
						<td align="right"><input type="submit" name="getConfig"
							value="Look up"></td>
						<td><input type="button" name="cancel1" value="Cancel"
							onclick="cancel()"></td>
					<tr>
				</table>
			</div>
		</form>
		<form method="post" name="snmpConfigForm"
			action="admin/snmpConfig?action=add"
			onsubmit="return verifySnmpConfig();">
			<!--  General parameters -->
			<h3>Updating SNMP Configuration</h3>
			<div id="general">
				<table>
					<tr>
						<th colspan="2">General parameters</th>
					</tr>
					<tr>
						<td width="25%">Version:</td>
						<td width="50%"><select id="version" name="version"
							onChange="onVersionChange()">
								<%=getOptions(version, "v2c", "v1", "v2c", "v3")%>
						</select></td>
					</tr>
					<tr>
						<td width="25%">First IP Address:</td>
						<td width="50%"><input size=15 name="firstIPAddress"
							value="<%=firstIpAddress%>"></td>
					</tr>

					<tr>
						<td width="25%">Last IP Address:</td>
						<td width="50%"><input size=15 name="lastIPAddress">
							(Optional)</td>
					</tr>

					<tr>
						<td width="25%">Timeout:</td>
						<td width="50%"><input size=15 name="timeout"
							value="<%=timeout%>"> (Optional)</td>
					</tr>

					<tr>
						<td width="25%">Retries:</td>
						<td width="50%"><input size=15 name="retryCount"
							value="<%=retryCount%>"> (Optional)</td>
					</tr>

					<tr>
						<td width="25%">Port:</td>
						<td width="50%"><input size=15 name="port" value="<%=port%>">
							(Optional)</td>
					</tr>

					<tr>
						<td width="25%">Max Request Size:</td>
						<td width="50%"><input size=15 name="maxRequestSize"
							value="<%=maxRequestSize%>" /> (Optional)</td>
					</tr>

					<tr>
						<td width="25%">Max Vars Per Pdu:</td>
						<td width="50%"><input size=15 name="maxVarsPerPdu"
							value="<%=maxVarsPerPdu%>" /> (Optional)</td>
					</tr>

					<tr>
						<td width="25%">Max Repetitions:</td>
						<td width="50%"><input size=15 name="maxRepetitions"
							value="<%=maxRepetitions%>" /> (Optional)</td>
					</tr>

				</table>
			</div>
			<!-- v1/v2c parameters -->
			<div id="v1v2" style="visibility: hidden; display: none;">
				<table>
					<tr>
						<th colspan="2">v1/v2c specific parameters</th>
					</tr>
					<tr>
						<td width="25%">Read Community String:</td>
						<td width="50%"><input size=30 name="readCommunityString"
							value="<%=readCommunityString%>" /> (Optional)</td>
					</tr>

					<tr>
						<td width="25%">Write Community String:</td>
						<td width="50%"><input size=30 name="writeCommunityString"
							value="<%=writeCommunityString%>" /> (Optional)</td>
					</tr>
				</table>
			</div>

			<!--  v3 parameters -->
			<div id="v3" style="visibility: hidden; display: none;">
				<table>
					<tr>
						<th colspan="2">v3 specific parameters</th>
					</tr>
					<tr>
						<td width="25%">Security Name:</td>
						<td width="50%"><input size=15 name="securityName"
							value="<%=securityName%>" /> (Optional)</td>
					</tr>

					<tr>
						<td width="25%">Security Level:</td>
						<td width="50%"><select name="securityLevel"
							style="width: 100px">
								<option value=""></option>
								<option value="1"
									<%="1".equals(securityLevel) ? "selected" : ""%>>noAuthNoPriv</option>
								<option value="2"
									<%="2".equals(securityLevel) ? "selected" : ""%>>authNoPriv</option>
								<option value="3"
									<%="3".equals(securityLevel) ? "selected" : ""%>>authPriv</option>
						</select> (Optional)</td>
					</tr>

					<tr>
						<td width="25%">Auth Passphrase:</td>
						<td width="50%"><input size=15 name="authPassPhrase"
							value="<%=authPassPhrase%>" /> (Optional)</td>
					</tr>

					<tr>
						<td width="25%">Auth Protocol:</td>
						<td width="50%"><select name="authProtocol"
							style="width: 100px">
								<%=getOptions(authProtocol, "", "", "MD5", "SHA")%>
						</select> (Optional)</td>
					</tr>

					<tr>
						<td width="25%">Privacy Passphrase:</td>
						<td width="50%"><input size=15 name="privacyPassPhrase"
							value="<%=privPassPhrase%>" /> (Optional)</td>
					</tr>

					<tr>
						<td width="25%">Privacy Protocol:</td>
						<td width="50%"><select name="privacyProtocol"
							style="width: 100px">
								<%=getOptions(privProtocol, "", "", "DES", "AES", "AES192", "AES256")%>
						</select> (Optional)</td>
					</tr>

					<tr>
						<td width="25%">Engine Id:</td>
						<td width="50%"><input size=15 name="engineId"
							value="<%=engineId%>" /> (Optional)</td>
					</tr>

					<tr>
						<td width="25%">Context Engine Id:</td>
						<td width="50%"><input size=15 name="contextEngineId"
							value="<%=contextEngineId%>" /> (Optional)</td>
					</tr>

					<tr>
						<td width="25%">Context Name:</td>
						<td width="50%"><input size=15 name="contextName"
							value="<%=contextName%>" /> (Optional)</td>
					</tr>
					<tr>
						<td width="25%">Enterprise Id:</td>
						<td width="50%"><input size=15 name="enterpriseId"
							value="<%=enterpriseId%>" /> (Optional)</td>
					</tr>
				</table>
			</div>
			<!--  submit area -->
			<div>
				<%
					if (request.getAttribute("success") != null) {
				%>
				<div>
					<p><b>Finished configuring SNMP.</b> OpenNMS does not need to be restarted.</p>
				</div>
				<%
					}
				%>
				<table>
					<tr>
						<td width="25%" align="right"><input type="submit"
							name="saveConfig" value="Save config"></td>
						<td width="50%"><input type="button" name="cancel2"
							value="Cancel" onclick="cancel()"></td>
					</tr>
				</table>
			</div>
		</form>

		<!--  Content of snmp-config.xml -->
		<div>
			<h3>Content of snmp-config.xml</h3>
			<p>
				<textarea disabled style="width: 100%; height: 100px"><%=request.getAttribute("snmpConfig")%></textarea>
			</p>
		</div>

	</div>

	<div class="TwoColRAdmin">
		<h3>Descriptions</h3>
		<p>
			<b>SNMP Config Lookup:</b> You can look up the actual SNMP
			configuration for a specific IP. To do so enter the IP Address in the
			SNMP Config Lookup box and press "Look up". The configuration will
			then be shown in the "Updateing SNMP Community Names" area.
		</p>

		<p>
			<b>Updating SNMP Configuration:</b> In the boxes on the left, enter
			in a specific IP address and community string, or a range of IP
			addresses and a community string, and other SNMP parameters.
		</p>
		<p>OpenNMS will optimize this list, so enter the most generic
			first (i.e. the largest range) and the specific IP addresses last,
			because if a range is added that includes a specific IP address, the
			community name for the specific address will be changed to be that of
			the range.</p>
		<p>For devices that have already been provisioned and that have an
			event stating that data collection has failed because the community
			name changed, it may be necessary to update the SNMP information on
			the interface page for that device (by selecting the "Update SNMP"
			link) for these changes to take effect.</p>

		<p>
			<b>Content of snmp-config.xml:</b> This area shows the content of the
			configuration file 'snmp-config.xml'. If you updated the SNMP
			Configuration you may or may not see your changes. If your changes
			are not visible they have been optimized (For further information
			have a look at (TODO link to wiki page))
		</p>
	</div>
	<jsp:include page="/includes/footer.jsp" flush="false" />
</body>
