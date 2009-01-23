package org.opennms.netmgt.invd.jmx;

import org.opennms.netmgt.daemon.AbstractSpringContextJmxServiceDaemon;


public class Invd extends AbstractSpringContextJmxServiceDaemon<org.opennms.netmgt.invd.Invd>
                  implements InvdMBean {

    @Override
    protected String getLoggingPrefix() {
        return "OpenNMS.Invd";
    }

    @Override
    protected String getSpringContext() {
        return "invdContext";
    }
}
