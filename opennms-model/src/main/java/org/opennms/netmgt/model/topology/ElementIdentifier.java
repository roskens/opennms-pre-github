package org.opennms.netmgt.model.topology;

import java.io.Serializable;
import java.util.Date;


public abstract class ElementIdentifier extends Pollable {

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

	private final ElementIdentifierType m_identifier;
	
	public ElementIdentifier(ElementIdentifierType identifier, Date now ) {
		super(now);
		m_identifier = identifier;
	}
	
	public ElementIdentifierType getType() {
		return m_identifier;
	}
	
	public boolean equals (Object o) {
		if (o instanceof ElementIdentifier) {
			if (((ElementIdentifier)o).getType().equals(getType()))
				return equals((ElementIdentifier)o);
		}
		return false;
	}

	public abstract boolean equals(ElementIdentifier elementIdentifier);
}
