package org.opennms.netmgt.model.topology;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;


public abstract class ElementIdentifier extends Pollable {

	public final static class ElementIdentifierType extends AbstractType 
	implements Serializable {

		private static final long serialVersionUID = 7220152765747623134L;

		public static final int ELEMENT_ID_TYPE_NODE   = 0;
		public static final int ELEMENT_ID_TYPE_LLDP   = 1;
		public static final int ELEMENT_ID_TYPE_CDP    = 2;
		public static final int ELEMENT_ID_TYPE_OSPF   = 3;
		public static final int ELEMENT_ID_TYPE_BRIDGE = 4;
		public static final int ELEMENT_ID_TYPE_INET   = 5;
		
		public static ElementIdentifierType INET   = new ElementIdentifierType(ELEMENT_ID_TYPE_INET);
		public static ElementIdentifierType BRIDGE = new ElementIdentifierType(ELEMENT_ID_TYPE_BRIDGE);
		public static ElementIdentifierType OSPF   = new ElementIdentifierType(ELEMENT_ID_TYPE_OSPF);
		public static ElementIdentifierType CDP    = new ElementIdentifierType(ELEMENT_ID_TYPE_CDP);
		public static ElementIdentifierType LLDP   = new ElementIdentifierType(ELEMENT_ID_TYPE_LLDP);
		public static ElementIdentifierType NODE   = new ElementIdentifierType(ELEMENT_ID_TYPE_NODE);

		public ElementIdentifierType(Integer elementIdentifierType) {
			super(elementIdentifierType);
		}
		
	    protected static final Map<Integer, String> s_typeMap = new HashMap<Integer, String>();

        static {
        	s_typeMap.put(0, "nodeid" );
        	s_typeMap.put(1, "lldp" );
        	s_typeMap.put(2, "cdp" );
        	s_typeMap.put(3, "ospf" );
        	s_typeMap.put(4, "bridge" );
           	s_typeMap.put(5, "inet" );
                   }

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
            case ELEMENT_ID_TYPE_INET: 	 return INET;
            case ELEMENT_ID_TYPE_BRIDGE: return BRIDGE;
            case ELEMENT_ID_TYPE_OSPF: 	 return OSPF;
            case ELEMENT_ID_TYPE_CDP: 	 return CDP;
            case ELEMENT_ID_TYPE_LLDP: 	 return LLDP;
            case ELEMENT_ID_TYPE_NODE: 	 return NODE;
            default:
                throw new IllegalArgumentException("Cannot create ElementIdentifierType from code "+code);
            }
        }		
	}

	private final ElementIdentifierType m_identifier;
	
	public ElementIdentifier(ElementIdentifierType identifier) {
		super();
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
