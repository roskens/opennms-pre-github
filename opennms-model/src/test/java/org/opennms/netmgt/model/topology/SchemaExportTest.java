package org.opennms.netmgt.model.topology;

import static org.junit.Assert.*;

import java.util.Properties;

import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.junit.Before;
import org.junit.Test;
import org.springframework.orm.hibernate3.annotation.AnnotationSessionFactoryBean;

public class SchemaExportTest {

	@Before
	public void setUp() throws Exception {
		AnnotationSessionFactoryBean factoryBean = new AnnotationSessionFactoryBean();
		factoryBean.setPackagesToScan(new String[] {
			"org.opennms.netmgt.model.topology"	
		});
		
		Properties properties = new Properties();
		properties.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
		properties.put("hibernate.hbm2ddl.auto", "create");
		factoryBean.setHibernateProperties(properties);
		factoryBean.afterPropertiesSet();
		
		
		SchemaExport export = new SchemaExport(factoryBean.getConfiguration());
		export.create(true, false);
		
	}

	@Test
	public void test() {
	}

}
