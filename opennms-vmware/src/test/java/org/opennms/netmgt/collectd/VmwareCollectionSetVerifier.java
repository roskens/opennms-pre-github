package org.opennms.netmgt.collectd;

import org.junit.Assert;
import org.opennms.netmgt.config.collector.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VmwareCollectionSetVerifier implements CollectionSetVisitor {
    /**
     * logging for VMware library VI Java
     */
    private final Logger logger = LoggerFactory.getLogger("OpenNMS.VMware." + VmwareCollectionSetVerifier.class.getName());

    @Override
    public void visitCollectionSet(CollectionSet set) {
        // TODO Auto-generated method stub
    }

    @Override
    public void visitResource(CollectionResource resource) {
        // TODO Auto-generated method stub
    }

    @Override
    public void visitGroup(AttributeGroup group) {
        logger.info("Found '{}' attributes for group '{}'", group.getAttributes().size(), group.getName());
        Assert.assertTrue(group.getAttributes().size() > 1);
    }

    @Override
    public void visitAttribute(CollectionAttribute attribute) {
        // TODO Auto-generated method stub
    }

    @Override
    public void completeAttribute(CollectionAttribute attribute) {
        // TODO Auto-generated method stub
    }

    @Override
    public void completeGroup(AttributeGroup group) {
        // TODO Auto-generated method stub
    }

    @Override
    public void completeResource(CollectionResource resource) {
        // TODO Auto-generated method stub
    }

    @Override
    public void completeCollectionSet(CollectionSet set) {
        // TODO Auto-generated method stub
    }

}
