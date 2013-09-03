package org.opennms.netmgt.collectd;

import org.opennms.core.utils.DefaultTimeKeeper;
import org.opennms.core.utils.TimeKeeper;
import org.opennms.netmgt.config.collector.CollectionAttribute;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Stack;

public class TSDBPersister extends BasePersister {
    final static TSDBPersister m_tsdbPersister = new TSDBPersister();

    private final int METRIC_THRESHOLD = 1000;
    private static final Logger LOG = LoggerFactory.getLogger(TSDBPersister.class);

    private Stack<TSDBEntry> m_collectionAttributes = new Stack<TSDBEntry>();
    private int m_tsdbPort;
    private String m_tsdbIp;
    private TimeKeeper m_timeKeeper = new DefaultTimeKeeper();

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
            return value.replaceAll(" ", "_");
        }

        public String getEntry() {
            // echo "put disc.dsUsed 1378125680 440 host=superhost" | nc -w 15 192.168.30.184 4242

            return "put " + m_key + " " + m_timestamp + " " + m_value + (m_instance != null ? " instance=" + cleanTag(m_instance) : "") + (m_foreignSource != null ? " foreignSource=" + cleanTag(m_foreignSource) : "") + (m_foreignId != null ? " foreignId=" + cleanTag(m_foreignId) : "") + (m_ipAddress != null ? " ipAddress=" + cleanTag(m_ipAddress) : "");
        }
    }

    private TSDBPersister() {
    }

    public static TSDBPersister getInstance(String ip, int port) {
        m_tsdbPersister.setIp(ip);
        m_tsdbPersister.setPort(port);

        return m_tsdbPersister;
    }

    public void setIp(String ip) {
        m_tsdbIp = ip;
    }

    public void setPort(int port) {
        m_tsdbPort = port;
    }

    private void persist() {
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
            LOG.warn("BMRHGA: error connecting {}:{} ", m_tsdbIp, m_tsdbPort, e);
        }
    }

    @Override
    public void persistNumericAttribute(CollectionAttribute attribute) {
        m_collectionAttributes.add(new TSDBEntry(m_timeKeeper.getCurrentTime(), attribute.getResource().getForeignSource(), attribute.getResource().getForeignId(), attribute.getResource().getIpAddress(), attribute.getResource().getInstance(), attribute.getMetricIdentifier(), attribute.getNumericValue()));
        if (m_collectionAttributes.size() > METRIC_THRESHOLD) {
            persist();
        }
    }

    @Override
    public void persistStringAttribute(CollectionAttribute attribute) {
        m_collectionAttributes.add(new TSDBEntry(m_timeKeeper.getCurrentTime(), attribute.getResource().getForeignSource(), attribute.getResource().getForeignId(), attribute.getResource().getIpAddress(), attribute.getResource().getInstance(), attribute.getMetricIdentifier(), attribute.getStringValue()));
        if (m_collectionAttributes.size() > METRIC_THRESHOLD) {
            persist();
        }
    }
}
