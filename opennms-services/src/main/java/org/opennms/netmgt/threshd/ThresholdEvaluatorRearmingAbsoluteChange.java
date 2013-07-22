/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2008-2012 The OpenNMS Group, Inc.
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
 * This works similar to <tt>absoluteChange</tt>; however, the <tt>trigger</tt>
 * value
 * is used to re-arm the event after so many iterations with an unchanged delta.
 *
 * @author bdymek
 * @version $Id: $
 */
public class ThresholdEvaluatorRearmingAbsoluteChange implements ThresholdEvaluator {

    /** The Constant LOG. */
    private static final Logger LOG = LoggerFactory.getLogger(ThresholdEvaluatorRearmingAbsoluteChange.class);

    /** The Constant TYPE. */
    private static final String TYPE = "rearmingAbsoluteChange";

    /** {@inheritDoc} */
    @Override
    public ThresholdEvaluatorState getThresholdEvaluatorState(BaseThresholdDefConfigWrapper threshold) {
        return new ThresholdEvaluatorStateRearmingAbsoluteChange(threshold);
    }

    /** {@inheritDoc} */
    @Override
    public boolean supportsType(String type) {
        return TYPE.equals(type);
    }

    /**
     * The Class ThresholdEvaluatorStateRearmingAbsoluteChange.
     */
    public static class ThresholdEvaluatorStateRearmingAbsoluteChange extends AbstractThresholdEvaluatorState {

        /** The m_threshold config. */
        private BaseThresholdDefConfigWrapper m_thresholdConfig;

        /** The m_last sample. */
        private double m_lastSample = Double.NaN;

        /** The m_previous triggering sample. */
        private double m_previousTriggeringSample = Double.NaN;

        /** The m_trigger count. */
        private int m_triggerCount = 0;

        /**
         * Instantiates a new threshold evaluator state rearming absolute
         * change.
         *
         * @param threshold
         *            the threshold
         */
        public ThresholdEvaluatorStateRearmingAbsoluteChange(BaseThresholdDefConfigWrapper threshold) {
            Assert.notNull(threshold, "threshold argument cannot be null");
            setThresholdConfig(threshold);
        }

        /**
         * Gets the type.
         *
         * @return the type
         */
        public String getType() {
            return getThresholdConfig().getType().toString();
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

            Assert.isTrue(TYPE.equals(thresholdConfig.getType()),
                          "threshold for ds-name '" + thresholdConfig.getDatasourceExpression() + "' has type of '"
                                  + thresholdConfig.getType()
                                  + "', but this evaluator only supports thresholds with a 'type' value of '" + TYPE
                                  + "'");

            Assert.isTrue(!Double.isNaN(thresholdConfig.getValue()),
                          "threshold must have a 'value' value that is a number");
            Assert.isTrue(thresholdConfig.getValue() != Double.POSITIVE_INFINITY
                                  && thresholdConfig.getValue() != Double.NEGATIVE_INFINITY,
                          "threshold must have a 'value' value that is not positive or negative infinity");

            m_thresholdConfig = thresholdConfig;
        }

        /* (non-Javadoc)
         * @see org.opennms.netmgt.threshd.ThresholdEvaluatorState#getThresholdConfig()
         */
        @Override
        public BaseThresholdDefConfigWrapper getThresholdConfig() {
            return m_thresholdConfig;
        }

        /* (non-Javadoc)
         * @see org.opennms.netmgt.threshd.ThresholdEvaluatorState#evaluate(double)
         */
        @Override
        public Status evaluate(double dsValue) {
            // log().debug(TYPE +
            // " threshold evaluating, sample value="+dsValue);
            try {
                if (!Double.valueOf(getPreviousTriggeringSample()).isNaN()) {
                    ++m_triggerCount;
                    if (!wasTriggered(dsValue) && (m_triggerCount >= getThresholdConfig().getTrigger())) {
                        setPreviousTriggeringSample(Double.NaN);
                        m_triggerCount = 0;
                        LOG.debug("{} threshold rearmed, sample value={}", TYPE, dsValue);
                        return Status.RE_ARMED;
                    }
                } else if (wasTriggered(dsValue)) {
                    setPreviousTriggeringSample(getLastSample());
                    m_triggerCount = 0;
                    LOG.debug("{} threshold triggered, sample value={}", TYPE, dsValue);
                    return Status.TRIGGERED;
                }
            } finally {
                setLastSample(dsValue);
            }

            return Status.NO_CHANGE;
        }

        /**
         * Was triggered.
         *
         * @param dsValue
         *            the ds value
         * @return true, if successful
         */
        private boolean wasTriggered(double dsValue) {
            // Test Code
            // if(Double.valueOf(getPreviousTriggeringSample()).isNaN()) {
            // log().debug(TYPE +
            // " threshold evaluate trigger, sample value="+dsValue);
            // return true;
            // }
            if (Double.valueOf(dsValue).isNaN()) {
                return false;
            }
            if (Double.valueOf(getLastSample()).isNaN()) {
                return false;
            }

            double threshold = Math.abs(getLastSample() - dsValue);
            // Test Code
            // log().debug(TYPE +
            // " threshold evaluate trigger, sample value="+dsValue+",prev value="+getLastSample()+",thresh="+threshold+",trigger="+getThresholdConfig().getValue());
            return threshold >= getThresholdConfig().getValue();
        }

        /**
         * Gets the last sample.
         *
         * @return the last sample
         */
        public Double getLastSample() {
            return m_lastSample;
        }

        /**
         * Sets the last sample.
         *
         * @param lastSample
         *            the new last sample
         */
        public void setLastSample(double lastSample) {
            m_lastSample = lastSample;
        }

        /* (non-Javadoc)
         * @see org.opennms.netmgt.threshd.ThresholdEvaluatorState#getEventForState(org.opennms.netmgt.threshd.ThresholdEvaluatorState.Status, java.util.Date, double, org.opennms.netmgt.threshd.CollectionResourceWrapper)
         */
        @Override
        public Event getEventForState(Status status, Date date, double dsValue, CollectionResourceWrapper resource) {
            if (status == Status.TRIGGERED) {
                String uei = getThresholdConfig().getTriggeredUEI();
                if (uei == null || "".equals(uei)) {
                    uei = EventConstants.REARMING_ABSOLUTE_CHANGE_EXCEEDED_EVENT_UEI;
                }
                return createBasicEvent(uei, date, dsValue, resource);
            }

            if (status == Status.RE_ARMED) {
                String uei = getThresholdConfig().getRearmedUEI();
                if (uei == null || "".equals(uei)) {
                    uei = EventConstants.REARMING_ABSOLUTE_CHANGE_REARM_EVENT_UEI;
                }
                return createBasicEvent(uei, date, dsValue, resource);
            }

            return null;
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
            params.put("previousValue", formatValue(getPreviousTriggeringSample()));
            params.put("threshold", Double.toString(getThresholdConfig().getValue()));
            params.put("trigger", Integer.toString(getThresholdConfig().getTrigger()));
            // params.put("rearm",
            // Double.toString(getThresholdConfig().getRearm()));
            return createBasicEvent(uei, date, dsValue, resource, params);
        }

        /**
         * Gets the previous triggering sample.
         *
         * @return the previous triggering sample
         */
        public double getPreviousTriggeringSample() {
            return m_previousTriggeringSample;
        }

        /**
         * Sets the previous triggering sample.
         *
         * @param previousTriggeringSample
         *            the new previous triggering sample
         */
        public void setPreviousTriggeringSample(double previousTriggeringSample) {
            m_previousTriggeringSample = previousTriggeringSample;
        }

        /* (non-Javadoc)
         * @see org.opennms.netmgt.threshd.ThresholdEvaluatorState#getCleanClone()
         */
        @Override
        public ThresholdEvaluatorState getCleanClone() {
            return new ThresholdEvaluatorStateRearmingAbsoluteChange(m_thresholdConfig);
        }

        /* (non-Javadoc)
         * @see org.opennms.netmgt.threshd.ThresholdEvaluatorState#isTriggered()
         */
        @Override
        public boolean isTriggered() {
            return wasTriggered(m_previousTriggeringSample); // TODO Is that
                                                             // right ?
        }

        /* (non-Javadoc)
         * @see org.opennms.netmgt.threshd.ThresholdEvaluatorState#clearState()
         */
        @Override
        public void clearState() {
            // Based on what evaluator does for rearmed state
            m_lastSample = Double.NaN;
            m_triggerCount = 0;
            setPreviousTriggeringSample(Double.NaN);
        }

    }
}
