package org.opennms.vaadin.applicationstack.provider;

import java.io.File;

/**
 *
 * @author marskuh
 */
public class ApplicationStackProviderFactory {
    private static final String FILE_NAME = "application-stacks.xml";
    
    public static final ApplicationStackProviderFactory instance = new ApplicationStackProviderFactory();
    
    private ApplicationStackProviderFactory() {}
    
    public ApplicationStackProvider createApplicationStackProvider() {
        return new ApplicationStackProvider(
                new File(System.getProperty("opennms.home"), "etc/" + FILE_NAME));
    }
}
