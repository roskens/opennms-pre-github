/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2009-2012 The OpenNMS Group, Inc.
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

package org.opennms.netmgt.dao;

import java.util.Collection;

import org.opennms.netmgt.model.OnmsMap;
import org.opennms.netmgt.model.OnmsMapElement;

public interface OnmsMapElementDao extends OnmsDao<OnmsMapElement, Integer> {
    OnmsMapElement findElement(int elementId, String type, OnmsMap map);
    Collection<OnmsMapElement> findElementsByMapId(OnmsMap map);
    Collection<OnmsMapElement> findElementsByNodeId(int nodeId);
    Collection<OnmsMapElement> findElementsByElementIdAndType(int elementId, String type);
    Collection<OnmsMapElement> findElementsByMapIdAndType(int mapId, String type);
    Collection<OnmsMapElement> findElementsByType(String type);
    Collection<OnmsMapElement> findMapElementsOnMap(int mapId);
    Collection<OnmsMapElement> findNodeElementsOnMap(int mapId);
    int deleteElementsByMapId(OnmsMap map);
    int deleteElementsByNodeid(int nodeId);
    int deleteElementsByType(String type);
    int deleteElementsByElementIdAndType(int elementId, String type);
    int deleteElementsByMapType(String mapType);
    int countElementsOnMap(int mapid);
}
