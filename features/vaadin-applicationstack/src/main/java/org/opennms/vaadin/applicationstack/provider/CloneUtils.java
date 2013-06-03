package org.opennms.vaadin.applicationstack.provider;

import org.opennms.vaadin.applicationstack.model.ApplicationLayer;
import org.opennms.vaadin.applicationstack.model.ApplicationStack;
import org.opennms.vaadin.applicationstack.model.Criteria;

public class CloneUtils {

	public static ApplicationStack clone(ApplicationStack input) {
		ApplicationStack output = new ApplicationStack(input.getLabel());
		for (ApplicationLayer eachLayer : input.getLayers()) {
			output.addLayer(clone(eachLayer));
		}
		return output;
	}
	
	private static Criteria clone(Criteria input) {
		return new Criteria(input.getEntityType(), input.getOperator(), input.getSearch());
	}
	
	private static ApplicationLayer clone(ApplicationLayer input) {
		ApplicationLayer output = new ApplicationLayer(input.getLabel(), input.getRow(), input.getColumn(), input.getWidth(), input.getHeight());
		for (Criteria eachCriteria : input.getCriterias()) {
			output.addCriteria(clone(eachCriteria));
		}
		return output;
	}
}
