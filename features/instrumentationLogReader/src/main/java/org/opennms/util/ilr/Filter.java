/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2011-2012 The OpenNMS Group, Inc.
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

package org.opennms.util.ilr;

import java.util.ArrayList;
import java.util.Collection;

/**
 * The Class Filter.
 */
public class Filter {

    /** The m_search string. */
    private static String m_searchString = null;

    /**
     * The Interface PropertyGetter.
     *
     * @param <T>
     *            the generic type
     */
    public static interface PropertyGetter<T> {

        /**
         * Gets the.
         *
         * @param c
         *            the c
         * @return the t
         */
        T get(ServiceCollector c);
    }

    /**
     * Service id.
     *
     * @return the property getter
     */
    static PropertyGetter<String> serviceID() {
        return new PropertyGetter<String>() {

            @Override
            public String get(ServiceCollector c) {
                return c.getServiceID();
            }

        };

    }

    /**
     * Collection count.
     *
     * @return the property getter
     */
    static PropertyGetter<Integer> collectionCount() {
        return new PropertyGetter<Integer>() {

            @Override
            public Integer get(ServiceCollector c) {
                return c.getCollectionCount();
            }

        };

    }

    /**
     * And.
     *
     * @param a
     *            the a
     * @param b
     *            the b
     * @return the predicate
     */
    static Predicate<ServiceCollector> and(final Predicate<ServiceCollector> a, final Predicate<ServiceCollector> b) {
        return new Predicate<ServiceCollector>() {

            @Override
            public boolean apply(ServiceCollector svcCollector) {
                return a.apply(svcCollector) && b.apply(svcCollector);
            }

        };
    }

    /**
     * Or.
     *
     * @param a
     *            the a
     * @param b
     *            the b
     * @return the predicate
     */
    static Predicate<ServiceCollector> or(final Predicate<ServiceCollector> a, final Predicate<ServiceCollector> b) {
        return new Predicate<ServiceCollector>() {

            @Override
            public boolean apply(ServiceCollector svcCollector) {
                return a.apply(svcCollector) || b.apply(svcCollector);
            }

        };
    }

    /**
     * Eq.
     *
     * @param <T>
     *            the generic type
     * @param getter
     *            the getter
     * @param val
     *            the val
     * @return the predicate
     */
    static <T> Predicate<ServiceCollector> eq(final PropertyGetter<T> getter, final T val) {
        return new Predicate<ServiceCollector>() {

            @Override
            public boolean apply(ServiceCollector svcCollector) {
                return getter.get(svcCollector).equals(val);
            }

        };
    }

    /**
     * Greater than.
     *
     * @param getter
     *            the getter
     * @param val
     *            the val
     * @return the predicate
     */
    static Predicate<ServiceCollector> greaterThan(final PropertyGetter<Integer> getter, final Integer val) {
        return new Predicate<ServiceCollector>() {

            @Override
            public boolean apply(ServiceCollector svcCollector) {
                return getter.get(svcCollector) > val;
            }

        };
    }

    /**
     * Less than.
     *
     * @param getter
     *            the getter
     * @param val
     *            the val
     * @return the predicate
     */
    static Predicate<ServiceCollector> lessThan(final PropertyGetter<Integer> getter, final Integer val) {
        return new Predicate<ServiceCollector>() {

            @Override
            public boolean apply(ServiceCollector svcCollector) {
                return getter.get(svcCollector) < val;
            }

        };
    }

    /**
     * By service id.
     *
     * @param serviceID
     *            the service id
     * @return the predicate
     */
    static Predicate<ServiceCollector> byServiceID(final String serviceID) {
        return new Predicate<ServiceCollector>() {

            @Override
            public boolean apply(ServiceCollector svcCollector) {
                return svcCollector.getServiceID().equals(serviceID);
            }

        };
    }

    /**
     * By partial service id.
     *
     * @param searchString
     *            the search string
     * @return the predicate
     */
    static Predicate<ServiceCollector> byPartialServiceID(final String searchString) {
        return new Predicate<ServiceCollector>() {

            @Override
            public boolean apply(ServiceCollector svcCollector) {
                return svcCollector.getServiceID().contains(searchString);
            }
        };
    }

    /**
     * By total collections.
     *
     * @param totalCollections
     *            the total collections
     * @return the predicate
     */
    static Predicate<ServiceCollector> byTotalCollections(final long totalCollections) {
        return new Predicate<ServiceCollector>() {

            @Override
            public boolean apply(ServiceCollector svcCollector) {
                return svcCollector.getCollectionCount() == totalCollections;
            }

        };
    }

    /**
     * By total collection time.
     *
     * @param totalCollectionTime
     *            the total collection time
     * @return the predicate
     */
    static Predicate<ServiceCollector> byTotalCollectionTime(final long totalCollectionTime) {
        return new Predicate<ServiceCollector>() {

            @Override
            public boolean apply(ServiceCollector svcCollector) {
                return svcCollector.getCollectionCount() == totalCollectionTime;
            }

        };
    }

    /**
     * By average collection time.
     *
     * @param averageCollectionTime
     *            the average collection time
     * @return the predicate
     */
    static Predicate<ServiceCollector> byAverageCollectionTime(final long averageCollectionTime) {
        return new Predicate<ServiceCollector>() {

            @Override
            public boolean apply(ServiceCollector svcCollector) {
                return svcCollector.getCollectionCount() == averageCollectionTime;
            }

        };
    }

    /**
     * By average time between collections.
     *
     * @param averageTimeBetweenCollections
     *            the average time between collections
     * @return the predicate
     */
    static Predicate<ServiceCollector> byAverageTimeBetweenCollections(final long averageTimeBetweenCollections) {
        return new Predicate<ServiceCollector>() {

            @Override
            public boolean apply(ServiceCollector svcCollector) {
                return svcCollector.getCollectionCount() == averageTimeBetweenCollections;
            }

        };
    }

    /**
     * By total successful collections.
     *
     * @param totalSuccessfulCollections
     *            the total successful collections
     * @return the predicate
     */
    static Predicate<ServiceCollector> byTotalSuccessfulCollections(final long totalSuccessfulCollections) {
        return new Predicate<ServiceCollector>() {

            @Override
            public boolean apply(ServiceCollector svcCollector) {
                return svcCollector.getCollectionCount() == totalSuccessfulCollections;
            }

        };
    }

    /**
     * By successful percentage.
     *
     * @param successfulPercentage
     *            the successful percentage
     * @return the predicate
     */
    static Predicate<ServiceCollector> bySuccessfulPercentage(final double successfulPercentage) {
        return new Predicate<ServiceCollector>() {

            @Override
            public boolean apply(ServiceCollector svcCollector) {
                return svcCollector.getCollectionCount() == successfulPercentage;
            }

        };
    }

    /**
     * By average successful collection time.
     *
     * @param averageSuccessfulCollectionTime
     *            the average successful collection time
     * @return the predicate
     */
    static Predicate<ServiceCollector> byAverageSuccessfulCollectionTime(final long averageSuccessfulCollectionTime) {
        return new Predicate<ServiceCollector>() {

            @Override
            public boolean apply(ServiceCollector svcCollector) {
                return svcCollector.getCollectionCount() == averageSuccessfulCollectionTime;
            }

        };
    }

    /**
     * By total unsuccessful collections.
     *
     * @param totalUnsuccessfulCollections
     *            the total unsuccessful collections
     * @return the predicate
     */
    static Predicate<ServiceCollector> byTotalUnsuccessfulCollections(final long totalUnsuccessfulCollections) {
        return new Predicate<ServiceCollector>() {

            @Override
            public boolean apply(ServiceCollector svcCollector) {
                return svcCollector.getCollectionCount() == totalUnsuccessfulCollections;
            }

        };
    }

    /**
     * By unsuccessful percentage.
     *
     * @param unsuccessfulPercentage
     *            the unsuccessful percentage
     * @return the predicate
     */
    static Predicate<ServiceCollector> byUnsuccessfulPercentage(final double unsuccessfulPercentage) {
        return new Predicate<ServiceCollector>() {

            @Override
            public boolean apply(ServiceCollector svcCollector) {
                return svcCollector.getCollectionCount() == unsuccessfulPercentage;
            }

        };
    }

    /**
     * By average unsuccessful collection time.
     *
     * @param averageUnsuccessfulCollectionTime
     *            the average unsuccessful collection time
     * @return the predicate
     */
    static Predicate<ServiceCollector> byAverageUnsuccessfulCollectionTime(final long averageUnsuccessfulCollectionTime) {
        return new Predicate<ServiceCollector>() {

            @Override
            public boolean apply(ServiceCollector svcCollector) {
                return svcCollector.getCollectionCount() == averageUnsuccessfulCollectionTime;
            }

        };
    }

    /**
     * By total persist time.
     *
     * @param totalPersistTime
     *            the total persist time
     * @return the predicate
     */
    static Predicate<ServiceCollector> byTotalPersistTime(final long totalPersistTime) {
        return new Predicate<ServiceCollector>() {

            @Override
            public boolean apply(ServiceCollector svcCollector) {
                return svcCollector.getCollectionCount() == totalPersistTime;
            }

        };
    }

    /**
     * Creates the integer based predicate.
     *
     * @param j
     *            the j
     * @return the predicate
     */
    public Predicate<Integer> createIntegerBasedPredicate(final int j) {
        Predicate<Integer> predicate = new Predicate<Integer>() {
            @Override
            public boolean apply(Integer i) {
                if (i == j) {
                    return true;
                } else {
                    return false;
                }
            }
        };
        return predicate;
    }

    /**
     * Creates the string based predicate.
     *
     * @param filterString
     *            the filter string
     * @return the predicate
     */
    public Predicate<String> createStringBasedPredicate(final String filterString) {
        Predicate<String> predicate = new Predicate<String>() {
            @Override
            public boolean apply(String s) {
                if (s.equals(filterString)) {
                    return true;
                } else {
                    return false;
                }
            }
        };
        return predicate;
    }

    /**
     * Filter.
     *
     * @param <T>
     *            the generic type
     * @param target
     *            the target
     * @param predicate
     *            the predicate
     * @return the collection
     */
    public static <T> Collection<T> filter(Collection<T> target, Predicate<T> predicate) {
        Collection<T> filteredCollection = new ArrayList<T>();
        for (T t : target) {
            if (predicate.apply(t)) {
                filteredCollection.add(t);
            }
        }
        return filteredCollection;
    }

    /**
     * The Interface Predicate.
     *
     * @param <T>
     *            the generic type
     */
    public interface Predicate<T> {

        /**
         * Apply.
         *
         * @param type
         *            the type
         * @return true, if successful
         */
        public boolean apply(T type);
    }

    /**
     * Sets the search string.
     *
     * @param searchString
     *            the new search string
     */
    public static void setSearchString(String searchString) {
        m_searchString = searchString;
    }

    /**
     * Gets the search string.
     *
     * @return the search string
     */
    public static String getSearchString() {
        return m_searchString;
    }
}
