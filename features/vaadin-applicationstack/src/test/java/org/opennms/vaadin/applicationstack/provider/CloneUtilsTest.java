/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.opennms.vaadin.applicationstack.provider;

import java.io.File;
import java.net.URISyntaxException;
import javax.xml.bind.JAXB;
import junit.framework.Assert;
import org.junit.Test;
import org.opennms.vaadin.applicationstack.model.ApplicationStack;
import org.opennms.vaadin.applicationstack.model.ApplicationStacks;

/**
 *
 * @author marskuh
 */
public class CloneUtilsTest {
    
    @Test
    public void testCloneApplicationStack() throws URISyntaxException {
        ApplicationStack stack = JAXB.unmarshal(
                    new File(getClass().getResource("/application-stacks.xml").toURI()), 
                    ApplicationStacks.class)
                .getFirst();
        Assert.assertEquals(stack, CloneUtils.clone(stack));
    }
}