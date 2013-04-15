package org.opennms.netmgt.model.topology;

public abstract class AbstractType {
	protected Integer m_type;
	
    public AbstractType(Integer type) {
		m_type=type;
	}
	
    public Integer getIntCode() {
        return m_type;
    }

    public void setIntCode(Integer type) {
    	m_type = type;
    }


     public int hashCode() {
        return toString().hashCode();
    }

    public String toString() {
        return String.valueOf(m_type);
    }

    public abstract boolean equals(Object o);

}
