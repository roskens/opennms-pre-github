package org.opennms.vaadin.applicationstack.provider;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import javax.xml.bind.JAXB;

import org.junit.Assert;
import org.junit.Test;
import org.opennms.vaadin.applicationstack.model.ApplicationLayer;
import org.opennms.vaadin.applicationstack.model.ApplicationStack;
import org.opennms.vaadin.applicationstack.model.ApplicationStacks;
import org.opennms.vaadin.applicationstack.model.Criteria;

/**
 *
 * @author marskuh
 */
public class ApplicationStacksProviderTest {

    @Test
    public void testLoad() {
        File configFile = new File("config.xml");
        configFile.delete(); // it must not be there
        Assert.assertTrue(!configFile.exists());
        
        ApplicationStacksProvider provider = new ApplicationStacksProvider("config.xml");
        ApplicationStacks stacks = provider.loadApplicationStacks();        
        
        Assert.assertEquals(stacks.getFirst(), stacks.getStack("Layer1"));
        Assert.assertNotNull(stacks);
        Assert.assertNotNull(stacks.getStacks());
        Assert.assertTrue(stacks.getStacks().isEmpty());
        Assert.assertTrue(configFile.exists()); // must exist now!
        
        configFile.delete();
    }
    
    @Test
    public void testSaveMethod() {
        File configFile = new File("config.xml");
        configFile.delete(); // it must not be there
        Assert.assertTrue(!configFile.exists());
        
        ApplicationStacksProvider provider = new ApplicationStacksProvider("config.xml");
        provider.saveApplicationStacks(createDummyApplicationStacks());
        ApplicationStack stack = provider.loadApplicationStacks().getFirst();
        Assert.assertNotNull(stack);
        Assert.assertEquals(stack, createDummyApplicationStacks().getFirst());
        
        int layers = stack.getLayers().size();
        stack.addLayer(new ApplicationLayer());
        
        provider.saveApplicationStack(stack);
        Assert.assertEquals(stack, provider.loadApplicationStacks().getFirst());
        Assert.assertEquals(layers+1, provider.loadApplicationStacks().getFirst().getLayers().size());
        configFile.delete();
    }
    
    @Test
    public void testNotEmptyConfiguration() throws IOException, URISyntaxException {
        File configFile = new File(getClass().getResource("/application-stacks.xml").toURI());
        Assert.assertTrue(configFile.exists());
        ApplicationStacks stacks = new ApplicationStacksProvider(configFile).loadApplicationStacks();
        Assert.assertTrue(!stacks.getFirst().getLayers().isEmpty());
        Assert.assertEquals(stacks, JAXB.unmarshal(configFile, ApplicationStacks.class));
    }
    
     private static ApplicationStacks createDummyApplicationStacks() {
        return new ApplicationStacks()
        	.addStack(new ApplicationStack("WAMA")
                .addLayer(new ApplicationLayer("Layer1", 0, 0, 4, 1)
                    .addCriteria(new Criteria(Criteria.EntityType.Categories, Criteria.Operator.Equals, "*")))
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
                .addLayer(new ApplicationLayer("Layer8b", 5, 1, 1, 2))
                .addLayer(new ApplicationLayer("Layer8c", 5, 2, 1, 2))
                .addLayer(new ApplicationLayer("Layer8d", 5, 3, 1, 2))
                .addLayer(new ApplicationLayer("Layer8e_1", 5, 4, 1, 1))
                .addLayer(new ApplicationLayer("Layer8e_2", 6, 4, 1, 1))
                .addLayer(new ApplicationLayer("Layer8f", 5, 5, 1, 2))
                .addLayer(new ApplicationLayer("Layer9", 7, 0, 6, 1))
                .addLayer(new ApplicationLayer("Layer10a", 8, 0, 3, 1))
                .addLayer(new ApplicationLayer("Layer10b", 8, 3, 3, 1))
                .addLayer(new ApplicationLayer("Layer11", 9, 0, 6, 1)));
    }
}