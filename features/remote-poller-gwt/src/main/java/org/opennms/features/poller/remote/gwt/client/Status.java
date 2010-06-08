/**
 * 
 */
package org.opennms.features.poller.remote.gwt.client;

import java.io.Serializable;

import com.google.gwt.user.client.rpc.IsSerializable;

public enum Status implements Serializable, IsSerializable {
	UP             { public String getColor() { return "#00ff00"; } public String getStyle() { return "statusUp"; } },
	MARGINAL       { public String getColor() { return "#ffff00"; } public String getStyle() { return "statusMarginal"; } },
	DOWN           { public String getColor() { return "#ff0000"; } public String getStyle() { return "statusDown"; } },
	UNKNOWN        { public String getColor() { return "#0000ff"; } public String getStyle() { return "statusUnknown"; } },
	UNINITIALIZED  { public String getColor() { return "#dddddd"; } public String getStyle() { return "statusUninitialized"; } };

	abstract public String getColor();
	abstract public String getStyle();
}