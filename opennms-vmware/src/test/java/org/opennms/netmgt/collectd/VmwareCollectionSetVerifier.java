package org.opennms.netmgt.collectd;

import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.Assert;
import org.opennms.core.utils.LogUtils;
import org.opennms.netmgt.config.collector.AttributeGroup;
import org.opennms.netmgt.config.collector.CollectionAttribute;
import org.opennms.netmgt.config.collector.CollectionResource;
import org.opennms.netmgt.config.collector.CollectionSet;
import org.opennms.netmgt.config.collector.CollectionSetVisitor;

public class VmwareCollectionSetVerifier implements CollectionSetVisitor {

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
        LogUtils.infof(this, "Found %d attributes for group %s.", group.getAttributes().size(), group.getName());
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
