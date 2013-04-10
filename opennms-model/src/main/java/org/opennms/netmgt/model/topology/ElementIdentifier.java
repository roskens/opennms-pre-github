package org.opennms.netmgt.model.topology;

import java.io.Serializable;


public abstract class ElementIdentifier {

	public final static class ElementIdentifierType extends AbstractType 
	implements Comparable<ElementIdentifierType>, Serializable {

		private static final long serialVersionUID = 7220152765747623134L;

		public static final int ELEMENT_ID_TYPE_ONMSNODE=0;
		public static final int ELEMENT_ID_TYPE_LLDP=1;
		
		public static ElementIdentifierType LLDP = new ElementIdentifierType(ELEMENT_ID_TYPE_LLDP);
		public static ElementIdentifierType ONMSNODE = new ElementIdentifierType(ELEMENT_ID_TYPE_ONMSNODE);

		public ElementIdentifierType(Integer elementIdentifierType) {
			super(elementIdentifierType);
		}
		
        static {
        	s_order.add(0, 0);
        	s_typeMap.put(0, "nodeid" );
        	s_order.add(1, 1);
        	s_typeMap.put(1, "lldp" );
        }

        @Override
        public int compareTo(ElementIdentifierType o) {
            return getIndex(m_type) - getIndex(o.m_type);
        }

        @Override
        public boolean equals(Object o) {
            if (o instanceof ElementIdentifierType) {
                return m_type.intValue() == ((ElementIdentifierType)o).m_type.intValue();
            }
            return false;
        }

        public static ElementIdentifierType get(Integer code) {
            if (code == null)
                throw new IllegalArgumentException("Cannot create ElementIdentifierType from null code");
            switch (code) {
            case ELEMENT_ID_TYPE_LLDP: 		return LLDP;
            case ELEMENT_ID_TYPE_ONMSNODE: 	return ONMSNODE;
            default:
                throw new IllegalArgumentException("Cannot create ElementIdentifierType from code "+code);
            }
        }		
	}

	private final String m_identifier;
	private String m_identifier2;
	private ElementIdentifierType m_type;
	private AbstractType m_subtype;
	
	public ElementIdentifier(String identifier) {
		m_identifier = identifier;
	}
	
	public ElementIdentifierType getType() {
		return m_type;
	}

	public void setType(ElementIdentifierType type) {
		m_type = type;
	}

	public AbstractType getSubtype() {
		return m_subtype;
	}

	public void setSubtype(AbstractType subtype) {
		m_subtype = subtype;
	}

	public String getIdentifier() {
		return m_identifier;
	}

	public String getIdentifier2() {
		return m_identifier2;
	}

	public void setIdentifier2(String identifier2) {
		m_identifier2 = identifier2;
	}

	
	
}
