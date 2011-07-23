package org.opennms.netmgt.rrd;

public class RrdConstants {
    /**
     * RRDTool defined Data Source Types NOTE: "DERIVE" and "ABSOLUTE" not
     * currently supported.
     */
    public static final String DST_GAUGE = "GAUGE";
    public static final String DST_COUNTER = "COUNTER";
    public static final String DST_DERIVE = "DERIVE";
    public static final String DST_ABSOLUTE = "ABSOLUTE";

    /** Constant <code>MAX_DS_NAME_LENGTH=19</code> */
    public static final int MAX_DS_NAME_LENGTH = 19;

    /**
     * Defines the list of supported (MIB) object types which may be mapped to
     * one of the supported RRD data source types. Currently the only two
     * supported RRD data source types are: COUNTER & GAUGE. A simple string
     * comparison is performed against this list of supported types to determine
     * if an object can be represented by an RRD data source. NOTE: String
     * comparison uses String.startsWith() method so "counter32" & "counter64"
     * values will match "counter" entry. Comparison is case sensitive.
     */
    public static final String[] s_supportedObjectTypes = new String[] { "counter", "gauge", "timeticks", "integer", "octetstring" };
}