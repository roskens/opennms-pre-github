package org.opennms.vaadin.applicationstack.model;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.opennms.core.criteria.restrictions.Restriction;
import org.opennms.core.criteria.restrictions.Restrictions;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@XmlRootElement
public class Criteria {

    private interface OperatorRestriction {
        public Restriction getRestriction(EntityType entityType, String attribute, String value);
    }

    public enum EntityType {
        Id(Integer.class, "id"),
        Interfaces(String.class, "ipInterfacesAlias.ipAddress", "ipInterfacesAlias.ipHostName"),
        Services(String.class, "serviceTypeAlias.name"),
        Categories(String.class, "categoriesAlias.name");

        String[] properties;
        Class clazz;

        EntityType(Class clazz, String... properties) {
            this.clazz = clazz;
            this.properties = properties;
        }

        public String[] getProperties() {
            return properties;
        }

        public Object valueOfSearchString(String s) {
            if (clazz.equals(String.class)) {
                return s;
            } else {
                if (clazz.equals(Integer.class)) {
                    Integer i = 0;
                    try {
                        i = Integer.parseInt(s);
                    } catch (NumberFormatException numberFormatException) {
                    }
                    return i;
                }

                return null;
            }
        }

        public List<Object> valuesOfSearchString(String s) {
            if (clazz.equals(String.class)) {
                return Arrays.asList((Object[]) s.split(","));
            } else {
                if (clazz.equals(Integer.class)) {
                    List<Object> intList = new ArrayList<Object>();
                    String strArr[] = s.split(",");
                    try {
                        for (String v : strArr) {
                            intList.add(Integer.parseInt(v));
                        }
                    } catch (NumberFormatException numberFormatException) {
                    }
                    return intList;
                }

                return null;
            }
        }
    }

    public enum Operator implements OperatorRestriction {
        Equals("=") {
            public Restriction getRestriction(EntityType entityType, String attribute, String value) {
                return Restrictions.eq(attribute, entityType.valueOfSearchString(value));
            }
        },
        NotEquals("\u2260") {
            public Restriction getRestriction(EntityType entityType, String attribute, String value) {
                return Restrictions.not(Restrictions.eq(attribute, entityType.valueOfSearchString(value)));
            }
        },
        In("\u2208") {
            public Restriction getRestriction(EntityType entityType, String attribute, String value) {
                return Restrictions.in(attribute, entityType.valuesOfSearchString(value));
            }
        },
        NotIn("\u2209") {
            public Restriction getRestriction(EntityType entityType, String attribute, String value) {
                return Restrictions.not(Restrictions.in(attribute, entityType.valuesOfSearchString(value)));
            }
        };

        private String title;

        Operator(String title) {
            this.title = title;
        }

        public String getTitle() {
            return title;
        }

        public String toString() {
            return title;
        }
    }

    private EntityType entityType;
    private Operator operator;
    private String search;

    public Criteria() {
    }

    public Criteria(EntityType entityType, Operator operator, String search) {
        this.entityType = entityType;
        this.operator = operator;
        this.search = search;
    }

    public EntityType getEntityType() {
        return entityType;
    }

    public void setEntityType(EntityType entityType) {
        this.entityType = entityType;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public String getSearch() {
        return search;
    }

    public Operator getOperator() {
        return operator;
    }

    public void setOperator(Operator operator) {
        this.operator = operator;
    }

    @Override
    public String toString() {
        return entityType.toString() + " " + operator.toString() + " '" + search + "'";
    }

    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }
}
