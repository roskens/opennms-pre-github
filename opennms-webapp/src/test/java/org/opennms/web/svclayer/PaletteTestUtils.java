/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2006-2012 The OpenNMS Group, Inc.
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

package org.opennms.web.svclayer;

import java.util.Iterator;
import java.util.List;

import junit.framework.Assert;

/**
 * The Class PaletteTestUtils.
 */
public abstract class PaletteTestUtils extends Assert {

    /**
     * Assert palette item equals.
     *
     * @param expectedItem
     *            the expected item
     * @param actualItem
     *            the actual item
     */
    public static void assertPaletteItemEquals(final PaletteItem expectedItem, final PaletteItem actualItem) {
        assertEquals(expectedItem.getId(), actualItem.getId());
        assertEquals(expectedItem.getLabel(), actualItem.getLabel());
        assertEquals(expectedItem.isSpacer(), actualItem.isSpacer());
    }

    /**
     * Assert palette cateries equals.
     *
     * @param expectedCategories
     *            the expected categories
     * @param actualCategories
     *            the actual categories
     */
    public static void assertPaletteCateriesEquals(final List<PaletteCategory> expectedCategories,
            final List<PaletteCategory> actualCategories) {
        if (expectedCategories == null) {
            assertNull(actualCategories);
            return;
        }
        // assertEquals(expectedCategories.size(), actualCategories.size());
        Iterator<PaletteCategory> iter = actualCategories.iterator();
        for (PaletteCategory expectedCategory : expectedCategories) {
            assertTrue(iter.hasNext());
            PaletteCategory actualCategory = iter.next();
            PaletteTestUtils.assertPaletteCategoryEquals(expectedCategory, actualCategory);
        }
        assertFalse(iter.hasNext());
    }

    /**
     * Assert palette equals.
     *
     * @param expectedPalette
     *            the expected palette
     * @param actualPalette
     *            the actual palette
     */
    public static void assertPaletteEquals(final Palette expectedPalette, final Palette actualPalette) {
        assertEquals(expectedPalette.getLabel(), actualPalette.getLabel());
        assertPaletteCateriesEquals(expectedPalette.getCategories(), actualPalette.getCategories());
    }

    /**
     * Assert palette items equal.
     *
     * @param expectedItems
     *            the expected items
     * @param actualItems
     *            the actual items
     */
    public static void assertPaletteItemsEqual(final List<PaletteItem> expectedItems,
            final List<PaletteItem> actualItems) {
        if (expectedItems == null) {
            assertNull(actualItems);
            return;
        }
        // assertEquals(expectedItems.size(), actualItems.size());
        Iterator<PaletteItem> iter = actualItems.iterator();
        for (PaletteItem expectedItem : expectedItems) {
            assertTrue("More expected items than actual, Missing: " + expectedItem, iter.hasNext());
            PaletteItem actualItem = iter.next();
            assertPaletteItemEquals(expectedItem, actualItem);
        }

        if (iter.hasNext()) {
            fail("More actual items than expected, Found: " + iter.next());
        }
    }

    /**
     * Assert palette category equals.
     *
     * @param expectedCategory
     *            the expected category
     * @param actualCategory
     *            the actual category
     */
    public static void assertPaletteCategoryEquals(final PaletteCategory expectedCategory,
            final PaletteCategory actualCategory) {
        assertEquals("Unexpected category label", expectedCategory.getLabel(), actualCategory.getLabel());
        assertPaletteItemsEqual(expectedCategory.getItems(), actualCategory.getItems());
    }

}
