package org.opennms.netmgt.rrd.cassandra;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import me.prettyprint.hector.api.Keyspace;
import me.prettyprint.hector.api.beans.HColumn;
import me.prettyprint.hector.api.exceptions.HectorException;
import me.prettyprint.hector.api.factory.HFactory;
import me.prettyprint.hector.api.query.ColumnQuery;
import me.prettyprint.hector.api.query.QueryResult;
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

    public CassRrd(CassRrdDef def) {
        m_fileName = def.getFileName();
        m_step = def.getStep();
        m_datasources = def.getDatasources();
        m_archives = def.getArchives();
    }

    public CassRrd(Keyspace keyspace, String mdColumnFamily, String fileName) throws Exception {
        m_fileName = fileName;

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

        } catch (HectorException e) {
            LogUtils.errorf(this, e, "exception on search");
            throw new RrdException(e.getMessage());
        } catch (Exception e) {
            LogUtils.errorf(this, e, "exception on search");
            throw e;
        }

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

    public String getFileName() {
        return m_fileName;
    }

    public long getStep() {
        return m_step;
    }

    public List<String> getDatasourceNames() {
        List<String> dsNames = new ArrayList<String>();
        for (RrdDataSource ds : m_datasources) {
            dsNames.add(ds.getName());
        }
        return dsNames;
    }

    public void close() {
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

}
