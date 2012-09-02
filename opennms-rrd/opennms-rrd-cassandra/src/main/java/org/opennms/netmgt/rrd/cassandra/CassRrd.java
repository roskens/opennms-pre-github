package org.opennms.netmgt.rrd.cassandra;

<<<<<<< HEAD
import java.io.IOException;
import java.io.StringReader;
=======
import java.io.StringWriter;
>>>>>>> local-dev/elfin/features/cassandra-rrd-backend
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

<<<<<<< HEAD
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import me.prettyprint.hector.api.Keyspace;
=======
import org.opennms.core.xml.JaxbUtils;

import me.prettyprint.cassandra.serializers.DoubleSerializer;
import me.prettyprint.cassandra.serializers.LongSerializer;
import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.hector.api.beans.ColumnSlice;
>>>>>>> local-dev/elfin/features/cassandra-rrd-backend
import me.prettyprint.hector.api.beans.HColumn;
import me.prettyprint.hector.api.exceptions.HectorException;
import me.prettyprint.hector.api.factory.HFactory;
import me.prettyprint.hector.api.query.ColumnQuery;
import me.prettyprint.hector.api.query.QueryResult;
<<<<<<< HEAD
import org.opennms.core.utils.LogUtils;
import org.opennms.netmgt.rrd.RrdDataSource;
import org.opennms.netmgt.rrd.RrdException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class CassRrd {

    private String m_fileName;

    private long m_step;

    private ArrayList<RrdDataSource> m_datasources = new ArrayList<RrdDataSource>();

    private ArrayList<String> m_archives = new ArrayList<String>();
    
    Persister m_persister;
    
    public CassRrd(CassRrdDef def) {
        m_fileName = def.getFileName();
        m_step = def.getStep();
        m_datasources = def.getDatasources();
        m_archives = def.getArchives();
    }
    public CassRrd(Keyspace keyspace, String mdColumnFamily, String fileName) throws Exception {
        this(keyspace, mdColumnFamily, fileName, null);
    }
        
    public CassRrd(Keyspace keyspace, String mdColumnFamily, String fileName, Persister persister) throws Exception {
        m_fileName = fileName;
        m_persister = persister;

        try {
            ColumnQuery<String, String, String> columnQuery = HFactory.createStringColumnQuery(keyspace);
            columnQuery.setColumnFamily(mdColumnFamily).setKey(fileName).setName(fileName);
            QueryResult<HColumn<String, String>> result = columnQuery.execute();

            HColumn<String, String> hc = result.get();
            
            if (hc == null) {
                throw new RrdException("file "+m_fileName+" does not exist!");
            }
            
            LogUtils.debugf(this, "metadata: found xml data for %s", m_fileName);
            LogUtils.debugf(this, "xml: %s", hc.getValue());
            parseXml(hc.getValue());

=======
import me.prettyprint.hector.api.query.SubSliceQuery;

import org.opennms.core.utils.LogUtils;
import org.opennms.netmgt.rrd.RrdException;
import org.opennms.netmgt.rrd.cassandra.config.Archive;
import org.opennms.netmgt.rrd.cassandra.config.Datasource;
import org.opennms.netmgt.rrd.cassandra.config.RrdDef;

public class CassRrd {
    private String m_fileName;

    private RrdDef m_rrddef;

    private CassandraRrdConnection m_connection;
    
    private static final StringSerializer s_ss = StringSerializer.get();

    private static final LongSerializer s_ls = LongSerializer.get();

    private static final DoubleSerializer s_ds = DoubleSerializer.get();

    public CassRrd(CassRrdDef def) {
        m_fileName = def.getFileName();
        m_rrddef = def.getRrdDef();
    }

    public CassRrd(CassandraRrdConnection connection, String fileName) throws Exception {
        m_connection = connection;
        m_fileName = fileName;

        try {
            ColumnQuery<String, String, String> columnQuery = HFactory.createStringColumnQuery(m_connection.getKeyspace());
            columnQuery.setColumnFamily(m_connection.getDataPointCFName()).setKey(fileName).setName(fileName);
            QueryResult<HColumn<String, String>> result = columnQuery.execute();

            HColumn<String, String> hc = result.get();

            if (hc == null) {
                throw new RrdException("file " + m_fileName + " does not exist!");
            }
            if (hc.getValue().isEmpty()) {
                throw new RrdException("file " + m_fileName + " had no valid metadata?");
            }

            LogUtils.debugf(this, "metadata: found xml data for %s", m_fileName);
            LogUtils.debugf(this, "xml: %s", hc.getValue());
            m_rrddef = JaxbUtils.unmarshal(RrdDef.class, hc.getValue(), true);
>>>>>>> local-dev/elfin/features/cassandra-rrd-backend
        } catch (HectorException e) {
            LogUtils.errorf(this, e, "exception on search");
            throw new RrdException(e.getMessage());
        } catch (Exception e) {
            LogUtils.errorf(this, e, "exception on search");
            throw e;
        }
    }
<<<<<<< HEAD
    
    public void setPersister(Persister persister) {
        m_persister = persister;
    }

    private void parseXml(String xmlInput) throws RrdException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        XPath xpath = XPathFactory.newInstance().newXPath();

        factory.setIgnoringComments(true);
        factory.setValidating(false);
        DocumentBuilder builder;
        try {
            builder = factory.newDocumentBuilder();
            InputSource inStream = new InputSource();
            inStream.setCharacterStream(new StringReader(xmlInput));
            Document doc = builder.parse(inStream);
            
            Node node = (Node) xpath.evaluate("/rrd_def/step", doc, XPathConstants.NODE);
            m_step = Integer.parseInt(node.getTextContent());
            NodeList nList = (NodeList) xpath.evaluate("/rrd_def/datasource", doc, XPathConstants.NODESET);
            for(int i = 0; i < nList.getLength(); i++) {
                Node ds = (Node) nList.item(i);
                NodeList dsAttrs = ds.getChildNodes();
                String name = null, type = null, min = null, max = null;
                Integer heartBeat = null;
                
                for(int j = 0; j < dsAttrs.getLength(); j++) {
                    Node dsAttr = (Node) dsAttrs.item(j);
                    if ("name".equals(dsAttr.getNodeName())) {
                        name = dsAttr.getTextContent();
                    }
                    if ("type".equals(dsAttr.getNodeName())) {
                        type = dsAttr.getTextContent();
                    }
                    if ("heartbeat".equals(dsAttr.getNodeName())) {
                        heartBeat = Integer.parseInt(dsAttr.getTextContent());
                    }
                    if ("min".equals(dsAttr.getNodeName())) {
                        min = dsAttr.getTextContent();
                    }
                    if ("max".equals(dsAttr.getNodeName())) {
                        max = dsAttr.getTextContent();
                    }
                }
                if(name != null && type != null && min != null && max != null && heartBeat != null) {
                    m_datasources.add(new RrdDataSource(name, type, heartBeat, min, max));
                } else {
                    LogUtils.errorf(this, "datasource format error, could not find values: %s", ds.toString());
                }
            }
            
            nList = (NodeList) xpath.evaluate("/rrd_def/archive", doc, XPathConstants.NODESET);
            for(int i = 0; i < nList.getLength(); i++) {
                Node arc = (Node) nList.item(i);
                NodeList arcAttrs = arc.getChildNodes();
                String cf = null, xff = null, steps = null, rows = null;
                
                for(int j = 0; j < arcAttrs.getLength(); j++) {
                    Node arcAttr = (Node) arcAttrs.item(j);
                    if ("cf".equals(arcAttr.getNodeName())) {
                        cf = arcAttr.getTextContent(); 
                    }
                    if ("xff".equals(arcAttr.getNodeName())) {
                        xff = arcAttr.getTextContent(); 
                    }
                    if ("steps".equals(arcAttr.getNodeName())) {
                        steps = arcAttr.getTextContent(); 
                    }
                    if ("rows".equals(arcAttr.getNodeName())) {
                        rows = arcAttr.getTextContent(); 
                    }
                }
                if(cf != null && xff != null && steps != null && rows != null) {
                    m_archives.add("RRA:"+cf+":"+xff+":"+steps+":"+rows);
                } else {
                    LogUtils.errorf(this, "archive format error, could not find values: %s", arc.toString());
                }
            }
            
        } catch (ParserConfigurationException e) {
            throw new RrdException(e.getMessage());
        } catch (SAXException e) {
            throw new RrdException(e.getMessage());
        } catch (IOException e) {
            throw new RrdException(e.getMessage());
        } catch (XPathExpressionException e) {
            throw new RrdException(e.getMessage());
        }

    }
=======
>>>>>>> local-dev/elfin/features/cassandra-rrd-backend

    public String getFileName() {
        return m_fileName;
    }
<<<<<<< HEAD
    
    public long getStep() {
        return m_step;
=======

    public Long getStep() {
        return m_rrddef.getStep();
>>>>>>> local-dev/elfin/features/cassandra-rrd-backend
    }

    public List<String> getDatasourceNames() {
        List<String> dsNames = new ArrayList<String>();
<<<<<<< HEAD
        for (RrdDataSource ds : m_datasources) {
=======
        for (Datasource ds : m_rrddef.getDatasourceCollection()) {
>>>>>>> local-dev/elfin/features/cassandra-rrd-backend
            dsNames.add(ds.getName());
        }
        return dsNames;
    }
<<<<<<< HEAD
    
    public String getDsName(int i) {
        return m_datasources.get(i).getName();
    }
    
    public int getDsCount() {
        return m_datasources.size();
=======

    public String getDsName(int i) {
        return m_rrddef.getDatasource(i).getName();
    }

    public int getDsCount() {
        return m_rrddef.getDatasourceCount();
>>>>>>> local-dev/elfin/features/cassandra-rrd-backend
    }

    public void close() {
    }
<<<<<<< HEAD
    
=======

>>>>>>> local-dev/elfin/features/cassandra-rrd-backend
    public void storeValues(String timeAndValues) throws RrdException {
        long timestamp;

        final StringTokenizer tokenizer = new StringTokenizer(timeAndValues, ":", false);
        final int tokenCount = tokenizer.countTokens();
        if (tokenCount > getDsCount() + 1) {
<<<<<<< HEAD
            throw new RrdException("Invalid number of values specified (found " + (tokenCount - 1) + ", "
                    + getDsCount() + " allowed)");
=======
            throw new RrdException("Invalid number of values specified (found " + (tokenCount - 1) + ", " + getDsCount()
                    + " allowed)");
>>>>>>> local-dev/elfin/features/cassandra-rrd-backend
        }
        final String timeToken = tokenizer.nextToken();
        try {
            timestamp = Long.parseLong(timeToken);
        } catch (final NumberFormatException nfe) {
            if (timeToken.equalsIgnoreCase("N") || timeToken.equalsIgnoreCase("NOW")) {
                timestamp = (System.currentTimeMillis() + 500L) / 1000L;
            } else {
                throw new RrdException("Invalid sample timestamp: " + timeToken);
            }
        }
        timestamp = normalize(timestamp, getStep());
<<<<<<< HEAD
        
=======

>>>>>>> local-dev/elfin/features/cassandra-rrd-backend
        for (int i = 0; tokenizer.hasMoreTokens(); i++) {
            try {
                Double value = Double.parseDouble(tokenizer.nextToken());
                Datapoint dp = new Datapoint(getFileName(), getDsName(i), timestamp, value);

<<<<<<< HEAD
                m_persister.persist(dp);
=======
                m_connection.persist(dp);
>>>>>>> local-dev/elfin/features/cassandra-rrd-backend
            } catch (final NumberFormatException nfe) {
                // NOP, value is already set to NaN
            }
        }
<<<<<<< HEAD
        
    }
    

    public String toXml() {
        StringBuilder sb = new StringBuilder();
        sb.append("<rrd_def>");
        sb.append("<step>").append(m_step).append("</step>");
        for (RrdDataSource ds : m_datasources) {
            sb.append("<datasource>");
            sb.append("<name>").append(ds.getName()).append("</name>");
            sb.append("<type>").append(ds.getType()).append("</type>");
            sb.append("<heartbeat>").append(ds.getHeartBeat()).append("</heartbeat>");
            sb.append("<min>").append(ds.getMin()).append("</min>");
            sb.append("<max>").append(ds.getMax()).append("</max>");
            sb.append("</datasource>");
        }
        for (int i = 0; i < m_archives.size(); i++) {
            String[] a = m_archives.get(i).split(":");
            if (a.length == 5) {
                sb.append("<archive>");
                sb.append("<cf>").append(a[1]).append("</cf>");
                sb.append("<xff>").append(a[2]).append("</xff>");
                sb.append("<steps>").append(a[3]).append("</steps>");
                sb.append("<rows>").append(a[4]).append("</rows>");
                sb.append(m_archives.get(i));
                sb.append("</archive>");
            }
        }
        sb.append("</rrd_def>");

        return sb.toString();
    }
    
    /**
     * Rounds the given timestamp to the nearest whole &quote;step&quote;. Rounded value is obtained
     * from the following expression:<p>
     * <code>timestamp - timestamp % step;</code><p>
     *
     * @param timestamp Timestamp in seconds
     * @param step    Step in seconds
     * @return "Rounded" timestamp
     */
    private static long normalize(long timestamp, long step) {
            return timestamp - timestamp % step;
=======
    }

    public String toXml() {
        final StringWriter writer = new StringWriter();
        JaxbUtils.marshal(m_rrddef, writer);
        final String xml = writer.toString();

        return xml;
    }

    /**
     * Rounds the given timestamp to the nearest whole &quote;step&quote;. Rounded value is obtained
     * from the following expression:
     * <p>
     * <code>timestamp - timestamp % step;</code>
     * <p>
     * 
     * @param timestamp
     *            Timestamp in seconds
     * @param step
     *            Step in seconds
     * @return "Rounded" timestamp
     */
    private static long normalize(long timestamp, long step) {
        return timestamp - timestamp % step;
    }

    public List<TimeSeriesPoint> fetchRequest(final String ds, final String consolidationFunction,
            final Long earliestUpdateTime, final Long latestUpdateTime) throws org.opennms.netmgt.rrd.RrdException {
        LogUtils.debugf(this, "fetchRequest(): fileName=%s, datasource=%s, begin=%d, end=%d", m_fileName, ds,
                        earliestUpdateTime, latestUpdateTime);
        ArrayList<TimeSeriesPoint> tspoints = new ArrayList<TimeSeriesPoint>();

        try {
            SubSliceQuery<String, String, Long, Double> ssquery = HFactory.createSubSliceQuery(m_connection.getKeyspace(), s_ss, s_ss, s_ls,
                                                                                               s_ds);
            ssquery.setColumnFamily(m_connection.getDataPointCFName());
            ssquery.setKey(m_fileName);
            ssquery.setSuperColumn(ds);
            ssquery.setRange(earliestUpdateTime, latestUpdateTime, false, Integer.MAX_VALUE);

            QueryResult<ColumnSlice<Long, Double>> ssresults = ssquery.execute();

            List<HColumn<Long, Double>> ssdatapoints = ssresults.get().getColumns();

            LogUtils.debugf(this, "found %d datapoints\n", ssdatapoints.size());
            for (HColumn<Long, Double> dp : ssdatapoints) {
                tspoints.add(new TimeSeriesPoint(dp.getName().longValue(), dp.getValue().doubleValue()));
            }
        } catch (HectorException e) {
            throw new org.opennms.netmgt.rrd.RrdException(e.getMessage());
        }
        return tspoints;
>>>>>>> local-dev/elfin/features/cassandra-rrd-backend
    }

}
