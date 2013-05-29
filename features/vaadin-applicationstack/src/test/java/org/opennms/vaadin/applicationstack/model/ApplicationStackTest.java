package org.opennms.vaadin.applicationstack.model;

import junit.framework.Assert;
import org.junit.Test;

/**
 *
 * @author mvrueden
 */
public class ApplicationStackTest {

    @Test
    public void testBasicFunctions() {
        ApplicationStack stack = new ApplicationStack("Waschmaschine")
                .addLayer(new ApplicationLayer("Layer1", 0, 0, 4, 1))
                .addLayer(new ApplicationLayer("Layer2a", 1, 0, 2, 1))
                .addLayer(new ApplicationLayer("Layer2b", 1, 2, 2, 1))
                .addLayer(new ApplicationLayer("Layer3", 2, 0, 4, 1))
                .addLayer(new ApplicationLayer("Layer4", 0, 4, 1, 3));
        
        Assert.assertEquals(5, stack.getColumnCount());
        Assert.assertEquals(3, stack.getRowCount());
        Assert.assertEquals("Waschmaschine", stack.getLabel());
        Assert.assertEquals(1000, stack.computeWidth());
    }
    
    @Test
    public void testBasicFunctions2() {
         ApplicationStack stack = new ApplicationStack("Waschmaschine")
                .addLayer(new ApplicationLayer("Layer1", 0, 0, 4, 1))
                .addLayer(new ApplicationLayer("Layer2a", 1, 0, 2, 1))
                .addLayer(new ApplicationLayer("Layer2b", 1, 2, 2, 1))
                .addLayer(new ApplicationLayer("Layer3", 2, 0, 4, 1))
                .addLayer(new ApplicationLayer("Layer4", 0, 4, 1, 3))
                .addLayer(new ApplicationLayer("Layer5", 0, 5, 1, 3))
                .addLayer(new ApplicationLayer("Layer6a", 3, 0, 2, 1))
                .addLayer(new ApplicationLayer("Layer6b", 3, 2, 1, 1))
                .addLayer(new ApplicationLayer("Layer6c", 3, 3, 1, 1))
                .addLayer(new ApplicationLayer("Layer6d", 3, 4, 2, 1))
                .addLayer(new ApplicationLayer("Layer7", 4, 0, 6, 1))
                .addLayer(new ApplicationLayer("Layer8a", 5, 0, 1, 2))
                .addLayer(new ApplicationLayer("Layer8_1a", 5, 1, 4, 1))
//                .addLayer(new ApplicationLayer("Layer8_2a", 6, 1, 2, 1))
//                .addLayer(new ApplicationLayer("Layer8_1b", 5, 3, 2, 1))
//                .addLayer(new ApplicationLayer("Layer8_2b", 6, 3, 2, 1))
                .addLayer(new ApplicationLayer("Layer8b", 5, 5, 1, 2));
           Assert.assertEquals(6, stack.getColumnCount());
        Assert.assertEquals(7, stack.getRowCount());
        Assert.assertEquals("Waschmaschine", stack.getLabel());
        Assert.assertEquals(1000, stack.computeWidth());
    }
}
