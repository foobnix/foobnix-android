/**
 * 
 */
package com.foobnix.util;

import java.util.Collection;

/**
 * @author iivanenko
 * 
 */
public class CollectionUtils {

    public static boolean isEmpty(Collection<?> list) {
        if (list == null) {
            return true;
        }
        return list.isEmpty();
    }

    public static boolean isNotEmpty(Collection<?> list) {
        return !isEmpty(list);
    }
}
