/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2013 The OpenNMS Group, Inc.
 * OpenNMS(R) is Copyright (C) 1999-2013 The OpenNMS Group, Inc.
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

package org.opennms.features.topology.plugins.browsers;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.opennms.core.criteria.Criteria;
import org.opennms.core.criteria.Order;
import org.opennms.core.criteria.restrictions.Restriction;
import org.opennms.features.topology.api.SelectionContext;
import org.opennms.features.topology.api.SelectionListener;
import org.opennms.features.topology.api.SelectionNotifier;
import org.opennms.netmgt.dao.api.OnmsDao;
import org.slf4j.LoggerFactory;

import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.util.BeanItem;

/**
 * The Class OnmsDaoContainer.
 *
 * @param <T>
 *            the generic type
 * @param <K>
 *            the key type
 */
public abstract class OnmsDaoContainer<T, K extends Serializable> implements Container, Container.Sortable,
        Container.Ordered, Container.Indexed, Container.ItemSetChangeNotifier, SelectionNotifier, SelectionListener {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -9131723065433979979L;

    /** The Constant DEFAULT_PAGE_SIZE. */
    protected static final int DEFAULT_PAGE_SIZE = 200; // items per page/cache

    /** The Constant DEFAULT_SIZE_RELOAD_TIME. */
    protected static final int DEFAULT_SIZE_RELOAD_TIME = 10000; // ms

    /**
     * The Class Page.
     */
    protected static class Page {

        /** The length. */
        protected int length;

        /** The offset. */
        protected int offset;

        /** The size. */
        protected final Size size;

        /**
         * Instantiates a new page.
         *
         * @param length
         *            the length
         * @param size
         *            the size
         */
        public Page(int length, Size size) {
            this.length = length;
            this.size = size;
        }

        /**
         * Updates the offset of the current page and returns weather the offset
         * was changed or not.
         *
         * @param index
         *            the index
         * @return true when the offset has changed, false otherwise.
         */
        public boolean updateOffset(int index) {
            int oldOffset = offset;
            if (index < 0)
                index = 0;
            offset = index / length * length;
            return oldOffset != offset; // an update has been made
        }

        /**
         * Gets the start.
         *
         * @return the start
         */
        public int getStart() {
            return offset;
        }
    }

    /**
     * The Class Size.
     */
    protected static class Size {

        /** The last update. */
        private Date lastUpdate = null;

        /** The value. */
        private int value;

        /** The reload timer. */
        private int reloadTimer; // in ms

        /** The reload strategy. */
        private final SizeReloadStrategy reloadStrategy;

        /**
         * Instantiates a new size.
         *
         * @param reloadTimer
         *            the reload timer
         * @param reloadStrategy
         *            the reload strategy
         */
        protected Size(int reloadTimer, SizeReloadStrategy reloadStrategy) {
            this.reloadTimer = reloadTimer == 0 ? DEFAULT_SIZE_RELOAD_TIME : reloadTimer;
            this.reloadStrategy = reloadStrategy;
        }

        /**
         * Gets the value.
         *
         * @return the value
         */
        public final synchronized int getValue() {
            if (isOutdated())
                reloadSize();
            return value;
        }

        /**
         * Checks if is outdated.
         *
         * @return true, if is outdated
         */
        private boolean isOutdated() {
            return lastUpdate == null || lastUpdate.getTime() + reloadTimer < new Date().getTime();
        }

        /**
         * Reload size.
         */
        private synchronized void reloadSize() {
            value = reloadStrategy.reload();
            lastUpdate = new Date();
        }

        /**
         * Sets the outdated.
         */
        private void setOutdated() {
            lastUpdate = null;
        }
    }

    /**
     * The Interface SizeReloadStrategy.
     */
    protected static interface SizeReloadStrategy {

        /**
         * Reload.
         *
         * @return the int
         */
        int reload();
    }

    /**
     * The Class SortEntry.
     */
    protected static class SortEntry implements Comparable<SortEntry> {

        /** The property id. */
        private final String propertyId;

        /** The ascending. */
        private final boolean ascending;

        /**
         * Instantiates a new sort entry.
         *
         * @param propertyId
         *            the property id
         * @param ascending
         *            the ascending
         */
        private SortEntry(String propertyId, boolean ascending) {
            this.propertyId = propertyId;
            this.ascending = ascending;
        }

        /* (non-Javadoc)
         * @see java.lang.Comparable#compareTo(java.lang.Object)
         */
        @Override
        public int compareTo(SortEntry o) {
            return propertyId.compareTo(o.propertyId);
        }
    }

    /**
     * The Class Cache.
     */
    protected class Cache {
        // Maps each itemId to a item.
        /** The cache content. */
        private Map<K, BeanItem<T>> cacheContent = new HashMap<K, BeanItem<T>>();

        // Maps each row to an itemId
        /** The row map. */
        private Map<Integer, K> rowMap = new HashMap<Integer, K>();

        /**
         * Instantiates a new cache.
         */
        private Cache() {
        }

        /**
         * Contains item id.
         *
         * @param itemId
         *            the item id
         * @return true, if successful
         */
        public boolean containsItemId(K itemId) {
            if (itemId == null)
                return false;
            return cacheContent.containsKey(itemId);
        }

        /**
         * Contains index.
         *
         * @param index
         *            the index
         * @return true, if successful
         */
        public boolean containsIndex(int index) {
            return rowMap.containsKey(Integer.valueOf(index));
        }

        /**
         * Gets the item.
         *
         * @param itemId
         *            the item id
         * @return the item
         */
        public BeanItem<T> getItem(K itemId) {
            if (containsItemId(itemId))
                return cacheContent.get(itemId);
            return null;
        }

        /**
         * Adds the item.
         *
         * @param rowNumber
         *            the row number
         * @param itemId
         *            the item id
         * @param bean
         *            the bean
         */
        public void addItem(int rowNumber, K itemId, T bean) {
            if (containsItemId(itemId))
                return; // already added
            cacheContent.put(itemId, new BeanItem<T>(bean));
            rowMap.put(rowNumber, itemId);
        }

        /**
         * Gets the index.
         *
         * @param itemId
         *            the item id
         * @return the index
         */
        public int getIndex(K itemId) {
            for (Map.Entry<Integer, K> eachRow : rowMap.entrySet()) {
                if (eachRow.getValue().equals(itemId))
                    return eachRow.getKey();
            }
            return -1; // not found
        }

        /**
         * Reset.
         */
        public void reset() {
            cacheContent.clear();
            rowMap.clear();
        }

        /**
         * Gets the item id.
         *
         * @param rowIndex
         *            the row index
         * @return the item id
         */
        public K getItemId(int rowIndex) {
            return rowMap.get(Integer.valueOf(rowIndex));
        }

        /**
         * Reload.
         *
         * @param page
         *            the page
         */
        public void reload(Page page) {
            size.setOutdated(); // force to be outdated
            reset();
            List<T> beans = getItemsForCache(m_dao, page);
            if (beans == null)
                return;
            int rowNumber = page.getStart();
            for (T eachBean : beans) {
                addItem(rowNumber, getId(eachBean), eachBean);
                doItemAddedCallBack(rowNumber, getId(eachBean), eachBean);
                rowNumber++;
            }
        }
    }

    /** The m_dao. */
    private final OnmsDao<T, K> m_dao;

    // ORDER/SORTING
    /** The m_orders. */
    private final List<Order> m_orders = new ArrayList<Order>();

    // FILTERING
    /** The m_restrictions. */
    private final List<Restriction> m_restrictions = new ArrayList<Restriction>();

    /** The m_sort entries. */
    private final List<SortEntry> m_sortEntries = new ArrayList<SortEntry>();

    /** TODO: Fix concurrent access to this field. */
    private final Collection<ItemSetChangeListener> m_itemSetChangeListeners = new HashSet<ItemSetChangeListener>();

    /** TODO: Fix concurrent access to this field. */
    private Collection<SelectionListener> m_selectionListeners = new HashSet<SelectionListener>();

    /** The m_bean to hibernate property mapping. */
    private final Map<String, String> m_beanToHibernatePropertyMapping = new HashMap<String, String>();

    /** The size. */
    private final Size size;

    /** The page. */
    private final Page page;

    /** The cache. */
    private final Cache cache;

    /** The m_item class. */
    private final Class<T> m_itemClass;

    /** The m_properties. */
    private Map<Object, Class<?>> m_properties;

    /**
     * Instantiates a new onms dao container.
     *
     * @param itemClass
     *            the item class
     * @param dao
     *            the dao
     */
    public OnmsDaoContainer(Class<T> itemClass, OnmsDao<T, K> dao) {
        m_itemClass = itemClass;
        m_dao = dao;
        size = new Size(DEFAULT_SIZE_RELOAD_TIME, new SizeReloadStrategy() {
            @Override
            public int reload() {
                return m_dao.countMatching(getCriteria(null, false)); // no
                                                                      // paging!!!!
            }
        });
        page = new Page(DEFAULT_PAGE_SIZE, size);
        cache = new Cache();
    }

    /* (non-Javadoc)
     * @see com.vaadin.data.Container#containsId(java.lang.Object)
     */
    @Override
    public boolean containsId(Object itemId) {
        if (itemId == null)
            return false;
        if (cache.containsItemId((K) itemId))
            return true;
        int index = indexOfId(itemId);
        return index >= 0;
    }

    /* (non-Javadoc)
     * @see com.vaadin.data.Container#getContainerProperty(java.lang.Object, java.lang.Object)
     */
    @Override
    public Property<?> getContainerProperty(Object itemId, Object propertyId) {
        Item item = getItem(itemId);
        if (item == null) {
            return null;
        } else {
            return item.getItemProperty(propertyId);
        }
    }

    /* (non-Javadoc)
     * @see com.vaadin.data.Container#getContainerPropertyIds()
     */
    @Override
    public Collection<?> getContainerPropertyIds() {
        loadPropertiesIfNull();
        updateContainerPropertyIds(m_properties);
        return Collections.unmodifiableCollection(m_properties.keySet());
    }

    /* (non-Javadoc)
     * @see com.vaadin.data.Container#getItem(java.lang.Object)
     */
    @Override
    public Item getItem(Object itemId) {
        if (itemId == null)
            return null;
        if (cache.containsItemId((K) itemId))
            return cache.getItem((K) itemId);

        // not in cache, get the right page
        final int index = indexOfId(itemId);
        if (index == -1)
            return null; // not in container

        // page has the item now in container
        return cache.getItem((K) itemId);
    }

    /**
     * Gets the item class.
     *
     * @return the item class
     */
    public Class<T> getItemClass() {
        return m_itemClass;
    }

    /**
     * Gets the id.
     *
     * @param bean
     *            the bean
     * @return the id
     */
    protected abstract K getId(T bean);

    /* (non-Javadoc)
     * @see com.vaadin.data.Container#getType(java.lang.Object)
     */
    @Override
    public Class<?> getType(Object propertyId) {
        return m_properties.get(propertyId);
    }

    /* (non-Javadoc)
     * @see com.vaadin.data.Container#removeAllItems()
     */
    @Override
    public boolean removeAllItems() throws UnsupportedOperationException {
        m_dao.clear();
        return true;
    }

    /* (non-Javadoc)
     * @see com.vaadin.data.Container#removeItem(java.lang.Object)
     */
    @Override
    public boolean removeItem(Object itemId) throws UnsupportedOperationException {
        m_dao.delete((K) itemId);
        return true;
    }

    /* (non-Javadoc)
     * @see com.vaadin.data.Container#size()
     */
    @Override
    public int size() {
        return size.getValue();
    }

    /* (non-Javadoc)
     * @see com.vaadin.data.Container.Ordered#firstItemId()
     */
    @Override
    public Object firstItemId() {
        if (!cache.containsIndex(0)) {
            updatePage(0);
        }
        return cache.getItemId(0);
    }

    /* (non-Javadoc)
     * @see com.vaadin.data.Container.Ordered#lastItemId()
     */
    @Override
    public Object lastItemId() {
        int lastIndex = size() - 1;
        if (!cache.containsIndex(lastIndex)) {
            updatePage(lastIndex);
        }
        return cache.getItemId(lastIndex);
    }

    /* (non-Javadoc)
     * @see com.vaadin.data.Container.Ordered#isFirstId(java.lang.Object)
     */
    @Override
    public boolean isFirstId(Object itemId) {
        return firstItemId().equals(itemId);
    }

    /**
     * Update page.
     *
     * @param index
     *            the index
     */
    private void updatePage(final int index) {
        boolean changed = page.updateOffset(index);
        if (changed) {
            cache.reload(page);
        }
    }

    /* (non-Javadoc)
     * @see com.vaadin.data.Container.Ordered#isLastId(java.lang.Object)
     */
    @Override
    public boolean isLastId(Object itemId) {
        return lastItemId().equals(itemId);
    }

    /* (non-Javadoc)
     * @see com.vaadin.data.Container.Ordered#nextItemId(java.lang.Object)
     */
    @Override
    public Object nextItemId(Object itemId) {
        if (itemId == null)
            return null;
        int nextIdIndex = indexOfId(itemId) + 1;
        if (cache.getItemId(nextIdIndex) == null) {
            updatePage(page.offset + page.length);
        }
        return cache.getItemId(nextIdIndex);
    }

    /* (non-Javadoc)
     * @see com.vaadin.data.Container.Ordered#prevItemId(java.lang.Object)
     */
    @Override
    public Object prevItemId(Object itemId) {
        if (itemId == null)
            return null;
        int prevIdIndex = indexOfId(itemId) - 1;
        if (cache.getItemId(prevIdIndex) == null) {
            updatePage(prevIdIndex);
        }
        return cache.getItemId(prevIdIndex);
    }

    /**
     * This function returns {@link #getContainerPropertyIds()}.
     *
     * @return the sortable container property ids
     */
    @Override
    public Collection<?> getSortableContainerPropertyIds() {
        return this.getContainerPropertyIds();
    }

    /* (non-Javadoc)
     * @see com.vaadin.data.Container.Sortable#sort(java.lang.Object[], boolean[])
     */
    @Override
    public void sort(Object[] propertyId, boolean[] ascending) {
        List<SortEntry> newSortEntries = createSortEntries(propertyId, ascending);
        if (!m_sortEntries.equals(newSortEntries)) {
            m_sortEntries.clear();
            m_sortEntries.addAll(newSortEntries);
            m_orders.clear();
            for (int i = 0; i < propertyId.length; i++) {
                final String beanProperty = (String) propertyId[i];
                String hibernateProperty = m_beanToHibernatePropertyMapping.get(beanProperty);
                if (hibernateProperty == null)
                    hibernateProperty = beanProperty;
                if (ascending[i]) {
                    m_orders.add(Order.asc(hibernateProperty));
                } else {
                    m_orders.add(Order.desc(hibernateProperty));
                }
            }
            cache.reload(page);
        }
    }

    /**
     * Creates the sort entries.
     *
     * @param propertyId
     *            the property id
     * @param ascending
     *            the ascending
     * @return the list
     */
    protected List<SortEntry> createSortEntries(Object[] propertyId, boolean[] ascending) {
        List<SortEntry> sortEntries = new ArrayList<SortEntry>();
        for (int i = 0; i < propertyId.length; i++) {
            sortEntries.add(new SortEntry((String) propertyId[i], ascending[i]));
        }
        return sortEntries;
    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.api.SelectionListener#selectionChanged(org.opennms.features.topology.api.SelectionContext)
     */
    @Override
    public abstract void selectionChanged(SelectionContext selectionContext);

    /* (non-Javadoc)
     * @see com.vaadin.data.Container.ItemSetChangeNotifier#addListener(com.vaadin.data.Container.ItemSetChangeListener)
     */
    @Override
    public void addListener(ItemSetChangeListener listener) {
        addItemSetChangeListener(listener);
    }

    /* (non-Javadoc)
     * @see com.vaadin.data.Container.ItemSetChangeNotifier#removeListener(com.vaadin.data.Container.ItemSetChangeListener)
     */
    @Override
    public void removeListener(ItemSetChangeListener listener) {
        removeItemSetChangeListener(listener);
    }

    /* (non-Javadoc)
     * @see com.vaadin.data.Container.ItemSetChangeNotifier#addItemSetChangeListener(com.vaadin.data.Container.ItemSetChangeListener)
     */
    @Override
    public void addItemSetChangeListener(ItemSetChangeListener listener) {
        m_itemSetChangeListeners.add(listener);
    }

    /* (non-Javadoc)
     * @see com.vaadin.data.Container.ItemSetChangeNotifier#removeItemSetChangeListener(com.vaadin.data.Container.ItemSetChangeListener)
     */
    @Override
    public void removeItemSetChangeListener(ItemSetChangeListener listener) {
        m_itemSetChangeListeners.remove(listener);
    }

    /**
     * Fire item set changed event.
     */
    protected void fireItemSetChangedEvent() {
        ItemSetChangeEvent event = new ItemSetChangeEvent() {
            private static final long serialVersionUID = -2796401359570611938L;

            @Override
            public Container getContainer() {
                return OnmsDaoContainer.this;
            }
        };
        for (ItemSetChangeListener listener : m_itemSetChangeListeners) {
            listener.containerItemSetChange(event);
        }
    }

    /**
     * Sets the restrictions.
     *
     * @param newRestrictions
     *            the new restrictions
     */
    public void setRestrictions(List<Restriction> newRestrictions) {
        m_restrictions.clear();
        if (newRestrictions == null)
            return;
        m_restrictions.addAll(newRestrictions);
    }

    /**
     * Gets the restrictions.
     *
     * @return the restrictions
     */
    public List<Restriction> getRestrictions() {
        return Collections.unmodifiableList(m_restrictions);
    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.api.SelectionNotifier#addSelectionListener(org.opennms.features.topology.api.SelectionListener)
     */
    @Override
    public void addSelectionListener(SelectionListener listener) {
        if (listener != null) {
            m_selectionListeners.add(listener);
        }
    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.api.SelectionNotifier#setSelectionListeners(java.util.Set)
     */
    @Override
    public void setSelectionListeners(Set<SelectionListener> listeners) {
        m_selectionListeners = listeners;
    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.api.SelectionNotifier#removeSelectionListener(org.opennms.features.topology.api.SelectionListener)
     */
    @Override
    public void removeSelectionListener(SelectionListener listener) {
        m_selectionListeners.remove(listener);
    }

    /**
     * Adds the bean to hibernate property mapping.
     *
     * @param key
     *            the key
     * @param value
     *            the value
     */
    public void addBeanToHibernatePropertyMapping(final String key, final String value) {
        m_beanToHibernatePropertyMapping.put(key, value);
    }

    /* (non-Javadoc)
     * @see com.vaadin.data.Container#getItemIds()
     */
    @Override
    public Collection<?> getItemIds() {
        return getItemIds(0, size());
    }

    /* (non-Javadoc)
     * @see com.vaadin.data.Container.Indexed#getItemIds(int, int)
     */
    @Override
    public List<?> getItemIds(int startIndex, int numberOfItems) {
        int endIndex = startIndex + numberOfItems;
        if (endIndex > size())
            endIndex = size() - 1;
        List<K> itemIds = new ArrayList<K>();
        for (int i = startIndex; i < endIndex; i++) {
            itemIds.add((K) getIdByIndex(i));
        }
        return itemIds;
    }

    /* (non-Javadoc)
     * @see com.vaadin.data.Container.Indexed#indexOfId(java.lang.Object)
     */
    @Override
    public int indexOfId(Object itemId) {
        if (cache.containsItemId(((K) itemId)))
            return cache.getIndex((K) itemId); // cache hit *yay*

        // itemId is not in the cache so we try to find the right page
        boolean circled = false; // we have run through the whole cache
        int startOffset = page.offset;
        do {
            int indexOfId = page.offset + page.length; // we have to start
                                                       // somewhere
            // check if we are not at the end, if so, start from beginning
            if (indexOfId > size()) {
                indexOfId = 0;
            }
            // reload next page
            updatePage(indexOfId);
            // check if element now is in cache
            if (cache.containsItemId((K) itemId)) {
                return cache.getIndex((K) itemId);
            }
            // ensure that we have not circled yet
            if (startOffset == indexOfId) {
                circled = true;
            }
        } while (!circled); // continue as far as we have not found the element
                            // yet
        return -1; // not found
    }

    /* (non-Javadoc)
     * @see com.vaadin.data.Container.Indexed#getIdByIndex(int)
     */
    @Override
    public Object getIdByIndex(int index) {
        if (cache.containsIndex(index))
            return cache.getItemId(index);
        updatePage(index);
        return cache.getItemId(index); // it is now in the cache or it does not
                                       // exist
    }

    /* (non-Javadoc)
     * @see com.vaadin.data.Container.Indexed#addItemAt(int)
     */
    @Override
    public Object addItemAt(int index) throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Cannot add new items to this container");
    }

    /* (non-Javadoc)
     * @see com.vaadin.data.Container.Indexed#addItemAt(int, java.lang.Object)
     */
    @Override
    public Item addItemAt(int index, Object newItemId) throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Cannot add new items to this container");
    }

    /* (non-Javadoc)
     * @see com.vaadin.data.Container.Ordered#addItemAfter(java.lang.Object)
     */
    @Override
    public Object addItemAfter(Object previousItemId) throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Cannot add new items to this container");
    }

    /* (non-Javadoc)
     * @see com.vaadin.data.Container.Ordered#addItemAfter(java.lang.Object, java.lang.Object)
     */
    @Override
    public Item addItemAfter(Object previousItemId, Object newItemId) throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Cannot add new items to this container");
    }

    /* (non-Javadoc)
     * @see com.vaadin.data.Container#addContainerProperty(java.lang.Object, java.lang.Class, java.lang.Object)
     */
    @Override
    public final boolean addContainerProperty(Object propertyId, Class<?> type, Object defaultValue)
            throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Cannot add new properties to objects in this container");
    }

    /**
     * Can be overridden if you want to support adding items.
     *
     * @return the object
     * @throws UnsupportedOperationException
     *             the unsupported operation exception
     */
    @Override
    public Object addItem() throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Cannot add new items to this container");
    }

    /**
     * Can be overridden if you want to support adding items.
     *
     * @param itemId
     *            the item id
     * @return the item
     * @throws UnsupportedOperationException
     *             the unsupported operation exception
     */
    @Override
    public Item addItem(Object itemId) throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Cannot add new items to this container");
    }

    /* (non-Javadoc)
     * @see com.vaadin.data.Container#removeContainerProperty(java.lang.Object)
     */
    @Override
    public final boolean removeContainerProperty(Object propertyId) throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Cannot remove properties from objects in this container");
    }

    /**
     * Update container property ids.
     *
     * @param properties
     *            the properties
     */
    protected void updateContainerPropertyIds(Map<Object, Class<?>> properties) {
        // by default we do nothing with the properties;
    }

    /**
     * Creates a {@link Criteria} object to get data from database.
     * If considerPaging is set Limit and offset values are added as
     * restriction.
     *
     * @param page
     *            the page
     * @param doOrder
     *            the do order
     * @return the criteria
     */
    protected Criteria getCriteria(Page page, boolean doOrder) {
        Criteria tmpCriteria = new Criteria(getItemClass());
        for (Restriction eachRestriction : m_restrictions) {
            tmpCriteria.addRestriction(eachRestriction);
        }
        if (doOrder) {
            tmpCriteria.setOrders(m_orders);
        }
        if (page != null) {
            tmpCriteria.setOffset(page.offset);
            tmpCriteria.setLimit(page.length);
        }
        addAdditionalCriteriaOptions(tmpCriteria, page, doOrder);
        return tmpCriteria;
    }

    // must be overwritten by subclass if you want to add some alias and so on
    /**
     * Adds the additional criteria options.
     *
     * @param criteria
     *            the criteria
     * @param page
     *            the page
     * @param doOrder
     *            the do order
     */
    protected void addAdditionalCriteriaOptions(Criteria criteria, Page page, boolean doOrder) {

    }

    /**
     * Do item added call back.
     *
     * @param rowNumber
     *            the row number
     * @param id
     *            the id
     * @param eachBean
     *            the each bean
     */
    protected void doItemAddedCallBack(int rowNumber, K id, T eachBean) {

    }

    /**
     * Gets the items for cache.
     *
     * @param dao
     *            the dao
     * @param page
     *            the page
     * @return the items for cache
     */
    protected List<T> getItemsForCache(final OnmsDao<T, K> dao, final Page page) {
        return dao.findMatching(getCriteria(page, true));
    }

    /**
     * Gets the cache.
     *
     * @return the cache
     */
    protected Cache getCache() {
        return cache;
    }

    /**
     * Gets the page.
     *
     * @return the page
     */
    protected Page getPage() {
        return page;
    }

    /**
     * Load properties if null.
     */
    private synchronized void loadPropertiesIfNull() {
        if (m_properties == null) {
            m_properties = new TreeMap<Object, Class<?>>();
            BeanItem<T> item = null;
            try {
                item = new BeanItem<T>(m_itemClass.newInstance());
            } catch (InstantiationException e) {
                LoggerFactory.getLogger(getClass()).error("Class {} does not have a default constructor. Cannot create an instance.",
                                                          getItemClass());
            } catch (IllegalAccessException e) {
                LoggerFactory.getLogger(getClass()).error("Class {} does not have a public default constructor. Cannot create an instance.",
                                                          getItemClass());
            }
            for (Object key : item.getItemPropertyIds()) {
                m_properties.put(key, item.getItemProperty(key).getType());
            }
        }
    }
}
