package org.opennms.vaadin.applicationstack.provider;

import java.io.Closeable;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import org.opennms.core.xml.JaxbUtils;
import org.opennms.vaadin.applicationstack.model.ApplicationStack;

/**
 * Loads and saves the ApplicationStack-Data to the filesystem.
 *
 * @author marskuh
 */
public class ApplicationStackProvider {

    private static void closeSilently(Closeable closeable) {
        if (closeable == null) return;
        try {
            closeable.close();
        } catch (IOException ioex) {
            ; // TODO exception handling
        }
    }
   
    private final File configFile;

    public ApplicationStackProvider(String configFile) {
        this(new File(configFile));
    }
    
    public ApplicationStackProvider(File configFile) {
        this.configFile = configFile;
    }

    public ApplicationStack loadApplicationStack() {
        if (!configFile.exists()) {
            saveApplicationStack(new ApplicationStack()); // DEFAULT
        }
        return JaxbUtils.unmarshal(ApplicationStack.class, configFile);
    }

    public void saveApplicationStack(final ApplicationStack stack) {
        FileWriter out = null;
        try {
            out = new FileWriter(configFile);
            JaxbUtils.marshal(stack, out);
        } catch (IOException ioEx) {
            ; //TODO exception handling
        } finally {
            closeSilently(out);
        }
    }
}
