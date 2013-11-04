package org.opennms.features.backup.light;

import com.opennms.saas.endpoint.backup.api.model.*;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.glassfish.jersey.media.multipart.MultiPartFeature;

import javax.ws.rs.client.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.*;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class BackupClient {

    private LocalBackupConfig m_localBackupConfig;

    public BackupClient(LocalBackupConfig localBackupConfig) {
        m_localBackupConfig = localBackupConfig;
    }

    public void setLocalBackupConfig(LocalBackupConfig localBackupConfig) {
        m_localBackupConfig = localBackupConfig;
    }

    public BackupConfig lookupBackupConfig() {
        BackupConfig backupConfig;
        try {
            backupConfig = lookupRemoteBackupConfig();
        } catch (Exception e) {
            throw new BackupClientException("Lookup of remote configuration failed", e);
        }
        return backupConfig;
    }

    public BackupInfo updateBackupInfo(BackupInfo backupInfo) {
        return remoteInfo(backupInfo);
    }

    public BackupInfo prepare() {
        BackupInfo backupInfo = null;

        try {
            backupInfo = (BackupInfo) remoteInitalizeBackup();
        } catch (Exception e) {
            throw new BackupClientException("Initializing of backup failed", e);
        }

        return backupInfo;
    }

    public ZipArchive create(BackupConfig backupConfig) throws IOException {
        Date creationDate = Calendar.getInstance().getTime();
        ZipArchive zipArchive = new ZipArchive(m_localBackupConfig.getLocalBackupDirectory() + File.separator + "backup." + creationDate.getTime() + ".zip", m_localBackupConfig.getBaseDirectory(), creationDate, backupConfig.getMaxChunkSize());
        zipArchive.setDirectories(m_localBackupConfig.getDirectories());
        zipArchive.create();

        if (zipArchive.getFilesize() > backupConfig.getMaxFileSize()) {
            throw new BackupClientException("Filesize of local backup exceeds maximum file upload limit of remote server");
        }

        return zipArchive;
    }

    public void upload(final BackupConfig backupConfig, final BackupInfo backupInfo, final ZipArchive zipArchive) throws IOException {
        FileInfo fileInfo = zipArchive.getFileInfo();

        List<ChunkInfo> chunkInfoList = fileInfo.getChunkInfos();
        int c = 0;

        Response response = remoteInformAboutFiles(backupInfo, fileInfo);

        if (response.getStatus() != 200) {
            throw new BackupClientException("Remote server returned status code " + response.getStatus());
        }

        int numberOfConcurrentUploads = Math.min(m_localBackupConfig.getMaxConcurrentUploads(), backupConfig.getMaxConcurrentUploads());

        System.out.println(numberOfConcurrentUploads);

        ExecutorService executorService = Executors.newFixedThreadPool(numberOfConcurrentUploads);

        for (final ChunkInfo chunkInfo : chunkInfoList) {
            System.out.println(chunkInfo.getPosition() + " - " + chunkInfo.getHash());

            final int chunkToUpload = c;

            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        InputStream inputStream = zipArchive.getInputStreamForChunk(chunkToUpload);
                        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream("/Users/chris/Desktop/backup/test" + chunkToUpload));

                        byte[] buffer = new byte[65536];
                        int read = 0;


                        while ((read = inputStream.read(buffer)) != -1) {
                            bufferedOutputStream.write(buffer, 0, read);
                        }

                        inputStream.close();
                        bufferedOutputStream.close();

                        Response uploadResponse = remoteUpload(backupInfo, chunkInfo, zipArchive.getInputStreamForChunk(chunkToUpload));

                        if (uploadResponse.getStatus() != 200) {
                            // TODO error handling
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });

            c++;
        }

        executorService.shutdown();

        BackupInfo backupInfo1 = remoteInfo(backupInfo);

        for (ChunkInfo chunkInfo1 : backupInfo1.getSuccessfullyReceivedChunks()) {
            System.out.println("Chunk #" + chunkInfo1.getPosition() + " received");
        }
    }

    public BackupState getBackupState(BackupInfo backupInfo) {
        return remoteInfo(backupInfo).getState();
    }

    public BackupInfo finish(BackupInfo backupInfo) {
        return remoteFinishBackup(backupInfo);
    }

    private BackupConfig lookupRemoteBackupConfig() {
        return remoteGetConfig();
    }

    private BackupInfo remoteInfo(BackupInfo backupInfo) {
        Client client = ClientBuilder.newClient();

        WebTarget webTarget = client.target(m_localBackupConfig.getBackupServiceLocation())
                .path(m_localBackupConfig.getKeyId())
                .path(backupInfo.getId());

        Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON_TYPE);

        Response response = invocationBuilder.get();

        return response.readEntity(BackupInfo.class);
    }

    private BackupConfig remoteGetConfig() {
        Client client = ClientBuilder.newClient();

        WebTarget webTarget = client.target(m_localBackupConfig.getBackupServiceLocation())
                .path(m_localBackupConfig.getKeyId())
                .path("config");

        Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON_TYPE);

        Response response = invocationBuilder.accept(MediaType.APPLICATION_JSON_TYPE).get();

        return response.readEntity(BackupConfig.class);
    }

    private BackupInfo remoteFinishBackup(BackupInfo backupInfo) {
        Client client = ClientBuilder.newClient();
        WebTarget webTarget = client.target(m_localBackupConfig.getBackupServiceLocation())
                .path(m_localBackupConfig.getKeyId())
                .path(backupInfo.getId())
                .path("finish");

        Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON_TYPE);

        Response response = invocationBuilder.post(null);

        return response.readEntity(BackupInfo.class);
    }

    private BackupInfo remoteInitalizeBackup() {
        Client client = ClientBuilder.newClient();
        WebTarget webTarget = client.target(m_localBackupConfig.getBackupServiceLocation())
                .path(m_localBackupConfig.getKeyId());

        Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON_TYPE);

        Response response = invocationBuilder.post(null);

        return response.readEntity(BackupInfo.class);
    }

    private Response remoteInformAboutFiles(BackupInfo backupInfo, FileInfo fileInfo) {
        Client client = ClientBuilder.newBuilder().register(MultiPartFeature.class).build();

        WebTarget webTarget = client.target(m_localBackupConfig.getBackupServiceLocation())
                .path(m_localBackupConfig.getKeyId())
                .path(backupInfo.getId())
                .path("fileInfo");

        Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON_TYPE);

        Response response = invocationBuilder.post(Entity.entity(fileInfo, MediaType.APPLICATION_JSON_TYPE));

        return response;
    }

    private Response remoteUpload(BackupInfo backupInfo, ChunkInfo chunkInfo, InputStream inputStream) {
        FormDataMultiPart formDataMultiPart = new FormDataMultiPart();
        formDataMultiPart.bodyPart(new FormDataBodyPart("file", inputStream, MediaType.APPLICATION_OCTET_STREAM_TYPE));

        Client client = ClientBuilder.newBuilder().register(MultiPartFeature.class).build();

        WebTarget webTarget = client.target(m_localBackupConfig.getBackupServiceLocation())
                .path(m_localBackupConfig.getKeyId())
                .path(backupInfo.getId())
                .path(chunkInfo.getHash());

        Invocation.Builder invocationBuilder = webTarget.request();

        return invocationBuilder.post(Entity.entity(formDataMultiPart, MediaType.MULTIPART_FORM_DATA_TYPE));
    }

    public static void main(String args[]) {

        LocalBackupConfig localBackupConfig = new LocalBackupConfig();
        localBackupConfig.setKeyId("8062629b-d401-426c-b7b3-77d51eb52e65");
        localBackupConfig.setBackupServiceLocation("http://localhost:8080/backup-war/endpoint/backups");
        localBackupConfig.setLocalBackupDirectory("/Users/chris/Desktop/backup");
        localBackupConfig.setBaseDirectory("/opt/opennms");
        localBackupConfig.setPgDumpLocation("/Library/PostgreSQL/9.2/bin/pg_dump");
        localBackupConfig.setMaxConcurrentUploads(4);

        localBackupConfig.addDirectory("etc");
        localBackupConfig.addDirectory("share");
        localBackupConfig.addDirectory("dbdump");

        localBackupConfig.setSecret("password");

        BackupClient backupClient = new BackupClient(localBackupConfig);

        try {
            BackupConfig backupConfig = backupClient.lookupRemoteBackupConfig();

            BackupInfo backupInfo = backupClient.prepare();

            System.out.println(backupClient.getBackupState(backupInfo));

            /*
            try {
                BackupDbUtil backupDbUtil = new BackupDbUtil(localBackupConfig, "/opt/opennms/etc/opennms-datasources.xml");

                backupDbUtil.createDump();
            } catch (Exception e) {
                e.printStackTrace();
            }
            */

            ZipArchive zipArchive = backupClient.create(backupConfig);

            System.out.println(backupClient.getBackupState(backupInfo));

            backupClient.upload(backupConfig, backupInfo, zipArchive);

            System.out.println(backupClient.getBackupState(backupInfo));

            backupClient.finish(backupInfo);

            System.out.println(backupClient.getBackupState(backupInfo));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
