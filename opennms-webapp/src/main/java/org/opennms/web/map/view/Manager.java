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

package org.opennms.web.map.view;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.opennms.web.map.MapNotFoundException;
import org.opennms.web.map.MapsException;

/**
 * <p>
 * Manager interface.
 * </p>
 *
 * @author maurizio
 * @author antonio
 * @version $Id: $
 * @since 1.8.1
 */
public interface Manager {

    /**
     * Create a new VMap and return it.
     *
     * @param owner
     *            a {@link java.lang.String} object.
     * @param userModifies
     *            a {@link java.lang.String} object.
     * @param width
     *            a int.
     * @param height
     *            a int.
     * @return the new VMap
     */
    VMap newMap(String owner, String userModifies, int width, int height);

    /**
     * Close a VMap previusly opened.
     */
    void closeMap();

    /**
     * <p>
     * openMap
     * </p>
     * .
     *
     * @return the default VMap, if exists
     * @throws MapNotFoundException
     *             the map not found exception
     */
    VMap openMap() throws MapNotFoundException;

    /**
     * Take the map with id in input and return it in VMap form.
     *
     * @param id
     *            a int.
     * @param user
     *            a {@link java.lang.String} object.
     * @param refreshElems
     *            says if refresh the map's elements
     * @return the VMap with identifier id
     * @throws MapNotFoundException
     *             the map not found exception
     * @throws MapsException
     *             the maps exception
     */
    VMap openMap(int id, String user, boolean refreshElems) throws MapNotFoundException, MapsException;

    /**
     * <p>
     * clearMap
     * </p>
     * .
     *
     * @throws MapNotFoundException
     *             the map not found exception
     * @throws MapsException
     *             the maps exception
     */
    void clearMap() throws MapNotFoundException, MapsException;

    /**
     * gets all visible maps for user in input.
     *
     * @param user
     *            a {@link java.lang.String} object.
     * @return a List of MapMenu objects.
     * @throws MapsException
     *             the maps exception
     */
    List<VMapInfo> getVisibleMapsMenu(String user) throws MapsException;

    /**
     * get the default map for specified user in input
     * if exists null otherwise.
     *
     * @param user
     *            a {@link java.lang.String} object.
     * @return a MapMenu object.
     * @throws MapsException
     *             the maps exception
     */
    VMapInfo getDefaultMapsMenu(String user) throws MapsException;

    /**
     * Create a new (not child) empty Submap with the identifier setted to id.
     *
     * @param mapid
     *            a int.
     * @param elementId
     *            a int.
     * @param type
     *            the node type
     * @return a {@link org.opennms.web.map.view.VElement} object.
     * @throws MapsException
     *             the maps exception
     */
    VElement newElement(int mapid, int elementId, String type) throws MapsException;

    /**
     * Create a new (not child) empty Submap with the identifier setted to id.
     *
     * @param mapId
     *            a int.
     * @param elementId
     *            a int.
     * @param type
     *            the node type
     * @param iconname
     *            a {@link java.lang.String} object.
     * @param x
     *            position on the x axis
     * @param y
     *            position on the y axis
     * @return the new VElement
     * @throws MapsException
     *             the maps exception
     */
    VElement newElement(int mapId, int elementId, String type, String iconname, int x, int y) throws MapsException;

    /**
     * delete the map current map.
     *
     * @throws MapsException
     *             the maps exception
     * @throws MapNotFoundException
     *             the map not found exception
     */
    void deleteMap() throws MapsException, MapNotFoundException;

    /**
     * save the map in input.
     *
     * @param map
     *            to save
     * @return a int.
     * @throws MapsException
     *             the maps exception
     */
    int save(VMap map) throws MapsException;

    /**
     * <p>
     * refreshMap
     * </p>
     * .
     *
     * @param map
     *            a {@link org.opennms.web.map.view.VMap} object.
     * @return a {@link org.opennms.web.map.view.VMap} object.
     * @throws MapsException
     *             the maps exception
     */
    VMap refreshMap(VMap map) throws MapsException;

    /**
     * Refreshs avail,severity and status of the map in input and its elements.
     *
     * @param map
     *            a {@link org.opennms.web.map.view.VMap} object.
     * @return the map refreshed
     * @throws MapsException
     *             the maps exception
     */
    VMap reloadMap(VMap map) throws MapsException;

    /**
     * <p>
     * foundLoopOnMaps
     * </p>
     * .
     *
     * @param parentMap
     *            a {@link org.opennms.web.map.view.VMap} object.
     * @param mapId
     *            a int.
     * @return a boolean.
     * @throws MapsException
     *             the maps exception
     */
    boolean foundLoopOnMaps(VMap parentMap, int mapId) throws MapsException;

    /**
     * <p>
     * getElementInfo
     * </p>
     * .
     *
     * @return a {@link java.util.List} object.
     * @throws MapsException
     *             the maps exception
     */
    List<VElementInfo> getElementInfo() throws MapsException;

    /**
     * Get a Map of nodelabel to the
     * container mapids.
     *
     * @param user
     *            a {@link java.lang.String} object.
     * @return a java$util$Map object.
     * @throws MapsException
     *             the maps exception
     */
    Map<String, Set<Integer>> getNodeLabelToMaps(String user) throws MapsException;

    /**
     * <p>
     * searchMap
     * </p>
     * .
     *
     * @param owner
     *            a {@link java.lang.String} object.
     * @param userModifies
     *            a {@link java.lang.String} object.
     * @param width
     *            a int.
     * @param height
     *            a int.
     * @param velems
     *            a {@link java.util.List} object.
     * @return a {@link org.opennms.web.map.view.VMap} object.
     * @throws MapsException
     *             the maps exception
     */
    VMap searchMap(String owner, String userModifies, int width, int height, List<VElement> velems)
            throws MapsException;

    /**
     * <p>
     * getProperties
     * </p>
     * .
     *
     * @param isUserAdmin
     *            a boolean.
     * @return a {@link org.opennms.web.map.view.VProperties} object.
     * @throws MapsException
     *             the maps exception
     */
    VProperties getProperties(boolean isUserAdmin) throws MapsException;

    /**
     * <p>
     * addElements
     * </p>
     * .
     *
     * @param map
     *            a {@link org.opennms.web.map.view.VMap} object.
     * @param velems
     *            a {@link java.util.List} object.
     * @return a {@link org.opennms.web.map.view.VMap} object.
     * @throws MapsException
     *             the maps exception
     */
    VMap addElements(VMap map, List<VElement> velems) throws MapsException;

    /**
     * <p>
     * reloadConfig
     * </p>
     * .
     *
     * @throws MapsException
     *             the maps exception
     */
    void reloadConfig() throws MapsException;

    /**
     * <p>
     * ExecCommand
     * </p>
     * .
     *
     * @param command
     *            the command
     * @return the string
     */
    String execCommand(Command command);

    /**
     * Gets the command.
     *
     * @param id
     *            the id
     * @return the command
     */
    Command getCommand(String id);

    /**
     * Removes the command.
     *
     * @param id
     *            the id
     */
    void removeCommand(String id);

    /**
     * Check command execution.
     *
     * @return true, if successful
     */
    boolean checkCommandExecution();
}
