/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2009-2014 The OpenNMS Group, Inc.
 * OpenNMS(R) is Copyright (C) 1999-2014 The OpenNMS Group, Inc.
 *
 * OpenNMS(R) is a registered trademark of The OpenNMS Group, Inc.
 *
 * OpenNMS(R) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version.
 *
 * OpenNMS(R) is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with OpenNMS(R).  If not, see:
 *      http://www.gnu.org/licenses/
 *
 * For more information contact:
 *     OpenNMS(R) Licensing <license@opennms.org>
 *     http://www.opennms.org/
 *     http://www.opennms.com/
 *******************************************************************************/

package org.opennms.sms.monitor.internal.config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.SchemaOutputResolver;
import javax.xml.bind.UnmarshalException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.ValidationEventHandler;
import javax.xml.bind.helpers.DefaultValidationEventHandler;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Result;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.custommonkey.xmlunit.DetailedDiff;
import org.custommonkey.xmlunit.Difference;
import org.custommonkey.xmlunit.XMLUnit;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.opennms.sms.monitor.internal.MobileSequenceConfigBuilder;
import org.opennms.sms.monitor.internal.MobileSequenceConfigBuilder.MobileSequenceTransactionBuilder;
import org.opennms.sms.monitor.session.UniqueNumber;
import org.opennms.test.PathAnticipator;
import org.smslib.USSDSessionStatus;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class SequenceXmlTest {

    private PathAnticipator m_pathAnticipator;
	private MobileSequenceConfig m_smsSequence;
	private JAXBContext m_context;
	private Marshaller m_marshaller;
	private Unmarshaller m_unmarshaller;

    static private class TestOutputResolver extends SchemaOutputResolver {
        private final Path m_schemaFile;

        public TestOutputResolver(Path schemaFile) {
            m_schemaFile = schemaFile;
        }

        @Override
        public Result createOutput(String namespaceUri, String suggestedFileName) throws IOException {
            return new StreamResult(m_schemaFile.toFile());
        }
    }

    @Before
    public void setUp() throws Exception {
        m_pathAnticipator = new PathAnticipator();

    	MobileSequenceConfigBuilder bldr = new MobileSequenceConfigBuilder();

    	bldr.variable("amount", UniqueNumber.class).parameter("min", 1).parameter("max", 15);

    	bldr.ussdRequest("req-balance-transfer", "ACM0", "*327*${recipient}*${amount}#").withTransactionLabel("ussd-transfer").withGatewayId("ACM0")
    	    .expectUssdResponse("balance-conf-resp")
    	    .onGateway("ACM0")
    	    .withSessionStatus(USSDSessionStatus.FURTHER_ACTION_REQUIRED)
    	    .matching("^Transfiere L ${amount} al ${recipient}$");


    	MobileSequenceTransactionBuilder transBldr = bldr.ussdRequest("conf-transfer", "ACM0", "1");

    	transBldr.withTransactionLabel("req-conf").withGatewayId("ACM0")
    	    .expectUssdResponse("processing")
    	    .onGateway("ACM0")
    	    .withSessionStatus(USSDSessionStatus.NO_FURTHER_ACTION_REQUIRED)
    	    .matching("^.*Su transaccion se esta procesando.*$");

    	transBldr.expectSmsResponse("transferred")
    	    .onGateway("ACM0")
    	    .matching("^.*le ha transferido L ${amount}.*$")
    	    .srcMatches("+3746");

    	m_smsSequence = bldr.getSequence();

    	m_context = JAXBContext.newInstance(
    			MobileSequenceConfig.class,
    			SmsSequenceRequest.class,
    			UssdSequenceRequest.class,
    			SmsSequenceResponse.class,
    			UssdSequenceResponse.class,
    			SmsFromRecipientResponseMatcher.class,
    			SmsSourceMatcher.class,
    			TextResponseMatcher.class,
    			UssdSessionStatusMatcher.class
    			);

    	m_marshaller = m_context.createMarshaller();
    	m_marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        m_marshaller.setProperty("com.sun.xml.bind.namespacePrefixMapper", new MobileSequenceNamespacePrefixMapper());

    	m_unmarshaller = m_context.createUnmarshaller();
    	m_unmarshaller.setSchema(null);

    	XMLUnit.setIgnoreComments(true);
        XMLUnit.setIgnoreWhitespace(true);
        XMLUnit.setIgnoreAttributeOrder(true);
        XMLUnit.setNormalize(true);
    }

    @After
    public void tearDown() throws Exception {
        m_pathAnticipator.tearDown();
    }

    @Test
    public void generateSchema() throws Exception {
        Path schemaFile = m_pathAnticipator.expecting("mobile-sequence.xsd");
        m_context.generateSchema(new TestOutputResolver(schemaFile));
        printFile(schemaFile);
        if (m_pathAnticipator.isInitialized()) {
            m_pathAnticipator.deleteExpected();
        }
    }

    @Test
    public void generateXML() throws Exception {
        // Marshal the test object to an XML string
        StringWriter objectXML = new StringWriter();
        m_marshaller.marshal(m_smsSequence, objectXML);
        System.err.println(objectXML.toString());
    }

    @Test(expected=UnmarshalException.class)
    public void readInvalidXML() throws Exception {
        Path exampleFile = Paths.get(ClassLoader.getSystemResource("invalid-sequence.xml").getFile());
    	ValidationEventHandler handler = new DefaultValidationEventHandler();
    	m_unmarshaller.setEventHandler(handler);
        MobileSequenceConfig s = (MobileSequenceConfig) m_unmarshaller.unmarshal(exampleFile.toFile());
    	System.err.println("sequence = " + s);

        assertTransactionParentsSet(s);
    }

    @Test(expected=UnmarshalException.class)
    public void readPoorlyFormedXML() throws Exception {
        Path exampleFile = Paths.get(ClassLoader.getSystemResource("poorly-formed-sequence.xml").getFile());
    	ValidationEventHandler handler = new DefaultValidationEventHandler();
    	m_unmarshaller.setEventHandler(handler);
        MobileSequenceConfig s = (MobileSequenceConfig) m_unmarshaller.unmarshal(exampleFile.toFile());
    	System.err.println("sequence = " + s);
        assertTransactionParentsSet(s);
    }

    @Test
    public void readAnotherSampleXML() throws Exception {
        Path exampleFile = Paths.get(ClassLoader.getSystemResource("alternate-ping-sequence.xml").getFile());
    	ValidationEventHandler handler = new DefaultValidationEventHandler();
    	m_unmarshaller.setEventHandler(handler);
        MobileSequenceConfig s = (MobileSequenceConfig) m_unmarshaller.unmarshal(exampleFile.toFile());
    	System.err.println("sequence = " + s);
        assertTransactionParentsSet(s);
    }

    @Test
    public void readXML() throws Exception {
        Path exampleFile = Paths.get(ClassLoader.getSystemResource("ussd-balance-sequence.xml").getFile());
    	ValidationEventHandler handler = new DefaultValidationEventHandler();
    	m_unmarshaller.setEventHandler(handler);
        MobileSequenceConfig s = (MobileSequenceConfig) m_unmarshaller.unmarshal(exampleFile.toFile());
    	System.err.println("sequence = " + s);
        assertTransactionParentsSet(s);
    }

    @Test
    public void validateXML() throws Exception {
        // Marshal the test object to an XML string
        StringWriter objectXML = new StringWriter();
        m_marshaller.marshal(m_smsSequence, objectXML);

        // Read the example XML from src/test/resources
        StringBuffer exampleXML = getXmlBuffer("ussd-balance-sequence.xml");
        System.err.println("========================================================================");
        System.err.println("Object XML:");
        System.err.println("========================================================================");
        System.err.print(objectXML.toString());
        System.err.println("========================================================================");
        System.err.println("Example XML:");
        System.err.println("========================================================================");
        System.err.print(exampleXML.toString());
        DetailedDiff myDiff = getDiff(objectXML, exampleXML);
        assertEquals("number of XMLUnit differences between the example XML and the mock object XML is 0", 0, myDiff.getAllDifferences().size());
    }

    @Test
    public void validateAgainstSchema() throws Exception {
        Path schemaFile = m_pathAnticipator.expecting("mobile-sequence.xsd");
        m_context.generateSchema(new TestOutputResolver(schemaFile));
        printFile(schemaFile);

        SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        Schema schema = factory.newSchema(schemaFile.toFile());
        Validator validator = schema.newValidator();

        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        dbFactory.setNamespaceAware(true);
        DocumentBuilder parser = dbFactory.newDocumentBuilder();
        Path sequenceFile = Paths.get(ClassLoader.getSystemResource("ussd-balance-sequence.xml").getFile());
        printFile(sequenceFile);
        Document document = parser.parse(sequenceFile.toFile());
        validator.validate(new DOMSource(document));

        if (m_pathAnticipator.isInitialized()) {
            m_pathAnticipator.deleteExpected();
        }
    }

    @Test
    public void tryFactory() throws Exception {
        Path exampleFile = Paths.get(ClassLoader.getSystemResource("ussd-balance-sequence.xml").getFile());
        MobileSequenceConfig sequence = SequenceConfigFactory.getInstance().getSequenceForFile(exampleFile.toFile());
    	assertEquals("ussd-transfer", sequence.getTransactions().iterator().next().getLabel());
    }

    @SuppressWarnings("unchecked")
	private DetailedDiff getDiff(StringWriter objectXML, StringBuffer exampleXML) throws SAXException, IOException {
        DetailedDiff myDiff = new DetailedDiff(XMLUnit.compareXML(exampleXML.toString(), objectXML.toString()));
        List<Difference> allDifferences = myDiff.getAllDifferences();
        if (allDifferences.size() > 0) {
            for (Difference d : allDifferences) {
                System.err.println(d);
            }
        }
        return myDiff;
    }

    private void assertTransactionParentsSet(MobileSequenceConfig s) {
        for ( MobileSequenceTransaction t : s.getTransactions() ) {
            assertEquals(s, t.getSequenceConfig());
        }
    }

    private StringBuffer getXmlBuffer(String fileName) throws IOException {
        StringBuffer xmlBuffer = new StringBuffer();
        Path xmlFile = Paths.get(ClassLoader.getSystemResource("ussd-balance-sequence.xml").getFile());
        assertTrue("ussd-balance-sequence.xml is readable", Files.isReadable(xmlFile));

        BufferedReader reader = Files.newBufferedReader(xmlFile, Charset.forName("UTF-8"));
        String line;
        while (true) {
            line = reader.readLine();
            if (line == null) {
                reader.close();
                break;
            }
            xmlBuffer.append(line).append("\n");
        }
        return xmlBuffer;
    }

    private void printFile(Path file) throws IOException {
        BufferedReader br = Files.newBufferedReader(file, Charset.forName("UTF-8"));
        StringBuilder sb = new StringBuilder();
        String line = null;

        while ((line = br.readLine()) != null) {
            sb.append(line).append("\n");
        }
        System.err.println(sb.toString());
    }

}
