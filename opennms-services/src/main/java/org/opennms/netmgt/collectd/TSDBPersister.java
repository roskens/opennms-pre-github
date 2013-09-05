package org.opennms.netmgt.collectd;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Stack;

public class TSDBPersister {
    private static final Logger LOG = LoggerFactory.getLogger(TSDBPersister.class);
    final static TSDBPersister m_tsdbPersister = new TSDBPersister();
    private final int METRIC_THRESHOLD = 1000;
    private Stack<TSDBEntry> m_collectionAttributes = new Stack<TSDBEntry>();
    private int m_tsdbPort;
    private String m_tsdbIp;

    class TSDBEntry {
        private String m_foreignId, m_foreignSource, m_key, m_value, m_ipAddress, m_instance;
        private long m_timestamp;

        public TSDBEntry(long timestamp, String foreignSource, String foreignId, String ipAddress, String instance, String key, String value) {
            m_foreignId = foreignId;
            m_foreignSource = foreignSource;
            m_key = key;
            m_value = value;
            m_ipAddress = ipAddress;
            m_timestamp = timestamp;
            m_instance = instance;
        }

        private String cleanTag(String value) {
            return value.replaceAll("\\. ", "_");
        }

        public String getEntry() {
            return "put " + m_key + (m_instance != null ? "." + cleanTag(m_instance) : "") + " " + m_timestamp + " " + m_value + " type=metric" + (m_foreignSource != null ? " foreignSource=" + cleanTag(m_foreignSource) : "") + (m_foreignId != null ? " foreignId=" + cleanTag(m_foreignId) : "") + (m_ipAddress != null ? " ipAddress=" + cleanTag(m_ipAddress) : "");
        }
    }

    private TSDBPersister() {
        m_tsdbPort = Integer.getInteger("org.opennms.tsdb.port");
        m_tsdbIp = System.getProperty("org.opennms.tsdb.ip");
    }

    public static TSDBPersister getInstance() {
        return m_tsdbPersister;
    }

    private void persist() {
        LOG.debug("sending {} metrics to {}:{}", m_collectionAttributes.size(), m_tsdbIp, m_tsdbPort);

        try {
            Socket socket = new Socket(m_tsdbIp, m_tsdbPort);

            PrintStream out = new PrintStream(socket.getOutputStream());

            while (!m_collectionAttributes.empty()) {
                TSDBEntry tsdbEntry = m_collectionAttributes.pop();
                out.println(tsdbEntry.getEntry());
            }

            out.flush();
            out.close();
            socket.close();
        } catch (IOException e) {
            LOG.warn("error connecting {}:{}, queueSize={}", m_tsdbIp, m_tsdbPort, m_collectionAttributes.size());
        }
    }

    public void addEntry(long timestamp, String foreignSource, String foreignId, String ipAddress, String instance, String key, String value) {
        TSDBEntry tsdbEntry = new TSDBEntry(timestamp, foreignSource, foreignId, ipAddress, instance, key, value);
        LOG.debug("queuing entry '{}' for {}:{}, queueSize={}", tsdbEntry.getEntry(), m_tsdbIp, m_tsdbPort, m_collectionAttributes.size());
        m_collectionAttributes.add(tsdbEntry);
        if (m_collectionAttributes.size() > METRIC_THRESHOLD) {
            persist();
        }
    }
}
