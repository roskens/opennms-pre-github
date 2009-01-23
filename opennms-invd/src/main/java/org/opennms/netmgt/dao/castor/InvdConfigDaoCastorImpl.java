package org.opennms.netmgt.dao.castor;

import org.opennms.netmgt.dao.InvdConfigDao;
import org.opennms.netmgt.config.invd.*;
import org.opennms.netmgt.config.invd.Package;
import org.opennms.netmgt.config.CollectdConfigFactory;
import org.opennms.netmgt.config.InvdConfig;
import org.opennms.netmgt.config.InvdConfigFactory;
import org.opennms.netmgt.config.InvdPackage;
import org.opennms.core.utils.ThreadCategory;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;
import org.apache.log4j.Category;

import java.util.Collection;
import java.lang.reflect.UndeclaredThrowableException;
import java.io.IOException;

public class InvdConfigDaoCastorImpl implements InvdConfigDao {
    public InvdConfigDaoCastorImpl() {
        loadConfigFactory();

    }

    public Category log() {
        return ThreadCategory.getInstance(getClass());
    }

    private InvdConfig getConfig() {
        return InvdConfigFactory.getInstance().getInvdConfig();
    }

    private void loadConfigFactory() {
        // Load collectd configuration file
        try {
            // XXX was reload(); this doesn't work well from unit tests, however
            CollectdConfigFactory.init();
        } catch (MarshalException ex) {
            log().fatal("loadConfigFactory: Failed to load collectd configuration", ex);
            throw new UndeclaredThrowableException(ex);
        } catch (ValidationException ex) {
            log().fatal("loadConfigFactory: Failed to load collectd configuration", ex);
            throw new UndeclaredThrowableException(ex);
        } catch (IOException ex) {
            log().fatal("loadConfigFactory: Failed to load collectd configuration", ex);
            throw new UndeclaredThrowableException(ex);
        }
    }
    
    public int getSchedulerThreads() {
        return getConfig().getThreads();
    }

    public Collection<Scanner> getScanners() {
        return getConfig().getConfig().getScannerCollection();
    }

    public void rebuildPackageIpListMap() {
        getConfig().createPackageIpListMap();
    }

    public Collection<InvdPackage> getPackages() {
        return getConfig().getPackages();
    }

    public InvdPackage getPackage(String name) {
        return getConfig().getPackage(name);
    }
}
