package org.opennms.vaadin.applicationstack.model;

import javax.xml.bind.JAXB;

public class Main {
	public static void main(String[] args) {
		ApplicationStacks stacks = new ApplicationStacks()
			.addStack(new ApplicationStack("1")
					.addLayer(
							new ApplicationLayer("1",  0,  0,  0,  0)
								.addCriteria(new Criteria())))
			.addStack(new ApplicationStack("2"));
		JAXB.marshal(stacks,  System.out);
	}
}
