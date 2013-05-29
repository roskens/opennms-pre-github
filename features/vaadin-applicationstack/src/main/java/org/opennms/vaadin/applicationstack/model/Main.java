package org.opennms.vaadin.applicationstack.model;

import javax.xml.bind.JAXB;

public class Main {
	public static void main(String[] args) {
		ApplicationStack stack = new ApplicationStack("test");
		JAXB.marshal(stack,  System.out);
	}
}
