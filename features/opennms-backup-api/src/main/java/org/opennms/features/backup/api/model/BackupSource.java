package org.opennms.features.backup.api.model;

import java.io.InputStream;
import java.util.Set;

public interface BackupSource {
    public Set<String> fullFiles();

    public boolean containsFullFile(String filename);

    public InputStream getInputStreamForFullFile(String filename);
}
