package org.opennms.netmgt.dao;

import org.opennms.netmgt.config.invd.Scanner;
import org.opennms.netmgt.config.InvdPackage;

import java.util.Collection;

public interface InvdConfigDao {
    /**
     * Retrieves the number of threads that
     * @return The number of threads the scheduler should run.
     */
    public abstract int getSchedulerThreads();

    public abstract Collection<Scanner> getScanners();

    public abstract void rebuildPackageIpListMap();

    public abstract Collection<InvdPackage> getPackages();

    public abstract InvdPackage getPackage(String name);
}
