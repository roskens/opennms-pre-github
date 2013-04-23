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
		var errorMsg = new String("");
		var ipValue = new String("");

		ipValue = new String(document.snmpConfigForm.firstIPAddress.value);

		if (!isValidIPAddress(ipValue)) {
			errorMsg = ipValue + " is not a valid IP address!";
		}
		if (errorMsg == "") {
			ipValue = new String(document.snmpConfigForm.lastIPAddress.value);
			if (ipValue != "") {
				if (!isValidIPAddress(ipValue)) {
					errorMsg = ipValue + " is not a valid IP address!";
				}
			}
		}

		// 		if (errorMsg == "") {
		// 			var communityStringValue = new String(
		// 					document.snmpConfigForm.communityString.value);
		// 			if (communityStringValue == "") {
		// 				errorMsg = "Community String is required";
		// 			}
		// 		}

		if (errorMsg != "") {
			alert(errorMsg);
			return false;
		} else {
			return true;
		}
	}

	/*
	 * On Versoin change only the specificy section is shown.
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

<body onload="onVersionChange()">

	<div>
		<h3>Finished configuring SNMP</h3>

		<p>OpenNMS does not need to be restarted.</p>
	</div>

	<div>
		<h3>snmp-config.xml</h3>
		<p>
			<textarea style="width: 100%; height: 100px"><%=Files.toString(SnmpPeerFactory.getInstance().getFile(), Charsets.UTF_8).trim()%></textarea>
		</p>
	</div>

	<form method="post" name="snmpConfigGetForm"
		action="admin/snmpConfig?action=get">
		<h3>SnmpConfig lookup</h3>
		<div>
			<table>
				<tr>
					<td width="25%">Ip:</td>
					<td><input type="text" name="firstIPAddress" /></td>
				</tr>
				<tr>
				<tr>
					<td><input type="submit" value="Submit"></td>
					<td><input type="button" value="Cancel" onclick="cancel()">
				<tr>
			</table>
		</div>
	</form>

	<form method="post" name="snmpConfigForm"
		action="adin/snmpConfig?action=add"
		onsubmit="return verifySnmpConfig();">

		<div class="TwoColLAdmin">

			<h3>Please enter an IP or a range of IPs and the parameters you
				want to set below.</h3>

			<!--  General parameters -->
			<div id="general">
				<h3>General parameters</h3>
				<table>
					<tr>
						<td width="25%">Version:</td>
						<td width="50%"><select id="version" name="version"
							onChange="onVersionChange()">
								<option>v1</option>
								<option selected>v2c</option>
								<option>v3</option>
						</select> (Optional)</td>
					</tr>
					<tr>
						<td width="25%">First IP Address:</td>
						<td width="50%"><input size=15 name="firstIPAddress"></td>
					</tr>

					<tr>
						<td width="25%">Last IP Address:</td>
						<td width="50%"><input size=15 name="lastIPAddress">
							(Optional)</td>
					</tr>

					<tr>
						<td width="25%">Timeout:</td>
						<td width="50%"><input size=15 name="timeout">
							(Optional)</td>
					</tr>

					<tr>
						<td width="25%">Retries:</td>
						<td width="50%"><input size=15 name="retryCount">
							(Optional)</td>
					</tr>

					<tr>
						<td width="25%">Port:</td>
						<td width="50%"><input size=15 name="port">
							(Optional)</td>
					</tr>

					<tr>
						<td width="25%">Max Request Size:</td>
						<td width="50%"><input size=15 name="maxRequestSize">
							(Optional)</td>
					</tr>

					<tr>
						<td width="25%">Max Vars Per Pdu:</td>
						<td width="50%"><input size=15 name="maxVarsPerPdu">
							(Optional)</td>
					</tr>

					<tr>
						<td width="25%">Max Repetitions:</td>
						<td width="50%"><input size=15 name="maxRepetitions">
							(Optional)</td>
					</tr>

				</table>
			</div>
			<!-- v1/v2c parameters -->
			<div id="v1v2" style="visibility: hidden; display: none;">
				<h3>v1/v2c specific parameters</h3>
				<table>
					<tr>
						<td width="25%">Read Community String:</td>
						<td width="50%"><input size=30 name="readCommunityString">
							(Optional)</td>
					</tr>

					<tr>
						<td width="25%">Write Community String:</td>
						<td width="50%"><input size=30 name="writeCommunityString">
							(Optional)</td>
					</tr>
				</table>
			</div>

			<!--  v3 parameters -->
			<div id="v3" style="visibility: hidden; display: none;">
				<h3>v3 specific parameters</h3>
				<table>
					<tr>
						<td width="25%">Security Name:</td>
						<td width="50%"><input size=15 name="securityName" />
							(Optional)</td>
					</tr>

					<tr>
						<td width="25%">Security Level:</td>
						<td width="50%"><select name="securityLevel"
							style="width: 100px">
								<option selected />
								<option>noAuthNoPriv</option>
								<option>authNoPriv</option>
								<option>authPriv</option>
						</select> (Optional)</td>
					</tr>

					<tr>
						<td width="25%">Auth Passphrase:</td>
						<td width="50%"><input size=15 name="authPassPhrase" />
							(Optional)</td>
					</tr>

					<tr>
						<td width="25%">Auth Protocol:</td>
						<td width="50%"><select name="authProtocol"
							style="width: 100px">
								<option selected />
								<option>MD5</option>
								<option>SHA</option>
						</select> (Optional)</td>
					</tr>

					<tr>
						<td width="25%">Privacy Passphrase:</td>
						<td width="50%"><input size=15 name="privacyPassPhrase" />
							(Optional)</td>
					</tr>

					<tr>
						<td width="25%">Privacy Passphrase:</td>
						<td width="50%"><select name="privacyProtocol"
							style="width: 100px">
								<option selected />
								<option>AES</option>
								<option>MD5</option>
						</select> (Optional)</td>
					</tr>

					<tr>
						<td width="25%">Engine Id:</td>
						<td width="50%"><input size=15 name="engineId" /> (Optional)</td>
					</tr>

					<tr>
						<td width="25%">Context Engine Id:</td>
						<td width="50%"><input size=15 name="contextEngineId" />
							(Optional)</td>
					</tr>

					<tr>
						<td width="25%">Context Name:</td>
						<td width="50%"><input size=15 name="contextName" />
							(Optional)</td>
					</tr>
					<tr>
						<td width="25%">Enterprise Id:</td>
						<td width="50%"><input size=15 name="enterpriseId" />
							(Optional)</td>
					</tr>
				</table>
			</div>
			<!--  submit area -->
			<div>
				<table>
					<tr>
						<td><input type="submit" value="Submit"></td>
						<td><input type="button" value="Cancel" onclick="cancel()">
						</td>
					</tr>
				</table>
			</div>
		</div>

		<div class="TwoColRAdmin">
			<h3>Updating SNMP Community Names</h3>

			<p>In the boxes on the left, enter in a specific IP address and
				community string, or a range of IP addresses and a community string,
				and other SNMP parameters.</p>

			<p>OpenNMS will optimize this list, so enter the most generic
				first (i.e. the largest range) and the specific IP addresses last,
				because if a range is added that includes a specific IP address, the
				community name for the specific address will be changed to be that
				of the range.</p>

			<p>For devices that have already been provisioned and that have
				an event stating that data collection has failed because the
				community name changed, it may be necessary to update the SNMP
				information on the interface page for that device (by selecting the
				"Update SNMP" link) for these changes to take effect.</p>
		</div>
	</form>
	<jsp:include page="/includes/footer.jsp" flush="false" />
</body>
