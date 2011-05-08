package org.opennms.gwt.web.ui.asset;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Collection;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.opennms.gwt.web.ui.asset.server.AssetServiceImpl;
import org.opennms.gwt.web.ui.asset.shared.AssetCommand;
import org.opennms.netmgt.dao.AssetRecordDao;
import org.opennms.netmgt.dao.DatabasePopulator;
import org.opennms.netmgt.dao.DistPollerDao;
import org.opennms.netmgt.dao.NodeDao;
import org.opennms.netmgt.dao.db.JUnitTemporaryDatabase;
import org.opennms.netmgt.dao.db.OpenNMSConfigurationExecutionListener;
import org.opennms.netmgt.dao.db.TemporaryDatabaseExecutionListener;
import org.opennms.netmgt.model.OnmsAssetRecord;
import org.opennms.netmgt.model.OnmsNode;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

@RunWith(SpringJUnit4ClassRunner.class)
@TestExecutionListeners({ OpenNMSConfigurationExecutionListener.class,
		TemporaryDatabaseExecutionListener.class,
		DependencyInjectionTestExecutionListener.class,
		DirtiesContextTestExecutionListener.class,
		TransactionalTestExecutionListener.class })
@ContextConfiguration(locations = {
		"classpath:/META-INF/opennms/applicationContext-dao.xml",
		"classpath:/META-INF/opennms/applicationContext-databasePopulator.xml",
		"classpath:/META-INF/opennms/applicationContext-setupIpLike-enabled.xml",
		"classpath*:/META-INF/opennms/component-dao.xml" })
@JUnitTemporaryDatabase()
public class AssetServiceImplTest {

	@Autowired
	private DistPollerDao m_distPollerDao;

	@Autowired
	private NodeDao m_nodeDao;

	@Autowired
	private AssetRecordDao m_assetRecordDao;

	@Autowired
	private DatabasePopulator m_databasePopulator;

	@Before
	public void setUp() {
		m_databasePopulator.populateDatabase();
	}

	@Test
	public void testCreateAndGets() {
		OnmsNode onmsNode = new OnmsNode(m_distPollerDao.load("localhost"));
		onmsNode.setLabel("myNode");
		m_nodeDao.save(onmsNode);
		OnmsAssetRecord assetRecord = onmsNode.getAssetRecord();
		assetRecord.setAssetNumber("imported-id: 7");
		m_assetRecordDao.update(assetRecord);
		m_assetRecordDao.flush();

		// Test findAll method
		Collection<OnmsAssetRecord> assetRecords = m_assetRecordDao.findAll();
		assertEquals(7, assetRecords.size());

		// Test countAll method
		assertEquals(7, m_assetRecordDao.countAll());

	}

	@Test
	public void testAssetServiceImpl() {
		OnmsNode onmsNode = new OnmsNode(m_distPollerDao.load("localhost"));
		onmsNode.setLabel("myNode");
		m_nodeDao.save(onmsNode);
		OnmsAssetRecord assetRecord = onmsNode.getAssetRecord();
		assetRecord.setAssetNumber("imported-id: 7");
		assetRecord.setAdmin("supermario");
		assetRecord.setZip("myzip");
		m_assetRecordDao.update(assetRecord);
		m_assetRecordDao.flush();

		onmsNode = new OnmsNode(m_distPollerDao.load("localhost"));
		onmsNode.setLabel("myNode2");
		m_nodeDao.save(onmsNode);
		assetRecord = onmsNode.getAssetRecord();
		assetRecord.setAssetNumber("imported-id: 23");
		assetRecord.setAdmin("mediummario");
		assetRecord.setZip("yourzip");
		m_assetRecordDao.update(assetRecord);
		m_assetRecordDao.flush();

		AssetServiceImpl assetServiceImpl = new AssetServiceImpl();
		assetServiceImpl.setNodeDao(this.m_nodeDao);
		assetServiceImpl.setAssetRecordDao(this.m_assetRecordDao);

		System.out.println("AssetCommand: "
				+ assetServiceImpl.getAssetByNodeId(7).toString());
		System.out.println("Suggestions: "
				+ assetServiceImpl.getAssetSuggestions());

	}

	@Test
	public void testSaveOrUpdate() {
		OnmsNode onmsNode = new OnmsNode(m_distPollerDao.load("localhost"));
		onmsNode.setLabel("myNode");
		m_nodeDao.save(onmsNode);
		OnmsAssetRecord assetRecord = onmsNode.getAssetRecord();
		assetRecord.setAssetNumber("imported-id: 7");
		assetRecord.setAdmin("supermario");
		assetRecord.setLastModifiedDate(new Date());
		assetRecord.setZip("myzip");
		m_assetRecordDao.update(assetRecord);
		m_assetRecordDao.flush();

		AssetCommand assetCommand = new AssetCommand();
		BeanUtils.copyProperties(assetRecord, assetCommand);

		System.out.println("AssetCommand (Source): " + assetCommand);
		System.out.println("Asset to Save (Target): " + assetRecord);

		AssetServiceImpl assetServiceImpl = new AssetServiceImpl();
		assetServiceImpl.setNodeDao(this.m_nodeDao);
		assetServiceImpl.setAssetRecordDao(this.m_assetRecordDao);
		assertTrue(assetServiceImpl.saveOrUpdateAssetByNodeId(7, assetCommand));
	}

	@Test
	public void testAssetSuggestion() {
		OnmsNode onmsNode = new OnmsNode(m_distPollerDao.load("localhost"));
		onmsNode.setLabel("your Node");
		onmsNode.setSysObjectId("mySysOid");
		m_nodeDao.save(onmsNode);
		OnmsAssetRecord assetRecord = onmsNode.getAssetRecord();
		assetRecord.setAssetNumber("imported-id: 666");
		assetRecord.setAdmin("medium mario");
		assetRecord.setLastModifiedDate(new Date());
		assetRecord.setZip("his zip");
		m_assetRecordDao.update(assetRecord);
		m_assetRecordDao.flush();

		onmsNode = new OnmsNode(m_distPollerDao.load("localhost"));
		onmsNode.setLabel("his Node");
		m_nodeDao.save(onmsNode);
		assetRecord = onmsNode.getAssetRecord();
		assetRecord.setAssetNumber("imported-id: 999");
		assetRecord.setAdmin("super mario");
		assetRecord.setLastModifiedDate(new Date());
		assetRecord.setZip("your zip");
		m_assetRecordDao.update(assetRecord);
		m_assetRecordDao.flush();

		AssetServiceImpl assetServiceImpl = new AssetServiceImpl();
		assetServiceImpl.setNodeDao(this.m_nodeDao);
		assetServiceImpl.setAssetRecordDao(this.m_assetRecordDao);
	}
}
