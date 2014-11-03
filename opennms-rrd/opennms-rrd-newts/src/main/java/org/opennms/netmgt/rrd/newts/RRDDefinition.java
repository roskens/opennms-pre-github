/******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2015 The OpenNMS Group, Inc.
 * OpenNMS(R) is Copyright (C) 1999-2015 The OpenNMS Group, Inc.
 *
 * OpenNMS(R) is a registered trademark of The OpenNMS Group, Inc.
 *
 * OpenNMS(R) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version.
 *
 * OpenNMS(R) is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with OpenNMS(R).  If not, see:
 *      http://www.gnu.org/licenses/
 *
 * For more information contact:
 *     OpenNMS(R) Licensing <license@opennms.org>
 *     http://www.opennms.org/
 *     http://www.opennms.com/
 ******************************************************************************/
package org.opennms.netmgt.rrd.newts;

import java.io.File;
import java.util.List;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.opennms.netmgt.rrd.RrdDataSource;
import org.opennms.netmgt.rrd.RrdUtils;

/**
 *
 * @author roskens
 */
public class RRDDefinition {

    final private String m_directory;
    final private String m_rrdName;
    final private int m_step;
    final List<RrdDataSource> m_dataSources;
    final List<String> m_rraList;

    RRDDefinition(final String directory, final String rrdName, final int step, final List<RrdDataSource> dataSources, final List<String> rraList) {
        m_directory = directory;
        m_rrdName = rrdName;
        m_step = step;
        m_dataSources = dataSources;
        m_rraList = rraList;
    }

    public String getPath() {
        return m_directory + File.separator + m_rrdName + RrdUtils.getExtension();
    }

    public String getDirectory() {
        return m_directory;
    }

    public String getRRDName() {
        return m_rrdName;
    }

    // unused
    public List<RrdDataSource> getDataSources() {
        return m_dataSources;
    }

    // unused
    public List<String> getRRAList() {
        return m_rraList;
    }

    public void saveDefinition(final File file) throws ConfigurationException {
        PropertiesConfiguration pc = new PropertiesConfiguration();
        int ds_num = 0;
        pc.addProperty("step", Integer.toString(m_step));
        for (RrdDataSource rds : m_dataSources) {
            pc.addProperty("ds." + ds_num + ".name", rds.getName());
            pc.addProperty("ds." + ds_num + ".type", rds.getType());
            pc.addProperty("ds." + ds_num + ".heartbeat", Integer.toString(rds.getHeartBeat()));
            pc.addProperty("ds." + ds_num + ".min", rds.getMin());
            pc.addProperty("ds." + ds_num + ".max", rds.getMax());
            ds_num++;
        }
        int rra_num = 0;
        for (String rra : m_rraList) {
            pc.addProperty("rra." + rra_num, rra);
            rra_num++;
        }

        pc.save(file);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("NewtsDef[");
        sb.append("directory=").append(m_directory);
        sb.append(", rrdName=").append(m_rrdName);
        sb.append(", step=").append(m_step);
        sb.append(", dataSources=").append(m_dataSources);
        sb.append(", rraList=").append(m_rraList);
        sb.append("]");

        return sb.toString();
    }
}
