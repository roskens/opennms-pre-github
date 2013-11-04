package org.opennms.features.backup.light;

import com.google.common.hash.Hasher;
import com.google.common.hash.Hashing;
import com.google.common.io.ByteStreams;
import com.opennms.saas.endpoint.backup.api.model.ChunkInfo;
import com.opennms.saas.endpoint.backup.api.model.FileInfo;

import java.io.*;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipArchive {
    private String m_filename;
    private String m_basePath;
    private long m_maxChunkSize;
    private ZipOutputStream m_zipOutputStream;
    private Date m_creationDate;

    private Set<String> m_directories = new TreeSet<String>();

    public ZipArchive(String filename, String basePath, Date creationDate, long maxChunkSize) throws FileNotFoundException {
        this.m_filename = filename;
        this.m_basePath = basePath;
        this.m_maxChunkSize = maxChunkSize;
        this.m_creationDate = creationDate;
    }

    public void addDirectory(String directory) {
        m_directories.add(directory);
    }

    public void setDirectories(Set<String> directories) {
        m_directories = directories;
    }

    public void create() throws IOException {
        m_zipOutputStream = new ZipOutputStream(new FileOutputStream(new File(m_filename)));

        for (String directory : m_directories) {
            addFolder(m_basePath + File.separator + directory);
        }

        m_zipOutputStream.close();
    }

    private void addFolder(String path) throws IOException {
        File directory = new File(path);

        for (File file : directory.listFiles()) {
            if (file.isDirectory()) {
                addFolder(file.getAbsolutePath());
            } else {
                addFile(file.getAbsolutePath());
            }
        }
    }

    /**
     * Returns the relative path of a file.
     *
     * @param baseDirectory the base directory
     * @param filePath      the file path
     * @return the relative path
     */
    private String getRelativePath(File baseDirectory, File filePath) {
        return baseDirectory.toURI().relativize(filePath.toURI()).getPath();
    }

    private void addFile(String filename) throws IOException {
        ZipEntry zipEntry = new ZipEntry(getRelativePath(new File(m_basePath), new File(filename)));

        m_zipOutputStream.putNextEntry(zipEntry);

        byte[] buffer = new byte[65536];
        int read = 0;

        BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(filename));

        while ((read = bufferedInputStream.read(buffer)) != -1) {
            m_zipOutputStream.write(buffer, 0, read);
        }

        m_zipOutputStream.closeEntry();
        bufferedInputStream.close();
    }

    public long getFilesize() {
        File file = new File(m_filename);
        return file.length();
    }

    public FileInfo getFileInfo() throws IOException {
        List<ChunkInfo> chunkInfos = new ArrayList<ChunkInfo>();
        File file = new File(m_filename);

        BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(file));

        byte[] buffer = new byte[65536];
        int bytesRead;
        int currentChunkIndex = 0;
        int totalBytesRead = 0;

        Hasher hasher = Hashing.md5().newHasher();
        Hasher overallHasher = Hashing.md5().newHasher();

        do {
            int bytesToRead = buffer.length;

            if ((totalBytesRead + bytesToRead) / (int) m_maxChunkSize != currentChunkIndex) {
                bytesToRead = (currentChunkIndex + 1) * (int) m_maxChunkSize - totalBytesRead;
            }

            bytesRead = bufferedInputStream.read(buffer, 0, bytesToRead);

            if (bytesRead > 0) {
                hasher.putBytes(buffer, 0, bytesRead);
                overallHasher.putBytes(buffer, 0, bytesRead);

                totalBytesRead += bytesRead;
            }

            int newChunkIndex = totalBytesRead / (int) m_maxChunkSize;

            if (currentChunkIndex != newChunkIndex || bytesRead <= 0) {
                ChunkInfo chunkInfo = new ChunkInfo();
                chunkInfo.setPosition(currentChunkIndex);
                chunkInfo.setHash(hasher.hash().toString());

                chunkInfos.add(chunkInfo);

                if (bytesRead > 0) {
                    currentChunkIndex = newChunkIndex;
                    hasher = Hashing.md5().newHasher();
                }
            }
        } while (bytesRead > 0);

        FileInfo fileInfo = new FileInfo();
        fileInfo.setChunkInfos(chunkInfos);
        fileInfo.setHash(overallHasher.hash().toString());
        fileInfo.setFileSize(file.length());
        fileInfo.setCreated(m_creationDate);
        fileInfo.setName(file.getName());

        return fileInfo;
    }

    public InputStream getInputStreamForChunk(final int index) throws IOException {
        File file = new File(m_filename);

        return ByteStreams.limit(new FileInputStream(file) {
            {
                skip(index * m_maxChunkSize);
            }
        }, m_maxChunkSize);
    }
}
