package org.opennms.features.backup.api.model;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Enumeration;
import java.util.Set;
import java.util.TreeSet;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class ZipBackup implements BackupTarget, Comparable<ZipBackup> {
    private String zipFilename;
    private long timestamp;

    public ZipBackup(String zipFilename) {
        this.zipFilename = zipFilename;
        this.timestamp = Long.valueOf(this.zipFilename.split("\\.")[1]);
    }

    public int compareTo(ZipBackup zipBackup) {
        if (getTimestamp() < zipBackup.getTimestamp()) {
            return -1;
        }
        if (getTimestamp() > zipBackup.getTimestamp()) {
            return +1;
        }
        return 0;
    }

    public String getFilename() {
        return zipFilename;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public Date getDate() {
        Date date = new Date();
        date.setTime(timestamp);
        return date;
    }

    public Set<String> diffFiles() {
        Set<String> files = new TreeSet<String>();

        try {
            ZipFile zipFile = new ZipFile(zipFilename);

            Enumeration zipEntries = zipFile.entries();

            while (zipEntries.hasMoreElements()) {
                ZipEntry zipEntry = (ZipEntry) zipEntries.nextElement();

                if (zipEntry.getName().startsWith("diff/")) {
                    files.add(zipEntry.getName().substring(5));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return files;
    }

    public Set<String> fullFiles() {
        Set<String> files = new TreeSet<String>();

        try {
            ZipFile zipFile = new ZipFile(zipFilename);

            Enumeration zipEntries = zipFile.entries();

            while (zipEntries.hasMoreElements()) {
                ZipEntry zipEntry = (ZipEntry) zipEntries.nextElement();

                if (zipEntry.getName().startsWith("full/")) {
                    files.add(zipEntry.getName().substring(5));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return files;
    }

    public boolean containsFullFile(String filename) {
        try {
            ZipFile zipFile = new ZipFile(zipFilename);
            ZipEntry zipEntry = zipFile.getEntry("full/" + filename);

            return (zipEntry != null);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean containsDiffFile(String filename) {
        try {
            ZipFile zipFile = new ZipFile(zipFilename);
            ZipEntry zipEntry = zipFile.getEntry("diff/" + filename);

            return (zipEntry != null);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    public InputStream getInputStreamForFullFile(String filename) {
        try {
            ZipFile zipFile = new ZipFile(zipFilename);
            ZipEntry zipEntry = zipFile.getEntry("full/" + filename);

            if (zipEntry != null) {
                return zipFile.getInputStream(zipEntry);
            } else {
                System.out.println(filename + " not found in " + zipFilename);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public InputStream getInputStreamForDiffFile(String filename) {
        try {
            ZipFile zipFile = new ZipFile(zipFilename);
            ZipEntry zipEntry = zipFile.getEntry("diff/" + filename);

            if (zipEntry != null) {
                return zipFile.getInputStream(zipEntry);
            } else {
                System.out.println(filename + " not found in " + zipFilename);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
