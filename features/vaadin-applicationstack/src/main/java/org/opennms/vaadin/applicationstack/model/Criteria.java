package org.opennms.vaadin.applicationstack.model;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Criteria {

    private interface SqlOperation {
        public String operatorIn(String search);

        public String operatorNotIn(String search);

        public String operatorEquals(String search);

        public String operatorNotEquals(String search);
    }

    public enum EntityType implements SqlOperation {
        Id() {
            public String operatorIn(String search) {
                return "SELECT DISTINCT nodeId FROM node WHERE CAST(nodeId AS TEXT) IN (" + toTextArray(search) + ")";
            }

            public String operatorNotIn(String search) {
                return "SELECT DISTINCT nodeId FROM node WHERE NOT CAST(nodeId AS TEXT) IN (" + toTextArray(search) + ")";
            }

            public String operatorEquals(String search) {
                return "SELECT DISTINCT nodeId FROM node WHERE CAST(nodeId AS TEXT) LIKE " + toText(search);
            }

            public String operatorNotEquals(String search) {
                return "SELECT DISTINCT nodeId FROM node WHERE NOT CAST(nodeId AS TEXT) LIKE " + toText(search);
            }
        },

        Interfaces() {
            public String operatorIn(String search) {
                return "SELECT DISTINCT node.nodeId FROM ipInterface LEFT JOIN node ON ipInterface.nodeId=node.nodeId WHERE ipAddr IN (" + toTextArray(search) + ")";
            }

            public String operatorNotIn(String search) {
                return "SELECT DISTINCT node.nodeId FROM ipInterface LEFT JOIN node ON ipInterface.nodeId=node.nodeId WHERE NOT ipAddr IN (" + toTextArray(search) + ")";
            }

            public String operatorEquals(String search) {
                return "SELECT DISTINCT node.nodeId FROM ipInterface LEFT JOIN node ON ipInterface.nodeId=node.nodeId WHERE ipAddr LIKE " + toText(search);
            }

            public String operatorNotEquals(String search) {
                return "SELECT DISTINCT node.nodeId FROM ipInterface LEFT JOIN node ON ipInterface.nodeId=node.nodeId WHERE NOT ipAddr LIKE " + toText(search);
            }
        },

        Services() {
            public String operatorIn(String search) {
                return "SELECT DISTINCT ifservices.nodeId FROM ifservices LEFT JOIN service ON ifservices.serviceId=service.serviceId WHERE serviceName IN (" + toTextArray(search) + ")";
            }

            public String operatorNotIn(String search) {
                return "SELECT DISTINCT ifservices.nodeId FROM ifservices LEFT JOIN service ON ifservices.serviceId=service.serviceId WHERE NOT serviceName IN (" + toTextArray(search) + ")";
            }

            public String operatorEquals(String search) {
                return "SELECT DISTINCT ifservices.nodeId FROM ifservices LEFT JOIN service ON ifservices.serviceId=service.serviceId WHERE serviceName LIKE " + toText(search);
            }

            public String operatorNotEquals(String search) {
                return "SELECT DISTINCT ifservices.nodeId FROM ifservices LEFT JOIN service ON ifservices.serviceId=service.serviceId WHERE NOT serviceName LIKE " + toText(search);
            }
        },

        Categories() {
            public String operatorIn(String search) {
                return "SELECT DISTINCT nodeId FROM category_node LEFT JOIN categories ON categories.categoryid=category_node.categoryid WHERE categoryName IN (" + toTextArray(search) + ")";
            }

            public String operatorNotIn(String search) {
                return "SELECT DISTINCT nodeId FROM category_node LEFT JOIN categories ON categories.categoryid=category_node.categoryid WHERE NOT categoryName IN (" + toTextArray(search) + ")";
            }

            public String operatorEquals(String search) {
                return "SELECT DISTINCT nodeId FROM category_node LEFT JOIN categories ON categories.categoryid=category_node.categoryid WHERE categoryName LIKE " + toText(search);
            }

            public String operatorNotEquals(String search) {
                return "SELECT DISTINCT nodeId FROM category_node LEFT JOIN categories ON categories.categoryid=category_node.categoryid WHERE NOT categoryName LIKE " + toText(search);
            }
        };

        EntityType() {
        }

        public String getSql(Operator operator, String s) {
            switch (operator) {
                case In:
                    return operatorIn(s);
                case NotIn:
                    return operatorNotIn(s);
                case Equals:
                    return operatorEquals(s);
                case NotEquals:
                    return operatorNotEquals(s);
                default:
                    return null;
            }
        }

        protected String toText(String search) {
            return "'" + search + "'";
        }

        protected String toTextArray(String search) {
            String arr[] = search.split(",");
            String list = "";
            for (String value : arr) {
                if (!"".equals(list)) {
                    list += ", ";
                }
                list += "'" + value + "'";
            }
            return list;
        }
    }

    public enum Operator {
        Equals("="),
        NotEquals("\u2260"),
        In("\u2208"),
        NotIn("\u2209");

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
