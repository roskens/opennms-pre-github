package org.opennms.vaadin.applicationstack.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

@XmlRootElement
public class ApplicationStacks {
	private List<ApplicationStack> stacks = new ArrayList<ApplicationStack>();
	
	public ApplicationStacks() {
	}
	
	public void setStacks(List<ApplicationStack> stacks) {
		this.stacks = stacks;
	}
	
	@XmlElement(name="stack")
	public List<ApplicationStack> getStacks() {
		return stacks;
	}

	public ApplicationStack getStack(String stackName) {
		if (stacks == null) return null; 
		for (ApplicationStack eachStack : stacks) {
			if (stackName.equalsIgnoreCase(eachStack.getLabel()))
				return eachStack;
		}
		return null; // not found
	}
	
	public ApplicationStacks addStack(String stackName) {
		for (ApplicationStack eachStack : stacks) {
			if (stackName.equalsIgnoreCase(eachStack.getLabel())) {
				return this; // already exists
			}
		}
		addStack(new ApplicationStack(stackName));
		return this;
	}

	public ApplicationStacks addStack(ApplicationStack stack) {
            if (!stacks.contains(stack))
                stacks.add(stack);
            return this;
	}

	public ApplicationStack getFirst() {
		if (stacks.isEmpty()) return null;
		return stacks.get(0);
	}
	
	@Override
	public boolean equals(Object obj) {
		return EqualsBuilder.reflectionEquals(this,  obj);
	}
	
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this);
	};

    public void removeStack(ApplicationStack stack) {
        stacks.remove(stack);
    }
}
