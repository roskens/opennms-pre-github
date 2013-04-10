package org.opennms.netmgt.model.topology;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractType {
	protected Integer m_type;
	
    protected static final List<Integer> s_order = new ArrayList<Integer>();

    protected static int getIndex(Integer code) {
    	int i=0;
        for (Integer item: s_order) {
            if (item == code) {
                return i;
            }
            i++;
        }
        throw new IllegalArgumentException("illegal ElementIdentifier code '"+code+"'");
    }

    public AbstractType(Integer type) {
		m_type=type;
	}
	
    protected static final Map<Integer, String> s_typeMap = new HashMap<Integer, String>();
    
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

    /**
     * <p>ElementIdentifierTypeString</p>
     *
     * @return a {@link java.lang.String} object.
     */
    /**
     */
    public static String getTypeString(Integer code) {
        if (s_typeMap.containsKey(code))
                return s_typeMap.get( code);
        return null;
    }

}
