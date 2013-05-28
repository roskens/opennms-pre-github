package org.opennms.vaadin.applicationstack.model;

import org.opennms.core.criteria.restrictions.Restriction;
import org.opennms.core.criteria.restrictions.Restrictions;

public class Criteria {

    private interface OperatorRestriction {
        public Restriction getRestriction(String attribute, String value);
    }

    public enum EntityType {
        Id("id"),
        Category("category"),
        Interfaces("primaryInterface"),
        Services("serviceName");

        String[] properties;

        EntityType(String ... properties) {
            this.properties=properties;
        }
    }

    public enum Operator implements OperatorRestriction {
        Equals("=") {
            public Restriction getRestriction(String attribute, String value) {
                return Restrictions.eq(attribute, value);
            }
        },
        NotEquals("\u2260") {
            public Restriction getRestriction(String attribute, String value) {
                return Restrictions.not(Restrictions.eq(attribute, value));
            }
        },
        In("\u2208") {
            public Restriction getRestriction(String attribute, String value) {
                return Restrictions.sql(attribute + " IN (" + value + ")");
            }
        },
        NotIn("\u2209") {
            public Restriction getRestriction(String attribute, String value) {
                return Restrictions.not(Restrictions.sql(attribute + " IN (" + value + ")"));
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
}
