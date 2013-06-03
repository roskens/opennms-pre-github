package org.opennms.vaadin.applicationstack.provider;

import java.io.File;

import javax.xml.bind.JAXB;

import org.opennms.vaadin.applicationstack.model.ApplicationStack;
import org.opennms.vaadin.applicationstack.model.ApplicationStacks;

/**
 * Loads and saves the ApplicationStack-Data to the filesystem.
 * 
 * @author marskuh
 */
public class ApplicationStacksProvider {

	private final File stacksFile;
	private ApplicationStacks stacks;

	public ApplicationStacksProvider(String configFile) {
		this(new File(configFile));
	}

	public ApplicationStacksProvider(File configFile) {
		this.stacksFile = configFile;
	}

	synchronized public ApplicationStack loadApplicationStack(final String stackName) {
		if (stacks == null) loadApplicationStacks();
		return stacks.getStack(stackName);
	}

	synchronized public void saveApplicationStack(final ApplicationStack stack) {
		if (stacks == null) loadApplicationStacks();
		stacks.addStack(stack);
		JAXB.marshal(stacks, stacksFile);
	}

	synchronized public void saveApplicationStacks(
			final ApplicationStacks stacks) {
		JAXB.marshal(stacks, stacksFile);
	}

	synchronized public ApplicationStacks loadApplicationStacks() {
		if (!stacksFile.exists()) {
			saveApplicationStacks(new ApplicationStacks()); // DEFAULT
		}
		stacks = JAXB.unmarshal(stacksFile, ApplicationStacks.class);
		return stacks;
	}
}
