package org.opennms.vaadin.applicationstack.provider;

import java.io.File;
import javax.xml.bind.JAXB;
import org.opennms.vaadin.applicationstack.model.ApplicationStack;

/**
 * Loads and saves the ApplicationStack-Data to the filesystem.
 *
 * @author marskuh
 */
public class ApplicationStackProvider {

    private final File configFile;

    public ApplicationStackProvider(String configFile) {
        this(new File(configFile));
    }
    
    public ApplicationStackProvider(File configFile) {
        this.configFile = configFile;
    }

    synchronized public ApplicationStack loadApplicationStack() {
        if (!configFile.exists()) {
            saveApplicationStack(new ApplicationStack()); // DEFAULT
        }
        return JAXB.unmarshal(configFile, ApplicationStack.class);
    }

    synchronized public void saveApplicationStack(final ApplicationStack stack) {
        JAXB.marshal(stack, configFile);
    }
}
