package org.opennms.features.backup.api.server;


import org.apache.commons.io.FileUtils;
import org.badiff.FileDiffs;
import org.badiff.imp.FileDiff;
import org.opennms.features.backup.api.model.ZipBackup;
import org.opennms.features.backup.api.rest.RestBackupSet;
import org.opennms.features.backup.api.rest.RestZipBackup;
import org.opennms.features.backup.api.rest.RestZipBackupContents;

import javax.xml.bind.JAXB;
import java.io.*;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

public class BackupSet {
    private String baseDirectory;
    private TreeSet<ZipBackup> zipBackupFiles = null;

    public BackupSet(String baseDirectory) {
        this.baseDirectory = baseDirectory;

        searchBackupFiles();
    }

    private void searchBackupFiles() {
        zipBackupFiles = new TreeSet<ZipBackup>();

        File directory = new File(baseDirectory);

        if (!directory.exists()) {
            return;
        }

        for (File file : directory.listFiles()) {
            if (!file.isDirectory()) {
                if (file.getName().matches("backup\\.[0-9]+\\.zip")) {
                    ZipBackup zipBackup = new ZipBackup(file.getAbsolutePath());
                    zipBackupFiles.add(zipBackup);
                }
            }
        }
    }

    public ZipBackup getZipBackup(long timestamp) {
        for (ZipBackup zipBackup : zipBackupFiles) {
            if (zipBackup.getTimestamp() == timestamp) {
                return zipBackup;
            }
        }
        return null;
    }

    public ZipBackup getZipBackup(String timestampString) {

        long timestamp = Long.valueOf(timestampString);

        for (ZipBackup zipBackup : zipBackupFiles) {
            if (zipBackup.getTimestamp() == timestamp) {
                return zipBackup;
            }
        }
        return null;
    }

    public Set<ZipBackup> getZipBackupFiles() {
        return zipBackupFiles;
    }

    private boolean writeFile(InputStream inputStream, File file) {
        File parent = file.getParentFile();

        if (!parent.exists() && !parent.mkdirs()) {
            return false;
        }

        try {
            byte[] buffer = new byte[65536];

            FileOutputStream fileOutputStream = new FileOutputStream(file);

            int read = 0;

            while ((read = inputStream.read(buffer)) != -1) {
                fileOutputStream.write(buffer, 0, read);
            }

            fileOutputStream.close();
            inputStream.close();
        } catch (IOException ioException) {
            ioException.printStackTrace();
            return false;
        }

        return true;
    }

    public boolean restoreFile(ZipBackup zipBackup, String restoreDirectory, String filename) {
        if (zipBackup.containsFullFile(filename)) {
            return writeFile(zipBackup.getInputStreamForFullFile(filename), new File(restoreDirectory + "/" + filename));
        } else {
            try {
                TreeSet<ZipBackup> olderBackups = new TreeSet<ZipBackup>(zipBackupFiles.headSet(zipBackup, true));

                ZipBackup lastFullBackup = null;

                for (ZipBackup zipBackupEntry : olderBackups) {
                    if (zipBackupEntry.containsFullFile(filename)) {
                        lastFullBackup = zipBackupEntry;
                    }
                }

                if (lastFullBackup == null) {
                    System.out.println("Error no full backup found for file '" + filename + "'");
                    return false;
                }

                SortedSet<ZipBackup> exactBackupSet = olderBackups.tailSet(lastFullBackup, false);

                File origFile = File.createTempFile("tmp", ".orig", new File(restoreDirectory));
                File diffFile = File.createTempFile("tmp", ".diff", new File(restoreDirectory));

                writeFile(lastFullBackup.getInputStreamForFullFile(filename), origFile);

                for (ZipBackup zipBackupEntry : exactBackupSet) {
                    writeFile(zipBackupEntry.getInputStreamForDiffFile(filename), diffFile);
                    File patchedFile = FileDiffs.apply(origFile, new FileDiff(diffFile));

                    FileUtils.deleteQuietly(diffFile);
                    FileUtils.deleteQuietly(origFile);
                    FileUtils.moveFile(patchedFile, origFile);
                }

                File targetFile = new File(restoreDirectory + "/" + filename);
                FileUtils.deleteQuietly(targetFile);
                FileUtils.moveFile(origFile, targetFile);
                FileUtils.deleteQuietly(diffFile);
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }

        return true;
    }

    public ZipBackup last() {
        return zipBackupFiles.last();
    }

    public ZipBackup first() {
        return zipBackupFiles.first();
    }

    public boolean validateFile(ZipBackup zipBackup, String filename) {
        if (zipBackup.containsFullFile(filename)) {
            return true;
        } else {
            TreeSet<ZipBackup> olderBackups = new TreeSet<ZipBackup>(zipBackupFiles.headSet(zipBackup, true));

            ZipBackup lastFullBackup = null;

            for (ZipBackup zipBackupEntry : olderBackups) {
                if (zipBackupEntry.containsFullFile(filename)) {
                    lastFullBackup = zipBackupEntry;
                }
            }

            if (lastFullBackup == null) {
                return false;
            }

            SortedSet<ZipBackup> exactBackupSet = olderBackups.tailSet(lastFullBackup, false);

            if (!lastFullBackup.containsFullFile(filename)) {
                return false;
            }

            for (ZipBackup zipBackupEntry : exactBackupSet) {
                if (!zipBackupEntry.containsDiffFile(filename)) {
                    return false;
                }
            }
        }

        return true;
    }

    public String xml() {
        RestBackupSet restBackupSet = new RestBackupSet();

        for (ZipBackup zipBackup : zipBackupFiles) {
            restBackupSet.getZipBackups().add(new RestZipBackup(zipBackup.getTimestamp()));
        }

        StringWriter stringWriter = new StringWriter();

        JAXB.marshal(restBackupSet, stringWriter);

        return stringWriter.getBuffer().toString();
    }

    public String getFilesXml(ZipBackup zipBackup) {
        Set<String> files = getFiles(zipBackup);

        RestZipBackupContents restZipBackupContents = new RestZipBackupContents(zipBackup.getTimestamp(), files);

        StringWriter stringWriter = new StringWriter();

        JAXB.marshal(restZipBackupContents, stringWriter);

        return stringWriter.getBuffer().toString();
    }

    public boolean validate(ZipBackup zipBackup) {
        boolean result = true;
        for (String filename : getFiles(zipBackup)) {
            result &= validateFile(zipBackup, filename);
        }
        return result;
    }

    public Set<String> getFiles(ZipBackup zipBackup) {
        Set<String> files = new TreeSet<String>();

        for (String filename : zipBackup.fullFiles()) {
            files.add(filename);
        }

        for (String filename : zipBackup.diffFiles()) {
            files.add(filename);
        }

        return files;
    }
}
