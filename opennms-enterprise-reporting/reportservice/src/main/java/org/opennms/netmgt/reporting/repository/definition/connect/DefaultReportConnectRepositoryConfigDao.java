package org.opennms.netmgt.reporting.repository.definition.connect;

import java.io.File;
import java.io.FileReader;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlRootElement;

//FIXME Tak: constructor is loading in instance of it's own type from filesystem... hmpf split into model and dao

@XmlRootElement(name = "ReportConnectConfig")
public class DefaultReportConnectRepositoryConfigDao implements
		ReportConnectRepositoryConfigDao {

	private static final String REPORT_CONNECT_REPOSITORY_XML = System
			.getProperty("opennms.home")
			+ File.separator
			+ "etc"
			+ File.separator + "report-connect-repository.xml";

	private String userName;
	private String connectReportPassword;
	private Boolean reportingActive = Boolean.FALSE;
	
	
	public DefaultReportConnectRepositoryConfigDao() {
		DefaultReportConnectRepositoryConfigDao configDao = null;
		try {
			JAXBContext context = JAXBContext
					.newInstance(DefaultReportConnectRepositoryConfigDao.class);
			Unmarshaller unmarshaller = context.createUnmarshaller();
			configDao = (DefaultReportConnectRepositoryConfigDao) unmarshaller
					.unmarshal(new FileReader(REPORT_CONNECT_REPOSITORY_XML));
			this.userName = configDao.getUserName();
			this.connectReportPassword = configDao.getConnectReportPassword();
			this.reportingActive = configDao.getReportingActive();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	@Override
	public String getConnectReportPassword() {
		return connectReportPassword;
	}

	public void setConnectReportPassword(String connectReportPassword) {
		this.connectReportPassword = connectReportPassword;
	}

	@Override
	public Boolean getReportingActive() {
		return reportingActive;
	}
	
	public void setReportingActive(Boolean active) {
		this.reportingActive = active;
	}
}