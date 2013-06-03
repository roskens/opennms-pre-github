package org.opennms.vaadin.applicationstack.provider;

import java.io.File;

/**
 *
 * @author marskuh
 */
public class ApplicationStacksProviderFactory {
    private static final String FILE_NAME = "application-stacks.xml";
    
    public static final ApplicationStacksProviderFactory instance = new ApplicationStacksProviderFactory();
    private static ApplicationStacksProvider providerInstance;
    
    private ApplicationStacksProviderFactory() {}
    
    private ApplicationStacksProvider createApplicationStacksProvider() {
    	return new ApplicationStacksProvider(
    			new File(System.getProperty("opennms.home"), "etc/" + FILE_NAME));
    }
    
    public ApplicationStacksProvider getApplicationStacksProvider() {
    	if (providerInstance == null)
    		providerInstance = createApplicationStacksProvider();
        return providerInstance;
    }
}
