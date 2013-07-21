/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2007-2012 The OpenNMS Group, Inc.
 * OpenNMS(R) is Copyright (C) 1999-2012 The OpenNMS Group, Inc.
 *
 * OpenNMS(R) is a registered trademark of The OpenNMS Group, Inc.
 *
 * OpenNMS(R) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published
 * by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version.
 *
 * OpenNMS(R) is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with OpenNMS(R).  If not, see:
 *      http://www.gnu.org/licenses/
 *
 * For more information contact:
 *     OpenNMS(R) Licensing <license@opennms.org>
 *     http://www.opennms.org/
 *     http://www.opennms.com/
 *******************************************************************************/

package org.opennms.netmgt.threshd;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.opennms.netmgt.EventConstants;
import org.opennms.netmgt.xml.event.Event;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

/**
 * <p>
 * ThresholdEvaluatorHighLow class.
 * </p>
 *
 * @author ranger
 * @version $Id: $
 */
public class ThresholdEvaluatorHighLow implements ThresholdEvaluator {

    /** The Constant LOG. */
    private static final Logger LOG = LoggerFactory.getLogger(ThresholdEvaluatorHighLow.class);

    /**
     * <p>
     * Constructor for ThresholdEvaluatorHighLow.
     * </p>
     */
    public ThresholdEvaluatorHighLow() {

    }

    /** {@inheritDoc} */
    @Override
    public boolean supportsType(String type) {
        return "low".equals(type) || "high".equals(type);
    }

    /** {@inheritDoc} */
    @Override
    public ThresholdEvaluatorState getThresholdEvaluatorState(BaseThresholdDefConfigWrapper threshold) {
        return new ThresholdEvaluatorStateHighLow(threshold);
    }

    /**
     * The Class ThresholdEvaluatorStateHighLow.
     */
    public static class ThresholdEvaluatorStateHighLow extends AbstractThresholdEvaluatorState {
        /**
         * Castor Threshold object containing threshold configuration data.
         */
        private BaseThresholdDefConfigWrapper m_thresholdConfig;

        /** Threshold exceeded count. */
        private int m_exceededCount;

        /**
         * Threshold armed flag
         * This flag must be true before evaluate() will return true (indicating
         * that the threshold has been triggered). This flag is initialized to
         * true
         * by the constructor and is set to false each time the threshold is
         * triggered. It can only be reset by the current value of the
         * datasource
         * falling below (for high threshold) or rising above (for low
         * threshold)
         * the rearm value.
         */
        private boolean m_armed;

        /** The m_last collection resource used. */
        private CollectionResourceWrapper m_lastCollectionResourceUsed;

        /**
         * Instantiates a new threshold evaluator state high low.
         *
         * @param threshold
         *            the threshold
         */
        public ThresholdEvaluatorStateHighLow(BaseThresholdDefConfigWrapper threshold) {
            Assert.notNull(threshold, "threshold argument cannot be null");

            setThresholdConfig(threshold);
            setExceededCount(0);
            setArmed(true);
        }

        /**
         * Checks if is armed.
         *
         * @return true, if is armed
         */
        public boolean isArmed() {
            return m_armed;
        }

        /**
         * Sets the armed.
         *
         * @param armed
         *            the new armed
         */
        public void setArmed(boolean armed) {
            m_armed = armed;
        }

        /**
         * Gets the exceeded count.
         *
         * @return the exceeded count
         */
        public int getExceededCount() {
            return m_exceededCount;
        }

        /**
         * Sets the exceeded count.
         *
         * @param exceededCount
         *            the new exceeded count
         */
        public void setExceededCount(int exceededCount) {
            m_exceededCount = exceededCount;
        }

        /* (non-Javadoc)
         * @see org.opennms.netmgt.threshd.ThresholdEvaluatorState#getThresholdConfig()
         */
        @Override
        public BaseThresholdDefConfigWrapper getThresholdConfig() {
            return m_thresholdConfig;
        }

        /**
         * Sets the threshold config.
         *
         * @param thresholdConfig
         *            the new threshold config
         */
        public void setThresholdConfig(BaseThresholdDefConfigWrapper thresholdConfig) {
            Assert.notNull(thresholdConfig.getType(), "threshold must have a 'type' value set");
            Assert.notNull(thresholdConfig.getDatasourceExpression(), "threshold must have a 'ds-name' value set");
            Assert.notNull(thresholdConfig.getDsType(), "threshold must have a 'ds-type' value set");
            Assert.isTrue(thresholdConfig.hasValue(), "threshold must have a 'value' value set");
            Assert.isTrue(thresholdConfig.hasRearm(), "threshold must have a 'rearm' value set");
            Assert.isTrue(thresholdConfig.hasTrigger(), "threshold must have a 'trigger' value set");

            m_thresholdConfig = thresholdConfig;
        }

        /**
         * Gets the type.
         *
         * @return the type
         */
        public String getType() {
            return getThresholdConfig().getType();
        }

        /* (non-Javadoc)
         * @see org.opennms.netmgt.threshd.ThresholdEvaluatorState#evaluate(double)
         */
        @Override
        public Status evaluate(double dsValue) {
            if (isThresholdExceeded(dsValue)) {
                if (isArmed()) {
                    setExceededCount(getExceededCount() + 1);

                    LOG.debug("evaluate: {} threshold exceeded, count={}", getType(), getExceededCount());

                    if (isTriggerCountExceeded()) {
                        LOG.debug("evaluate: {} threshold triggered", getType());
                        setExceededCount(1);
                        setArmed(false);
                        return Status.TRIGGERED;
                    }
                }
            } else if (isRearmExceeded(dsValue)) {
                if (!isArmed()) {
                    LOG.debug("evaluate: {} threshold rearmed", getType());
                    setArmed(true);
                    setExceededCount(0);
                    return Status.RE_ARMED;
                }
                if (getExceededCount() > 0) {
                    LOG.debug("evaluate: resetting {} threshold count to 0, because the current value indicates that the in-progress threshold has been rearmed, but it doesn't triggered yet.",
                              getType());
                    setExceededCount(0);
                }
            } else {
                LOG.debug("evaluate: resetting {} threshold count to 0", getType());
                setExceededCount(0);
            }

            return Status.NO_CHANGE;
        }

        /**
         * Checks if is threshold exceeded.
         *
         * @param dsValue
         *            the ds value
         * @return true, if is threshold exceeded
         */
        protected boolean isThresholdExceeded(double dsValue) {
            if ("high".equals(getThresholdConfig().getType())) {
                return dsValue >= getThresholdConfig().getValue();
            } else if ("low".equals(getThresholdConfig().getType())) {
                return dsValue <= getThresholdConfig().getValue();
            } else {
                throw new IllegalStateException(
                                                "This thresholding strategy can only be used for thresholding types of 'high' and 'low'.");
            }
        }

        /**
         * Checks if is rearm exceeded.
         *
         * @param dsValue
         *            the ds value
         * @return true, if is rearm exceeded
         */
        protected boolean isRearmExceeded(double dsValue) {
            if ("high".equals(getThresholdConfig().getType())) {
                return dsValue <= getThresholdConfig().getRearm();
            } else if ("low".equals(getThresholdConfig().getType())) {
                return dsValue >= getThresholdConfig().getRearm();
            } else {
                throw new IllegalStateException(
                                                "This thresholding strategy can only be used for thresholding types of 'high' and 'low'.");
            }
        }

        /**
         * Checks if is trigger count exceeded.
         *
         * @return true, if is trigger count exceeded
         */
        protected boolean isTriggerCountExceeded() {
            return getExceededCount() >= getThresholdConfig().getTrigger();
        }

        /* (non-Javadoc)
         * @see org.opennms.netmgt.threshd.ThresholdEvaluatorState#getEventForState(org.opennms.netmgt.threshd.ThresholdEvaluatorState.Status, java.util.Date, double, org.opennms.netmgt.threshd.CollectionResourceWrapper)
         */
        @Override
        public Event getEventForState(Status status, Date date, double dsValue, CollectionResourceWrapper resource) {
            /*
             * If resource is null, we will use m_lastCollectionResourceUsed;
             * else we will use provided resource.
             * For future calls we will preserve the latest not null resource on
             * m_lastCollectionResourceUsed.
             * See ThresholdEntity.merge
             */
            if (resource == null) {
                resource = m_lastCollectionResourceUsed;
            }
            m_lastCollectionResourceUsed = resource;
            String uei;
            switch (status) {
            case TRIGGERED:
                uei = getThresholdConfig().getTriggeredUEI();
                if ("low".equals(getThresholdConfig().getType())) {
                    if (uei == null || "".equals(uei)) {
                        uei = EventConstants.LOW_THRESHOLD_EVENT_UEI;
                    }
                    return createBasicEvent(uei, date, dsValue, resource);
                } else if ("high".equals(getThresholdConfig().getType())) {
                    if (uei == null || "".equals(uei)) {
                        uei = EventConstants.HIGH_THRESHOLD_EVENT_UEI;
                    }
                    return createBasicEvent(uei, date, dsValue, resource);
                } else {
                    throw new IllegalArgumentException("Threshold type " + getThresholdConfig().getType()
                            + " is not supported");
                }

            case RE_ARMED:
                uei = getThresholdConfig().getRearmedUEI();
                if ("low".equals(getThresholdConfig().getType())) {
                    if (uei == null || "".equals(uei)) {
                        uei = EventConstants.LOW_THRESHOLD_REARM_EVENT_UEI;
                    }
                    return createBasicEvent(uei, date, dsValue, resource);
                } else if ("high".equals(getThresholdConfig().getType())) {
                    if (uei == null || "".equals(uei)) {
                        uei = EventConstants.HIGH_THRESHOLD_REARM_EVENT_UEI;
                    }
                    return createBasicEvent(uei, date, dsValue, resource);
                } else {
                    throw new IllegalArgumentException("Threshold type " + getThresholdConfig().getType()
                            + " is not supported");
                }

            case NO_CHANGE:
                return null;

            default:
                throw new IllegalArgumentException("Status " + status + " is not supported for converting to an event.");
            }
        }

        /**
         * Creates the basic event.
         *
         * @param uei
         *            the uei
         * @param date
         *            the date
         * @param dsValue
         *            the ds value
         * @param resource
         *            the resource
         * @return the event
         */
        private Event createBasicEvent(String uei, Date date, double dsValue, CollectionResourceWrapper resource) {
            Map<String, String> params = new HashMap<String, String>();
            params.put("threshold", Double.toString(getThresholdConfig().getValue()));
            params.put("trigger", Integer.toString(getThresholdConfig().getTrigger()));
            params.put("rearm", Double.toString(getThresholdConfig().getRearm()));
            return createBasicEvent(uei, date, dsValue, resource, params);
        }

        /* (non-Javadoc)
         * @see org.opennms.netmgt.threshd.ThresholdEvaluatorState#getCleanClone()
         */
        @Override
        public ThresholdEvaluatorState getCleanClone() {
            return new ThresholdEvaluatorStateHighLow(m_thresholdConfig);
        }

        /* (non-Javadoc)
         * @see org.opennms.netmgt.threshd.ThresholdEvaluatorState#isTriggered()
         */
        @Override
        public boolean isTriggered() {
            return !isArmed();
        }

        /* (non-Javadoc)
         * @see org.opennms.netmgt.threshd.ThresholdEvaluatorState#clearState()
         */
        @Override
        public void clearState() {
            setArmed(true);
            setExceededCount(0);
        }

    }

}
