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

import static org.opennms.web.svclayer.PaletteTestUtils.assertPaletteEquals;
import junit.framework.TestCase;

/**
 * The Class PaletteBuilderTest.
 */
public class PaletteBuilderTest extends TestCase {

    /** The m_palette. */
    private Palette m_palette;

    /** The m_current cat. */
    private PaletteCategory m_currentCat;

    /**
     * Test building palatte.
     */
    public final void testBuildingPalatte() {

        m_palette = new Palette("paletteLabel");
        PaletteBuilder m_builder = new PaletteBuilder("paletteLabel");

        m_builder.addCategory("cat1");
        createCategory("cat1");
        m_builder.addItem("c1a_id", "c1a_label");
        createItem("c1a_id", "c1a_label");
        m_builder.addItem("c1b_id", "c1b_label");
        createItem("c1b_id", "c1b_label");
        m_builder.addItem("c1c_id", "c1c_label");
        createItem("c1c_id", "c1c_label");

        m_builder.addCategory("cat2");
        createCategory("cat2");
        m_builder.addItem("c2a_id", "c2a_label");
        createItem("c2a_id", "c2a_label");
        m_builder.addSpacer();
        createSpacer();
        m_builder.addItem("c2b_id", "c2b_label");
        createItem("c2b_id", "c2b_label");

        assertPaletteEquals(m_palette, m_builder.getPalette());

    }

    /**
     * Creates the spacer.
     */
    private void createSpacer() {
        m_currentCat.addItem(PaletteItem.SPACER);
    }

    /**
     * Creates the category.
     *
     * @param label
     *            the label
     */
    private void createCategory(final String label) {
        PaletteCategory cat = new PaletteCategory(label);
        m_palette.addCategory(cat);
        m_currentCat = cat;
    }

    /**
     * Creates the item.
     *
     * @param id
     *            the id
     * @param label
     *            the label
     */
    private void createItem(final String id, final String label) {
        PaletteItem item = new PaletteItem(id, label);
        m_currentCat.addItem(item);
    }

}
