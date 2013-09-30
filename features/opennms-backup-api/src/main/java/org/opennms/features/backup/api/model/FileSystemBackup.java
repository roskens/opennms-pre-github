package org.opennms.features.backup.api.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;

public class FileSystemBackup implements BackupSource {
    private String fileSystemBasePath;
    private Set<String> directories = new TreeSet<String>();

    public FileSystemBackup(String fileSystemBasePath, String[] directories) {
        this.fileSystemBasePath = fileSystemBasePath;
        this.directories.addAll(Arrays.asList(directories));
    }

    public FileSystemBackup(String fileSystemBasePath, Set<String> directories) {
        this.fileSystemBasePath = fileSystemBasePath;
        this.directories.addAll(directories);
    }

    private String getRelativePath(File baseDirectory, File filePath) {
        return baseDirectory.toURI().relativize(filePath.toURI()).getPath();
    }

    private Set<String> searchFiles(String path) {
        Set<String> files = new TreeSet<String>();

        File directory = new File(path);

        for (File file : directory.listFiles()) {
            if (file.isDirectory()) {
                files.addAll(searchFiles(file.getAbsolutePath()));
            } else {
                files.add(getRelativePath(new File(fileSystemBasePath), file));
            }
        }

        return files;
    }

    public Set<String> fullFiles() {
        Set<String> files = new TreeSet<String>();

        for (String directory : directories) {
            files.addAll(searchFiles(fileSystemBasePath + "/" + directory));
        }

        return files;
    }

    public boolean containsFullFile(String filename) {
        File file = new File(fileSystemBasePath + "/" + filename);
        return file.exists();
    }

    public InputStream getInputStreamForFullFile(String filename) {
        try {
            return new FileInputStream(new File(fileSystemBasePath + "/" + filename));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }
}
