/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.opennms.netmgt.rrd.cassandra;

import com.netflix.astyanax.annotations.Component;

/**
 *
 * @author roskens
 */
public class DataPointColumn {
    @Component(ordinal=0) String m_dsName;
    @Component(ordinal=1) Long m_timestamp;
    public DataPointColumn(final String dsName, final Long timestamp) {
        m_dsName = dsName;
        m_timestamp = timestamp;
    }
}
