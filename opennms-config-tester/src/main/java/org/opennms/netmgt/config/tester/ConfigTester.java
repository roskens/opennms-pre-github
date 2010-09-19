package org.opennms.netmgt.config.tester;

import java.util.Map;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.opennms.core.utils.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.util.StringUtils;

public class ConfigTester implements ApplicationContextAware {
	private ApplicationContext m_context;
	private Map<String, String> m_configs;

	public Map<String, String> getConfigs() {
		return m_configs;
	}

	public void setConfigs(Map<String, String> configs) {
		m_configs = configs;
	}

	public ApplicationContext getApplicationContext() {
		return m_context;
	}

	public void setApplicationContext(ApplicationContext context) throws BeansException {
		m_context = context;
	}

	public void testConfig(String name) {
		if (!m_configs.containsKey(name)) {
			throw new IllegalArgumentException("config '" + name + "' is not a known config name");
		}
		
		m_context.getBean(m_configs.get(name));
	}

	public static void main(String[] argv) {
		BasicConfigurator.configure();
		Logger.getRootLogger().setLevel(Level.WARN);
		
		Logger.getLogger("org.springframework").setLevel(Level.WARN);
		
		ApplicationContext context = BeanUtils.getFactory("configTesterContext", ClassPathXmlApplicationContext.class);
		ConfigTester tester = context.getBean("configTester", ConfigTester.class);

		if (argv.length != 1) {
			String configNames = StringUtils.collectionToCommaDelimitedString(tester.getConfigs().keySet());
			throw new IllegalArgumentException("Invalid usage.  You must specify exactly one configuration file to test.  Supported configuration files: " + configNames);
		}
		
		String config = argv[0];
		
		tester.testConfig(config);
	}
}
