package org.opennms.features.backup.api.model;

import java.io.InputStream;
import java.util.Set;

public interface BackupTarget extends BackupSource {
    public Set<String> diffFiles();

    public boolean containsDiffFile(String filename);

    public InputStream getInputStreamForDiffFile(String filename);
}
