/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2012 The OpenNMS Group, Inc.
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

package org.opennms.features.jmxconfiggenerator.webui.data;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.util.AbstractInMemoryContainer;
import com.vaadin.data.util.VaadinPropertyDescriptor;

/**
 * This class represents a vaadin container (data source). Therefore it extends
 * <code>AbstractInMemoryContainer</code>. Usually
 * <code>BeanItemContainer</code> or <code>AbstractBeanContainer</code> would be
 * the best container to use. But we need to add a property to the
 * container items which indicates if any item is selected. Due to some
 * limitations we cannot inherit BeanItemContainer
 * or AbstractBeanContainer to fulfill this requirement. Therefore this class is
 * mainly a rough copy of the <code>BeanItemContainer</code> but does not
 * support any kind of filtering. This may be included in future releases.
 *
 * @param <T>
 *            the type of the bean we want to store in SelectableItem
 * @author Markus von Rüden
 * @see SelectableItem
 */
public class SelectableBeanItemContainer<T> extends AbstractInMemoryContainer<T, String, SelectableItem<T>> {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** Mapping of an itemId to a <code>SelectableItem</code>. */
    private final Map<T, SelectableItem<T>> itemIdToItem = new HashMap<T, SelectableItem<T>>();

    /**
     * We build a model to make a mapping between bean objects and the selected
     * property. In this case we do not have to
     * deal with reading and writing the data to the underlying been.
     */
    private final Map<String, VaadinPropertyDescriptor> model;

    /** The type of our bean. */
    private final Class<? super T> type;

    /**
     * Instantiates a new selectable bean item container.
     *
     * @param type
     *            the type
     */
    public SelectableBeanItemContainer(Class<? super T> type) {
        model = SelectableItem.getPropertyDescriptors(type);
        this.type = type;
    }

    /* (non-Javadoc)
     * @see com.vaadin.data.util.AbstractInMemoryContainer#getUnfilteredItem(java.lang.Object)
     */
    @Override
    protected SelectableItem<T> getUnfilteredItem(Object itemId) {
        return itemIdToItem.get(itemId);
    }

    /* (non-Javadoc)
     * @see com.vaadin.data.Container#getContainerPropertyIds()
     */
    @Override
    public Collection<String> getContainerPropertyIds() {
        return model.keySet();
    }

    /* (non-Javadoc)
     * @see com.vaadin.data.Container#getContainerProperty(java.lang.Object, java.lang.Object)
     */
    @Override
    public Property getContainerProperty(Object itemId, Object propertyId) {
        Item item = getItem(itemId);
        if (item == null) {
            return null;
        }
        return item.getItemProperty(propertyId);
    }

    /* (non-Javadoc)
     * @see com.vaadin.data.Container#getType(java.lang.Object)
     */
    @Override
    public Class<?> getType(Object propertyId) {
        return model.get(propertyId).getPropertyType();
    }

    /**
     * Checks if is valid.
     *
     * @param itemId
     *            the item id
     * @return true, if is valid
     */
    private boolean isValid(Object itemId) {
        return itemId != null && type.isAssignableFrom(itemId.getClass());
    }

    /* (non-Javadoc)
     * @see com.vaadin.data.util.AbstractInMemoryContainer#addItemAt(int, java.lang.Object)
     */
    @Override
    public Item addItemAt(int index, Object newItemId) {
        if (!isValid(newItemId)) {
            return null;
        }
        return internalAddItemAt(index, (T) newItemId, createItem((T) newItemId), true);
    }

    /**
     * Creates the item.
     *
     * @param itemId
     *            the item id
     * @return the selectable item
     */
    private SelectableItem<T> createItem(T itemId) {
        if (itemId == null) {
            return null;
        }
        return new SelectableItem<T>(itemId, model);
    }

    /* (non-Javadoc)
     * @see com.vaadin.data.util.AbstractInMemoryContainer#addItemAfter(java.lang.Object, java.lang.Object)
     */
    @Override
    public Item addItemAfter(Object previousItemId, Object newItemId) {
        if (!isValid(previousItemId) || !isValid(newItemId)) {
            return null;
        }
        return internalAddItemAfter((T) previousItemId, (T) newItemId, createItem((T) newItemId), true);
    }

    /* (non-Javadoc)
     * @see com.vaadin.data.util.AbstractInMemoryContainer#addItem(java.lang.Object)
     */
    @Override
    public Item addItem(Object itemId) {
        if (!isValid(itemId)) {
            return null;
        }
        return internalAddItemAtEnd((T) itemId, createItem((T) itemId), true);
    }

    /* (non-Javadoc)
     * @see com.vaadin.data.util.AbstractInMemoryContainer#removeItem(java.lang.Object)
     */
    @Override
    public boolean removeItem(Object itemId) {
        int position = indexOfId(itemId);
        if (internalRemoveItem(itemId)) {
            itemIdToItem.remove(itemId);
            fireItemRemoved(position, itemId);
            return true;
        }
        return false;
    }

    /* (non-Javadoc)
     * @see com.vaadin.data.util.AbstractInMemoryContainer#removeAllItems()
     */
    @Override
    public boolean removeAllItems() {
        internalRemoveAllItems();
        itemIdToItem.clear();
        fireItemSetChange();
        return true;
    }

    /* (non-Javadoc)
     * @see com.vaadin.data.util.AbstractInMemoryContainer#registerNewItem(int, java.lang.Object, com.vaadin.data.Item)
     */
    @Override
    protected void registerNewItem(int position, T itemId, SelectableItem<T> item) {
        itemIdToItem.put(itemId, item);
    }

    /**
     * we do not allow adding additional properties to the container.
     *
     * @param propertyId
     *            the property id
     * @param type
     *            the type
     * @param defaultValue
     *            the default value
     * @return true, if successful
     * @throws UnsupportedOperationException
     *             the unsupported operation exception
     */
    @Override
    public boolean addContainerProperty(Object propertyId, Class<?> type, Object defaultValue)
            throws UnsupportedOperationException {
        throw new UnsupportedOperationException(
                                                "Adding container properties not supported. Override the addContainerProperty() method if required.");
    }

    /**
     * We do not allow removing properties from the container. This may change
     * in future releases.
     *
     * @param propertyId
     *            the property id
     * @return true, if successful
     * @throws UnsupportedOperationException
     *             the unsupported operation exception
     */
    @Override
    public boolean removeContainerProperty(Object propertyId) throws UnsupportedOperationException {
        throw new UnsupportedOperationException(
                                                "Removing container properties not supported. Override the addContainerProperty() method if required.");
    }
}
