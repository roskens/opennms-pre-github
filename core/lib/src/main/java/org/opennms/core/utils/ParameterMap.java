//
// This file is part of the OpenNMS(R) Application.
//
// OpenNMS(R) is Copyright (C) 2002-2003 The OpenNMS Group, Inc.  All rights reserved.
// OpenNMS(R) is a derivative work, containing both original code, included code and modified
// code that was published under the GNU General Public License. Copyrights for modified 
// and included code are below.
//
// OpenNMS(R) is a registered trademark of The OpenNMS Group, Inc.
//
// Modifications:
//
// 31 Jan 2003: Cleaned up some unused imports.
//
// Original code base Copyright (C) 1999-2001 Oculan Corp.  All rights reserved.
//
// This program is free software; you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation; either version 2 of the License, or
// (at your option) any later version.
//
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.                                                            
//
// You should have received a copy of the GNU General Public License
// along with this program; if not, write to the Free Software
// Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
//       
// For more information contact: 
//      OpenNMS Licensing       <license@opennms.org>
//      http://www.opennms.org/
//      http://www.opennms.com/
//

package org.opennms.core.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * Convenience class for looking up string and integer values in a parameter
 * map.
 */
public abstract class ParameterMap {
	
    /**
     * This method is used to lookup a specific key in the map. If the mapped
     * value is a string it is converted to a long and the original string
     * value is replaced in the map. The converted value is returned to the
     * caller. If the value cannot be converted then the default value is stored
     * in the map. If the specified key does not exist in the map then the
     * default value is returned.
     * 
     * @return The long value associated with the key.
     */
	public static long getKeyedLong(final Map map, final String key, final long defValue) {
	    
	    if (map == null) return defValue;
	    
        long value = defValue;
        Object oValue = map.get(key);

        if (oValue != null && oValue instanceof String) {
            try {
                value = Long.parseLong((String) oValue);
            } catch (NumberFormatException ne) {
                value = defValue;
                LogUtils.infof(ParameterMap.class, ne, "getKeyedLong: Failed to convert value %s for key %s", oValue , key);
            }
            map.put(key, new Long(value));
        } else if (oValue != null) {
            value = ((Number) oValue).longValue();
        }
        return value;
	}
	
    /**
     * This method is used to lookup a specific key in the map. If the mapped
     * value is a string it is converted to an integer and the original string
     * value is replaced in the map. The converted value is returned to the
     * caller. If the value cannot be converted then the default value is stored
     * in the map. If the specified key does not exist in the map then the
     * default value is returned.
     * 
     * @return The int value associated with the key.
     */
    public static int getKeyedInteger(final Map map, final String key, final int defValue) {
    	return new Long(ParameterMap.getKeyedLong(map, key, new Long(defValue))).intValue();
    }

    /**
     * This method is used to lookup a specific key in the map. If the mapped
     * value is a string is is converted to an integer and the original string
     * value is replaced in the map. The converted value is returned to the
     * caller. If the value cannot be converted then the default value is used.
     * 
     * @return The array of integer values associated with the key.
     */
    public final static int[] getKeyedIntegerArray(final Map map, final String key, final int[] defValues) {
        
        if (map == null) return defValues;
        
        int[] result = defValues;
        Object oValue = map.get(key);

        if (oValue != null && oValue instanceof int[]) {
            result = (int[]) oValue;
        } else if (oValue != null) {
            List<Integer> tmpList = new ArrayList<Integer>(5);

            // Split on spaces, commas, colons, or semicolons
            //
            StringTokenizer ints = new StringTokenizer(oValue.toString(), " ;:,");
            while (ints.hasMoreElements()) {
                String token = ints.nextToken();
                try {
                    int x = Integer.parseInt(token);
                    tmpList.add(Integer.valueOf(x));
                } catch (NumberFormatException e) {
                    LogUtils.warnf(ParameterMap.class, e, "getKeyedIntegerArray: failed to convert value %s to int array for key %s due to value %s", oValue, key, token);
                }
            }
            result = new int[tmpList.size()];

            for (int x = 0; x < result.length; x++)
                result[x] = ((Integer) tmpList.get(x)).intValue();

            map.put(key, result);
        }
        return result;
    }

    /**
     * This method is used to lookup a specific key in the map. If the mapped
     * value is not a String it is converted to a String and the original value
     * is replaced in the map. The converted value is returned to the caller. If
     * the specified key does not exist in the map then the default value is
     * returned.
     * 
     * @return The String value associated with the key.
     */
    public static String getKeyedString(final Map map, final String key, final String defValue) {
        
        if (map == null) return defValue;

        String value = defValue;
        Object oValue = map.get(key);

        if (oValue != null && oValue instanceof String) {
            value = (String) oValue;
        } else if (oValue != null) {
            value = oValue.toString();
            map.put(key, value);
        }
        return value;
    }

    /**
     * This method is used to lookup a specific key in the map. If the mapped
     * value is a string it is converted to a boolean and the original string
     * value is replaced in the map. The converted value is returned to the
     * caller. If the value cannot be converted then the default value is stored
     * in the map. If the specified key does not exist in the map then the
     * default value is returned.
     *
     * @return The bool value associated with the key.
     */
    public static boolean getKeyedBoolean(final Map map, final String key, final boolean defValue) {
        
        if (map == null) return defValue;
        
        boolean value = defValue;
        Object oValue = map.get(key);

               if (oValue != null && oValue instanceof String) {
                       oValue = Boolean.valueOf((String) oValue);
               }

        if (oValue != null && oValue instanceof Boolean) {
            try {
                value = ((Boolean) oValue).booleanValue();
            } catch (NumberFormatException ne) {
                value = defValue;
                LogUtils.infof(ParameterMap.class, ne, "getKeyedBoolean: Failed to convert value %s for key %s", oValue, key);
            }
            map.put(key, Boolean.valueOf(value));
        } else if (oValue != null) {
            value = ((Boolean) oValue).booleanValue();
        }
        return value;
    }
}
