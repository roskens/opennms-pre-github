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

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.jexl2.JexlContext;
import org.apache.commons.jexl2.JexlEngine;
import org.opennms.netmgt.config.threshd.Expression;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Class ExpressionConfigWrapper.
 *
 * @author <a href="mailto:jeffg@opennms.org">Jeff Gehlbach</a>
 * @author <a href="mailto:cmiskell@opennms.org">Craig Miskell</a>
 */
public class ExpressionConfigWrapper extends BaseThresholdDefConfigWrapper {

    /** The Constant LOG. */
    private static final Logger LOG = LoggerFactory.getLogger(ExpressionConfigWrapper.class);

    /**
     * This class is used to sniff all of the variable names that a script tries
     * to use out of the ScriptContext during a call to eval(). This will allow
     * us to construct a list of required parameters for the script expression.
     */
    private static class BindingsSniffer extends HashMap<String, Object> implements JexlContext {

        /** The Constant serialVersionUID. */
        private static final long serialVersionUID = 5595028572061424206L;

        /** The m_sniffed keys. */
        private final Set<String> m_sniffedKeys = new HashSet<String>();

        /** The ignore these keys. */
        private final String[] ignoreTheseKeys = new String[] { "math" };

        /* (non-Javadoc)
         * @see org.apache.commons.jexl2.JexlContext#get(java.lang.String)
         */
        @Override
        public Object get(String key) {
            LOG.trace("Bindings.get({})", key);
            m_sniffedKeys.add(((String) key).intern());
            return super.get(key);
        }

        /* (non-Javadoc)
         * @see org.apache.commons.jexl2.JexlContext#has(java.lang.String)
         */
        @Override
        public boolean has(String key) {
            LOG.trace("Bindings.containsKey({})", key);
            m_sniffedKeys.add(((String) key).intern());
            return super.containsKey(key);
        }

        /* (non-Javadoc)
         * @see org.apache.commons.jexl2.JexlContext#set(java.lang.String, java.lang.Object)
         */
        @Override
        public void set(String key, Object value) {
            LOG.trace("Bindings.set({}, {})", key, value);
            m_sniffedKeys.add(((String) key).intern());
            super.put(key, value);
        }

        /**
         * Gets the sniffed keys.
         *
         * @return the sniffed keys
         */
        public Set<String> getSniffedKeys() {
            LOG.trace("Bindings.getSniffedKeys({})");
            m_sniffedKeys.removeAll(Arrays.asList(ignoreTheseKeys));
            return Collections.unmodifiableSet(m_sniffedKeys);
        }
    }

    /** The m_expression. */
    private final Expression m_expression;

    /** The m_datasources. */
    private final Collection<String> m_datasources;

    /**
     * Instantiates a new expression config wrapper.
     *
     * @param expression
     *            the expression
     * @throws ThresholdExpressionException
     *             the threshold expression exception
     */
    public ExpressionConfigWrapper(Expression expression) throws ThresholdExpressionException {
        super(expression);
        m_expression = expression;

        // Fetch an instance of the JEXL script engine
        JexlEngine expressionParser = new JexlEngine();

        BindingsSniffer sniffer = new BindingsSniffer();
        sniffer.put("math", new MathBinding());
        sniffer.put("datasources", new HashMap<String, Double>()); // To
                                                                   // workaround
                                                                   // NMS-5019

        // Test parsing of the expression and collect the variable names by
        // using
        // a Bindings instance that sniffs all of the variable names
        try {
            expressionParser.createExpression(m_expression.getExpression()).evaluate(sniffer);
        } catch (Throwable e) {
            throw new ThresholdExpressionException("Could not parse threshold expression:" + e.getMessage(), e);
        }

        m_datasources = sniffer.getSniffedKeys();
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.threshd.BaseThresholdDefConfigWrapper#getDatasourceExpression()
     */
    @Override
    public String getDatasourceExpression() {
        return m_expression.getExpression();
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.threshd.BaseThresholdDefConfigWrapper#getRequiredDatasources()
     */
    @Override
    public Collection<String> getRequiredDatasources() {
        return m_datasources;
    }

    /**
     * This class provides an instance that gives access to the.
     * {@link java.lang.Math} functions.
     * You can access this variable in your expressions by using the
     * <code>math</code> variable
     * (ie. <code>math.abs(-1)</code>).
     */
    public static class MathBinding {

        /**
         * Abs.
         *
         * @param a
         *            the a
         * @return the double
         */
        public double abs(double a) {
            return Math.abs(a);
        }

        /**
         * Abs.
         *
         * @param a
         *            the a
         * @return the float
         */
        public float abs(float a) {
            return Math.abs(a);
        }

        /**
         * Abs.
         *
         * @param a
         *            the a
         * @return the int
         */
        public int abs(int a) {
            return Math.abs(a);
        }

        /**
         * Abs.
         *
         * @param a
         *            the a
         * @return the long
         */
        public long abs(long a) {
            return Math.abs(a);
        }

        /**
         * Acos.
         *
         * @param a
         *            the a
         * @return the double
         */
        public double acos(double a) {
            return Math.acos(a);
        }

        /**
         * Asin.
         *
         * @param a
         *            the a
         * @return the double
         */
        public double asin(double a) {
            return Math.asin(a);
        }

        /**
         * Atan.
         *
         * @param a
         *            the a
         * @return the double
         */
        public double atan(double a) {
            return Math.atan(a);
        }

        /**
         * Atan2.
         *
         * @param a
         *            the a
         * @param b
         *            the b
         * @return the double
         */
        public double atan2(double a, double b) {
            return Math.atan2(a, b);
        }

        /**
         * Cbrt.
         *
         * @param a
         *            the a
         * @return the double
         */
        public double cbrt(double a) {
            return Math.cbrt(a);
        }

        /**
         * Ceil.
         *
         * @param a
         *            the a
         * @return the double
         */
        public double ceil(double a) {
            return Math.ceil(a);
        }

        /**
         * Cos.
         *
         * @param a
         *            the a
         * @return the double
         */
        public double cos(double a) {
            return Math.cos(a);
        }

        /**
         * Cosh.
         *
         * @param a
         *            the a
         * @return the double
         */
        public double cosh(double a) {
            return Math.cosh(a);
        }

        /**
         * Exp.
         *
         * @param a
         *            the a
         * @return the double
         */
        public double exp(double a) {
            return Math.exp(a);
        }

        /**
         * Expm1.
         *
         * @param a
         *            the a
         * @return the double
         */
        public double expm1(double a) {
            return Math.expm1(a);
        }

        /**
         * Floor.
         *
         * @param a
         *            the a
         * @return the double
         */
        public double floor(double a) {
            return Math.floor(a);
        }

        /**
         * Hypot.
         *
         * @param a
         *            the a
         * @param b
         *            the b
         * @return the double
         */
        public double hypot(double a, double b) {
            return Math.hypot(a, b);
        }

        /**
         * IEE eremainder.
         *
         * @param a
         *            the a
         * @param b
         *            the b
         * @return the double
         */
        public double IEEEremainder(double a, double b) {
            return Math.IEEEremainder(a, b);
        }

        /**
         * Log.
         *
         * @param a
         *            the a
         * @return the double
         */
        public double log(double a) {
            return Math.log(a);
        }

        /**
         * Log10.
         *
         * @param a
         *            the a
         * @return the double
         */
        public double log10(double a) {
            return Math.log10(a);
        }

        /**
         * Log1p.
         *
         * @param a
         *            the a
         * @return the double
         */
        public double log1p(double a) {
            return Math.log1p(a);
        }

        /**
         * Max.
         *
         * @param a
         *            the a
         * @param b
         *            the b
         * @return the double
         */
        public double max(double a, double b) {
            return Math.max(a, b);
        }

        /**
         * Max.
         *
         * @param a
         *            the a
         * @param b
         *            the b
         * @return the float
         */
        public float max(float a, float b) {
            return Math.max(a, b);
        }

        /**
         * Max.
         *
         * @param a
         *            the a
         * @param b
         *            the b
         * @return the int
         */
        public int max(int a, int b) {
            return Math.max(a, b);
        }

        /**
         * Max.
         *
         * @param a
         *            the a
         * @param b
         *            the b
         * @return the long
         */
        public long max(long a, long b) {
            return Math.max(a, b);
        }

        /**
         * Min.
         *
         * @param a
         *            the a
         * @param b
         *            the b
         * @return the double
         */
        public double min(double a, double b) {
            return Math.min(a, b);
        }

        /**
         * Min.
         *
         * @param a
         *            the a
         * @param b
         *            the b
         * @return the float
         */
        public float min(float a, float b) {
            return Math.min(a, b);
        }

        /**
         * Min.
         *
         * @param a
         *            the a
         * @param b
         *            the b
         * @return the int
         */
        public int min(int a, int b) {
            return Math.min(a, b);
        }

        /**
         * Min.
         *
         * @param a
         *            the a
         * @param b
         *            the b
         * @return the long
         */
        public long min(long a, long b) {
            return Math.min(a, b);
        }

        /**
         * Pow.
         *
         * @param a
         *            the a
         * @param b
         *            the b
         * @return the double
         */
        public double pow(double a, double b) {
            return Math.pow(a, b);
        }

        /**
         * Random.
         *
         * @return the double
         */
        public double random() {
            return Math.random();
        }

        /**
         * Rint.
         *
         * @param a
         *            the a
         * @return the double
         */
        public double rint(double a) {
            return Math.rint(a);
        }

        /**
         * Round.
         *
         * @param a
         *            the a
         * @return the long
         */
        public long round(double a) {
            return Math.round(a);
        }

        /**
         * Round.
         *
         * @param a
         *            the a
         * @return the int
         */
        public int round(float a) {
            return Math.round(a);
        }

        /**
         * Signum.
         *
         * @param a
         *            the a
         * @return the double
         */
        public double signum(double a) {
            return Math.signum(a);
        }

        /**
         * Signum.
         *
         * @param a
         *            the a
         * @return the float
         */
        public float signum(float a) {
            return Math.signum(a);
        }

        /**
         * Sin.
         *
         * @param a
         *            the a
         * @return the double
         */
        public double sin(double a) {
            return Math.sin(a);
        }

        /**
         * Sinh.
         *
         * @param a
         *            the a
         * @return the double
         */
        public double sinh(double a) {
            return Math.sinh(a);
        }

        /**
         * Sqrt.
         *
         * @param a
         *            the a
         * @return the double
         */
        public double sqrt(double a) {
            return Math.sqrt(a);
        }

        /**
         * Tan.
         *
         * @param a
         *            the a
         * @return the double
         */
        public double tan(double a) {
            return Math.tan(a);
        }

        /**
         * Tanh.
         *
         * @param a
         *            the a
         * @return the double
         */
        public double tanh(double a) {
            return Math.tanh(a);
        }

        /**
         * To degrees.
         *
         * @param a
         *            the a
         * @return the double
         */
        public double toDegrees(double a) {
            return Math.toDegrees(a);
        }

        /**
         * To radians.
         *
         * @param a
         *            the a
         * @return the double
         */
        public double toRadians(double a) {
            return Math.toRadians(a);
        }

        /**
         * Ulp.
         *
         * @param a
         *            the a
         * @return the double
         */
        public double ulp(double a) {
            return Math.ulp(a);
        }

        /**
         * Ulp.
         *
         * @param a
         *            the a
         * @return the float
         */
        public float ulp(float a) {
            return Math.ulp(a);
        }
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.threshd.BaseThresholdDefConfigWrapper#evaluate(java.util.Map)
     */
    @Override
    public double evaluate(Map<String, Double> values) throws ThresholdExpressionException {
        // Add all of the variable values to the script context
        BindingsSniffer context = new BindingsSniffer();
        context.putAll(values);
        context.set("datasources", new HashMap<String, Double>(values)); // To
                                                                         // workaround
                                                                         // NMS-5019
        context.put("math", new MathBinding());
        double result = Double.NaN;
        try {
            // Fetch an instance of the JEXL script engine to evaluate the
            // script expression
            Object resultObject = new JexlEngine().createExpression(m_expression.getExpression()).evaluate(context);
            result = Double.parseDouble(resultObject.toString());
        } catch (Throwable e) {
            throw new ThresholdExpressionException("Error while evaluating expression " + m_expression.getExpression()
                    + ": " + e.getMessage(), e);
        }
        return result;
    }
}
